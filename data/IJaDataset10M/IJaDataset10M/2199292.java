package spatschorke.da.ooconnector;

import java.util.List;
import org.eclipse.core.resources.IFile;
import org.tuba.data.IArtefactPosition;
import org.tuba.plugins.ITutorialDocument;
import spatschorke.da.ooconnector.connection.ConnectorMaster;
import ag.ion.bion.officelayer.document.DocumentException;
import ag.ion.bion.officelayer.document.IDocument;

public class OODocument implements ITutorialDocument {

    private IDocument document;

    private IFile file;

    public OODocument(IDocument document, IFile file) {
        super();
        this.document = document;
        this.file = file;
    }

    public IDocument getDocument() {
        return document;
    }

    @Override
    public String getType() {
        return document.getDocumentType();
    }

    @Override
    public boolean equalsTo(ITutorialDocument tutorialDocument) {
        if (tutorialDocument == null || !tutorialDocument.getType().equals(document.getDocumentType())) return false;
        return document.equalsTo(((OODocument) tutorialDocument).getDocument());
    }

    @Override
    public List<IArtefactPosition> searchArtefactTags() {
        ConnectorMaster connectorMaster = ConnectorMaster.getInstance();
        List<IArtefactPosition> positions = connectorMaster.searchArtefactTags(this);
        return positions;
    }

    @Override
    public void setModified(boolean value) {
        try {
            document.setModified(value);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isModified() {
        return document.isModified();
    }

    @Override
    public IFile getFile() {
        return file;
    }
}
