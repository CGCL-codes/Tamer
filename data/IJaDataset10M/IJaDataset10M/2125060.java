package org.yactu.tools.stack;

/**
 * @author ploix
 *
 */
public class StackSamples {

    /**
     *
     */
    public StackSamples() {
        super();
    }

    public void fifoLifo() {
        SaferStack lifo = new SaferStack(new NormalStack());
        SaferStack fifo = new SaferStack(new NormalStack(true));
        lifo.push(new Integer(1));
        lifo.push(new Integer(2));
        System.err.println("last in lifo = " + lifo.pop());
        fifo.push(new Integer(1));
        fifo.push(new Integer(2));
        System.err.println("last in fifo = " + fifo.pop());
    }

    public void listenEvents() {
        final SaferStack lifo = new SaferStack(new NormalStack());
        lifo.addStackEventListener(new StackEventHandler() {

            public void stackWaitPushed(Stack _stack, Object _pushed) {
                System.err.println("received a waiting push event with object " + _pushed);
            }
        });
        lifo.waitingPush(new Integer(1));
    }

    public void waitPushPop() {
        final SaferStack lifo = new SaferStack(new NormalStack());
        new Thread() {

            public synchronized void run() {
                try {
                    wait(1000);
                } catch (InterruptedException e) {
                }
                lifo.waitingPush(new Integer(1));
            }
        }.start();
        System.err.println("after waiting a bit, I get : " + lifo.waitingPop());
    }

    public synchronized void waitTimeoutPush() {
        final SaferStack fifo = new SaferStack(new NormalStack());
        fifo.waitingPushTimeout(new Integer(1), 500);
        fifo.waitingPushTimeout(new Integer(2), 1500);
        fifo.waitingPushTimeout(new Integer(3), 500);
        try {
            wait(1000);
        } catch (InterruptedException e) {
        }
        System.err.println("after waiting a bit because of timeout, I get : " + fifo.waitingPop());
    }

    public static void main(String[] args) {
        StackSamples samples = new StackSamples();
        samples.fifoLifo();
        samples.waitPushPop();
        samples.waitTimeoutPush();
        samples.listenEvents();
    }
}
