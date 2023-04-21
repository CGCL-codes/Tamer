package org.omg.CosNotifyComm;

public abstract class _PullSupplierImplBase extends org.omg.CORBA.portable.ObjectImpl implements PullSupplier, org.omg.CORBA.portable.InvokeHandler {

    static final String[] _ids_list = { "IDL:omg.org/CosNotifyComm/PullSupplier:1.0", "IDL:omg.org/CosNotifyComm/NotifySubscribe:1.0", "IDL:omg.org/CosEventComm/PullSupplier:1.0" };

    public String[] _ids() {
        return _ids_list;
    }

    public org.omg.CORBA.portable.OutputStream _invoke(String opName, org.omg.CORBA.portable.InputStream _is, org.omg.CORBA.portable.ResponseHandler handler) {
        org.omg.CORBA.portable.OutputStream _output = null;
        if (opName.equals("subscription_change")) {
            org.omg.CosNotification.EventType[] arg0_in = org.omg.CosNotification.EventTypeSeqHelper.read(_is);
            org.omg.CosNotification.EventType[] arg1_in = org.omg.CosNotification.EventTypeSeqHelper.read(_is);
            try {
                subscription_change(arg0_in, arg1_in);
                _output = handler.createReply();
            } catch (org.omg.CosNotifyComm.InvalidEventType _exception) {
                _output = handler.createExceptionReply();
                org.omg.CosNotifyComm.InvalidEventTypeHelper.write(_output, _exception);
            }
            return _output;
        } else if (opName.equals("pull")) {
            try {
                org.omg.CORBA.Any _arg_result = pull();
                _output = handler.createReply();
                _output.write_any(_arg_result);
            } catch (org.omg.CosEventComm.Disconnected _exception) {
                _output = handler.createExceptionReply();
                org.omg.CosEventComm.DisconnectedHelper.write(_output, _exception);
            }
            return _output;
        } else if (opName.equals("try_pull")) {
            org.omg.CORBA.BooleanHolder arg0_out = new org.omg.CORBA.BooleanHolder();
            try {
                org.omg.CORBA.Any _arg_result = try_pull(arg0_out);
                _output = handler.createReply();
                _output.write_any(_arg_result);
                _output.write_boolean(arg0_out.value);
            } catch (org.omg.CosEventComm.Disconnected _exception) {
                _output = handler.createExceptionReply();
                org.omg.CosEventComm.DisconnectedHelper.write(_output, _exception);
            }
            return _output;
        } else if (opName.equals("disconnect_pull_supplier")) {
            disconnect_pull_supplier();
            _output = handler.createReply();
            return _output;
        } else throw new org.omg.CORBA.BAD_OPERATION();
    }
}
