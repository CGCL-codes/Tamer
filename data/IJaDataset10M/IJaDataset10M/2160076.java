package org.butu.dcom;

import org.jinterop.dcom.common.JIException;
import org.jinterop.dcom.core.JIString;
import org.jinterop.dcom.core.JIVariant;
import org.jinterop.dcom.impls.JIObjectFactory;
import org.jinterop.dcom.impls.automation.IJIDispatch;

public class COMUtils {

    public static IJIDispatch getDispatch(IJIDispatch obj, String name) throws JIException {
        JIVariant res = obj.get(name);
        IJIDispatch dispatch = (IJIDispatch) JIObjectFactory.narrowObject(res.getObjectAsComObject());
        return dispatch;
    }

    public static IJIDispatch getDispatch(IJIDispatch obj, String name, Object... inparams) throws JIException {
        JIVariant[] res = obj.get(name, inparams);
        IJIDispatch dispatch = (IJIDispatch) JIObjectFactory.narrowObject(res[0].getObjectAsComObject());
        return dispatch;
    }

    public static IJIDispatch invokeDispatch(IJIDispatch obj, String name, Object... inparams) throws JIException {
        JIVariant[] res = obj.callMethodA(name, inparams);
        IJIDispatch dispatch = (IJIDispatch) JIObjectFactory.narrowObject(res[0].getObjectAsComObject());
        return dispatch;
    }

    public static JIVariant[] invoke(IJIDispatch obj, String name, Object... inparams) throws JIException {
        for (int i = 0; i < inparams.length; i++) {
            if (inparams[i] instanceof String) {
                inparams[i] = new JIString((String) inparams[i]);
            }
        }
        return obj.callMethodA(name, inparams);
    }
}
