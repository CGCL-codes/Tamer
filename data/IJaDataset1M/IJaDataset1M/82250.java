package org.kwantu.m2.model;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.kwantu.persistence.AbstractPersistentObject;
import org.kwantu.m2.KwantuFaultException;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import java.util.HashSet;
import java.util.Set;

/**
 * Describes the individual business entities making up a KwantuModel.
 * Instances of this class might be Project, Deliverable, and ProjectManager.
 *
 * The hibernate strategy of hbm2ddl.auto=update is problematic when
 * inheritance changes:
 * This is an issue e.g. when
 *   1) class has neither super class nor sub classes
 *   2) There exists data for this class in the database
 *   3) The class gets a sub class (or a super class)
 * What happens then is that hibernate creates an extra column "discriminator"
 * to distinguish between classes in the same hierarchy (and in the same table),
 * see http://www.hibernate.org/hib_docs/reference/en/html/inheritance.html.
 * This column is however empty (null values) for all rows that are in the
 * table already ... resulting in a org.hibernate.WrongClassException when trying
 * to load from the database.
 *
 */
@Entity
public class KwantuClass extends AbstractPersistentObject {

    /** logging. */
    public static final Log LOG = LogFactory.getLog(KwantuClass.class);

    private KwantuBusinessObjectModel owningKwantuBusinessObjectModel;

    private String name;

    private KwantuClass kwantuSuperClass;

    private AbstractBusinessSet<KwantuClass, String> kwantuSubclasses = new AbstractBusinessSet<KwantuClass, String>() {

        @Override
        public String getKey(final KwantuClass kc) {
            return kc.getName();
        }
    };

    private AbstractBusinessSet<KwantuAttribute, String> kwantuAttributes = new AbstractBusinessSet<KwantuAttribute, String>() {

        @Override
        public String getKey(final KwantuAttribute ka) {
            return ka.getName();
        }
    };

    private AbstractBusinessSet<KwantuMethod, String> kwantuMethods = new AbstractBusinessSet<KwantuMethod, String>() {

        @Override
        public String getKey(final KwantuMethod km) {
            return km.getSignature();
        }
    };

    private AbstractBusinessSet<KwantuImport, String> kwantuImports = new AbstractBusinessSet<KwantuImport, String>() {

        @Override
        public String getKey(final KwantuImport ki) {
            return ki.getImportedClass();
        }
    };

    private AbstractBusinessSet<KwantuRelationship, String> kwantuRelationships = new AbstractBusinessSet<KwantuRelationship, String>() {

        @Override
        public String getKey(final KwantuRelationship kr) {
            return kr.getName();
        }
    };

    private AbstractBusinessSet<KwantuValidation, String> kwantuValidations = new AbstractBusinessSet<KwantuValidation, String>() {

        @Override
        public String getKey(final KwantuValidation kv) {
            return kv.getBooleanXPathExpression();
        }
    };

    private boolean showInExpertModeOnly;

    public KwantuClass() {
    }

    public KwantuClass(final String name) {
        this.name = name;
    }

    public KwantuClass(final KwantuBusinessObjectModel owningKwantuBusinessObjectModel, final String name) {
        this.owningKwantuBusinessObjectModel = owningKwantuBusinessObjectModel;
        this.name = name;
    }

    @ManyToOne
    public KwantuBusinessObjectModel getOwningKwantuBusinessObjectModel() {
        return owningKwantuBusinessObjectModel;
    }

    public void setOwningKwantuBusinessObjectModel(final KwantuBusinessObjectModel owningKwantuBusinessObjectModel) {
        this.owningKwantuBusinessObjectModel = owningKwantuBusinessObjectModel;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public boolean isShowInExpertModeOnly() {
        return showInExpertModeOnly;
    }

    public void setShowInExpertModeOnly(final boolean showInExpertModeOnly) {
        this.showInExpertModeOnly = showInExpertModeOnly;
    }

    @ManyToOne
    public KwantuClass getKwantuSuperClass() {
        return kwantuSuperClass;
    }

    public void setKwantuSuperClass(final KwantuClass superClass) {
        kwantuSuperClass = superClass;
    }

    /**
     * Update the superClass and the subclass list in synch.
     * @param superClass class which is to be the superclass of this one.
     */
    public void updateKwantuSuperClass(final KwantuClass superClass) {
        if (kwantuSuperClass == superClass) {
            return;
        }
        if (kwantuSuperClass != null) {
            kwantuSuperClass.removeKwantuSubclass(name);
            kwantuSuperClass = null;
        }
        if (superClass != null) {
            kwantuSuperClass = superClass;
            superClass.putKwantuSubclass(this);
        }
    }

    public KwantuClass findKwantuSubclass(final String kwantuClassName) {
        return kwantuSubclasses.findItem(kwantuClassName);
    }

    public KwantuClass getKwantuSubclass(final String kwantuClassName) {
        return kwantuSubclasses.getItem(kwantuClassName);
    }

    /**
     * Get transitive set of subclasses.
     * @return all subclasses of current class in a hibernate-detached HashSet
     */
    @Transient
    public Set<KwantuClass> getKwantuSubclasses() {
        HashSet<KwantuClass> subClasses = new HashSet<KwantuClass>();
        for (KwantuClass kc : kwantuSubclasses.getBusinessSet()) {
            subClasses.addAll(kc.getKwantuSubclasses());
        }
        subClasses.addAll(kwantuSubclasses.getBusinessSet());
        return subClasses;
    }

    @OneToMany(mappedBy = "kwantuSuperClass", cascade = CascadeType.ALL)
    public Set<KwantuClass> getDeclaredKwantuSubclasses() {
        return kwantuSubclasses.getBusinessSet();
    }

    public void setDeclaredKwantuSubclasses(final Set<KwantuClass> aKwantuSubclasses) {
        this.kwantuSubclasses.setBusinessSet(aKwantuSubclasses);
    }

    public void putKwantuSubclass(final KwantuClass kwantuClass) {
        kwantuSubclasses.addItem(kwantuClass);
        LOG.info("Kwantu class " + kwantuClass.getName() + " made subclass of" + getName() + ".");
    }

    public void removeKwantuSubclass(final String kwantuClassName) {
        kwantuSubclasses.removeItemByKey(kwantuClassName);
    }

    public KwantuAttribute findDeclaredKwantuAttribute(final String kwantuAttributeName) {
        return kwantuAttributes.findItem(kwantuAttributeName);
    }

    public KwantuAttribute getDeclaredKwantuAttribute(final String kwantuAttributeName) {
        return kwantuAttributes.getItem(kwantuAttributeName);
    }

    public KwantuAttribute findKwantuAttribute(final String name) {
        KwantuAttribute a = findDeclaredKwantuAttribute(name);
        if (a != null) return a;
        if (a == null && getKwantuSuperClass() != null) {
            return getKwantuSuperClass().findKwantuAttribute(name);
        }
        return null;
    }

    /**
     * Get all KwantuAttributes of this KwantuClass including those of superclasses.
     * @return A database-detached list of attributes including inherited ones.
     */
    @Transient
    public Set<KwantuAttribute> getKwantuAttributes() {
        HashSet<KwantuAttribute> attributes = new HashSet<KwantuAttribute>();
        if (getKwantuSuperClass() != null) {
            LOG.debug("Fetching superclass attributes from " + getKwantuSuperClass().getName());
            attributes.addAll(getKwantuSuperClass().getKwantuAttributes());
        }
        attributes.addAll(getDeclaredKwantuAttributes());
        return attributes;
    }

    @OneToMany(mappedBy = "owningKwantuClass", cascade = CascadeType.ALL)
    @Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
    public Set<KwantuAttribute> getDeclaredKwantuAttributes() {
        return kwantuAttributes.getBusinessSet();
    }

    public void setDeclaredKwantuAttributes(final Set<KwantuAttribute> kAttributes) {
        this.kwantuAttributes.setBusinessSet(kAttributes);
    }

    public KwantuAttribute createKwantuAttribute(final String kwantuAttributeName, final KwantuAttribute.Type type) {
        return kwantuAttributes.addItem(new KwantuAttribute(this, kwantuAttributeName, type));
    }

    public void deleteKwantuAttribute(final String kwantuAttributeName) {
        kwantuAttributes.removeItemByKey(kwantuAttributeName);
    }

    @Deprecated
    public KwantuClass addKwantuAttributes(final KwantuAttribute... attributes) {
        for (KwantuAttribute att : attributes) {
            att.setOwningKwantuClass(this);
            getDeclaredKwantuAttributes().add(att);
        }
        return this;
    }

    @Deprecated
    public KwantuClass addKwantuAttributesAsArray(final KwantuAttribute[] attributes) {
        for (KwantuAttribute att : attributes) {
            att.setOwningKwantuClass(this);
            getDeclaredKwantuAttributes().add(att);
        }
        return this;
    }

    public KwantuClass addAttribute(final String name, final KwantuAttribute.Type type, final String label) {
        KwantuAttribute att = createKwantuAttribute(name, type);
        att.setLabel(label);
        return this;
    }

    public KwantuMethod findKwantuMethodByName(final String name) {
        for (KwantuMethod m : kwantuMethods.getBusinessSet()) {
            if (m.getMethodName().equals(name)) {
                return m;
            }
        }
        return null;
    }

    public KwantuMethod findKwantuMethod(final String signature) {
        return kwantuMethods.findItem(signature);
    }

    public KwantuMethod getKwantuMethod(final String signature) {
        return kwantuMethods.getItem(signature);
    }

    public KwantuMethod findKwantuMethod(final long tableId) {
        if (tableId < 1) {
            throw new KwantuFaultException("Invalid tableId " + tableId);
        }
        for (KwantuMethod m : kwantuMethods.getBusinessSet()) {
            if (tableId == m.getTableId()) {
                return m;
            }
        }
        return null;
    }

    /**
     *  Get transitive set of KwantuMethods.
     * @return set of KwantuMethods, including inherited ones, detached
     * from the database.
     */
    @Transient
    public Set<KwantuMethod> getKwantuMethods() {
        HashSet<KwantuMethod> methods = new HashSet<KwantuMethod>();
        if (getKwantuSuperClass() != null) {
            methods.addAll(getKwantuSuperClass().getKwantuMethods());
        }
        methods.addAll(getDeclaredKwantuMethods());
        return methods;
    }

    @Deprecated
    public KwantuClass addKwantuMethods(final KwantuMethod... methods) {
        for (KwantuMethod method : methods) {
            method.setOwningKwantuClass(this);
            getDeclaredKwantuMethods().add(method);
        }
        return this;
    }

    @Deprecated
    public KwantuClass addKwantuMethodsAsArray(final KwantuMethod[] methods) {
        for (KwantuMethod method : methods) {
            method.setOwningKwantuClass(this);
            getDeclaredKwantuMethods().add(method);
        }
        return this;
    }

    public KwantuClass addMethod(final String signature, final String body) {
        createKwantuMethod(signature, body);
        return this;
    }

    /**
     * Get set of KwantuMethods defined in this class.
     * @return KwantuMethods of class.
     */
    @OneToMany(mappedBy = "owningKwantuClass", cascade = CascadeType.ALL)
    @Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
    public Set<KwantuMethod> getDeclaredKwantuMethods() {
        return kwantuMethods.getBusinessSet();
    }

    public void setDeclaredKwantuMethods(final Set<KwantuMethod> kMethods) {
        this.kwantuMethods.setBusinessSet(kMethods);
    }

    public KwantuMethod createKwantuMethod(final String signature, final String body) {
        LOG.info(getName() + " adding method " + signature);
        return kwantuMethods.addItem(new KwantuMethod(this, signature, body));
    }

    /** We have this method primarily for not losing the tableId when composing 
     * an effective model. In general, when intending to persist the KwantuMethod
     * using hibernate later on, copying the tableId from one instance to another
     * will cause problems/exceptions.
     */
    public KwantuMethod createKwantuMethod(final String signature, final String body, final long tableId) {
        KwantuMethod newMethod = createKwantuMethod(signature, body);
        newMethod.setTableId(tableId);
        return newMethod;
    }

    public KwantuMethod findKwantuMethodByTableId(final long tableId) {
        for (KwantuMethod m : kwantuMethods.getBusinessSet()) {
            if (tableId == m.getTableId()) {
                return m;
            }
        }
        return null;
    }

    public KwantuMethod updateKwantuMethod(final KwantuMethod m, final String signature, final String body) {
        m.setSignature(signature);
        m.setBody(body);
        LOG.info("Updated method " + m.getTableId() + " of Class " + name + ".");
        return m;
    }

    public void deleteKwantuMethod(final String signature) {
        kwantuMethods.removeItemByKey(signature);
    }

    @OneToMany(mappedBy = "owningKwantuClass", cascade = CascadeType.ALL)
    @Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
    public Set<KwantuImport> getKwantuImports() {
        return kwantuImports.getBusinessSet();
    }

    public void setKwantuImports(final Set<KwantuImport> kwantuImports) {
        this.kwantuImports.setBusinessSet(kwantuImports);
    }

    public KwantuImport findKwantuImport(final String importedClass) {
        return kwantuImports.findItem(importedClass);
    }

    public KwantuImport createKwantuImport(final String importedClass) {
        return kwantuImports.addItem(new KwantuImport(this, importedClass));
    }

    /** Utility method for generating test data. */
    public KwantuClass addKwantuImports(KwantuImport... imports) {
        for (KwantuImport imp : imports) {
            addKwantuImport(imp);
        }
        return this;
    }

    public KwantuClass addKwantuImport(KwantuImport imp) {
        imp.setOwningKwantuClass(this);
        getKwantuImports().add(imp);
        return this;
    }

    public KwantuClass addKwantuImport(String s) {
        KwantuImport imp = new KwantuImport(s);
        return addKwantuImport(imp);
    }

    public KwantuRelationship findKwantuRelationship(final String kwantuRelationshipName) {
        return kwantuRelationships.findItem(kwantuRelationshipName);
    }

    public KwantuRelationship getKwantuRelationship(final String kwantuRelationshipName) {
        return kwantuRelationships.getItem(kwantuRelationshipName);
    }

    /**
     * Get all relationships of this class and also of any superclasses.
     * Note: the returned list is detached from the database.
     * @return  a list of relationships including those inherited from superclasses.
     *
     */
    @Transient
    public Set<KwantuRelationship> getKwantuRelationships() {
        HashSet<KwantuRelationship> relationships = new HashSet<KwantuRelationship>();
        if (getKwantuSuperClass() != null) {
            LOG.debug("Fetching superclass relationships from " + getKwantuSuperClass().getName());
            relationships.addAll(getKwantuSuperClass().getKwantuRelationships());
        }
        relationships.addAll(getDeclaredKwantuRelationships());
        return relationships;
    }

    @OneToMany(mappedBy = "owningKwantuClass", cascade = CascadeType.ALL)
    @Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
    public Set<KwantuRelationship> getDeclaredKwantuRelationships() {
        return kwantuRelationships.getBusinessSet();
    }

    public void setDeclaredKwantuRelationships(final Set<KwantuRelationship> kRelationships) {
        this.kwantuRelationships.setBusinessSet(kRelationships);
    }

    public KwantuRelationship updateKwantuRelationship(final KwantuRelationship kwantuRelationship, final String relationshipName, final KwantuClass relationshipTo, final KwantuRelationship.Cardinality cardinality, final String inverseRelationshipName, final boolean owner) {
        KwantuRelationship kr = kwantuRelationship;
        if (kr.getRelationshipToKwantuClass().equals(relationshipTo)) {
            kr.setName(relationshipName);
            kr.setCardinality(cardinality);
            kr.getInverseKwantuRelationship().setName(inverseRelationshipName);
            kr.setOwner(owner);
        } else {
            deleteKwantuRelationship(kr.getName());
            kr = createKwantuRelationship(relationshipName, relationshipTo, cardinality, inverseRelationshipName, owner);
        }
        return kr;
    }

    public KwantuRelationship createKwantuRelationship(final String relationshipName, final KwantuClass relationshipTo, final KwantuRelationship.Cardinality cardinality, final String inverseRelationshipName, final boolean owner) {
        KwantuRelationship relationship = new KwantuRelationship(this, relationshipName, relationshipTo, cardinality, owner);
        relationship.setPrimaryRelationship(true);
        KwantuRelationship inverseRelationship = new KwantuRelationship(relationshipTo, inverseRelationshipName, this, KwantuRelationship.Cardinality.ONE, false);
        inverseRelationship.setPrimaryRelationship(false);
        relationship.setInverseKwantuRelationship(inverseRelationship);
        inverseRelationship.setInverseKwantuRelationship(relationship);
        relationshipTo.putKwantuRelationship(inverseRelationship);
        putKwantuRelationship(relationship);
        LOG.info("kwantu relationship " + relationshipName + " and " + inverseRelationshipName + " created.");
        return relationship;
    }

    protected void putKwantuRelationship(final KwantuRelationship relationship) {
        kwantuRelationships.addItem(relationship);
    }

    /**
     * Delete the relationship and the corresponding inverse relationship at the same time.
     * @param kwantuRelationshipName  Name of the relationship to be dropped.
     */
    public void deleteKwantuRelationship(final String kwantuRelationshipName) {
        KwantuRelationship r = getKwantuRelationship(kwantuRelationshipName);
        kwantuRelationships.removeItem(r);
        KwantuRelationship inverse = r.getInverseKwantuRelationship();
        KwantuClass relationshipToKwantuClass = inverse.getOwningKwantuClass();
        if (relationshipToKwantuClass.findKwantuRelationship(inverse.getName()) != null) {
            relationshipToKwantuClass.deleteKwantuRelationship(inverse.getName());
        }
        LOG.info("Kwantu Relationship " + kwantuRelationshipName + " removed from class " + name + ".");
    }

    @OneToMany(mappedBy = "owningKwantuClass", cascade = CascadeType.ALL)
    @Fetch(FetchMode.SUBSELECT)
    @Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
    public Set<KwantuValidation> getKwantuValidations() {
        return kwantuValidations.getBusinessSet();
    }

    public void setKwantuValidations(final Set<KwantuValidation> kwantuValidations) {
        this.kwantuValidations.setBusinessSet(kwantuValidations);
    }

    public KwantuValidation findKwantuValidation(final String booleanXPathExpression) {
        return kwantuValidations.findItem(booleanXPathExpression);
    }

    public KwantuValidation createKwantuValidation(final String booleanXPathExpression, final String errorMessage) {
        return kwantuValidations.addItem(new KwantuValidation(this, booleanXPathExpression, errorMessage));
    }

    public KwantuClass addValidation(String booleanXPathExpression, String errorMessage) {
        createKwantuValidation(booleanXPathExpression, errorMessage);
        return this;
    }

    public void deleteKwantuValidation(KwantuValidation kwantuValidation) {
        getKwantuValidations().remove(kwantuValidation);
    }

    /** determine the package name of the java class that can be generated from
     * this KwantuClass.
     *
     * @return name of the java package for this KwantuClass
     */
    @Transient
    public String getJavaPackageName(String groupId, String javaArtifactId) {
        return groupId + "." + javaArtifactId + ".model";
    }

    /** determines the fully qualified name of the java class that can be generated
     * from this KwantuClass.
     * @param groupId   package name prefix.
     * @param javaArtifactId main keyword in package name.
     * @return fully qualified name of the java class for this KwantuClass.
     */
    @Transient
    public String getJavaQualifiedName(String groupId, String javaArtifactId) {
        return getJavaPackageName(groupId, javaArtifactId) + "." + getName();
    }

    /** Find out if this class is owned by any other class by surveying all
     *  relationships, including those of superclasses.
     * @return the KwantuRelationship which owns every instance of this KwantuClass.
     */
    @Transient
    public final KwantuRelationship getOwningKwantuRelationship() {
        for (KwantuRelationship r : getKwantuRelationships()) {
            if (r.getInverseKwantuRelationship().isOwner()) {
                return r;
            }
        }
        return null;
    }

    private static final String[] requiredImportClassNames = new String[] { "org.kwantu.persistence.AbstractPersistentObject", "org.kwantu.m2.KwantuItemAlreadyExistsException", "org.kwantu.m2.model.AbstractApplicationController", "org.hibernate.annotations.Cascade", "javax.persistence.CascadeType", "javax.persistence.Entity", "javax.persistence.ManyToOne", "javax.persistence.OneToMany", "javax.persistence.OneToOne", "javax.persistence.Transient", "javax.persistence.Lob", "java.util.Collections", "java.util.HashMap", "java.util.HashSet", "java.util.Set" };

    public static final String[] getJavaCodeRequiredImportClassNames() {
        return requiredImportClassNames;
    }

    public static boolean isRequiredImportClass(String className) {
        for (String requiredImportClass : getJavaCodeRequiredImportClassNames()) {
            if (className.equals(requiredImportClass)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Convert class definition into Java source code.
     * @param groupId   package name prefix.
     * @param javaArtifactId main keyword in package name.
     * @return java source code describing a class.
     */
    @Transient
    public String getJavaCode(String groupId, String javaArtifactId) {
        StringBuilder sb = new StringBuilder();
        sb.append("\npackage ");
        sb.append(getJavaPackageName(groupId, javaArtifactId));
        sb.append(";\n\n");
        for (String importName : getJavaCodeRequiredImportClassNames()) {
            sb.append("import ");
            sb.append(importName);
            sb.append(";\n");
        }
        if (getKwantuImports().size() > 0) {
            sb.append("\n// additional imports\n");
            for (KwantuImport kwantuImport : getKwantuImports()) {
                sb.append("import ");
                sb.append(kwantuImport.getImportedClass());
                sb.append(";\n");
            }
        }
        sb.append("\n/**\n");
        sb.append(" *  Kwantu generated code. \n");
        sb.append(" */\n");
        sb.append("@Entity\n");
        sb.append("public class ");
        sb.append(name);
        sb.append(" extends ");
        if (kwantuSuperClass == null) {
            sb.append("AbstractPersistentObject");
        } else {
            sb.append(kwantuSuperClass.getName());
        }
        sb.append("  {\n\n");
        for (KwantuAttribute a : getDeclaredKwantuAttributes()) {
            sb.append(a.getJavaCodeDefinition());
        }
        for (KwantuRelationship r : getDeclaredKwantuRelationships()) {
            sb.append(r.getJavaCodeDefinition());
        }
        KwantuRelationship owning = getOwningKwantuRelationship();
        if (owning == null && kwantuSuperClass == null) {
            sb.append("    // Make controller transient to prevent it being serialized\n");
            sb.append("    private transient AbstractApplicationController controller;\n");
        }
        sb.append("\n");
        sb.append("    public " + getName() + "() {\n");
        if (kwantuSuperClass != null) {
            sb.append("        super();\n");
        }
        sb.append("    }\n");
        if (owning != null) {
            if (isAManyToManyLink()) {
                createManyToManyLinkConstructor(sb);
            } else {
                createOwnedClassConstructor(sb);
            }
        }
        sb.append("\n\n");
        if (kwantuSuperClass == null) {
            sb.append("    private static AbstractApplicationController _controller;\n");
            sb.append("    public static AbstractApplicationController getController() {\n");
            sb.append("        return _controller;\n");
            sb.append("    }\n");
            sb.append("    public static void setController(AbstractApplicationController c) {\n");
            sb.append("        _controller = c;\n");
            sb.append("    }\n\n");
        }
        for (KwantuAttribute a : getDeclaredKwantuAttributes()) {
            sb.append(a.getJavaCodeGetter());
            sb.append("\n");
            sb.append(a.getJavaCodeSetter());
            sb.append("\n");
        }
        for (KwantuRelationship r : getDeclaredKwantuRelationships()) {
            sb.append(r.getJavaCodeGetter());
            sb.append("\n");
            sb.append(r.getJavaCodeSetter());
            if ((!r.isOwner()) && r.isPrimaryRelationship()) {
                sb.append("\n");
                r.getJavaHelperMethods(sb);
            }
            sb.append("\n");
        }
        for (KwantuMethod m : getDeclaredKwantuMethods()) {
            sb.append(m.getJavaCode());
            sb.append("\n");
        }
        sb.append("}\n");
        return sb.toString();
    }

    @Transient
    public boolean isAManyToManyLink() {
        if (getKwantuAttributes().size() > 0) {
            return false;
        }
        if (getKwantuMethods().size() > 0) {
            return false;
        }
        if (getKwantuRelationships().size() != 2) {
            return false;
        }
        boolean isOwned = false;
        boolean isNotOwned = false;
        for (KwantuRelationship rel : getKwantuRelationships()) {
            if (rel.getInverseKwantuRelationship().getCardinality() != KwantuRelationship.Cardinality.MANY) {
                return false;
            }
            if (rel.getInverseKwantuRelationship().isOwner()) {
                isOwned = true;
            } else {
                isNotOwned = true;
            }
        }
        if (!(isOwned && isNotOwned)) {
            return false;
        }
        return true;
    }

    @Transient
    private boolean isSuperClassOwned() {
        if (kwantuSuperClass == null) {
            return false;
        }
        if (getOwningKwantuRelationship().getInverseKwantuRelationship().getRelationshipToKwantuClass().equals(this)) {
            return false;
        }
        return true;
    }

    private void createOwnedClassConstructor(StringBuilder sb) {
        KwantuRelationship owning = getOwningKwantuRelationship();
        sb.append("    public " + getName() + "(" + owning.getRelationshipToKwantuClass().getName() + " owner) {\n");
        if (isSuperClassOwned()) {
            sb.append("        super(owner);\n");
        } else {
            sb.append("        " + owning.getName() + " = owner;\n");
            if (owning.getInverseKwantuRelationship().getCardinality() == KwantuRelationship.Cardinality.MANY) {
                sb.append("        owner.get" + StringUtil.upshiftLeadingLetter(owning.getInverseKwantuRelationship().getName()) + "().add(this);\n");
            } else {
                sb.append("        owner.set" + StringUtil.upshiftLeadingLetter(owning.getInverseKwantuRelationship().getName()) + "(this);\n");
            }
        }
        sb.append("    }\n");
    }

    /** Create a constructor for a class whose sole function is to provide a many-to-many link
     * between two other classes.
     * @param sb put generated code into this string buffer.
     */
    private void createManyToManyLinkConstructor(StringBuilder sb) {
        KwantuRelationship owning = null;
        KwantuRelationship nonOwning = null;
        if (!isAManyToManyLink()) {
            throw new KwantuFaultException("Failed the attempting to create a constructor for a " + "many-to-many link class because the class does not meet the requirements. ");
        }
        for (KwantuRelationship rel : getKwantuRelationships()) {
            if (rel.getInverseKwantuRelationship().isOwner()) {
                owning = rel;
            } else {
                nonOwning = rel;
            }
        }
        sb.append("    public " + getName() + "(" + owning.getRelationshipToKwantuClass().getName() + " owner, " + nonOwning.getRelationshipToKwantuClass().getName() + " nonOwner) {\n");
        if (kwantuSuperClass != null) {
            sb.append("        super(owner, nonOwner);\n");
        } else {
            sb.append("        " + owning.getName() + " = owner;\n");
            sb.append("        " + nonOwning.getName() + " = nonOwner;\n");
            sb.append("        owner.get" + StringUtil.upshiftLeadingLetter(owning.getInverseKwantuRelationship().getName()) + "().add(this);\n");
            sb.append("        nonOwner.get" + StringUtil.upshiftLeadingLetter(nonOwning.getInverseKwantuRelationship().getName()) + "().add(this);\n");
        }
        sb.append("    }\n");
    }

    /** Used in picking lists. */
    @Override
    public String toString() {
        return getName();
    }

    @Transient
    public boolean isToplevelClass() {
        return getOwningKwantuBusinessObjectModel().getTopLevelKwantuClasses().contains(this);
    }

    @Transient
    public boolean isSuperClassOf(KwantuClass other) {
        KwantuClass c = other.getKwantuSuperClass();
        while (c != null) {
            if (c.equals(this)) {
                return true;
            }
            c = c.getKwantuSuperClass();
        }
        return false;
    }

    @Transient
    public boolean isAssignableFrom(KwantuClass other) {
        if (this.equals(other)) {
            return true;
        }
        if (isSuperClassOf(other)) {
            return true;
        }
        return false;
    }

    @Transient
    public boolean isInstance(Object o) {
        KwantuClass c = getOwningKwantuBusinessObjectModel().findKwantuClass(o.getClass().getSimpleName());
        return this.isAssignableFrom(c);
    }

    /** Get a list of possible candidates for superclass of this class.*/
    @Transient
    public Set<KwantuClass> getSuperclassCandidates() {
        Set<KwantuClass> set = new HashSet<KwantuClass>(getOwningKwantuBusinessObjectModel().getKwantuClasses());
        set.remove(this);
        return set;
    }
}
