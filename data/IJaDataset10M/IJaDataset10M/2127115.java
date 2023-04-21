package net.infian.framework.db.pooling;

final class AsynchronousTransactionThread implements Runnable {

    private final ConnectionPool pool;

    AsynchronousTransactionThread(final ConnectionPool pool) {
        this.pool = pool;
    }

    public final void run() {
        while (true) {
            pool.executeAsynchronousUpdate();
        }
    }
}
