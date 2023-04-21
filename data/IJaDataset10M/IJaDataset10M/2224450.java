package net.sf.rcpforms.modeladapter.path.elements;

import net.sf.rcpforms.modeladapter.path.IPropertyElement;

public abstract class AbstractElement implements IPropertyElement {

    protected transient String asString;

    protected final String identifier;

    @SuppressWarnings("hiding")
    public AbstractElement(String identifier) {
        super();
        asString = null;
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }

    /**
     * @see net.sf.rcpforms.modeladapter.path.IPropertyElement#asString()
     */
    public String asString() {
        if (asString == null) {
            asString = glueStringRepresentation();
        }
        return asString;
    }

    public boolean isSimple() {
        return false;
    }

    protected abstract String glueStringRepresentation();

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj.getClass() != getClass()) return false;
        AbstractElement other = (AbstractElement) obj;
        if (other.identifier == null && this.identifier == null) return true;
        if (!other.identifier.equals(this.identifier)) return false;
        return equalsImpl(other);
    }

    protected boolean equalsImpl(AbstractElement other) {
        return true;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return asString().hashCode();
    }
}
