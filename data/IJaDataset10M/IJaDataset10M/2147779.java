package com.sun.xml.xsom.visitor;

import com.sun.xml.xsom.XSAnnotation;
import com.sun.xml.xsom.XSAttGroupDecl;
import com.sun.xml.xsom.XSAttributeDecl;
import com.sun.xml.xsom.XSAttributeUse;
import com.sun.xml.xsom.XSComplexType;
import com.sun.xml.xsom.XSFacet;
import com.sun.xml.xsom.XSIdentityConstraint;
import com.sun.xml.xsom.XSNotation;
import com.sun.xml.xsom.XSSchema;
import com.sun.xml.xsom.XSXPath;

/**
 * Visitor for {@link com.sun.xml.xsom.XSComponent}
 */
public interface XSVisitor extends XSTermVisitor, XSContentTypeVisitor {

    void annotation(XSAnnotation ann);

    void attGroupDecl(XSAttGroupDecl decl);

    void attributeDecl(XSAttributeDecl decl);

    void attributeUse(XSAttributeUse use);

    void complexType(XSComplexType type);

    void schema(XSSchema schema);

    void facet(XSFacet facet);

    void notation(XSNotation notation);

    void identityConstraint(XSIdentityConstraint decl);

    void xpath(XSXPath xp);
}
