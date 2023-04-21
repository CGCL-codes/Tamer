package com.nokia.ats4.appmodel.perspective.modeldesign.controller;

import com.nokia.ats4.appmodel.event.EventQueue;
import com.nokia.ats4.appmodel.event.KendoEvent;
import com.nokia.ats4.appmodel.event.KendoEventListener;
import com.nokia.ats4.appmodel.grapheditor.event.ImageCellReSizeEvent;
import com.nokia.ats4.appmodel.grapheditor.event.InsertImageEvent;
import com.nokia.ats4.appmodel.model.domain.SystemState;
import com.nokia.ats4.appmodel.util.image.ImageData;
import com.nokia.ats4.appmodel.util.Settings;
import com.nokia.ats4.appmodel.util.KendoResources;
import com.nokia.ats4.appmodel.util.image.ImageGallery;
import java.io.File;
import org.apache.log4j.Logger;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.jgraph.JGraph;
import org.jgraph.graph.DefaultGraphCell;

/**
 * InsertImageCommand handles the inserting of an screenshot image (JPG, PNG
 * etc.) to a system response state.
 *
 * @author Kimmo Tuukkanen
 * @version $Revision: 2 $
 */
public class InsertImageCommand implements KendoEventListener {

    /** Logging */
    private static Logger log = Logger.getLogger(InsertImageCommand.class);

    /** The current directory location for file dialog */
    private File currentDirectory = null;

    /**
     * Creates a new instance of InsertImageCommand
     */
    public InsertImageCommand() {
        String path = Settings.getProperty("default.directory");
        if (path != null) {
            File dir = new File(path);
            if (dir.exists() && dir.isDirectory()) {
                this.currentDirectory = dir;
            }
        }
    }

    @Override
    public void processEvent(KendoEvent event) {
        log.debug("Processing InsertImageEvent..");
        InsertImageEvent evt = (InsertImageEvent) event;
        Object obj = evt.getCell();
        if (obj instanceof DefaultGraphCell) {
            DefaultGraphCell cell = (DefaultGraphCell) obj;
            File f = this.getFile();
            if (f != null) {
                SystemState state = (SystemState) cell.getUserObject();
                this.loadAndSetImage(f, state);
                ((JGraph) event.getSource()).repaint();
                boolean fit = Settings.getBooleanProperty("graph.systemState.fitImageToCell");
                EventQueue.dispatchEvent(new ImageCellReSizeEvent(event.getSource(), fit));
            }
        }
    }

    /**
     * Displays a file open dialog
     */
    private File getFile() {
        JFileChooser jfc = new JFileChooser();
        FileFilter flt = new FileNameExtensionFilter("JPG, GIF or PNG image", "jpg", "gif", "png");
        jfc.setFileFilter(flt);
        jfc.setMultiSelectionEnabled(false);
        jfc.setAcceptAllFileFilterUsed(false);
        jfc.setDialogTitle(KendoResources.getString("dialog.openImage.title"));
        jfc.setCurrentDirectory(this.currentDirectory);
        int i = jfc.showOpenDialog(jfc);
        return i == JFileChooser.APPROVE_OPTION ? jfc.getSelectedFile() : null;
    }

    /**
     * Loads the given file and sets it to given system state.
     *
     * @param f File to load
     * @param state SystemState where the image data is attached
     */
    void loadAndSetImage(File f, SystemState state) {
        if (f.exists()) {
            try {
                String variant = Settings.getProperty("language.variant");
                ImageData id = state.getImageData();
                ImageGallery ig = id.getImageGallery();
                File imported = ig.importImage(f);
                id.setImageFilename(variant, imported.getName());
                this.currentDirectory = f.getParentFile();
            } catch (Exception ex) {
                log.error("Error while loading image from " + f.getAbsolutePath(), ex);
            }
        }
    }
}
