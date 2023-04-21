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
 * Represents COM interface IHTMLObjectElement.
 */
public class IHTMLObjectElementImpl extends IDispatchImpl implements IHTMLObjectElement {

    public static final String INTERFACE_IDENTIFIER = IHTMLObjectElement.INTERFACE_IDENTIFIER;

    private static final IID _iid = IID.create(INTERFACE_IDENTIFIER);

    public IHTMLObjectElementImpl() {
    }

    protected IHTMLObjectElementImpl(IUnknownImpl that) throws ComException {
        super(that);
    }

    public IHTMLObjectElementImpl(IUnknown that) throws ComException {
        super(that);
    }

    public IHTMLObjectElementImpl(CLSID clsid, ClsCtx dwClsContext) throws ComException {
        super(clsid, dwClsContext);
    }

    public IHTMLObjectElementImpl(CLSID clsid, IUnknownImpl pUnkOuter, ClsCtx dwClsContext) throws ComException {
        super(clsid, pUnkOuter, dwClsContext);
    }

    public IDispatch getObject() throws ComException {
        IDispatchImpl p = new IDispatchImpl();
        invokeStandardVirtualMethod(7, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer((Parameter) p) });
        return p;
    }

    public BStr getClassid() throws ComException {
        BStr p = new BStr();
        invokeStandardVirtualMethod(8, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public BStr getData() throws ComException {
        BStr p = new BStr();
        invokeStandardVirtualMethod(9, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public void setRecordset(IDispatch p) throws ComException {
        invokeStandardVirtualMethod(10, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Const((Parameter) p) });
    }

    public IDispatch getRecordset() throws ComException {
        IDispatchImpl p = new IDispatchImpl();
        invokeStandardVirtualMethod(11, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer((Parameter) p) });
        return p;
    }

    public void setAlign(BStr p) throws ComException {
        invokeStandardVirtualMethod(12, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Const(p) });
    }

    public BStr getAlign() throws ComException {
        BStr p = new BStr();
        invokeStandardVirtualMethod(13, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public void setName(BStr p) throws ComException {
        invokeStandardVirtualMethod(14, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Const(p) });
    }

    public BStr getName() throws ComException {
        BStr p = new BStr();
        invokeStandardVirtualMethod(15, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public void setCodeBase(BStr p) throws ComException {
        invokeStandardVirtualMethod(16, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Const(p) });
    }

    public BStr getCodeBase() throws ComException {
        BStr p = new BStr();
        invokeStandardVirtualMethod(17, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public void setCodeType(BStr p) throws ComException {
        invokeStandardVirtualMethod(18, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Const(p) });
    }

    public BStr getCodeType() throws ComException {
        BStr p = new BStr();
        invokeStandardVirtualMethod(19, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public void setCode(BStr p) throws ComException {
        invokeStandardVirtualMethod(20, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Const(p) });
    }

    public BStr getCode() throws ComException {
        BStr p = new BStr();
        invokeStandardVirtualMethod(21, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public BStr getBaseHref() throws ComException {
        BStr p = new BStr();
        invokeStandardVirtualMethod(22, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public void setType(BStr p) throws ComException {
        invokeStandardVirtualMethod(23, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Const(p) });
    }

    public BStr getType() throws ComException {
        BStr p = new BStr();
        invokeStandardVirtualMethod(24, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public IHTMLFormElement getForm() throws ComException {
        IHTMLFormElementImpl p = new IHTMLFormElementImpl();
        invokeStandardVirtualMethod(25, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer((Parameter) p) });
        return p;
    }

    public void setWidth(Variant p) throws ComException {
        invokeStandardVirtualMethod(26, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p });
    }

    public Variant getWidth() throws ComException {
        Variant p = new Variant();
        invokeStandardVirtualMethod(27, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public void setHeight(Variant p) throws ComException {
        invokeStandardVirtualMethod(28, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p });
    }

    public Variant getHeight() throws ComException {
        Variant p = new Variant();
        invokeStandardVirtualMethod(29, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public Int32 getReadyState() throws ComException {
        Int32 p = new Int32();
        invokeStandardVirtualMethod(30, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public void setOnreadystatechange(Variant p) throws ComException {
        invokeStandardVirtualMethod(31, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p });
    }

    public Variant getOnreadystatechange() throws ComException {
        Variant p = new Variant();
        invokeStandardVirtualMethod(32, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public void setOnerror(Variant p) throws ComException {
        invokeStandardVirtualMethod(33, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p });
    }

    public Variant getOnerror() throws ComException {
        Variant p = new Variant();
        invokeStandardVirtualMethod(34, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public void setAltHtml(BStr p) throws ComException {
        invokeStandardVirtualMethod(35, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Const(p) });
    }

    public BStr getAltHtml() throws ComException {
        BStr p = new BStr();
        invokeStandardVirtualMethod(36, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public void setVspace(Int32 p) throws ComException {
        invokeStandardVirtualMethod(37, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p });
    }

    public Int32 getVspace() throws ComException {
        Int32 p = new Int32();
        invokeStandardVirtualMethod(38, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public void setHspace(Int32 p) throws ComException {
        invokeStandardVirtualMethod(39, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p });
    }

    public Int32 getHspace() throws ComException {
        Int32 p = new Int32();
        invokeStandardVirtualMethod(40, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public IID getIID() {
        return _iid;
    }

    public Object clone() {
        return new IHTMLObjectElementImpl(this);
    }
}
