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
 * Represents COM interface IHtmlDlgSafeHelper.
 */
public class IHtmlDlgSafeHelperImpl extends IDispatchImpl implements IHtmlDlgSafeHelper {

    public static final String INTERFACE_IDENTIFIER = IHtmlDlgSafeHelper.INTERFACE_IDENTIFIER;

    private static final IID _iid = IID.create(INTERFACE_IDENTIFIER);

    public IHtmlDlgSafeHelperImpl() {
    }

    protected IHtmlDlgSafeHelperImpl(IUnknownImpl that) throws ComException {
        super(that);
    }

    public IHtmlDlgSafeHelperImpl(IUnknown that) throws ComException {
        super(that);
    }

    public IHtmlDlgSafeHelperImpl(CLSID clsid, ClsCtx dwClsContext) throws ComException {
        super(clsid, dwClsContext);
    }

    public IHtmlDlgSafeHelperImpl(CLSID clsid, IUnknownImpl pUnkOuter, ClsCtx dwClsContext) throws ComException {
        super(clsid, pUnkOuter, dwClsContext);
    }

    public Variant choosecolordlg(Variant initColor) throws ComException {
        Variant rgbColor = new Variant();
        invokeStandardVirtualMethod(7, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { initColor, rgbColor == null ? (Parameter) PTR_NULL : new Pointer(rgbColor) });
        return rgbColor;
    }

    public Variant getCharset(BStr fontName) throws ComException {
        Variant charset = new Variant();
        invokeStandardVirtualMethod(8, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { fontName == null ? (Parameter) PTR_NULL : new Const(fontName), charset == null ? (Parameter) PTR_NULL : new Pointer(charset) });
        return charset;
    }

    public IDispatch getFonts() throws ComException {
        IDispatchImpl p = new IDispatchImpl();
        invokeStandardVirtualMethod(9, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer((Parameter) p) });
        return p;
    }

    public IDispatch getBlockFormats() throws ComException {
        IDispatchImpl p = new IDispatchImpl();
        invokeStandardVirtualMethod(10, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer((Parameter) p) });
        return p;
    }

    public IID getIID() {
        return _iid;
    }

    public Object clone() {
        return new IHtmlDlgSafeHelperImpl(this);
    }
}
