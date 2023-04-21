package EqualsHashcode;

/**
 */
public class D {

    /**
   * @audit HashcodeNotDeclared
   */
    public boolean equals(Object that) {
        return (this == that);
    }
}
