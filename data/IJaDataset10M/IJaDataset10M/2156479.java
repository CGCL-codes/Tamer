package org.minions.stigma.editor.resourceset;

/**
 * Base document class for a parameterized object type.
 * @param <T>
 *            type of the object this document is for
 */
public abstract class ResourceSetDocument<T> extends ResourceSetViewNode {

    private T resource;

    private ResourceSetCategory<T> resourceSetCategory;

    /**
     * Constructor.
     * @param resource
     *            resource
     * @param resourceSetCategory
     *            category of the resource
     */
    public ResourceSetDocument(T resource, ResourceSetCategory<T> resourceSetCategory) {
        this.resource = resource;
        this.resourceSetCategory = resourceSetCategory;
    }

    /**
     * Returns the category of this document.
     * @return the category of this document
     */
    public ResourceSetCategory<T> getCategory() {
        return resourceSetCategory;
    }

    /**
     * Returns the resource that this document wraps.
     * @return the resource
     */
    public T getResource() {
        return resource;
    }

    /**
     * Returns an inited document editor.
     * @return editor
     */
    public ResourceEditor<T> getInitedEditor() {
        ResourceEditor<T> editor = getCategory().getEditor();
        editor.init(this);
        return editor;
    }
}
