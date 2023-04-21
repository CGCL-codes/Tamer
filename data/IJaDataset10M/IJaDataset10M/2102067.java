package org.jtools.elements;

import org.jtools.feature.Named;
import org.jtools.feature.QNamed;

public interface Item extends Named, QNamed.Initialisable {

    Element getParent();

    void setParent(Element parent);

    <R, D> R accept(ElementsVisitor<R, D> visitor, D data);

    Item copy(Element parent);

    boolean equalsContent(Item with);
}
