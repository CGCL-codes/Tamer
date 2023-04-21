package editor.view.action;

import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.Action;
import base.exception.ExceptionHandler;
import base.exception.TechnicalException;
import editor.cmd.LoadDatasheet;
import editor.util.Resources;
import editor.view.GraphicFrame;

/**
 * Action loads a xml file by setting the file through a method and not by dialog
 */
public final class LoadSheetFromFileAction extends AbstractLocaleAction {

    private final GraphicFrame oFrame;

    private File oLoadFile = null;

    /**
     * Constructor
     *
     * @param frame the Controller frame
     */
    public LoadSheetFromFileAction(final GraphicFrame frame) {
        super(null);
        oFrame = frame;
    }

    /**
     * Loads the given file to the Application
     *
     * @param e ActionEvent
     */
    @Override
    public final void actionPerformed(final ActionEvent e) {
        if (oLoadFile != null && isXmlFile(oLoadFile)) {
            final LoadDatasheet cmd = new LoadDatasheet(oFrame, oLoadFile);
            try {
                cmd.doCommand();
            } catch (TechnicalException exc) {
                ExceptionHandler.ERRORHANDLER.handleException(new TechnicalException(exc.getMessage(), exc));
            }
        }
    }

    /**
     * Updates the action to the current locale
     *
     * @see base.gui.action.UpdateAction
     */
    @Override
    public final void updateLocale() {
        putValue(Action.NAME, getActionName());
    }

    /**
     * Sets the file to be load, if action is performed
     *
     * @param s file to load
     */
    public final void setFile(final File s) {
        oLoadFile = s;
        setActionName(s.getAbsolutePath());
        updateLocale();
    }

    private final boolean isXmlFile(final File f) {
        return (f.getName().endsWith("." + Resources.getText("file_filter.xml.extension"))) ? true : false;
    }
}
