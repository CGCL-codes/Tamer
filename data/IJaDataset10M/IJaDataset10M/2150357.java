package org.omg.CosTypedNotifyComm;

public abstract class _TypedPushConsumerImplBase extends org.omg.CORBA.portable.ObjectImpl implements TypedPushConsumer, org.omg.CORBA.portable.InvokeHandler {

    static final String[] _ids_list = { "IDL:omg.org/CosTypedNotifyComm/TypedPushConsumer:1.0", "IDL:omg.org/CosTypedEventComm/TypedPushConsumer:1.0", "IDL:omg.org/CosEventComm/PushConsumer:1.0", "IDL:omg.org/CosNotifyComm/NotifyPublish:1.0" };

    public String[] _ids() {
        return _ids_list;
    }

    public org.omg.CORBA.portable.OutputStream _invoke(String opName, org.omg.CORBA.portable.InputStream _is, org.omg.CORBA.portable.ResponseHandler handler) {
        org.omg.CORBA.portable.OutputStream _output = null;
        if (opName.equals("get_typed_consumer")) {
            org.omg.CORBA.Object _arg_result = get_typed_consumer();
            _output = handler.createReply();
            _output.write_Object(_arg_result);
            return _output;
        } else if (opName.equals("push")) {
            org.omg.CORBA.Any arg0_in = _is.read_any();
            try {
                push(arg0_in);
                _output = handler.createReply();
            } catch (org.omg.CosEventComm.Disconnected _exception) {
                _output = handler.createExceptionReply();
                org.omg.CosEventComm.DisconnectedHelper.write(_output, _exception);
            }
            return _output;
        } else if (opName.equals("disconnect_push_consumer")) {
            disconnect_push_consumer();
            _output = handler.createReply();
            return _output;
        } else if (opName.equals("offer_change")) {
            org.omg.CosNotification.EventType[] arg0_in = org.omg.CosNotification.EventTypeSeqHelper.read(_is);
            org.omg.CosNotification.EventType[] arg1_in = org.omg.CosNotification.EventTypeSeqHelper.read(_is);
            try {
                offer_change(arg0_in, arg1_in);
                _output = handler.createReply();
            } catch (org.omg.CosNotifyComm.InvalidEventType _exception) {
                _output = handler.createExceptionReply();
                org.omg.CosNotifyComm.InvalidEventTypeHelper.write(_output, _exception);
            }
            return _output;
        } else throw new org.omg.CORBA.BAD_OPERATION();
    }
}
