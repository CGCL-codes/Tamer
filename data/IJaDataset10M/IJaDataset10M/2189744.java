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
 * Represents COM interface IElementBehavior.
 */
public class IElementBehaviorImpl extends IUnknownImpl implements IElementBehavior {

    public static final String INTERFACE_IDENTIFIER = IElementBehavior.INTERFACE_IDENTIFIER;

    private static final IID _iid = IID.create(INTERFACE_IDENTIFIER);

    public IElementBehaviorImpl() {
    }

    protected IElementBehaviorImpl(IUnknownImpl that) throws ComException {
        super(that);
    }

    public IElementBehaviorImpl(IUnknown that) throws ComException {
        super(that);
    }

    public IElementBehaviorImpl(CLSID clsid, ClsCtx dwClsContext) throws ComException {
        super(clsid, dwClsContext);
    }

    public IElementBehaviorImpl(CLSID clsid, IUnknownImpl pUnkOuter, ClsCtx dwClsContext) throws ComException {
        super(clsid, pUnkOuter, dwClsContext);
    }

    public void init(IElementBehaviorSite pBehaviorSite) throws ComException {
        invokeStandardVirtualMethod(3, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { pBehaviorSite == null ? (Parameter) PTR_NULL : new Const((Parameter) pBehaviorSite) });
    }

    public void invokeNotify(Int32 lEvent, Variant pVar) throws ComException {
        invokeStandardVirtualMethod(4, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { lEvent, pVar == null ? (Parameter) PTR_NULL : new Pointer(pVar) });
    }

    public void detach() throws ComException {
        invokeStandardVirtualMethod(5, Function.STDCALL_CALLING_CONVENTION, new Parameter[0]);
    }

    public IID getIID() {
        return _iid;
    }

    public Object clone() {
        return new IElementBehaviorImpl(this);
    }
}
