package org.aspencloud.simple9.builder.editor;

import org.aspencloud.simple9.builder.Simple9Plugin;
import org.eclipse.core.filebuffers.IDocumentSetupParticipant;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentExtension3;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.rules.FastPartitioner;

/**
 * 
 */
public class EspDocumentSetupParticipant implements IDocumentSetupParticipant {

    /**
	 */
    public EspDocumentSetupParticipant() {
    }

    public void setup(IDocument document) {
        if (document instanceof IDocumentExtension3) {
            IDocumentExtension3 extension3 = (IDocumentExtension3) document;
            IDocumentPartitioner partitioner = new FastPartitioner(Simple9Plugin.getDefault().getEspPartitionScanner(), EspPartitionScanner.ESP_PARTITION_TYPES);
            extension3.setDocumentPartitioner(Simple9Plugin.ESP_PARTITIONING, partitioner);
            partitioner.connect(document);
        }
    }
}
