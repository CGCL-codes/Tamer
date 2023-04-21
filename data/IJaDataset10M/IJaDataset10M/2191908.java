package org.jucetice.javascript.extensions;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jucetice.javascript.ScriptEngine;
import org.jucetice.javascript.ScriptUtils;
import org.jucetice.javascript.classes.ScriptableRequest;
import org.jucetice.javascript.classes.ScriptableResponse;
import org.jucetice.javascript.utils.Validator;
import org.jucetice.javascript.utils.json.JSONSerializer;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.Undefined;

public class ServiceExtension {

    /**
	 * Default variable and function names
	 */
    public static final String START_FUNC_NAME = "onStart";

    public static final String CUSTOM_FUNC_NAME = "onCustomValidation";

    public static final String ACTION_FUNC_NAME = "onAction";

    public static final String ERROR_FUNC_NAME = "onError";

    public static final String RESPONSE_FUNC_NAME = "onResponse";

    public static final String VALIDATION_RULES_NAME = "validationRules";

    public static final String PERMISSION_RULES_NAME = "permissionRules";

    public static final String VALIDATION_ERROR_STATUS = "ValidationError";

    public static final String GENERIC_ERROR_STATUS = "GenericError";

    /**
	 * 
	 * @param scope
	 * @param cx
	 */
    public static void register(Scriptable scope, Context cx) {
        ScriptableObject serviceProto = (ScriptableObject) cx.newObject(scope);
        serviceProto.defineFunctionProperties(new String[] { "define" }, ServiceExtension.class, ScriptableObject.DONTENUM | ScriptableObject.PERMANENT | ScriptableObject.READONLY);
        serviceProto.sealObject();
        ScriptableObject.putProperty(scope, "Service", serviceProto);
    }

    /**
     * 
     * @param cx
     * @param thisObj
     * @param args
     * @param funObj
     * @return
     */
    public static void define(Context cx, Scriptable thisObj, Object[] args, Function funObj) {
        if (args.length == 0) throw new IllegalArgumentException("Cannot call define with no arguments");
        if (args.length >= 1 && args[0] != null && args[0] != Undefined.instance && args[0] instanceof ScriptableObject) {
            ScriptableObject service = (ScriptableObject) args[0];
            executePrepare(cx, service, service.get(START_FUNC_NAME, service));
            if (executeValidation(cx, service, service.get(VALIDATION_RULES_NAME, service))) {
                executeAction(cx, service, service.get(ACTION_FUNC_NAME, service));
            }
            executeResponse(cx, service, service.get(RESPONSE_FUNC_NAME, service));
        }
    }

    /**
     * 
     * @param cx
     * @param thisObj
     * @param action
     * @return
     */
    protected static boolean executePrepare(Context cx, ScriptableObject thisObj, Object action) {
        thisObj.put("data", thisObj, cx.newObject(thisObj));
        thisObj.put("request", thisObj, cx.newObject(thisObj));
        thisObj.put("errors", thisObj, cx.newArray(thisObj, 0));
        thisObj.put("status", thisObj, null);
        thisObj.defineFunctionProperties(new String[] { "pushError" }, ServiceExtension.class, ScriptableObject.DONTENUM | ScriptableObject.PERMANENT | ScriptableObject.READONLY);
        if (action != null && action != Undefined.instance && action instanceof Function) {
            Function onAction = (Function) action;
            onAction.call(cx, thisObj, thisObj, Context.emptyArgs);
        }
        return true;
    }

    /**
     * 
     * @param cx
     * @param thisObj
     * @param args
     * @param funObj
     * @return
     */
    public static void pushError(Context cx, Scriptable thisObj, Object[] args, Function funObj) {
        if (args.length == 0) throw new IllegalArgumentException("Cannot call define with no arguments");
        NativeArray errorsData = (NativeArray) thisObj.get("errors", thisObj);
        ScriptableObject error = (ScriptableObject) cx.newObject(thisObj);
        if (args.length >= 1) error.put("name", error, args[0]);
        if (args.length >= 2) error.put("message", error, args[1]);
        errorsData.put((int) errorsData.getLength(), errorsData, error);
    }

    /**
	 * 
	 * @param cx
	 * @param thisObj
	 * @param rules
	 * @return
	 */
    protected static boolean executeValidation(Context cx, ScriptableObject thisObj, Object rules) {
        if (rules != null && rules != Undefined.instance && rules instanceof ScriptableObject) {
            ScriptableObject validationRules = (ScriptableObject) rules;
            ScriptableRequest requestObject = (ScriptableRequest) cx.getThreadLocal(ScriptEngine.REQUEST);
            ScriptableObject requestData = (ScriptableObject) thisObj.get("request", thisObj);
            NativeArray errorsData = (NativeArray) thisObj.get("errors", thisObj);
            Object bypassValidationObject = thisObj.get("bypassValidation", thisObj);
            boolean bypassValidation = false;
            if (Validator.isValid(bypassValidationObject) && Context.toBoolean(bypassValidationObject)) bypassValidation = true;
            HttpServletRequest request = requestObject.getServletRequest();
            Object[] ids = validationRules.getAllIds();
            for (int i = 0; i < ids.length; i++) {
                String key = (String) ids[i];
                ScriptableObject rule = (ScriptableObject) validationRules.get(key, validationRules);
                String type = "";
                Object typeObject = rule.get("type", rule);
                Object requiredObject = rule.get("required", rule);
                Object messageObject = rule.get("message", rule);
                Object defaultValue = rule.get("defaultValue", rule);
                Object isInArrayObject = rule.get("isIn", rule);
                NativeArray isInArray = null;
                if (Validator.isValid(isInArrayObject)) isInArray = (NativeArray) isInArrayObject;
                String param = request.getParameter(key);
                String[] paramValues = request.getParameterValues(key);
                boolean processErrors = false;
                if (Validator.isValid(typeObject)) type = Context.toString(typeObject);
                if (type.equals("string")) {
                    if (Validator.isValid(param)) {
                        boolean found = false;
                        if (isInArray != null) {
                            for (int j = 0; j < isInArray.getLength(); j++) {
                                if (param.equals(isInArray.get(j, isInArray))) {
                                    found = true;
                                    break;
                                }
                            }
                        } else {
                            found = true;
                        }
                        if (found) requestData.put(key, requestData, param); else processErrors = true;
                    } else {
                        processErrors = true;
                    }
                } else if (type.equals("integer")) {
                    if (Validator.isValid(param) && Validator.isInteger(param)) {
                        int intParam = (int) Context.toNumber(param);
                        boolean found = false;
                        if (isInArray != null) {
                            for (int j = 0; j < isInArray.getLength(); j++) {
                                if (intParam == (int) Context.toNumber(isInArray.get(j, isInArray))) {
                                    found = true;
                                    break;
                                }
                            }
                        } else {
                            found = true;
                        }
                        if (found) requestData.put(key, requestData, intParam); else processErrors = true;
                    } else {
                        processErrors = true;
                    }
                } else if (type.equals("float")) {
                    if (Validator.isValid(param) && Validator.isFloat(param)) {
                        double floatParam = Context.toNumber(param);
                        boolean found = false;
                        if (isInArray != null) {
                            for (int j = 0; j < isInArray.getLength(); j++) {
                                if (floatParam == Context.toNumber(isInArray.get(j, isInArray))) {
                                    found = true;
                                    break;
                                }
                            }
                        } else {
                            found = true;
                        }
                        if (found) requestData.put(key, requestData, floatParam); else processErrors = true;
                    } else {
                        processErrors = true;
                    }
                } else if (type.equals("time")) {
                    Date convertedDate = null;
                    if (Validator.isValid(param) && Validator.isTime(param)) convertedDate = Validator.toTime(param, null, null);
                    if (convertedDate != null) {
                        boolean found = false;
                        if (isInArray != null) {
                            for (int j = 0; j < isInArray.getLength(); j++) {
                                if (convertedDate.compareTo(Validator.toDate(Context.toString(isInArray.get(j, isInArray)), null, null)) == 0) {
                                    found = true;
                                    break;
                                }
                            }
                        } else {
                            found = true;
                        }
                        if (found) requestData.put(key, requestData, cx.newObject(thisObj, "Date", new Object[] { convertedDate.getTime() })); else processErrors = true;
                    } else {
                        processErrors = true;
                    }
                } else if (type.equals("date")) {
                    Date convertedDate = null;
                    if (Validator.isValid(param) && Validator.isDate(param)) convertedDate = Validator.toDate(param, null, null);
                    if (convertedDate != null) {
                        boolean found = false;
                        if (isInArray != null) {
                            for (int j = 0; j < isInArray.getLength(); j++) {
                                if (convertedDate.compareTo(Validator.toDate(Context.toString(isInArray.get(j, isInArray)), null, null)) == 0) {
                                    found = true;
                                    break;
                                }
                            }
                        } else {
                            found = true;
                        }
                        if (found) requestData.put(key, requestData, cx.newObject(thisObj, "Date", new Object[] { convertedDate.getTime() })); else processErrors = true;
                    } else {
                        processErrors = true;
                    }
                } else if (type.equals("datetime")) {
                    Date convertedDate = null;
                    if (Validator.isValid(param) && Validator.isDateTime(param)) convertedDate = Validator.toDateTime(param, null, null);
                    if (convertedDate != null) {
                        boolean found = false;
                        if (isInArray != null) {
                            for (int j = 0; j < isInArray.getLength(); j++) {
                                if (convertedDate.compareTo(Validator.toDate(Context.toString(isInArray.get(j, isInArray)), null, null)) == 0) {
                                    found = true;
                                    break;
                                }
                            }
                        } else {
                            found = true;
                        }
                        if (found) requestData.put(key, requestData, cx.newObject(thisObj, "Date", new Object[] { convertedDate.getTime() })); else processErrors = true;
                    } else {
                        processErrors = true;
                    }
                } else if (type.equals("array")) {
                    if (Validator.isValid(paramValues)) {
                        requestData.put(key, requestData, ScriptUtils.getJsArray(thisObj, paramValues));
                    } else {
                        processErrors = true;
                    }
                } else if (type.equals("datearray")) {
                    if (Validator.isValid(paramValues)) {
                        Vector vector = new Vector();
                        for (int j = 0; j < paramValues.length; j++) {
                            String value = paramValues[j];
                            if (value != null && Validator.isDate(value)) {
                                Date convertedDate = Validator.toDate(value, null, null);
                                if (convertedDate != null) vector.add(cx.newObject(thisObj, "Date", new Object[] { convertedDate.getTime() }));
                            }
                        }
                        requestData.put(key, requestData, ScriptUtils.getJsArray(thisObj, vector));
                    } else {
                        processErrors = true;
                    }
                } else if (type.equals("intarray")) {
                    if (Validator.isValid(paramValues)) {
                        Vector vector = new Vector();
                        for (int j = 0; j < paramValues.length; j++) {
                            String value = paramValues[j];
                            if (value != null && Validator.isInteger(value)) vector.add((int) Context.toNumber(value));
                        }
                        requestData.put(key, requestData, ScriptUtils.getJsArray(thisObj, vector));
                    } else {
                        processErrors = true;
                    }
                } else if (type.equals("floatarray")) {
                    if (Validator.isValid(paramValues)) {
                        Vector vector = new Vector();
                        for (int j = 0; j < paramValues.length; j++) {
                            String value = paramValues[j];
                            if (value != null && Validator.isFloat(value)) vector.add(Context.toNumber(value));
                        }
                        requestData.put(key, requestData, ScriptUtils.getJsArray(thisObj, vector));
                    } else {
                        processErrors = true;
                    }
                } else if (type.equals("csv")) {
                    if (Validator.isValid(paramValues)) {
                        requestData.put(key, requestData, ScriptUtils.getJsArray(thisObj, paramValues));
                    } else {
                        if (Validator.isValid(param)) {
                            String[] split = param.split(",");
                            requestData.put(key, requestData, ScriptUtils.getJsArray(thisObj, split));
                        } else {
                            processErrors = true;
                        }
                    }
                } else {
                    throw new IllegalArgumentException("Unknown validation type: " + type);
                }
                if (processErrors && !bypassValidation) {
                    String required = Context.toString(requiredObject);
                    if (Validator.isValid(required) && (required.equalsIgnoreCase("true") || required.equals("1"))) {
                        ScriptableObject errorValue = (ScriptableObject) cx.newObject(thisObj);
                        errorValue.put("name", errorValue, key);
                        if (Validator.isValid(messageObject)) errorValue.put("message", errorValue, Context.toString(messageObject));
                        errorsData.put((int) errorsData.getLength(), errorsData, errorValue);
                    } else {
                        if (!Validator.isNotFound(defaultValue)) requestData.put(key, requestData, defaultValue);
                    }
                }
            }
            boolean returnCustomValidation = true;
            Object customValidation = thisObj.get(CUSTOM_FUNC_NAME, thisObj);
            if (customValidation != null && customValidation != Undefined.instance && customValidation instanceof Function) {
                Function onAction = (Function) customValidation;
                Object retValue = onAction.call(cx, thisObj, thisObj, Context.emptyArgs);
                if (Validator.isValid(retValue)) returnCustomValidation = Context.toBoolean(retValue);
            }
            return (returnCustomValidation) && (errorsData.getLength() == 0);
        }
        return true;
    }

    /**
	 * 
	 * @param cx
	 * @param thisObj
	 * @param action
	 * @return
	 */
    protected static boolean executeAction(Context cx, ScriptableObject thisObj, Object action) {
        if (action != null && action != Undefined.instance && action instanceof Function) {
            Function onAction = (Function) action;
            onAction.call(cx, thisObj, thisObj, Context.emptyArgs);
        }
        return true;
    }

    /**
	 * 
	 * @param cx
	 * @param thisObj
	 * @param action
	 * @return
	 */
    protected static boolean executeResponse(Context cx, ScriptableObject thisObj, Object action) {
        if (action != null && action != Undefined.instance && action instanceof Function) {
            Function onAction = (Function) action;
            onAction.call(cx, thisObj, thisObj, Context.emptyArgs);
        } else {
            JSONSerializer serializer = new JSONSerializer();
            ScriptableResponse responseObject = (ScriptableResponse) cx.getThreadLocal(ScriptEngine.RESPONSE);
            HttpServletResponse response = responseObject.getServletResponse();
            ScriptableObject returnValue = (ScriptableObject) cx.newObject(thisObj);
            NativeArray errorsData = (NativeArray) thisObj.get("errors", thisObj);
            if (errorsData.getLength() != 0) returnValue.put("status", returnValue, VALIDATION_ERROR_STATUS); else returnValue.put("status", returnValue, thisObj.get("status", thisObj));
            returnValue.put("errors", returnValue, errorsData);
            returnValue.put("data", returnValue, thisObj.get("data", thisObj));
            returnValue.put("elapsed", returnValue, responseObject.getElapsedTime());
            try {
                PrintWriter out = response.getWriter();
                out.print(serializer.serialize(returnValue));
            } catch (IOException ex) {
                ScriptEngine engine = (ScriptEngine) cx.getThreadLocal("engine");
                engine.getLog().info(ex.getMessage());
            }
        }
        return true;
    }
}
