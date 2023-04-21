package org.objectwiz.core.facet.customization;

import org.objectwiz.core.Application;
import org.objectwiz.core.AbstractFacet;
import org.objectwiz.core.facet.persistence.PersistenceUnit;

/**
 * Default implementation of {@linlk CustomizationFacet}.
 *
 * @author Benoit Del Basso <benoit.delbasso at helmet.fr>
 */
public class CustomizationFacetImpl extends AbstractFacet<CustomizationFacet> implements CustomizationFacet {

    private PersistenceUnit customizationUnit;

    public CustomizationFacetImpl(Application application, PersistenceUnit customizationUnit) {
        super(application);
        if (customizationUnit == null) throw new NullPointerException("customizationUnit");
        this.customizationUnit = customizationUnit;
    }

    @Override
    public PersistenceUnit getCustomizationUnit() {
        return customizationUnit;
    }
}
