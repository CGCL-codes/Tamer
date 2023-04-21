package org.wikiup.modules.spring.beans;

import org.wikiup.core.Wikiup;
import org.wikiup.core.inf.ModelProvider;
import org.wikiup.modules.spring.WikiupSpringDynamicSingleton;

public class SpringModelProvider implements ModelProvider {

    private WikiupSpringDynamicSingleton spring = Wikiup.getModel(WikiupSpringDynamicSingleton.class);

    public <E> E getModel(Class<E> clazz) {
        return spring.getModel(clazz);
    }
}
