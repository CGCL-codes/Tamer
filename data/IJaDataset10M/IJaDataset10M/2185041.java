package org.objectwiz.security;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import org.objectwiz.Application;
import org.objectwiz.PersistenceUnit;
import org.objectwiz.metadata.BusinessBean;
import org.objectwiz.metadata.BusinessMethod;
import org.objectwiz.metadata.MappedClass;
import org.objectwiz.metadata.MappedProperty;

/**
 * Object that gathers all the access rights for a given user
 * and a given unit. It is serializable.
 *
 * @author Benoît Del Basso <benoit.delbasso at helmet.fr>
 */
public class ApplicationAccessRights implements Serializable {

    private boolean appAdmin;

    private Map<String, Boolean> readableUnits = new HashMap();

    private Map<String, Boolean> readOnlyUnits = new HashMap();

    private Map<String, Permission> classViewPermissions = new HashMap();

    private Map<String, Permission> classCreatePermissions = new HashMap();

    private Map<String, Permission> classUpdatePermissions = new HashMap();

    private Map<String, Permission> classRemovePermissions = new HashMap();

    private Map<String, Permission> propertyReadPermissions = new HashMap();

    private Map<String, Permission> propertyUpdatePermissions = new HashMap();

    private Map<String, Permission> businessBeanPermissions = new HashMap();

    private Map<String, Permission> businessMethodPermissions = new HashMap();

    /**
     * Default public no-args constructor.
     */
    public ApplicationAccessRights() {
    }

    public ApplicationAccessRights(Application application, SecurityManager mgr) {
        appAdmin = mgr.isApplicationAdmin(application);
        for (PersistenceUnit unit : application.getPersistenceUnits().values()) {
            String puKey = unit.getName();
            readableUnits.put(puKey, mgr.canRead(unit));
            readOnlyUnits.put(puKey, mgr.isReadonly(unit));
            for (MappedClass mc : unit.getMetadata().getMappedClasses()) {
                String mKey = mc.getClassName();
                classViewPermissions.put(mKey, mgr.canViewObjects(mc));
                classCreatePermissions.put(mKey, mgr.canCreateObjects(mc));
                classUpdatePermissions.put(mKey, mgr.canUpdateObjects(mc));
                classRemovePermissions.put(mKey, mgr.canRemoveObjects(mc));
                for (MappedProperty property : mc.getMappedProperties()) {
                    String pKey = property.getName();
                    propertyReadPermissions.put(pKey, mgr.canReadProperty(property));
                    propertyUpdatePermissions.put(pKey, mgr.canUpdateProperty(property));
                }
            }
            for (BusinessBean bean : unit.getApplication().getBusinessBeans().values()) {
                businessBeanPermissions.put(bean.getBeanName(), mgr.canUseBean(bean));
                for (BusinessMethod method : bean.getMethods().values()) {
                    businessMethodPermissions.put(method.getName(), mgr.canCallMethod(method));
                }
            }
        }
    }

    public boolean isAdministrator() {
        return appAdmin;
    }

    public boolean canReadUnit(PersistenceUnit unit) {
        return retrieveBoolean(readableUnits, unit.getName());
    }

    public boolean isReadonlyUnit(PersistenceUnit unit) {
        return retrieveBoolean(readOnlyUnits, unit.getName());
    }

    public Permission canViewObjects(MappedClass mc) {
        return retrievePerm(classViewPermissions, mc.getClassName());
    }

    public Permission canCreateObjects(MappedClass mc) {
        return retrievePerm(classCreatePermissions, mc.getClassName());
    }

    public Permission canUpdateObjects(MappedClass mc) {
        return retrievePerm(classUpdatePermissions, mc.getClassName());
    }

    public Permission canRemoveObjects(MappedClass mc) {
        return retrievePerm(classRemovePermissions, mc.getClassName());
    }

    public Permission canReadProperty(MappedProperty property) {
        return retrievePerm(propertyReadPermissions, property.getName());
    }

    public Permission canUpdateProperty(MappedProperty property) {
        return retrievePerm(propertyUpdatePermissions, property.getName());
    }

    public Permission canUseBean(BusinessBean bean) {
        return retrievePerm(businessBeanPermissions, bean.getBeanName());
    }

    public Permission canCallMethod(BusinessMethod method) {
        return retrievePerm(businessMethodPermissions, method.getName());
    }

    private Permission retrievePerm(Map<String, Permission> map, String key) {
        Permission p = map.get(key);
        return p == null ? Permission.FORBIDDEN : p;
    }

    private boolean retrieveBoolean(Map<String, Boolean> map, String key) {
        Boolean b = map.get(key);
        if (b == null) return false;
        return b;
    }
}
