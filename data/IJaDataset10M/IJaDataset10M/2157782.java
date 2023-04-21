package jmud;

class Monitor {

    synchronized void block() {
        try {
            wait();
        } catch (InterruptedException e) {
        }
    }

    synchronized void release() {
        notifyAll();
    }
}
