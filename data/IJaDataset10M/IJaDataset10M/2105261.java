package editor.cmd;

import java.io.File;
import java.io.IOException;
import base.exception.ExceptionHandler;
import base.exception.TechnicalException;
import editor.EditorSettings;
import editor.model.xml.StationDataSheet;
import editor.util.Resources;
import editor.view.GraphicFrame;
import org.jdom.JDOMException;

public class LoadDatasheet {

    private final GraphicFrame oFrame;

    private final File oFileToLoad;

    public LoadDatasheet(final GraphicFrame frame, final File file) {
        oFrame = frame;
        oFileToLoad = file;
    }

    public boolean doCommand() throws TechnicalException {
        try {
            if (oFrame.getView().hasActiveSheet()) {
                oFrame.getModel().removeAllDatasheetChangeListener();
                oFrame.getView().closeSheet();
            }
            oFrame.getView().newSheet(StationDataSheet.loadDataSheet(oFileToLoad));
            EditorSettings.getInstance().setLastUsedDirectory(oFileToLoad.getParent());
            EditorSettings.getInstance().addDocumentToLastUsed(oFileToLoad.getAbsolutePath());
            EditorSettings.getInstance().save();
            oFrame.updateView();
            oFrame.getModel().addDatasheetChangeListener(oFrame.getUpdater());
            oFrame.getModel().setCurrentFile(oFileToLoad);
            oFrame.getModel().addDatasheetChangeListener(oFrame.getActionFactory().getSaveSheetAction());
            oFrame.getActionFactory().getSaveSheetAsAction().setEnabled(true);
            oFrame.getActionFactory().getCloseSheetAction().setEnabled(true);
            return true;
        } catch (JDOMException exc) {
            ExceptionHandler.ERRORHANDLER.handleException(new TechnicalException(Resources.getFormattedText("error.reading_file", oFileToLoad.getName()), exc));
            return false;
        } catch (Exception exc) {
            ExceptionHandler.ERRORHANDLER.handleException(new TechnicalException(Resources.getText("error.open_file"), exc.getCause()));
            return false;
        }
    }
}
