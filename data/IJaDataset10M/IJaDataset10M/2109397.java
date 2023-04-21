package watij.mshtml.impl;

import com.jniwrapper.*;
import com.jniwrapper.win32.*;
import com.jniwrapper.win32.automation.*;
import com.jniwrapper.win32.automation.impl.*;
import com.jniwrapper.win32.automation.types.*;
import com.jniwrapper.win32.com.*;
import com.jniwrapper.win32.com.impl.*;
import com.jniwrapper.win32.com.types.*;
import watij.mshtml.*;

/**
 * Represents COM interface IHTMLTableSection3.
 */
public class IHTMLTableSection3Impl extends IDispatchImpl implements IHTMLTableSection3 {

    public static final String INTERFACE_IDENTIFIER = IHTMLTableSection3.INTERFACE_IDENTIFIER;

    private static final IID _iid = IID.create(INTERFACE_IDENTIFIER);

    public IHTMLTableSection3Impl() {
    }

    protected IHTMLTableSection3Impl(IUnknownImpl that) throws ComException {
        super(that);
    }

    public IHTMLTableSection3Impl(IUnknown that) throws ComException {
        super(that);
    }

    public IHTMLTableSection3Impl(CLSID clsid, ClsCtx dwClsContext) throws ComException {
        super(clsid, dwClsContext);
    }

    public IHTMLTableSection3Impl(CLSID clsid, IUnknownImpl pUnkOuter, ClsCtx dwClsContext) throws ComException {
        super(clsid, pUnkOuter, dwClsContext);
    }

    public void setCh(BStr p) throws ComException {
        invokeStandardVirtualMethod(7, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Const(p) });
    }

    public BStr getCh() throws ComException {
        BStr p = new BStr();
        invokeStandardVirtualMethod(8, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public void setChOff(BStr p) throws ComException {
        invokeStandardVirtualMethod(9, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Const(p) });
    }

    public BStr getChOff() throws ComException {
        BStr p = new BStr();
        invokeStandardVirtualMethod(10, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public IID getIID() {
        return _iid;
    }

    public Object clone() {
        return new IHTMLTableSection3Impl(this);
    }
}
