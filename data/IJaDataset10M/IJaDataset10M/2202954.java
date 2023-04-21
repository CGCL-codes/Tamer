package org.in4ama.editor.project.documents;

import java.io.InputStream;
import org.in4ama.documentautomator.DocumentMgr;
import org.in4ama.documentautomator.documents.Document;
import org.in4ama.documentautomator.documents.odt.OdtDocument;
import org.in4ama.documentautomator.documents.odt.OdtLetterFragment;
import org.in4ama.documentautomator.documents.xslfo.XslFoDocument;
import org.in4ama.documentautomator.documents.xslfo.XslFoLetterFragment;
import org.in4ama.editor.exception.EditorException;
import org.in4ama.editor.oo.Odt2XslFoConverter;

public class OdtLetterFragmentEditor extends DocumentTypeEditor {

    public static final String ICON = "oodoc_icon_48.png";

    public static final String TREE_ICON = "tree_letterfragment_20.png";

    public static final String EDITOR_PAGE = "lettereditorpage";

    public static final DocumentTypeEditor instance = new OdtLetterFragmentEditor();

    @Override
    public String getDispTypeName() {
        return OdtLetterFragment.DISP_TYPE;
    }

    @Override
    public String getTypeName() {
        return OdtLetterFragment.TYPE;
    }

    @Override
    public String getEditorPageName() {
        return EDITOR_PAGE;
    }

    @Override
    public String getIcon() {
        return ICON;
    }

    @Override
    public String getTreeIcon() {
        return TREE_ICON;
    }

    @Override
    public boolean createNewSupported() {
        return true;
    }

    @Override
    public void saveDocument(Document doc) throws EditorException {
        String name = doc.getName();
        String type = doc.getType();
        try {
            OdtDocument document = (OdtDocument) doc;
            super.saveDocument(document);
            InputStream odtContent = document.getContent();
            InputStream xslFoContent = Odt2XslFoConverter.getInstance().convertToXslFo(odtContent);
            DocumentMgr documentMgr = getDefaultDocumentMgr();
            XslFoDocument xslFoDoc = (XslFoDocument) documentMgr.createDocument(name, XslFoLetterFragment.TYPE);
            xslFoDoc.setContent(xslFoContent);
            documentMgr.saveDocument(xslFoDoc);
        } catch (Exception ex) {
            String msg = "Unable to save the document '" + name + "' of a type '" + type + "'.";
            throw new EditorException(msg, ex);
        }
    }
}
