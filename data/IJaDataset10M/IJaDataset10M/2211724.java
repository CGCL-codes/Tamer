package com.em.validation.client.model.generic;

import javax.validation.constraints.Min;

public interface ParentInterface {

    @Min(4)
    public int getParentInterfaceInt();
}
