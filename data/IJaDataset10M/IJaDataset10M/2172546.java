package org.jscience.mathMLImpl;

import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.mathML.MathMLContentElement;
import org.w3c.dom.mathML.MathMLVectorElement;

/**
 * Implements a MathML <code>vector</code> element.
 *
 * @author Mark Hale
 * @version 1.0
 */
public class MathMLVectorElementImpl extends MathMLElementImpl implements MathMLVectorElement {

    /**
     * Constructs a MathML <code>vector</code> element.
     *
     * @param owner         DOCUMENT ME!
     * @param qualifiedName DOCUMENT ME!
     */
    public MathMLVectorElementImpl(MathMLDocumentImpl owner, String qualifiedName) {
        super(owner, qualifiedName);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getNcomponents() {
        return getComponentsGetLength();
    }

    /**
     * DOCUMENT ME!
     *
     * @param index DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws DOMException DOCUMENT ME!
     */
    public MathMLContentElement getComponent(int index) throws DOMException {
        Node component = getComponentsItem(index - 1);
        if (component == null) {
            throw new DOMException(DOMException.INDEX_SIZE_ERR, "Index out of bounds");
        }
        return (MathMLContentElement) component;
    }

    /**
     * DOCUMENT ME!
     *
     * @param newComponent DOCUMENT ME!
     * @param index DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws DOMException DOCUMENT ME!
     */
    public MathMLContentElement setComponent(MathMLContentElement newComponent, int index) throws DOMException {
        final int componentsLength = getComponentsGetLength();
        if ((index < 1) || (index > (componentsLength + 1))) {
            throw new DOMException(DOMException.INDEX_SIZE_ERR, "Index out of bounds");
        }
        if (index == (componentsLength + 1)) {
            return (MathMLContentElement) appendChild(newComponent);
        } else {
            return (MathMLContentElement) replaceChild(newComponent, getComponentsItem(index - 1));
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param newComponent DOCUMENT ME!
     * @param index DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws DOMException DOCUMENT ME!
     */
    public MathMLContentElement insertComponent(MathMLContentElement newComponent, int index) throws DOMException {
        final int componentsLength = getComponentsGetLength();
        if ((index < 0) || (index > (componentsLength + 1))) {
            throw new DOMException(DOMException.INDEX_SIZE_ERR, "Index out of bounds");
        }
        if ((index == 0) || (index == (componentsLength + 1))) {
            return (MathMLContentElement) appendChild(newComponent);
        } else {
            return (MathMLContentElement) insertBefore(newComponent, getComponentsItem(index - 1));
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param index DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws DOMException DOCUMENT ME!
     */
    public MathMLContentElement removeComponent(int index) throws DOMException {
        Node component = getComponentsItem(index - 1);
        if (component == null) {
            throw new DOMException(DOMException.INDEX_SIZE_ERR, "Index out of bounds");
        }
        return (MathMLContentElement) removeChild(component);
    }

    /**
     * DOCUMENT ME!
     *
     * @param index DOCUMENT ME!
     *
     * @throws DOMException DOCUMENT ME!
     */
    public void deleteComponent(int index) throws DOMException {
        removeComponent(index);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private int getComponentsGetLength() {
        final int length = getLength();
        int numComponents = 0;
        for (int i = 0; i < length; i++) {
            if (item(i) instanceof MathMLContentElement) {
                numComponents++;
            }
        }
        return numComponents;
    }

    /**
     * DOCUMENT ME!
     *
     * @param index DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private Node getComponentsItem(int index) {
        final int componentsLength = getComponentsGetLength();
        if ((index < 0) || (index >= componentsLength)) {
            return null;
        }
        Node node = null;
        int n = -1;
        for (int i = 0; n < index; i++) {
            node = item(i);
            if (node instanceof MathMLContentElement) {
                n++;
            }
        }
        return node;
    }
}
