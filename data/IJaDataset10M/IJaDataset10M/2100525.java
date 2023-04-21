package org.sharemediarim.viewer.simple;

import java.util.concurrent.Semaphore;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.sharemedia.libraries.IMedia;
import org.sharemedia.services.impl.imagemanager.ImageService;

public class HighDefJob extends Job {

    private static Logger logger = Logger.getLogger(HighDefJob.class);

    IMedia media;

    int definition;

    FullScreen2DViewer view;

    static Semaphore sem = new Semaphore(2, true);

    public IMedia getMedia() {
        return media;
    }

    public HighDefJob(String name, FullScreen2DViewer view, IMedia m, int definition) {
        super(name);
        this.setSystem(true);
        this.setPriority(Job.LONG);
        this.media = m;
        this.view = view;
        this.definition = definition;
    }

    @Override
    protected IStatus run(IProgressMonitor monitor) {
        logger.debug("definition " + definition);
        try {
            logger.debug("sem.acquire();");
            synchronized (this) {
                sem.acquire();
            }
            logger.debug("acquired;");
        } catch (InterruptedException e) {
            logger.error(e, e);
        }
        if (monitor.isCanceled()) {
            synchronized (this) {
                sem.release();
            }
            finish();
            return Status.CANCEL_STATUS;
        }
        Image img;
        try {
            img = media.getImage(this.definition);
        } catch (Exception e) {
            logger.error("Error getting image for media " + media.getId());
            synchronized (this) {
                sem.release();
            }
            finish();
            return Status.CANCEL_STATUS;
        }
        logger.debug(img);
        if (img == null) {
            synchronized (this) {
                sem.release();
            }
            logger.warn("Cannot open " + media.getId());
            finish();
            return Status.OK_STATUS;
        }
        if (monitor.isCanceled()) {
            ImageService.getInstance().release(img);
            synchronized (this) {
                sem.release();
            }
            finish();
            return Status.CANCEL_STATUS;
        }
        if (view != null) {
            view.replaceImage(media, img, definition);
        }
        ImageService.getInstance().release(img);
        synchronized (this) {
            sem.release();
        }
        finish();
        return Status.OK_STATUS;
    }

    void finish() {
        Display.getDefault().asyncExec(new Runnable() {

            public void run() {
                view.setBusyIndicator(false, media);
            }
        });
    }
}
