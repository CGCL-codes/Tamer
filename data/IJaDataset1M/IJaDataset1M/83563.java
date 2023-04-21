package com.koylu.caffein.model.container;

public interface ClazzContainer extends Container {

    public static Class<?>[] interfaces = new Class[] { ClazzContainer.class };

    public Object getContainerId();

    public String getContainerUniqueId();

    public boolean isContainerUpdated() throws Exception;

    public boolean isContainerIdUpdated() throws Exception;
}
