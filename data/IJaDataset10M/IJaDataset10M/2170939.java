package com.tresys.slide.plugin.editors.iffiles;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentExtension3;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.rules.FastPartitioner;
import com.tresys.slide.plugin.editors.BaseDocumentProvider;
import com.tresys.slide.plugin.text.rules.XMLDocumentPartitioner;

public class IFDocumentProvider extends BaseDocumentProvider {

    public IFDocumentProvider(IFEditor editor) {
        super(editor);
    }

    public static final String DEFAULT_PARTITIONER = "if_file_default_partitioner";

    /**
	 * Connect to the given element
	 * @param element	the element to connect this document provider to
	 * @throws CoreException	if bad things happen
	 */
    public void connect(Object element) throws CoreException {
        super.connect(element);
        IDocument currentDocument = getDocument(element);
        if (currentDocument != null && currentDocument instanceof IDocumentExtension3) {
            IDocumentExtension3 currDoc = (IDocumentExtension3) currentDocument;
            IDocumentPartitioner normalPartitioner = new FastPartitioner(new XMLDocumentPartitioner(), XMLDocumentPartitioner.IF_PARTITION_TYPES);
            normalPartitioner.connect((IDocument) currDoc);
            currDoc.setDocumentPartitioner(DEFAULT_PARTITIONER, normalPartitioner);
        }
    }
}
