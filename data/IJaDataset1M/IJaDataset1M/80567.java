package org.itemscript.template.expression;

import org.itemscript.core.JsonSystem;
import org.itemscript.core.values.JsonNumber;
import org.itemscript.core.values.JsonValue;
import org.itemscript.template.TemplateExec;

public class NumericLiteralFunction extends FunctionBase {

    private final JsonNumber number;

    public NumericLiteralFunction(JsonSystem system, double number) {
        super(system, null);
        this.number = system.createNumber(number);
    }

    public JsonValue execute(TemplateExec template, JsonValue context, JsonValue value) {
        return number;
    }
}
