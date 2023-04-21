package org.omg.CosNotifyComm;

/**
 *	Generated from IDL definition of interface "StructuredPullConsumer"
 *	@author JacORB IDL compiler 
 */
public class _StructuredPullConsumerStub extends org.omg.CORBA.portable.ObjectImpl implements org.omg.CosNotifyComm.StructuredPullConsumer {

    private String[] ids = { "IDL:omg.org/CosNotifyComm/StructuredPullConsumer:1.0", "IDL:omg.org/CosNotifyComm/NotifyPublish:1.0", "IDL:omg.org/CORBA/Object:1.0" };

    public String[] _ids() {
        return ids;
    }

    public static final java.lang.Class _opsClass = org.omg.CosNotifyComm.StructuredPullConsumerOperations.class;

    public void disconnect_structured_pull_consumer() {
        while (true) {
            if (!this._is_local()) {
                org.omg.CORBA.portable.InputStream _is = null;
                try {
                    org.omg.CORBA.portable.OutputStream _os = _request("disconnect_structured_pull_consumer", true);
                    _is = _invoke(_os);
                    return;
                } catch (org.omg.CORBA.portable.RemarshalException _rx) {
                } catch (org.omg.CORBA.portable.ApplicationException _ax) {
                    String _id = _ax.getId();
                    throw new RuntimeException("Unexpected exception " + _id);
                } finally {
                    this._releaseReply(_is);
                }
            } else {
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("disconnect_structured_pull_consumer", _opsClass);
                if (_so == null) throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
                StructuredPullConsumerOperations _localServant = (StructuredPullConsumerOperations) _so.servant;
                try {
                    _localServant.disconnect_structured_pull_consumer();
                } finally {
                    _servant_postinvoke(_so);
                }
                return;
            }
        }
    }

    public void offer_change(org.omg.CosNotification.EventType[] added, org.omg.CosNotification.EventType[] removed) throws org.omg.CosNotifyComm.InvalidEventType {
        while (true) {
            if (!this._is_local()) {
                org.omg.CORBA.portable.InputStream _is = null;
                try {
                    org.omg.CORBA.portable.OutputStream _os = _request("offer_change", true);
                    org.omg.CosNotification.EventTypeSeqHelper.write(_os, added);
                    org.omg.CosNotification.EventTypeSeqHelper.write(_os, removed);
                    _is = _invoke(_os);
                    return;
                } catch (org.omg.CORBA.portable.RemarshalException _rx) {
                } catch (org.omg.CORBA.portable.ApplicationException _ax) {
                    String _id = _ax.getId();
                    if (_id.equals("IDL:omg.org/CosNotifyComm/InvalidEventType:1.0")) {
                        throw org.omg.CosNotifyComm.InvalidEventTypeHelper.read(_ax.getInputStream());
                    } else throw new RuntimeException("Unexpected exception " + _id);
                } finally {
                    this._releaseReply(_is);
                }
            } else {
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("offer_change", _opsClass);
                if (_so == null) throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
                StructuredPullConsumerOperations _localServant = (StructuredPullConsumerOperations) _so.servant;
                try {
                    _localServant.offer_change(added, removed);
                } finally {
                    _servant_postinvoke(_so);
                }
                return;
            }
        }
    }
}
