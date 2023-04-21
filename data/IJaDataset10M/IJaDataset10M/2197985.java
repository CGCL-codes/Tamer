package org.itemscript.schema;

import java.util.List;
import org.itemscript.core.JsonSystem;
import org.itemscript.core.Params;
import org.itemscript.core.exceptions.ItemscriptError;
import org.itemscript.core.values.JsonObject;
import org.itemscript.core.values.JsonValue;
import org.itemscript.template.Template;
import org.itemscript.template.TemplateExec;
import org.itemscript.template.expression.Expression;
import org.itemscript.template.expression.Function;
import org.itemscript.template.expression.FunctionBase;
import org.itemscript.template.expression.FunctionFactory;
import org.itemscript.template.expression.FunctionFoundry;

/**
 * Represents a more specific validate Function used to parse user input that is provided in a String
 * into a Number. Only used to validate values against the Number or Integer Types.
 * 
 * @author Eileen Bai
 */
public class ValidateAsNumberFunction extends FunctionBase {

    /**
	 * Adds the "validateAsNumber" function into the FunctionFoundry.
	 * Therefore ensuring that this function is only intialized when it is needed.
	 */
    public static void init() {
        FunctionFoundry.put("validateAsNumber", new FunctionFactory() {

            public Function create(JsonSystem system, List<Expression> args) {
                return new ValidateAsNumberFunction(system, args);
            }
        });
    }

    Schema schema;

    /**
	 * Constructs a new ValidateAsNumberFunction.
	 * 
	 * @param system
	 * @param args - the arguments provided to "validateAsNumber."
	 */
    public ValidateAsNumberFunction(JsonSystem system, List<Expression> args) {
        super(system, args);
        schema = new Schema(system);
    }

    public JsonValue execute(TemplateExec templateExec, JsonValue context, JsonValue value) {
        if (value == null) {
            return Template.setErrorMessage(onError(ItemscriptError.internalError(this, "execute.validateAsNumber.value.was.null", value + "")));
        } else if (!(value.isString() || value.isNumber())) {
            throw ItemscriptError.internalError(this, "execute.validateAsNumber.value.was.not.a.string.or.number", value + "");
        } else if (args().size() != 1) {
            throw ItemscriptError.internalError(this, "execute.validateAsNumber.only.one.arg.allowed");
        } else {
            JsonValue typeValue = args().get(0).interpret(templateExec, context);
            Type type;
            try {
                type = schema.resolve(typeValue);
            } catch (ItemscriptError e) {
                return Template.setErrorMessage(onError(e));
            }
            if (!(type.isNumber() || type.isInteger())) {
                throw ItemscriptError.internalError(this, "execute.validateAsNumber.type.was.not.number.or.integer", value + "");
            }
            if (value.isString()) {
                double val;
                try {
                    val = Double.parseDouble(value.stringValue());
                } catch (NumberFormatException e) {
                    return Template.setErrorMessage(onError(ItemscriptError.internalError(this, "execute.validateAsNumber.value.asNumber.could.not.be.parsed.into.double", value + "")).p("value", value + ""));
                }
                value = system().createNumber(val);
            }
            try {
                schema.validate(type, value);
                return onSuccess();
            } catch (ItemscriptError e) {
                return Template.setErrorMessage(onError(e));
            }
        }
    }

    private JsonObject onError(Throwable e) {
        JsonObject errorObject = system().createObject();
        errorObject.put("valid", false);
        Params params = (Params) ((ItemscriptError) e).params();
        errorObject.put("specified", params.get("specified"));
        errorObject.put("input", params.get("input"));
        errorObject.put("value", params.get("value"));
        errorObject.put("key", params.get("key"));
        if (e instanceof ItemscriptError) {
            errorObject.put("errorMessage", ((ItemscriptError) e).key());
        }
        return errorObject;
    }

    private JsonObject onSuccess() {
        JsonObject successObject = system().createObject();
        successObject.put("valid", true);
        successObject.put("message", "validated successfully");
        return successObject;
    }
}
