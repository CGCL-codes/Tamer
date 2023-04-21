package org.asam.ods;

/**
 *	Generated from IDL interface "EnumerationDefinition"
 *	@author JacORB IDL compiler V 2.2.3, 10-Dec-2005
 */
public class _EnumerationDefinitionStub extends org.omg.CORBA.portable.ObjectImpl implements org.asam.ods.EnumerationDefinition {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private String[] ids = { "IDL:org/asam/ods/EnumerationDefinition:1.0" };

    public String[] _ids() {
        return ids;
    }

    public static final java.lang.Class _opsClass = org.asam.ods.EnumerationDefinitionOperations.class;

    public int getIndex() throws org.asam.ods.AoException {
        while (true) {
            if (!this._is_local()) {
                org.omg.CORBA.portable.InputStream _is = null;
                try {
                    org.omg.CORBA.portable.OutputStream _os = _request("getIndex", true);
                    _is = _invoke(_os);
                    int _result = _is.read_long();
                    return _result;
                } catch (org.omg.CORBA.portable.RemarshalException _rx) {
                } catch (org.omg.CORBA.portable.ApplicationException _ax) {
                    String _id = _ax.getId();
                    if (_id.equals("IDL:org/asam/ods/AoException:1.0")) {
                        throw org.asam.ods.AoExceptionHelper.read(_ax.getInputStream());
                    } else throw new RuntimeException("Unexpected exception " + _id);
                } finally {
                    this._releaseReply(_is);
                }
            } else {
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("getIndex", _opsClass);
                if (_so == null) throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
                EnumerationDefinitionOperations _localServant = (EnumerationDefinitionOperations) _so.servant;
                int _result;
                try {
                    _result = _localServant.getIndex();
                } finally {
                    _servant_postinvoke(_so);
                }
                return _result;
            }
        }
    }

    public void setName(java.lang.String enumName) throws org.asam.ods.AoException {
        while (true) {
            if (!this._is_local()) {
                org.omg.CORBA.portable.InputStream _is = null;
                try {
                    org.omg.CORBA.portable.OutputStream _os = _request("setName", true);
                    _os.write_string(enumName);
                    _is = _invoke(_os);
                    return;
                } catch (org.omg.CORBA.portable.RemarshalException _rx) {
                } catch (org.omg.CORBA.portable.ApplicationException _ax) {
                    String _id = _ax.getId();
                    if (_id.equals("IDL:org/asam/ods/AoException:1.0")) {
                        throw org.asam.ods.AoExceptionHelper.read(_ax.getInputStream());
                    } else throw new RuntimeException("Unexpected exception " + _id);
                } finally {
                    this._releaseReply(_is);
                }
            } else {
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("setName", _opsClass);
                if (_so == null) throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
                EnumerationDefinitionOperations _localServant = (EnumerationDefinitionOperations) _so.servant;
                try {
                    _localServant.setName(enumName);
                } finally {
                    _servant_postinvoke(_so);
                }
                return;
            }
        }
    }

    public java.lang.String getItemName(int item) throws org.asam.ods.AoException {
        while (true) {
            if (!this._is_local()) {
                org.omg.CORBA.portable.InputStream _is = null;
                try {
                    org.omg.CORBA.portable.OutputStream _os = _request("getItemName", true);
                    _os.write_long(item);
                    _is = _invoke(_os);
                    java.lang.String _result = _is.read_string();
                    return _result;
                } catch (org.omg.CORBA.portable.RemarshalException _rx) {
                } catch (org.omg.CORBA.portable.ApplicationException _ax) {
                    String _id = _ax.getId();
                    if (_id.equals("IDL:org/asam/ods/AoException:1.0")) {
                        throw org.asam.ods.AoExceptionHelper.read(_ax.getInputStream());
                    } else throw new RuntimeException("Unexpected exception " + _id);
                } finally {
                    this._releaseReply(_is);
                }
            } else {
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("getItemName", _opsClass);
                if (_so == null) throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
                EnumerationDefinitionOperations _localServant = (EnumerationDefinitionOperations) _so.servant;
                java.lang.String _result;
                try {
                    _result = _localServant.getItemName(item);
                } finally {
                    _servant_postinvoke(_so);
                }
                return _result;
            }
        }
    }

    public void renameItem(java.lang.String oldItemName, java.lang.String newItemName) throws org.asam.ods.AoException {
        while (true) {
            if (!this._is_local()) {
                org.omg.CORBA.portable.InputStream _is = null;
                try {
                    org.omg.CORBA.portable.OutputStream _os = _request("renameItem", true);
                    _os.write_string(oldItemName);
                    _os.write_string(newItemName);
                    _is = _invoke(_os);
                    return;
                } catch (org.omg.CORBA.portable.RemarshalException _rx) {
                } catch (org.omg.CORBA.portable.ApplicationException _ax) {
                    String _id = _ax.getId();
                    if (_id.equals("IDL:org/asam/ods/AoException:1.0")) {
                        throw org.asam.ods.AoExceptionHelper.read(_ax.getInputStream());
                    } else throw new RuntimeException("Unexpected exception " + _id);
                } finally {
                    this._releaseReply(_is);
                }
            } else {
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("renameItem", _opsClass);
                if (_so == null) throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
                EnumerationDefinitionOperations _localServant = (EnumerationDefinitionOperations) _so.servant;
                try {
                    _localServant.renameItem(oldItemName, newItemName);
                } finally {
                    _servant_postinvoke(_so);
                }
                return;
            }
        }
    }

    public java.lang.String[] listItemNames() throws org.asam.ods.AoException {
        while (true) {
            if (!this._is_local()) {
                org.omg.CORBA.portable.InputStream _is = null;
                try {
                    org.omg.CORBA.portable.OutputStream _os = _request("listItemNames", true);
                    _is = _invoke(_os);
                    java.lang.String[] _result = org.asam.ods.NameSequenceHelper.read(_is);
                    return _result;
                } catch (org.omg.CORBA.portable.RemarshalException _rx) {
                } catch (org.omg.CORBA.portable.ApplicationException _ax) {
                    String _id = _ax.getId();
                    if (_id.equals("IDL:org/asam/ods/AoException:1.0")) {
                        throw org.asam.ods.AoExceptionHelper.read(_ax.getInputStream());
                    } else throw new RuntimeException("Unexpected exception " + _id);
                } finally {
                    this._releaseReply(_is);
                }
            } else {
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("listItemNames", _opsClass);
                if (_so == null) throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
                EnumerationDefinitionOperations _localServant = (EnumerationDefinitionOperations) _so.servant;
                java.lang.String[] _result;
                try {
                    _result = _localServant.listItemNames();
                } finally {
                    _servant_postinvoke(_so);
                }
                return _result;
            }
        }
    }

    public int getItem(java.lang.String itemName) throws org.asam.ods.AoException {
        while (true) {
            if (!this._is_local()) {
                org.omg.CORBA.portable.InputStream _is = null;
                try {
                    org.omg.CORBA.portable.OutputStream _os = _request("getItem", true);
                    _os.write_string(itemName);
                    _is = _invoke(_os);
                    int _result = _is.read_long();
                    return _result;
                } catch (org.omg.CORBA.portable.RemarshalException _rx) {
                } catch (org.omg.CORBA.portable.ApplicationException _ax) {
                    String _id = _ax.getId();
                    if (_id.equals("IDL:org/asam/ods/AoException:1.0")) {
                        throw org.asam.ods.AoExceptionHelper.read(_ax.getInputStream());
                    } else throw new RuntimeException("Unexpected exception " + _id);
                } finally {
                    this._releaseReply(_is);
                }
            } else {
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("getItem", _opsClass);
                if (_so == null) throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
                EnumerationDefinitionOperations _localServant = (EnumerationDefinitionOperations) _so.servant;
                int _result;
                try {
                    _result = _localServant.getItem(itemName);
                } finally {
                    _servant_postinvoke(_so);
                }
                return _result;
            }
        }
    }

    public void addItem(java.lang.String itemName) throws org.asam.ods.AoException {
        while (true) {
            if (!this._is_local()) {
                org.omg.CORBA.portable.InputStream _is = null;
                try {
                    org.omg.CORBA.portable.OutputStream _os = _request("addItem", true);
                    _os.write_string(itemName);
                    _is = _invoke(_os);
                    return;
                } catch (org.omg.CORBA.portable.RemarshalException _rx) {
                } catch (org.omg.CORBA.portable.ApplicationException _ax) {
                    String _id = _ax.getId();
                    if (_id.equals("IDL:org/asam/ods/AoException:1.0")) {
                        throw org.asam.ods.AoExceptionHelper.read(_ax.getInputStream());
                    } else throw new RuntimeException("Unexpected exception " + _id);
                } finally {
                    this._releaseReply(_is);
                }
            } else {
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("addItem", _opsClass);
                if (_so == null) throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
                EnumerationDefinitionOperations _localServant = (EnumerationDefinitionOperations) _so.servant;
                try {
                    _localServant.addItem(itemName);
                } finally {
                    _servant_postinvoke(_so);
                }
                return;
            }
        }
    }

    public java.lang.String getName() throws org.asam.ods.AoException {
        while (true) {
            if (!this._is_local()) {
                org.omg.CORBA.portable.InputStream _is = null;
                try {
                    org.omg.CORBA.portable.OutputStream _os = _request("getName", true);
                    _is = _invoke(_os);
                    java.lang.String _result = _is.read_string();
                    return _result;
                } catch (org.omg.CORBA.portable.RemarshalException _rx) {
                } catch (org.omg.CORBA.portable.ApplicationException _ax) {
                    String _id = _ax.getId();
                    if (_id.equals("IDL:org/asam/ods/AoException:1.0")) {
                        throw org.asam.ods.AoExceptionHelper.read(_ax.getInputStream());
                    } else throw new RuntimeException("Unexpected exception " + _id);
                } finally {
                    this._releaseReply(_is);
                }
            } else {
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("getName", _opsClass);
                if (_so == null) throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
                EnumerationDefinitionOperations _localServant = (EnumerationDefinitionOperations) _so.servant;
                java.lang.String _result;
                try {
                    _result = _localServant.getName();
                } finally {
                    _servant_postinvoke(_so);
                }
                return _result;
            }
        }
    }
}
