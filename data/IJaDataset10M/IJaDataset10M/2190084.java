package org.xaware.ide.xadev.gui;

import java.io.File;
import java.io.FileOutputStream;
import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.filesystem.IFileSystem;
import org.eclipse.jface.text.IDocument;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.internal.editors.text.JavaFileEditorInput;
import org.jdom.Document;
import org.jdom.Element;
import org.xaware.ide.xadev.XA_Designer_Plugin;
import org.xaware.ide.xadev.common.ControlFactory;
import org.xaware.ide.xadev.common.XMLUtil;
import org.xaware.ide.xadev.datamodel.XMLTreeNode;
import org.xaware.ide.xadev.gui.editor.XMLEditorPanel;
import org.xaware.shared.util.logging.XAwareLogger;

/**
 * Class represents XML view Panel.
 *
 * @author T Vasu
 * @version 1.0
 */
public class XMLViewPanel extends Composite {

    /** Logger for XAware. */
    private static final XAwareLogger logger = XAwareLogger.getXAwareLogger(XMLViewPanel.class.getName());

    /** Root element for this panel. */
    private XMLTreeNode rootElement;

    /** IEditorPart instance. */
    private IEditorPart editor;

    /** Document instance. */
    private Document document;

    /** execution results variable */
    protected String executeResult;

    /**
     * Creates a new XMLViewPanel object.
     *
     * @param parent parent composite.
     * @param parentInstance XMLEditorPanel instance.
     */
    public XMLViewPanel(final Composite parent, final XMLEditorPanel parentInstance) {
        super(parent, SWT.NONE);
        setLayout(new FillLayout());
        try {
            editor = ControlFactory.createInternalXMLEditor();
            final File tempFile = File.createTempFile("temp", ".executeresults");
            final FileOutputStream fos = new FileOutputStream(tempFile);
            fos.write("<dummy/>".getBytes());
            fos.flush();
            fos.close();
            final IFileSystem fileSystem = EFS.getLocalFileSystem();
            final IFileStore fileStore = fileSystem.fromLocalFile(tempFile);
            editor.init(parentInstance.getFrame().getEditorSite(), new JavaFileEditorInput(fileStore));
            editor.createPartControl(this);
        } catch (final Exception e) {
        }
    }

    /**
     * Gets the root element.
     *
     * @return Element instance.
     */
    public XMLTreeNode getElement() {
        return rootElement;
    }

    /**
     * Gets the text control.
     *
     * @return Text instance.
     */
    public String getText() {
        final IDocument iDocument = XA_Designer_Plugin.getIDocumentForEditor(editor);
        if (iDocument != null) {
            return iDocument.get();
        }
        return "";
    }

    /**
     * Sets the root element.
     *
     * @param element root element.
     */
    public void setElement(final XMLTreeNode element) {
        this.rootElement = element;
        refreshView(null);
    }

    /**
     * Reloads the content in the text control.
     *
     * @param inElement root element.
     */
    public void refreshView(final Element inElement) {
        document = null;
        try {
            if ((rootElement == null) && (inElement != null)) {
                if (inElement.getDocument() == null) {
                    document = new Document(inElement);
                } else {
                    document = inElement.getDocument();
                }
            } else {
                final XMLTreeNode treeNode = rootElement;
                if (((Element) treeNode.getJDOMContent().getContent()).getDocument() == null) {
                    document = new Document(((Element) treeNode.getJDOMContent().getContent()));
                } else {
                    document = ((Element) treeNode.getJDOMContent().getContent()).getDocument();
                }
            }
            executeResult = XMLUtil.documentToXML(document);
            Display.getDefault().asyncExec(new Runnable() {

                public void run() {
                    setText(executeResult);
                }
            });
        } catch (final Throwable ex) {
            logger.finest("Error updating view:" + ex);
        }
    }

    /**
     * Sets the text to text pane.
     *
     * @param text string instance.
     */
    public void setText(final String text) {
        final IDocument iDocument = XA_Designer_Plugin.getIDocumentForEditor(editor);
        if (iDocument != null) {
            editor.setFocus();
            iDocument.set(text);
        }
    }

    /**
     * Gets the content of execution results.
     *
     * @return Returns the executeResult.
     */
    public String getExecuteResult() {
        return executeResult;
    }
}
