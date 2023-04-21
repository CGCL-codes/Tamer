package com.newisys.langschema.jove;

import com.newisys.langschema.Name;
import com.newisys.langschema.java.JavaClass;
import com.newisys.langschema.java.JavaConstructor;
import com.newisys.langschema.java.JavaRawAbstractClass;
import com.newisys.langschema.java.JavaStructuredType;
import com.newisys.langschema.java.JavaType;
import com.newisys.langschema.java.JavaTypeVisitor;

/**
 * Represents a bit vector type of a particular size.
 * 
 * @author Trevor Robinson
 */
public final class JoveBitVectorType extends JavaRawAbstractClass implements JavaClass {

    private final int size;

    private String description;

    public JoveBitVectorType(JavaClass baseClass, int size) {
        this(baseClass, size, null);
    }

    public JoveBitVectorType(JavaClass baseClass, int size, String description) {
        super(baseClass.getSchema(), generateID(baseClass.getName(), size), baseClass.getPackage(), null);
        this.baseClass = baseClass;
        this.size = size;
        this.description = description;
    }

    private static String generateID(Name name, int size) {
        return name.getIdentifier() + "<" + size + ">";
    }

    public int getSize() {
        return size;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isSubtype(JavaType type) {
        return type instanceof JoveBitVectorType || type == getBaseClass() || super.isSubtype(type);
    }

    public boolean isStrictIntegral() {
        return true;
    }

    public JavaConstructor getConstructor(JavaType[] argTypes, JavaStructuredType typeContext) {
        return getBaseClass().getConstructor(argTypes, typeContext);
    }

    public boolean equals(Object obj) {
        return obj instanceof JoveBitVectorType && ((JoveBitVectorType) obj).size == size;
    }

    public int hashCode() {
        return getClass().hashCode() ^ (size * 43);
    }

    public void accept(JavaTypeVisitor visitor) {
        ((JoveTypeVisitor) visitor).visit(this);
    }
}
