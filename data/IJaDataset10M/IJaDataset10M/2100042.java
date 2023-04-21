package recoder.java.reference;

import recoder.java.NonTerminalProgramElement;

/**
 * Type reference container.
 * 
 * @author <TT>AutoDoc</TT>
 */
public interface TypeReferenceContainer extends NonTerminalProgramElement {

    /**
     * Get the number of type references in this container.
     * 
     * @return the number of type references.
     */
    int getTypeReferenceCount();

    TypeReference getTypeReferenceAt(int index);
}
