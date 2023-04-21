package eyetrackercalibrator.trialmanaging;

import eyetrackercalibrator.framemanaging.FrameSynchronizor;
import eyetrackercalibrator.framemanaging.InformationDatabase;
import eyetrackercalibrator.gui.util.IntervalMarkerManager;
import java.io.File;
import java.util.Vector;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author ruj
 */
public abstract class TrialFileHandler extends FileFilter {

    public TrialFileHandler(String extension, String description) {
        this.extension = extension;
        this.description = description;
    }

    public String extension;

    public String description;

    @Override
    public boolean accept(File f) {
        String ext = Utils.getExtension(f);
        return ext != null && (ext.equalsIgnoreCase(extension) || ext.equalsIgnoreCase(extension.concat(".txt")));
    }

    @Override
    public String getDescription() {
        return "(" + extension + ") " + description;
    }

    /**
     * Parsing input file and output the trial object
     * @param inputFile
     * @return null if there is error in format or file opening
     */
    public abstract Trial parse(File inputFile);

    /**
     * Process information using all provided informaion to create an array 
     * (returned in the form of vector) of trial marker
     * @param infoDatabase 
     * @param trial 
     * @param firstTrialStartFrame 
     * @param lastTrialStartFrame 
     * @param eyeOffset 
     * @param screenOffset 
     * @param intervalMarkerManager 
     * @return
     */
    public abstract Vector<TrialMarker> estimateTrials(InformationDatabase infoDatabase, Trial trial, int firstTrialStartFrame, int lastTrialStartFrame, FrameSynchronizor frameSynchronizor, IntervalMarkerManager intervalMarkerManager);

    /**
     *  This method estimate trials from Illumination computed
     * 
     * @param informationDatabase Database of Illumination computed
     * @param trials array of trials.  The content is ecpected to change after
     * going through this method
     */
    public abstract void estimateTrialMarking(InformationDatabase informationDatabase, TrialMarker[] trials, FrameSynchronizor frameSynchronizor);
}
