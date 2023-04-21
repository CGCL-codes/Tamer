package org.dmd.dmr.shared.ldap.generated.dmo;

import java.io.Serializable;
import java.util.*;
import org.dmd.dmc.DmcAttribute;
import org.dmd.dmc.DmcNamedObjectIF;
import org.dmd.dmc.DmcSliceInfo;
import org.dmd.dmc.DmcValueException;
import org.dmd.dmc.types.FullyQualifiedName;
import org.dmd.dmr.shared.base.generated.dmo.HierarchicObjectDMO;
import org.dmd.dms.generated.dmo.MetaDMSAG;
import org.dmd.dms.generated.types.DmcTypeFullyQualifiedNameSV;
import org.dmd.dms.generated.types.DmcTypeModifierMV;
import org.dmd.dms.generated.dmo.MetaVCAG;
import org.dmd.dmc.DmcAttributeValidator;
import org.dmd.dmc.DmcObjectValidator;

/**
 * The LDAPHierarchicObject provides some additional structure and
 * conventions to the HierarchicObject to make it usable in conjunction with
 * an LDAP enabled directory server. Classes derived from
 * LDAPHierarchicObject must be defined in a schema that has been extended
 * with the LDAPSchemaExtension auxiliary class and must themselves be
 * extended with the LDAPClassExtension auxiliary class; the class must have
 * a defined naming attribute. Furthermore, naming attributes should be
 * unique for each defined class.
 * <P>
 * Generated from the dmr.ldap schema at version 0.1
 * <P>
 * This code was auto-generated by the dmogenerator utility and shouldn't be alterred manually!
 * Generated from: org.dmd.dms.util.DmoFormatter.dumpDMO(DmoFormatter.java:133)
 */
@SuppressWarnings("serial")
public class LDAPHierarchicObjectDMO extends HierarchicObjectDMO implements DmcNamedObjectIF, Serializable {

    public static final String constructionClassName = "LDAPHierarchicObject";

    static Map<Integer, HashMap<String, DmcAttributeValidator>> _AvDmAp;

    static Map<String, DmcObjectValidator> _OvDmAp;

    static {
        _AvDmAp = new HashMap<Integer, HashMap<String, DmcAttributeValidator>>();
        _OvDmAp = new HashMap<String, DmcObjectValidator>();
        _OvDmAp.put(MetaVCAG.__AttributeSetValidator.getName(), MetaVCAG.__AttributeSetValidator);
    }

    public LDAPHierarchicObjectDMO() {
        super("LDAPHierarchicObject");
    }

    protected LDAPHierarchicObjectDMO(String oc) {
        super(oc);
    }

    protected Map<Integer, HashMap<String, DmcAttributeValidator>> getAttributeValidators() {
        return (_AvDmAp);
    }

    protected Map<String, DmcObjectValidator> getObjectValidators() {
        return (_OvDmAp);
    }

    @Override
    public LDAPHierarchicObjectDMO getNew() {
        LDAPHierarchicObjectDMO rc = new LDAPHierarchicObjectDMO();
        return (rc);
    }

    @Override
    public LDAPHierarchicObjectDMO getSlice(DmcSliceInfo info) {
        LDAPHierarchicObjectDMO rc = new LDAPHierarchicObjectDMO();
        populateSlice(rc, info);
        return (rc);
    }

    public LDAPHierarchicObjectDMO(DmcTypeModifierMV mods) {
        super("LDAPHierarchicObject");
        modrec(true);
        setModifier(mods);
    }

    public LDAPHierarchicObjectDMO getModificationRecorder() {
        LDAPHierarchicObjectDMO rc = new LDAPHierarchicObjectDMO();
        rc.setFQN(getFQN());
        rc.setModifier(new DmcTypeModifierMV(MetaDMSAG.__modify));
        rc.modrec(true);
        return (rc);
    }

    public FullyQualifiedName getObjectName() {
        DmcAttribute<?> name = get(MetaDMSAG.__FQN);
        if (name != null) return ((FullyQualifiedName) name.getSV());
        return (null);
    }

    public DmcAttribute<?> getObjectNameAttribute() {
        DmcAttribute<?> name = get(MetaDMSAG.__FQN);
        return (name);
    }

    public boolean equals(Object obj) {
        if (obj instanceof LDAPHierarchicObjectDMO) {
            return (getObjectName().equals(((LDAPHierarchicObjectDMO) obj).getObjectName()));
        }
        return (false);
    }

    public int hashCode() {
        FullyQualifiedName objn = getObjectName();
        if (objn == null) return (0);
        return (objn.hashCode());
    }

    public FullyQualifiedName getFQN() {
        DmcTypeFullyQualifiedNameSV attr = (DmcTypeFullyQualifiedNameSV) get(MetaDMSAG.__FQN);
        if (attr == null) return (null);
        return (attr.getSV());
    }

    public void setFQN(FullyQualifiedName value) {
        DmcAttribute<?> attr = get(MetaDMSAG.__FQN);
        if (attr == null) attr = new DmcTypeFullyQualifiedNameSV(MetaDMSAG.__FQN);
        try {
            attr.set(value);
            set(MetaDMSAG.__FQN, attr);
        } catch (DmcValueException ex) {
            throw (new IllegalStateException("The type specific set() method shouldn't throw exceptions!", ex));
        }
    }

    public void setFQN(Object value) throws DmcValueException {
        DmcTypeFullyQualifiedNameSV attr = (DmcTypeFullyQualifiedNameSV) get(MetaDMSAG.__FQN);
        if (attr == null) attr = new DmcTypeFullyQualifiedNameSV(MetaDMSAG.__FQN);
        attr.set(value);
        set(MetaDMSAG.__FQN, attr);
    }

    public void remFQN() {
        rem(MetaDMSAG.__FQN);
    }
}