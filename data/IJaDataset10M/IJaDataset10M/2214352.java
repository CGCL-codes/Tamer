package com.sun.sgs.impl.service.data.gc;

import com.sun.sgs.app.*;
import com.sun.sgs.auth.Identity;
import com.sun.sgs.impl.service.data.DataServiceImpl;
import com.sun.sgs.impl.util.AbstractKernelRunnable;
import com.sun.sgs.kernel.TransactionScheduler;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Esko Luontola
 * @since 1.1.2009
 */
public class GarbageCollectorImpl implements GarbageCollector {

    private final ConcurrentMap<BigInteger, Color> nodeColors = new ConcurrentSkipListMap<BigInteger, Color>();

    private final Queue<BigInteger> grayNodes = new ConcurrentLinkedQueue<BigInteger>();

    private final ReentrantLock gcInProgress = new ReentrantLock();

    private final TransactionScheduler txnScheduler;

    private final DataServiceImpl dataService;

    private final Identity owner;

    public GarbageCollectorImpl(TransactionScheduler txnScheduler, DataServiceImpl dataService, Identity owner) {
        this.txnScheduler = txnScheduler;
        this.dataService = dataService;
        this.owner = owner;
    }

    public void runGarbageCollector() throws Exception {
        gcInProgress.lock();
        try {
            nodeColors.clear();
            markAllRootsGray();
            scanAllReachableNodesBlack();
            removeAllWhiteGarbageNodes();
        } finally {
            nodeColors.clear();
            gcInProgress.unlock();
        }
    }

    public void fireObjectModified(BigInteger source, Set<BigInteger> targets) {
        if (gcInProgress.isLocked()) {
            scanToBlack(source, targets);
        }
    }

    private void markAllRootsGray() throws Exception {
        txnScheduler.runTask(new MarkAllRootsGray(), owner);
    }

    private void scanAllReachableNodesBlack() throws Exception {
        BigInteger grayNode;
        while ((grayNode = grayNodes.poll()) != null) {
            txnScheduler.runTask(new ScanReachableNodeBlack(grayNode), owner);
        }
    }

    private void removeAllWhiteGarbageNodes() throws Exception {
        RemoveWhiteGarbageNodes task = new RemoveWhiteGarbageNodes();
        while (!task.isDone()) {
            txnScheduler.runTask(task, owner);
        }
    }

    private void shadeToGray(BigInteger id) {
        Color current = getColor(id);
        if (current.equals(Color.WHITE)) {
            setColor(id, Color.GRAY);
        }
    }

    private void scanToBlack(BigInteger source, Set<BigInteger> targets) {
        for (BigInteger target : targets) {
            shadeToGray(target);
        }
        setColor(source, Color.BLACK);
    }

    private void setColor(BigInteger id, Color color) {
        nodeColors.put(id, color);
        if (color.equals(Color.GRAY)) {
            grayNodes.add(id);
        }
    }

    private Color getColor(BigInteger id) {
        Color color = nodeColors.get(id);
        if (color == null) {
            color = Color.WHITE;
        }
        return color;
    }

    private enum Color {

        WHITE, GRAY, BLACK
    }

    private class MarkAllRootsGray extends AbstractKernelRunnable {

        public MarkAllRootsGray() {
            super("MarkAllRootsGray");
        }

        public void run() throws Exception {
            for (String binding = dataService.nextBoundName(null); binding != null; binding = dataService.nextBoundName(binding)) {
                ManagedObject obj = dataService.getBinding(binding);
                shadeToGray(getId(obj));
            }
            for (String binding = dataService.nextServiceBoundName(null); binding != null; binding = dataService.nextServiceBoundName(binding)) {
                ManagedObject obj = dataService.getServiceBinding(binding);
                shadeToGray(getId(obj));
            }
        }

        private BigInteger getId(ManagedObject obj) {
            ManagedReference<?> ref = dataService.createReference(obj);
            return ref.getId();
        }
    }

    private class ScanReachableNodeBlack extends AbstractKernelRunnable {

        private final BigInteger source;

        public ScanReachableNodeBlack(BigInteger source) {
            super("ScanReachableNodeBlack");
            this.source = source;
        }

        public void run() throws Exception {
            Set<BigInteger> targets = dataService.getReferencedObjectIds(source);
            scanToBlack(source, targets);
        }
    }

    private class RemoveWhiteGarbageNodes extends AbstractKernelRunnable {

        private BigInteger previous = null;

        private boolean done = false;

        public RemoveWhiteGarbageNodes() {
            super("RemoveWhiteGarbageNodes");
        }

        public void run() throws Exception {
            BigInteger current = dataService.nextObjectId(previous);
            if (current == null) {
                done = true;
            } else {
                if (getColor(current).equals(Color.WHITE)) {
                    remove(current);
                }
                previous = current;
            }
        }

        private void remove(BigInteger id) {
            ManagedReference<?> ref = dataService.createReferenceForId(id);
            dataService.removeObject(ref.getForUpdate());
        }

        public boolean isDone() {
            return done;
        }
    }
}
