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
 * Represents COM interface IHTMLAppBehavior3.
 */
public class IHTMLAppBehavior3Impl extends IDispatchImpl implements IHTMLAppBehavior3 {

    public static final String INTERFACE_IDENTIFIER = IHTMLAppBehavior3.INTERFACE_IDENTIFIER;

    private static final IID _iid = IID.create(INTERFACE_IDENTIFIER);

    public IHTMLAppBehavior3Impl() {
    }

    protected IHTMLAppBehavior3Impl(IUnknownImpl that) throws ComException {
        super(that);
    }

    public IHTMLAppBehavior3Impl(IUnknown that) throws ComException {
        super(that);
    }

    public IHTMLAppBehavior3Impl(CLSID clsid, ClsCtx dwClsContext) throws ComException {
        super(clsid, dwClsContext);
    }

    public IHTMLAppBehavior3Impl(CLSID clsid, IUnknownImpl pUnkOuter, ClsCtx dwClsContext) throws ComException {
        super(clsid, pUnkOuter, dwClsContext);
    }

    public void setNavigable(BStr p) throws ComException {
        invokeStandardVirtualMethod(7, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Const(p) });
    }

    public BStr getNavigable() throws ComException {
        BStr p = new BStr();
        invokeStandardVirtualMethod(8, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public IID getIID() {
        return _iid;
    }

    public Object clone() {
        return new IHTMLAppBehavior3Impl(this);
    }
}
