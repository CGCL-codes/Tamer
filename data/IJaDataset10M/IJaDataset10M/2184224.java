package org.openscada.opc.dcom.da.impl;

import java.util.LinkedList;
import java.util.List;
import org.jinterop.dcom.common.JIException;
import org.jinterop.dcom.core.JIArray;
import org.jinterop.dcom.core.JIFlags;
import org.jinterop.dcom.core.JILocalCoClass;
import org.jinterop.dcom.core.JILocalInterfaceDefinition;
import org.jinterop.dcom.core.JILocalMethodDescriptor;
import org.jinterop.dcom.core.JILocalParamsDescriptor;
import org.jinterop.dcom.core.JIStruct;
import org.jinterop.dcom.core.JIVariant;
import org.openscada.opc.dcom.common.FILETIME;
import org.openscada.opc.dcom.common.KeyedResult;
import org.openscada.opc.dcom.common.KeyedResultSet;
import org.openscada.opc.dcom.common.Result;
import org.openscada.opc.dcom.common.ResultSet;
import org.openscada.opc.dcom.common.impl.EventHandlerImpl;
import org.openscada.opc.dcom.da.Constants;
import org.openscada.opc.dcom.da.IOPCDataCallback;
import org.openscada.opc.dcom.da.ValueData;

public class OPCDataCallback extends EventHandlerImpl {

    private IOPCDataCallback _callback = null;

    private JILocalCoClass _coClass = null;

    public OPCDataCallback() {
        super();
    }

    public Object[] OnDataChange(final int transactionId, final int serverGroupHandle, final int masterQuality, final int masterErrorCode, final int count, final JIArray clientHandles, final JIArray values, final JIArray qualities, final JIArray timestamps, final JIArray errors) {
        IOPCDataCallback callback = this._callback;
        if (callback == null) {
            return new Object[] { org.openscada.opc.dcom.common.Constants.S_OK };
        }
        Integer[] errorCodes = (Integer[]) errors.getArrayInstance();
        Integer[] itemHandles = (Integer[]) clientHandles.getArrayInstance();
        Short[] qualitiesArray = (Short[]) qualities.getArrayInstance();
        JIVariant[] valuesArray = (JIVariant[]) values.getArrayInstance();
        JIStruct[] timestampArray = (JIStruct[]) timestamps.getArrayInstance();
        KeyedResultSet<Integer, ValueData> result = new KeyedResultSet<Integer, ValueData>();
        for (int i = 0; i < count; i++) {
            ValueData vd = new ValueData();
            vd.setQuality(qualitiesArray[i]);
            vd.setTimestamp(FILETIME.fromStruct(timestampArray[i]).asCalendar());
            vd.setValue(valuesArray[i]);
            result.add(new KeyedResult<Integer, ValueData>(itemHandles[i], vd, errorCodes[i]));
        }
        try {
            callback.dataChange(transactionId, serverGroupHandle, masterQuality, masterErrorCode, result);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return new Object[] { org.openscada.opc.dcom.common.Constants.S_OK };
    }

    public synchronized Object[] OnReadComplete(final int transactionId, final int serverGroupHandle, final int masterQuality, final int masterErrorCode, final int count, final JIArray clientHandles, final JIArray values, final JIArray qualities, final JIArray timestamps, final JIArray errors) {
        if (this._callback == null) {
            return new Object[] { org.openscada.opc.dcom.common.Constants.S_OK };
        }
        Integer[] errorCodes = (Integer[]) errors.getArrayInstance();
        Integer[] itemHandles = (Integer[]) clientHandles.getArrayInstance();
        Short[] qualitiesArray = (Short[]) qualities.getArrayInstance();
        JIVariant[] valuesArray = (JIVariant[]) values.getArrayInstance();
        JIStruct[] timestampArray = (JIStruct[]) timestamps.getArrayInstance();
        KeyedResultSet<Integer, ValueData> result = new KeyedResultSet<Integer, ValueData>();
        for (int i = 0; i < count; i++) {
            ValueData vd = new ValueData();
            vd.setQuality(qualitiesArray[i]);
            vd.setTimestamp(FILETIME.fromStruct(timestampArray[i]).asCalendar());
            vd.setValue(valuesArray[i]);
            result.add(new KeyedResult<Integer, ValueData>(itemHandles[i], vd, errorCodes[i]));
        }
        try {
            this._callback.readComplete(transactionId, serverGroupHandle, masterQuality, masterErrorCode, result);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return new Object[] { org.openscada.opc.dcom.common.Constants.S_OK };
    }

    public synchronized Object[] OnWriteComplete(final int transactionId, final int serverGroupHandle, final int masterErrorCode, final int count, final JIArray clientHandles, final JIArray errors) {
        if (this._callback == null) {
            return new Object[] { org.openscada.opc.dcom.common.Constants.S_OK };
        }
        Integer[] errorCodes = (Integer[]) errors.getArrayInstance();
        Integer[] itemHandles = (Integer[]) clientHandles.getArrayInstance();
        ResultSet<Integer> result = new ResultSet<Integer>();
        for (int i = 0; i < count; i++) {
            result.add(new Result<Integer>(itemHandles[i], errorCodes[i]));
        }
        try {
            this._callback.writeComplete(transactionId, serverGroupHandle, masterErrorCode, result);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return new Object[] { org.openscada.opc.dcom.common.Constants.S_OK };
    }

    public synchronized Object[] OnCancelComplete(final int transactionId, final int serverGroupHandle) {
        if (this._callback == null) {
            return new Object[] { org.openscada.opc.dcom.common.Constants.S_OK };
        }
        this._callback.cancelComplete(transactionId, serverGroupHandle);
        return new Object[] { org.openscada.opc.dcom.common.Constants.S_OK };
    }

    public synchronized JILocalCoClass getCoClass() throws JIException {
        if (this._coClass != null) {
            return this._coClass;
        }
        this._coClass = new JILocalCoClass(new JILocalInterfaceDefinition(Constants.IOPCDataCallback_IID, false), this, false);
        JILocalParamsDescriptor params;
        JILocalMethodDescriptor method;
        params = new JILocalParamsDescriptor();
        params.addInParamAsType(Integer.class, JIFlags.FLAG_NULL);
        params.addInParamAsType(Integer.class, JIFlags.FLAG_NULL);
        params.addInParamAsType(Integer.class, JIFlags.FLAG_NULL);
        params.addInParamAsType(Integer.class, JIFlags.FLAG_NULL);
        params.addInParamAsType(Integer.class, JIFlags.FLAG_NULL);
        params.addInParamAsObject(new JIArray(Integer.class, null, 1, true), JIFlags.FLAG_NULL);
        params.addInParamAsObject(new JIArray(JIVariant.class, null, 1, true), JIFlags.FLAG_NULL);
        params.addInParamAsObject(new JIArray(Short.class, null, 1, true), JIFlags.FLAG_NULL);
        params.addInParamAsObject(new JIArray(FILETIME.getStruct(), null, 1, true), JIFlags.FLAG_NULL);
        params.addInParamAsObject(new JIArray(Integer.class, null, 1, true), JIFlags.FLAG_NULL);
        method = new JILocalMethodDescriptor("OnDataChange", params);
        this._coClass.getInterfaceDefinition().addMethodDescriptor(method);
        params = new JILocalParamsDescriptor();
        params.addInParamAsType(Integer.class, JIFlags.FLAG_NULL);
        params.addInParamAsType(Integer.class, JIFlags.FLAG_NULL);
        params.addInParamAsType(Integer.class, JIFlags.FLAG_NULL);
        params.addInParamAsType(Integer.class, JIFlags.FLAG_NULL);
        params.addInParamAsType(Integer.class, JIFlags.FLAG_NULL);
        params.addInParamAsObject(new JIArray(Integer.class, null, 1, true), JIFlags.FLAG_NULL);
        params.addInParamAsObject(new JIArray(JIVariant.class, null, 1, true), JIFlags.FLAG_NULL);
        params.addInParamAsObject(new JIArray(Short.class, null, 1, true), JIFlags.FLAG_NULL);
        params.addInParamAsObject(new JIArray(FILETIME.getStruct(), null, 1, true), JIFlags.FLAG_NULL);
        params.addInParamAsObject(new JIArray(Integer.class, null, 1, true), JIFlags.FLAG_NULL);
        method = new JILocalMethodDescriptor("OnReadComplete", params);
        this._coClass.getInterfaceDefinition().addMethodDescriptor(method);
        params = new JILocalParamsDescriptor();
        params.addInParamAsType(Integer.class, JIFlags.FLAG_NULL);
        params.addInParamAsType(Integer.class, JIFlags.FLAG_NULL);
        params.addInParamAsType(Integer.class, JIFlags.FLAG_NULL);
        params.addInParamAsType(Integer.class, JIFlags.FLAG_NULL);
        params.addInParamAsObject(new JIArray(Integer.class, null, 1, true), JIFlags.FLAG_NULL);
        params.addInParamAsObject(new JIArray(Integer.class, null, 1, true), JIFlags.FLAG_NULL);
        method = new JILocalMethodDescriptor("OnWriteComplete", params);
        this._coClass.getInterfaceDefinition().addMethodDescriptor(method);
        params = new JILocalParamsDescriptor();
        params.addInParamAsType(Integer.class, JIFlags.FLAG_NULL);
        params.addInParamAsType(Integer.class, JIFlags.FLAG_NULL);
        method = new JILocalMethodDescriptor("OnCancelComplete", params);
        this._coClass.getInterfaceDefinition().addMethodDescriptor(method);
        List<String> eventInterfaces = new LinkedList<String>();
        eventInterfaces.add(Constants.IOPCDataCallback_IID);
        this._coClass.setSupportedEventInterfaces(eventInterfaces);
        return this._coClass;
    }

    public void setCallback(final IOPCDataCallback callback) {
        this._callback = callback;
    }

    public IOPCDataCallback getCallback() {
        return this._callback;
    }
}
