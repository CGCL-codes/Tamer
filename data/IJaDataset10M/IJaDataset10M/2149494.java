package com.hyper9.simdk.codegen.impls;

import java.lang.reflect.Method;
import com.hyper9.simdk.codegen.Constants;
import com.hyper9.simdk.codegen.types.ManagedObjectType;
import com.hyper9.simdk.codegen.types.MetaProperty;
import com.hyper9.simdk.codegen.types.OutType;

public class ManagedObjectTypeProcessorImpl extends TypeProcessorImpl<ManagedObjectType> {

    @Override
    public void processClass(Class<?> clazz, String sourceFilePath) throws Exception {
        ManagedObjectType meta = new ManagedObjectTypeImpl(clazz, sourceFilePath);
        meta.getOutType().setSimpleName(clazz.getSimpleName());
        if (clazz.getSuperclass() != Object.class) {
            OutType outSuper = new OutTypeImpl();
            outSuper.setSimpleName(clazz.getSuperclass().getSimpleName());
            meta.getOutType().setSuperclass(outSuper);
        }
        if (clazz != Constants.MANAGED_OBJECT_CLASS) {
            Method[] methods = clazz.getMethods();
            for (Method m : methods) {
                if (m.getDeclaringClass() != clazz) {
                    continue;
                }
                if (!m.getName().matches("^(?:get|is).+$")) {
                    continue;
                }
                if (m.getParameterTypes() != null && m.getParameterTypes().length > 0) {
                    continue;
                }
                String methodNameMinusPropertyVerb = m.getName().startsWith("get") ? m.getName().substring(3) : m.getName().substring(2);
                String propName = firstCharToLowerCase(methodNameMinusPropertyVerb);
                MetaProperty mp = new MetaPropertyImpl(meta);
                mp.setName(propName);
                String simpleName = m.getReturnType().getSimpleName();
                mp.getOutType().setSimpleName(simpleName);
                if (m.getReturnType().getPackage() != null && !m.getReturnType().getPackage().getName().contains("vmware")) {
                    mp.getOutType().setPackageName(m.getReturnType().getPackage().getName());
                }
                meta.getProperties().add(mp);
            }
        }
        getProcessedMetaTypes().put(meta.getOutType().getSimpleName(), meta);
    }

    @Override
    public boolean shouldProcess(Class<?> clazz, String sourceFilePath) throws Exception {
        return Constants.MANAGED_OBJECT_CLASS.isAssignableFrom(clazz);
    }

    private static String firstCharToLowerCase(String s) {
        return s.substring(0, 1).toLowerCase() + s.substring(1);
    }
}
