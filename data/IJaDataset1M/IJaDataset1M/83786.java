package org.openscience.cdk.qsar.result;

import org.openscience.cdk.annotations.TestClass;
import org.openscience.cdk.annotations.TestMethod;

/**
 * Object that provides access to the calculated descriptor value.
 *
 * @cdk.module standard
 * @cdk.githash
 */
@TestClass("org.openscience.cdk.qsar.result.BooleanResultTest")
public class BooleanResult extends BooleanResultType {

    private static final long serialVersionUID = 3746767816253035856L;

    private boolean value;

    public BooleanResult(boolean value) {
        this.value = value;
    }

    @TestMethod("testBooleanValue")
    public boolean booleanValue() {
        return this.value;
    }

    @TestMethod("testToString")
    public String toString() {
        return Boolean.toString(value);
    }
}
