package org.objectwiz.security;

import org.objectwiz.PersistenceUnit;
import org.objectwiz.metadata.BusinessBean;
import org.objectwiz.metadata.BusinessMethod;
import org.objectwiz.metadata.MappedClass;
import org.objectwiz.metadata.MappedProperty;

/**
 * Security-related methods.
 *
 * @author Benoît Del Basso <benoit.delbasso at helmet.fr>
 */
public interface SecurityManager {

    public boolean isReadonly(PersistenceUnit unit);

    public Permission canReadProperty(MappedProperty property);

    public Permission canUpdateProperty(MappedProperty property);

    public Permission canViewObjects(MappedClass mc);

    public Permission canCreateObjects(MappedClass mc);

    public Permission canUpdateObjects(MappedClass mc);

    public Permission canRemoveObjects(MappedClass mc);

    public Permission canUseBean(BusinessBean bean);

    public Permission canCallMethod(BusinessMethod method);

    public boolean canReadProperty(MappedProperty property, Object obj);

    public boolean canUpdateProperty(MappedProperty property, Object obj);

    public boolean canCreateObject(MappedClass mc, Object obj);

    public boolean canViewObject(MappedClass mc, Object obj);

    public boolean canUpdateObject(MappedClass mc, Object obj);

    public boolean canRemoveObject(MappedClass mc, Object obj);

    public boolean canCallMethod(BusinessMethod method, Object[] args);
}
