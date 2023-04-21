package org.jtools.tef;

import java.util.HashMap;
import java.util.Map;
import org.jpattern.factory.Factory;

public class SimpleTEFGeneratorFactory implements Factory<TEFGenerator> {

    private static final Map<Object, TEFGenerator> generatorByTask = new HashMap<Object, TEFGenerator>();

    private static TEFGenerator defaultTask;

    private static final Factory<TEFGenerator> factory = new SimpleTEFGeneratorFactory();

    public static synchronized TEFGenerator getGenerator(Object task) {
        if (task == null) {
            if (defaultTask == null) defaultTask = factory.newInstance();
            return defaultTask;
        }
        TEFGenerator result = generatorByTask.get(task);
        if (result == null) {
            result = factory.newInstance();
            generatorByTask.put(task, result);
        }
        return result;
    }

    public TEFGenerator newInstance() {
        return new SimpleTEFGenerator();
    }
}
