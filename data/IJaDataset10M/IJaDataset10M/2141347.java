package memops.metamodel;

import java.util.*;
import memops.general.MemopsException;

@Deprecated
public class MetaClass extends MetaModelElement {

    private static final String containerType = "class";

    private static final Map allowedTags;

    private static final List metaParameterNames;

    private static final List booleanMetaParameters;

    private static final List fixedMetaParameters;

    private static final Map metaParameterDefaults;

    static {
        Map t = (Map) (TaggedValues.allowedTags.get("MetaClass"));
        allowedTags = Util.cloneMap(t);
        Object[] s1 = { isRootString, isLeafString, isAbstractString, visibilityString, isSingletonString, hasSpecialConstructorString, hasSpecialDestructorString };
        metaParameterNames = Util.copyList(s1);
        Object[] s2 = { isRootString, isLeafString, isAbstractString, isSingletonString, hasSpecialConstructorString, hasSpecialDestructorString };
        booleanMetaParameters = Util.copyList(s2);
        Object[] s3 = { isRootString, isLeafString, visibilityString, isSingletonString };
        fixedMetaParameters = Util.copyList(s3);
        Object[] k = { isRootString, isLeafString, isAbstractString, visibilityString, isSingletonString, hasSpecialConstructorString, hasSpecialDestructorString };
        Object[] v = { Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, ImpConstants.public_visibility, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE };
        metaParameterDefaults = Util.cloneMap(k, v);
    }

    public Boolean isRoot = null;

    public Boolean isLeaf = null;

    public Boolean isAbstract = null;

    public String visibility = null;

    public Boolean isSingleton = null;

    public Boolean hasSpecialConstructor = null;

    public Boolean hasSpecialDestructor = null;

    public List attributeNames = new ArrayList();

    public List attributeAllNames = new ArrayList();

    public List roleNames = new ArrayList();

    public List roleAllNames = new ArrayList();

    public List keyNames = new ArrayList();

    public List operationNames = new ArrayList();

    public List operationAllNames = new ArrayList();

    public MetaPackage _package;

    public MetaClass superclass = null;

    public List subclasses = new ArrayList();

    public MetaRole parentRole = null;

    public Map constraints = new HashMap();

    public MetaClass(MetaPackage container, Map params) throws MemopsException {
        initialiser(container, params);
        _package = container;
        _package.addClass(this);
    }

    public void addConstraint(MetaConstraint constraint) throws MemopsException {
        if (constraints.containsKey(constraint.name)) throwException("class already contains element named '" + constraint.name + "'");
        constraints.put(constraint.name, constraint);
    }

    public void setSuperclass(MetaClass superclass) throws MemopsException {
        if (this.superclass != null) throwException("attempt to override class superclass");
        if (isAbstract.booleanValue() && !superclass.isAbstract.booleanValue()) throwException("Abstract class has non-abstract superclass '" + superclass + "'");
        if (_package != superclass._package) {
            if (!superclass.isAbstract.booleanValue()) throwException("superclass '" + superclass + "' is nonabstract and from a different package");
            if (superclass.parentRole != null) throwException("superclass '" + superclass + "' has parentRole and is from a different package");
            if (!_package.canAccessPackage(superclass._package)) throwException("superclass '" + superclass + "' is from non-accessible package");
        }
        ArrayList ancestors = new ArrayList();
        MetaClass ss = superclass;
        while (ss != null) {
            ancestors.add(ss);
            ss = ss.superclass;
        }
        if (ancestors.contains(this)) throwException("Class generalises itself, maybe indirectly");
        this.superclass = superclass;
        superclass.subclasses.add(this);
    }

    public MetaClass getParentClass() {
        if (parentRole != null) return parentRole.otherClass; else return null;
    }

    public List getChildClasses() {
        ArrayList children = new ArrayList();
        Iterator iterator = roleAllNames.iterator();
        while (iterator.hasNext()) {
            String name = (String) iterator.next();
            MetaRole role = (MetaRole) elementDict.get(name);
            if (role.hierarchy == ImpConstants.child_hierarchy) children.add(role.otherClass);
        }
        return children;
    }

    public List getSubclasses() {
        return getSubclasses(true);
    }

    public List getSubclasses(boolean include_self) {
        return getSubclasses(include_self, true);
    }

    public List getSubclasses(boolean include_self, boolean recurse) {
        ArrayList subclasses = new ArrayList();
        if (include_self) subclasses.add(this);
        subclasses.addAll(this.subclasses);
        if (recurse) {
            Iterator iterator = this.subclasses.iterator();
            while (iterator.hasNext()) {
                MetaClass subclass = (MetaClass) iterator.next();
                subclasses.addAll(subclass.getSubclasses(false));
            }
        }
        return subclasses;
    }

    public List getAllSupertypes() {
        ArrayList result = new ArrayList();
        MetaClass ss = superclass;
        while (ss != null) {
            result.add(ss);
            ss = ss.superclass;
        }
        return result;
    }

    public List getNonabstractSubclasses() {
        return getNonabstractSubclasses(true);
    }

    public List getNonabstractSubclasses(boolean include_self) {
        return getNonabstractSubclasses(include_self, true);
    }

    public List getNonabstractSubclasses(boolean include_self, boolean recurse) {
        List subclasses = getSubclasses(include_self, recurse);
        ArrayList subs = new ArrayList();
        Iterator iterator = subclasses.iterator();
        while (iterator.hasNext()) {
            MetaClass subclass = (MetaClass) iterator.next();
            if (!subclass.isAbstract.booleanValue()) subs.add(subclass);
        }
        return subs;
    }

    public void addAttribute(MetaAttribute attribute) throws MemopsException {
        addElement(containerType, attribute, attributeAllNames);
        if (this == attribute.clazz) attributeNames.add(attribute.name);
    }

    public void addRole(MetaRole role) throws MemopsException {
        if (elementDict.containsKey(role.name)) {
            MetaModelElement other = (MetaModelElement) elementDict.get(role.name);
            if ((other instanceof MetaRole) && ((MetaRole) other).isAbstract.booleanValue() && !isAbstract.booleanValue()) elementDict.put(role.name, role); else throwException("class already contains element named '" + role.name + "'");
        } else {
            elementDict.put(role.name, role);
            roleAllNames.add(role.name);
        }
        if (this == role.clazz) roleNames.add(role.name);
        if (role.hierarchy == ImpConstants.parent_hierarchy) parentRole = role;
    }

    public void addOperation(MetaOperation operation) throws MemopsException {
        addElement(containerType, operation, operationAllNames);
        if (this == operation.clazz) operationNames.add(operation.name);
    }

    public void addKeyName(String name) {
        keyNames.add(name);
    }

    public void checkValid() throws MemopsException {
        super.checkValid();
    }

    public void sortElementNames() {
        sortNames(roleNames);
        sortNames(roleAllNames);
    }
}
