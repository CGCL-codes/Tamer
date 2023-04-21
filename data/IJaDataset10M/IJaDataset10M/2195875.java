package org.formaria.oracle.forms.xml.node;

import org.formaria.oracle.forms.xml.visitor.FormsNodeVisitor;

public interface FormsNode {

    public Object accept(FormsNodeVisitor visitor, Object object);

    public Object childrenAccept(FormsNodeVisitor visitor, Object object);
}
