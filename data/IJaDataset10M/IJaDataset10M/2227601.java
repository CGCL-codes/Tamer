package pl.vdl.azbest.mremote.probe.signal;

import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/** How to make a queue - study */
public class TestQueue {

    public static void main(String[] args) {
        LinkedList<String> ll = new LinkedList<String>();
        ll.add("1st one");
        ll.add("2nd one");
    }

    public void startTest() {
        CmdAdder ca = new CmdAdder();
        CmdRemover cr = new CmdRemover();
        ca.addCommands();
        cr.doCommands();
    }

    private class CmdAdder {

        private int n = 3;

        public void addCommands() {
            final Timer timer = new Timer();
            class MyTimerTask extends TimerTask {

                public void run() {
                    Command c = new Cmd_command(n);
                    Invoker2.getInstance().addCommand(c);
                    n--;
                    if (n > 0) {
                        timer.schedule(new MyTimerTask(), 1000);
                    } else {
                        timer.cancel();
                    }
                }
            }
        }
    }

    private class CmdRemover {

        private void doCommands() {
            class Remover implements Runnable {

                public void run() {
                    Invoker2.getInstance().invoke();
                    Invoker2.getInstance().invoke();
                    Invoker2.getInstance().invoke();
                }
            }
            ;
            Thread t = new Thread(new Remover());
            t.start();
        }
    }
}

class Invoker2 {

    private static Invoker2 instance = new Invoker2();

    public static Invoker2 getInstance() {
        return instance;
    }

    private final int QUEUE_SIZE = 5;

    private BlockingQueue<Command> bq = new ArrayBlockingQueue<Command>(QUEUE_SIZE);

    public void addCommand(Command c) {
        bq.add(c);
    }

    public void invoke() {
        Command c;
        if ((c = bq.poll()) != null) c.invoke(); else System.out.println("Queue is eempty.");
    }
}

interface Command {

    public void invoke();
}

class Cmd_command implements Command {

    private int n;

    public Cmd_command(int n) {
        this.n = n;
    }

    public void invoke() {
        try {
            Thread.sleep(3 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
