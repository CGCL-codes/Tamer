package org.mantikhor.llapi.impl;

import org.mantikhor.util.MantikhorUtil;
import org.mantikhor.util.MantikhorUtil.StringRepType;
import org.mantikhor.llapi.DomainURI;
import org.mantikhor.llapi.PropertyCategory;
import org.mantikhor.llapi.PropertyDefinition;

/**
 * <code>PropertyDefinitionImpl</code> is an immutable implementation of <code>PropertyDefinition</code>
 *    
 * @author Dave Winslow
 */
public class PropertyDefinitionImpl implements PropertyDefinition {

    PropertyCategory propertyCategory;

    DomainURI domainURI;

    private int hashCode;

    /**
	 * 
	 * @param propertyCategory
	 * @param domainURI
	 * @throws IllegalArgumentException if propertyCategory is null or domainURI is null
	 */
    public PropertyDefinitionImpl(PropertyCategory propertyCategory, DomainURI domainURI) throws IllegalArgumentException {
        if (propertyCategory == null) {
            throw new IllegalArgumentException("Parameter 'propertyCategory' not permitted to be null.");
        }
        if (domainURI == null) {
            throw new IllegalArgumentException("Parameter 'domainURI' not permitted to be null.");
        }
        this.propertyCategory = propertyCategory;
        this.domainURI = domainURI;
        this.hashCode = this.propertyCategory.hashCode() + this.domainURI.hashCode();
    }

    public PropertyCategory getCategory() {
        return propertyCategory;
    }

    public DomainURI getTypedefURI() {
        return domainURI;
    }

    public int compareTo(PropertyDefinition other) {
        if (other == null) {
            throw new IllegalArgumentException("Parameter 'other' not permitted to be null.");
        }
        if (other == this) {
            return 0;
        }
        if (!this.getClass().getName().equals(other.getClass().getName())) {
            throw MantikhorUtil.makeTypeMismatchException(this, other);
        }
        return this.propertyCategory.ordinal() - other.getCategory().ordinal() != 0 ? this.propertyCategory.ordinal() - other.getCategory().ordinal() : this.domainURI.compareTo(other.getTypedefURI());
    }

    @Override
    public boolean equals(Object that) {
        if (that == null) {
            return false;
        }
        if (that == this) {
            return true;
        }
        if (!this.getClass().getName().equals(that.getClass().getName())) {
            return false;
        }
        PropertyDefinitionImpl thatAsPDI = (PropertyDefinitionImpl) that;
        return this.domainURI.equals(thatAsPDI.domainURI) && (this.propertyCategory == thatAsPDI.propertyCategory);
    }

    @Override
    public int hashCode() {
        return this.hashCode;
    }

    @Override
    public String toString() {
        StringBuilder rep = new StringBuilder(2000);
        rep.append(MantikhorUtil.formatString(this, StringRepType.CLASS_NAME, new String[0]));
        rep.append(MantikhorUtil.getMarker(StringRepType.BEGIN));
        rep.append(MantikhorUtil.formatString(this.propertyCategory, StringRepType.ENUM, new String[0])).append(MantikhorUtil.getMarker(StringRepType.SEP));
        rep.append(MantikhorUtil.formatString(this.domainURI, StringRepType.DELEGATE_TO_STRING, new String[] { "DomainURI-null" }));
        rep.append(MantikhorUtil.getMarker(StringRepType.END));
        return rep.toString();
    }
}
