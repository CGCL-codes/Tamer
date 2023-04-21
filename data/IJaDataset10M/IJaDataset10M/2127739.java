package org.w3c.dom.mathML;

import org.w3c.dom.DOMException;

/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.2 $
 */
public interface MathMLVectorElement extends MathMLContentElement {

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getNcomponents();

    /**
     * DOCUMENT ME!
     *
     * @param index DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public MathMLContentElement getComponent(int index);

    /**
     * DOCUMENT ME!
     *
     * @param newComponent DOCUMENT ME!
     * @param index        DOCUMENT ME!
     * @return DOCUMENT ME!
     * @throws DOMException DOCUMENT ME!
     */
    public MathMLContentElement insertComponent(MathMLContentElement newComponent, int index) throws DOMException;

    /**
     * DOCUMENT ME!
     *
     * @param newComponent DOCUMENT ME!
     * @param index        DOCUMENT ME!
     * @return DOCUMENT ME!
     * @throws DOMException DOCUMENT ME!
     */
    public MathMLContentElement setComponent(MathMLContentElement newComponent, int index) throws DOMException;

    /**
     * DOCUMENT ME!
     *
     * @param index DOCUMENT ME!
     * @throws DOMException DOCUMENT ME!
     */
    public void deleteComponent(int index) throws DOMException;

    /**
     * DOCUMENT ME!
     *
     * @param index DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public MathMLContentElement removeComponent(int index);
}

;
