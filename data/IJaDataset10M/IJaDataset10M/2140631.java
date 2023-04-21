package org.nakedobjects.plugins.dndviewer.viewer.list;

import java.util.Iterator;
import java.util.List;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.collections.modify.CollectionFacet;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociation;
import org.nakedobjects.plugins.dndviewer.CollectionContent;
import org.nakedobjects.plugins.dndviewer.Content;
import org.nakedobjects.plugins.dndviewer.ViewAxis;

class HistogramAxis implements ViewAxis {

    private NakedObjectAssociation field;

    private int maxValue;

    public HistogramAxis(Content content) {
        List<? extends NakedObjectAssociation> associationList = HistogramSpecification.availableFields((CollectionContent) content);
        field = associationList.get(0);
        maxValue = 0;
        CollectionFacet collectionFacet = content.getSpecification().getFacet(CollectionFacet.class);
        Iterator<NakedObject> iterator = collectionFacet.iterator(content.getNaked());
        while (iterator.hasNext()) {
            NakedObject element = iterator.next();
            Integer integer = (Integer) field.get(element).getObject();
            maxValue = Math.max(maxValue, integer.intValue());
        }
    }

    public double getLengthFor(Content content) {
        Integer integer = (Integer) field.get(content.getNaked()).getObject();
        return integer * 1.0 / maxValue;
    }
}
