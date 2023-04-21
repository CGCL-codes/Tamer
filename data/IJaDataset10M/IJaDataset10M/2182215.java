package org.nakedobjects.viewer.skylark;

public interface CompositeViewSpecification extends ViewSpecification {

    CompositeViewBuilder getSubviewBuilder();
}
