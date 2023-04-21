package org.nakedobjects.noa.adapter.value;

import org.nakedobjects.noa.adapter.NakedObject;
import org.nakedobjects.noa.facets.Facet;

public interface FloatingPointValueFacet extends Facet {

    Float floatValue(NakedObject object);

    NakedObject createValue(Float value);
}
