package org.in4ama.editor.project.documents;

import org.in4ama.documentautomator.documents.acroform.AcroFormDocument;
import org.in4ama.documentautomator.documents.acroform.OOAcroFormDocument;

public class OOAcroFormEditor extends DocumentTypeEditor {

    public static final String ICON = "pdf_icon_48.png";

    public static final String TREE_ICON = "tree_pdf_20.png";

    public static final String EDITOR_PAGE = "pdfeditorpage";

    public static final DocumentTypeEditor instance = new OOAcroFormEditor();

    @Override
    public String getIcon() {
        return ICON;
    }

    @Override
    public String getTreeIcon() {
        return TREE_ICON;
    }

    @Override
    public String getTypeName() {
        return OOAcroFormDocument.TYPE;
    }

    @Override
    public String getDispTypeName() {
        return AcroFormDocument.DISP_TYPE;
    }

    @Override
    public String getEditorPageName() {
        return EDITOR_PAGE;
    }

    @Override
    public boolean createNewSupported() {
        return false;
    }
}
