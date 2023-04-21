package org.omg.CosNotifyChannelAdmin;

public class _SequenceProxyPushConsumerStub extends org.omg.CORBA.portable.ObjectImpl implements org.omg.CosNotifyChannelAdmin.SequenceProxyPushConsumer {

    public void connect_sequence_push_supplier(org.omg.CosNotifyComm.SequencePushSupplier push_supplier) throws org.omg.CosEventChannelAdmin.AlreadyConnected {
        org.omg.CORBA.portable.InputStream $in = null;
        try {
            org.omg.CORBA.portable.OutputStream $out = _request("connect_sequence_push_supplier", true);
            org.omg.CosNotifyComm.SequencePushSupplierHelper.write($out, push_supplier);
            $in = _invoke($out);
            return;
        } catch (org.omg.CORBA.portable.ApplicationException $ex) {
            $in = $ex.getInputStream();
            String _id = $ex.getId();
            if (_id.equals("IDL:omg.org/CosEventChannelAdmin/AlreadyConnected:1.0")) throw org.omg.CosEventChannelAdmin.AlreadyConnectedHelper.read($in); else throw new org.omg.CORBA.MARSHAL(_id);
        } catch (org.omg.CORBA.portable.RemarshalException $rm) {
            connect_sequence_push_supplier(push_supplier);
        } finally {
            _releaseReply($in);
        }
    }

    public org.omg.CosNotifyChannelAdmin.ProxyType MyType() {
        org.omg.CORBA.portable.InputStream $in = null;
        try {
            org.omg.CORBA.portable.OutputStream $out = _request("_get_MyType", true);
            $in = _invoke($out);
            org.omg.CosNotifyChannelAdmin.ProxyType $result = org.omg.CosNotifyChannelAdmin.ProxyTypeHelper.read($in);
            return $result;
        } catch (org.omg.CORBA.portable.ApplicationException $ex) {
            $in = $ex.getInputStream();
            String _id = $ex.getId();
            throw new org.omg.CORBA.MARSHAL(_id);
        } catch (org.omg.CORBA.portable.RemarshalException $rm) {
            return MyType();
        } finally {
            _releaseReply($in);
        }
    }

    public org.omg.CosNotifyChannelAdmin.SupplierAdmin MyAdmin() {
        org.omg.CORBA.portable.InputStream $in = null;
        try {
            org.omg.CORBA.portable.OutputStream $out = _request("_get_MyAdmin", true);
            $in = _invoke($out);
            org.omg.CosNotifyChannelAdmin.SupplierAdmin $result = org.omg.CosNotifyChannelAdmin.SupplierAdminHelper.read($in);
            return $result;
        } catch (org.omg.CORBA.portable.ApplicationException $ex) {
            $in = $ex.getInputStream();
            String _id = $ex.getId();
            throw new org.omg.CORBA.MARSHAL(_id);
        } catch (org.omg.CORBA.portable.RemarshalException $rm) {
            return MyAdmin();
        } finally {
            _releaseReply($in);
        }
    }

    public org.omg.CosNotification.EventType[] obtain_subscription_types(org.omg.CosNotifyChannelAdmin.ObtainInfoMode mode) {
        org.omg.CORBA.portable.InputStream $in = null;
        try {
            org.omg.CORBA.portable.OutputStream $out = _request("obtain_subscription_types", true);
            org.omg.CosNotifyChannelAdmin.ObtainInfoModeHelper.write($out, mode);
            $in = _invoke($out);
            org.omg.CosNotification.EventType $result[] = org.omg.CosNotification.EventTypeSeqHelper.read($in);
            return $result;
        } catch (org.omg.CORBA.portable.ApplicationException $ex) {
            $in = $ex.getInputStream();
            String _id = $ex.getId();
            throw new org.omg.CORBA.MARSHAL(_id);
        } catch (org.omg.CORBA.portable.RemarshalException $rm) {
            return obtain_subscription_types(mode);
        } finally {
            _releaseReply($in);
        }
    }

    public void validate_event_qos(org.omg.CosNotification.Property[] required_qos, org.omg.CosNotification.NamedPropertyRangeSeqHolder available_qos) throws org.omg.CosNotification.UnsupportedQoS {
        org.omg.CORBA.portable.InputStream $in = null;
        try {
            org.omg.CORBA.portable.OutputStream $out = _request("validate_event_qos", true);
            org.omg.CosNotification.QoSPropertiesHelper.write($out, required_qos);
            $in = _invoke($out);
            available_qos.value = org.omg.CosNotification.NamedPropertyRangeSeqHelper.read($in);
            return;
        } catch (org.omg.CORBA.portable.ApplicationException $ex) {
            $in = $ex.getInputStream();
            String _id = $ex.getId();
            if (_id.equals("IDL:omg.org/CosNotification/UnsupportedQoS:1.0")) throw org.omg.CosNotification.UnsupportedQoSHelper.read($in); else throw new org.omg.CORBA.MARSHAL(_id);
        } catch (org.omg.CORBA.portable.RemarshalException $rm) {
            validate_event_qos(required_qos, available_qos);
        } finally {
            _releaseReply($in);
        }
    }

    public org.omg.CosNotification.Property[] get_qos() {
        org.omg.CORBA.portable.InputStream $in = null;
        try {
            org.omg.CORBA.portable.OutputStream $out = _request("get_qos", true);
            $in = _invoke($out);
            org.omg.CosNotification.Property $result[] = org.omg.CosNotification.QoSPropertiesHelper.read($in);
            return $result;
        } catch (org.omg.CORBA.portable.ApplicationException $ex) {
            $in = $ex.getInputStream();
            String _id = $ex.getId();
            throw new org.omg.CORBA.MARSHAL(_id);
        } catch (org.omg.CORBA.portable.RemarshalException $rm) {
            return get_qos();
        } finally {
            _releaseReply($in);
        }
    }

    public void set_qos(org.omg.CosNotification.Property[] qos) throws org.omg.CosNotification.UnsupportedQoS {
        org.omg.CORBA.portable.InputStream $in = null;
        try {
            org.omg.CORBA.portable.OutputStream $out = _request("set_qos", true);
            org.omg.CosNotification.QoSPropertiesHelper.write($out, qos);
            $in = _invoke($out);
            return;
        } catch (org.omg.CORBA.portable.ApplicationException $ex) {
            $in = $ex.getInputStream();
            String _id = $ex.getId();
            if (_id.equals("IDL:omg.org/CosNotification/UnsupportedQoS:1.0")) throw org.omg.CosNotification.UnsupportedQoSHelper.read($in); else throw new org.omg.CORBA.MARSHAL(_id);
        } catch (org.omg.CORBA.portable.RemarshalException $rm) {
            set_qos(qos);
        } finally {
            _releaseReply($in);
        }
    }

    public void validate_qos(org.omg.CosNotification.Property[] required_qos, org.omg.CosNotification.NamedPropertyRangeSeqHolder available_qos) throws org.omg.CosNotification.UnsupportedQoS {
        org.omg.CORBA.portable.InputStream $in = null;
        try {
            org.omg.CORBA.portable.OutputStream $out = _request("validate_qos", true);
            org.omg.CosNotification.QoSPropertiesHelper.write($out, required_qos);
            $in = _invoke($out);
            available_qos.value = org.omg.CosNotification.NamedPropertyRangeSeqHelper.read($in);
            return;
        } catch (org.omg.CORBA.portable.ApplicationException $ex) {
            $in = $ex.getInputStream();
            String _id = $ex.getId();
            if (_id.equals("IDL:omg.org/CosNotification/UnsupportedQoS:1.0")) throw org.omg.CosNotification.UnsupportedQoSHelper.read($in); else throw new org.omg.CORBA.MARSHAL(_id);
        } catch (org.omg.CORBA.portable.RemarshalException $rm) {
            validate_qos(required_qos, available_qos);
        } finally {
            _releaseReply($in);
        }
    }

    public int add_filter(org.omg.CosNotifyFilter.Filter new_filter) {
        org.omg.CORBA.portable.InputStream $in = null;
        try {
            org.omg.CORBA.portable.OutputStream $out = _request("add_filter", true);
            org.omg.CosNotifyFilter.FilterHelper.write($out, new_filter);
            $in = _invoke($out);
            int $result = org.omg.CosNotifyFilter.FilterIDHelper.read($in);
            return $result;
        } catch (org.omg.CORBA.portable.ApplicationException $ex) {
            $in = $ex.getInputStream();
            String _id = $ex.getId();
            throw new org.omg.CORBA.MARSHAL(_id);
        } catch (org.omg.CORBA.portable.RemarshalException $rm) {
            return add_filter(new_filter);
        } finally {
            _releaseReply($in);
        }
    }

    public void remove_filter(int filter) throws org.omg.CosNotifyFilter.FilterNotFound {
        org.omg.CORBA.portable.InputStream $in = null;
        try {
            org.omg.CORBA.portable.OutputStream $out = _request("remove_filter", true);
            org.omg.CosNotifyFilter.FilterIDHelper.write($out, filter);
            $in = _invoke($out);
            return;
        } catch (org.omg.CORBA.portable.ApplicationException $ex) {
            $in = $ex.getInputStream();
            String _id = $ex.getId();
            if (_id.equals("IDL:omg.org/CosNotifyFilter/FilterNotFound:1.0")) throw org.omg.CosNotifyFilter.FilterNotFoundHelper.read($in); else throw new org.omg.CORBA.MARSHAL(_id);
        } catch (org.omg.CORBA.portable.RemarshalException $rm) {
            remove_filter(filter);
        } finally {
            _releaseReply($in);
        }
    }

    public org.omg.CosNotifyFilter.Filter get_filter(int filter) throws org.omg.CosNotifyFilter.FilterNotFound {
        org.omg.CORBA.portable.InputStream $in = null;
        try {
            org.omg.CORBA.portable.OutputStream $out = _request("get_filter", true);
            org.omg.CosNotifyFilter.FilterIDHelper.write($out, filter);
            $in = _invoke($out);
            org.omg.CosNotifyFilter.Filter $result = org.omg.CosNotifyFilter.FilterHelper.read($in);
            return $result;
        } catch (org.omg.CORBA.portable.ApplicationException $ex) {
            $in = $ex.getInputStream();
            String _id = $ex.getId();
            if (_id.equals("IDL:omg.org/CosNotifyFilter/FilterNotFound:1.0")) throw org.omg.CosNotifyFilter.FilterNotFoundHelper.read($in); else throw new org.omg.CORBA.MARSHAL(_id);
        } catch (org.omg.CORBA.portable.RemarshalException $rm) {
            return get_filter(filter);
        } finally {
            _releaseReply($in);
        }
    }

    public int[] get_all_filters() {
        org.omg.CORBA.portable.InputStream $in = null;
        try {
            org.omg.CORBA.portable.OutputStream $out = _request("get_all_filters", true);
            $in = _invoke($out);
            int $result[] = org.omg.CosNotifyFilter.FilterIDSeqHelper.read($in);
            return $result;
        } catch (org.omg.CORBA.portable.ApplicationException $ex) {
            $in = $ex.getInputStream();
            String _id = $ex.getId();
            throw new org.omg.CORBA.MARSHAL(_id);
        } catch (org.omg.CORBA.portable.RemarshalException $rm) {
            return get_all_filters();
        } finally {
            _releaseReply($in);
        }
    }

    public void remove_all_filters() {
        org.omg.CORBA.portable.InputStream $in = null;
        try {
            org.omg.CORBA.portable.OutputStream $out = _request("remove_all_filters", true);
            $in = _invoke($out);
            return;
        } catch (org.omg.CORBA.portable.ApplicationException $ex) {
            $in = $ex.getInputStream();
            String _id = $ex.getId();
            throw new org.omg.CORBA.MARSHAL(_id);
        } catch (org.omg.CORBA.portable.RemarshalException $rm) {
            remove_all_filters();
        } finally {
            _releaseReply($in);
        }
    }

    public void push_structured_events(org.omg.CosNotification.StructuredEvent[] notifications) throws org.omg.CosEventComm.Disconnected {
        org.omg.CORBA.portable.InputStream $in = null;
        try {
            org.omg.CORBA.portable.OutputStream $out = _request("push_structured_events", true);
            org.omg.CosNotification.EventBatchHelper.write($out, notifications);
            $in = _invoke($out);
            return;
        } catch (org.omg.CORBA.portable.ApplicationException $ex) {
            $in = $ex.getInputStream();
            String _id = $ex.getId();
            if (_id.equals("IDL:omg.org/CosEventComm/Disconnected:1.0")) throw org.omg.CosEventComm.DisconnectedHelper.read($in); else throw new org.omg.CORBA.MARSHAL(_id);
        } catch (org.omg.CORBA.portable.RemarshalException $rm) {
            push_structured_events(notifications);
        } finally {
            _releaseReply($in);
        }
    }

    public void disconnect_sequence_push_consumer() {
        org.omg.CORBA.portable.InputStream $in = null;
        try {
            org.omg.CORBA.portable.OutputStream $out = _request("disconnect_sequence_push_consumer", true);
            $in = _invoke($out);
            return;
        } catch (org.omg.CORBA.portable.ApplicationException $ex) {
            $in = $ex.getInputStream();
            String _id = $ex.getId();
            throw new org.omg.CORBA.MARSHAL(_id);
        } catch (org.omg.CORBA.portable.RemarshalException $rm) {
            disconnect_sequence_push_consumer();
        } finally {
            _releaseReply($in);
        }
    }

    public void offer_change(org.omg.CosNotification.EventType[] added, org.omg.CosNotification.EventType[] removed) throws org.omg.CosNotifyComm.InvalidEventType {
        org.omg.CORBA.portable.InputStream $in = null;
        try {
            org.omg.CORBA.portable.OutputStream $out = _request("offer_change", true);
            org.omg.CosNotification.EventTypeSeqHelper.write($out, added);
            org.omg.CosNotification.EventTypeSeqHelper.write($out, removed);
            $in = _invoke($out);
            return;
        } catch (org.omg.CORBA.portable.ApplicationException $ex) {
            $in = $ex.getInputStream();
            String _id = $ex.getId();
            if (_id.equals("IDL:omg.org/CosNotifyComm/InvalidEventType:1.0")) throw org.omg.CosNotifyComm.InvalidEventTypeHelper.read($in); else throw new org.omg.CORBA.MARSHAL(_id);
        } catch (org.omg.CORBA.portable.RemarshalException $rm) {
            offer_change(added, removed);
        } finally {
            _releaseReply($in);
        }
    }

    private static String[] __ids = { "IDL:omg.org/CosNotifyChannelAdmin/SequenceProxyPushConsumer:1.0", "IDL:omg.org/CosNotifyChannelAdmin/ProxyConsumer:1.0", "IDL:omg.org/CosNotification/QoSAdmin:1.0", "IDL:omg.org/CosNotifyFilter/FilterAdmin:1.0", "IDL:omg.org/CosNotifyComm/SequencePushConsumer:1.0", "IDL:omg.org/CosNotifyComm/NotifyPublish:1.0" };

    public String[] _ids() {
        return (String[]) __ids.clone();
    }

    private void readObject(java.io.ObjectInputStream s) throws java.io.IOException {
        String str = s.readUTF();
        String[] args = null;
        java.util.Properties props = null;
        org.omg.CORBA.Object obj = org.omg.CORBA.ORB.init(args, props).string_to_object(str);
        org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl) obj)._get_delegate();
        _set_delegate(delegate);
    }

    private void writeObject(java.io.ObjectOutputStream s) throws java.io.IOException {
        String[] args = null;
        java.util.Properties props = null;
        String str = org.omg.CORBA.ORB.init(args, props).object_to_string(this);
        s.writeUTF(str);
    }
}
