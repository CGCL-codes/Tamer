package com.gorillalogic.compile.xmidoc.v2;

import org.w3c.dom.*;
import com.gorillalogic.compile.xmidoc.*;
import com.gorillalogic.compile.Constants;
import com.gorillalogic.compile.XMIImporter;
import java.util.*;

/**
 * XMI Version 1 Association Element
 */
public class V2AssociationElement extends V2XMIElement implements AssociationElement {

    public V2AssociationElement(Element el, XMIImporter imp) {
        super(el, imp);
    }

    public boolean isAssociationClass() {
        return getXMIType().equals(V2Constants.UML.ASSOCIATION_CLASS);
    }

    public String getAssociationClassName() {
        return getName();
    }

    public AssociationEndElement[] getEnds() throws XMIElementException {
        String endTag = isAssociationClass() ? V2Constants.UML.ASSOCIATION_END_TAG : V2Constants.UML.ATTRIBUTE_TAG;
        if (getExporter().equals(V2Constants.MagicDraw.EXPORTER_NAME)) {
            endTag = V2Constants.UML.ATTRIBUTE_TAG;
        }
        String endsXPath = assnAttrXPath(endTag);
        boolean secondIsNotNavigable = false;
        Element[] endEls = getElements(endsXPath);
        if (endEls.length < 1) {
            return getUsecaseActorEnds();
        } else if (endEls.length == 1) {
            if (!isAssociationClass()) {
                String xpath = assnAttrXPath(V2Constants.UML.ASSOCIATION_END_TAG);
                Element nonNav = getElement(xpath);
                if (nonNav != null) {
                    Element[] els2 = new Element[2];
                    els2[0] = endEls[0];
                    els2[1] = nonNav;
                    endEls = els2;
                    secondIsNotNavigable = true;
                }
            }
        }
        if (endEls.length == 1) {
            throw new XMIElementException(getImporter(), "can only find one end for association " + getName());
        }
        V2AssociationEndElement[] rez = new V2AssociationEndElement[endEls.length];
        for (int i = 0; i < endEls.length; i++) {
            rez[i] = new V2AssociationEndElement(endEls[i], getImporter(), this);
        }
        if (secondIsNotNavigable) {
            rez[1].setNavigability(false);
        }
        return rez;
    }

    public AssociationEndElement[] getUsecaseActorEnds() throws XMIElementException {
        String endTag = V2Constants.UML.ASSOCIATION_END_TAG;
        String endsXPath = assnAttrXPath(endTag);
        boolean secondIsNotNavigable = false;
        Element[] endEls = getElements(endsXPath);
        if (endEls.length < 1) {
            throw new XMIElementException(getImporter(), "cannot find association ends for " + "Association " + getName());
        }
        V2AssociationEndElement[] rez = new V2AssociationEndElement[endEls.length];
        for (int i = 0; i < endEls.length; i++) {
            rez[i] = new V2AssociationEndElement(endEls[i], getImporter(), this);
        }
        if (secondIsNotNavigable) {
            rez[1].setNavigability(false);
        }
        return rez;
    }

    private String assnAttrXPath(String elementTag) throws XMIElementException {
        String xpath = "//" + elementTag + "[@" + V2Constants.UML.ASSOCIATION_ATTR + "='" + getID() + "']";
        return xpath;
    }

    protected boolean isInitialValueExpressionComment(String comment) {
        boolean rez = super.isInitialValueExpressionComment(comment);
        if (rez == false) {
            if (comment.indexOf('=') != -1) {
                rez = true;
            }
        }
        return rez;
    }
}
