package onepoint.persistence;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import onepoint.log.XLog;
import onepoint.log.XLogFactory;
import onepoint.persistence.OpUserType.FieldDescription;

public class OpPrototype extends OpType {

    private static final XLog logger = XLogFactory.getLogger(OpPrototype.class);

    private String superTypeName;

    private OpPrototype superType;

    private final Set<OpPrototype> subTypes;

    private final Map<String, OpMember> declaredMembers;

    private final Map<String, OpMember> members;

    private Map<String, OpMember> altMembers;

    private int size;

    private Integer batchSize = null;

    private String tableName = null;

    private Map<OpConstrainedMember, String> columnNameByMember;

    /**
    * List of prototypes this prototypes depends on for backup.
    */
    private List<OpPrototype> backupDependencies = null;

    private String[] implementingNames;

    private Set<OpPrototype> implementedTypes;

    private boolean interfaceType = false;

    private boolean abstractType = false;

    public OpPrototype() {
        subTypes = new HashSet<OpPrototype>();
        declaredMembers = new LinkedHashMap<String, OpMember>();
        members = new LinkedHashMap<String, OpMember>();
    }

    void addCreatedAndModifiedFields() {
        OpField field = new OpField();
        field.setName(OpSiteObject.CREATED);
        field.setMandatory(true);
        OpType type = OpTypeManager.getType("Timestamp");
        field.setTypeID(type.getID());
        field.setTypeName(type.getName());
        declaredMembers.put(OpSiteObject.CREATED, field);
        field = new OpField();
        field.setName(OpSiteObject.MODIFIED);
        field.setMandatory(true);
        type = OpTypeManager.getType("Timestamp");
        field.setTypeID(type.getID());
        field.setTypeName(type.getName());
        declaredMembers.put(OpSiteObject.MODIFIED, field);
    }

    public final void setSuperTypeName(String super_type_name) {
        superTypeName = super_type_name;
    }

    public final OpPrototype getSuperType() {
        return superType;
    }

    public final Iterator<OpPrototype> subTypes() {
        return subTypes.iterator();
    }

    public final int getSize() {
        return size;
    }

    public final int getMemberSize() {
        return members.size();
    }

    public void addDeclaredMember(OpMember member) {
        declaredMembers.put(member.getName(), member);
    }

    public final int getDeclaredSize() {
        return declaredMembers.size();
    }

    public final Iterator<OpMember> getDeclaredMembers() {
        return declaredMembers.values().iterator();
    }

    public final Map<OpMember, Boolean> getResolvedDeclaredMembers() {
        Map<OpMember, Boolean> resolved = new HashMap<OpMember, Boolean>();
        addDeclaredMembersOfType(resolved, this);
        if (getImplementedTypes() != null) {
            for (OpPrototype type : getImplementedTypes()) {
                addDeclaredMembersOfType(resolved, type);
            }
        }
        return resolved;
    }

    private void addDeclaredMembersOfType(Map<OpMember, Boolean> resolved, OpPrototype type) {
        Iterator<OpMember> mit = type.getDeclaredMembers();
        while (mit.hasNext()) {
            resolved.put(mit.next(), Boolean.valueOf(type.isInterface()));
        }
    }

    public final OpMember getDeclaredMember(String name) {
        return declaredMembers.get(name);
    }

    public final Collection<OpMember> getMembers() {
        return members.values();
    }

    public final OpMember getMember(String name) {
        return members.get(name);
    }

    @Override
    public void onRegister() {
        super.onRegister();
        if (superTypeName == null && !isInterface()) {
            addCreatedAndModifiedFields();
        }
        size = declaredMembers.size();
        implementedTypes = new HashSet<OpPrototype>();
        OpPrototype type = this;
        while (type != null) {
            if (type.implementingNames != null && type.implementingNames.length != 0) {
                for (int pos = 0; pos < type.implementingNames.length; pos++) {
                    OpType includedType = OpTypeManager.getType(type.implementingNames[pos]);
                    if (includedType instanceof OpPrototype) {
                        OpPrototype incProto = (OpPrototype) includedType;
                        implementedTypes.add(incProto);
                        incProto.subTypes.add(this);
                        for (OpMember member : incProto.getMembers()) {
                            addMember(member);
                        }
                    }
                }
            }
            type = type.getSuperType();
        }
        if (superTypeName != null) {
            OpType super_type = OpTypeManager.getType(superTypeName);
            if (!(super_type instanceof OpPrototype)) {
                ;
            }
            superType = (OpPrototype) super_type;
            if (superType == null) {
                logger.error("super type: " + superTypeName + " of type: " + this.getName() + " is null");
            }
            superType.subTypes.add(this);
            size += superType.size;
            for (OpMember member : superType.getMembers()) {
                addMember(member);
            }
        }
        for (OpMember declaredMember : declaredMembers.values()) {
            addMember(declaredMember);
        }
    }

    private void addMember(OpMember member) {
        members.put(member.getName(), member);
        updateAltNamesMap(member);
    }

    private void updateAltNamesMap(OpMember member) {
        if (member instanceof OpUserType) {
            OpUserType c = (OpUserType) member;
            Iterator<FieldDescription> fit = c.getFieldIterator();
            while (fit.hasNext()) {
                updateAltNamesMap(fit.next().getMember());
            }
        } else {
            if (member.getAltNames() != null) {
                for (String an : member.getAltNames()) {
                    if (altMembers == null) {
                        altMembers = new HashMap<String, OpMember>();
                    }
                    if (altMembers.put(an, member) != null) {
                        logger.debug("DUPLICATE alternative member " + an + " in " + getName());
                    }
                }
            }
        }
    }

    /**
    * Returns a list with this prototype's dependencies, in terms of the order in which the backup has to be done.
    *
    * @return a <code>List</code> of <code>XProptotype</code> representing the dependent prototypes.
    */
    public List<OpPrototype> getSubsequentBackupDependencies() {
        if (backupDependencies == null) {
            backupDependencies = new ArrayList<OpPrototype>();
            for (OpMember member : getMembers()) {
                if (member instanceof OpRelationship) {
                    OpRelationship relationship = (OpRelationship) member;
                    if (!relationship.getInverse() && !relationship.getRecursive() && !relationship.isTransient()) {
                        OpPrototype dependentType = OpTypeManager.getPrototype(relationship.getTypeName());
                        if (dependentType == null) {
                            logger.error("no prototype found for: " + relationship.getTypeName() + ", within: " + this.getName());
                        }
                        if (dependentType.isInterface()) {
                            Set<OpPrototype> subtypes = dependentType.getSubTypes();
                            if (subtypes != null) {
                                for (OpPrototype subtype : subtypes) {
                                    checkCircularDependencies(relationship, subtype);
                                    backupDependencies.add(subtype);
                                }
                            }
                        } else {
                            checkCircularDependencies(relationship, dependentType);
                            backupDependencies.add(dependentType);
                        }
                    }
                }
            }
        }
        return backupDependencies;
    }

    private void checkCircularDependencies(OpRelationship relationship, OpPrototype dependentType) {
        if (dependentType.getSubsequentBackupDependencies() != null && dependentType.getSubsequentBackupDependencies().contains(this)) {
            StringBuffer exceptionMessage = new StringBuffer("Detected circular dependency caused by relationships between ");
            exceptionMessage.append(this.getName());
            exceptionMessage.append("<");
            exceptionMessage.append(relationship.getName());
            exceptionMessage.append("> and ");
            exceptionMessage.append(dependentType.getName());
            throw new IllegalStateException(exceptionMessage.toString());
        }
    }

    public List<OpPrototype> getDeleteDependencies() {
        ArrayList<OpPrototype> dependencies = new ArrayList<OpPrototype>();
        Iterator it = this.getDeclaredMembers();
        while (it.hasNext()) {
            OpMember member = (OpMember) it.next();
            if (member instanceof OpRelationship) {
                OpRelationship relationship = (OpRelationship) member;
                if (relationship.getInverse() && (OpRelationship.CASCADE_ALL.equals(relationship.getCascadeMode()) || OpRelationship.CASCADE_DELETE.equals(relationship.getCascadeMode()))) {
                    OpPrototype dependentType = OpTypeManager.getPrototype(relationship.getTypeName());
                    dependencies.add(dependentType);
                }
            }
        }
        return dependencies;
    }

    public List<OpPrototype> getNonInverseNonRecursiveDependencies() {
        ArrayList<OpPrototype> dependencies = new ArrayList<OpPrototype>();
        Iterator it = this.getDeclaredMembers();
        while (it.hasNext()) {
            OpMember member = (OpMember) it.next();
            if (member instanceof OpRelationship) {
                OpRelationship relationship = (OpRelationship) member;
                if (!relationship.getInverse() && !relationship.getRecursive() && !relationship.isTransient()) {
                    OpPrototype dependentType = OpTypeManager.getPrototype(relationship.getTypeName());
                    dependencies.add(dependentType);
                }
            }
        }
        return dependencies;
    }

    /**
    * Extends this prototype with the information from the parent prototype (extension means
    * copying all non-conflicting members.
    * @param parentPrototype a <code>OpPrototype</code> representing the parent
    * prototype.
    */
    public void extend(OpPrototype parentPrototype) {
        Iterator<OpMember> parentMemebersIt = parentPrototype.getDeclaredMembers();
        while (parentMemebersIt.hasNext()) {
            OpMember parentMember = parentMemebersIt.next();
            if (this.declaredMembers.get(parentMember.getName()) == null) {
                this.declaredMembers.put(parentMember.getName(), parentMember);
            }
        }
    }

    /**
    * Returns the recursive relationship this prototype has, or null if it doesn't have any (there shouldn't be prototypes with more than 1 recursive relationships)
    * @return a <code>OpRelationship</code> or <code>null</code>.
    */
    public List<OpRelationship> getRelationships() {
        List<OpRelationship> ret = new LinkedList<OpRelationship>();
        for (OpMember member : members.values()) {
            if (member instanceof OpRelationship) {
                ret.add((OpRelationship) member);
            }
        }
        return ret;
    }

    /**
    * Returns the recursive relationship this prototype has, or null if it doesn't have any (there shouldn't be prototypes with more than 1 recursive relationships)
    * @return a <code>OpRelationship</code> or <code>null</code>.
    */
    public List<OpRelationship> getRecursiveRelationships() {
        List<OpRelationship> ret = new LinkedList<OpRelationship>();
        for (OpMember member : members.values()) {
            if (member instanceof OpRelationship && ((OpRelationship) member).getRecursive()) {
                ret.add((OpRelationship) member);
            }
        }
        return ret;
    }

    /**
    * Checks whether the protoype contains the given memeber as a declared member.
    * @param member a <code>OpMember</code> instance.
    * @return <code>true</code> if the prototype contains as a delcared member the
    * given member.
    */
    public boolean containsDeclaredMember(OpMember member) {
        return declaredMembers.containsKey(member.getName());
    }

    /**
    * Gets the batch-size for the prototype.
    *
    * @return a <code>Integer</code> the batch size of the prototype
    */
    public Integer getBatchSize() {
        return batchSize;
    }

    /**
    * Sets the batch size for the prototype
    *
    * @param batchSize an <code>Integer</code> the batch-size of the prototype.
    */
    public void setBatchSize(Integer batchSize) {
        this.batchSize = batchSize;
    }

    @Override
    public String toString() {
        return getName();
    }

    /**
    * @param values
    * @pre
    * @post
    */
    public void setImplementingNames(String[] implementingNames) {
        this.implementingNames = implementingNames;
    }

    /**
    * @return the includes
    */
    public String[] getImplementingNames() {
        return implementingNames;
    }

    public Set<OpPrototype> getImplementedTypes() {
        return implementedTypes;
    }

    /**
    * @return the subTypes
    */
    public Set<OpPrototype> getSubTypes() {
        return subTypes;
    }

    /**
    * @param b
    * @pre
    * @post
    */
    public void setInterface(boolean interfaceType) {
        this.interfaceType = interfaceType;
    }

    public boolean isInterface() {
        return interfaceType;
    }

    /**
    * @return
    * @pre
    * @post
    */
    public boolean isAbstract() {
        return abstractType;
    }

    public void setAbstract(boolean abstractType) {
        this.abstractType = abstractType;
    }

    /**
    * @param prototype
    * @return
    * @pre
    * @post
    */
    public boolean isInstanceOf(OpPrototype prototype) {
        if (this.equals(prototype)) {
            return true;
        }
        Set<OpPrototype> impls = getImplementedTypes();
        if (impls != null) {
            for (OpPrototype pt : impls) {
                if (pt.isInstanceOf(prototype)) {
                    return true;
                }
            }
        }
        superType = getSuperType();
        if (superType == null) {
            return false;
        }
        return superType.isInstanceOf(prototype);
    }

    public static List<Field> getAccessibleField(Class c, List<String> names) throws NoSuchFieldException {
        List<Field> fields = new LinkedList<Field>();
        Iterator<String> nit = names.iterator();
        while (nit.hasNext()) {
            String n = nit.next();
            if (c == null) {
                throw new NoSuchFieldException(n);
            }
            Field f = getAccessibleField(c, n);
            if (f == null) {
                throw new NoSuchFieldException(n);
            }
            fields.add(f);
            c = f.getType();
        }
        return fields;
    }

    public static Field getAccessibleField(Class c, String fieldName) {
        fieldName = fieldName.substring(0, 1).toLowerCase() + fieldName.substring(1);
        Field f = null;
        Class cc = c;
        while (cc != null && f == null) {
            try {
                f = cc.getDeclaredField(fieldName);
            } catch (SecurityException e) {
                logger.warn("Could not get Field: " + fieldName + " in Class " + cc.getName());
            } catch (NoSuchFieldException e) {
                cc = cc.getSuperclass();
            }
        }
        if (f != null) {
            f.setAccessible(true);
            logger.debug("Field '" + f.getName() + "' found in " + cc.getName());
        } else {
            logger.debug("Field '" + fieldName + "' NOT found in " + c.getName());
        }
        return f;
    }

    public static Object getFieldValue(Object object, Field field) {
        if (field != null && object != null) {
            try {
                return field.get(object);
            } catch (IllegalArgumentException e) {
                logger.error("Field: Could not restore property value for " + field.getName() + " in class " + object.getClass().getName(), e);
            } catch (IllegalAccessException e) {
                logger.error("Field: Could not restore property value for " + field.getName() + " in class " + object.getClass().getName(), e);
            }
        }
        return null;
    }

    public OpMember getMemberByAltName(String altName) {
        return altMembers != null ? altMembers.get(altName) : null;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableName() {
        return tableName;
    }

    public void addMemberColumnName(OpConstrainedMember member, String columnName) {
        if (columnNameByMember == null) {
            columnNameByMember = new HashMap<OpConstrainedMember, String>();
        }
        columnNameByMember.put(member, columnName);
    }

    public String getColumnNameForMember(OpConstrainedMember member) {
        return columnNameByMember != null ? columnNameByMember.get(member) : null;
    }
}
