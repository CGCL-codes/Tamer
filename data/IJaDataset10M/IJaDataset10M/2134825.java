package ant.famix.model.model22;

/**
 * A StructuralEntity represents the definition in source code of a structural entity, i.e. it
 * denotes an aspect of the state of a system. The different kinds of structural entities mainly
 * differ in lifetime: some have the same lifetime as the entity they belong to, e.g. an attribute
 * and a class, some have a lifetime that is the same as the whole system, e.g. a global variable.
 * Subclasses of this class represent different mechanisms for defining such an entity.
 * All possible variable definitions are subclasses of the class StructuralEntity.
 * StructuralEntity itself participates in the Access association.
 *
 * @author mzeibig
 * @created 08.04.2004 22:00:00
 * @since 
 * @version $Revision: 1.1 $
 */
public abstract class StructuralEntity extends Entity implements ant.famix.model.StructuralEntity {

    /**
     * Is a qualifier that via interpretation outside the model20 refers to the type of the defined
     * structure. Typically this will be a class, a pointer or a primitive type (e.g. "int" in Java).
     * declaredType is null if the return type is not known or the empty string (i.e. "") if the
     * StructuralEntity does not have a return type (for instance, the C++ void; we don�t use
     * "void", because this causes problems for languages where it is possible to define a class
     * called "void", like for instance Smalltalk and Ada). Note that this is consistent with UML
     * 1.1 [Booc96a].
     * Note that we need a language dependent interpretation to link a type name to a class
     * name, because in most OO languages, types are not always equivalent to a class. How the
     * declaredType may be recognised in source code and how the type matches to a class are
     * language dependent issue.
     */
    private String declaredType;

    /**
     * The unique name of the class that is implicit in the declaredType. The declaredType
     * might be the class itself, but might also be a pointer to a class (for instance, Class* in
     * C++) or a primitive type (such as "int" in Java), or something else depending on the
     * language. Therefore, the declaredClass will contain the name of the class which is
     * designated already by the declaredType, or the name of the class where the
     * declaredType points to, null if it is unknown if there is an implicit class in the declared
     * type, and the empty string (i.e. "") if it is known that there is no implicit class in the
     * declaredReturnType. What exactly is the relationship between declaredClass and
     * declaredType is a language-dependent issue.
     * Note that this is useful information for, among others, dependency analysis (a requirement
     * for this model20), hence the presence in this model20.
     */
    private String declaredClass;

    protected StructuralEntity(String name, String uniqueName) {
        super(name, uniqueName);
    }

    public final String getDeclaredType() {
        return declaredType;
    }

    public final void setDeclaredType(String declaredType) {
        this.declaredType = declaredType;
    }

    public final String getDeclaredClass() {
        return declaredClass;
    }

    public final void setDeclaredClass(String declaredClass) {
        this.declaredClass = declaredClass;
    }
}
