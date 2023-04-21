package org.nakedobjects.viewer.skylark.basic;

import org.nakedobjects.object.NakedObjectApplicationException;
import org.nakedobjects.viewer.skylark.View;
import org.apache.log4j.Logger;

public final class BackgroundThread {

    private static final Logger LOG = Logger.getLogger(BackgroundTask.class);

    private BackgroundThread() {
    }

    public static void run(final View view, final BackgroundTask task) {
        Thread t = new Thread("nofBackground") {

            public void run() {
                view.getState().setActive();
                view.getViewManager().setBusy(view);
                repaint(view);
                try {
                    task.execute();
                } catch (Throwable e) {
                    if (!(e instanceof NakedObjectApplicationException)) {
                        String message = "Error while running background task " + task.getName();
                        LOG.error(message, e);
                    }
                    view.getViewManager().showException(e);
                } finally {
                    view.getState().setInactive();
                    view.getViewManager().clearBusy(view);
                    repaint(view);
                }
            }
        };
        t.start();
    }

    private static void repaint(final View view) {
        view.markDamaged();
        view.getViewManager().repaint();
    }
}
