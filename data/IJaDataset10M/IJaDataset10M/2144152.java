package com.byterefinery.rmbench.export.text;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.eclipse.jface.text.source.AnnotationModel;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.ui.editors.text.FileDocumentProvider;
import com.byterefinery.rmbench.export.DDLEditorInput;
import com.byterefinery.rmbench.export.ModelCompareEditorInput;

/**
 * configures new documents for partitioning into DDL partition types, which are
 * later used in syntax coloring
 * 
 * @author cse
 */
public class DDLDocumentProvider extends FileDocumentProvider {

    protected IDocument createDocument(Object element) throws CoreException {
        IDocument document = super.createDocument(element);
        if (document != null) {
            IDocumentPartitioner partitioner = new FastPartitioner(new DDLPartitionScanner(), DDLSourceViewerConfiguration.CONTENT_TYPES);
            partitioner.connect(document);
            document.setDocumentPartitioner(partitioner);
        }
        return document;
    }

    protected IAnnotationModel createAnnotationModel(Object element) throws CoreException {
        if (element instanceof DDLEditorInput || element instanceof ModelCompareEditorInput) {
            return new AnnotationModel();
        }
        return super.createAnnotationModel(element);
    }
}
