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
 * Represents COM interface ISearch.
 */
public class ISearchImpl extends IDispatchImpl implements ISearch {

    public static final String INTERFACE_IDENTIFIER = ISearch.INTERFACE_IDENTIFIER;

    private static final IID _iid = IID.create(INTERFACE_IDENTIFIER);

    public ISearchImpl() {
    }

    protected ISearchImpl(IUnknownImpl that) throws ComException {
        super(that);
    }

    public ISearchImpl(IUnknown that) throws ComException {
        super(that);
    }

    public ISearchImpl(CLSID clsid, ClsCtx dwClsContext) throws ComException {
        super(clsid, dwClsContext);
    }

    public ISearchImpl(CLSID clsid, IUnknownImpl pUnkOuter, ClsCtx dwClsContext) throws ComException {
        super(clsid, pUnkOuter, dwClsContext);
    }

    public BStr getTitle() throws ComException {
        BStr pbstrTitle = new BStr();
        invokeStandardVirtualMethod(7, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { pbstrTitle == null ? (Parameter) PTR_NULL : new Pointer(pbstrTitle) });
        return pbstrTitle;
    }

    public BStr getId() throws ComException {
        BStr pbstrId = new BStr();
        invokeStandardVirtualMethod(8, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { pbstrId == null ? (Parameter) PTR_NULL : new Pointer(pbstrId) });
        return pbstrId;
    }

    public BStr getURL() throws ComException {
        BStr pbstrUrl = new BStr();
        invokeStandardVirtualMethod(9, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { pbstrUrl == null ? (Parameter) PTR_NULL : new Pointer(pbstrUrl) });
        return pbstrUrl;
    }

    public IID getIID() {
        return _iid;
    }

    public Object clone() {
        return new ISearchImpl(this);
    }
}
