package com.android.ide.eclipse.adt.internal.editors.xml;

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AndroidConstants;
import com.android.ide.eclipse.adt.internal.editors.AndroidEditor;
import com.android.ide.eclipse.adt.internal.editors.FirstElementParser;
import com.android.ide.eclipse.adt.internal.editors.descriptors.DocumentDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.ElementDescriptor;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiDocumentNode;
import com.android.ide.eclipse.adt.internal.sdk.AndroidTargetData;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.SdkConstants;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.FileEditorInput;
import org.w3c.dom.Document;

/**
 * Multi-page form editor for /res/xml XML files.
 */
public class XmlEditor extends AndroidEditor {

    public static final String ID = AndroidConstants.EDITORS_NAMESPACE + ".xml.XmlEditor";

    /** Root node of the UI element hierarchy */
    private UiDocumentNode mUiRootNode;

    /**
     * Creates the form editor for resources XML files.
     */
    public XmlEditor() {
        super();
    }

    /**
     * Returns the root node of the UI element hierarchy, which here
     * is the document node.
     */
    @Override
    public UiDocumentNode getUiRootNode() {
        return mUiRootNode;
    }

    /**
     * Indicates if this is a file that this {@link XmlEditor} can handle.
     * <p/>
     * The {@link XmlEditor} can handle XML files that have a <searchable> or
     * <Preferences> root XML element with the adequate xmlns:android attribute.
     *
     * @return True if the {@link XmlEditor} can handle that file.
     */
    public static boolean canHandleFile(IFile file) {
        IProject project = file.getProject();
        IAndroidTarget target = Sdk.getCurrent().getTarget(project);
        if (target != null) {
            AndroidTargetData data = Sdk.getCurrent().getTargetData(target);
            FirstElementParser.Result result = FirstElementParser.parse(file.getLocation().toOSString(), SdkConstants.NS_RESOURCES);
            if (result != null && data != null) {
                String name = result.getElement();
                if (name != null && result.getXmlnsPrefix() != null) {
                    DocumentDescriptor desc = data.getXmlDescriptors().getDescriptor();
                    for (ElementDescriptor elem : desc.getChildren()) {
                        if (elem.getXmlName().equals(name)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Returns whether the "save as" operation is supported by this editor.
     * <p/>
     * Save-As is a valid operation for the ManifestEditor since it acts on a
     * single source file.
     *
     * @see IEditorPart
     */
    @Override
    public boolean isSaveAsAllowed() {
        return true;
    }

    /**
     * Create the various form pages.
     */
    @Override
    protected void createFormPages() {
        try {
            addPage(new XmlTreePage(this));
        } catch (PartInitException e) {
            AdtPlugin.log(e, "Error creating nested page");
        }
    }

    @Override
    protected void setInput(IEditorInput input) {
        super.setInput(input);
        if (input instanceof FileEditorInput) {
            FileEditorInput fileInput = (FileEditorInput) input;
            IFile file = fileInput.getFile();
            setPartName(String.format("%1$s", file.getName()));
        }
    }

    /**
     * Processes the new XML Model, which XML root node is given.
     *
     * @param xml_doc The XML document, if available, or null if none exists.
     */
    @Override
    protected void xmlModelChanged(Document xml_doc) {
        initUiRootNode(false);
        mUiRootNode.loadFromXmlNode(xml_doc);
        super.xmlModelChanged(xml_doc);
    }

    /**
     * Creates the initial UI Root Node, including the known mandatory elements.
     * @param force if true, a new UiRootNode is recreated even if it already exists.
     */
    @Override
    protected void initUiRootNode(boolean force) {
        if (mUiRootNode == null || force) {
            Document doc = null;
            if (mUiRootNode != null) {
                doc = mUiRootNode.getXmlDocument();
            }
            AndroidTargetData data = getTargetData();
            DocumentDescriptor desc;
            if (data == null) {
                desc = new DocumentDescriptor("temp", null);
            } else {
                desc = data.getXmlDescriptors().getDescriptor();
            }
            mUiRootNode = (UiDocumentNode) desc.createUiNode();
            mUiRootNode.setEditor(this);
            onDescriptorsChanged(doc);
        }
    }

    /**
     * Reloads the UI manifest node from the XML, and calls the pages to update.
     */
    private void onDescriptorsChanged(Document document) {
        if (document != null) {
            mUiRootNode.loadFromXmlNode(document);
        } else {
            mUiRootNode.reloadFromXmlNode(mUiRootNode.getXmlNode());
        }
    }
}
