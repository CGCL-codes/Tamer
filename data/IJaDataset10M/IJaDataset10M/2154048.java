package crafty.metamodel;

import crafty.util.CopyUtil;

public abstract class MetaTypedElement extends MetaFeature {

    private TypeUsage type;

    public MetaTypedElement() {
    }

    public MetaTypedElement(String name, MetaClassifier type) {
        this(name, new TypeUsage(type));
    }

    public MetaTypedElement(String name, TypeUsage type) {
        super(name);
        this.type = type;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MetaTypedElement)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        final MetaTypedElement element = (MetaTypedElement) o;
        if (!type.equals(element.type)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int result = super.hashCode();
        result = 29 * result + type.hashCode();
        return result;
    }

    public String toString() {
        return getName() + ":" + type;
    }

    public Object copy() {
        MetaTypedElement other = (MetaTypedElement) super.copy();
        other.type = (TypeUsage) CopyUtil.copy(type);
        return other;
    }

    public TypeUsage getType() {
        return type;
    }

    public MetaTypedElement setType(TypeUsage type) {
        this.type = type;
        return this;
    }
}
