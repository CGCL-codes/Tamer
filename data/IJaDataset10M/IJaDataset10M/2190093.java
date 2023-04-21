package fr.free.davidsoft.calibre.stanza.i18n;

import java.lang.reflect.Method;
import fr.free.davidsoft.tools.Helper;

public class MethodCallerConverter implements Object2StringConverter {

    private String methodName;

    public MethodCallerConverter() {
        super();
    }

    public MethodCallerConverter(String methodName) {
        this();
        this.methodName = methodName;
    }

    String getMethodName() {
        return methodName;
    }

    void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    protected String callMethod(String methodName, Object o) {
        return callMethod(methodName, o, true);
    }

    protected String callMethod(String methodName, Object o, boolean returnObjectIfNull) {
        if (o == null) return null;
        if (methodName == null) return null;
        String result = null;
        try {
            Method method = o.getClass().getDeclaredMethod(methodName);
            if (method != null) result = (String) method.invoke(o);
        } catch (Exception e) {
        }
        if (Helper.isNullOrEmpty(result) && returnObjectIfNull) result = o.toString();
        return result;
    }

    public String getStringValue(Object o) {
        return callMethod(getMethodName(), o);
    }

    public String getStringValueOrNull(Object o) {
        return callMethod(getMethodName(), o, false);
    }
}
