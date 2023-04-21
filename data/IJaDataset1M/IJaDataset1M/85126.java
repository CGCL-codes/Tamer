package org.omg.CORBA.FT;

/**
 *	Generated from IDL definition of interface "HostFaultDetector"
 *	@author JacORB IDL compiler 
 */
public class _HostFaultDetectorStub extends org.omg.CORBA.portable.ObjectImpl implements org.omg.CORBA.FT.HostFaultDetector {

    private String[] ids = { "IDL:omg.org/CORBA/FT/HostFaultDetector:1.0", "IDL:omg.org/CORBA/FT/PullMonitor:1.0", "IDL:omg.org/CORBA/FT/PullMonitorable:1.0", "IDL:omg.org/CORBA/FT/RingMember:1.0", "IDL:omg.org/CORBA/FT/Monitorable:1.0", "IDL:omg.org/CORBA/FT/Voter:1.0", "IDL:omg.org/CORBA/Object:1.0" };

    public String[] _ids() {
        return ids;
    }

    public static final java.lang.Class _opsClass = org.omg.CORBA.FT.HostFaultDetectorOperations.class;

    public void suspect_breach(org.omg.CORBA.Object target, int loop_id) {
        while (true) {
            if (!this._is_local()) {
                org.omg.CORBA.portable.InputStream _is = null;
                try {
                    org.omg.CORBA.portable.OutputStream _os = _request("suspect_breach", false);
                    _os.write_Object(target);
                    _os.write_long(loop_id);
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
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("suspect_breach", _opsClass);
                if (_so == null) throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
                HostFaultDetectorOperations _localServant = (HostFaultDetectorOperations) _so.servant;
                try {
                    _localServant.suspect_breach(target, loop_id);
                } finally {
                    _servant_postinvoke(_so);
                }
                return;
            }
        }
    }

    public void update_ring_information(org.omg.CORBA.Object group) {
        while (true) {
            if (!this._is_local()) {
                org.omg.CORBA.portable.InputStream _is = null;
                try {
                    org.omg.CORBA.portable.OutputStream _os = _request("update_ring_information", false);
                    _os.write_Object(group);
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
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("update_ring_information", _opsClass);
                if (_so == null) throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
                HostFaultDetectorOperations _localServant = (HostFaultDetectorOperations) _so.servant;
                try {
                    _localServant.update_ring_information(group);
                } finally {
                    _servant_postinvoke(_so);
                }
                return;
            }
        }
    }

    public void check(org.omg.CORBA.Object target, int poll_id) {
        while (true) {
            if (!this._is_local()) {
                org.omg.CORBA.portable.InputStream _is = null;
                try {
                    org.omg.CORBA.portable.OutputStream _os = _request("check", false);
                    _os.write_Object(target);
                    _os.write_long(poll_id);
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
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("check", _opsClass);
                if (_so == null) throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
                HostFaultDetectorOperations _localServant = (HostFaultDetectorOperations) _so.servant;
                try {
                    _localServant.check(target, poll_id);
                } finally {
                    _servant_postinvoke(_so);
                }
                return;
            }
        }
    }

    public void pass(int loop_id) {
        while (true) {
            if (!this._is_local()) {
                org.omg.CORBA.portable.InputStream _is = null;
                try {
                    org.omg.CORBA.portable.OutputStream _os = _request("pass", false);
                    _os.write_long(loop_id);
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
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("pass", _opsClass);
                if (_so == null) throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
                HostFaultDetectorOperations _localServant = (HostFaultDetectorOperations) _so.servant;
                try {
                    _localServant.pass(loop_id);
                } finally {
                    _servant_postinvoke(_so);
                }
                return;
            }
        }
    }

    public void vote(org.omg.CORBA.Object target, int poll_id, org.omg.CORBA.FT.Status opinion) {
        while (true) {
            if (!this._is_local()) {
                org.omg.CORBA.portable.InputStream _is = null;
                try {
                    org.omg.CORBA.portable.OutputStream _os = _request("vote", false);
                    _os.write_Object(target);
                    _os.write_long(poll_id);
                    org.omg.CORBA.FT.StatusHelper.write(_os, opinion);
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
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("vote", _opsClass);
                if (_so == null) throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
                HostFaultDetectorOperations _localServant = (HostFaultDetectorOperations) _so.servant;
                try {
                    _localServant.vote(target, poll_id, opinion);
                } finally {
                    _servant_postinvoke(_so);
                }
                return;
            }
        }
    }

    public void update_monitoring(org.omg.CORBA.FT.PullMonitorable target, int granularity, long monitoring_interval, long monitoring_timeout) {
        while (true) {
            if (!this._is_local()) {
                org.omg.CORBA.portable.InputStream _is = null;
                try {
                    org.omg.CORBA.portable.OutputStream _os = _request("update_monitoring", false);
                    org.omg.CORBA.FT.PullMonitorableHelper.write(_os, target);
                    _os.write_long(granularity);
                    _os.write_ulonglong(monitoring_interval);
                    _os.write_ulonglong(monitoring_timeout);
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
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("update_monitoring", _opsClass);
                if (_so == null) throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
                HostFaultDetectorOperations _localServant = (HostFaultDetectorOperations) _so.servant;
                try {
                    _localServant.update_monitoring(target, granularity, monitoring_interval, monitoring_timeout);
                } finally {
                    _servant_postinvoke(_so);
                }
                return;
            }
        }
    }

    public void start_monitoring(org.omg.CORBA.FT.PullMonitorable target, int granularity, long monitoring_interval, long monitoring_timeout) {
        while (true) {
            if (!this._is_local()) {
                org.omg.CORBA.portable.InputStream _is = null;
                try {
                    org.omg.CORBA.portable.OutputStream _os = _request("start_monitoring", false);
                    org.omg.CORBA.FT.PullMonitorableHelper.write(_os, target);
                    _os.write_long(granularity);
                    _os.write_ulonglong(monitoring_interval);
                    _os.write_ulonglong(monitoring_timeout);
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
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("start_monitoring", _opsClass);
                if (_so == null) throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
                HostFaultDetectorOperations _localServant = (HostFaultDetectorOperations) _so.servant;
                try {
                    _localServant.start_monitoring(target, granularity, monitoring_interval, monitoring_timeout);
                } finally {
                    _servant_postinvoke(_so);
                }
                return;
            }
        }
    }

    public boolean is_alive() {
        while (true) {
            if (!this._is_local()) {
                org.omg.CORBA.portable.InputStream _is = null;
                try {
                    org.omg.CORBA.portable.OutputStream _os = _request("is_alive", true);
                    _is = _invoke(_os);
                    boolean _result = _is.read_boolean();
                    return _result;
                } catch (org.omg.CORBA.portable.RemarshalException _rx) {
                } catch (org.omg.CORBA.portable.ApplicationException _ax) {
                    String _id = _ax.getId();
                    throw new RuntimeException("Unexpected exception " + _id);
                } finally {
                    this._releaseReply(_is);
                }
            } else {
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("is_alive", _opsClass);
                if (_so == null) throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
                HostFaultDetectorOperations _localServant = (HostFaultDetectorOperations) _so.servant;
                boolean _result;
                try {
                    _result = _localServant.is_alive();
                } finally {
                    _servant_postinvoke(_so);
                }
                return _result;
            }
        }
    }

    public void stop_monitoring(org.omg.CORBA.FT.PullMonitorable target) {
        while (true) {
            if (!this._is_local()) {
                org.omg.CORBA.portable.InputStream _is = null;
                try {
                    org.omg.CORBA.portable.OutputStream _os = _request("stop_monitoring", false);
                    org.omg.CORBA.FT.PullMonitorableHelper.write(_os, target);
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
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("stop_monitoring", _opsClass);
                if (_so == null) throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
                HostFaultDetectorOperations _localServant = (HostFaultDetectorOperations) _so.servant;
                try {
                    _localServant.stop_monitoring(target);
                } finally {
                    _servant_postinvoke(_so);
                }
                return;
            }
        }
    }
}
