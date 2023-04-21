package org.starobjects.vaadin.data;

import org.nakedobjects.applib.value.Image;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.consent.Consent;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.object.ident.title.TitleFacet;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociation;
import org.nakedobjects.metamodel.spec.feature.ParseableEntryActionParameter;

public abstract class AbstractTextParsableContent extends AbstractContent {

    public abstract void clear();

    public abstract void entryComplete();

    public Image getIconPicture(final int iconHeight) {
        return null;
    }

    public abstract boolean isEmpty();

    @Override
    public boolean isPersistable() {
        return false;
    }

    public boolean isTransient() {
        return false;
    }

    public abstract void parseTextEntry(final String entryText);

    public abstract Consent isEditable();

    @Override
    public boolean isTextParseable() {
        return true;
    }

    /**
     * @param propertyOrParamValue
     *            the target property or parameter
     * @param propertyOrParam
     *            the {@link NakedObjectAssociation} or {@link ParseableEntryActionParameter}
     * @param propertyOrParamTypeSpecification
     *            the specification of the type of the property or parameter (for fallback).
     */
    protected String titleString(final NakedObject propertyOrParamValue, final FacetHolder propertyOrParam, final FacetHolder propertyOrParamTypeSpecification) {
        TitleFacet titleFacet = propertyOrParam.getFacet(TitleFacet.class);
        if (titleFacet != null) {
            return titleFacet.title(propertyOrParamValue);
        } else {
            return propertyOrParamValue.titleString();
        }
    }
}
