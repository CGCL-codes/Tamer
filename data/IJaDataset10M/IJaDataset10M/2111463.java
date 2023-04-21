package pl.omtt.eclipse.ui.document;

import org.eclipse.core.filebuffers.IDocumentSetupParticipant;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioner;
import pl.omtt.eclipse.ui.partitioner.OmttPartitioner;

public class OmttDocumentSetupParticipant implements IDocumentSetupParticipant {

    @Override
    public void setup(IDocument document) {
        if (document != null) {
            IDocumentPartitioner partitioner = new OmttPartitioner();
            partitioner.connect(document);
            document.setDocumentPartitioner(partitioner);
        }
    }
}
