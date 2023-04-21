package uk.ac.ebi.pride.gui.action.impl;

import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ebi.pride.data.controller.DataAccessController;
import uk.ac.ebi.pride.data.controller.DataAccessException;
import uk.ac.ebi.pride.gui.PrideInspectorContext;
import uk.ac.ebi.pride.gui.action.PrideAction;
import uk.ac.ebi.pride.gui.component.dialog.SimpleFileDialog;
import uk.ac.ebi.pride.gui.desktop.Desktop;
import uk.ac.ebi.pride.gui.event.ForegroundDataSourceEvent;
import uk.ac.ebi.pride.gui.task.impl.ExportSpectrumMGFTask;
import uk.ac.ebi.pride.gui.utils.DefaultGUIBlocker;
import uk.ac.ebi.pride.gui.utils.GUIBlocker;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;
import static uk.ac.ebi.pride.gui.utils.Constants.DOT;
import static uk.ac.ebi.pride.gui.utils.Constants.MGF_FILE;

/**
 * Export spectra to mgf format.
 * <p/>
 * User: dani
 * Date: 23-Aug-2010
 * Time: 11:38:26
 * To change this template use File | Settings | File Templates.
 */
public class ExportSpectrumAction extends PrideAction {

    private static final Logger logger = LoggerFactory.getLogger(ExportSpectrumAction.class);

    private static final String FILE_NAME = "spectrum";

    public ExportSpectrumAction(String name, Icon icon) {
        super(name, icon);
        AnnotationProcessor.process(this);
        this.setEnabled(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        PrideInspectorContext context = (PrideInspectorContext) Desktop.getInstance().getDesktopContext();
        DataAccessController controller = context.getForegroundDataAccessController();
        String defaultFileName = controller.getName().split("\\" + DOT)[0] + "_" + FILE_NAME;
        SimpleFileDialog ofd = new SimpleFileDialog(context.getOpenFilePath(), "Select File To Export Spectrum Data To", defaultFileName, false, MGF_FILE);
        ofd.setMultiSelectionEnabled(false);
        int result = ofd.showDialog(Desktop.getInstance().getMainComponent(), null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = ofd.getSelectedFile();
            String filePath = selectedFile.getPath();
            context.setOpenFilePath(filePath.replace(selectedFile.getName(), ""));
            ExportSpectrumMGFTask newTask = new ExportSpectrumMGFTask(controller, filePath + (filePath.endsWith(MGF_FILE) ? "" : MGF_FILE));
            newTask.setGUIBlocker(new DefaultGUIBlocker(newTask, GUIBlocker.Scope.NONE, null));
            Desktop.getInstance().getDesktopContext().addTask(newTask);
        }
    }

    @EventSubscriber(eventClass = ForegroundDataSourceEvent.class)
    public void onForegroundDataSourceEvent(ForegroundDataSourceEvent evt) {
        try {
            DataAccessController controller = (DataAccessController) evt.getNewForegroundDataSource();
            this.setEnabled(controller != null && controller.hasSpectrum());
        } catch (DataAccessException e) {
            logger.error("Failed to check the data access controller", e);
        }
    }
}
