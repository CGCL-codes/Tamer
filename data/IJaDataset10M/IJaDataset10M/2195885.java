package org.omg.CORBA.FT;

/**
 *	Generated from IDL definition of interface "RequestDurationPolicy"
 *	@author JacORB IDL compiler 
 */
public abstract class RequestDurationPolicyPOA extends org.omg.PortableServer.Servant implements org.omg.CORBA.portable.InvokeHandler, org.omg.CORBA.FT.RequestDurationPolicyOperations {

    private static final java.util.Hashtable m_opsHash = new java.util.Hashtable();

    static {
        m_opsHash.put("_get_request_duration_value", new java.lang.Integer(0));
        m_opsHash.put("_get_policy_type", new java.lang.Integer(1));
        m_opsHash.put("copy", new java.lang.Integer(2));
        m_opsHash.put("destroy", new java.lang.Integer(3));
    }

    private String[] ids = { "IDL:omg.org/CORBA/FT/RequestDurationPolicy:1.0", "IDL:omg.org/CORBA/Policy:1.0", "IDL:omg.org/CORBA/Object:1.0" };

    public org.omg.CORBA.FT.RequestDurationPolicy _this() {
        return org.omg.CORBA.FT.RequestDurationPolicyHelper.narrow(_this_object());
    }

    public org.omg.CORBA.FT.RequestDurationPolicy _this(org.omg.CORBA.ORB orb) {
        return org.omg.CORBA.FT.RequestDurationPolicyHelper.narrow(_this_object(orb));
    }

    public org.omg.CORBA.portable.OutputStream _invoke(String method, org.omg.CORBA.portable.InputStream _input, org.omg.CORBA.portable.ResponseHandler handler) throws org.omg.CORBA.SystemException {
        org.omg.CORBA.portable.OutputStream _out = null;
        java.lang.Integer opsIndex = (java.lang.Integer) m_opsHash.get(method);
        if (null == opsIndex) throw new org.omg.CORBA.BAD_OPERATION(method + " not found");
        switch(opsIndex.intValue()) {
            case 0:
                {
                    _out = handler.createReply();
                    _out.write_ulonglong(request_duration_value());
                    break;
                }
            case 1:
                {
                    _out = handler.createReply();
                    _out.write_ulong(policy_type());
                    break;
                }
            case 2:
                {
                    _out = handler.createReply();
                    org.omg.CORBA.PolicyHelper.write(_out, copy());
                    break;
                }
            case 3:
                {
                    _out = handler.createReply();
                    destroy();
                    break;
                }
        }
        return _out;
    }

    public String[] _all_interfaces(org.omg.PortableServer.POA poa, byte[] obj_id) {
        return ids;
    }
}
