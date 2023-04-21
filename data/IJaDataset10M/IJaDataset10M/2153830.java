package org.emftext.language.pl0.resource.pl0.ui;

/**
 * A background parsing strategy that starts parsing after a amount of time after
 * the last key stroke. If keys are pressed within the delay interval, the delay
 * is reset. If keys are pressed during background parsing the parse thread is
 * stopped and a new parse task is scheduled.
 */
public class Pl0BackgroundParsingStrategy {

    private static long DELAY = 500;

    /**
	 * this timer is used to schedule a parsing task and execute it after a given delay
	 */
    private Object lock = new Object();

    /**
	 * the background parsing task (may be null)
	 */
    private org.eclipse.core.runtime.jobs.Job job;

    /**
	 * Schedules a task for background parsing that will be started after a delay.
	 */
    public void parse(org.eclipse.jface.text.DocumentEvent event, final org.emftext.language.pl0.resource.pl0.IPl0TextResource resource, final org.emftext.language.pl0.resource.pl0.ui.Pl0Editor editor) {
        if (resource == null) {
            return;
        }
        final String contents = event.getDocument().get();
        if (contents == null) {
            return;
        }
        synchronized (lock) {
            if (job != null) {
                job.cancel();
                try {
                    job.join();
                } catch (InterruptedException e) {
                }
            }
            job = new org.eclipse.core.runtime.jobs.Job("parsing document") {

                protected org.eclipse.core.runtime.IStatus run(org.eclipse.core.runtime.IProgressMonitor monitor) {
                    try {
                        resource.reload(new java.io.ByteArrayInputStream(contents.getBytes()), null);
                    } catch (java.io.IOException e) {
                        e.printStackTrace();
                    }
                    org.eclipse.core.runtime.jobs.Job finishJob = new org.eclipse.core.runtime.jobs.Job("refreshing views") {

                        protected org.eclipse.core.runtime.IStatus run(org.eclipse.core.runtime.IProgressMonitor monitor) {
                            editor.notifyBackgroundParsingFinished();
                            return org.eclipse.core.runtime.Status.OK_STATUS;
                        }
                    };
                    finishJob.schedule(10);
                    return org.eclipse.core.runtime.Status.OK_STATUS;
                }

                protected void canceling() {
                    resource.cancelReload();
                }
            };
            job.schedule(DELAY);
        }
    }
}
