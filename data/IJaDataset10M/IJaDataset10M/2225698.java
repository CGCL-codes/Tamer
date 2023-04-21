package progranet.ganesa.metamodel;

import java.io.Serializable;
import java.util.Set;
import progranet.omg.core.types.Class;
import progranet.omg.core.types.Enumeration;
import progranet.omg.core.types.Property;
import progranet.omg.core.types.Type;
import progranet.omg.customlibrary.impl.Ocl;
import progranet.omg.ocl.Model;
import progranet.omg.ocl.expression.OclException;
import progranet.omg.ocl.types.CollectionType;

public class PropertyImpl extends NamedElementImpl implements Property, Serializable {

    private static final long serialVersionUID = 5980962973746911774L;

    public PropertyImpl() {
        super();
    }

    public Integer getLower() {
        return (Integer) this.get(Model.MULTIPLICITY_ELEMENT_LOWER);
    }

    public Object getUpper() {
        return this.get(Model.MULTIPLICITY_ELEMENT_UPPER);
    }

    public Class getOwner() {
        return (Class) this.get(Model.PROPERTY_CLASS);
    }

    public String getQualifiedName() {
        return this.getOwner().getQualifiedName() + "::" + this.getName();
    }

    public Ocl getOclInit() {
        return (Ocl) this.get(Model.PROPERTY_INIT);
    }

    public boolean isDefferedInit() {
        Object bool = this.get(Model.PROPERTY_DEFFERED_INIT);
        return bool != null && (Boolean) bool;
    }

    public Ocl getOclDerive() {
        return (Ocl) this.get(Model.PROPERTY_DERIVE);
    }

    public boolean isDerived() {
        return (this.getOclDerive() != null && !this.getOclDerive().isEmpty()) || this.isDerivedUnion();
    }

    public boolean isDerivedUnion() {
        Boolean bool = (Boolean) this.get(Model.PROPERTY_IS_DERIVED_UNION);
        return bool != null && bool;
    }

    public Set<Property> getSubsettedProperty() {
        return this.getAsSet(Model.PROPERTY_SUBSETTED_PROPERTY, Property.class);
    }

    public Set<Property> getRedefinedProperty() {
        return this.getAsSet(Model.PROPERTY_REDEFINED_PROPERTY, Property.class);
    }

    public boolean isCollection() {
        return !new Integer(1).equals(this.getUpper());
    }

    public boolean isRequired() {
        return this.getLower() > 0;
    }

    public boolean isEnumeration() {
        return this.getType() instanceof Enumeration;
    }

    public boolean isAssociation() {
        return this.getType() instanceof Class;
    }

    public boolean isFile() {
        return false;
    }

    public Type getType() {
        return (Type) this.get(Model.TYPED_ELEMENT_TYPE);
    }

    public Type getClassifier() {
        return Model.PROPERTY;
    }

    public Boolean isOrdered() {
        return false;
    }

    public Boolean isUnique() {
        return true;
    }

    public Property getOpposite() {
        return (Property) this.get(Model.PROPERTY_OPPOSITE);
    }

    public boolean isComposite() {
        Object o = this.get(Model.PROPERTY_ASSOCIATION_TYPE);
        return o != null && o.equals(Model.ASSOCIATION_TYPE_ENUM_COMPOSITION);
    }

    public boolean isShared() {
        Object o = this.get(Model.PROPERTY_ASSOCIATION_TYPE);
        return o.equals(Model.ASSOCIATION_TYPE_ENUM_AGGREGATION);
    }

    protected void postPrepare() {
        super.postPrepare();
        if (this.getOclInit() != null) this.getOclInit().setContextType(this.getOwner());
        if (this.getOclDerive() != null) this.getOclDerive().setContextType(this.getOwner());
    }

    public void validate() throws Exception {
        super.validate();
        if (!this.getName().matches("^[a-z][a-zA-Z_0-9]*$")) throw new OclException("Name syntax error");
        this.postPrepare();
        if (this.getOclInit() != null && this.getOclInit().getExecutable() != null) {
            Type type = this.getOclInit().getExecutable().getType();
            if (this.isCollection()) {
                if (!(type instanceof CollectionType) || ((!((CollectionType) type).getElementType().equals(this.getType())) && (!((CollectionType) type).getElementType().isSubclassOf(this.getType())))) throw new OclException("Init expression does not evaluate to property type");
            } else {
                if (type instanceof CollectionType || (!type.equals(this.getType()) && !type.isSubclassOf(this.getType()))) throw new OclException("Init expression does not evaluate to property type");
            }
        }
        if (this.getOclDerive() != null && this.getOclDerive().getExecutable() != null) {
            Type type = this.getOclDerive().getExecutable().getType();
            if (this.isCollection()) {
                if (!(type instanceof CollectionType) || ((!((CollectionType) type).getElementType().equals(this.getType())) && (!((CollectionType) type).getElementType().isSubclassOf(this.getType())))) throw new OclException("Derive expression does not evaluate to property type");
            } else {
                if (type instanceof CollectionType || (!type.equals(this.getType()) && !type.isSubclassOf(this.getType()))) throw new OclException("Derive expression does not evaluate to property type");
            }
        }
        if (this.getOclInit() != null && this.getOclInit().getException() != null) throw new OclException(this.getOclInit().getException());
        if (this.getOclDerive() != null && this.getOclDerive().getException() != null) throw new OclException(this.getOclDerive().getException());
    }
}
