package net.sf.eclipse.portlet.ui.internal.common.tree;

import org.eclipse.swt.graphics.Image;

/**
 * A base implementation of a tree element
 * 
 * @author fwjwiegerinck
 * @since 0.2
 */
public class BaseTreeElement implements ITreeElement {

    /**
	 * Store the source backing this element
	 */
    private Object source;

    /**
	 * Store the parent of this element
	 */
    private IParentTreeElement<? extends ITreeElement> parent;

    /**
	 * Store the title of this element
	 */
    private String title;

    /**
	 * Store the description of this element
	 */
    private String description;

    /**
	 * Store the image of this element
	 */
    private Image image;

    /**
	 * Initialize base tree lement
	 * 
	 * @param source Source Source backing this element
	 * @param parent Parent of this TreeElement, or NULL if non defined
	 * @param title Title for this element, or NULL if non defined
	 * @param description Description for this element, or NULL if non defined
	 * @param image Image for this element, or NULL if non defined
	 */
    public BaseTreeElement(Object source, IParentTreeElement<? extends ITreeElement> parent, String title, String description, Image image) {
        super();
        this.source = source;
        this.parent = parent;
        this.title = title;
        this.description = description;
        this.image = image;
    }

    /**
	 * @see net.sf.eclipse.portlet.ui.internal.common.tree.ITreeElement#getDescription()
	 */
    public String getDescription() {
        return this.description;
    }

    /**
	 * @see net.sf.eclipse.portlet.ui.internal.common.tree.ITreeElement#getImage()
	 */
    public Image getImage() {
        return this.image;
    }

    /**
	 * @see net.sf.eclipse.portlet.ui.internal.common.tree.ITreeElement#getParent()
	 */
    public IParentTreeElement<? extends ITreeElement> getParent() {
        return parent;
    }

    /**
	 * @see net.sf.eclipse.portlet.ui.internal.common.tree.ITreeElement#getSource()
	 */
    public Object getSource() {
        return this.source;
    }

    /**
	 * @see net.sf.eclipse.portlet.ui.internal.common.tree.ITreeElement#getTitle()
	 */
    public String getTitle() {
        return this.title;
    }
}
