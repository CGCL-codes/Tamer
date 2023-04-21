package de.srcml.uml.model;

import java.util.List;

public interface Relationship extends Element {

    public List<? extends Element> getRelatedElement();
}
