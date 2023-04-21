package com.google.devtools.depan.java.graph;

import com.google.devtools.depan.eclipse.utils.DottedNameTools;

/**
 * Element representing an interface in the code source. Use the fully qualified
 * name to inuquely identify this element.
 * 
 * @author ycoppel@google.com (Yohann Coppel)
 */
public class InterfaceElement extends JavaElement {

    /**
   * This interface's fully qualified name.
   */
    private final String fullyQualifiedName;

    /**
   * @param fullyQualifiedName
   */
    public InterfaceElement(String fullyQualifiedName) {
        this.fullyQualifiedName = fullyQualifiedName;
    }

    /**
   * @return the fully qualified name of this InterfaceElement.
   */
    public String getFullyQualifiedName() {
        return fullyQualifiedName;
    }

    /**
   * Uses fullyQualifiedName to create a hashCode.
   * 
   * @see java.lang.Object#hashCode()
   */
    @Override
    public int hashCode() {
        return fullyQualifiedName.hashCode();
    }

    /**
   * Two {@link InterfaceElement}s are equals iif their fullyQualifiedName are
   * equals.
   * 
   * @see java.lang.Object#equals(java.lang.Object)
   */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof InterfaceElement) {
            return ((InterfaceElement) obj).fullyQualifiedName.equals(this.fullyQualifiedName);
        }
        return super.equals(obj);
    }

    @Override
    public String getJavaId() {
        return getFullyQualifiedName();
    }

    @Override
    public String toString() {
        return "Interface " + getFullyQualifiedName();
    }

    @Override
    public String friendlyString() {
        return DottedNameTools.getFinalNameSegment(getFullyQualifiedName());
    }

    @Override
    public void accept(JavaElementVisitor visitor) {
        visitor.visitInterfaceElement(this);
    }
}
