package org.itemscript.template.expression;

import org.itemscript.core.JsonSystem;
import org.itemscript.core.url.Url;
import org.itemscript.core.values.JsonValue;
import org.itemscript.template.TemplateExec;

public class DataUrlFunction extends FunctionBase {

    public DataUrlFunction(JsonSystem system) {
        super(system, null);
    }

    public JsonValue execute(TemplateExec context, JsonValue contextVal, JsonValue value) {
        if (value.isString()) {
            String contentType = value.item().meta().getString("Content-Type");
            contentType = contentType.replaceAll(" ", "");
            if (contentType.startsWith("text")) {
                return system().createString("data:" + contentType + "," + Url.encode(value.stringValue()));
            } else {
                return system().createString("data:" + contentType + ";base64," + value.stringValue());
            }
        } else {
            return system().createNull();
        }
    }
}
