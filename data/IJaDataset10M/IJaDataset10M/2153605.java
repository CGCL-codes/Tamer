package org.hl7.types;

/** This interface is defined to plug in a bunch of methods useful
    in Java. These methods are like the methods that use HL7 types
    as arguments, but they use Java builtins. That way we can speed
    up some work and it's easier for the programmer since they can
    use Java literals for those builtins.
*/
public interface Ordinal {

    BL equal(int x);

    BL equal(long x);

    BL equal(float x);

    BL equal(double x);

    BL compares(long x);

    BL lessOrEqual(int x);

    BL lessOrEqual(long x);

    BL lessOrEqual(float x);

    BL lessOrEqual(double x);
}
