package net.openchrom.rcp.connector.microsoft.office.ui.editors;

public class WordEditorDocx extends OLEEditor {

    public WordEditorDocx() {
        super(IOLEApplication.PROG_ID_WORD, "docx", "Microsoft Word 2007 (*.docx)");
    }
}
