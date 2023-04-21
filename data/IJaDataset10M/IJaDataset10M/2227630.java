package de.fhg.igd.semoa.compat.jade;

public class SemoaResourceManager implements jade.core.ResourceManager {

    ThreadGroup tg_;

    public SemoaResourceManager(ThreadGroup tg) {
        tg_ = tg;
    }

    public Thread getThread(int type, String name, Runnable target) {
        return new Thread(tg_, target, name);
    }

    public void releaseResources() {
    }
}
