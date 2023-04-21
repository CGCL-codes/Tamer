package org.semantic.reasoning.screech.asyn.util;

public abstract class WriterThread extends Thread {

    protected String threadID;

    protected String query;

    protected ResultPool pool;

    public WriterThread(String threadID, ResultPool pool) {
        this.threadID = threadID;
        this.pool = pool;
    }

    public void run() {
        if (query == null) {
            System.err.println("Error! The setQuery-Method should be invoked before the run-Method!");
        } else {
            Object result = answerquery(query);
            pool.writeData(threadID, result);
        }
    }

    protected abstract Object answerquery(String query);

    public void setQuery(String query) {
        this.query = query;
    }
}
