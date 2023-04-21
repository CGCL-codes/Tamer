package oracle.toplink.libraries.asm.tree.analysis;

/**
 * An immutable symbolic value for semantic interpretation of bytecode.
 * 
 * @author Eric Bruneton
 */
public interface Value {

    /**
   * Returns the size of this value in words.
   * 
   * @return either 1 or 2.
   */
    int getSize();

    /**
   * Compares this value with the given value.
   * 
   * @param value a value.
   * @return <tt>true</tt> if the values are equals, <tt>false</tt> otherwise.
   */
    boolean equals(Value value);
}
