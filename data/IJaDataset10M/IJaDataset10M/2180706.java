package org.nakedobjects.noa.adapter.value;

import org.nakedobjects.noa.adapter.NakedObject;
import org.nakedobjects.noa.facets.Facet;

public interface ColorValueFacet extends Facet {

    int colorValue(NakedObject object);

    NakedObject createValue(NakedObject object, int color);
}
