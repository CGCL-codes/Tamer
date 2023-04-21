package org.apache.ws.jaxme.xs.xml.impl;

import org.apache.ws.jaxme.xs.xml.*;
import org.xml.sax.SAXException;

/** <p>Implementation of the <code>xs:extensionType</code> type, as
 * specified by:
 * <pre>
 *  &lt;xs:complexType name="extensionType"&gt;
 *    &lt;xs:complexContent&gt;
 *      &lt;xs:extension base="xs:annotated"&gt;
 *        &lt;xs:sequence&gt;
 *          &lt;xs:group ref="xs:typeDefParticle" minOccurs="0"/&gt;
 *          &lt;xs:group ref="xs:attrDecls"/&gt;
 *        &lt;/xs:sequence&gt;
 *        &lt;xs:attribute name="base" type="xs:QName" use="required"/&gt;
 *      &lt;/xs:extension&gt;
 *    &lt;/xs:complexContent&gt;
 *  &lt;/xs:complexType&gt;
 * </pre></p>
 *
 * @author <a href="mailto:joe@ispsoft.de">Jochen Wiedmann</a>
 */
public class XsTExtensionTypeImpl extends XsTAnnotatedImpl implements XsTExtensionType {

    private XsQName base;

    private final XsGTypeDefParticle particle;

    private final XsGAttrDecls attrDecls;

    protected XsTExtensionTypeImpl(XsObject pParent) {
        super(pParent);
        particle = getObjectFactory().newXsGTypeDefParticle(this);
        attrDecls = getObjectFactory().newXsGAttrDecls(this);
    }

    public void setBase(XsQName pBase) {
        base = pBase;
    }

    public void setBase(String pBase) throws SAXException {
        setBase(asXsQName(pBase));
    }

    public XsQName getBase() {
        return base;
    }

    public XsTGroupRef createGroup() {
        return particle.createGroup();
    }

    public XsTAll createAll() {
        return particle.createAll();
    }

    public XsEChoice createChoice() {
        return particle.createChoice();
    }

    public XsESequence createSequence() {
        return particle.createSequence();
    }

    public XsTTypeDefParticle getTypeDefParticle() {
        return particle.getTypeDefParticle();
    }

    public XsTAttribute createAttribute() {
        return attrDecls.createAttribute();
    }

    public XsTAttribute[] getAttributes() {
        return attrDecls.getAttributes();
    }

    public XsTAttributeGroupRef createAttributeGroup() {
        return attrDecls.createAttributeGroup();
    }

    public XsTAttributeGroupRef[] getAttributeGroups() {
        return attrDecls.getAttributeGroups();
    }

    public XsTWildcard createAnyAttribute() {
        return attrDecls.createAnyAttribute();
    }

    public XsTWildcard getAnyAttribute() {
        return attrDecls.getAnyAttribute();
    }

    public Object[] getAllAttributes() {
        return attrDecls.getAllAttributes();
    }

    public void validate() throws SAXException {
        super.validate();
        if (getBase() == null) {
            throw new NullPointerException("Missing 'base' attribute.");
        }
    }
}
