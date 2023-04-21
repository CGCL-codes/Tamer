package pl.rmalinowski.gwtphp.mapgen;

import java.lang.reflect.Method;
import org.antlr.stringtemplate.StringTemplate;

public class TemplateMapController {

    final Class clazz;

    final StringTemplate st;

    public TemplateMapController(StringTemplate st, Class clazz) {
        super();
        this.clazz = clazz;
        this.st = st;
    }

    public TemplateMethodController[] getDeclaredMethods() {
        Method[] declaredMethods = clazz.getDeclaredMethods();
        TemplateMethodController[] controllers = new TemplateMethodController[declaredMethods.length];
        for (int i = 0; i < declaredMethods.length; i++) {
            controllers[i] = new TemplateMethodController(st, declaredMethods[i]);
        }
        return controllers;
    }

    public Class getClazz() {
        return clazz;
    }
}
