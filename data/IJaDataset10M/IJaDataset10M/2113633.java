package org.omg.CORBA.FT;

/**
 *	Generated from IDL definition of interface "Updateable"
 *	@author JacORB IDL compiler 
 */
public class _UpdateableStub extends org.omg.CORBA.portable.ObjectImpl implements org.omg.CORBA.FT.Updateable {

    private String[] ids = { "IDL:omg.org/CORBA/FT/Updateable:1.0", "IDL:omg.org/CORBA/FT/Checkpointable:1.0", "IDL:omg.org/CORBA/Object:1.0" };

    public String[] _ids() {
        return ids;
    }

    public static final java.lang.Class _opsClass = org.omg.CORBA.FT.UpdateableOperations.class;

    public void set_update(byte[] s) throws org.omg.CORBA.FT.InvalidUpdate {
        while (true) {
            if (!this._is_local()) {
                org.omg.CORBA.portable.InputStream _is = null;
                try {
                    org.omg.CORBA.portable.OutputStream _os = _request("set_update", true);
                    org.omg.CORBA.FT.StateHelper.write(_os, s);
                    _is = _invoke(_os);
                    return;
                } catch (org.omg.CORBA.portable.RemarshalException _rx) {
                } catch (org.omg.CORBA.portable.ApplicationException _ax) {
                    String _id = _ax.getId();
                    if (_id.equals("IDL:omg.org/CORBA/FT/InvalidUpdate:1.0")) {
                        throw org.omg.CORBA.FT.InvalidUpdateHelper.read(_ax.getInputStream());
                    } else throw new RuntimeException("Unexpected exception " + _id);
                } finally {
                    this._releaseReply(_is);
                }
            } else {
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("set_update", _opsClass);
                if (_so == null) throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
                UpdateableOperations _localServant = (UpdateableOperations) _so.servant;
                try {
                    _localServant.set_update(s);
                } finally {
                    _servant_postinvoke(_so);
                }
                return;
            }
        }
    }

    public byte[] get_state() throws org.omg.CORBA.FT.NoStateAvailable {
        while (true) {
            if (!this._is_local()) {
                org.omg.CORBA.portable.InputStream _is = null;
                try {
                    org.omg.CORBA.portable.OutputStream _os = _request("get_state", true);
                    _is = _invoke(_os);
                    byte[] _result = org.omg.CORBA.FT.StateHelper.read(_is);
                    return _result;
                } catch (org.omg.CORBA.portable.RemarshalException _rx) {
                } catch (org.omg.CORBA.portable.ApplicationException _ax) {
                    String _id = _ax.getId();
                    if (_id.equals("IDL:omg.org/CORBA/FT/NoStateAvailable:1.0")) {
                        throw org.omg.CORBA.FT.NoStateAvailableHelper.read(_ax.getInputStream());
                    } else throw new RuntimeException("Unexpected exception " + _id);
                } finally {
                    this._releaseReply(_is);
                }
            } else {
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("get_state", _opsClass);
                if (_so == null) throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
                UpdateableOperations _localServant = (UpdateableOperations) _so.servant;
                byte[] _result;
                try {
                    _result = _localServant.get_state();
                } finally {
                    _servant_postinvoke(_so);
                }
                return _result;
            }
        }
    }

    public void set_state(byte[] s) throws org.omg.CORBA.FT.InvalidState {
        while (true) {
            if (!this._is_local()) {
                org.omg.CORBA.portable.InputStream _is = null;
                try {
                    org.omg.CORBA.portable.OutputStream _os = _request("set_state", true);
                    org.omg.CORBA.FT.StateHelper.write(_os, s);
                    _is = _invoke(_os);
                    return;
                } catch (org.omg.CORBA.portable.RemarshalException _rx) {
                } catch (org.omg.CORBA.portable.ApplicationException _ax) {
                    String _id = _ax.getId();
                    if (_id.equals("IDL:omg.org/CORBA/FT/InvalidState:1.0")) {
                        throw org.omg.CORBA.FT.InvalidStateHelper.read(_ax.getInputStream());
                    } else throw new RuntimeException("Unexpected exception " + _id);
                } finally {
                    this._releaseReply(_is);
                }
            } else {
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("set_state", _opsClass);
                if (_so == null) throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
                UpdateableOperations _localServant = (UpdateableOperations) _so.servant;
                try {
                    _localServant.set_state(s);
                } finally {
                    _servant_postinvoke(_so);
                }
                return;
            }
        }
    }

    public byte[] get_update() throws org.omg.CORBA.FT.NoUpdateAvailable {
        while (true) {
            if (!this._is_local()) {
                org.omg.CORBA.portable.InputStream _is = null;
                try {
                    org.omg.CORBA.portable.OutputStream _os = _request("get_update", true);
                    _is = _invoke(_os);
                    byte[] _result = org.omg.CORBA.FT.StateHelper.read(_is);
                    return _result;
                } catch (org.omg.CORBA.portable.RemarshalException _rx) {
                } catch (org.omg.CORBA.portable.ApplicationException _ax) {
                    String _id = _ax.getId();
                    if (_id.equals("IDL:omg.org/CORBA/FT/NoUpdateAvailable:1.0")) {
                        throw org.omg.CORBA.FT.NoUpdateAvailableHelper.read(_ax.getInputStream());
                    } else throw new RuntimeException("Unexpected exception " + _id);
                } finally {
                    this._releaseReply(_is);
                }
            } else {
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("get_update", _opsClass);
                if (_so == null) throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
                UpdateableOperations _localServant = (UpdateableOperations) _so.servant;
                byte[] _result;
                try {
                    _result = _localServant.get_update();
                } finally {
                    _servant_postinvoke(_so);
                }
                return _result;
            }
        }
    }
}
