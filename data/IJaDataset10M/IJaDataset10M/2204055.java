package org.opennms.protocols.wmi.wbem.jinterop;

import org.jinterop.dcom.impls.automation.IJIDispatch;
import org.jinterop.dcom.impls.automation.IJIEnumVariant;
import org.jinterop.dcom.impls.JIObjectFactory;
import org.jinterop.dcom.core.JIVariant;
import org.jinterop.dcom.core.IJIComObject;
import org.jinterop.dcom.core.JIArray;
import org.jinterop.dcom.common.JIException;
import org.opennms.protocols.wmi.WmiException;
import org.opennms.protocols.wmi.wbem.OnmsWbemMethod;
import org.opennms.protocols.wmi.wbem.OnmsWbemMethodSet;

public class OnmsWbemMethodSetImpl implements OnmsWbemMethodSet {

    private IJIDispatch wbemMethodSetDispatch;

    public OnmsWbemMethodSetImpl(IJIDispatch wbemMethodSetDispatch) {
        this.wbemMethodSetDispatch = wbemMethodSetDispatch;
    }

    public Integer getCount() throws WmiException {
        try {
            JIVariant jviCount = wbemMethodSetDispatch.get("Count");
            return jviCount.getObjectAsInt();
        } catch (JIException e) {
            throw new WmiException("Failed to retrieve Method Set count: " + e.getMessage(), e);
        }
    }

    public OnmsWbemMethod get(Integer idx) throws WmiException {
        try {
            IJIComObject enumComObject = wbemMethodSetDispatch.get("_NewEnum").getObjectAsComObject();
            IJIEnumVariant enumVariant = (IJIEnumVariant) JIObjectFactory.narrowObject(enumComObject.queryInterface(IJIEnumVariant.IID));
            OnmsWbemMethod wbemMethod;
            IJIDispatch wbemMethod_dispatch = null;
            for (int i = 0; i < (idx + 1); i++) {
                Object[] values = enumVariant.next(1);
                JIArray array = (JIArray) values[0];
                Object[] arrayObj = (Object[]) array.getArrayInstance();
                for (int j = 0; j < arrayObj.length; j++) {
                    wbemMethod_dispatch = (IJIDispatch) JIObjectFactory.narrowObject(((JIVariant) arrayObj[j]).getObjectAsComObject());
                }
            }
            wbemMethod = new OnmsWbemMethodImpl(wbemMethod_dispatch);
            return wbemMethod;
        } catch (JIException e) {
            throw new WmiException("Failed to enumerate WbemObject variant: " + e.getMessage(), e);
        }
    }
}
