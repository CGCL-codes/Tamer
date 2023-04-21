package com.sebulli.fakturama.editors;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;
import com.sebulli.fakturama.data.UniDataSet;
import com.sebulli.fakturama.logger.Logger;

/**
 * Input for most editors of type UniDataSet
 * 
 * @author Gerd Bartelt
 */
public class UniDataSetEditorInput implements IEditorInput {

    private final UniDataSet uds;

    private String category = "";

    private boolean duplicate = false;

    private DocumentEditor documentEditor;

    /**
	 * Default Constructor Sets the unidataset
	 * 
	 * @param uds
	 *            The editor's unidataset
	 */
    public UniDataSetEditorInput(UniDataSet uds) {
        this.uds = uds;
        this.duplicate = false;
    }

    /**
	 * Constructor Creates an editor's input, if an unidataset is duplicated The
	 * unidataset is the parent unidataset. It won't be modified by the editor.
	 * The editor will use this unidataset just as a template to create a
	 * duplicate one.
	 * 
	 * @param category
	 *            Category of the new unidataset
	 * @param uds
	 *            Parent unidataset
	 */
    public UniDataSetEditorInput(String category, UniDataSet uds) {
        this.uds = uds;
        this.category = category;
        if ((category != null) && (uds != null)) this.duplicate = true;
    }

    /**
	 * Constructor Creates an editor's input. This will open an editor with a
	 * new content and a reference to a document editor.
	 * 
	 * @param category
	 *            The new category
	 */
    public UniDataSetEditorInput(DocumentEditor documentEditor) {
        this.uds = null;
        this.category = "";
        this.duplicate = false;
        this.documentEditor = documentEditor;
    }

    /**
	 * Constructor Creates an editor's input. This will open an editor with a
	 * new content of the specified category.
	 * 
	 * @param category
	 *            The new category
	 */
    public UniDataSetEditorInput(String category) {
        this.uds = null;
        this.category = category;
        this.duplicate = false;
    }

    /**
	 * Returns the unidataset that is associated with the editors input.
	 * 
	 * @return The unidataset
	 */
    public UniDataSet getUniDataSet() {
        return uds;
    }

    /**
	 * Returns the category of the editor's input
	 * 
	 * @return The category
	 */
    public String getCategory() {
        return category;
    }

    /**
	 * Returns, if the editor should take this unidataset as a template and
	 * create a duplicate of it.
	 * 
	 * @return
	 */
    public boolean getDuplicate() {
        return duplicate;
    }

    /**
	 * Returns the document editor.
	 * 
	 * @return
	 * 		The document editor or null
	 */
    public DocumentEditor getDocumentEditor() {
        return documentEditor;
    }

    /**
	 * Returns whether the editor input exists.
	 * 
	 * @see org.eclipse.ui.IEditorInput#exists()
	 */
    @Override
    public boolean exists() {
        return false;
    }

    /**
	 * Returns the image descriptor for this input.
	 * 
	 * @see org.eclipse.ui.IEditorInput#getImageDescriptor()
	 */
    @Override
    public ImageDescriptor getImageDescriptor() {
        return null;
    }

    /**
	 * Returns the name of this editor input for display purposes.
	 * 
	 * @see org.eclipse.ui.IEditorInput#getName()
	 */
    @Override
    public String getName() {
        if (uds == null) return "neu";
        return uds.getStringValueByKey("name");
    }

    /**
	 * Returns an object that can be used to save the state of this editor
	 * input.
	 * 
	 * @see org.eclipse.ui.IEditorInput#getPersistable()
	 */
    @Override
    public IPersistableElement getPersistable() {
        return null;
    }

    /**
	 * Returns the tool tip text for this editor input.
	 * 
	 * @see org.eclipse.ui.IEditorInput#getToolTipText()
	 */
    @Override
    public String getToolTipText() {
        return this.getName();
    }

    /**
	 * Returns an object which is an instance of the given class associated with
	 * this object
	 * 
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
    @Override
    public Object getAdapter(@SuppressWarnings("rawtypes") Class adapter) {
        return null;
    }

    /**
	 * Indicates whether some other object is "equal to" this one.
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
    @Override
    public boolean equals(Object obj) {
        if (super.equals(obj)) {
            return true;
        }
        if (uds == null) return false;
        if (obj instanceof UniDataSetEditorInput) {
            return (uds.equals(((UniDataSetEditorInput) obj).getUniDataSet()) && category.equals(((UniDataSetEditorInput) obj).getCategory()) && duplicate == ((UniDataSetEditorInput) obj).getDuplicate());
        }
        return false;
    }

    /**
	 * Returns a hash code value for the object
	 * 
	 * @see java.lang.Object#hashCode()
	 */
    @Override
    public int hashCode() {
        if (uds == null) {
            Logger.logInfo("hashCode 0");
            return 0;
        }
        return uds.hashCode();
    }
}
