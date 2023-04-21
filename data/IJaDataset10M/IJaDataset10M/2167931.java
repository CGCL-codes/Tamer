package com.dukesoftware.utils.thread.pattern.workerthread;

public interface Command {

    void execute() throws InterruptedException;
}
