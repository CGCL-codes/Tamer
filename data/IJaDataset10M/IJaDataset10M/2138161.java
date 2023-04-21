package com.tomczarniecki.s3.gui;

class DirectWorker implements Worker {

    public void executeInBackground(Runnable command) {
        command.run();
    }

    public void executeOnEventLoop(Runnable command) {
        command.run();
    }
}
