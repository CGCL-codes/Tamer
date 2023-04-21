package org.tagbox.engine.substituter;

import org.w3c.dom.Element;
import org.tagbox.engine.TagEnvironment;
import org.tagbox.engine.service.ServiceEnvironment;
import org.tagbox.xpath.primitive.Primitive;
import org.tagbox.xpath.primitive.StringPrimitive;
import org.tagbox.util.Log;

public class ServiceSubstituter implements Substituter {

    public void init(Element config) {
    }

    public Primitive getValue(String key, TagEnvironment env) {
        ServiceEnvironment senv = (ServiceEnvironment) env;
        String param = senv.getParameter(key);
        if (param == null) {
            Log.warning("undefined parameter: " + key);
            return null;
        }
        return new StringPrimitive(param);
    }
}
