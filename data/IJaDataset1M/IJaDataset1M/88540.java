package com.global360.sketchpadbpmn.dialogs;

import java.io.File;
import org.jdom.Document;
import com.global360.sketchpadbpmn.SketchManager;
import com.global360.sketchpadbpmn.utility.MessageLog;

public class ImportSuccessfulDialog extends ImportErrorsDialog {

    private static final long serialVersionUID = 111837018935814908L;

    public ImportSuccessfulDialog(SketchManager ownerIn, String xpdlFileNameIn, MessageLog messages, Document importedDocument, File documentDirectoryIn) {
        super(ownerIn, xpdlFileNameIn, messages, importedDocument, documentDirectoryIn);
    }

    @Override
    public String getClosingMessage() {
        return "";
    }

    @Override
    public String getClosingQuestion() {
        return "Continue importing the file?";
    }

    @Override
    public String getOpeningMessage() {
        return "The file import transform was successful.";
    }

    @Override
    public String getTitle() {
        return "Import Successful";
    }

    @Override
    public String getDialogName() {
        return "importSuccessfulDialog";
    }
}
