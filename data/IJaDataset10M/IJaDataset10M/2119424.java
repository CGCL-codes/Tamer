package org.omg.CosTypedNotifyChannelAdmin;

public class _TypedEventChannelFactoryStub extends org.omg.CORBA.portable.ObjectImpl implements TypedEventChannelFactory {

    private static final String[] _ob_ids_ = { "IDL:omg.org/CosTypedNotifyChannelAdmin/TypedEventChannelFactory:1.0" };

    public String[] _ids() {
        return _ob_ids_;
    }

    public static final java.lang.Class _ob_opsClass = TypedEventChannelFactoryOperations.class;

    public TypedEventChannel create_typed_channel(org.omg.CosNotification.Property[] _ob_a0, org.omg.CosNotification.Property[] _ob_a1, org.omg.CORBA.IntHolder _ob_ah2) throws org.omg.CosNotification.UnsupportedQoS, org.omg.CosNotification.UnsupportedAdmin {
        while (true) {
            if (!this._is_local()) {
                org.omg.CORBA.portable.OutputStream out = null;
                org.omg.CORBA.portable.InputStream in = null;
                try {
                    out = _request("create_typed_channel", true);
                    org.omg.CosNotification.QoSPropertiesHelper.write(out, _ob_a0);
                    org.omg.CosNotification.AdminPropertiesHelper.write(out, _ob_a1);
                    in = _invoke(out);
                    TypedEventChannel _ob_r = TypedEventChannelHelper.read(in);
                    _ob_ah2.value = org.omg.CosNotifyChannelAdmin.ChannelIDHelper.read(in);
                    return _ob_r;
                } catch (org.omg.CORBA.portable.RemarshalException _ob_ex) {
                    continue;
                } catch (org.omg.CORBA.portable.ApplicationException _ob_aex) {
                    final String _ob_id = _ob_aex.getId();
                    in = _ob_aex.getInputStream();
                    if (_ob_id.equals(org.omg.CosNotification.UnsupportedQoSHelper.id())) throw org.omg.CosNotification.UnsupportedQoSHelper.read(in);
                    if (_ob_id.equals(org.omg.CosNotification.UnsupportedAdminHelper.id())) throw org.omg.CosNotification.UnsupportedAdminHelper.read(in);
                    throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: " + _ob_id);
                } finally {
                    _releaseReply(in);
                }
            } else {
                org.omg.CORBA.portable.ServantObject _ob_so = _servant_preinvoke("create_typed_channel", _ob_opsClass);
                if (_ob_so == null) continue;
                TypedEventChannelFactoryOperations _ob_self = (TypedEventChannelFactoryOperations) _ob_so.servant;
                try {
                    return _ob_self.create_typed_channel(_ob_a0, _ob_a1, _ob_ah2);
                } finally {
                    _servant_postinvoke(_ob_so);
                }
            }
        }
    }

    public int[] get_all_typed_channels() {
        while (true) {
            if (!this._is_local()) {
                org.omg.CORBA.portable.OutputStream out = null;
                org.omg.CORBA.portable.InputStream in = null;
                try {
                    out = _request("get_all_typed_channels", true);
                    in = _invoke(out);
                    int[] _ob_r = org.omg.CosNotifyChannelAdmin.ChannelIDSeqHelper.read(in);
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
                org.omg.CORBA.portable.ServantObject _ob_so = _servant_preinvoke("get_all_typed_channels", _ob_opsClass);
                if (_ob_so == null) continue;
                TypedEventChannelFactoryOperations _ob_self = (TypedEventChannelFactoryOperations) _ob_so.servant;
                try {
                    return _ob_self.get_all_typed_channels();
                } finally {
                    _servant_postinvoke(_ob_so);
                }
            }
        }
    }

    public TypedEventChannel get_typed_event_channel(int _ob_a0) throws org.omg.CosNotifyChannelAdmin.ChannelNotFound {
        while (true) {
            if (!this._is_local()) {
                org.omg.CORBA.portable.OutputStream out = null;
                org.omg.CORBA.portable.InputStream in = null;
                try {
                    out = _request("get_typed_event_channel", true);
                    org.omg.CosNotifyChannelAdmin.ChannelIDHelper.write(out, _ob_a0);
                    in = _invoke(out);
                    TypedEventChannel _ob_r = TypedEventChannelHelper.read(in);
                    return _ob_r;
                } catch (org.omg.CORBA.portable.RemarshalException _ob_ex) {
                    continue;
                } catch (org.omg.CORBA.portable.ApplicationException _ob_aex) {
                    final String _ob_id = _ob_aex.getId();
                    in = _ob_aex.getInputStream();
                    if (_ob_id.equals(org.omg.CosNotifyChannelAdmin.ChannelNotFoundHelper.id())) throw org.omg.CosNotifyChannelAdmin.ChannelNotFoundHelper.read(in);
                    throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: " + _ob_id);
                } finally {
                    _releaseReply(in);
                }
            } else {
                org.omg.CORBA.portable.ServantObject _ob_so = _servant_preinvoke("get_typed_event_channel", _ob_opsClass);
                if (_ob_so == null) continue;
                TypedEventChannelFactoryOperations _ob_self = (TypedEventChannelFactoryOperations) _ob_so.servant;
                try {
                    return _ob_self.get_typed_event_channel(_ob_a0);
                } finally {
                    _servant_postinvoke(_ob_so);
                }
            }
        }
    }
}
