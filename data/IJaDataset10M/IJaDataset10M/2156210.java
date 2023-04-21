package org.stamppagetor.image;

import javax.swing.SwingWorker;

/**
 * Swing worker for executing process() method of image picker
 */
public class ImagePickerWorker extends SwingWorker<Integer, Void> implements ImagePickerProgressListener {

    public static final String PHASE_PROPERTY = "progressPhase";

    public static final String PERCENT_PROPERTY = "progress";

    private Integer oldPhase = null;

    private final ImagePickerInterface picker;

    /**
	 * constructor
	 * 
	 * @param imagePicker
	 */
    public ImagePickerWorker(ImagePickerInterface imagePicker) {
        this.picker = imagePicker;
    }

    @Override
    public Integer doInBackground() {
        imagePickerProgress(0);
        this.picker.process(this);
        imagePickerProgress(100);
        return new Integer(1);
    }

    @Override
    protected void done() {
    }

    /**
     * This is part of ImagePickerProgressListener interface.
     * Worker thread call this from picker.
     */
    public void imagePickerPhase(int phaseIndex) {
        final Integer newPhase = new Integer(phaseIndex);
        firePropertyChange(PHASE_PROPERTY, this.oldPhase, newPhase);
        this.oldPhase = newPhase;
    }

    /**
     * This is part of ImagePickerProgressListener interface.
     * Worker thread call this from picker.
     */
    public void imagePickerProgress(int percent) {
        setProgress(percent);
    }
}
