package net.sf.betterj.model.type.method;

import java.util.List;
import net.sf.betterj.model.parameter.IParameter;
import net.sf.betterj.model.type.IType;
import net.sf.betterj.model.type.access.IAccessAttributes;

/**
 * @author Viktor Halitsyn
 */
public abstract class AbstractMethod implements IMethod {

    protected IAccessAttributes _accessAttributes;

    protected IMethodAttributes _methodAttributes;

    protected IMethodBody _body;

    protected String _name;

    protected List<IParameter> _parameters;

    protected IType _type;

    @Override
    public IAccessAttributes getAccessAttributes() {
        return _accessAttributes;
    }

    @Override
    public IMethodAttributes getAttributes() {
        return _methodAttributes;
    }

    @Override
    public IMethodBody getBody() {
        return _body;
    }

    @Override
    public String getName() {
        return _name;
    }

    @Override
    public List<IParameter> getParameters() {
        return _parameters;
    }

    @Override
    public IType getType() {
        return _type;
    }

    @Override
    public void setAccessAttributes(IAccessAttributes value) {
        _accessAttributes = value;
    }

    @Override
    public void setAttributes(IMethodAttributes value) {
        _methodAttributes = value;
    }

    @Override
    public void setBody(IMethodBody body) {
        _body = body;
    }

    @Override
    public void setName(String value) {
        _name = value;
    }

    @Override
    public void setParameters(List<IParameter> value) {
        _parameters = value;
    }

    @Override
    public void setType(IType type) {
        _type = type;
    }
}
