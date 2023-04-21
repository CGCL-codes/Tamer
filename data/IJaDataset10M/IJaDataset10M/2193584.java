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
 * Represents COM interface IHTMLAreaElement.
 */
public class IHTMLAreaElementImpl extends IDispatchImpl implements IHTMLAreaElement {

    public static final String INTERFACE_IDENTIFIER = IHTMLAreaElement.INTERFACE_IDENTIFIER;

    private static final IID _iid = IID.create(INTERFACE_IDENTIFIER);

    public IHTMLAreaElementImpl() {
    }

    protected IHTMLAreaElementImpl(IUnknownImpl that) throws ComException {
        super(that);
    }

    public IHTMLAreaElementImpl(IUnknown that) throws ComException {
        super(that);
    }

    public IHTMLAreaElementImpl(CLSID clsid, ClsCtx dwClsContext) throws ComException {
        super(clsid, dwClsContext);
    }

    public IHTMLAreaElementImpl(CLSID clsid, IUnknownImpl pUnkOuter, ClsCtx dwClsContext) throws ComException {
        super(clsid, pUnkOuter, dwClsContext);
    }

    public void setShape(BStr p) throws ComException {
        invokeStandardVirtualMethod(7, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Const(p) });
    }

    public BStr getShape() throws ComException {
        BStr p = new BStr();
        invokeStandardVirtualMethod(8, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public void setCoords(BStr p) throws ComException {
        invokeStandardVirtualMethod(9, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Const(p) });
    }

    public BStr getCoords() throws ComException {
        BStr p = new BStr();
        invokeStandardVirtualMethod(10, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public void setHref(BStr p) throws ComException {
        invokeStandardVirtualMethod(11, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Const(p) });
    }

    public BStr getHref() throws ComException {
        BStr p = new BStr();
        invokeStandardVirtualMethod(12, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public void setTarget(BStr p) throws ComException {
        invokeStandardVirtualMethod(13, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Const(p) });
    }

    public BStr getTarget() throws ComException {
        BStr p = new BStr();
        invokeStandardVirtualMethod(14, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public void setAlt(BStr p) throws ComException {
        invokeStandardVirtualMethod(15, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Const(p) });
    }

    public BStr getAlt() throws ComException {
        BStr p = new BStr();
        invokeStandardVirtualMethod(16, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public void setNoHref(VariantBool p) throws ComException {
        invokeStandardVirtualMethod(17, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p });
    }

    public VariantBool getNoHref() throws ComException {
        VariantBool p = new VariantBool();
        invokeStandardVirtualMethod(18, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public void setHost(BStr p) throws ComException {
        invokeStandardVirtualMethod(19, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Const(p) });
    }

    public BStr getHost() throws ComException {
        BStr p = new BStr();
        invokeStandardVirtualMethod(20, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public void setHostname(BStr p) throws ComException {
        invokeStandardVirtualMethod(21, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Const(p) });
    }

    public BStr getHostname() throws ComException {
        BStr p = new BStr();
        invokeStandardVirtualMethod(22, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public void setPathname(BStr p) throws ComException {
        invokeStandardVirtualMethod(23, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Const(p) });
    }

    public BStr getPathname() throws ComException {
        BStr p = new BStr();
        invokeStandardVirtualMethod(24, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public void setPort(BStr p) throws ComException {
        invokeStandardVirtualMethod(25, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Const(p) });
    }

    public BStr getPort() throws ComException {
        BStr p = new BStr();
        invokeStandardVirtualMethod(26, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public void setProtocol(BStr p) throws ComException {
        invokeStandardVirtualMethod(27, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Const(p) });
    }

    public BStr getProtocol() throws ComException {
        BStr p = new BStr();
        invokeStandardVirtualMethod(28, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public void setSearch(BStr p) throws ComException {
        invokeStandardVirtualMethod(29, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Const(p) });
    }

    public BStr getSearch() throws ComException {
        BStr p = new BStr();
        invokeStandardVirtualMethod(30, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public void setHash(BStr p) throws ComException {
        invokeStandardVirtualMethod(31, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Const(p) });
    }

    public BStr getHash() throws ComException {
        BStr p = new BStr();
        invokeStandardVirtualMethod(32, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public void setOnblur(Variant p) throws ComException {
        invokeStandardVirtualMethod(33, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p });
    }

    public Variant getOnblur() throws ComException {
        Variant p = new Variant();
        invokeStandardVirtualMethod(34, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public void setOnfocus(Variant p) throws ComException {
        invokeStandardVirtualMethod(35, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p });
    }

    public Variant getOnfocus() throws ComException {
        Variant p = new Variant();
        invokeStandardVirtualMethod(36, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public void setTabIndex(Int16 p) throws ComException {
        invokeStandardVirtualMethod(37, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p });
    }

    public Int16 getTabIndex() throws ComException {
        Int16 p = new Int16();
        invokeStandardVirtualMethod(38, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public void focus() throws ComException {
        invokeStandardVirtualMethod(39, Function.STDCALL_CALLING_CONVENTION, new Parameter[0]);
    }

    public void blur() throws ComException {
        invokeStandardVirtualMethod(40, Function.STDCALL_CALLING_CONVENTION, new Parameter[0]);
    }

    public IID getIID() {
        return _iid;
    }

    public Object clone() {
        return new IHTMLAreaElementImpl(this);
    }
}
