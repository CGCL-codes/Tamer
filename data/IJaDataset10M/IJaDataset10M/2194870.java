package com.ht.use.attack;

import java.util.Map;
import com.framework.intercept.IResult;
import com.ht.IUser;
import com.ht.attribute.IAttribute;
import com.ht.attribute.IFunction;
import com.ht.item.weapon.IWeapon;
import com.ht.use.IAttackInvocation;

/**
 * TODO_DOCUMENT_ME
 * 
 * @since 1.0
 */
public class DAttackInvocation implements IAttackInvocation {

    private IUser _user;

    private String _uid;

    private Map<String, IAttribute> _attributes;

    private Map<String, IFunction> _functions;

    @Override
    public IResult invoke() {
        return null;
    }

    @Override
    public IUser getUser() {
        return _user;
    }

    public void setUser(IUser user) {
        _user = user;
    }

    @Override
    public Map<String, IAttribute> getAttributes() {
        return _attributes;
    }

    public void setAttributes(Map<String, IAttribute> attributes) {
        _attributes = attributes;
    }

    @Override
    public IAttribute getAttribute(String name) {
        return getAttributes().get(name);
    }

    @Override
    public Integer getAttributeValue(String name) {
        return getAttributeValue(name, null);
    }

    @Override
    public Integer getAttributeValue(String name, Integer defaultValue) {
        IAttribute att = getAttribute(name);
        if (att != null) {
            return att.getValue();
        }
        return defaultValue;
    }

    @Override
    public Map<String, IFunction> getFunctions() {
        return _functions;
    }

    public void setFunctions(Map<String, IFunction> functions) {
        _functions = functions;
    }

    @Override
    public IWeapon getItem() {
        return null;
    }

    @Override
    public String getUid() {
        return _uid;
    }

    public void setUid(String uid) {
        _uid = uid;
    }
}
