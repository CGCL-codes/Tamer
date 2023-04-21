package TwitterSimpleClient.EntityCondivise;

public final class _ENotaCondivisaDelM extends Ice._ObjectDelM implements _ENotaCondivisaDel {

    public String getContent(java.util.Map<String, String> __ctx) throws IceInternal.LocalExceptionWrapper {
        IceInternal.Outgoing __og = __handler.getOutgoing("getContent", Ice.OperationMode.Normal, __ctx);
        try {
            boolean __ok = __og.invoke();
            try {
                if (!__ok) {
                    try {
                        __og.throwUserException();
                    } catch (Ice.UserException __ex) {
                        throw new Ice.UnknownUserException(__ex.ice_name());
                    }
                }
                IceInternal.BasicStream __is = __og.is();
                __is.startReadEncaps();
                String __ret;
                __ret = __is.readString();
                __is.endReadEncaps();
                return __ret;
            } catch (Ice.LocalException __ex) {
                throw new IceInternal.LocalExceptionWrapper(__ex, false);
            }
        } finally {
            __handler.reclaimOutgoing(__og);
        }
    }

    public long getData(java.util.Map<String, String> __ctx) throws IceInternal.LocalExceptionWrapper {
        IceInternal.Outgoing __og = __handler.getOutgoing("getData", Ice.OperationMode.Normal, __ctx);
        try {
            boolean __ok = __og.invoke();
            try {
                if (!__ok) {
                    try {
                        __og.throwUserException();
                    } catch (Ice.UserException __ex) {
                        throw new Ice.UnknownUserException(__ex.ice_name());
                    }
                }
                IceInternal.BasicStream __is = __og.is();
                __is.startReadEncaps();
                long __ret;
                __ret = __is.readLong();
                __is.endReadEncaps();
                return __ret;
            } catch (Ice.LocalException __ex) {
                throw new IceInternal.LocalExceptionWrapper(__ex, false);
            }
        } finally {
            __handler.reclaimOutgoing(__og);
        }
    }

    public int getId(java.util.Map<String, String> __ctx) throws IceInternal.LocalExceptionWrapper {
        IceInternal.Outgoing __og = __handler.getOutgoing("getId", Ice.OperationMode.Normal, __ctx);
        try {
            boolean __ok = __og.invoke();
            try {
                if (!__ok) {
                    try {
                        __og.throwUserException();
                    } catch (Ice.UserException __ex) {
                        throw new Ice.UnknownUserException(__ex.ice_name());
                    }
                }
                IceInternal.BasicStream __is = __og.is();
                __is.startReadEncaps();
                int __ret;
                __ret = __is.readInt();
                __is.endReadEncaps();
                return __ret;
            } catch (Ice.LocalException __ex) {
                throw new IceInternal.LocalExceptionWrapper(__ex, false);
            }
        } finally {
            __handler.reclaimOutgoing(__og);
        }
    }

    public EUtenteCondiviso getScrittaDa(java.util.Map<String, String> __ctx) throws IceInternal.LocalExceptionWrapper {
        IceInternal.Outgoing __og = __handler.getOutgoing("getScrittaDa", Ice.OperationMode.Normal, __ctx);
        try {
            boolean __ok = __og.invoke();
            try {
                if (!__ok) {
                    try {
                        __og.throwUserException();
                    } catch (Ice.UserException __ex) {
                        throw new Ice.UnknownUserException(__ex.ice_name());
                    }
                }
                IceInternal.BasicStream __is = __og.is();
                __is.startReadEncaps();
                EUtenteCondivisoHolder __ret = new EUtenteCondivisoHolder();
                __is.readObject(__ret.getPatcher());
                __is.readPendingObjects();
                __is.endReadEncaps();
                return __ret.value;
            } catch (Ice.LocalException __ex) {
                throw new IceInternal.LocalExceptionWrapper(__ex, false);
            }
        } finally {
            __handler.reclaimOutgoing(__og);
        }
    }

    public void setContent(String content, java.util.Map<String, String> __ctx) throws IceInternal.LocalExceptionWrapper {
        IceInternal.Outgoing __og = __handler.getOutgoing("setContent", Ice.OperationMode.Normal, __ctx);
        try {
            try {
                IceInternal.BasicStream __os = __og.os();
                __os.writeString(content);
            } catch (Ice.LocalException __ex) {
                __og.abort(__ex);
            }
            boolean __ok = __og.invoke();
            if (!__og.is().isEmpty()) {
                try {
                    if (!__ok) {
                        try {
                            __og.throwUserException();
                        } catch (Ice.UserException __ex) {
                            throw new Ice.UnknownUserException(__ex.ice_name());
                        }
                    }
                    __og.is().skipEmptyEncaps();
                } catch (Ice.LocalException __ex) {
                    throw new IceInternal.LocalExceptionWrapper(__ex, false);
                }
            }
        } finally {
            __handler.reclaimOutgoing(__og);
        }
    }

    public void setData(long data, java.util.Map<String, String> __ctx) throws IceInternal.LocalExceptionWrapper {
        IceInternal.Outgoing __og = __handler.getOutgoing("setData", Ice.OperationMode.Normal, __ctx);
        try {
            try {
                IceInternal.BasicStream __os = __og.os();
                __os.writeLong(data);
            } catch (Ice.LocalException __ex) {
                __og.abort(__ex);
            }
            boolean __ok = __og.invoke();
            if (!__og.is().isEmpty()) {
                try {
                    if (!__ok) {
                        try {
                            __og.throwUserException();
                        } catch (Ice.UserException __ex) {
                            throw new Ice.UnknownUserException(__ex.ice_name());
                        }
                    }
                    __og.is().skipEmptyEncaps();
                } catch (Ice.LocalException __ex) {
                    throw new IceInternal.LocalExceptionWrapper(__ex, false);
                }
            }
        } finally {
            __handler.reclaimOutgoing(__og);
        }
    }

    public void setId(int id, java.util.Map<String, String> __ctx) throws IceInternal.LocalExceptionWrapper {
        IceInternal.Outgoing __og = __handler.getOutgoing("setId", Ice.OperationMode.Normal, __ctx);
        try {
            try {
                IceInternal.BasicStream __os = __og.os();
                __os.writeInt(id);
            } catch (Ice.LocalException __ex) {
                __og.abort(__ex);
            }
            boolean __ok = __og.invoke();
            if (!__og.is().isEmpty()) {
                try {
                    if (!__ok) {
                        try {
                            __og.throwUserException();
                        } catch (Ice.UserException __ex) {
                            throw new Ice.UnknownUserException(__ex.ice_name());
                        }
                    }
                    __og.is().skipEmptyEncaps();
                } catch (Ice.LocalException __ex) {
                    throw new IceInternal.LocalExceptionWrapper(__ex, false);
                }
            }
        } finally {
            __handler.reclaimOutgoing(__og);
        }
    }

    public void setScrittaDa(EUtenteCondiviso untente, java.util.Map<String, String> __ctx) throws IceInternal.LocalExceptionWrapper {
        IceInternal.Outgoing __og = __handler.getOutgoing("setScrittaDa", Ice.OperationMode.Normal, __ctx);
        try {
            try {
                IceInternal.BasicStream __os = __og.os();
                __os.writeObject(untente);
                __os.writePendingObjects();
            } catch (Ice.LocalException __ex) {
                __og.abort(__ex);
            }
            boolean __ok = __og.invoke();
            if (!__og.is().isEmpty()) {
                try {
                    if (!__ok) {
                        try {
                            __og.throwUserException();
                        } catch (Ice.UserException __ex) {
                            throw new Ice.UnknownUserException(__ex.ice_name());
                        }
                    }
                    __og.is().skipEmptyEncaps();
                } catch (Ice.LocalException __ex) {
                    throw new IceInternal.LocalExceptionWrapper(__ex, false);
                }
            }
        } finally {
            __handler.reclaimOutgoing(__og);
        }
    }
}
