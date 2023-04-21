package watij.mshtml.impl;

import com.jniwrapper.*;
import com.jniwrapper.win32.*;
import com.jniwrapper.win32.automation.types.*;
import com.jniwrapper.win32.com.*;
import com.jniwrapper.win32.com.impl.*;
import com.jniwrapper.win32.com.types.*;
import watij.mshtml.*;

/**
 * Represents COM interface IEnumPrivacyRecords.
 */
public class IEnumPrivacyRecordsImpl extends IUnknownImpl implements IEnumPrivacyRecords {

    public static final String INTERFACE_IDENTIFIER = IEnumPrivacyRecords.INTERFACE_IDENTIFIER;

    private static final IID _iid = IID.create(INTERFACE_IDENTIFIER);

    public IEnumPrivacyRecordsImpl() {
    }

    protected IEnumPrivacyRecordsImpl(IUnknownImpl that) throws ComException {
        super(that);
    }

    public IEnumPrivacyRecordsImpl(IUnknown that) throws ComException {
        super(that);
    }

    public IEnumPrivacyRecordsImpl(CLSID clsid, ClsCtx dwClsContext) throws ComException {
        super(clsid, dwClsContext);
    }

    public IEnumPrivacyRecordsImpl(CLSID clsid, IUnknownImpl pUnkOuter, ClsCtx dwClsContext) throws ComException {
        super(clsid, pUnkOuter, dwClsContext);
    }

    public void reset() throws ComException {
        invokeStandardVirtualMethod(3, Function.STDCALL_CALLING_CONVENTION, new Parameter[0]);
    }

    public void getSize(UInt32 pSize) throws ComException {
        invokeStandardVirtualMethod(4, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { pSize == null ? (Parameter) PTR_NULL : new Pointer(pSize) });
    }

    public void getPrivacyImpacted(Int32 pState) throws ComException {
        invokeStandardVirtualMethod(5, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { pState == null ? (Parameter) PTR_NULL : new Pointer(pState) });
    }

    public void next(BStr pbstrUrl, BStr pbstrPolicyRef, Int32 pdwReserved, UInt32 pdwPrivacyFlags) throws ComException {
        invokeStandardVirtualMethod(6, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { pbstrUrl == null ? (Parameter) PTR_NULL : new Pointer(pbstrUrl), pbstrPolicyRef == null ? (Parameter) PTR_NULL : new Pointer(pbstrPolicyRef), pdwReserved == null ? (Parameter) PTR_NULL : new Pointer(pdwReserved), pdwPrivacyFlags == null ? (Parameter) PTR_NULL : new Pointer(pdwPrivacyFlags) });
    }

    public IID getIID() {
        return _iid;
    }

    public Object clone() {
        return new IEnumPrivacyRecordsImpl(this);
    }
}
