package com.kni.etl.ketl.smp;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Comparator;
import org.w3c.dom.Node;
import com.kni.etl.ketl.exceptions.KETLThreadException;
import com.kni.etl.util.XMLHelper;

/**
 * @author nwakefield To change the template for this generated type comment go to Window&gt;Preferences&gt;Java&gt;Code
 *         Generation&gt;Code and Comments
 */
public class ETLThreadGroup {

    private ManagedBlockingQueue[] mQueue;

    private ETLWorker[] mETLWorkers;

    public static final int FANOUT = 0;

    public static final int PIPELINE = 2;

    public static final int PIPELINE_MERGE = 4;

    public static final int PIPELINE_SPLIT = 5;

    public static final int FANOUTIN = 1;

    public static final int FANIN = 3;

    private ETLThreadManager mETLThreadManager;

    private int mQueueSize;

    public static final int DEFAULTQUEUESIZE = 5;

    private String[] mPortList = null;

    public static ETLThreadGroup newInstance(ETLThreadGroup srcGrp, int iType, Step type, int partitions, ETLThreadManager pThreadManager) throws SecurityException, IllegalArgumentException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, KETLThreadException {
        return new ETLThreadGroup(srcGrp, iType, type, partitions, pThreadManager);
    }

    public static ETLThreadGroup newInstance(ETLThreadGroup srcLeftGrp, ETLThreadGroup srcRightGrp, int iType, Step type, int partitions, ETLThreadManager pThreadManager) throws SecurityException, IllegalArgumentException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, KETLThreadException {
        return new ETLThreadGroup(srcLeftGrp, srcRightGrp, iType, type, partitions, pThreadManager);
    }

    public static ETLThreadGroup[] newInstances(ETLThreadGroup srcGrp, String[] pPorts, int iType, Step type, int partitions, ETLThreadManager pThreadManager) throws SecurityException, IllegalArgumentException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, KETLThreadException {
        if (iType != PIPELINE_SPLIT || ETLSplit.class.isAssignableFrom(type.getNodeClass()) == false) throw new KETLThreadException("Invalid type supplied", Thread.currentThread());
        ETLThreadGroup[] grp = new ETLThreadGroup[pPorts.length];
        for (int i = 0; i < pPorts.length; i++) {
            grp[i] = new ETLThreadGroup();
            grp[i].mPortList = pPorts;
            grp[i].mQueueSize = XMLHelper.getAttributeAsInt(type.getConfig().getAttributes(), "QUEUESIZE", DEFAULTQUEUESIZE);
            grp[i].mETLThreadManager = pThreadManager;
            grp[i].mQueue = new ManagedBlockingQueue[srcGrp.mQueue.length];
            for (int q = 0; q < grp[i].mQueue.length; q++) grp[i].mQueue[q] = grp[i].getManagedQueue();
        }
        ETLWorker[] workers = new ETLWorker[srcGrp.mQueue.length];
        for (int i = 0; i < workers.length; i++) {
            Constructor cons = type.getNodeClass().getConstructor(new Class[] { Node.class, int.class, int.class, ETLThreadManager.class });
            workers[i] = (ETLWorker) cons.newInstance(new Object[] { (Node) type.getConfig(), i, workers.length, pThreadManager });
            if (workers[i] instanceof ETLSplit) {
                ((ETLSplit) workers[i]).queue = new ManagedBlockingQueue[pPorts.length];
                for (int p = 0; p < pPorts.length; p++) {
                    ((ETLSplit) workers[i]).queue[p] = grp[p].mQueue[i];
                    grp[p].mETLWorkers = workers;
                }
                ((ETLSplit) workers[i]).setSourceQueue(srcGrp.mQueue[i], srcGrp.mETLWorkers[i]);
                srcGrp.mETLWorkers[i].postSourceConnectedInitialize();
            }
        }
        return grp;
    }

    public ETLThreadGroup(ETLThreadGroup srcGrp, int iType, Step type, int partitions, ETLThreadManager pThreadManager) throws InstantiationException, IllegalAccessException, SecurityException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException, KETLThreadException {
        super();
        this.mQueueSize = XMLHelper.getAttributeAsInt(type.getConfig().getAttributes(), "QUEUESIZE", DEFAULTQUEUESIZE);
        this.mETLThreadManager = pThreadManager;
        if (iType == FANOUT) {
            if (srcGrp != null && srcGrp.mETLWorkers.length > 1) {
                if (srcGrp.mETLWorkers.length != partitions) {
                    com.kni.etl.dbutils.ResourcePool.LogMessage(Thread.currentThread(), com.kni.etl.dbutils.ResourcePool.INFO_MESSAGE, "Source thread group is already partitioned, matching source partition count and switching to pipelined parrallism");
                    partitions = srcGrp.mETLWorkers.length;
                } else {
                    com.kni.etl.dbutils.ResourcePool.LogMessage(Thread.currentThread(), com.kni.etl.dbutils.ResourcePool.INFO_MESSAGE, "Source thread group is already partitioned, switching to pipelined parrallism");
                }
                iType = PIPELINE;
            }
        }
        switch(iType) {
            case PIPELINE:
                if (srcGrp == null) {
                    mETLWorkers = new ETLWorker[partitions];
                    this.mQueue = new ManagedBlockingQueue[partitions];
                } else {
                    mETLWorkers = new ETLWorker[srcGrp.mQueue.length];
                    this.mQueue = new ManagedBlockingQueue[srcGrp.mQueue.length];
                }
                for (int i = 0; i < mETLWorkers.length; i++) {
                    Constructor cons = type.getNodeClass().getConstructor(new Class[] { Node.class, int.class, int.class, ETLThreadManager.class });
                    mETLWorkers[i] = (ETLWorker) cons.newInstance(new Object[] { type.getConfig(), i, mETLWorkers.length, pThreadManager });
                    this.mQueue[i] = this.getManagedQueue();
                    if (mETLWorkers[i] instanceof ETLWriter) {
                        ((ETLWriter) mETLWorkers[i]).setSourceQueue(srcGrp.mQueue[i], srcGrp.mETLWorkers[i]);
                        srcGrp.mETLWorkers[i].postSourceConnectedInitialize();
                    }
                    if (mETLWorkers[i] instanceof ETLReader) {
                        ((ETLReader) mETLWorkers[i]).queue = this.mQueue[i];
                    }
                    if (mETLWorkers[i] instanceof ETLTransform) {
                        ((ETLTransform) mETLWorkers[i]).queue = this.mQueue[i];
                        ((ETLTransform) mETLWorkers[i]).setSourceQueue(srcGrp.mQueue[i], srcGrp.mETLWorkers[i]);
                        srcGrp.mETLWorkers[i].postSourceConnectedInitialize();
                    }
                }
                break;
            case FANIN:
                mETLWorkers = new ETLWorker[1];
                this.mQueue = new ManagedBlockingQueue[1];
                this.mQueue[0] = this.getManagedQueue();
                ManagedBlockingQueue q = srcGrp.mQueue[0];
                for (int i = 0; i < srcGrp.mETLWorkers.length; i++) {
                    if (srcGrp.mETLWorkers[i] instanceof ETLReader) {
                        ((ETLReader) srcGrp.mETLWorkers[i]).queue = q;
                    }
                    if (srcGrp.mETLWorkers[i] instanceof ETLTransform) {
                        ((ETLTransform) srcGrp.mETLWorkers[i]).queue = q;
                    }
                    srcGrp.mQueue = new ManagedBlockingQueue[1];
                    srcGrp.mQueue[0] = q;
                }
                {
                    Constructor cons = type.getNodeClass().getConstructor(new Class[] { Node.class, int.class, int.class, ETLThreadManager.class });
                    mETLWorkers[0] = (ETLWorker) cons.newInstance(new Object[] { (Node) type.getConfig(), 0, srcGrp.mQueue.length, pThreadManager });
                    for (int i = 0; i < srcGrp.mETLWorkers.length; i++) {
                        if (mETLWorkers[0] instanceof ETLWriter) {
                            ((ETLWriter) mETLWorkers[0]).setSourceQueue(q, srcGrp.mETLWorkers[i]);
                        }
                        if (mETLWorkers[0] instanceof ETLTransform) {
                            this.mQueue[0] = this.getManagedQueue();
                            ((ETLTransform) mETLWorkers[0]).queue = this.mQueue[0];
                            ((ETLTransform) mETLWorkers[0]).setSourceQueue(q, srcGrp.mETLWorkers[i]);
                        }
                        srcGrp.mETLWorkers[i].postSourceConnectedInitialize();
                    }
                }
                break;
            case FANOUT:
                mETLWorkers = new ETLWorker[partitions];
                this.mQueue = new ManagedBlockingQueue[partitions];
                Partitioner partitioningQueue = getPartitioner(type.getConfig(), partitions);
                for (int partition = 0; partition < partitions; partition++) {
                    Constructor cons = type.getNodeClass().getConstructor(new Class[] { Node.class, int.class, int.class, ETLThreadManager.class });
                    mETLWorkers[partition] = (ETLWorker) cons.newInstance(new Object[] { (Node) type.getConfig(), partition, partitions, pThreadManager });
                    if (mETLWorkers[partition] instanceof ETLReader) {
                        this.mQueue[partition] = this.getManagedQueue();
                        ((ETLReader) mETLWorkers[partition]).queue = this.mQueue[partition];
                    } else {
                        ETLWorker srcWorker = srcGrp.mETLWorkers[0];
                        ManagedBlockingQueue srcQueue = srcGrp.mQueue[0];
                        if (partitioningQueue != null) {
                            srcWorker.switchTargetQueue(srcGrp.mQueue[0], partitioningQueue);
                            srcGrp.mQueue[0] = partitioningQueue;
                            srcQueue = partitioningQueue.getTargetSourceQueue(partition);
                        }
                        if (mETLWorkers[partition] instanceof ETLTransform) {
                            this.mQueue[partition] = this.getManagedQueue();
                            ((ETLTransform) mETLWorkers[partition]).setSourceQueue(srcQueue, srcWorker);
                            ((ETLTransform) mETLWorkers[partition]).queue = this.mQueue[partition];
                        }
                        if (mETLWorkers[partition] instanceof ETLWriter) {
                            ((ETLWriter) mETLWorkers[partition]).setSourceQueue(srcQueue, srcWorker);
                            this.mQueue = null;
                        }
                    }
                }
                if (srcGrp != null) srcGrp.mETLWorkers[0].postSourceConnectedInitialize();
                break;
        }
    }

    private Partitioner getPartitioner(Node xmlNode, int targetPartitions) throws KETLThreadException {
        Node[] partitionKeys = XMLHelper.getElementsByName(xmlNode, "IN", "PARTITIONKEY", null);
        Node[] sortKeys = XMLHelper.getElementsByName(xmlNode, "IN", "BUFFERSORT", null);
        Comparator comp = null;
        if (sortKeys != null && sortKeys.length > 0) {
            Integer[] elements = new Integer[sortKeys.length];
            Boolean[] elementOrder = new Boolean[sortKeys.length];
            for (int i = 0; i < sortKeys.length; i++) {
                elements[i] = XMLHelper.getAttributeAsInt(sortKeys[i].getAttributes(), "BUFFERSORT", 0);
                elementOrder[i] = XMLHelper.getAttributeAsBoolean(sortKeys[i].getAttributes(), "BUFFERSORTORDER", true);
            }
            comp = new DefaultComparator(elements, elementOrder);
            if (partitionKeys == null || partitionKeys.length == 0) return null;
        }
        if (partitionKeys == null || partitionKeys.length == 0) return null;
        int[] indexCheck = new int[partitionKeys.length];
        java.util.Arrays.fill(indexCheck, -1);
        for (int x = 0; x < partitionKeys.length; x++) {
            int id = XMLHelper.getAttributeAsInt(partitionKeys[x].getAttributes(), "PARTITIONKEY", -1);
            if (id <= indexCheck.length && id > 0) {
                indexCheck[id - 1] = 0;
            } else throw new KETLThreadException("Invalid PARTITIONKEY value", this);
        }
        for (int i = 0; i < indexCheck.length; i++) {
            if (indexCheck[i] == -1) throw new KETLThreadException("Invalid PARTITIONKEY settings, key sequence order is wrong", this);
        }
        return new Partitioner(partitionKeys, comp, targetPartitions, this.mQueueSize);
    }

    private ETLThreadGroup() {
        super();
    }

    public ETLThreadGroup(ETLThreadGroup srcGrp, int pPaths, int iType, Step type, int partitions, ETLThreadManager pThreadManager) throws InstantiationException, IllegalAccessException, SecurityException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException, KETLThreadException {
        super();
        this.mQueueSize = XMLHelper.getAttributeAsInt(type.getConfig().getAttributes(), "QUEUESIZE", DEFAULTQUEUESIZE);
        this.mETLThreadManager = pThreadManager;
        switch(iType) {
            case PIPELINE_SPLIT:
                if (srcGrp == null) {
                    mETLWorkers = new ETLWorker[partitions];
                    this.mQueue = new ManagedBlockingQueue[partitions];
                } else {
                    mETLWorkers = new ETLWorker[srcGrp.mQueue.length];
                    this.mQueue = new ManagedBlockingQueue[srcGrp.mQueue.length];
                }
                for (int i = 0; i < mETLWorkers.length; i++) {
                    Constructor cons = type.getNodeClass().getConstructor(new Class[] { Node.class, int.class, int.class, ETLThreadManager.class });
                    mETLWorkers[i] = (ETLWorker) cons.newInstance(new Object[] { (Node) type.getConfig(), i, mETLWorkers.length, pThreadManager });
                    this.mQueue[i] = this.getManagedQueue();
                    if (mETLWorkers[i] instanceof ETLSplit) {
                        ((ETLSplit) mETLWorkers[i]).queue[pPaths] = this.mQueue[i];
                        ((ETLSplit) mETLWorkers[i]).setSourceQueue(srcGrp.mQueue[i], srcGrp.mETLWorkers[i]);
                    }
                    srcGrp.mETLWorkers[i].postSourceConnectedInitialize();
                }
                break;
        }
    }

    public ETLThreadGroup(ETLThreadGroup srcLeftGrp, ETLThreadGroup srcRightGrp, int iType, Step type, int partitions, ETLThreadManager pThreadManager) throws InstantiationException, IllegalAccessException, SecurityException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException, KETLThreadException {
        super();
        this.mQueueSize = XMLHelper.getAttributeAsInt(type.getConfig().getAttributes(), "QUEUESIZE", DEFAULTQUEUESIZE);
        this.mETLThreadManager = pThreadManager;
        switch(iType) {
            case PIPELINE_MERGE:
                if (srcLeftGrp.mQueue.length != srcRightGrp.mQueue.length) throw new KETLThreadException("Left and right sources must have the same parallism", this);
                mETLWorkers = new ETLMerge[srcLeftGrp.mQueue.length];
                this.mQueue = new ManagedBlockingQueue[srcLeftGrp.mQueue.length];
                for (int i = 0; i < mETLWorkers.length; i++) {
                    Constructor cons = type.getNodeClass().getConstructor(new Class[] { Node.class, int.class, int.class, ETLThreadManager.class });
                    mETLWorkers[i] = (ETLWorker) cons.newInstance(new Object[] { (Node) type.getConfig(), i, mETLWorkers.length, pThreadManager });
                    this.mQueue[i] = this.getManagedQueue();
                    if (mETLWorkers[i] instanceof ETLMerge) {
                        ((ETLMerge) mETLWorkers[i]).queue = this.mQueue[i];
                        ((ETLMerge) mETLWorkers[i]).setSourceQueueLeft(srcLeftGrp.mQueue[i], srcLeftGrp.mETLWorkers[i]);
                        ((ETLMerge) mETLWorkers[i]).setSourceQueueRight(srcRightGrp.mQueue[i], srcRightGrp.mETLWorkers[i]);
                    }
                    srcLeftGrp.mETLWorkers[i].postSourceConnectedInitialize();
                    srcRightGrp.mETLWorkers[i].postSourceConnectedInitialize();
                }
                break;
        }
    }

    public final ManagedBlockingQueue getManagedQueue() throws KETLThreadException {
        return this.mETLThreadManager.requestQueue(this.mQueueSize);
    }

    public String getPortName(int i) {
        if (this.mPortList == null) return "DEFAULT";
        return this.mPortList[i];
    }

    public void setQueueName(String port) {
        for (int i = 0; i < this.mQueue.length; i++) this.mQueue[i].setName(port);
    }
}
