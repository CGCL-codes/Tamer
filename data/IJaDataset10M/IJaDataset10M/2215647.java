package org.w3c.dom.mathML;

import org.w3c.dom.DOMException;

/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.1 $
 */
public interface MathMLContentContainer extends MathMLContentElement, MathMLContainer {

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getNBoundVariables();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public MathMLConditionElement getCondition();

    /**
     * DOCUMENT ME!
     *
     * @param condition DOCUMENT ME!
     *
     * @throws DOMException DOCUMENT ME!
     */
    public void setCondition(MathMLConditionElement condition) throws DOMException;

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public MathMLElement getOpDegree();

    /**
     * DOCUMENT ME!
     *
     * @param opDegree DOCUMENT ME!
     *
     * @throws DOMException DOCUMENT ME!
     */
    public void setOpDegree(MathMLElement opDegree) throws DOMException;

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public MathMLElement getDomainOfApplication();

    /**
     * DOCUMENT ME!
     *
     * @param domainOfApplication DOCUMENT ME!
     *
     * @throws DOMException DOCUMENT ME!
     */
    public void setDomainOfApplication(MathMLElement domainOfApplication) throws DOMException;

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public MathMLElement getMomentAbout();

    /**
     * DOCUMENT ME!
     *
     * @param momentAbout DOCUMENT ME!
     *
     * @throws DOMException DOCUMENT ME!
     */
    public void setMomentAbout(MathMLElement momentAbout) throws DOMException;

    /**
     * DOCUMENT ME!
     *
     * @param index DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public MathMLBvarElement getBoundVariable(int index);

    /**
     * DOCUMENT ME!
     *
     * @param newBVar DOCUMENT ME!
     * @param index DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws DOMException DOCUMENT ME!
     */
    public MathMLBvarElement insertBoundVariable(MathMLBvarElement newBVar, int index) throws DOMException;

    /**
     * DOCUMENT ME!
     *
     * @param newBVar DOCUMENT ME!
     * @param index DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws DOMException DOCUMENT ME!
     */
    public MathMLBvarElement setBoundVariable(MathMLBvarElement newBVar, int index) throws DOMException;

    /**
     * DOCUMENT ME!
     *
     * @param index DOCUMENT ME!
     */
    public void deleteBoundVariable(int index);

    /**
     * DOCUMENT ME!
     *
     * @param index DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public MathMLBvarElement removeBoundVariable(int index);
}

;
