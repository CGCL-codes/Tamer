package org.asam.ods;

/**
 *	Generated from IDL interface "QueryConstants"
 *	@author JacORB IDL compiler V 2.2.3, 10-Dec-2005
 */
public abstract class QueryConstantsPOA extends org.omg.PortableServer.Servant implements org.omg.CORBA.portable.InvokeHandler, org.asam.ods.QueryConstantsOperations {

    private String[] ids = { "IDL:org/asam/ods/QueryConstants:1.0" };

    public org.asam.ods.QueryConstants _this() {
        return org.asam.ods.QueryConstantsHelper.narrow(_this_object());
    }

    public org.asam.ods.QueryConstants _this(org.omg.CORBA.ORB orb) {
        return org.asam.ods.QueryConstantsHelper.narrow(_this_object(orb));
    }

    public org.omg.CORBA.portable.OutputStream _invoke(String method, org.omg.CORBA.portable.InputStream _input, org.omg.CORBA.portable.ResponseHandler handler) throws org.omg.CORBA.SystemException {
        throw new org.omg.CORBA.BAD_OPERATION(method + " not found");
    }

    public String[] _all_interfaces(org.omg.PortableServer.POA poa, byte[] obj_id) {
        return ids;
    }
}
