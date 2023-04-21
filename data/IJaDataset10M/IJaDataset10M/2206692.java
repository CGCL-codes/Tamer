package fr.itris.glips.svgeditor.io.managers.monitor;

import java.awt.*;
import java.io.*;
import javax.swing.*;
import fr.itris.glips.library.monitor.*;
import fr.itris.glips.svgeditor.resources.*;

/**
 * the class of the monitor used for an open action
 * @author Jordi SUC
 */
public class OpenMonitor extends Monitor {

    /**
	 * the labels
	 */
    private String titleLabel = "", messageLabel = "", documentLoadingLabel = "", imageBuildingLabel = "", imageRenderingLabel = "";

    /**
	 * the monitored file
	 */
    private File file;

    /**
	 * the constructor of the class
	 * @param file the file whose loading is monitored
	 * @param parent the parent component used to 
	 * display the progress dialog
	 * @param relativeComponent the component relatively to 
	 * which the dialog will be shown
	 * @param min the min value of the progress
	 * @param max the max value of the progress
	 */
    public OpenMonitor(File file, Component parent, JComponent relativeComponent, int min, int max) {
        super(parent, relativeComponent, min, max);
        this.file = file;
        initialize();
    }

    @Override
    protected void initialize() {
        titleLabel = ResourcesManager.bundle.getString("OpenMonitorTitle");
        messageLabel = ResourcesManager.bundle.getString("OpenMonitorMessage");
        messageLabel += file.toURI().toASCIIString();
        documentLoadingLabel = ResourcesManager.bundle.getString("OpenMonitorDocumentLoading");
        imageBuildingLabel = ResourcesManager.bundle.getString("OpenMonitorImageBuilding");
        imageRenderingLabel = ResourcesManager.bundle.getString("OpenMonitorImageRendering");
        waitDialog.setTitleMessage(titleLabel);
        waitDialog.setMessage(messageLabel);
    }

    @Override
    public void setProgress(int value) {
        if (value < 40) {
            setProgressMessage(documentLoadingLabel);
        } else if (value < 80) {
            setProgressMessage(imageBuildingLabel);
        } else if (value <= 100) {
            setProgressMessage(imageRenderingLabel);
        }
        super.setProgress(value);
    }
}
