package org.omg.CosNotifyComm;

/**
 *	Generated from IDL definition of interface "StructuredPushSupplier"
 *	@author JacORB IDL compiler 
 */
public abstract class StructuredPushSupplierPOA extends org.omg.PortableServer.Servant implements org.omg.CORBA.portable.InvokeHandler, org.omg.CosNotifyComm.StructuredPushSupplierOperations {

    private static final java.util.Hashtable m_opsHash = new java.util.Hashtable();

    static {
        m_opsHash.put("disconnect_structured_push_supplier", new java.lang.Integer(0));
        m_opsHash.put("subscription_change", new java.lang.Integer(1));
    }

    private String[] ids = { "IDL:omg.org/CosNotifyComm/StructuredPushSupplier:1.0", "IDL:omg.org/CosNotifyComm/NotifySubscribe:1.0", "IDL:omg.org/CORBA/Object:1.0" };

    public org.omg.CosNotifyComm.StructuredPushSupplier _this() {
        return org.omg.CosNotifyComm.StructuredPushSupplierHelper.narrow(_this_object());
    }

    public org.omg.CosNotifyComm.StructuredPushSupplier _this(org.omg.CORBA.ORB orb) {
        return org.omg.CosNotifyComm.StructuredPushSupplierHelper.narrow(_this_object(orb));
    }

    public org.omg.CORBA.portable.OutputStream _invoke(String method, org.omg.CORBA.portable.InputStream _input, org.omg.CORBA.portable.ResponseHandler handler) throws org.omg.CORBA.SystemException {
        org.omg.CORBA.portable.OutputStream _out = null;
        java.lang.Integer opsIndex = (java.lang.Integer) m_opsHash.get(method);
        if (null == opsIndex) throw new org.omg.CORBA.BAD_OPERATION(method + " not found");
        switch(opsIndex.intValue()) {
            case 0:
                {
                    _out = handler.createReply();
                    disconnect_structured_push_supplier();
                    break;
                }
            case 1:
                {
                    try {
                        org.omg.CosNotification.EventType[] _arg0 = org.omg.CosNotification.EventTypeSeqHelper.read(_input);
                        org.omg.CosNotification.EventType[] _arg1 = org.omg.CosNotification.EventTypeSeqHelper.read(_input);
                        _out = handler.createReply();
                        subscription_change(_arg0, _arg1);
                    } catch (org.omg.CosNotifyComm.InvalidEventType _ex0) {
                        _out = handler.createExceptionReply();
                        org.omg.CosNotifyComm.InvalidEventTypeHelper.write(_out, _ex0);
                    }
                    break;
                }
        }
        return _out;
    }

    public String[] _all_interfaces(org.omg.PortableServer.POA poa, byte[] obj_id) {
        return ids;
    }
}
