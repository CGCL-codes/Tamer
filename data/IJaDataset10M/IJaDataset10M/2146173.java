package mpi;

import mpjbuf.*;

public class Min extends Op {

    Min() {
        worker = new MinWorker();
    }
}
