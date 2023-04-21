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
 * Represents COM interface IHTMLLabelElement.
 */
public class IHTMLLabelElementImpl extends IDispatchImpl implements IHTMLLabelElement {

    public static final String INTERFACE_IDENTIFIER = IHTMLLabelElement.INTERFACE_IDENTIFIER;

    private static final IID _iid = IID.create(INTERFACE_IDENTIFIER);

    public IHTMLLabelElementImpl() {
    }

    protected IHTMLLabelElementImpl(IUnknownImpl that) throws ComException {
        super(that);
    }

    public IHTMLLabelElementImpl(IUnknown that) throws ComException {
        super(that);
    }

    public IHTMLLabelElementImpl(CLSID clsid, ClsCtx dwClsContext) throws ComException {
        super(clsid, dwClsContext);
    }

    public IHTMLLabelElementImpl(CLSID clsid, IUnknownImpl pUnkOuter, ClsCtx dwClsContext) throws ComException {
        super(clsid, pUnkOuter, dwClsContext);
    }

    public void setHtmlFor(BStr p) throws ComException {
        invokeStandardVirtualMethod(7, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Const(p) });
    }

    public BStr getHtmlFor() throws ComException {
        BStr p = new BStr();
        invokeStandardVirtualMethod(8, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public void setAccessKey(BStr p) throws ComException {
        invokeStandardVirtualMethod(9, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Const(p) });
    }

    public BStr getAccessKey() throws ComException {
        BStr p = new BStr();
        invokeStandardVirtualMethod(10, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public IID getIID() {
        return _iid;
    }

    public Object clone() {
        return new IHTMLLabelElementImpl(this);
    }
}
