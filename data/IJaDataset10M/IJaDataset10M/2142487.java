package in.raster.mayam.delegate;

import in.raster.mayam.context.ApplicationContext;
import in.raster.mayam.form.display.Display;
import in.raster.mayam.model.AEModel;
import in.raster.mayam.model.Instance;
import in.raster.mayam.model.Series;
import in.raster.mayam.util.core.DcmSnd;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author  BabuHussain
 * @version 0.5
 *
 */
public class SendingDelegate extends Thread {

    String forwardAET = "";

    String forwardHost = "";

    int forwardPort;

    String studyIUID = "";

    AEModel ae = null;

    public SendingDelegate(String studyIUID, AEModel ae) {
        this.studyIUID = studyIUID;
        this.ae = ae;
        this.start();
    }

    /**
     * This routine used to send the study to the specified AE
     * @param studyIUID
     * @param ae
     */
    public void send(String studyIUID, AEModel ae) {
        ApplicationContext.sendingProgress.updateBar(0);
        forwardAET = ae.getAeTitle();
        forwardHost = ae.getHostName();
        forwardPort = ae.getPort();
        int count = 0;
        ApplicationContext.sendingProgress.setProgressMaximum(ApplicationContext.databaseRef.getStudyLevelInstance(studyIUID));
        Display.alignScreen(ApplicationContext.sendingProgress);
        ApplicationContext.sendingProgress.setVisible(true);
        ArrayList<Series> seriesList = ApplicationContext.databaseRef.getSeriesList(studyIUID);
        Iterator<Series> seriesItr = seriesList.iterator();
        while (seriesItr.hasNext()) {
            Series series = seriesItr.next();
            Iterator<Instance> imgitr = series.getImageList().iterator();
            while (imgitr.hasNext()) {
                Instance img = imgitr.next();
                File temp = new File(ApplicationContext.getAppDirectory() + File.separator + img.getFilepath());
                String forwardParam[];
                if (temp.isFile()) {
                    forwardParam = new String[] { forwardAET + "@" + forwardHost + ":" + forwardPort, temp.getAbsolutePath() };
                } else {
                    temp = new File(img.getFilepath());
                    forwardParam = new String[] { forwardAET + "@" + forwardHost + ":" + forwardPort, temp.getAbsolutePath() };
                }
                DcmSnd.main(forwardParam);
                count++;
                ApplicationContext.sendingProgress.updateBar(count);
            }
        }
        try {
            this.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(SendingDelegate.class.getName()).log(Level.SEVERE, null, ex);
        }
        ApplicationContext.sendingProgress.setVisible(false);
    }

    public void run() {
        send(this.studyIUID, this.ae);
    }
}
