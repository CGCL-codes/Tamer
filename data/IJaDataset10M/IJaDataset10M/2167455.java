package QosContractRepository;

/**
 * <ul>
 * <li> <b>IDL Source</b>    "QoSContractRepository.idl"
 * <li> <b>IDL Name</b>      ::QosContractRepository::QosContractType
 * <li> <b>Repository Id</b> IDL:QosContractRepository/QosContractType:1.0
 * </ul>
 * <b>IDL definition:</b>
 * <pre>
 * interface QosContractType : QosContractRepository.Container,
                            QosContractRepository.QosContractTypeClass {
  ...
};
 * </pre>
 */
public abstract class QosContractTypePOA extends org.omg.PortableServer.Servant implements org.omg.CORBA.portable.InvokeHandler, QosContractRepository.QosContractTypeOperations {

    public QosContractRepository.QosContractType _this() {
        return QosContractRepository.QosContractTypeHelper.narrow(super._this_object());
    }

    public QosContractRepository.QosContractType _this(org.omg.CORBA.ORB orb) {
        return QosContractRepository.QosContractTypeHelper.narrow(super._this_object(orb));
    }

    public java.lang.String[] _all_interfaces(final org.omg.PortableServer.POA poa, final byte[] objectId) {
        return __ids;
    }

    private static java.lang.String[] __ids = { "IDL:QosContractRepository/QosContractType:1.0", "IDL:QosContractRepository/Container:1.0", "IDL:QosContractRepository/Contained:1.0", "IDL:QosContractRepository/ContainedClass:1.0", "IDL:Reflective/RefObject:1.0", "IDL:Reflective/RefBaseObject:1.0", "IDL:QosContractRepository/ContainerClass:1.0", "IDL:QosContractRepository/QosContractTypeClass:1.0" };

    private static java.util.Dictionary _methods = new java.util.Hashtable();

    static {
        _methods.put("major_version", new int[] { 0, 0 });
        _methods.put("minor_version", new int[] { 0, 1 });
        _methods.put("contents", new int[] { 1, 0 });
        _methods.put("set_contents", new int[] { 1, 1 });
        _methods.put("add_contents", new int[] { 1, 2 });
        _methods.put("modify_contents", new int[] { 1, 3 });
        _methods.put("remove_contents", new int[] { 1, 4 });
        _methods.put("add_contents_before", new int[] { 1, 5 });
        _methods.put("name", new int[] { 2, 0 });
        _methods.put("set_name", new int[] { 2, 1 });
        _methods.put("defined_in", new int[] { 2, 2 });
        _methods.put("set_defined_in", new int[] { 2, 3 });
        _methods.put("unset_defined_in", new int[] { 2, 4 });
        _methods.put("_get_all_of_type_contained", new int[] { 3, 0 });
        _methods.put("ref_is_instance_of", new int[] { 4, 0 });
        _methods.put("ref_create_instance", new int[] { 4, 1 });
        _methods.put("ref_all_objects", new int[] { 4, 2 });
        _methods.put("ref_set_value", new int[] { 4, 3 });
        _methods.put("ref_value", new int[] { 4, 4 });
        _methods.put("ref_unset_value", new int[] { 4, 5 });
        _methods.put("ref_add_value", new int[] { 4, 6 });
        _methods.put("ref_add_value_before", new int[] { 4, 7 });
        _methods.put("ref_add_value_at", new int[] { 4, 8 });
        _methods.put("ref_modify_value", new int[] { 4, 9 });
        _methods.put("ref_modify_value_at", new int[] { 4, 10 });
        _methods.put("ref_remove_value", new int[] { 4, 11 });
        _methods.put("ref_remove_value_at", new int[] { 4, 12 });
        _methods.put("ref_immediate_composite", new int[] { 4, 13 });
        _methods.put("ref_outermost_composite", new int[] { 4, 14 });
        _methods.put("ref_invoke_operation", new int[] { 4, 15 });
        _methods.put("ref_mof_id", new int[] { 5, 0 });
        _methods.put("ref_meta_object", new int[] { 5, 1 });
        _methods.put("ref_itself", new int[] { 5, 2 });
        _methods.put("ref_immediate_package", new int[] { 5, 3 });
        _methods.put("ref_outermost_package", new int[] { 5, 4 });
        _methods.put("ref_delete", new int[] { 5, 5 });
        _methods.put("_get_all_of_type_container", new int[] { 6, 0 });
        _methods.put("_get_all_of_type_qos_contract_type", new int[] { 7, 0 });
        _methods.put("_get_all_of_class_qos_contract_type", new int[] { 7, 1 });
        _methods.put("create_qos_contract_type", new int[] { 7, 2 });
    }

    public org.omg.CORBA.portable.OutputStream _invoke(java.lang.String opName, org.omg.CORBA.portable.InputStream _input, org.omg.CORBA.portable.ResponseHandler handler) {
        int[] method = (int[]) _methods.get(opName);
        if (method == null) {
            throw new org.omg.CORBA.BAD_OPERATION();
        }
        switch(method[0]) {
            case 0:
                {
                    return QosContractRepository.QosContractTypePOA._invoke(this, method[1], _input, handler);
                }
            case 1:
                {
                    return QosContractRepository.ContainerPOA._invoke(this, method[1], _input, handler);
                }
            case 2:
                {
                    return QosContractRepository.ContainedPOA._invoke(this, method[1], _input, handler);
                }
            case 3:
                {
                    return QosContractRepository.ContainedClassPOA._invoke(this, method[1], _input, handler);
                }
            case 4:
                {
                    return Reflective.RefObjectPOA._invoke(this, method[1], _input, handler);
                }
            case 5:
                {
                    return Reflective.RefBaseObjectPOA._invoke(this, method[1], _input, handler);
                }
            case 6:
                {
                    return QosContractRepository.ContainerClassPOA._invoke(this, method[1], _input, handler);
                }
            case 7:
                {
                    return QosContractRepository.QosContractTypeClassPOA._invoke(this, method[1], _input, handler);
                }
        }
        throw new org.omg.CORBA.BAD_OPERATION();
    }

    public static org.omg.CORBA.portable.OutputStream _invoke(QosContractRepository.QosContractTypeOperations _self, int _method_id, org.omg.CORBA.portable.InputStream _input, org.omg.CORBA.portable.ResponseHandler _handler) {
        org.omg.CORBA.portable.OutputStream _output = null;
        {
            switch(_method_id) {
                case 0:
                    {
                        try {
                            short _result = _self.major_version();
                            _output = _handler.createReply();
                            _output.write_short((short) _result);
                        } catch (Reflective.MofError _exception) {
                            _output = _handler.createExceptionReply();
                            Reflective.MofErrorHelper.write(_output, _exception);
                        }
                        return _output;
                    }
                case 1:
                    {
                        try {
                            short _result = _self.minor_version();
                            _output = _handler.createReply();
                            _output.write_short((short) _result);
                        } catch (Reflective.MofError _exception) {
                            _output = _handler.createExceptionReply();
                            Reflective.MofErrorHelper.write(_output, _exception);
                        }
                        return _output;
                    }
            }
            throw new org.omg.CORBA.BAD_OPERATION();
        }
    }
}
