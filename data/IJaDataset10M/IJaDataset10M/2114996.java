package de.beas.explicanto.client.rcp.projects.views;

import java.lang.reflect.InvocationTargetException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Display;
import de.bea.services.vidya.client.datasource.VidyaDataTree;
import de.bea.services.vidya.client.datasource.types.WSResponse;
import de.bea.services.vidya.client.datastructures.CCourse;
import de.bea.services.vidya.client.exceptions.ExplicantoException;
import de.beas.explicanto.XPCConst;
import de.beas.explicanto.client.ExplicantoClientPlugin;
import de.beas.explicanto.client.I18N;
import de.beas.explicanto.client.rcp.model.Model;

public class CoursePreviewer implements IRunnableWithProgress {

    private long projectId;

    private long cnId;

    private long courseId;

    private String format;

    private boolean zip;

    private boolean climp;

    private CCourse course;

    private WSResponse resp = null;

    /**
     * @param climp
     * @param id
     * @param id2
     * @param format
     * @param id3
     * @param zip
     */
    public CoursePreviewer(long projId, long cnId, long courseId, String format, boolean zip, boolean climp, CCourse course) {
        this.climp = climp;
        this.cnId = cnId;
        this.courseId = courseId;
        this.format = format;
        this.projectId = projId;
        this.zip = zip;
        this.course = course;
    }

    public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
        monitor.beginTask(I18N.translate("actions.messages.genPreview"), 1000);
        ProgressChanger pg = new ProgressChanger(monitor, Display.getDefault(), 1000);
        pg.start();
        try {
            resp = VidyaDataTree.getDefault().coursePreview(projectId, cnId, courseId, format, zip, climp);
        } catch (final ExplicantoException e) {
            if ((e.getResponse().getErrorCode() == XPCConst.E_NO_LESSON_IN_COURSE || e.getResponse().getErrorCode() == XPCConst.E_NO_UNIT_IN_COURSE) && course.countUnits() != 0) Model.getInstance().loadLessons(course);
            Display.getDefault().syncExec(new Runnable() {

                public void run() {
                    ExplicantoClientPlugin.handleException(e, course);
                }
            });
        } catch (final Exception e) {
            Display.getDefault().syncExec(new Runnable() {

                public void run() {
                    ExplicantoClientPlugin.handleException(e, course);
                }
            });
        }
        pg.stopIt();
        pg.join();
        monitor.done();
    }

    public WSResponse getResponse() {
        return resp;
    }
}

class ProgressChanger extends Thread {

    private Display display;

    private IProgressMonitor monitor;

    private int total;

    private boolean stop = false;

    private int worked = 0;

    /**
     * @param arg0
     * @param monitor
     * @param display
     * @param total
     */
    public ProgressChanger(IProgressMonitor monitor, Display display, int total) {
        this.monitor = monitor;
        this.display = display;
        this.total = total;
    }

    public void run() {
        while (!stop) {
            display.syncExec(new Runnable() {

                public void run() {
                    if (worked < total - 10) {
                        worked++;
                        monitor.worked(worked);
                    }
                }
            });
            pause(500);
        }
    }

    public void stopIt() {
        stop = true;
    }

    private void pause(long mils) {
        try {
            Thread.sleep(mils);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
