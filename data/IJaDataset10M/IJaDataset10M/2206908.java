package net.sf.betterj.model.parameter;

import net.sf.betterj.model.type.IType;
import net.sf.betterj.model.value.IValue;

/**
 * @author Oleh Sklyarenko
 */
public interface IParameter {

    String getName();

    void setName(String value);

    IType getType();

    void setType(IType value);

    IParameterAttributes getAttributes();

    void setAttributes(IParameterAttributes value);

    void setValue(IValue value);

    public IValue getValue();
}
