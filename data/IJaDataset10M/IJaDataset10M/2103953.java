package com.carbonfive.flash;

import java.util.*;
import java.io.Serializable;
import org.apache.log4j.*;
import flashgateway.io.ASObject;

public class TranslatorFactory {

    private static TranslatorFactory instance = null;

    private TranslatorFactory() {
    }

    static synchronized TranslatorFactory getInstance() {
        if (instance == null) {
            instance = new TranslatorFactory();
        }
        return instance;
    }

    Translator getTranslatorToActionScript(ASTranslator astranslator, Object serverObject) {
        Translator translator = null;
        Class clazz = serverObject.getClass();
        boolean isNativeObject = isActionScriptNative(serverObject);
        boolean isNumberObject = Number.class.isAssignableFrom(clazz);
        boolean isArray = clazz.isArray();
        boolean isCollection = Collection.class.isAssignableFrom(clazz);
        boolean isMap = Map.class.isAssignableFrom(clazz);
        boolean isJavaBean = Serializable.class.isAssignableFrom(clazz);
        if (isNativeObject) {
            translator = new NativeTranslator(astranslator, serverObject, null);
        } else if (isNumberObject) {
            translator = new NumberTranslator(astranslator, serverObject, null);
        } else if (isArray) {
            translator = new ArrayTranslator(astranslator, serverObject, null);
        } else if (isMap) {
            translator = new MapTranslator(astranslator, serverObject, null);
        } else if (isCollection) {
            translator = new CollectionTranslator(astranslator, serverObject, null);
        } else if (isJavaBean) {
            translator = new JavaBeanTranslator(astranslator, serverObject, null);
        } else {
            translator = new NativeTranslator(astranslator, serverObject, null);
        }
        return translator;
    }

    Translator getTranslatorFromActionScript(ASTranslator astranslator, Object clientObject, Class desiredClass) {
        Translator translator = null;
        if (!objectIsCompatibleWithClass(clientObject, desiredClass)) {
            return null;
        }
        boolean isNativeObject = (isActionScriptNative(clientObject));
        boolean isNumberObject = (clientObject instanceof Number);
        boolean isArray = (clientObject instanceof ArrayList && desiredClass.isArray());
        boolean isCollection = (clientObject instanceof ArrayList && Collection.class.isAssignableFrom(desiredClass));
        boolean isMap = (clientObject instanceof Map && !(clientObject instanceof ASObject)) || (clientObject instanceof ASObject && ((ASObject) clientObject).getType() == null);
        boolean isJavaBean = (clientObject instanceof ASObject && ((ASObject) clientObject).getType() != null);
        if (isNativeObject) {
            translator = new NativeTranslator(astranslator, clientObject, desiredClass);
        } else if (isNumberObject) {
            translator = new NumberTranslator(astranslator, clientObject, desiredClass);
        } else if (isArray) {
            translator = new ArrayTranslator(astranslator, clientObject, desiredClass);
        } else if (isCollection) {
            translator = new CollectionTranslator(astranslator, clientObject, desiredClass);
        } else if (isMap) {
            translator = new MapTranslator(astranslator, clientObject, desiredClass);
        } else if (isJavaBean) {
            translator = new JavaBeanTranslator(astranslator, clientObject, desiredClass);
        }
        return translator;
    }

    private static boolean isActionScriptNative(Object obj) {
        if (obj == null) return false;
        if (obj instanceof Boolean) return true;
        if (obj instanceof Date) return true;
        if (obj instanceof String) return true;
        if (obj instanceof org.w3c.dom.Document) return true;
        if (obj instanceof java.sql.ResultSet) return true;
        return false;
    }

    private static boolean objectIsCompatibleWithClass(Object clientObject, Class desiredClass) {
        return true;
    }
}
