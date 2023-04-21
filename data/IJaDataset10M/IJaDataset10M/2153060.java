package com.mwaysolutions.ocm.test;

import com.mwaysolutions.ocm.OcmProperty;
import com.mwaysolutions.ocm.OcmType;

@OcmType(name = "gfr:BeanWithBeanArray")
public class BeanWithBeanArray {

    private SimpleBean[] simpleBeans;

    @OcmProperty
    public SimpleBean[] getSimpleBeans() {
        return simpleBeans;
    }

    public void setSimpleBeans(final SimpleBean[] simpleBeans) {
        this.simpleBeans = simpleBeans;
    }
}
