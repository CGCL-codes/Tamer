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
 * Represents COM dispinterface DispCEventObj.
 */
public class DispCEventObjImpl extends IDispatchImpl implements DispCEventObj {

    public static final String INTERFACE_IDENTIFIER = "{3050F558-98B5-11CF-BB82-00AA00BDCE0B}";

    private static final IID _iid = IID.create(INTERFACE_IDENTIFIER);

    public DispCEventObjImpl() {
    }

    protected DispCEventObjImpl(IUnknownImpl that) throws ComException {
        super(that);
    }

    public DispCEventObjImpl(IUnknown that) throws ComException {
        super(that);
    }

    public DispCEventObjImpl(CLSID clsid, ClsCtx dwClsContext) throws ComException {
        super(clsid, dwClsContext);
    }

    public DispCEventObjImpl(CLSID clsid, IUnknownImpl pUnkOuter, ClsCtx dwClsContext) throws ComException {
        super(clsid, pUnkOuter, dwClsContext);
    }

    public void setReturnValue(Variant param1) {
        Parameter[] parameters = new Parameter[] { param1 };
        Automation.setDispatchProperty(this, "returnValue", parameters);
    }

    public Variant getReturnValue() {
        Parameter[] parameters = new Parameter[0];
        return (Variant) Automation.getDispatchProperty(this, "returnValue", parameters, Variant.class);
    }

    public void setCancelBubble(VariantBool param1) {
        Parameter[] parameters = new Parameter[] { param1 };
        Automation.setDispatchProperty(this, "cancelBubble", parameters);
    }

    public VariantBool getCancelBubble() {
        Parameter[] parameters = new Parameter[0];
        return (VariantBool) Automation.getDispatchProperty(this, "cancelBubble", parameters, VariantBool.class);
    }

    public void setKeyCode(Int32 param1) {
        Parameter[] parameters = new Parameter[] { param1 };
        Automation.setDispatchProperty(this, "keyCode", parameters);
    }

    public Int32 getKeyCode() {
        Parameter[] parameters = new Parameter[0];
        return (Int32) Automation.getDispatchProperty(this, "keyCode", parameters, Int32.class);
    }

    public void setAttribute(BStr strAttributeName, Variant AttributeValue, Int32 lFlags) {
        Parameter[] parameters = new Parameter[] { strAttributeName == null ? (Parameter) PTR_NULL : strAttributeName, AttributeValue, lFlags };
        Automation.invokeDispatch(this, "setAttribute", parameters, void.class);
    }

    public Variant getAttribute(BStr strAttributeName, Int32 lFlags) {
        Parameter[] parameters = new Parameter[] { strAttributeName == null ? (Parameter) PTR_NULL : strAttributeName, lFlags };
        Object result = Automation.invokeDispatch(this, "getAttribute", parameters, Variant.class);
        return (Variant) result;
    }

    public VariantBool removeAttribute(BStr strAttributeName, Int32 lFlags) {
        Parameter[] parameters = new Parameter[] { strAttributeName == null ? (Parameter) PTR_NULL : strAttributeName, lFlags };
        Object result = Automation.invokeDispatch(this, "removeAttribute", parameters, VariantBool.class);
        return (VariantBool) result;
    }

    public void setPropertyName(BStr param1) {
        Parameter[] parameters = new Parameter[] { param1 == null ? (Parameter) PTR_NULL : param1 };
        Automation.setDispatchProperty(this, "propertyName", parameters);
    }

    public BStr getPropertyName() {
        Parameter[] parameters = new Parameter[0];
        return (BStr) Automation.getDispatchProperty(this, "propertyName", parameters, BStr.class);
    }

    public void setBookmarks(IHTMLBookmarkCollection param1) {
        Parameter[] parameters = new Parameter[] { param1 == null ? (Parameter) PTR_NULL : (Parameter) param1 };
    }

    public IHTMLBookmarkCollection getBookmarks() {
        Parameter[] parameters = new Parameter[0];
        return (IHTMLBookmarkCollection) Automation.getDispatchProperty(this, "bookmarks", parameters, IHTMLBookmarkCollectionImpl.class);
    }

    public void setRecordset(IDispatch param1) {
        Parameter[] parameters = new Parameter[] { param1 == null ? (Parameter) PTR_NULL : (Parameter) param1 };
    }

    public IDispatch getRecordset() {
        Parameter[] parameters = new Parameter[0];
        return (IDispatch) Automation.getDispatchProperty(this, "recordset", parameters, IDispatchImpl.class);
    }

    public void setDataFld(BStr param1) {
        Parameter[] parameters = new Parameter[] { param1 == null ? (Parameter) PTR_NULL : param1 };
        Automation.setDispatchProperty(this, "dataFld", parameters);
    }

    public BStr getDataFld() {
        Parameter[] parameters = new Parameter[0];
        return (BStr) Automation.getDispatchProperty(this, "dataFld", parameters, BStr.class);
    }

    public void setBoundElements(IHTMLElementCollection param1) {
        Parameter[] parameters = new Parameter[] { param1 == null ? (Parameter) PTR_NULL : (Parameter) param1 };
    }

    public IHTMLElementCollection getBoundElements() {
        Parameter[] parameters = new Parameter[0];
        return (IHTMLElementCollection) Automation.getDispatchProperty(this, "boundElements", parameters, IHTMLElementCollectionImpl.class);
    }

    public void setRepeat(VariantBool param1) {
        Parameter[] parameters = new Parameter[] { param1 };
        Automation.setDispatchProperty(this, "repeat", parameters);
    }

    public VariantBool getRepeat() {
        Parameter[] parameters = new Parameter[0];
        return (VariantBool) Automation.getDispatchProperty(this, "repeat", parameters, VariantBool.class);
    }

    public void setSrcUrn(BStr param1) {
        Parameter[] parameters = new Parameter[] { param1 == null ? (Parameter) PTR_NULL : param1 };
        Automation.setDispatchProperty(this, "srcUrn", parameters);
    }

    public BStr getSrcUrn() {
        Parameter[] parameters = new Parameter[0];
        return (BStr) Automation.getDispatchProperty(this, "srcUrn", parameters, BStr.class);
    }

    public void setSrcElement(IHTMLElement param1) {
        Parameter[] parameters = new Parameter[] { param1 == null ? (Parameter) PTR_NULL : (Parameter) param1 };
    }

    public IHTMLElement getSrcElement() {
        Parameter[] parameters = new Parameter[0];
        return (IHTMLElement) Automation.getDispatchProperty(this, "srcElement", parameters, IHTMLElementImpl.class);
    }

    public void setAltKey(VariantBool param1) {
        Parameter[] parameters = new Parameter[] { param1 };
        Automation.setDispatchProperty(this, "altKey", parameters);
    }

    public VariantBool getAltKey() {
        Parameter[] parameters = new Parameter[0];
        return (VariantBool) Automation.getDispatchProperty(this, "altKey", parameters, VariantBool.class);
    }

    public void setCtrlKey(VariantBool param1) {
        Parameter[] parameters = new Parameter[] { param1 };
        Automation.setDispatchProperty(this, "ctrlKey", parameters);
    }

    public VariantBool getCtrlKey() {
        Parameter[] parameters = new Parameter[0];
        return (VariantBool) Automation.getDispatchProperty(this, "ctrlKey", parameters, VariantBool.class);
    }

    public void setShiftKey(VariantBool param1) {
        Parameter[] parameters = new Parameter[] { param1 };
        Automation.setDispatchProperty(this, "shiftKey", parameters);
    }

    public VariantBool getShiftKey() {
        Parameter[] parameters = new Parameter[0];
        return (VariantBool) Automation.getDispatchProperty(this, "shiftKey", parameters, VariantBool.class);
    }

    public void setFromElement(IHTMLElement param1) {
        Parameter[] parameters = new Parameter[] { param1 == null ? (Parameter) PTR_NULL : (Parameter) param1 };
    }

    public IHTMLElement getFromElement() {
        Parameter[] parameters = new Parameter[0];
        return (IHTMLElement) Automation.getDispatchProperty(this, "fromElement", parameters, IHTMLElementImpl.class);
    }

    public void setToElement(IHTMLElement param1) {
        Parameter[] parameters = new Parameter[] { param1 == null ? (Parameter) PTR_NULL : (Parameter) param1 };
    }

    public IHTMLElement getToElement() {
        Parameter[] parameters = new Parameter[0];
        return (IHTMLElement) Automation.getDispatchProperty(this, "toElement", parameters, IHTMLElementImpl.class);
    }

    public void setButton(Int32 param1) {
        Parameter[] parameters = new Parameter[] { param1 };
        Automation.setDispatchProperty(this, "button", parameters);
    }

    public Int32 getButton() {
        Parameter[] parameters = new Parameter[0];
        return (Int32) Automation.getDispatchProperty(this, "button", parameters, Int32.class);
    }

    public void setType(BStr param1) {
        Parameter[] parameters = new Parameter[] { param1 == null ? (Parameter) PTR_NULL : param1 };
        Automation.setDispatchProperty(this, "type", parameters);
    }

    public BStr getType() {
        Parameter[] parameters = new Parameter[0];
        return (BStr) Automation.getDispatchProperty(this, "type", parameters, BStr.class);
    }

    public void setQualifier(BStr param1) {
        Parameter[] parameters = new Parameter[] { param1 == null ? (Parameter) PTR_NULL : param1 };
        Automation.setDispatchProperty(this, "qualifier", parameters);
    }

    public BStr getQualifier() {
        Parameter[] parameters = new Parameter[0];
        return (BStr) Automation.getDispatchProperty(this, "qualifier", parameters, BStr.class);
    }

    public void setReason(Int32 param1) {
        Parameter[] parameters = new Parameter[] { param1 };
        Automation.setDispatchProperty(this, "reason", parameters);
    }

    public Int32 getReason() {
        Parameter[] parameters = new Parameter[0];
        return (Int32) Automation.getDispatchProperty(this, "reason", parameters, Int32.class);
    }

    public void setX(Int32 param1) {
        Parameter[] parameters = new Parameter[] { param1 };
        Automation.setDispatchProperty(this, "x", parameters);
    }

    public Int32 getX() {
        Parameter[] parameters = new Parameter[0];
        return (Int32) Automation.getDispatchProperty(this, "x", parameters, Int32.class);
    }

    public void setY(Int32 param1) {
        Parameter[] parameters = new Parameter[] { param1 };
        Automation.setDispatchProperty(this, "y", parameters);
    }

    public Int32 getY() {
        Parameter[] parameters = new Parameter[0];
        return (Int32) Automation.getDispatchProperty(this, "y", parameters, Int32.class);
    }

    public void setClientX(Int32 param1) {
        Parameter[] parameters = new Parameter[] { param1 };
        Automation.setDispatchProperty(this, "clientX", parameters);
    }

    public Int32 getClientX() {
        Parameter[] parameters = new Parameter[0];
        return (Int32) Automation.getDispatchProperty(this, "clientX", parameters, Int32.class);
    }

    public void setClientY(Int32 param1) {
        Parameter[] parameters = new Parameter[] { param1 };
        Automation.setDispatchProperty(this, "clientY", parameters);
    }

    public Int32 getClientY() {
        Parameter[] parameters = new Parameter[0];
        return (Int32) Automation.getDispatchProperty(this, "clientY", parameters, Int32.class);
    }

    public void setOffsetX(Int32 param1) {
        Parameter[] parameters = new Parameter[] { param1 };
        Automation.setDispatchProperty(this, "offsetX", parameters);
    }

    public Int32 getOffsetX() {
        Parameter[] parameters = new Parameter[0];
        return (Int32) Automation.getDispatchProperty(this, "offsetX", parameters, Int32.class);
    }

    public void setOffsetY(Int32 param1) {
        Parameter[] parameters = new Parameter[] { param1 };
        Automation.setDispatchProperty(this, "offsetY", parameters);
    }

    public Int32 getOffsetY() {
        Parameter[] parameters = new Parameter[0];
        return (Int32) Automation.getDispatchProperty(this, "offsetY", parameters, Int32.class);
    }

    public void setScreenX(Int32 param1) {
        Parameter[] parameters = new Parameter[] { param1 };
        Automation.setDispatchProperty(this, "screenX", parameters);
    }

    public Int32 getScreenX() {
        Parameter[] parameters = new Parameter[0];
        return (Int32) Automation.getDispatchProperty(this, "screenX", parameters, Int32.class);
    }

    public void setScreenY(Int32 param1) {
        Parameter[] parameters = new Parameter[] { param1 };
        Automation.setDispatchProperty(this, "screenY", parameters);
    }

    public Int32 getScreenY() {
        Parameter[] parameters = new Parameter[0];
        return (Int32) Automation.getDispatchProperty(this, "screenY", parameters, Int32.class);
    }

    public void setSrcFilter(IDispatch param1) {
        Parameter[] parameters = new Parameter[] { param1 == null ? (Parameter) PTR_NULL : (Parameter) param1 };
    }

    public IDispatch getSrcFilter() {
        Parameter[] parameters = new Parameter[0];
        return (IDispatch) Automation.getDispatchProperty(this, "srcFilter", parameters, IDispatchImpl.class);
    }

    public IHTMLDataTransfer getDataTransfer() {
        Parameter[] parameters = new Parameter[0];
        return (IHTMLDataTransfer) Automation.getDispatchProperty(this, "dataTransfer", parameters, IHTMLDataTransferImpl.class);
    }

    public VariantBool getContentOverflow() {
        Parameter[] parameters = new Parameter[0];
        return (VariantBool) Automation.getDispatchProperty(this, "contentOverflow", parameters, VariantBool.class);
    }

    public void setShiftLeft(VariantBool param1) {
        Parameter[] parameters = new Parameter[] { param1 };
        Automation.setDispatchProperty(this, "shiftLeft", parameters);
    }

    public VariantBool getShiftLeft() {
        Parameter[] parameters = new Parameter[0];
        return (VariantBool) Automation.getDispatchProperty(this, "shiftLeft", parameters, VariantBool.class);
    }

    public void setAltLeft(VariantBool param1) {
        Parameter[] parameters = new Parameter[] { param1 };
        Automation.setDispatchProperty(this, "altLeft", parameters);
    }

    public VariantBool getAltLeft() {
        Parameter[] parameters = new Parameter[0];
        return (VariantBool) Automation.getDispatchProperty(this, "altLeft", parameters, VariantBool.class);
    }

    public void setCtrlLeft(VariantBool param1) {
        Parameter[] parameters = new Parameter[] { param1 };
        Automation.setDispatchProperty(this, "ctrlLeft", parameters);
    }

    public VariantBool getCtrlLeft() {
        Parameter[] parameters = new Parameter[0];
        return (VariantBool) Automation.getDispatchProperty(this, "ctrlLeft", parameters, VariantBool.class);
    }

    public LongPtr getImeCompositionChange() {
        Parameter[] parameters = new Parameter[0];
        return (LongPtr) Automation.getDispatchProperty(this, "imeCompositionChange", parameters, LongPtr.class);
    }

    public LongPtr getImeNotifyCommand() {
        Parameter[] parameters = new Parameter[0];
        return (LongPtr) Automation.getDispatchProperty(this, "imeNotifyCommand", parameters, LongPtr.class);
    }

    public LongPtr getImeNotifyData() {
        Parameter[] parameters = new Parameter[0];
        return (LongPtr) Automation.getDispatchProperty(this, "imeNotifyData", parameters, LongPtr.class);
    }

    public LongPtr getImeRequest() {
        Parameter[] parameters = new Parameter[0];
        return (LongPtr) Automation.getDispatchProperty(this, "imeRequest", parameters, LongPtr.class);
    }

    public LongPtr getImeRequestData() {
        Parameter[] parameters = new Parameter[0];
        return (LongPtr) Automation.getDispatchProperty(this, "imeRequestData", parameters, LongPtr.class);
    }

    public LongPtr getKeyboardLayout() {
        Parameter[] parameters = new Parameter[0];
        return (LongPtr) Automation.getDispatchProperty(this, "keyboardLayout", parameters, LongPtr.class);
    }

    public Int32 getBehaviorCookie() {
        Parameter[] parameters = new Parameter[0];
        return (Int32) Automation.getDispatchProperty(this, "behaviorCookie", parameters, Int32.class);
    }

    public Int32 getBehaviorPart() {
        Parameter[] parameters = new Parameter[0];
        return (Int32) Automation.getDispatchProperty(this, "behaviorPart", parameters, Int32.class);
    }

    public BStr getNextPage() {
        Parameter[] parameters = new Parameter[0];
        return (BStr) Automation.getDispatchProperty(this, "nextPage", parameters, BStr.class);
    }

    public Int32 getWheelDelta() {
        Parameter[] parameters = new Parameter[0];
        return (Int32) Automation.getDispatchProperty(this, "wheelDelta", parameters, Int32.class);
    }

    public IID getIID() {
        return _iid;
    }

    public Object clone() {
        return new DispCEventObjImpl(this);
    }
}
