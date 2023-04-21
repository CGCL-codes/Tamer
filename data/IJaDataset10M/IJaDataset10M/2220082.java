package tests.generics;

import java.io.Serializable;

/**
 * 
 * mapping strategy is :
 * - private final fields -> private readonly
 * - public final wo override method -> not virtual
 * - public final with override method -> sealed
 *
 */
public interface InterfaceFields extends SuperInterfaceFields {

    public static final int i0 = R + 1;

    public static final int i1 = i0 + 1;
}
