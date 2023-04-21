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
 * Represents COM interface IHTMLScriptElement.
 */
public class IHTMLScriptElementImpl extends IDispatchImpl implements IHTMLScriptElement {

    public static final String INTERFACE_IDENTIFIER = IHTMLScriptElement.INTERFACE_IDENTIFIER;

    private static final IID _iid = IID.create(INTERFACE_IDENTIFIER);

    public IHTMLScriptElementImpl() {
    }

    protected IHTMLScriptElementImpl(IUnknownImpl that) throws ComException {
        super(that);
    }

    public IHTMLScriptElementImpl(IUnknown that) throws ComException {
        super(that);
    }

    public IHTMLScriptElementImpl(CLSID clsid, ClsCtx dwClsContext) throws ComException {
        super(clsid, dwClsContext);
    }

    public IHTMLScriptElementImpl(CLSID clsid, IUnknownImpl pUnkOuter, ClsCtx dwClsContext) throws ComException {
        super(clsid, pUnkOuter, dwClsContext);
    }

    public void setSrc(BStr p) throws ComException {
        invokeStandardVirtualMethod(7, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Const(p) });
    }

    public BStr getSrc() throws ComException {
        BStr p = new BStr();
        invokeStandardVirtualMethod(8, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public void setHtmlFor(BStr p) throws ComException {
        invokeStandardVirtualMethod(9, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Const(p) });
    }

    public BStr getHtmlFor() throws ComException {
        BStr p = new BStr();
        invokeStandardVirtualMethod(10, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public void setEvent(BStr p) throws ComException {
        invokeStandardVirtualMethod(11, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Const(p) });
    }

    public BStr getEvent() throws ComException {
        BStr p = new BStr();
        invokeStandardVirtualMethod(12, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public void setText(BStr p) throws ComException {
        invokeStandardVirtualMethod(13, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Const(p) });
    }

    public BStr getText() throws ComException {
        BStr p = new BStr();
        invokeStandardVirtualMethod(14, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public void setDefer(VariantBool p) throws ComException {
        invokeStandardVirtualMethod(15, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p });
    }

    public VariantBool getDefer() throws ComException {
        VariantBool p = new VariantBool();
        invokeStandardVirtualMethod(16, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public BStr getReadyState() throws ComException {
        BStr p = new BStr();
        invokeStandardVirtualMethod(17, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public void setOnerror(Variant p) throws ComException {
        invokeStandardVirtualMethod(18, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p });
    }

    public Variant getOnerror() throws ComException {
        Variant p = new Variant();
        invokeStandardVirtualMethod(19, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public void setType(BStr p) throws ComException {
        invokeStandardVirtualMethod(20, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Const(p) });
    }

    public BStr getType() throws ComException {
        BStr p = new BStr();
        invokeStandardVirtualMethod(21, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public IID getIID() {
        return _iid;
    }

    public Object clone() {
        return new IHTMLScriptElementImpl(this);
    }
}
