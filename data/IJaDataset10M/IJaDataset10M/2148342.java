package org.stamppagetor.image;

/**
 * ImagePickers uses this interface to inform other program about progress. 
 */
public interface ImagePickerProgressListener {

    /**
	 * This is called when phase of progress changes.
	 * 
	 * @param phaseIndex - index of phase
	 */
    public void imagePickerPhase(int phaseIndex);

    /**
	 * This is called during progress.
	 *  
	 * @param percent - how many percent of the processing is done
	 */
    public void imagePickerProgress(int percent);
}
