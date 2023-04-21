package com.google.code.javascribd.connection;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import com.google.code.javascribd.connection.annotation.GETParameter;
import com.google.code.javascribd.connection.annotation.Method;
import com.google.code.javascribd.connection.annotation.POSTParameter;
import com.google.code.javascribd.type.SecretKey;

public class AbstractScribdMethod<T extends ScribdResponse> implements ScribdMethod<T> {

    private static final String METHOD = "method";

    private static final String API_SIG = "api_sig";

    private SecretKey secretKey;

    public SecretKey getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(SecretKey secretKey) {
        this.secretKey = secretKey;
    }

    public String getMethodName() {
        Method methodAnnotation = this.getClass().getAnnotation(Method.class);
        if (methodAnnotation != null) {
            return methodAnnotation.name();
        } else {
            throw new IllegalStateException("use " + Method.class.getName() + " annotation to define the method name of class '" + this.getClass().getName() + "'");
        }
    }

    @SuppressWarnings("unchecked")
    public Class<T> getResponseType() {
        Class<? extends ScribdMethod> c = this.getClass();
        Type type = c.getGenericSuperclass();
        if (ParameterizedType.class.isAssignableFrom(type.getClass())) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Type[] types = parameterizedType.getActualTypeArguments();
            if (types[0] instanceof Class) {
                return (Class<T>) types[0];
            }
        }
        return null;
    }

    public Map<String, StreamableData> getPOSTParameters() {
        Map<String, StreamableData> parameters = new HashMap<String, StreamableData>();
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field field : fields) {
            POSTParameter parameter = field.getAnnotation(POSTParameter.class);
            if (parameter != null) {
                String name = parameter.name();
                Object valueObject = null;
                try {
                    boolean accessible = field.isAccessible();
                    if (accessible == false) {
                        field.setAccessible(true);
                    }
                    valueObject = field.get(this);
                    if (accessible == false) {
                        field.setAccessible(false);
                    }
                } catch (Exception e) {
                    throw new IllegalStateException("The field '" + field.getName() + "' must be protected.", e);
                }
                if (valueObject != null) {
                    if (valueObject instanceof StreamableData) {
                        parameters.put(name, (StreamableData) valueObject);
                    } else {
                        throw new IllegalStateException(POSTParameter.class.getName() + " value must be a instance of " + StreamableData.class.getName());
                    }
                }
            }
        }
        return parameters;
    }

    public boolean hasPOSTParameters() {
        return getPOSTParameters().size() > 0;
    }

    @Override
    public String getGETParametersForURL() {
        Map<String, String> parameters = getGETParametersByAnnotation();
        parameters.put(METHOD, this.getMethodName());
        if (getSecretKey() != null) {
            String apiSigMd5 = ScribdParameterUtil.calculateApiSig(getSecretKey().toString(), parameters);
            parameters.put(API_SIG, apiSigMd5);
        }
        return ScribdParameterUtil.getEncodedParametersForURL(parameters);
    }

    private Map<String, String> getGETParametersByAnnotation() {
        Map<String, String> parameters = new TreeMap<String, String>();
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field field : fields) {
            GETParameter parameter = field.getAnnotation(GETParameter.class);
            if (parameter != null) {
                String name = parameter.name();
                Object valueObject = null;
                try {
                    boolean accessible = field.isAccessible();
                    if (accessible == false) {
                        field.setAccessible(true);
                    }
                    valueObject = field.get(this);
                    if (accessible == false) {
                        field.setAccessible(false);
                    }
                } catch (Exception e) {
                    throw new IllegalStateException("The field '" + field.getName() + "' must be protected.", e);
                }
                if (valueObject != null) {
                    parameters.put(name, valueObject.toString());
                }
            }
        }
        return parameters;
    }
}
