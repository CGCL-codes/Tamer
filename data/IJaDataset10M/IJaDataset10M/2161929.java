package progranet.omg.cmof.impl;

import java.util.HashSet;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import progranet.omg.cmof.InstanceSpecification;
import progranet.omg.cmof.ValueSpecification;
import progranet.omg.core.types.Property;
import progranet.omg.core.types.impl.PropertyImpl;
import progranet.omg.ocl.expression.OclException;

public class ValueSpecificationImpl implements ValueSpecification {

    protected final Log logger = LogFactory.getLog(this.getClass());

    private InstanceSpecification instance;

    private PropertyImpl property;

    private Object value = null;

    private static enum states {

        NEW, PREPARED
    }

    ;

    private states state = states.NEW;

    public static ValueSpecification getInstance(PropertyImpl property, InstanceSpecification instance) {
        return new ValueSpecificationImpl(property, instance);
    }

    private ValueSpecificationImpl(PropertyImpl property, InstanceSpecification instance) {
        this.instance = instance;
        this.property = property;
    }

    public void add(Object value) {
    }

    public void remove(Object value) {
    }

    public InstanceSpecification getOwningInstance() {
        return this.instance;
    }

    public Property getProperty() {
        return this.property;
    }

    public Object getValuePre() {
        return this.get();
    }

    private Set<Property> getUnion() {
        Set<Property> result = new HashSet<Property>();
        for (Property property : this.getOwningInstance().getMetaClass().getAllAttribute()) {
            if (property.getSubsettedProperty().contains(this.getProperty())) result.add(property);
        }
        return result;
    }

    public Object get() {
        if (this.state == states.PREPARED) return this.value;
        if (this.property.isDerivedUnion()) {
            if (!this.property.isCollection()) {
                for (Property property : getUnion()) {
                    Object value = this.getOwningInstance().get(property);
                    if (value != null) this.value = value;
                }
            } else {
                Set union = new HashSet();
                for (Property property : getUnion()) {
                    Object value = this.getOwningInstance().get(property);
                    if (value != null) {
                        if (property.isCollection()) union.addAll((Set) value); else union.add(this.getOwningInstance().get(property));
                    }
                }
                this.value = union;
            }
        } else if (this.property.isDerived()) {
            try {
                this.value = this.property.getOclDerive().execute(this.getOwningInstance());
            } catch (OclException e) {
                logger.error("Evaluation error. Cannot derive Property '" + this.getProperty() + "' value for '" + this.getOwningInstance() + "'", e);
            }
        }
        this.state = states.PREPARED;
        return this.value;
    }

    public void set(Object value) {
        this.value = value;
    }

    public String toString() {
        return this.value == null ? "<null>" : this.value.toString();
    }
}
