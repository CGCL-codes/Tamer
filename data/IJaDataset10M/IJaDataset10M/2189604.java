package net.kortsoft.gameportlet.model.impl;

import java.util.HashMap;
import java.util.Map;
import net.kortsoft.gameportlet.model.GameInvoker;

public class GameInvokerRegistry {

    private Map<Class<? extends GameInvoker>, GameInvokerFactory> invokerFactoryMap = new HashMap<Class<? extends GameInvoker>, GameInvokerFactory>();

    public void setInvokerFactoryMap(Map<Class<? extends GameInvoker>, GameInvokerFactory> invokerFactoryMap) {
        this.invokerFactoryMap = invokerFactoryMap;
    }

    public GameInvokerFactory getGameInvokerFactoryFor(Class<? extends GameInvoker> invokerClass) {
        return (getInvokerFactoryMap().get(invokerClass));
    }

    public GameInvokerFactory put(GameInvokerFactory invokerFactory) {
        return invokerFactoryMap.put(invokerFactory.getInvokerClass(), invokerFactory);
    }

    private Map<Class<? extends GameInvoker>, GameInvokerFactory> getInvokerFactoryMap() {
        return this.invokerFactoryMap;
    }
}
