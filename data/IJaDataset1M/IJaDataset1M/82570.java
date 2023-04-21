package com.mangobop.lang;

import com.mangobop.impl.functions.SimpleFunctionId;
import com.mangobop.types.TypeManagerFactory;

/**
 * @author Stefan Meyer
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class IntegerValueStateImpl extends com.mangobop.impl.functions.StateImpl implements IntegerValueState {

    private Integer out = new Integer(0);

    public IntegerValueStateImpl() {
        super();
        SimpleFunctionId id = new SimpleFunctionId();
        id.setName("");
        id.setType(TypeManagerFactory.getInstance().getType("com.mangobop.lang.IntegerValueType"));
        id.setFunction(this);
        setValueType(TypeManagerFactory.getInstance().getType("com.mangobop.lang.IntegerValueType"));
        setId(id);
    }

    public void setOut(Integer o) {
        out = o;
    }

    public Integer getOut() {
        return out;
    }
}
