package bagaturchess.search.impl.rootsearch.sequential;

import java.util.List;

class SearchThread extends Thread {

    private Object sync;

    private List<Runnable> tasks;

    SearchThread(Object _sync, List<Runnable> _tasks) {
        sync = _sync;
        tasks = _tasks;
    }

    @Override
    public void run() {
        while (true) {
            Runnable r = null;
            synchronized (sync) {
                if (tasks.size() <= 0) {
                    try {
                        sync.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (tasks.size() > 0) {
                    r = tasks.remove(0);
                }
            }
            r.run();
        }
    }
}
