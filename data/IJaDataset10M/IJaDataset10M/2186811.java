package in.raster.mayam.delegate;

import in.raster.mayam.context.ApplicationContext;
import in.raster.mayam.model.Series;
import in.raster.mayam.model.Study;
import in.raster.mayam.util.measurement.StudyAnnotation;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author  BabuHussain
 * @version 0.5
 *
 */
public class StudyListUpdator extends Thread {

    private String siuid = "";

    private ArrayList<Study> studyList;

    private String studyDir = "";

    StudyAnnotation studyAnnotation = null;

    public StudyListUpdator() {
    }

    public void run() {
        addStudyToStudyList();
    }

    public void setAnnotationToObject() {
        File annotationInfo = new File(studyDir, "info.ser");
        if (annotationInfo.isFile()) {
            FileInputStream fis = null;
            ObjectInputStream obs = null;
            try {
                fis = new FileInputStream(annotationInfo);
                obs = new ObjectInputStream(fis);
                try {
                    studyAnnotation = (StudyAnnotation) obs.readObject();
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(ShowViewerDelegate.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (IOException ex) {
                Logger.getLogger(ShowViewerDelegate.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(ShowViewerDelegate.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    fis.close();
                } catch (IOException ex) {
                    Logger.getLogger(ShowViewerDelegate.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    obs.close();
                } catch (IOException ex) {
                    Logger.getLogger(ShowViewerDelegate.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    /**
     * This routine used to add the study object of the specified studyUID to the studylist.
     * @param siuid
     * @param studyList
     */
    public void addStudyToStudyList(String siuid, ArrayList<Study> studyList, String studyDir) {
        this.siuid = siuid;
        this.studyList = studyList;
        this.studyDir = new File(studyDir).getParent();
        this.start();
    }

    public void addStudyToStudyList() {
        ArrayList<Series> seriesList = ApplicationContext.databaseRef.getSeriesList(siuid);
        setAnnotationToObject();
        SeriesListUpdator seriesListUpdator = new SeriesListUpdator();
        seriesListUpdator.setDicomReader();
        if (studyAnnotation != null) {
            seriesListUpdator.setStudyAnnotation(studyAnnotation);
        }
        int i = 0;
        for (Series series : seriesList) {
            if (ApplicationContext.imageViewExist()) {
                if (i == 0) {
                    if (ApplicationContext.databaseRef.getMultiframeStatus()) seriesListUpdator.addSeriesToStudyList(siuid, series.getSeriesInstanceUID(), series.isMultiframe(), series.getInstanceUID(), false); else seriesListUpdator.addSeriesToStudyList(siuid, series.getSeriesInstanceUID(), false);
                } else {
                    if (ApplicationContext.databaseRef.getMultiframeStatus()) seriesListUpdator.addSeriesToStudyList(siuid, series.getSeriesInstanceUID(), series.isMultiframe(), series.getInstanceUID(), true); else seriesListUpdator.addSeriesToStudyList(siuid, series.getSeriesInstanceUID(), true);
                }
                i++;
            }
        }
    }
}
