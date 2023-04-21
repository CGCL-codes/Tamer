package de.mxro.examples.threads;

import java.util.Vector;

public class C extends Thread {

    private Vector v;

    public void setP(P p) {
    }

    public C(Vector v) {
        this.v = v;
    }

    @Override
    public void run() {
        int j = 20;
        while (j != 0) {
            synchronized (this.v) {
                if (this.v.size() < 1) {
                    try {
                        this.v.wait();
                    } catch (final InterruptedException e) {
                    }
                } else {
                    System.out.print(" Konsument fand " + (String) this.v.elementAt(0));
                    this.v.removeElementAt(0);
                    j--;
                    this.v.notify();
                    System.out.println(" (verbleiben: " + this.v.size() + ")");
                }
            }
        }
    }
}
