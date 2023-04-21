package org.asam.ods;

/**
 *	Generated from IDL interface "SMatLink"
 *	@author JacORB IDL compiler V 2.2.3, 10-Dec-2005
 */
public class _SMatLinkStub extends org.omg.CORBA.portable.ObjectImpl implements org.asam.ods.SMatLink {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private String[] ids = { "IDL:org/asam/ods/SMatLink:1.0" };

    public String[] _ids() {
        return ids;
    }

    public static final java.lang.Class _opsClass = org.asam.ods.SMatLinkOperations.class;

    public org.asam.ods.Column[] getSMat1Columns() throws org.asam.ods.AoException {
        while (true) {
            if (!this._is_local()) {
                org.omg.CORBA.portable.InputStream _is = null;
                try {
                    org.omg.CORBA.portable.OutputStream _os = _request("getSMat1Columns", true);
                    _is = _invoke(_os);
                    org.asam.ods.Column[] _result = org.asam.ods.ColumnSequenceHelper.read(_is);
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
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("getSMat1Columns", _opsClass);
                if (_so == null) throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
                SMatLinkOperations _localServant = (SMatLinkOperations) _so.servant;
                org.asam.ods.Column[] _result;
                try {
                    _result = _localServant.getSMat1Columns();
                } finally {
                    _servant_postinvoke(_so);
                }
                return _result;
            }
        }
    }

    public void setSMat1Columns(org.asam.ods.Column[] columns) throws org.asam.ods.AoException {
        while (true) {
            if (!this._is_local()) {
                org.omg.CORBA.portable.InputStream _is = null;
                try {
                    org.omg.CORBA.portable.OutputStream _os = _request("setSMat1Columns", true);
                    org.asam.ods.ColumnSequenceHelper.write(_os, columns);
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
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("setSMat1Columns", _opsClass);
                if (_so == null) throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
                SMatLinkOperations _localServant = (SMatLinkOperations) _so.servant;
                try {
                    _localServant.setSMat1Columns(columns);
                } finally {
                    _servant_postinvoke(_so);
                }
                return;
            }
        }
    }

    public org.asam.ods.SubMatrix getSMat1() throws org.asam.ods.AoException {
        while (true) {
            if (!this._is_local()) {
                org.omg.CORBA.portable.InputStream _is = null;
                try {
                    org.omg.CORBA.portable.OutputStream _os = _request("getSMat1", true);
                    _is = _invoke(_os);
                    org.asam.ods.SubMatrix _result = org.asam.ods.SubMatrixHelper.read(_is);
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
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("getSMat1", _opsClass);
                if (_so == null) throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
                SMatLinkOperations _localServant = (SMatLinkOperations) _so.servant;
                org.asam.ods.SubMatrix _result;
                try {
                    _result = _localServant.getSMat1();
                } finally {
                    _servant_postinvoke(_so);
                }
                return _result;
            }
        }
    }

    public void setOrdinalNumber(int ordinalNumber) throws org.asam.ods.AoException {
        while (true) {
            if (!this._is_local()) {
                org.omg.CORBA.portable.InputStream _is = null;
                try {
                    org.omg.CORBA.portable.OutputStream _os = _request("setOrdinalNumber", true);
                    _os.write_long(ordinalNumber);
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
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("setOrdinalNumber", _opsClass);
                if (_so == null) throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
                SMatLinkOperations _localServant = (SMatLinkOperations) _so.servant;
                try {
                    _localServant.setOrdinalNumber(ordinalNumber);
                } finally {
                    _servant_postinvoke(_so);
                }
                return;
            }
        }
    }

    public void setSMat1(org.asam.ods.SubMatrix subMat1) throws org.asam.ods.AoException {
        while (true) {
            if (!this._is_local()) {
                org.omg.CORBA.portable.InputStream _is = null;
                try {
                    org.omg.CORBA.portable.OutputStream _os = _request("setSMat1", true);
                    org.asam.ods.SubMatrixHelper.write(_os, subMat1);
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
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("setSMat1", _opsClass);
                if (_so == null) throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
                SMatLinkOperations _localServant = (SMatLinkOperations) _so.servant;
                try {
                    _localServant.setSMat1(subMat1);
                } finally {
                    _servant_postinvoke(_so);
                }
                return;
            }
        }
    }

    public org.asam.ods.SubMatrix getSMat2() throws org.asam.ods.AoException {
        while (true) {
            if (!this._is_local()) {
                org.omg.CORBA.portable.InputStream _is = null;
                try {
                    org.omg.CORBA.portable.OutputStream _os = _request("getSMat2", true);
                    _is = _invoke(_os);
                    org.asam.ods.SubMatrix _result = org.asam.ods.SubMatrixHelper.read(_is);
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
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("getSMat2", _opsClass);
                if (_so == null) throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
                SMatLinkOperations _localServant = (SMatLinkOperations) _so.servant;
                org.asam.ods.SubMatrix _result;
                try {
                    _result = _localServant.getSMat2();
                } finally {
                    _servant_postinvoke(_so);
                }
                return _result;
            }
        }
    }

    public org.asam.ods.Column[] getSMat2Columns() throws org.asam.ods.AoException {
        while (true) {
            if (!this._is_local()) {
                org.omg.CORBA.portable.InputStream _is = null;
                try {
                    org.omg.CORBA.portable.OutputStream _os = _request("getSMat2Columns", true);
                    _is = _invoke(_os);
                    org.asam.ods.Column[] _result = org.asam.ods.ColumnSequenceHelper.read(_is);
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
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("getSMat2Columns", _opsClass);
                if (_so == null) throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
                SMatLinkOperations _localServant = (SMatLinkOperations) _so.servant;
                org.asam.ods.Column[] _result;
                try {
                    _result = _localServant.getSMat2Columns();
                } finally {
                    _servant_postinvoke(_so);
                }
                return _result;
            }
        }
    }

    public int getOrdinalNumber() throws org.asam.ods.AoException {
        while (true) {
            if (!this._is_local()) {
                org.omg.CORBA.portable.InputStream _is = null;
                try {
                    org.omg.CORBA.portable.OutputStream _os = _request("getOrdinalNumber", true);
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
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("getOrdinalNumber", _opsClass);
                if (_so == null) throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
                SMatLinkOperations _localServant = (SMatLinkOperations) _so.servant;
                int _result;
                try {
                    _result = _localServant.getOrdinalNumber();
                } finally {
                    _servant_postinvoke(_so);
                }
                return _result;
            }
        }
    }

    public void setSMat2(org.asam.ods.SubMatrix subMat2) throws org.asam.ods.AoException {
        while (true) {
            if (!this._is_local()) {
                org.omg.CORBA.portable.InputStream _is = null;
                try {
                    org.omg.CORBA.portable.OutputStream _os = _request("setSMat2", true);
                    org.asam.ods.SubMatrixHelper.write(_os, subMat2);
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
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("setSMat2", _opsClass);
                if (_so == null) throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
                SMatLinkOperations _localServant = (SMatLinkOperations) _so.servant;
                try {
                    _localServant.setSMat2(subMat2);
                } finally {
                    _servant_postinvoke(_so);
                }
                return;
            }
        }
    }

    public void setLinkType(org.asam.ods.BuildUpFunction linkType) throws org.asam.ods.AoException {
        while (true) {
            if (!this._is_local()) {
                org.omg.CORBA.portable.InputStream _is = null;
                try {
                    org.omg.CORBA.portable.OutputStream _os = _request("setLinkType", true);
                    org.asam.ods.BuildUpFunctionHelper.write(_os, linkType);
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
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("setLinkType", _opsClass);
                if (_so == null) throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
                SMatLinkOperations _localServant = (SMatLinkOperations) _so.servant;
                try {
                    _localServant.setLinkType(linkType);
                } finally {
                    _servant_postinvoke(_so);
                }
                return;
            }
        }
    }

    public void setSMat2Columns(org.asam.ods.Column[] columns) throws org.asam.ods.AoException {
        while (true) {
            if (!this._is_local()) {
                org.omg.CORBA.portable.InputStream _is = null;
                try {
                    org.omg.CORBA.portable.OutputStream _os = _request("setSMat2Columns", true);
                    org.asam.ods.ColumnSequenceHelper.write(_os, columns);
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
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("setSMat2Columns", _opsClass);
                if (_so == null) throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
                SMatLinkOperations _localServant = (SMatLinkOperations) _so.servant;
                try {
                    _localServant.setSMat2Columns(columns);
                } finally {
                    _servant_postinvoke(_so);
                }
                return;
            }
        }
    }

    public org.asam.ods.BuildUpFunction getLinkType() throws org.asam.ods.AoException {
        while (true) {
            if (!this._is_local()) {
                org.omg.CORBA.portable.InputStream _is = null;
                try {
                    org.omg.CORBA.portable.OutputStream _os = _request("getLinkType", true);
                    _is = _invoke(_os);
                    org.asam.ods.BuildUpFunction _result = org.asam.ods.BuildUpFunctionHelper.read(_is);
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
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("getLinkType", _opsClass);
                if (_so == null) throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
                SMatLinkOperations _localServant = (SMatLinkOperations) _so.servant;
                org.asam.ods.BuildUpFunction _result;
                try {
                    _result = _localServant.getLinkType();
                } finally {
                    _servant_postinvoke(_so);
                }
                return _result;
            }
        }
    }
}
