package com.koylu.caffein.validator;

import java.io.InputStream;
import org.apache.commons.digester.Digester;

public class ValidatorManager {

    private static Validator validator = new Validator();

    private static Digester digester = null;

    static {
        digester = new Digester();
        digester.addObjectCreate("validator", Validator.class);
        digester.addSetProperties("validator", "resource-bundle", "resourceBundle");
        digester.addObjectCreate("validator/rule", Rule.class);
        digester.addSetProperties("validator/rule", "name", "name");
        digester.addSetProperties("validator/rule", "class", "clazz");
        digester.addSetProperties("validator/rule", "method", "method");
        digester.addSetProperties("validator/rule", "msg", "msg");
        digester.addSetProperties("validator/rule", "resource-bundle", "resourceBundle");
        digester.addSetNext("validator/rule", "addRule");
    }

    public static Rule findRule(String name) {
        return ValidatorManager.validator.getRules().get(name);
    }

    public static synchronized void addValidator(String configFile) throws Exception {
        if (!configFile.startsWith("/")) {
            configFile = "/" + configFile;
        }
        addValidator(ValidatorManager.class.getResourceAsStream(configFile));
    }

    public static synchronized void addValidator(InputStream configStream) throws Exception {
        Validator validator = ((Validator) digester.parse(configStream));
        for (Rule rule : validator.getRules().values()) {
            if (rule.getResourceBundle() == null || "".equals(rule.getResourceBundle())) {
                rule.setResourceBundle(validator.getResourceBundle());
            }
            ValidatorManager.validator.addRule(rule);
        }
    }
}
