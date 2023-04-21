package gov.nasa.jpf.rtembed.memory;

import javax.realtime.LTMemory;
import javax.realtime.RealtimeThread;
import javax.realtime.ImmortalMemory;
import javax.realtime.HeapMemory;
import javax.realtime.MemoryArea;

public class AssignmentError5 {

    public static volatile boolean WITH_ERROR = true;

    public static class MyRealtimeThread implements Runnable {

        public class ScopeThread implements Runnable {

            public class InnerScopedThread implements Runnable {

                public Object fieldInParentScope;

                public class AssignerToParentScope implements Runnable {

                    public int toAssign;

                    public void run() {
                        System.err.println("Running in a assigner thread allocated in " + MemoryArea.getMemoryArea(this) + ", running in " + RealtimeThread.getCurrentMemoryArea());
                        fieldInParentScope = new Integer(toAssign);
                    }
                }

                public void run() {
                    System.err.println("Running in a inner scope thread allocated in " + MemoryArea.getMemoryArea(this) + ", running in " + RealtimeThread.getCurrentMemoryArea());
                    AssignerToParentScope assigner = new AssignerToParentScope();
                    assigner.toAssign = 211;
                    MemoryArea.getMemoryArea(this).executeInArea(assigner);
                    assigner.toAssign = 212;
                    ImmortalMemory.instance().executeInArea(assigner);
                    assigner.toAssign = 213;
                    HeapMemory.instance().executeInArea(assigner);
                    assigner.toAssign = 214;
                    if (WITH_ERROR) {
                        RealtimeThread.getCurrentMemoryArea().executeInArea(assigner);
                    }
                }
            }

            public void run() {
                System.err.println("Running in a scope thread allocated in " + MemoryArea.getMemoryArea(this) + ", running in " + RealtimeThread.getCurrentMemoryArea());
                LTMemory innerScope = new LTMemory(10000, 10000);
                innerScope.enter(new InnerScopedThread());
            }
        }

        public void run() {
            System.err.println("Running in a realtime thread allocated in " + MemoryArea.getMemoryArea(this) + ", running in " + RealtimeThread.getCurrentMemoryArea());
            LTMemory scope = new LTMemory(10000, 10000);
            scope.enter(new ScopeThread());
        }
    }

    public static void main(String[] args) {
        assert (args.length == 1);
        WITH_ERROR = args[0].equals("WITH_ERROR");
        RealtimeThread thread = new RealtimeThread(null, null, null, ImmortalMemory.instance(), null, new MyRealtimeThread());
        thread.start();
    }
}
