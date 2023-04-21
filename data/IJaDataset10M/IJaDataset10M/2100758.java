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
 * Represents COM interface IHTMLAttributeCollection2.
 */
public class IHTMLAttributeCollection2Impl extends IDispatchImpl implements IHTMLAttributeCollection2 {

    public static final String INTERFACE_IDENTIFIER = IHTMLAttributeCollection2.INTERFACE_IDENTIFIER;

    private static final IID _iid = IID.create(INTERFACE_IDENTIFIER);

    public IHTMLAttributeCollection2Impl() {
    }

    protected IHTMLAttributeCollection2Impl(IUnknownImpl that) throws ComException {
        super(that);
    }

    public IHTMLAttributeCollection2Impl(IUnknown that) throws ComException {
        super(that);
    }

    public IHTMLAttributeCollection2Impl(CLSID clsid, ClsCtx dwClsContext) throws ComException {
        super(clsid, dwClsContext);
    }

    public IHTMLAttributeCollection2Impl(CLSID clsid, IUnknownImpl pUnkOuter, ClsCtx dwClsContext) throws ComException {
        super(clsid, pUnkOuter, dwClsContext);
    }

    public IHTMLDOMAttribute getNamedItem(BStr bstrName) throws ComException {
        IHTMLDOMAttributeImpl newretNode = new IHTMLDOMAttributeImpl();
        invokeStandardVirtualMethod(7, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { bstrName == null ? (Parameter) PTR_NULL : new Const(bstrName), newretNode == null ? (Parameter) PTR_NULL : new Pointer((Parameter) newretNode) });
        return newretNode;
    }

    public IHTMLDOMAttribute setNamedItem(IHTMLDOMAttribute ppNode) throws ComException {
        IHTMLDOMAttributeImpl newretNode = new IHTMLDOMAttributeImpl();
        invokeStandardVirtualMethod(8, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { ppNode == null ? (Parameter) PTR_NULL : new Const((Parameter) ppNode), newretNode == null ? (Parameter) PTR_NULL : new Pointer((Parameter) newretNode) });
        return newretNode;
    }

    public IHTMLDOMAttribute removeNamedItem(BStr bstrName) throws ComException {
        IHTMLDOMAttributeImpl newretNode = new IHTMLDOMAttributeImpl();
        invokeStandardVirtualMethod(9, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { bstrName == null ? (Parameter) PTR_NULL : new Const(bstrName), newretNode == null ? (Parameter) PTR_NULL : new Pointer((Parameter) newretNode) });
        return newretNode;
    }

    public IID getIID() {
        return _iid;
    }

    public Object clone() {
        return new IHTMLAttributeCollection2Impl(this);
    }
}
