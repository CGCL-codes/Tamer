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
 * Represents COM interface IHTMLControlRange.
 */
public class IHTMLControlRangeImpl extends IDispatchImpl implements IHTMLControlRange {

    public static final String INTERFACE_IDENTIFIER = IHTMLControlRange.INTERFACE_IDENTIFIER;

    private static final IID _iid = IID.create(INTERFACE_IDENTIFIER);

    public IHTMLControlRangeImpl() {
    }

    protected IHTMLControlRangeImpl(IUnknownImpl that) throws ComException {
        super(that);
    }

    public IHTMLControlRangeImpl(IUnknown that) throws ComException {
        super(that);
    }

    public IHTMLControlRangeImpl(CLSID clsid, ClsCtx dwClsContext) throws ComException {
        super(clsid, dwClsContext);
    }

    public IHTMLControlRangeImpl(CLSID clsid, IUnknownImpl pUnkOuter, ClsCtx dwClsContext) throws ComException {
        super(clsid, pUnkOuter, dwClsContext);
    }

    public void select() throws ComException {
        invokeStandardVirtualMethod(7, Function.STDCALL_CALLING_CONVENTION, new Parameter[0]);
    }

    public void add(IHTMLControlElement item) throws ComException {
        invokeStandardVirtualMethod(8, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { item == null ? (Parameter) PTR_NULL : new Const((Parameter) item) });
    }

    public void remove(Int32 index) throws ComException {
        invokeStandardVirtualMethod(9, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { index });
    }

    public IHTMLElement item(Int32 index) throws ComException {
        IHTMLElementImpl pdisp = new IHTMLElementImpl();
        invokeStandardVirtualMethod(10, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { index, pdisp == null ? (Parameter) PTR_NULL : new Pointer((Parameter) pdisp) });
        return pdisp;
    }

    public void scrollIntoView(Variant varargStart) throws ComException {
        invokeStandardVirtualMethod(11, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { varargStart });
    }

    public VariantBool queryCommandSupported(BStr cmdID) throws ComException {
        VariantBool pfRet = new VariantBool();
        invokeStandardVirtualMethod(12, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { cmdID == null ? (Parameter) PTR_NULL : new Const(cmdID), pfRet == null ? (Parameter) PTR_NULL : new Pointer(pfRet) });
        return pfRet;
    }

    public VariantBool queryCommandEnabled(BStr cmdID) throws ComException {
        VariantBool pfRet = new VariantBool();
        invokeStandardVirtualMethod(13, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { cmdID == null ? (Parameter) PTR_NULL : new Const(cmdID), pfRet == null ? (Parameter) PTR_NULL : new Pointer(pfRet) });
        return pfRet;
    }

    public VariantBool queryCommandState(BStr cmdID) throws ComException {
        VariantBool pfRet = new VariantBool();
        invokeStandardVirtualMethod(14, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { cmdID == null ? (Parameter) PTR_NULL : new Const(cmdID), pfRet == null ? (Parameter) PTR_NULL : new Pointer(pfRet) });
        return pfRet;
    }

    public VariantBool queryCommandIndeterm(BStr cmdID) throws ComException {
        VariantBool pfRet = new VariantBool();
        invokeStandardVirtualMethod(15, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { cmdID == null ? (Parameter) PTR_NULL : new Const(cmdID), pfRet == null ? (Parameter) PTR_NULL : new Pointer(pfRet) });
        return pfRet;
    }

    public BStr queryCommandText(BStr cmdID) throws ComException {
        BStr pcmdText = new BStr();
        invokeStandardVirtualMethod(16, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { cmdID == null ? (Parameter) PTR_NULL : new Const(cmdID), pcmdText == null ? (Parameter) PTR_NULL : new Pointer(pcmdText) });
        return pcmdText;
    }

    public Variant queryCommandValue(BStr cmdID) throws ComException {
        Variant pcmdValue = new Variant();
        invokeStandardVirtualMethod(17, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { cmdID == null ? (Parameter) PTR_NULL : new Const(cmdID), pcmdValue == null ? (Parameter) PTR_NULL : new Pointer(pcmdValue) });
        return pcmdValue;
    }

    public VariantBool execCommand(BStr cmdID, VariantBool showUI, Variant value) throws ComException {
        VariantBool pfRet = new VariantBool();
        invokeStandardVirtualMethod(18, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { cmdID == null ? (Parameter) PTR_NULL : new Const(cmdID), showUI, value, pfRet == null ? (Parameter) PTR_NULL : new Pointer(pfRet) });
        return pfRet;
    }

    public VariantBool execCommandShowHelp(BStr cmdID) throws ComException {
        VariantBool pfRet = new VariantBool();
        invokeStandardVirtualMethod(19, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { cmdID == null ? (Parameter) PTR_NULL : new Const(cmdID), pfRet == null ? (Parameter) PTR_NULL : new Pointer(pfRet) });
        return pfRet;
    }

    public IHTMLElement commonParentElement() throws ComException {
        IHTMLElementImpl parent = new IHTMLElementImpl();
        invokeStandardVirtualMethod(20, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { parent == null ? (Parameter) PTR_NULL : new Pointer((Parameter) parent) });
        return parent;
    }

    public Int32 invokeGetLength() throws ComException {
        Int32 p = new Int32();
        invokeStandardVirtualMethod(21, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public IID getIID() {
        return _iid;
    }

    public Object clone() {
        return new IHTMLControlRangeImpl(this);
    }
}
