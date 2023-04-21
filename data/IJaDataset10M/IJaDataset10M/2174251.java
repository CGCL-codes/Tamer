package com.mtp.pounder;

import java.io.File;
import com.mtp.pounder.controller.PounderController;

/**

This class should be updated for every new release.  It will allow
conversion to a new version through a simple playback/recording
interface.

@author Matthew Pekar

**/
public class VersionConversion {

    protected PounderController controller;

    protected PounderModel model;

    public VersionConversion() {
    }

    /** Begin recording to given file using the most thorough
	 * recording style available. **/
    public void beginRecording(File f) {
        model = new PounderModel();
        controller = new PounderController(model);
        model.getFileModel().setFile(f);
        controller.getModel().beginVerbatimRecording();
    }

    /** Play the script in the given file. **/
    public void play(File f) throws Exception {
        new Player(f.getAbsolutePath()).play();
    }

    /** End the recording started in beginRecording().  Save data to
	 * File that was given there. **/
    public void endRecording() {
        controller.getSaveAction().actionPerformed(null);
    }
}
