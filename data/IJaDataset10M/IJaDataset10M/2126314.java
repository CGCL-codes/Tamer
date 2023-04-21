package com.geoffholden.xfngraph.spider;

class SpiderProcessThread extends Thread {

    private final Spider parent;

    private final SpiderProgressListener progress;

    boolean running = false;

    public void run() {
        Site site;
        while (true) {
            site = null;
            synchronized (parent.processQueue) {
                try {
                    parent.processQueue.wait(100);
                } catch (InterruptedException e) {
                }
                if (parent.processQueue.isEmpty()) {
                    boolean threadsRunning = false;
                    for (int i = 0; i < parent.threads.length; i++) {
                        synchronized (parent.threads[i]) {
                            threadsRunning = threadsRunning || parent.threads[i].running;
                        }
                        if (!threadsRunning) {
                            running = false;
                            synchronized (parent) {
                                parent.notifyAll();
                            }
                            return;
                        }
                    }
                } else {
                    running = true;
                    site = (Site) parent.processQueue.remove(0);
                }
            }
            if (site != null) {
                parent.spider(site, progress);
                running = false;
                synchronized (parent.processQueue) {
                    parent.processQueue.notifyAll();
                }
            }
        }
    }

    public SpiderProcessThread(Spider parent, SpiderProgressListener progress) {
        super();
        this.parent = parent;
        this.progress = progress;
    }
}
