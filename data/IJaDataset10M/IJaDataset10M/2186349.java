package co.edu.unal.ungrid.grid.master.remote;

import java.io.Serializable;

public class RemoteTask implements Serializable {

    public static final long serialVersionUID = 200609220000001L;

    public RemoteTask(int n) {
        this.n = n;
    }

    public Serializable compute() {
        return Integer.valueOf(n);
    }

    private final int n;
}
