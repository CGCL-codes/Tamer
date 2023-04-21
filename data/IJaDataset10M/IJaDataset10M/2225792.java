package org.pojonode.demo.model.aspects;

import org.pojonode.aop.annotations.Aspect;
import org.pojonode.aop.annotations.Property;
import org.pojonode.model.pojonode.PojoAspect;

/**
 * @author Cosmin Marginean, 13/05/2011
 */
@Aspect(name = "pnd:container")
public class Container extends PojoAspect {

    @Property(visible = false)
    private String containerId;

    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }
}
