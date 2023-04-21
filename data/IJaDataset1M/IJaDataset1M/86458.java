package org.openscience.cdk.renderer.generators.parameter;

import org.openscience.cdk.annotations.TestClass;
import org.openscience.cdk.annotations.TestMethod;
import org.openscience.cdk.renderer.generators.IGeneratorParameter;

/**
 * Abstract class to provide the base functionality for
 * {@link IGeneratorParameter} implementations.
 *
 * @cdk.module  render
 * @cdk.githash
 */
@TestClass("org.openscience.cdk.renderer.generators.parameter.AbstractGeneratorParameterTest")
public abstract class AbstractGeneratorParameter<T> implements IGeneratorParameter<T> {

    private T parameterSetting;

    /**
     * Sets the value for this parameter.
     *
     * @param value the new parameter value
     */
    @TestMethod("testValue")
    public void setValue(T value) {
        this.parameterSetting = value;
    }

    /**
     * Gets the value for this parameter. It must provide a reasonable
     * default when no other value has been set.
     *
     * @return the current parameter value
     */
    @TestMethod("testValue")
    public T getValue() {
        if (this.parameterSetting == null) return getDefault(); else return this.parameterSetting;
    }
}
