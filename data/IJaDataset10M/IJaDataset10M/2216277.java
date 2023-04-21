package org.jcyclone.core.rtc;

import org.jcyclone.core.profiler.IProfilable;
import org.jcyclone.core.queue.*;
import java.util.List;

/**
 * @author Jean Morissette
 */
public class AdmissionControlledSink implements IAdmissionControlledSink, IProfilable {

    volatile IEnqueuePredicate pred;

    IBlockingSink sink;

    public AdmissionControlledSink(IBlockingSink sink) {
        this.sink = sink;
    }

    public synchronized void setEnqueuePredicate(IEnqueuePredicate pred) {
        this.pred = pred;
    }

    public IEnqueuePredicate getEnqueuePredicate() {
        return pred;
    }

    public void blockingEnqueue(IElement element) throws InterruptedException {
        if (pred != null) pred.blockingAccept(element);
        sink.blockingEnqueue(element);
    }

    public void enqueue(IElement element) throws SinkException {
        if (pred != null && !pred.accept(element)) throw new SinkFullException();
        sink.enqueue(element);
    }

    public boolean enqueueLossy(IElement element) {
        if (pred != null && !pred.accept(element)) return false;
        return sink.enqueueLossy(element);
    }

    public void enqueueMany(List list) throws SinkException {
        if (pred != null && !pred.acceptMany(list)) throw new SinkFullException();
        sink.enqueueMany(list);
    }

    public ITransaction enqueuePrepare(List elements) throws SinkException {
        if (pred != null && !pred.acceptMany(elements)) throw new SinkFullException();
        return sink.enqueuePrepare(elements);
    }

    public void enqueuePrepare(List elements, ITransaction txn) throws SinkException {
        if (pred != null && !pred.acceptMany(elements)) throw new SinkFullException();
        sink.enqueuePrepare(elements, txn);
    }

    public int size() {
        return sink.size();
    }

    public void setCapacity(int newCapacity) {
        sink.setCapacity(newCapacity);
    }

    public int capacity() {
        return sink.capacity();
    }

    public boolean enqueueLossy(IElement element, int timeout_millis) throws InterruptedException {
        if (pred != null && !pred.accept(element)) return false;
        return sink.enqueueLossy(element, timeout_millis);
    }

    public int profileSize() {
        return sink.size();
    }
}
