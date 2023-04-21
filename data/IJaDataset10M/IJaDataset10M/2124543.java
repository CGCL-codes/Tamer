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
 * Represents COM interface IHTMLBodyElement.
 */
public class IHTMLBodyElementImpl extends IDispatchImpl implements IHTMLBodyElement {

    public static final String INTERFACE_IDENTIFIER = IHTMLBodyElement.INTERFACE_IDENTIFIER;

    private static final IID _iid = IID.create(INTERFACE_IDENTIFIER);

    public IHTMLBodyElementImpl() {
    }

    protected IHTMLBodyElementImpl(IUnknownImpl that) throws ComException {
        super(that);
    }

    public IHTMLBodyElementImpl(IUnknown that) throws ComException {
        super(that);
    }

    public IHTMLBodyElementImpl(CLSID clsid, ClsCtx dwClsContext) throws ComException {
        super(clsid, dwClsContext);
    }

    public IHTMLBodyElementImpl(CLSID clsid, IUnknownImpl pUnkOuter, ClsCtx dwClsContext) throws ComException {
        super(clsid, pUnkOuter, dwClsContext);
    }

    public void setBackground(BStr p) throws ComException {
        invokeStandardVirtualMethod(7, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Const(p) });
    }

    public BStr getBackground() throws ComException {
        BStr p = new BStr();
        invokeStandardVirtualMethod(8, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public void setBgProperties(BStr p) throws ComException {
        invokeStandardVirtualMethod(9, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Const(p) });
    }

    public BStr getBgProperties() throws ComException {
        BStr p = new BStr();
        invokeStandardVirtualMethod(10, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public void setLeftMargin(Variant p) throws ComException {
        invokeStandardVirtualMethod(11, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p });
    }

    public Variant getLeftMargin() throws ComException {
        Variant p = new Variant();
        invokeStandardVirtualMethod(12, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public void setTopMargin(Variant p) throws ComException {
        invokeStandardVirtualMethod(13, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p });
    }

    public Variant getTopMargin() throws ComException {
        Variant p = new Variant();
        invokeStandardVirtualMethod(14, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public void setRightMargin(Variant p) throws ComException {
        invokeStandardVirtualMethod(15, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p });
    }

    public Variant getRightMargin() throws ComException {
        Variant p = new Variant();
        invokeStandardVirtualMethod(16, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public void setBottomMargin(Variant p) throws ComException {
        invokeStandardVirtualMethod(17, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p });
    }

    public Variant getBottomMargin() throws ComException {
        Variant p = new Variant();
        invokeStandardVirtualMethod(18, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public void setNoWrap(VariantBool p) throws ComException {
        invokeStandardVirtualMethod(19, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p });
    }

    public VariantBool getNoWrap() throws ComException {
        VariantBool p = new VariantBool();
        invokeStandardVirtualMethod(20, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public void setBgColor(Variant p) throws ComException {
        invokeStandardVirtualMethod(21, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p });
    }

    public Variant getBgColor() throws ComException {
        Variant p = new Variant();
        invokeStandardVirtualMethod(22, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public void setText(Variant p) throws ComException {
        invokeStandardVirtualMethod(23, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p });
    }

    public Variant getText() throws ComException {
        Variant p = new Variant();
        invokeStandardVirtualMethod(24, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public void setLink(Variant p) throws ComException {
        invokeStandardVirtualMethod(25, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p });
    }

    public Variant getLink() throws ComException {
        Variant p = new Variant();
        invokeStandardVirtualMethod(26, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public void setVLink(Variant p) throws ComException {
        invokeStandardVirtualMethod(27, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p });
    }

    public Variant getVLink() throws ComException {
        Variant p = new Variant();
        invokeStandardVirtualMethod(28, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public void setALink(Variant p) throws ComException {
        invokeStandardVirtualMethod(29, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p });
    }

    public Variant getALink() throws ComException {
        Variant p = new Variant();
        invokeStandardVirtualMethod(30, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public void setOnload(Variant p) throws ComException {
        invokeStandardVirtualMethod(31, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p });
    }

    public Variant getOnload() throws ComException {
        Variant p = new Variant();
        invokeStandardVirtualMethod(32, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public void setOnunload(Variant p) throws ComException {
        invokeStandardVirtualMethod(33, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p });
    }

    public Variant getOnunload() throws ComException {
        Variant p = new Variant();
        invokeStandardVirtualMethod(34, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public void setScroll(BStr p) throws ComException {
        invokeStandardVirtualMethod(35, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Const(p) });
    }

    public BStr getScroll() throws ComException {
        BStr p = new BStr();
        invokeStandardVirtualMethod(36, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public void setOnselect(Variant p) throws ComException {
        invokeStandardVirtualMethod(37, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p });
    }

    public Variant getOnselect() throws ComException {
        Variant p = new Variant();
        invokeStandardVirtualMethod(38, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public void setOnbeforeunload(Variant p) throws ComException {
        invokeStandardVirtualMethod(39, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p });
    }

    public Variant getOnbeforeunload() throws ComException {
        Variant p = new Variant();
        invokeStandardVirtualMethod(40, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public IHTMLTxtRange createTextRange() throws ComException {
        IHTMLTxtRangeImpl range = new IHTMLTxtRangeImpl();
        invokeStandardVirtualMethod(41, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { range == null ? (Parameter) PTR_NULL : new Pointer((Parameter) range) });
        return range;
    }

    public IID getIID() {
        return _iid;
    }

    public Object clone() {
        return new IHTMLBodyElementImpl(this);
    }
}
