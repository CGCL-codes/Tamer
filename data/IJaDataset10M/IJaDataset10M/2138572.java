package ru.scriptum.db4o.spring;

import java.util.Iterator;
import java.util.List;
import com.db4o.Db4o;
import com.db4o.config.ObjectClass;

/**
 * Class configuration for db4o.
 */
public class Db4oClassConfiguration {

    private Class clazz;

    private Boolean callConstructor;

    private Boolean cascadeOnActivate;

    private Boolean cascadeOnDelete;

    private Boolean cascadeOnUpdate;

    private Integer maxActivationDepth;

    private Integer minActivationDepth;

    private Boolean storeTransientFields;

    private Integer updateDepth;

    private List fieldConfigurations;

    public void setClassName(String className) throws ClassNotFoundException {
        this.clazz = Class.forName(className);
    }

    public void setCallConstructor(Boolean callConstructor) {
        this.callConstructor = callConstructor;
    }

    public void setCascadeOnActivate(Boolean cascadeOnActivate) {
        this.cascadeOnActivate = cascadeOnActivate;
    }

    public void setCascadeOnDelete(Boolean cascadeOnDelete) {
        this.cascadeOnDelete = cascadeOnDelete;
    }

    public void setCascadeOnUpdate(Boolean cascadeOnUpdate) {
        this.cascadeOnUpdate = cascadeOnUpdate;
    }

    public void setMaxActivationDepth(Integer maxActivationDepth) {
        this.maxActivationDepth = maxActivationDepth;
    }

    public void setMinActivationDepth(Integer minActivationDepth) {
        this.minActivationDepth = minActivationDepth;
    }

    public void setStoreTransientFields(Boolean storeTransientFields) {
        this.storeTransientFields = storeTransientFields;
    }

    public void setUpdateDepth(Integer updateDepth) {
        this.updateDepth = updateDepth;
    }

    public void setFieldConfigurations(List fieldConfigurations) {
        this.fieldConfigurations = fieldConfigurations;
    }

    public void configure() {
        ObjectClass objectClass = Db4o.configure().objectClass(clazz);
        if (callConstructor != null) objectClass.callConstructor(callConstructor.booleanValue());
        if (cascadeOnActivate != null) objectClass.cascadeOnActivate(cascadeOnActivate.booleanValue());
        if (cascadeOnDelete != null) objectClass.cascadeOnDelete(cascadeOnDelete.booleanValue());
        if (cascadeOnUpdate != null) objectClass.cascadeOnUpdate(cascadeOnUpdate.booleanValue());
        if (maxActivationDepth != null) objectClass.maximumActivationDepth(maxActivationDepth.intValue());
        if (minActivationDepth != null) objectClass.minimumActivationDepth(minActivationDepth.intValue());
        if (storeTransientFields != null) objectClass.storeTransientFields(storeTransientFields.booleanValue());
        if (updateDepth != null) objectClass.updateDepth(updateDepth.intValue());
        if (fieldConfigurations != null) {
            for (Iterator iterator = fieldConfigurations.iterator(); iterator.hasNext(); ) {
                Db4oFieldConfiguration configuration = (Db4oFieldConfiguration) iterator.next();
                configuration.configure(objectClass);
            }
        }
    }
}
