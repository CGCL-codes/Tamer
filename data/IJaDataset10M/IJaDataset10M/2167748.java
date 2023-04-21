package annone.ui;

import annone.engine.ChannelCallback;

class OpenChannelTask extends TaskRunnable implements ChannelCallback {

    @Override
    public String getName() {
        return getClass().getName();
    }

    @Override
    public void run() {
        Task task = getTask();
    }
}
