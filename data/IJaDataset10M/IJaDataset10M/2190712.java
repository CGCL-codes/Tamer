package QosContractRepository;

/**
 * <ul>
 * <li> <b>IDL Source</b>    "QoSContractRepository.idl"
 * <li> <b>IDL Name</b>      ::QosContractRepository::DimensionClass
 * <li> <b>Repository Id</b> IDL:QosContractRepository/DimensionClass:1.0
 * </ul>
 * <b>IDL definition:</b>
 * <pre>
 * interface DimensionClass : QosContractRepository.ContainedClass {
  ...
};
 * </pre>
 */
public abstract class DimensionClassPOA extends org.omg.PortableServer.Servant implements org.omg.CORBA.portable.InvokeHandler, QosContractRepository.DimensionClassOperations {

    public QosContractRepository.DimensionClass _this() {
        return QosContractRepository.DimensionClassHelper.narrow(super._this_object());
    }

    public QosContractRepository.DimensionClass _this(org.omg.CORBA.ORB orb) {
        return QosContractRepository.DimensionClassHelper.narrow(super._this_object(orb));
    }

    public java.lang.String[] _all_interfaces(final org.omg.PortableServer.POA poa, final byte[] objectId) {
        return __ids;
    }

    private static java.lang.String[] __ids = { "IDL:QosContractRepository/DimensionClass:1.0", "IDL:QosContractRepository/ContainedClass:1.0", "IDL:Reflective/RefObject:1.0", "IDL:Reflective/RefBaseObject:1.0" };

    private static java.util.Dictionary _methods = new java.util.Hashtable();

    static {
        _methods.put("_get_all_of_type_dimension", new int[] { 0, 0 });
        _methods.put("_get_all_of_class_dimension", new int[] { 0, 1 });
        _methods.put("create_dimension", new int[] { 0, 2 });
        _methods.put("_get_all_of_type_contained", new int[] { 1, 0 });
        _methods.put("ref_is_instance_of", new int[] { 2, 0 });
        _methods.put("ref_create_instance", new int[] { 2, 1 });
        _methods.put("ref_all_objects", new int[] { 2, 2 });
        _methods.put("ref_set_value", new int[] { 2, 3 });
        _methods.put("ref_value", new int[] { 2, 4 });
        _methods.put("ref_unset_value", new int[] { 2, 5 });
        _methods.put("ref_add_value", new int[] { 2, 6 });
        _methods.put("ref_add_value_before", new int[] { 2, 7 });
        _methods.put("ref_add_value_at", new int[] { 2, 8 });
        _methods.put("ref_modify_value", new int[] { 2, 9 });
        _methods.put("ref_modify_value_at", new int[] { 2, 10 });
        _methods.put("ref_remove_value", new int[] { 2, 11 });
        _methods.put("ref_remove_value_at", new int[] { 2, 12 });
        _methods.put("ref_immediate_composite", new int[] { 2, 13 });
        _methods.put("ref_outermost_composite", new int[] { 2, 14 });
        _methods.put("ref_invoke_operation", new int[] { 2, 15 });
        _methods.put("ref_mof_id", new int[] { 3, 0 });
        _methods.put("ref_meta_object", new int[] { 3, 1 });
        _methods.put("ref_itself", new int[] { 3, 2 });
        _methods.put("ref_immediate_package", new int[] { 3, 3 });
        _methods.put("ref_outermost_package", new int[] { 3, 4 });
        _methods.put("ref_delete", new int[] { 3, 5 });
    }

    public org.omg.CORBA.portable.OutputStream _invoke(java.lang.String opName, org.omg.CORBA.portable.InputStream _input, org.omg.CORBA.portable.ResponseHandler handler) {
        int[] method = (int[]) _methods.get(opName);
        if (method == null) {
            throw new org.omg.CORBA.BAD_OPERATION();
        }
        switch(method[0]) {
            case 0:
                {
                    return QosContractRepository.DimensionClassPOA._invoke(this, method[1], _input, handler);
                }
            case 1:
                {
                    return QosContractRepository.ContainedClassPOA._invoke(this, method[1], _input, handler);
                }
            case 2:
                {
                    return Reflective.RefObjectPOA._invoke(this, method[1], _input, handler);
                }
            case 3:
                {
                    return Reflective.RefBaseObjectPOA._invoke(this, method[1], _input, handler);
                }
        }
        throw new org.omg.CORBA.BAD_OPERATION();
    }

    public static org.omg.CORBA.portable.OutputStream _invoke(QosContractRepository.DimensionClassOperations _self, int _method_id, org.omg.CORBA.portable.InputStream _input, org.omg.CORBA.portable.ResponseHandler _handler) {
        org.omg.CORBA.portable.OutputStream _output = null;
        {
            switch(_method_id) {
                case 0:
                    {
                        QosContractRepository.Dimension[] _result = _self.all_of_type_dimension();
                        _output = _handler.createReply();
                        QosContractRepository.DimensionSetHelper.write(_output, _result);
                        return _output;
                    }
                case 1:
                    {
                        QosContractRepository.Dimension[] _result = _self.all_of_class_dimension();
                        _output = _handler.createReply();
                        QosContractRepository.DimensionSetHelper.write(_output, _result);
                        return _output;
                    }
                case 2:
                    {
                        try {
                            java.lang.String name;
                            name = _input.read_string();
                            QosContractRepository.direction_kind direction;
                            direction = QosContractRepository.direction_kindHelper.read(_input);
                            org.omg.CORBA.TypeCode dimension_type;
                            dimension_type = _input.read_TypeCode();
                            java.lang.String unit_description;
                            unit_description = _input.read_string();
                            boolean allow_multi_constraint;
                            allow_multi_constraint = _input.read_boolean();
                            QosContractRepository.Dimension _result = _self.create_dimension(name, direction, dimension_type, unit_description, allow_multi_constraint);
                            _output = _handler.createReply();
                            QosContractRepository.DimensionHelper.write(_output, _result);
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
