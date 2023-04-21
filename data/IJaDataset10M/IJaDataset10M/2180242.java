package com.yilan.context;

import com.yilan.component.runtime.RuntimeComponent;

public interface RuntimeConfiguration {

    public void connectComponent(RuntimeComponent from, RuntimeComponent to);

    public RuntimeComponent deployInstance(String id, Object instance);
}
