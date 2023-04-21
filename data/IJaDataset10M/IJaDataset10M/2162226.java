package com.prefabware.jmodel;

import com.prefabware.commons.QualifiedName;
import com.prefabware.jmodel.code.CodeBuffer;
import com.prefabware.jmodel.code.JCodeOptions;

/**
 * the declaration of a JType e.g. JClass, JInterface etc.
 * a JTypeDeclaration can have type arguments when it is extending a generic supertype
 * @author sisele_job
 * 
 */
public class JTypeDeclaration extends JDeclarationBase {

    public JTypeDeclaration(QualifiedName qualifiedName, JModifiers modifiers, JVisibility visibility, JType componentType) {
        super(qualifiedName, modifiers, visibility);
        this.componentType = componentType;
    }

    @Override
    public String toString() {
        return "JTypeDeclaration [componentType=" + componentType + ", name=" + qualifiedName + "]";
    }

    /**
	 * @return the dimensions of this type, if its an array
	 * 0=its not an array
	 */
    public int getArrayDim() {
        if (this.componentType != null) {
            return 1 + this.componentType.getDeclaration().getArrayDim();
        } else {
            return 0;
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((componentType == null) ? 0 : componentType.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!super.equals(obj)) return false;
        if (getClass() != obj.getClass()) return false;
        JTypeDeclaration other = (JTypeDeclaration) obj;
        if (componentType == null) {
            if (other.componentType != null) return false;
        } else if (!componentType.equals(other.componentType)) return false;
        return true;
    }

    public JType getType() {
        return this.getDeclaringType();
    }

    ;

    private JType componentType;

    public JType getComponentType() {
        return componentType;
    }

    @Override
    public String asCode(JCodeOptions options) {
        CodeBuffer buf = new CodeBuffer(options);
        buf.append(this.getVisibilityAndModifier());
        buf.append(this.getType().getJavaKeyword());
        buf.append(this.getName());
        buf.setInsertLeadingSpace(false);
        return buf.toString().trim();
    }
}
