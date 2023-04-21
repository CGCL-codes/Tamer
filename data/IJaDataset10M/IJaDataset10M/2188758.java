package nl.adaptivity.adapt;

/**
 * An interface implemented by all classes that in some way know of attribute
 * types. This can also be implemented by instances that forward it to their
 * types.
 * 
 * @author Paul de Vrieze
 * @version 1.0
 */
public interface AttributeTypeContainer {

    /**
   * Check whether the specified symbol name exists in the container. A symbol
   * can be an attribute, a function, or class or object static variables.
   * 
   * @param pName The name of the symbol.
   * @return <code>true</code> if the symbol is valid in this context.
   */
    boolean containsSymbol(final String pName);

    /**
   * Get the Attribute Type of the specified name.
   * 
   * @param pName The name of the type.
   * @return the type.
   */
    AttributeType<?> getAttributeType(final String pName);

    /**
   * Check whether the attribute with the specified name can be contained in
   * this Container.
   * 
   * @param pName the name of the attribute.
   * @return true if it is contained.
   */
    boolean containsAttributeType(final String pName);

    /**
   * Get the question type of the specified name.
   * 
   * @param pName The name of the question.
   * @return the QuestionType.
   */
    QuestionType<?> getQuestionType(final String pName);

    /**
   * Check whether this question exists in this container.
   * 
   * @param pName The name of the question.
   * @return <code>true</code> if the question is contained in this container.
   */
    boolean containsQuestionType(final String pName);
}
