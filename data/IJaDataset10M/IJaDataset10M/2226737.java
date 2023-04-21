package com.google.gwt.eclipse.core.uibinder.formatter;

import com.google.gdt.eclipse.core.XmlUtilities;
import com.google.gdt.eclipse.core.formatter.IndependentMultiPassContentFormatter;
import com.google.gdt.eclipse.platform.content.XmlFormattingStrategy;
import com.google.gwt.eclipse.core.uibinder.UiBinderException;
import com.google.gwt.eclipse.core.uibinder.sse.StructuredTextPartitionerForUiBinderXml;
import com.google.gwt.eclipse.core.uibinder.sse.css.InlinedCssFormattingStrategy;
import com.google.gwt.eclipse.core.uibinder.text.IDocumentPartitionerFactory;
import com.google.gwt.eclipse.core.uibinder.text.StructuredDocumentCloner;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.formatter.IContentFormatter;
import org.eclipse.wst.css.core.text.ICSSPartitions;
import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocument;
import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredPartitioning;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMDocument;
import org.eclipse.wst.xml.core.text.IXMLPartitions;
import java.io.IOException;

/**
 * Utility methods for formatting UiBinder template (ui.xml) files.
 */
@SuppressWarnings("restriction")
public final class UiBinderFormatter {

    private static final IDocumentPartitionerFactory UIBINDER_XML_PARTITIONER_FACTORY = new IDocumentPartitionerFactory() {

        public IDocumentPartitioner createDocumentPartitioner() {
            return new StructuredTextPartitionerForUiBinderXml();
        }
    };

    public static IContentFormatter createFormatter(String partitioning) {
        IndependentMultiPassContentFormatter formatter = new IndependentMultiPassContentFormatter(partitioning, IXMLPartitions.XML_DEFAULT, new StructuredDocumentCloner(partitioning, UIBINDER_XML_PARTITIONER_FACTORY));
        formatter.setMasterStrategy(new XmlFormattingStrategy());
        formatter.setSlaveStrategy2(new InlinedCssFormattingStrategy(), ICSSPartitions.STYLE);
        formatter.setReplaceSlavePartitionsDuringMasterFormat(true);
        formatter.setCheckForNonwhitespaceChanges(true);
        return formatter;
    }

    /**
   * Formats a UiBinder template file.
   * 
   * @param uiXmlFile the ui.xml file to format
   * @param forceSave if <code>true</code>, the file will be saved even if it
   *          already has pending edits. If there are no pending edits, the file
   *          will always be saved after formatting.
   */
    public static void format(IFile uiXmlFile, boolean forceSave) throws UiBinderException {
        final IContentFormatter formatter = createFormatter(IStructuredPartitioning.DEFAULT_STRUCTURED_PARTITIONING);
        try {
            new XmlUtilities.EditOperation(uiXmlFile) {

                @Override
                protected void edit(IDOMDocument document) {
                    IStructuredDocument doc = document.getModel().getStructuredDocument();
                    formatter.format(doc, new Region(0, doc.getLength()));
                }
            }.run(forceSave);
        } catch (IOException e) {
            throw new UiBinderException(e);
        } catch (CoreException e) {
            throw new UiBinderException(e);
        }
    }

    private UiBinderFormatter() {
    }
}
