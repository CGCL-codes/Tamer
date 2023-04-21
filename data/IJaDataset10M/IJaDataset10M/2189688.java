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
 * Represents COM interface IHTMLEditDesigner.
 */
public class IHTMLEditDesignerImpl extends IUnknownImpl implements IHTMLEditDesigner {

    public static final String INTERFACE_IDENTIFIER = IHTMLEditDesigner.INTERFACE_IDENTIFIER;

    private static final IID _iid = IID.create(INTERFACE_IDENTIFIER);

    public IHTMLEditDesignerImpl() {
    }

    protected IHTMLEditDesignerImpl(IUnknownImpl that) throws ComException {
        super(that);
    }

    public IHTMLEditDesignerImpl(IUnknown that) throws ComException {
        super(that);
    }

    public IHTMLEditDesignerImpl(CLSID clsid, ClsCtx dwClsContext) throws ComException {
        super(clsid, dwClsContext);
    }

    public IHTMLEditDesignerImpl(CLSID clsid, IUnknownImpl pUnkOuter, ClsCtx dwClsContext) throws ComException {
        super(clsid, pUnkOuter, dwClsContext);
    }

    public void preHandleEvent(Int32 inEvtDispId, IHTMLEventObj pIEventObj) throws ComException {
        invokeStandardVirtualMethod(3, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { inEvtDispId, pIEventObj == null ? (Parameter) PTR_NULL : new Const((Parameter) pIEventObj) });
    }

    public void postHandleEvent(Int32 inEvtDispId, IHTMLEventObj pIEventObj) throws ComException {
        invokeStandardVirtualMethod(4, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { inEvtDispId, pIEventObj == null ? (Parameter) PTR_NULL : new Const((Parameter) pIEventObj) });
    }

    public void translateAccelerator(Int32 inEvtDispId, IHTMLEventObj pIEventObj) throws ComException {
        invokeStandardVirtualMethod(5, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { inEvtDispId, pIEventObj == null ? (Parameter) PTR_NULL : new Const((Parameter) pIEventObj) });
    }

    public void postEditorEventNotify(Int32 inEvtDispId, IHTMLEventObj pIEventObj) throws ComException {
        invokeStandardVirtualMethod(6, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { inEvtDispId, pIEventObj == null ? (Parameter) PTR_NULL : new Const((Parameter) pIEventObj) });
    }

    public IID getIID() {
        return _iid;
    }

    public Object clone() {
        return new IHTMLEditDesignerImpl(this);
    }
}
