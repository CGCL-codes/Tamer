package org.omg.CosTypedNotifyChannelAdmin;

public class _TypedProxyPullConsumerStub extends org.omg.CORBA.portable.ObjectImpl implements TypedProxyPullConsumer {

    private static final String[] _ob_ids_ = { "IDL:omg.org/CosTypedNotifyChannelAdmin/TypedProxyPullConsumer:1.0", "IDL:omg.org/CosNotifyChannelAdmin/ProxyConsumer:1.0", "IDL:omg.org/CosNotification/QoSAdmin:1.0", "IDL:omg.org/CosNotifyFilter/FilterAdmin:1.0", "IDL:omg.org/CosNotifyComm/PullConsumer:1.0", "IDL:omg.org/CosNotifyComm/NotifyPublish:1.0", "IDL:omg.org/CosEventComm/PullConsumer:1.0" };

    public String[] _ids() {
        return _ob_ids_;
    }

    public static final java.lang.Class _ob_opsClass = TypedProxyPullConsumerOperations.class;

    public org.omg.CosNotifyChannelAdmin.ProxyType MyType() {
        while (true) {
            if (!this._is_local()) {
                org.omg.CORBA.portable.OutputStream out = null;
                org.omg.CORBA.portable.InputStream in = null;
                try {
                    out = _request("_get_MyType", true);
                    in = _invoke(out);
                    org.omg.CosNotifyChannelAdmin.ProxyType _ob_r = org.omg.CosNotifyChannelAdmin.ProxyTypeHelper.read(in);
                    return _ob_r;
                } catch (org.omg.CORBA.portable.RemarshalException _ob_ex) {
                    continue;
                } catch (org.omg.CORBA.portable.ApplicationException _ob_aex) {
                    final String _ob_id = _ob_aex.getId();
                    throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: " + _ob_id);
                } finally {
                    _releaseReply(in);
                }
            } else {
                org.omg.CORBA.portable.ServantObject _ob_so = _servant_preinvoke("MyType", _ob_opsClass);
                if (_ob_so == null) continue;
                TypedProxyPullConsumerOperations _ob_self = (TypedProxyPullConsumerOperations) _ob_so.servant;
                try {
                    return _ob_self.MyType();
                } finally {
                    _servant_postinvoke(_ob_so);
                }
            }
        }
    }

    public org.omg.CosNotifyChannelAdmin.SupplierAdmin MyAdmin() {
        while (true) {
            if (!this._is_local()) {
                org.omg.CORBA.portable.OutputStream out = null;
                org.omg.CORBA.portable.InputStream in = null;
                try {
                    out = _request("_get_MyAdmin", true);
                    in = _invoke(out);
                    org.omg.CosNotifyChannelAdmin.SupplierAdmin _ob_r = org.omg.CosNotifyChannelAdmin.SupplierAdminHelper.read(in);
                    return _ob_r;
                } catch (org.omg.CORBA.portable.RemarshalException _ob_ex) {
                    continue;
                } catch (org.omg.CORBA.portable.ApplicationException _ob_aex) {
                    final String _ob_id = _ob_aex.getId();
                    throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: " + _ob_id);
                } finally {
                    _releaseReply(in);
                }
            } else {
                org.omg.CORBA.portable.ServantObject _ob_so = _servant_preinvoke("MyAdmin", _ob_opsClass);
                if (_ob_so == null) continue;
                TypedProxyPullConsumerOperations _ob_self = (TypedProxyPullConsumerOperations) _ob_so.servant;
                try {
                    return _ob_self.MyAdmin();
                } finally {
                    _servant_postinvoke(_ob_so);
                }
            }
        }
    }

    public void connect_typed_pull_supplier(org.omg.CosTypedEventComm.TypedPullSupplier _ob_a0) throws org.omg.CosEventChannelAdmin.AlreadyConnected, org.omg.CosEventChannelAdmin.TypeError {
        while (true) {
            if (!this._is_local()) {
                org.omg.CORBA.portable.OutputStream out = null;
                org.omg.CORBA.portable.InputStream in = null;
                try {
                    out = _request("connect_typed_pull_supplier", true);
                    org.omg.CosTypedEventComm.TypedPullSupplierHelper.write(out, _ob_a0);
                    in = _invoke(out);
                    return;
                } catch (org.omg.CORBA.portable.RemarshalException _ob_ex) {
                    continue;
                } catch (org.omg.CORBA.portable.ApplicationException _ob_aex) {
                    final String _ob_id = _ob_aex.getId();
                    in = _ob_aex.getInputStream();
                    if (_ob_id.equals(org.omg.CosEventChannelAdmin.AlreadyConnectedHelper.id())) throw org.omg.CosEventChannelAdmin.AlreadyConnectedHelper.read(in);
                    if (_ob_id.equals(org.omg.CosEventChannelAdmin.TypeErrorHelper.id())) throw org.omg.CosEventChannelAdmin.TypeErrorHelper.read(in);
                    throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: " + _ob_id);
                } finally {
                    _releaseReply(in);
                }
            } else {
                org.omg.CORBA.portable.ServantObject _ob_so = _servant_preinvoke("connect_typed_pull_supplier", _ob_opsClass);
                if (_ob_so == null) continue;
                TypedProxyPullConsumerOperations _ob_self = (TypedProxyPullConsumerOperations) _ob_so.servant;
                try {
                    _ob_self.connect_typed_pull_supplier(_ob_a0);
                    return;
                } finally {
                    _servant_postinvoke(_ob_so);
                }
            }
        }
    }

    public void suspend_connection() throws org.omg.CosNotifyChannelAdmin.ConnectionAlreadyInactive {
        while (true) {
            if (!this._is_local()) {
                org.omg.CORBA.portable.OutputStream out = null;
                org.omg.CORBA.portable.InputStream in = null;
                try {
                    out = _request("suspend_connection", true);
                    in = _invoke(out);
                    return;
                } catch (org.omg.CORBA.portable.RemarshalException _ob_ex) {
                    continue;
                } catch (org.omg.CORBA.portable.ApplicationException _ob_aex) {
                    final String _ob_id = _ob_aex.getId();
                    in = _ob_aex.getInputStream();
                    if (_ob_id.equals(org.omg.CosNotifyChannelAdmin.ConnectionAlreadyInactiveHelper.id())) throw org.omg.CosNotifyChannelAdmin.ConnectionAlreadyInactiveHelper.read(in);
                    throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: " + _ob_id);
                } finally {
                    _releaseReply(in);
                }
            } else {
                org.omg.CORBA.portable.ServantObject _ob_so = _servant_preinvoke("suspend_connection", _ob_opsClass);
                if (_ob_so == null) continue;
                TypedProxyPullConsumerOperations _ob_self = (TypedProxyPullConsumerOperations) _ob_so.servant;
                try {
                    _ob_self.suspend_connection();
                    return;
                } finally {
                    _servant_postinvoke(_ob_so);
                }
            }
        }
    }

    public void resume_connection() throws org.omg.CosNotifyChannelAdmin.ConnectionAlreadyActive {
        while (true) {
            if (!this._is_local()) {
                org.omg.CORBA.portable.OutputStream out = null;
                org.omg.CORBA.portable.InputStream in = null;
                try {
                    out = _request("resume_connection", true);
                    in = _invoke(out);
                    return;
                } catch (org.omg.CORBA.portable.RemarshalException _ob_ex) {
                    continue;
                } catch (org.omg.CORBA.portable.ApplicationException _ob_aex) {
                    final String _ob_id = _ob_aex.getId();
                    in = _ob_aex.getInputStream();
                    if (_ob_id.equals(org.omg.CosNotifyChannelAdmin.ConnectionAlreadyActiveHelper.id())) throw org.omg.CosNotifyChannelAdmin.ConnectionAlreadyActiveHelper.read(in);
                    throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: " + _ob_id);
                } finally {
                    _releaseReply(in);
                }
            } else {
                org.omg.CORBA.portable.ServantObject _ob_so = _servant_preinvoke("resume_connection", _ob_opsClass);
                if (_ob_so == null) continue;
                TypedProxyPullConsumerOperations _ob_self = (TypedProxyPullConsumerOperations) _ob_so.servant;
                try {
                    _ob_self.resume_connection();
                    return;
                } finally {
                    _servant_postinvoke(_ob_so);
                }
            }
        }
    }

    public org.omg.CosNotification.EventType[] obtain_subscription_types(org.omg.CosNotifyChannelAdmin.ObtainInfoMode _ob_a0) {
        while (true) {
            if (!this._is_local()) {
                org.omg.CORBA.portable.OutputStream out = null;
                org.omg.CORBA.portable.InputStream in = null;
                try {
                    out = _request("obtain_subscription_types", true);
                    org.omg.CosNotifyChannelAdmin.ObtainInfoModeHelper.write(out, _ob_a0);
                    in = _invoke(out);
                    org.omg.CosNotification.EventType[] _ob_r = org.omg.CosNotification.EventTypeSeqHelper.read(in);
                    return _ob_r;
                } catch (org.omg.CORBA.portable.RemarshalException _ob_ex) {
                    continue;
                } catch (org.omg.CORBA.portable.ApplicationException _ob_aex) {
                    final String _ob_id = _ob_aex.getId();
                    in = _ob_aex.getInputStream();
                    throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: " + _ob_id);
                } finally {
                    _releaseReply(in);
                }
            } else {
                org.omg.CORBA.portable.ServantObject _ob_so = _servant_preinvoke("obtain_subscription_types", _ob_opsClass);
                if (_ob_so == null) continue;
                TypedProxyPullConsumerOperations _ob_self = (TypedProxyPullConsumerOperations) _ob_so.servant;
                try {
                    return _ob_self.obtain_subscription_types(_ob_a0);
                } finally {
                    _servant_postinvoke(_ob_so);
                }
            }
        }
    }

    public void validate_event_qos(org.omg.CosNotification.Property[] _ob_a0, org.omg.CosNotification.NamedPropertyRangeSeqHolder _ob_ah1) throws org.omg.CosNotification.UnsupportedQoS {
        while (true) {
            if (!this._is_local()) {
                org.omg.CORBA.portable.OutputStream out = null;
                org.omg.CORBA.portable.InputStream in = null;
                try {
                    out = _request("validate_event_qos", true);
                    org.omg.CosNotification.QoSPropertiesHelper.write(out, _ob_a0);
                    in = _invoke(out);
                    _ob_ah1.value = org.omg.CosNotification.NamedPropertyRangeSeqHelper.read(in);
                    return;
                } catch (org.omg.CORBA.portable.RemarshalException _ob_ex) {
                    continue;
                } catch (org.omg.CORBA.portable.ApplicationException _ob_aex) {
                    final String _ob_id = _ob_aex.getId();
                    in = _ob_aex.getInputStream();
                    if (_ob_id.equals(org.omg.CosNotification.UnsupportedQoSHelper.id())) throw org.omg.CosNotification.UnsupportedQoSHelper.read(in);
                    throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: " + _ob_id);
                } finally {
                    _releaseReply(in);
                }
            } else {
                org.omg.CORBA.portable.ServantObject _ob_so = _servant_preinvoke("validate_event_qos", _ob_opsClass);
                if (_ob_so == null) continue;
                TypedProxyPullConsumerOperations _ob_self = (TypedProxyPullConsumerOperations) _ob_so.servant;
                try {
                    _ob_self.validate_event_qos(_ob_a0, _ob_ah1);
                    return;
                } finally {
                    _servant_postinvoke(_ob_so);
                }
            }
        }
    }

    public org.omg.CosNotification.Property[] get_qos() {
        while (true) {
            if (!this._is_local()) {
                org.omg.CORBA.portable.OutputStream out = null;
                org.omg.CORBA.portable.InputStream in = null;
                try {
                    out = _request("get_qos", true);
                    in = _invoke(out);
                    org.omg.CosNotification.Property[] _ob_r = org.omg.CosNotification.QoSPropertiesHelper.read(in);
                    return _ob_r;
                } catch (org.omg.CORBA.portable.RemarshalException _ob_ex) {
                    continue;
                } catch (org.omg.CORBA.portable.ApplicationException _ob_aex) {
                    final String _ob_id = _ob_aex.getId();
                    in = _ob_aex.getInputStream();
                    throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: " + _ob_id);
                } finally {
                    _releaseReply(in);
                }
            } else {
                org.omg.CORBA.portable.ServantObject _ob_so = _servant_preinvoke("get_qos", _ob_opsClass);
                if (_ob_so == null) continue;
                TypedProxyPullConsumerOperations _ob_self = (TypedProxyPullConsumerOperations) _ob_so.servant;
                try {
                    return _ob_self.get_qos();
                } finally {
                    _servant_postinvoke(_ob_so);
                }
            }
        }
    }

    public void set_qos(org.omg.CosNotification.Property[] _ob_a0) throws org.omg.CosNotification.UnsupportedQoS {
        while (true) {
            if (!this._is_local()) {
                org.omg.CORBA.portable.OutputStream out = null;
                org.omg.CORBA.portable.InputStream in = null;
                try {
                    out = _request("set_qos", true);
                    org.omg.CosNotification.QoSPropertiesHelper.write(out, _ob_a0);
                    in = _invoke(out);
                    return;
                } catch (org.omg.CORBA.portable.RemarshalException _ob_ex) {
                    continue;
                } catch (org.omg.CORBA.portable.ApplicationException _ob_aex) {
                    final String _ob_id = _ob_aex.getId();
                    in = _ob_aex.getInputStream();
                    if (_ob_id.equals(org.omg.CosNotification.UnsupportedQoSHelper.id())) throw org.omg.CosNotification.UnsupportedQoSHelper.read(in);
                    throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: " + _ob_id);
                } finally {
                    _releaseReply(in);
                }
            } else {
                org.omg.CORBA.portable.ServantObject _ob_so = _servant_preinvoke("set_qos", _ob_opsClass);
                if (_ob_so == null) continue;
                TypedProxyPullConsumerOperations _ob_self = (TypedProxyPullConsumerOperations) _ob_so.servant;
                try {
                    _ob_self.set_qos(_ob_a0);
                    return;
                } finally {
                    _servant_postinvoke(_ob_so);
                }
            }
        }
    }

    public void validate_qos(org.omg.CosNotification.Property[] _ob_a0, org.omg.CosNotification.NamedPropertyRangeSeqHolder _ob_ah1) throws org.omg.CosNotification.UnsupportedQoS {
        while (true) {
            if (!this._is_local()) {
                org.omg.CORBA.portable.OutputStream out = null;
                org.omg.CORBA.portable.InputStream in = null;
                try {
                    out = _request("validate_qos", true);
                    org.omg.CosNotification.QoSPropertiesHelper.write(out, _ob_a0);
                    in = _invoke(out);
                    _ob_ah1.value = org.omg.CosNotification.NamedPropertyRangeSeqHelper.read(in);
                    return;
                } catch (org.omg.CORBA.portable.RemarshalException _ob_ex) {
                    continue;
                } catch (org.omg.CORBA.portable.ApplicationException _ob_aex) {
                    final String _ob_id = _ob_aex.getId();
                    in = _ob_aex.getInputStream();
                    if (_ob_id.equals(org.omg.CosNotification.UnsupportedQoSHelper.id())) throw org.omg.CosNotification.UnsupportedQoSHelper.read(in);
                    throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: " + _ob_id);
                } finally {
                    _releaseReply(in);
                }
            } else {
                org.omg.CORBA.portable.ServantObject _ob_so = _servant_preinvoke("validate_qos", _ob_opsClass);
                if (_ob_so == null) continue;
                TypedProxyPullConsumerOperations _ob_self = (TypedProxyPullConsumerOperations) _ob_so.servant;
                try {
                    _ob_self.validate_qos(_ob_a0, _ob_ah1);
                    return;
                } finally {
                    _servant_postinvoke(_ob_so);
                }
            }
        }
    }

    public int add_filter(org.omg.CosNotifyFilter.Filter _ob_a0) {
        while (true) {
            if (!this._is_local()) {
                org.omg.CORBA.portable.OutputStream out = null;
                org.omg.CORBA.portable.InputStream in = null;
                try {
                    out = _request("add_filter", true);
                    org.omg.CosNotifyFilter.FilterHelper.write(out, _ob_a0);
                    in = _invoke(out);
                    int _ob_r = org.omg.CosNotifyFilter.FilterIDHelper.read(in);
                    return _ob_r;
                } catch (org.omg.CORBA.portable.RemarshalException _ob_ex) {
                    continue;
                } catch (org.omg.CORBA.portable.ApplicationException _ob_aex) {
                    final String _ob_id = _ob_aex.getId();
                    in = _ob_aex.getInputStream();
                    throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: " + _ob_id);
                } finally {
                    _releaseReply(in);
                }
            } else {
                org.omg.CORBA.portable.ServantObject _ob_so = _servant_preinvoke("add_filter", _ob_opsClass);
                if (_ob_so == null) continue;
                TypedProxyPullConsumerOperations _ob_self = (TypedProxyPullConsumerOperations) _ob_so.servant;
                try {
                    return _ob_self.add_filter(_ob_a0);
                } finally {
                    _servant_postinvoke(_ob_so);
                }
            }
        }
    }

    public void remove_filter(int _ob_a0) throws org.omg.CosNotifyFilter.FilterNotFound {
        while (true) {
            if (!this._is_local()) {
                org.omg.CORBA.portable.OutputStream out = null;
                org.omg.CORBA.portable.InputStream in = null;
                try {
                    out = _request("remove_filter", true);
                    org.omg.CosNotifyFilter.FilterIDHelper.write(out, _ob_a0);
                    in = _invoke(out);
                    return;
                } catch (org.omg.CORBA.portable.RemarshalException _ob_ex) {
                    continue;
                } catch (org.omg.CORBA.portable.ApplicationException _ob_aex) {
                    final String _ob_id = _ob_aex.getId();
                    in = _ob_aex.getInputStream();
                    if (_ob_id.equals(org.omg.CosNotifyFilter.FilterNotFoundHelper.id())) throw org.omg.CosNotifyFilter.FilterNotFoundHelper.read(in);
                    throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: " + _ob_id);
                } finally {
                    _releaseReply(in);
                }
            } else {
                org.omg.CORBA.portable.ServantObject _ob_so = _servant_preinvoke("remove_filter", _ob_opsClass);
                if (_ob_so == null) continue;
                TypedProxyPullConsumerOperations _ob_self = (TypedProxyPullConsumerOperations) _ob_so.servant;
                try {
                    _ob_self.remove_filter(_ob_a0);
                    return;
                } finally {
                    _servant_postinvoke(_ob_so);
                }
            }
        }
    }

    public org.omg.CosNotifyFilter.Filter get_filter(int _ob_a0) throws org.omg.CosNotifyFilter.FilterNotFound {
        while (true) {
            if (!this._is_local()) {
                org.omg.CORBA.portable.OutputStream out = null;
                org.omg.CORBA.portable.InputStream in = null;
                try {
                    out = _request("get_filter", true);
                    org.omg.CosNotifyFilter.FilterIDHelper.write(out, _ob_a0);
                    in = _invoke(out);
                    org.omg.CosNotifyFilter.Filter _ob_r = org.omg.CosNotifyFilter.FilterHelper.read(in);
                    return _ob_r;
                } catch (org.omg.CORBA.portable.RemarshalException _ob_ex) {
                    continue;
                } catch (org.omg.CORBA.portable.ApplicationException _ob_aex) {
                    final String _ob_id = _ob_aex.getId();
                    in = _ob_aex.getInputStream();
                    if (_ob_id.equals(org.omg.CosNotifyFilter.FilterNotFoundHelper.id())) throw org.omg.CosNotifyFilter.FilterNotFoundHelper.read(in);
                    throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: " + _ob_id);
                } finally {
                    _releaseReply(in);
                }
            } else {
                org.omg.CORBA.portable.ServantObject _ob_so = _servant_preinvoke("get_filter", _ob_opsClass);
                if (_ob_so == null) continue;
                TypedProxyPullConsumerOperations _ob_self = (TypedProxyPullConsumerOperations) _ob_so.servant;
                try {
                    return _ob_self.get_filter(_ob_a0);
                } finally {
                    _servant_postinvoke(_ob_so);
                }
            }
        }
    }

    public int[] get_all_filters() {
        while (true) {
            if (!this._is_local()) {
                org.omg.CORBA.portable.OutputStream out = null;
                org.omg.CORBA.portable.InputStream in = null;
                try {
                    out = _request("get_all_filters", true);
                    in = _invoke(out);
                    int[] _ob_r = org.omg.CosNotifyFilter.FilterIDSeqHelper.read(in);
                    return _ob_r;
                } catch (org.omg.CORBA.portable.RemarshalException _ob_ex) {
                    continue;
                } catch (org.omg.CORBA.portable.ApplicationException _ob_aex) {
                    final String _ob_id = _ob_aex.getId();
                    in = _ob_aex.getInputStream();
                    throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: " + _ob_id);
                } finally {
                    _releaseReply(in);
                }
            } else {
                org.omg.CORBA.portable.ServantObject _ob_so = _servant_preinvoke("get_all_filters", _ob_opsClass);
                if (_ob_so == null) continue;
                TypedProxyPullConsumerOperations _ob_self = (TypedProxyPullConsumerOperations) _ob_so.servant;
                try {
                    return _ob_self.get_all_filters();
                } finally {
                    _servant_postinvoke(_ob_so);
                }
            }
        }
    }

    public void remove_all_filters() {
        while (true) {
            if (!this._is_local()) {
                org.omg.CORBA.portable.OutputStream out = null;
                org.omg.CORBA.portable.InputStream in = null;
                try {
                    out = _request("remove_all_filters", true);
                    in = _invoke(out);
                    return;
                } catch (org.omg.CORBA.portable.RemarshalException _ob_ex) {
                    continue;
                } catch (org.omg.CORBA.portable.ApplicationException _ob_aex) {
                    final String _ob_id = _ob_aex.getId();
                    in = _ob_aex.getInputStream();
                    throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: " + _ob_id);
                } finally {
                    _releaseReply(in);
                }
            } else {
                org.omg.CORBA.portable.ServantObject _ob_so = _servant_preinvoke("remove_all_filters", _ob_opsClass);
                if (_ob_so == null) continue;
                TypedProxyPullConsumerOperations _ob_self = (TypedProxyPullConsumerOperations) _ob_so.servant;
                try {
                    _ob_self.remove_all_filters();
                    return;
                } finally {
                    _servant_postinvoke(_ob_so);
                }
            }
        }
    }

    public void offer_change(org.omg.CosNotification.EventType[] _ob_a0, org.omg.CosNotification.EventType[] _ob_a1) throws org.omg.CosNotifyComm.InvalidEventType {
        while (true) {
            if (!this._is_local()) {
                org.omg.CORBA.portable.OutputStream out = null;
                org.omg.CORBA.portable.InputStream in = null;
                try {
                    out = _request("offer_change", true);
                    org.omg.CosNotification.EventTypeSeqHelper.write(out, _ob_a0);
                    org.omg.CosNotification.EventTypeSeqHelper.write(out, _ob_a1);
                    in = _invoke(out);
                    return;
                } catch (org.omg.CORBA.portable.RemarshalException _ob_ex) {
                    continue;
                } catch (org.omg.CORBA.portable.ApplicationException _ob_aex) {
                    final String _ob_id = _ob_aex.getId();
                    in = _ob_aex.getInputStream();
                    if (_ob_id.equals(org.omg.CosNotifyComm.InvalidEventTypeHelper.id())) throw org.omg.CosNotifyComm.InvalidEventTypeHelper.read(in);
                    throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: " + _ob_id);
                } finally {
                    _releaseReply(in);
                }
            } else {
                org.omg.CORBA.portable.ServantObject _ob_so = _servant_preinvoke("offer_change", _ob_opsClass);
                if (_ob_so == null) continue;
                TypedProxyPullConsumerOperations _ob_self = (TypedProxyPullConsumerOperations) _ob_so.servant;
                try {
                    _ob_self.offer_change(_ob_a0, _ob_a1);
                    return;
                } finally {
                    _servant_postinvoke(_ob_so);
                }
            }
        }
    }

    public void disconnect_pull_consumer() {
        while (true) {
            if (!this._is_local()) {
                org.omg.CORBA.portable.OutputStream out = null;
                org.omg.CORBA.portable.InputStream in = null;
                try {
                    out = _request("disconnect_pull_consumer", true);
                    in = _invoke(out);
                    return;
                } catch (org.omg.CORBA.portable.RemarshalException _ob_ex) {
                    continue;
                } catch (org.omg.CORBA.portable.ApplicationException _ob_aex) {
                    final String _ob_id = _ob_aex.getId();
                    in = _ob_aex.getInputStream();
                    throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: " + _ob_id);
                } finally {
                    _releaseReply(in);
                }
            } else {
                org.omg.CORBA.portable.ServantObject _ob_so = _servant_preinvoke("disconnect_pull_consumer", _ob_opsClass);
                if (_ob_so == null) continue;
                TypedProxyPullConsumerOperations _ob_self = (TypedProxyPullConsumerOperations) _ob_so.servant;
                try {
                    _ob_self.disconnect_pull_consumer();
                    return;
                } finally {
                    _servant_postinvoke(_ob_so);
                }
            }
        }
    }
}
