package org.openmobster.core.console.server;

import org.openmobster.core.common.validation.ObjectValidator;

/**
 *
 * @author openmobster@gmail.com
 */
public final class ObjectValidatorFactory {

    private static ObjectValidator singleton;

    public static ObjectValidator getInstance() {
        if (singleton == null) {
            synchronized (ObjectValidatorFactory.class) {
                if (singleton == null) {
                    singleton = new ObjectValidator();
                    singleton.setName("Console App Validator");
                    singleton.setRulesFile("META-INF/console-validationRules.xml");
                    singleton.start();
                }
            }
        }
        return singleton;
    }
}
