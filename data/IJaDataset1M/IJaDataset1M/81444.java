package practica4;

import practica2.CuaCircular;

public class SocketBuffer implements Buffer {

    private CuaCircular c;

    public SocketBuffer(int len) {
        c = new CuaCircular(len);
    }

    public synchronized void put(Object o) {
        while (c.isFull()) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        c.put(o);
        notifyAll();
    }

    public synchronized Object get() {
        while (c.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Object o = c.get();
        notifyAll();
        return o;
    }
}
