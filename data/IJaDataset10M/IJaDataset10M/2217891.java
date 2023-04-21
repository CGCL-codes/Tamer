package org.callbackparams.internal;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * CallbackContext implementation used by CallbackTestClassReloader for adding
 * method-params of classes that have not yet been initialized in the target-
 * context. When the method's declaring classes have been initilized in the
 * target-context the addMethodParams invocations can be passed on to the
 * target-context with the method releaseQue().
 *
 * @author Henrik Kaipe
 */
class CallbackContextQue implements CallbackContext {

    private final CallbackContext targetContext;

    private final LinkedList addedMethodsQue = new LinkedList();

    private final LinkedList addedInjectFieldsQue = new LinkedList();

    private int methodParameterCount = 0;

    CallbackContextQue(CallbackContext targetContext) {
        this.targetContext = targetContext;
    }

    public void addFieldToInject(Field f) {
        addedInjectFieldsQue.add(f);
    }

    public Iterator iterateFields() {
        return targetContext.iterateFields();
    }

    public int addMethodParams(Method m) {
        try {
            return methodParameterCount;
        } finally {
            addedMethodsQue.add(m);
            methodParameterCount += m.getParameterTypes().length;
        }
    }

    public Iterator iterateMethods() {
        return targetContext.iterateMethods();
    }

    public List getCallbackRecord() {
        return targetContext.getCallbackRecord();
    }

    public void setCallbackRecord(Collection callbackRecord) {
        targetContext.setCallbackRecord(callbackRecord);
    }

    void releaseQue() {
        while (false == addedMethodsQue.isEmpty()) {
            targetContext.addMethodParams((Method) addedMethodsQue.removeFirst());
        }
        while (false == addedInjectFieldsQue.isEmpty()) {
            targetContext.addFieldToInject((Field) addedInjectFieldsQue.removeFirst());
        }
    }

    CallbackContext getTargetContext() {
        return targetContext;
    }
}
