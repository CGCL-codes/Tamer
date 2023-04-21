package cummingsm.rewrite.npcmanager;

public class OperationThreadManager implements Runnable {

    private static final long MAXWAITPEROPERATION = 10000;

    private final CircularlyLinkedList<RunnableOperation> list = new CircularlyLinkedList<RunnableOperation>();

    private OperationThreadManager() {
    }

    /**
	    * SingletonHolder is loaded on the first execution of Singleton.getInstance() 
	    * or the first access to SingletonHolder.INSTANCE, not before.
	    */
    private static class SingletonHolder {

        public static final OperationThreadManager INSTANCE = new OperationThreadManager();
    }

    public static OperationThreadManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private Object addLock = new Object();

    public void insertNext(RunnableOperation ro) {
        synchronized (addLock) {
            list.insertNext(ro);
        }
    }

    public void add(RunnableOperation ro) {
        synchronized (addLock) {
            list.add(ro);
        }
    }

    @Override
    public void run() {
        RunnableOperation rc = null;
        Operation operation = new Operation();
        while (true) {
            synchronized (addLock) {
                rc = list.next();
            }
            if (rc == null) {
                System.err.println("No Operations, Waiting!!!");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                continue;
            }
            operation.operation = rc;
            operation.run();
            if (operation.resultState == OperationState.RemoveYes) {
                synchronized (addLock) {
                    list.removeCurrent();
                }
            } else if (operation.resultState == OperationState.Exception) {
                synchronized (addLock) {
                    list.removeCurrent();
                }
                System.out.println("Removed Opeartion with Exception: " + operation.operation.getClass().getName());
            }
        }
    }

    private static enum OperationState {

        NotRun, Running, RemoveYes, RemoveNo, Exception
    }

    class Operation implements Runnable {

        public volatile RunnableOperation operation = null;

        public volatile OperationState resultState = OperationState.NotRun;

        @Override
        public void run() {
            boolean result = false;
            try {
                resultState = OperationState.Running;
                result = operation.performOperation();
            } catch (Exception e) {
                e.printStackTrace();
                resultState = OperationState.Exception;
                return;
            }
            if (result) {
                resultState = OperationState.RemoveYes;
            } else {
                resultState = OperationState.RemoveNo;
            }
        }
    }
}
