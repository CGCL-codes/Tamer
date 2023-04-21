package com.hyper9.simdk.codegen.impls;

import com.hyper9.simdk.codegen.types.MetaParam;
import com.hyper9.simdk.codegen.types.OutType;

/**
 * A meta parameter.
 * 
 * @author akutz
 * 
 */
public class MetaParamImpl implements MetaParam {

    private String name;

    private OutType outType = new OutTypeImpl();

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public OutType getOutType() {
        return this.outType;
    }

    @Override
    public void setName(String toSet) {
        this.name = toSet;
    }
}
