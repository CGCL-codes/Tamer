package org.asam.ods;

/**
 *	Generated from IDL interface "BaseAttribute"
 *	@author JacORB IDL compiler V 2.2.3, 10-Dec-2005
 */
public abstract class BaseAttributePOA extends org.omg.PortableServer.Servant implements org.omg.CORBA.portable.InvokeHandler, org.asam.ods.BaseAttributeOperations {

    private static final java.util.Hashtable m_opsHash = new java.util.Hashtable();

    static {
        m_opsHash.put("isUnique", new java.lang.Integer(0));
        m_opsHash.put("isObligatory", new java.lang.Integer(1));
        m_opsHash.put("getDataType", new java.lang.Integer(2));
        m_opsHash.put("getBaseElement", new java.lang.Integer(3));
        m_opsHash.put("getName", new java.lang.Integer(4));
    }

    private String[] ids = { "IDL:org/asam/ods/BaseAttribute:1.0" };

    public org.asam.ods.BaseAttribute _this() {
        return org.asam.ods.BaseAttributeHelper.narrow(_this_object());
    }

    public org.asam.ods.BaseAttribute _this(org.omg.CORBA.ORB orb) {
        return org.asam.ods.BaseAttributeHelper.narrow(_this_object(orb));
    }

    public org.omg.CORBA.portable.OutputStream _invoke(String method, org.omg.CORBA.portable.InputStream _input, org.omg.CORBA.portable.ResponseHandler handler) throws org.omg.CORBA.SystemException {
        org.omg.CORBA.portable.OutputStream _out = null;
        java.lang.Integer opsIndex = (java.lang.Integer) m_opsHash.get(method);
        if (null == opsIndex) throw new org.omg.CORBA.BAD_OPERATION(method + " not found");
        switch(opsIndex.intValue()) {
            case 0:
                {
                    try {
                        _out = handler.createReply();
                        _out.write_boolean(isUnique());
                    } catch (org.asam.ods.AoException _ex0) {
                        _out = handler.createExceptionReply();
                        org.asam.ods.AoExceptionHelper.write(_out, _ex0);
                    }
                    break;
                }
            case 1:
                {
                    try {
                        _out = handler.createReply();
                        _out.write_boolean(isObligatory());
                    } catch (org.asam.ods.AoException _ex0) {
                        _out = handler.createExceptionReply();
                        org.asam.ods.AoExceptionHelper.write(_out, _ex0);
                    }
                    break;
                }
            case 2:
                {
                    try {
                        _out = handler.createReply();
                        org.asam.ods.DataTypeHelper.write(_out, getDataType());
                    } catch (org.asam.ods.AoException _ex0) {
                        _out = handler.createExceptionReply();
                        org.asam.ods.AoExceptionHelper.write(_out, _ex0);
                    }
                    break;
                }
            case 3:
                {
                    try {
                        _out = handler.createReply();
                        org.asam.ods.BaseElementHelper.write(_out, getBaseElement());
                    } catch (org.asam.ods.AoException _ex0) {
                        _out = handler.createExceptionReply();
                        org.asam.ods.AoExceptionHelper.write(_out, _ex0);
                    }
                    break;
                }
            case 4:
                {
                    try {
                        _out = handler.createReply();
                        _out.write_string(getName());
                    } catch (org.asam.ods.AoException _ex0) {
                        _out = handler.createExceptionReply();
                        org.asam.ods.AoExceptionHelper.write(_out, _ex0);
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
