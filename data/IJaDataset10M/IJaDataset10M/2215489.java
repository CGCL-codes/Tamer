package org.omg.DsObservationAccess;

/**
 * Interface definition: SupplierAccess.
 * 
 * @author OpenORB Compiler
 */
public abstract class SupplierAccessPOA extends org.omg.PortableServer.Servant implements SupplierAccessOperations, org.omg.CORBA.portable.InvokeHandler {

    public SupplierAccess _this() {
        return SupplierAccessHelper.narrow(_this_object());
    }

    public SupplierAccess _this(org.omg.CORBA.ORB orb) {
        return SupplierAccessHelper.narrow(_this_object(orb));
    }

    private static String[] _ids_list = { "IDL:omg.org/DsObservationAccess/SupplierAccess:1.0", "IDL:omg.org/DsObservationAccess/AbstractFactory:1.0", "IDL:omg.org/DsObservationAccess/AccessComponent:1.0" };

    public String[] _all_interfaces(org.omg.PortableServer.POA poa, byte[] objectId) {
        return _ids_list;
    }

    private static final java.util.Map operationMap = new java.util.HashMap();

    static {
        operationMap.put("_get_coas_version", new Operation__get_coas_version());
        operationMap.put("_get_current_connections", new Operation__get_current_connections());
        operationMap.put("_get_max_connections", new Operation__get_max_connections());
        operationMap.put("_get_naming_service", new Operation__get_naming_service());
        operationMap.put("_get_pid_service", new Operation__get_pid_service());
        operationMap.put("_get_terminology_service", new Operation__get_terminology_service());
        operationMap.put("_get_trader_service", new Operation__get_trader_service());
        operationMap.put("are_iterators_supported", new Operation_are_iterators_supported());
        operationMap.put("create_supplier", new Operation_create_supplier());
        operationMap.put("get_components", new Operation_get_components());
        operationMap.put("get_current_time", new Operation_get_current_time());
        operationMap.put("get_default_policies", new Operation_get_default_policies());
        operationMap.put("get_supplier_by_id", new Operation_get_supplier_by_id());
        operationMap.put("get_supported_codes", new Operation_get_supported_codes());
        operationMap.put("get_supported_policies", new Operation_get_supported_policies());
        operationMap.put("get_supported_qualifiers", new Operation_get_supported_qualifiers());
        operationMap.put("get_type_code_for_observation_type", new Operation_get_type_code_for_observation_type());
    }

    public final org.omg.CORBA.portable.OutputStream _invoke(final String opName, final org.omg.CORBA.portable.InputStream _is, final org.omg.CORBA.portable.ResponseHandler handler) {
        final AbstractOperation operation = (AbstractOperation) operationMap.get(opName);
        if (null == operation) {
            throw new org.omg.CORBA.BAD_OPERATION(opName);
        }
        return operation.invoke(this, _is, handler);
    }

    private org.omg.CORBA.portable.OutputStream _invoke_create_supplier(final org.omg.CORBA.portable.InputStream _is, final org.omg.CORBA.portable.ResponseHandler handler) {
        org.omg.CORBA.portable.OutputStream _output;
        try {
            org.omg.DsObservationAccess.EventSupplier _arg_result = create_supplier();
            _output = handler.createReply();
            org.omg.DsObservationAccess.EventSupplierHelper.write(_output, _arg_result);
        } catch (org.omg.DsObservationAccess.MaxConnectionsExceeded _exception) {
            _output = handler.createExceptionReply();
            org.omg.DsObservationAccess.MaxConnectionsExceededHelper.write(_output, _exception);
        }
        return _output;
    }

    private org.omg.CORBA.portable.OutputStream _invoke_get_supplier_by_id(final org.omg.CORBA.portable.InputStream _is, final org.omg.CORBA.portable.ResponseHandler handler) {
        org.omg.CORBA.portable.OutputStream _output;
        int arg0_in = org.omg.DsObservationAccess.EndpointIdHelper.read(_is);
        try {
            org.omg.DsObservationAccess.EventSupplier _arg_result = get_supplier_by_id(arg0_in);
            _output = handler.createReply();
            org.omg.DsObservationAccess.EventSupplierHelper.write(_output, _arg_result);
        } catch (org.omg.DsObservationAccess.InvalidEndpointId _exception) {
            _output = handler.createExceptionReply();
            org.omg.DsObservationAccess.InvalidEndpointIdHelper.write(_output, _exception);
        }
        return _output;
    }

    private org.omg.CORBA.portable.OutputStream _invoke__get_max_connections(final org.omg.CORBA.portable.InputStream _is, final org.omg.CORBA.portable.ResponseHandler handler) {
        org.omg.CORBA.portable.OutputStream _output;
        int arg = max_connections();
        _output = handler.createReply();
        _output.write_long(arg);
        return _output;
    }

    private org.omg.CORBA.portable.OutputStream _invoke__get_current_connections(final org.omg.CORBA.portable.InputStream _is, final org.omg.CORBA.portable.ResponseHandler handler) {
        org.omg.CORBA.portable.OutputStream _output;
        int[] arg = current_connections();
        _output = handler.createReply();
        org.omg.DsObservationAccess.EndpointIdSeqHelper.write(_output, arg);
        return _output;
    }

    private org.omg.CORBA.portable.OutputStream _invoke__get_coas_version(final org.omg.CORBA.portable.InputStream _is, final org.omg.CORBA.portable.ResponseHandler handler) {
        org.omg.CORBA.portable.OutputStream _output;
        String arg = coas_version();
        _output = handler.createReply();
        _output.write_string(arg);
        return _output;
    }

    private org.omg.CORBA.portable.OutputStream _invoke__get_pid_service(final org.omg.CORBA.portable.InputStream _is, final org.omg.CORBA.portable.ResponseHandler handler) {
        org.omg.CORBA.portable.OutputStream _output;
        org.omg.PersonIdService.IdentificationComponent arg = pid_service();
        _output = handler.createReply();
        org.omg.DsObservationAccess.IdentificationComponentHelper.write(_output, arg);
        return _output;
    }

    private org.omg.CORBA.portable.OutputStream _invoke__get_terminology_service(final org.omg.CORBA.portable.InputStream _is, final org.omg.CORBA.portable.ResponseHandler handler) {
        org.omg.CORBA.portable.OutputStream _output;
        org.omg.TerminologyServices.TerminologyService arg = terminology_service();
        _output = handler.createReply();
        org.omg.DsObservationAccess.TerminologyServiceHelper.write(_output, arg);
        return _output;
    }

    private org.omg.CORBA.portable.OutputStream _invoke__get_trader_service(final org.omg.CORBA.portable.InputStream _is, final org.omg.CORBA.portable.ResponseHandler handler) {
        org.omg.CORBA.portable.OutputStream _output;
        org.omg.CosTrading.TraderComponents arg = trader_service();
        _output = handler.createReply();
        org.omg.DsObservationAccess.TraderComponentsHelper.write(_output, arg);
        return _output;
    }

    private org.omg.CORBA.portable.OutputStream _invoke__get_naming_service(final org.omg.CORBA.portable.InputStream _is, final org.omg.CORBA.portable.ResponseHandler handler) {
        org.omg.CORBA.portable.OutputStream _output;
        org.omg.CosNaming.NamingContext arg = naming_service();
        _output = handler.createReply();
        org.omg.DsObservationAccess.NamingContextHelper.write(_output, arg);
        return _output;
    }

    private org.omg.CORBA.portable.OutputStream _invoke_get_components(final org.omg.CORBA.portable.InputStream _is, final org.omg.CORBA.portable.ResponseHandler handler) {
        org.omg.CORBA.portable.OutputStream _output;
        org.omg.DsObservationAccess.AccessComponentData _arg_result = get_components();
        _output = handler.createReply();
        org.omg.DsObservationAccess.AccessComponentDataHelper.write(_output, _arg_result);
        return _output;
    }

    private org.omg.CORBA.portable.OutputStream _invoke_get_supported_codes(final org.omg.CORBA.portable.InputStream _is, final org.omg.CORBA.portable.ResponseHandler handler) {
        org.omg.CORBA.portable.OutputStream _output;
        int arg0_in = _is.read_ulong();
        org.omg.DsObservationAccess.QualifiedCodeIteratorHolder arg1_out = new org.omg.DsObservationAccess.QualifiedCodeIteratorHolder();
        String[] _arg_result = get_supported_codes(arg0_in, arg1_out);
        _output = handler.createReply();
        org.omg.DsObservationAccess.QualifiedCodeStrSeqHelper.write(_output, _arg_result);
        org.omg.DsObservationAccess.QualifiedCodeIteratorHelper.write(_output, arg1_out.value);
        return _output;
    }

    private org.omg.CORBA.portable.OutputStream _invoke_get_supported_qualifiers(final org.omg.CORBA.portable.InputStream _is, final org.omg.CORBA.portable.ResponseHandler handler) {
        org.omg.CORBA.portable.OutputStream _output;
        String arg0_in = org.omg.DsObservationAccess.QualifiedCodeStrHelper.read(_is);
        try {
            String[] _arg_result = get_supported_qualifiers(arg0_in);
            _output = handler.createReply();
            org.omg.DsObservationAccess.QualifiedCodeStrSeqHelper.write(_output, _arg_result);
        } catch (org.omg.DsObservationAccess.InvalidCodes _exception) {
            _output = handler.createExceptionReply();
            org.omg.DsObservationAccess.InvalidCodesHelper.write(_output, _exception);
        } catch (org.omg.DsObservationAccess.NotImplemented _exception) {
            _output = handler.createExceptionReply();
            org.omg.DsObservationAccess.NotImplementedHelper.write(_output, _exception);
        }
        return _output;
    }

    private org.omg.CORBA.portable.OutputStream _invoke_get_supported_policies(final org.omg.CORBA.portable.InputStream _is, final org.omg.CORBA.portable.ResponseHandler handler) {
        org.omg.CORBA.portable.OutputStream _output;
        String[] _arg_result = get_supported_policies();
        _output = handler.createReply();
        org.omg.DsObservationAccess.QualifiedCodeStrSeqHelper.write(_output, _arg_result);
        return _output;
    }

    private org.omg.CORBA.portable.OutputStream _invoke_get_default_policies(final org.omg.CORBA.portable.InputStream _is, final org.omg.CORBA.portable.ResponseHandler handler) {
        org.omg.CORBA.portable.OutputStream _output;
        org.omg.DsObservationAccess.NameValuePair[] _arg_result = get_default_policies();
        _output = handler.createReply();
        org.omg.DsObservationAccess.QueryPolicySeqHelper.write(_output, _arg_result);
        return _output;
    }

    private org.omg.CORBA.portable.OutputStream _invoke_get_type_code_for_observation_type(final org.omg.CORBA.portable.InputStream _is, final org.omg.CORBA.portable.ResponseHandler handler) {
        org.omg.CORBA.portable.OutputStream _output;
        String arg0_in = org.omg.DsObservationAccess.QualifiedCodeStrHelper.read(_is);
        try {
            org.omg.CORBA.TypeCode _arg_result = get_type_code_for_observation_type(arg0_in);
            _output = handler.createReply();
            org.omg.DsObservationAccess.TypeCodeHelper.write(_output, _arg_result);
        } catch (org.omg.DsObservationAccess.InvalidCodes _exception) {
            _output = handler.createExceptionReply();
            org.omg.DsObservationAccess.InvalidCodesHelper.write(_output, _exception);
        } catch (org.omg.DsObservationAccess.NotImplemented _exception) {
            _output = handler.createExceptionReply();
            org.omg.DsObservationAccess.NotImplementedHelper.write(_output, _exception);
        }
        return _output;
    }

    private org.omg.CORBA.portable.OutputStream _invoke_are_iterators_supported(final org.omg.CORBA.portable.InputStream _is, final org.omg.CORBA.portable.ResponseHandler handler) {
        org.omg.CORBA.portable.OutputStream _output;
        boolean _arg_result = are_iterators_supported();
        _output = handler.createReply();
        _output.write_boolean(_arg_result);
        return _output;
    }

    private org.omg.CORBA.portable.OutputStream _invoke_get_current_time(final org.omg.CORBA.portable.InputStream _is, final org.omg.CORBA.portable.ResponseHandler handler) {
        org.omg.CORBA.portable.OutputStream _output;
        String _arg_result = get_current_time();
        _output = handler.createReply();
        org.omg.DsObservationAccess.TimeStampHelper.write(_output, _arg_result);
        return _output;
    }

    private abstract static class AbstractOperation {

        protected abstract org.omg.CORBA.portable.OutputStream invoke(SupplierAccessPOA target, org.omg.CORBA.portable.InputStream _is, org.omg.CORBA.portable.ResponseHandler handler);
    }

    private static final class Operation_create_supplier extends AbstractOperation {

        protected org.omg.CORBA.portable.OutputStream invoke(final SupplierAccessPOA target, final org.omg.CORBA.portable.InputStream _is, final org.omg.CORBA.portable.ResponseHandler handler) {
            return target._invoke_create_supplier(_is, handler);
        }
    }

    private static final class Operation_get_supplier_by_id extends AbstractOperation {

        protected org.omg.CORBA.portable.OutputStream invoke(final SupplierAccessPOA target, final org.omg.CORBA.portable.InputStream _is, final org.omg.CORBA.portable.ResponseHandler handler) {
            return target._invoke_get_supplier_by_id(_is, handler);
        }
    }

    private static final class Operation__get_max_connections extends AbstractOperation {

        protected org.omg.CORBA.portable.OutputStream invoke(final SupplierAccessPOA target, final org.omg.CORBA.portable.InputStream _is, final org.omg.CORBA.portable.ResponseHandler handler) {
            return target._invoke__get_max_connections(_is, handler);
        }
    }

    private static final class Operation__get_current_connections extends AbstractOperation {

        protected org.omg.CORBA.portable.OutputStream invoke(final SupplierAccessPOA target, final org.omg.CORBA.portable.InputStream _is, final org.omg.CORBA.portable.ResponseHandler handler) {
            return target._invoke__get_current_connections(_is, handler);
        }
    }

    private static final class Operation__get_coas_version extends AbstractOperation {

        protected org.omg.CORBA.portable.OutputStream invoke(final SupplierAccessPOA target, final org.omg.CORBA.portable.InputStream _is, final org.omg.CORBA.portable.ResponseHandler handler) {
            return target._invoke__get_coas_version(_is, handler);
        }
    }

    private static final class Operation__get_pid_service extends AbstractOperation {

        protected org.omg.CORBA.portable.OutputStream invoke(final SupplierAccessPOA target, final org.omg.CORBA.portable.InputStream _is, final org.omg.CORBA.portable.ResponseHandler handler) {
            return target._invoke__get_pid_service(_is, handler);
        }
    }

    private static final class Operation__get_terminology_service extends AbstractOperation {

        protected org.omg.CORBA.portable.OutputStream invoke(final SupplierAccessPOA target, final org.omg.CORBA.portable.InputStream _is, final org.omg.CORBA.portable.ResponseHandler handler) {
            return target._invoke__get_terminology_service(_is, handler);
        }
    }

    private static final class Operation__get_trader_service extends AbstractOperation {

        protected org.omg.CORBA.portable.OutputStream invoke(final SupplierAccessPOA target, final org.omg.CORBA.portable.InputStream _is, final org.omg.CORBA.portable.ResponseHandler handler) {
            return target._invoke__get_trader_service(_is, handler);
        }
    }

    private static final class Operation__get_naming_service extends AbstractOperation {

        protected org.omg.CORBA.portable.OutputStream invoke(final SupplierAccessPOA target, final org.omg.CORBA.portable.InputStream _is, final org.omg.CORBA.portable.ResponseHandler handler) {
            return target._invoke__get_naming_service(_is, handler);
        }
    }

    private static final class Operation_get_components extends AbstractOperation {

        protected org.omg.CORBA.portable.OutputStream invoke(final SupplierAccessPOA target, final org.omg.CORBA.portable.InputStream _is, final org.omg.CORBA.portable.ResponseHandler handler) {
            return target._invoke_get_components(_is, handler);
        }
    }

    private static final class Operation_get_supported_codes extends AbstractOperation {

        protected org.omg.CORBA.portable.OutputStream invoke(final SupplierAccessPOA target, final org.omg.CORBA.portable.InputStream _is, final org.omg.CORBA.portable.ResponseHandler handler) {
            return target._invoke_get_supported_codes(_is, handler);
        }
    }

    private static final class Operation_get_supported_qualifiers extends AbstractOperation {

        protected org.omg.CORBA.portable.OutputStream invoke(final SupplierAccessPOA target, final org.omg.CORBA.portable.InputStream _is, final org.omg.CORBA.portable.ResponseHandler handler) {
            return target._invoke_get_supported_qualifiers(_is, handler);
        }
    }

    private static final class Operation_get_supported_policies extends AbstractOperation {

        protected org.omg.CORBA.portable.OutputStream invoke(final SupplierAccessPOA target, final org.omg.CORBA.portable.InputStream _is, final org.omg.CORBA.portable.ResponseHandler handler) {
            return target._invoke_get_supported_policies(_is, handler);
        }
    }

    private static final class Operation_get_default_policies extends AbstractOperation {

        protected org.omg.CORBA.portable.OutputStream invoke(final SupplierAccessPOA target, final org.omg.CORBA.portable.InputStream _is, final org.omg.CORBA.portable.ResponseHandler handler) {
            return target._invoke_get_default_policies(_is, handler);
        }
    }

    private static final class Operation_get_type_code_for_observation_type extends AbstractOperation {

        protected org.omg.CORBA.portable.OutputStream invoke(final SupplierAccessPOA target, final org.omg.CORBA.portable.InputStream _is, final org.omg.CORBA.portable.ResponseHandler handler) {
            return target._invoke_get_type_code_for_observation_type(_is, handler);
        }
    }

    private static final class Operation_are_iterators_supported extends AbstractOperation {

        protected org.omg.CORBA.portable.OutputStream invoke(final SupplierAccessPOA target, final org.omg.CORBA.portable.InputStream _is, final org.omg.CORBA.portable.ResponseHandler handler) {
            return target._invoke_are_iterators_supported(_is, handler);
        }
    }

    private static final class Operation_get_current_time extends AbstractOperation {

        protected org.omg.CORBA.portable.OutputStream invoke(final SupplierAccessPOA target, final org.omg.CORBA.portable.InputStream _is, final org.omg.CORBA.portable.ResponseHandler handler) {
            return target._invoke_get_current_time(_is, handler);
        }
    }
}
