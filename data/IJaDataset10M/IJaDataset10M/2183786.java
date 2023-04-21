package watij.shdocvw.impl;

import com.jniwrapper.*;
import com.jniwrapper.win32.*;
import com.jniwrapper.win32.automation.*;
import com.jniwrapper.win32.automation.impl.*;
import com.jniwrapper.win32.automation.types.*;
import com.jniwrapper.win32.com.*;
import com.jniwrapper.win32.com.impl.*;
import com.jniwrapper.win32.com.types.*;
import watij.shdocvw.*;

/**
 * Represents COM interface ISearchAssistantOC2.
 */
public class ISearchAssistantOC2Impl extends ISearchAssistantOCImpl implements ISearchAssistantOC2 {

    public static final String INTERFACE_IDENTIFIER = ISearchAssistantOC2.INTERFACE_IDENTIFIER;

    private static final IID _iid = IID.create(INTERFACE_IDENTIFIER);

    public ISearchAssistantOC2Impl() {
    }

    protected ISearchAssistantOC2Impl(IUnknownImpl that) throws ComException {
        super(that);
    }

    public ISearchAssistantOC2Impl(IUnknown that) throws ComException {
        super(that);
    }

    public ISearchAssistantOC2Impl(CLSID clsid, ClsCtx dwClsContext) throws ComException {
        super(clsid, dwClsContext);
    }

    public ISearchAssistantOC2Impl(CLSID clsid, IUnknownImpl pUnkOuter, ClsCtx dwClsContext) throws ComException {
        super(clsid, pUnkOuter, dwClsContext);
    }

    public VariantBool getShowFindPrinter() throws ComException {
        VariantBool pbShowFindPrinter = new VariantBool();
        invokeStandardVirtualMethod(34, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { pbShowFindPrinter == null ? (Parameter) PTR_NULL : new Pointer(pbShowFindPrinter) });
        return pbShowFindPrinter;
    }

    public IID getIID() {
        return _iid;
    }

    public Object clone() {
        return new ISearchAssistantOC2Impl(this);
    }
}
