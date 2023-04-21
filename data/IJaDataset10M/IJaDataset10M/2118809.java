package de.mguennewig.pobjects.metadata;

/**
 * Non scalar type denoting a join between two tables.
 *
 * <p>On the SQL level, this type is not materialized at all.  The
 * child table must follow a certain structure, depending on the type
 * of the join.  The parent table defines a <code>join</code> element
 * using one of the subclasses of <code>JoinType</code> to declare the
 * semantics of the relationship between the parent and child
 * data.</p>
 *
 */
public abstract class JoinType implements ReferenceToClass {

    /** Name of the Java type used to access values of this
   * <code>JoinType</code>. */
    public static final int TYPE_NAME = Type.TYPE_NAME;

    /** Return the name of the Java class that holds the type information of this
   * <code>Type</code> in the Java application.
   */
    public static final int CLASS_NAME = Type.CLASS_NAME;

    /** The module of the target class. */
    private final String targetModule;

    /** This is the name of the class the pointer (or foreign key) refers to. */
    private final String targetName;

    private ClassDecl targetClass;

    private String typeClass;

    /** Preinitialize join type.
   *
   * <p>Note before real usage of this type a call to
   * {@link #setTargetClass(ClassDecl)} is required.</p>
   *
   * @param targetModule Module of the target class.
   * @param targetName The name of the class the join refers to.
   * @see #setTargetClass(ClassDecl)
   */
    protected JoinType(final String targetModule, final String targetName) {
        super();
        this.targetModule = targetModule;
        this.targetName = targetName;
        this.typeClass = getClass().getName();
        this.targetClass = null;
    }

    /** Initialize join type.
   *
   * @param targetModule Module of the target class.
   * @param targetName The name of the class the join refers to.
   * @param targetClass The class declaration of the referenced table.  This
   *   must be given or <code>null</code> with a later call to
   *   {@link #setTargetClass(ClassDecl)} before any usage of this type.
   * @see #setTargetClass(ClassDecl)
   */
    public JoinType(final String targetModule, final String targetName, final ClassDecl targetClass) {
        this(targetModule, targetName);
        setTargetClass(targetClass);
    }

    public final String getTargetModule() {
        return targetModule;
    }

    public final String getTargetName() {
        return targetName;
    }

    /** Returns the {@link ClassDecl class declaration} of the referenced table.
   *
   * @see #setTargetClass
   */
    public final ClassDecl getTargetClass() {
        return targetClass;
    }

    /** Sets the {@link ClassDecl class declaration} of the referenced table.
   *
   * <p>A call to this method is only necessary if the class declaration has
   * not been passed to the constructor to solve circle problems.</p>
   *
   * <p>The referenced table must fulfill a couple of requirements depending
   * on join type.  The following requirements are common for all:</p>
   * <ul>
   * <li>requires a
   *   {@link ClassDecl#getPrimaryKeyConstraint() primary key declaration}
   *   (no {@link ClassDecl#getIdField() id field})</li>
   *
   * <li>the first primary key column must refer to the parent class and be
   *   defined as `on delete cascade'</li>
   * 
   * <li>the referenced table must have more columns then just the parent
   *   reference</li>
   * </ul>
   *
   * @throws IllegalArgumentException if the given target class does not
   *   fulfill the requirements of this join type.
   * @throws IllegalStateException if target class is already set.
   */
    public void setTargetClass(final ClassDecl targetClass) {
        if (targetClass == null || this.targetClass != null) {
            if (this.targetClass != targetClass) throw new IllegalStateException("target class already set");
            return;
        }
        final PrimaryKeyConstraint pk = targetClass.getPrimaryKeyConstraint();
        if (pk == null) {
            throw new IllegalArgumentException("Class `" + targetClass.getName() + "' has no primary key declaration");
        } else if (!(pk.getField(0).getType() instanceof RefType)) {
            throw new IllegalArgumentException("In class `" + targetClass.getName() + "', the first primary key column does not refer to the parent class");
        }
        final RefType ref = (RefType) pk.getField(0).getType();
        if (ref.getOnDelete() != RefType.ON_DELETE_CASCADE) {
            throw new IllegalArgumentException("In class `" + targetClass.getName() + "', the first primary key column is not an `on delete cascade'' reference");
        } else if (targetClass.getNumFields() < 2) {
            throw new IllegalArgumentException("Class `" + targetClass.getName() + "' has no field besides the parent reference");
        }
        this.targetClass = targetClass;
    }

    /**
   * Sets the fully qualified class name of the type class that should be
   * used instead of this one.
   */
    public final void setTypeClass(final String typeClass) {
        this.typeClass = typeClass;
    }

    /**
   * Returns different names depending on <code>variant</code> for the code
   * generator.
   *
   * @throws IllegalArgumentException if <code>variant</code> is unknown.
   */
    public String name(final int variant) {
        switch(variant) {
            case TYPE_NAME:
                throw new Error("TYPE_NAME must be returned by sub-class " + getClass().getName());
            case CLASS_NAME:
                final String className = getClass().getName();
                if (typeClass.equals(className)) {
                    final int pkgNameLen = getClass().getPackage().getName().length();
                    if (pkgNameLen > 0) return className.substring(pkgNameLen + 1);
                }
                return typeClass;
            default:
                throw new IllegalArgumentException("unknown variant value " + variant);
        }
    }
}
