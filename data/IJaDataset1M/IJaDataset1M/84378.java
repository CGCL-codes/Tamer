package com.jeantessier.dependency;

import java.util.*;

public interface DependencyListener extends EventListener {

    public void beginSession(DependencyEvent event);

    public void beginClass(DependencyEvent event);

    public void dependency(DependencyEvent event);

    public void endClass(DependencyEvent event);

    public void endSession(DependencyEvent event);
}
