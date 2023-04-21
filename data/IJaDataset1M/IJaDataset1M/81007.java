package com.aratana;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import org.apache.log4j.Logger;
import com.aratana.annotation.Datasource;

public final class Model {

    private final Logger logger;

    private final MVCUnit unit;

    private final HashMap<String, ModelInvoker> invokers = new HashMap<String, ModelInvoker>();

    private final HashMap<String, ModelInvoker> datasources = new HashMap<String, ModelInvoker>();

    Model(MVCUnit unit) {
        this.unit = unit;
        logger = Logger.getLogger("aratana." + getUnit().getName() + ".model");
    }

    public MVCUnit getUnit() {
        return unit;
    }

    public Object get(String name) throws Exception {
        ModelInvoker invoker = invokers.get(name);
        if (invoker != null) {
            return invoker.getMethod.invoke(invoker.ref);
        }
        return null;
    }

    public void set(String name, Object value) throws Exception {
        ModelInvoker invoker = invokers.get(name);
        if (invoker != null) {
            invoker.setMethod.invoke(invoker.ref, unit.convert(value, invoker.setMethod.getParameterTypes()[0]));
        }
    }

    public Object getData(String name) throws Exception {
        ModelInvoker invoker = datasources.get(name);
        if (invoker != null) {
            return invoker.getMethod.invoke(invoker.ref);
        }
        return null;
    }

    public <M> M addAsModel(M asModel) {
        if (asModel != null) {
            if (!(asModel instanceof PreparedModel<?>)) {
                asModel = MVCUnit.prepareModel(asModel);
            }
            ((PreparedModel<?>) asModel).addToUnit(unit);
            Class<?> clazz = asModel.getClass();
            do {
                for (Method method : clazz.getDeclaredMethods()) {
                    if (!Modifier.isStatic(method.getModifiers())) {
                        ModelInvoker invoker;
                        Datasource datasource = method.getAnnotation(Datasource.class);
                        if (datasource != null) {
                            String name = datasource.name().isEmpty() ? method.getName() : datasource.name();
                            invoker = datasources.get(name);
                            if (invoker == null) {
                                invoker = new ModelInvoker(asModel, name);
                                invoker.getMethod = method;
                                datasources.put(name, invoker);
                            } else {
                                logger.warn("Attemp to override a model datasource ('" + name + "').");
                            }
                        } else {
                            String name = getPropGetName(method.getName());
                            if (name != null) {
                                invoker = invokers.get(name);
                                if (invoker == null) {
                                    invoker = new ModelInvoker(asModel, name);
                                    invoker.getMethod = method;
                                    invokers.put(name, invoker);
                                } else if (invoker.getMethod == null) {
                                    invoker.getMethod = method;
                                    getUnit().getView().updateFromModel(name);
                                }
                            } else {
                                name = getPropSetName(method.getName());
                                if (name != null) {
                                    invoker = invokers.get(name);
                                    if (invoker == null) {
                                        invoker = new ModelInvoker(asModel, name);
                                        invoker.setMethod = method;
                                        invokers.put(name, invoker);
                                    } else if (invoker.setMethod == null) {
                                        invoker.setMethod = method;
                                        getUnit().getView().updateFromModel(name);
                                    }
                                }
                            }
                        }
                    }
                }
                for (ModelInvoker invoker : datasources.values()) {
                    if (invoker.ref == asModel) {
                        getUnit().getView().updateFromDatasource(invoker.name);
                    }
                }
                clazz = clazz.getSuperclass();
            } while (clazz != Object.class);
        }
        return asModel;
    }

    private static final String getPropGetName(String name) {
        if (name.startsWith("get")) {
            return Character.toLowerCase(name.charAt(3)) + name.substring(4);
        } else if (name.startsWith("is")) {
            return Character.toLowerCase(name.charAt(2)) + name.substring(3);
        }
        return null;
    }

    private static final String getPropSetName(String name) {
        if (name.startsWith("set")) {
            return Character.toLowerCase(name.charAt(3)) + name.substring(4);
        }
        return null;
    }

    private final class ModelInvoker {

        private final Object ref;

        private Method getMethod;

        private Method setMethod;

        private final String name;

        ModelInvoker(Object ref, String name) {
            this.name = name;
            this.ref = ref;
        }

        @Override
        public String toString() {
            return "invoker@" + name;
        }
    }
}
