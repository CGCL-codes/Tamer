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
 * Represents COM dispinterface DispHTMLTextAreaElement.
 */
public class DispHTMLTextAreaElementImpl extends IDispatchImpl implements DispHTMLTextAreaElement {

    public static final String INTERFACE_IDENTIFIER = "{3050F521-98B5-11CF-BB82-00AA00BDCE0B}";

    private static final IID _iid = IID.create(INTERFACE_IDENTIFIER);

    public DispHTMLTextAreaElementImpl() {
    }

    protected DispHTMLTextAreaElementImpl(IUnknownImpl that) throws ComException {
        super(that);
    }

    public DispHTMLTextAreaElementImpl(IUnknown that) throws ComException {
        super(that);
    }

    public DispHTMLTextAreaElementImpl(CLSID clsid, ClsCtx dwClsContext) throws ComException {
        super(clsid, dwClsContext);
    }

    public DispHTMLTextAreaElementImpl(CLSID clsid, IUnknownImpl pUnkOuter, ClsCtx dwClsContext) throws ComException {
        super(clsid, pUnkOuter, dwClsContext);
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

    public void setClassName(BStr param1) {
        Parameter[] parameters = new Parameter[] { param1 == null ? (Parameter) PTR_NULL : param1 };
        Automation.setDispatchProperty(this, "className", parameters);
    }

    public BStr getClassName() {
        Parameter[] parameters = new Parameter[0];
        return (BStr) Automation.getDispatchProperty(this, "className", parameters, BStr.class);
    }

    public void setId(BStr param1) {
        Parameter[] parameters = new Parameter[] { param1 == null ? (Parameter) PTR_NULL : param1 };
        Automation.setDispatchProperty(this, "id", parameters);
    }

    public BStr getId() {
        Parameter[] parameters = new Parameter[0];
        return (BStr) Automation.getDispatchProperty(this, "id", parameters, BStr.class);
    }

    public BStr getTagName() {
        Parameter[] parameters = new Parameter[0];
        return (BStr) Automation.getDispatchProperty(this, "tagName", parameters, BStr.class);
    }

    public IHTMLElement getParentElement() {
        Parameter[] parameters = new Parameter[0];
        return (IHTMLElement) Automation.getDispatchProperty(this, "parentElement", parameters, IHTMLElementImpl.class);
    }

    public IHTMLStyle getStyle() {
        Parameter[] parameters = new Parameter[0];
        return (IHTMLStyle) Automation.getDispatchProperty(this, "style", parameters, IHTMLStyleImpl.class);
    }

    public void setOnhelp(Variant param1) {
        Parameter[] parameters = new Parameter[] { param1 };
        Automation.setDispatchProperty(this, "onhelp", parameters);
    }

    public Variant getOnhelp() {
        Parameter[] parameters = new Parameter[0];
        return (Variant) Automation.getDispatchProperty(this, "onhelp", parameters, Variant.class);
    }

    public void setOnclick(Variant param1) {
        Parameter[] parameters = new Parameter[] { param1 };
        Automation.setDispatchProperty(this, "onclick", parameters);
    }

    public Variant getOnclick() {
        Parameter[] parameters = new Parameter[0];
        return (Variant) Automation.getDispatchProperty(this, "onclick", parameters, Variant.class);
    }

    public void setOndblclick(Variant param1) {
        Parameter[] parameters = new Parameter[] { param1 };
        Automation.setDispatchProperty(this, "ondblclick", parameters);
    }

    public Variant getOndblclick() {
        Parameter[] parameters = new Parameter[0];
        return (Variant) Automation.getDispatchProperty(this, "ondblclick", parameters, Variant.class);
    }

    public void setOnkeydown(Variant param1) {
        Parameter[] parameters = new Parameter[] { param1 };
        Automation.setDispatchProperty(this, "onkeydown", parameters);
    }

    public Variant getOnkeydown() {
        Parameter[] parameters = new Parameter[0];
        return (Variant) Automation.getDispatchProperty(this, "onkeydown", parameters, Variant.class);
    }

    public void setOnkeyup(Variant param1) {
        Parameter[] parameters = new Parameter[] { param1 };
        Automation.setDispatchProperty(this, "onkeyup", parameters);
    }

    public Variant getOnkeyup() {
        Parameter[] parameters = new Parameter[0];
        return (Variant) Automation.getDispatchProperty(this, "onkeyup", parameters, Variant.class);
    }

    public void setOnkeypress(Variant param1) {
        Parameter[] parameters = new Parameter[] { param1 };
        Automation.setDispatchProperty(this, "onkeypress", parameters);
    }

    public Variant getOnkeypress() {
        Parameter[] parameters = new Parameter[0];
        return (Variant) Automation.getDispatchProperty(this, "onkeypress", parameters, Variant.class);
    }

    public void setOnmouseout(Variant param1) {
        Parameter[] parameters = new Parameter[] { param1 };
        Automation.setDispatchProperty(this, "onmouseout", parameters);
    }

    public Variant getOnmouseout() {
        Parameter[] parameters = new Parameter[0];
        return (Variant) Automation.getDispatchProperty(this, "onmouseout", parameters, Variant.class);
    }

    public void setOnmouseover(Variant param1) {
        Parameter[] parameters = new Parameter[] { param1 };
        Automation.setDispatchProperty(this, "onmouseover", parameters);
    }

    public Variant getOnmouseover() {
        Parameter[] parameters = new Parameter[0];
        return (Variant) Automation.getDispatchProperty(this, "onmouseover", parameters, Variant.class);
    }

    public void setOnmousemove(Variant param1) {
        Parameter[] parameters = new Parameter[] { param1 };
        Automation.setDispatchProperty(this, "onmousemove", parameters);
    }

    public Variant getOnmousemove() {
        Parameter[] parameters = new Parameter[0];
        return (Variant) Automation.getDispatchProperty(this, "onmousemove", parameters, Variant.class);
    }

    public void setOnmousedown(Variant param1) {
        Parameter[] parameters = new Parameter[] { param1 };
        Automation.setDispatchProperty(this, "onmousedown", parameters);
    }

    public Variant getOnmousedown() {
        Parameter[] parameters = new Parameter[0];
        return (Variant) Automation.getDispatchProperty(this, "onmousedown", parameters, Variant.class);
    }

    public void setOnmouseup(Variant param1) {
        Parameter[] parameters = new Parameter[] { param1 };
        Automation.setDispatchProperty(this, "onmouseup", parameters);
    }

    public Variant getOnmouseup() {
        Parameter[] parameters = new Parameter[0];
        return (Variant) Automation.getDispatchProperty(this, "onmouseup", parameters, Variant.class);
    }

    public IDispatch getDocument() {
        Parameter[] parameters = new Parameter[0];
        return (IDispatch) Automation.getDispatchProperty(this, "document", parameters, IDispatchImpl.class);
    }

    public void setTitle(BStr param1) {
        Parameter[] parameters = new Parameter[] { param1 == null ? (Parameter) PTR_NULL : param1 };
        Automation.setDispatchProperty(this, "title", parameters);
    }

    public BStr getTitle() {
        Parameter[] parameters = new Parameter[0];
        return (BStr) Automation.getDispatchProperty(this, "title", parameters, BStr.class);
    }

    public void setLanguage(BStr param1) {
        Parameter[] parameters = new Parameter[] { param1 == null ? (Parameter) PTR_NULL : param1 };
        Automation.setDispatchProperty(this, "language", parameters);
    }

    public BStr getLanguage() {
        Parameter[] parameters = new Parameter[0];
        return (BStr) Automation.getDispatchProperty(this, "language", parameters, BStr.class);
    }

    public void setOnselectstart(Variant param1) {
        Parameter[] parameters = new Parameter[] { param1 };
        Automation.setDispatchProperty(this, "onselectstart", parameters);
    }

    public Variant getOnselectstart() {
        Parameter[] parameters = new Parameter[0];
        return (Variant) Automation.getDispatchProperty(this, "onselectstart", parameters, Variant.class);
    }

    public void scrollIntoView(Variant varargStart) {
        Parameter[] parameters = new Parameter[] { varargStart };
        Automation.invokeDispatch(this, "scrollIntoView", parameters, void.class);
    }

    public VariantBool contains(IHTMLElement pChild) {
        Parameter[] parameters = new Parameter[] { pChild == null ? (Parameter) PTR_NULL : (Parameter) pChild };
        Object result = Automation.invokeDispatch(this, "contains", parameters, VariantBool.class);
        return (VariantBool) result;
    }

    public Int32 getSourceIndex() {
        Parameter[] parameters = new Parameter[0];
        return (Int32) Automation.getDispatchProperty(this, "sourceIndex", parameters, Int32.class);
    }

    public Variant getRecordNumber() {
        Parameter[] parameters = new Parameter[0];
        return (Variant) Automation.getDispatchProperty(this, "recordNumber", parameters, Variant.class);
    }

    public void setLang(BStr param1) {
        Parameter[] parameters = new Parameter[] { param1 == null ? (Parameter) PTR_NULL : param1 };
        Automation.setDispatchProperty(this, "lang", parameters);
    }

    public BStr getLang() {
        Parameter[] parameters = new Parameter[0];
        return (BStr) Automation.getDispatchProperty(this, "lang", parameters, BStr.class);
    }

    public Int32 getOffsetLeft() {
        Parameter[] parameters = new Parameter[0];
        return (Int32) Automation.getDispatchProperty(this, "offsetLeft", parameters, Int32.class);
    }

    public Int32 getOffsetTop() {
        Parameter[] parameters = new Parameter[0];
        return (Int32) Automation.getDispatchProperty(this, "offsetTop", parameters, Int32.class);
    }

    public Int32 getOffsetWidth() {
        Parameter[] parameters = new Parameter[0];
        return (Int32) Automation.getDispatchProperty(this, "offsetWidth", parameters, Int32.class);
    }

    public Int32 getOffsetHeight() {
        Parameter[] parameters = new Parameter[0];
        return (Int32) Automation.getDispatchProperty(this, "offsetHeight", parameters, Int32.class);
    }

    public IHTMLElement getOffsetParent() {
        Parameter[] parameters = new Parameter[0];
        return (IHTMLElement) Automation.getDispatchProperty(this, "offsetParent", parameters, IHTMLElementImpl.class);
    }

    public void setInnerHTML(BStr param1) {
        Parameter[] parameters = new Parameter[] { param1 == null ? (Parameter) PTR_NULL : param1 };
        Automation.setDispatchProperty(this, "innerHTML", parameters);
    }

    public BStr getInnerHTML() {
        Parameter[] parameters = new Parameter[0];
        return (BStr) Automation.getDispatchProperty(this, "innerHTML", parameters, BStr.class);
    }

    public void setInnerText(BStr param1) {
        Parameter[] parameters = new Parameter[] { param1 == null ? (Parameter) PTR_NULL : param1 };
        Automation.setDispatchProperty(this, "innerText", parameters);
    }

    public BStr getInnerText() {
        Parameter[] parameters = new Parameter[0];
        return (BStr) Automation.getDispatchProperty(this, "innerText", parameters, BStr.class);
    }

    public void setOuterHTML(BStr param1) {
        Parameter[] parameters = new Parameter[] { param1 == null ? (Parameter) PTR_NULL : param1 };
        Automation.setDispatchProperty(this, "outerHTML", parameters);
    }

    public BStr getOuterHTML() {
        Parameter[] parameters = new Parameter[0];
        return (BStr) Automation.getDispatchProperty(this, "outerHTML", parameters, BStr.class);
    }

    public void setOuterText(BStr param1) {
        Parameter[] parameters = new Parameter[] { param1 == null ? (Parameter) PTR_NULL : param1 };
        Automation.setDispatchProperty(this, "outerText", parameters);
    }

    public BStr getOuterText() {
        Parameter[] parameters = new Parameter[0];
        return (BStr) Automation.getDispatchProperty(this, "outerText", parameters, BStr.class);
    }

    public void insertAdjacentHTML(BStr where, BStr html) {
        Parameter[] parameters = new Parameter[] { where == null ? (Parameter) PTR_NULL : where, html == null ? (Parameter) PTR_NULL : html };
        Automation.invokeDispatch(this, "insertAdjacentHTML", parameters, void.class);
    }

    public void insertAdjacentText(BStr where, BStr text) {
        Parameter[] parameters = new Parameter[] { where == null ? (Parameter) PTR_NULL : where, text == null ? (Parameter) PTR_NULL : text };
        Automation.invokeDispatch(this, "insertAdjacentText", parameters, void.class);
    }

    public IHTMLElement getParentTextEdit() {
        Parameter[] parameters = new Parameter[0];
        return (IHTMLElement) Automation.getDispatchProperty(this, "parentTextEdit", parameters, IHTMLElementImpl.class);
    }

    public VariantBool getIsTextEdit() {
        Parameter[] parameters = new Parameter[0];
        return (VariantBool) Automation.getDispatchProperty(this, "isTextEdit", parameters, VariantBool.class);
    }

    public void click() {
        Parameter[] parameters = new Parameter[0];
        Automation.invokeDispatch(this, "click", parameters, void.class);
    }

    public IHTMLFiltersCollection getFilters() {
        Parameter[] parameters = new Parameter[0];
        return (IHTMLFiltersCollection) Automation.getDispatchProperty(this, "filters", parameters, IHTMLFiltersCollectionImpl.class);
    }

    public void setOndragstart(Variant param1) {
        Parameter[] parameters = new Parameter[] { param1 };
        Automation.setDispatchProperty(this, "ondragstart", parameters);
    }

    public Variant getOndragstart() {
        Parameter[] parameters = new Parameter[0];
        return (Variant) Automation.getDispatchProperty(this, "ondragstart", parameters, Variant.class);
    }

    public BStr invokeToString() {
        Parameter[] parameters = new Parameter[0];
        Object result = Automation.invokeDispatch(this, "invokeToString", parameters, BStr.class);
        return (BStr) result;
    }

    public void setOnbeforeupdate(Variant param1) {
        Parameter[] parameters = new Parameter[] { param1 };
        Automation.setDispatchProperty(this, "onbeforeupdate", parameters);
    }

    public Variant getOnbeforeupdate() {
        Parameter[] parameters = new Parameter[0];
        return (Variant) Automation.getDispatchProperty(this, "onbeforeupdate", parameters, Variant.class);
    }

    public void setOnafterupdate(Variant param1) {
        Parameter[] parameters = new Parameter[] { param1 };
        Automation.setDispatchProperty(this, "onafterupdate", parameters);
    }

    public Variant getOnafterupdate() {
        Parameter[] parameters = new Parameter[0];
        return (Variant) Automation.getDispatchProperty(this, "onafterupdate", parameters, Variant.class);
    }

    public void setOnerrorupdate(Variant param1) {
        Parameter[] parameters = new Parameter[] { param1 };
        Automation.setDispatchProperty(this, "onerrorupdate", parameters);
    }

    public Variant getOnerrorupdate() {
        Parameter[] parameters = new Parameter[0];
        return (Variant) Automation.getDispatchProperty(this, "onerrorupdate", parameters, Variant.class);
    }

    public void setOnrowexit(Variant param1) {
        Parameter[] parameters = new Parameter[] { param1 };
        Automation.setDispatchProperty(this, "onrowexit", parameters);
    }

    public Variant getOnrowexit() {
        Parameter[] parameters = new Parameter[0];
        return (Variant) Automation.getDispatchProperty(this, "onrowexit", parameters, Variant.class);
    }

    public void setOnrowenter(Variant param1) {
        Parameter[] parameters = new Parameter[] { param1 };
        Automation.setDispatchProperty(this, "onrowenter", parameters);
    }

    public Variant getOnrowenter() {
        Parameter[] parameters = new Parameter[0];
        return (Variant) Automation.getDispatchProperty(this, "onrowenter", parameters, Variant.class);
    }

    public void setOndatasetchanged(Variant param1) {
        Parameter[] parameters = new Parameter[] { param1 };
        Automation.setDispatchProperty(this, "ondatasetchanged", parameters);
    }

    public Variant getOndatasetchanged() {
        Parameter[] parameters = new Parameter[0];
        return (Variant) Automation.getDispatchProperty(this, "ondatasetchanged", parameters, Variant.class);
    }

    public void setOndataavailable(Variant param1) {
        Parameter[] parameters = new Parameter[] { param1 };
        Automation.setDispatchProperty(this, "ondataavailable", parameters);
    }

    public Variant getOndataavailable() {
        Parameter[] parameters = new Parameter[0];
        return (Variant) Automation.getDispatchProperty(this, "ondataavailable", parameters, Variant.class);
    }

    public void setOndatasetcomplete(Variant param1) {
        Parameter[] parameters = new Parameter[] { param1 };
        Automation.setDispatchProperty(this, "ondatasetcomplete", parameters);
    }

    public Variant getOndatasetcomplete() {
        Parameter[] parameters = new Parameter[0];
        return (Variant) Automation.getDispatchProperty(this, "ondatasetcomplete", parameters, Variant.class);
    }

    public void setOnfilterchange(Variant param1) {
        Parameter[] parameters = new Parameter[] { param1 };
        Automation.setDispatchProperty(this, "onfilterchange", parameters);
    }

    public Variant getOnfilterchange() {
        Parameter[] parameters = new Parameter[0];
        return (Variant) Automation.getDispatchProperty(this, "onfilterchange", parameters, Variant.class);
    }

    public IDispatch getChildren() {
        Parameter[] parameters = new Parameter[0];
        return (IDispatch) Automation.getDispatchProperty(this, "children", parameters, IDispatchImpl.class);
    }

    public IDispatch getAll() {
        Parameter[] parameters = new Parameter[0];
        return (IDispatch) Automation.getDispatchProperty(this, "all", parameters, IDispatchImpl.class);
    }

    public BStr getScopeName() {
        Parameter[] parameters = new Parameter[0];
        return (BStr) Automation.getDispatchProperty(this, "scopeName", parameters, BStr.class);
    }

    public void setCapture(VariantBool containerCapture) {
        Parameter[] parameters = new Parameter[] { containerCapture };
        Automation.invokeDispatch(this, "setCapture", parameters, void.class);
    }

    public void releaseCapture() {
        Parameter[] parameters = new Parameter[0];
        Automation.invokeDispatch(this, "releaseCapture", parameters, void.class);
    }

    public void setOnlosecapture(Variant param1) {
        Parameter[] parameters = new Parameter[] { param1 };
        Automation.setDispatchProperty(this, "onlosecapture", parameters);
    }

    public Variant getOnlosecapture() {
        Parameter[] parameters = new Parameter[0];
        return (Variant) Automation.getDispatchProperty(this, "onlosecapture", parameters, Variant.class);
    }

    public BStr componentFromPoint(Int32 x, Int32 y) {
        Parameter[] parameters = new Parameter[] { x, y };
        Object result = Automation.invokeDispatch(this, "componentFromPoint", parameters, BStr.class);
        return (BStr) result;
    }

    public void doScroll(Variant component) {
        Parameter[] parameters = new Parameter[] { component };
        Automation.invokeDispatch(this, "doScroll", parameters, void.class);
    }

    public void setOnscroll(Variant param1) {
        Parameter[] parameters = new Parameter[] { param1 };
        Automation.setDispatchProperty(this, "onscroll", parameters);
    }

    public Variant getOnscroll() {
        Parameter[] parameters = new Parameter[0];
        return (Variant) Automation.getDispatchProperty(this, "onscroll", parameters, Variant.class);
    }

    public void setOndrag(Variant param1) {
        Parameter[] parameters = new Parameter[] { param1 };
        Automation.setDispatchProperty(this, "ondrag", parameters);
    }

    public Variant getOndrag() {
        Parameter[] parameters = new Parameter[0];
        return (Variant) Automation.getDispatchProperty(this, "ondrag", parameters, Variant.class);
    }

    public void setOndragend(Variant param1) {
        Parameter[] parameters = new Parameter[] { param1 };
        Automation.setDispatchProperty(this, "ondragend", parameters);
    }

    public Variant getOndragend() {
        Parameter[] parameters = new Parameter[0];
        return (Variant) Automation.getDispatchProperty(this, "ondragend", parameters, Variant.class);
    }

    public void setOndragenter(Variant param1) {
        Parameter[] parameters = new Parameter[] { param1 };
        Automation.setDispatchProperty(this, "ondragenter", parameters);
    }

    public Variant getOndragenter() {
        Parameter[] parameters = new Parameter[0];
        return (Variant) Automation.getDispatchProperty(this, "ondragenter", parameters, Variant.class);
    }

    public void setOndragover(Variant param1) {
        Parameter[] parameters = new Parameter[] { param1 };
        Automation.setDispatchProperty(this, "ondragover", parameters);
    }

    public Variant getOndragover() {
        Parameter[] parameters = new Parameter[0];
        return (Variant) Automation.getDispatchProperty(this, "ondragover", parameters, Variant.class);
    }

    public void setOndragleave(Variant param1) {
        Parameter[] parameters = new Parameter[] { param1 };
        Automation.setDispatchProperty(this, "ondragleave", parameters);
    }

    public Variant getOndragleave() {
        Parameter[] parameters = new Parameter[0];
        return (Variant) Automation.getDispatchProperty(this, "ondragleave", parameters, Variant.class);
    }

    public void setOndrop(Variant param1) {
        Parameter[] parameters = new Parameter[] { param1 };
        Automation.setDispatchProperty(this, "ondrop", parameters);
    }

    public Variant getOndrop() {
        Parameter[] parameters = new Parameter[0];
        return (Variant) Automation.getDispatchProperty(this, "ondrop", parameters, Variant.class);
    }

    public void setOnbeforecut(Variant param1) {
        Parameter[] parameters = new Parameter[] { param1 };
        Automation.setDispatchProperty(this, "onbeforecut", parameters);
    }

    public Variant getOnbeforecut() {
        Parameter[] parameters = new Parameter[0];
        return (Variant) Automation.getDispatchProperty(this, "onbeforecut", parameters, Variant.class);
    }

    public void setOncut(Variant param1) {
        Parameter[] parameters = new Parameter[] { param1 };
        Automation.setDispatchProperty(this, "oncut", parameters);
    }

    public Variant getOncut() {
        Parameter[] parameters = new Parameter[0];
        return (Variant) Automation.getDispatchProperty(this, "oncut", parameters, Variant.class);
    }

    public void setOnbeforecopy(Variant param1) {
        Parameter[] parameters = new Parameter[] { param1 };
        Automation.setDispatchProperty(this, "onbeforecopy", parameters);
    }

    public Variant getOnbeforecopy() {
        Parameter[] parameters = new Parameter[0];
        return (Variant) Automation.getDispatchProperty(this, "onbeforecopy", parameters, Variant.class);
    }

    public void setOncopy(Variant param1) {
        Parameter[] parameters = new Parameter[] { param1 };
        Automation.setDispatchProperty(this, "oncopy", parameters);
    }

    public Variant getOncopy() {
        Parameter[] parameters = new Parameter[0];
        return (Variant) Automation.getDispatchProperty(this, "oncopy", parameters, Variant.class);
    }

    public void setOnbeforepaste(Variant param1) {
        Parameter[] parameters = new Parameter[] { param1 };
        Automation.setDispatchProperty(this, "onbeforepaste", parameters);
    }

    public Variant getOnbeforepaste() {
        Parameter[] parameters = new Parameter[0];
        return (Variant) Automation.getDispatchProperty(this, "onbeforepaste", parameters, Variant.class);
    }

    public void setOnpaste(Variant param1) {
        Parameter[] parameters = new Parameter[] { param1 };
        Automation.setDispatchProperty(this, "onpaste", parameters);
    }

    public Variant getOnpaste() {
        Parameter[] parameters = new Parameter[0];
        return (Variant) Automation.getDispatchProperty(this, "onpaste", parameters, Variant.class);
    }

    public IHTMLCurrentStyle getCurrentStyle() {
        Parameter[] parameters = new Parameter[0];
        return (IHTMLCurrentStyle) Automation.getDispatchProperty(this, "currentStyle", parameters, IHTMLCurrentStyleImpl.class);
    }

    public void setOnpropertychange(Variant param1) {
        Parameter[] parameters = new Parameter[] { param1 };
        Automation.setDispatchProperty(this, "onpropertychange", parameters);
    }

    public Variant getOnpropertychange() {
        Parameter[] parameters = new Parameter[0];
        return (Variant) Automation.getDispatchProperty(this, "onpropertychange", parameters, Variant.class);
    }

    public IHTMLRectCollection getClientRects() {
        Parameter[] parameters = new Parameter[0];
        Object result = Automation.invokeDispatch(this, "getClientRects", parameters, IHTMLRectCollectionImpl.class);
        return (IHTMLRectCollection) result;
    }

    public IHTMLRect getBoundingClientRect() {
        Parameter[] parameters = new Parameter[0];
        Object result = Automation.invokeDispatch(this, "getBoundingClientRect", parameters, IHTMLRectImpl.class);
        return (IHTMLRect) result;
    }

    public void setExpression(BStr propname, BStr expression, BStr language) {
        Parameter[] parameters = new Parameter[] { propname == null ? (Parameter) PTR_NULL : propname, expression == null ? (Parameter) PTR_NULL : expression, language == null ? (Parameter) PTR_NULL : language };
        Automation.invokeDispatch(this, "setExpression", parameters, void.class);
    }

    public Variant getExpression(BStr propname) {
        Parameter[] parameters = new Parameter[] { propname == null ? (Parameter) PTR_NULL : propname };
        Object result = Automation.invokeDispatch(this, "getExpression", parameters, Variant.class);
        return (Variant) result;
    }

    public VariantBool removeExpression(BStr propname) {
        Parameter[] parameters = new Parameter[] { propname == null ? (Parameter) PTR_NULL : propname };
        Object result = Automation.invokeDispatch(this, "removeExpression", parameters, VariantBool.class);
        return (VariantBool) result;
    }

    public void setTabIndex(Int16 param1) {
        Parameter[] parameters = new Parameter[] { param1 };
        Automation.setDispatchProperty(this, "tabIndex", parameters);
    }

    public Int16 getTabIndex() {
        Parameter[] parameters = new Parameter[0];
        return (Int16) Automation.getDispatchProperty(this, "tabIndex", parameters, Int16.class);
    }

    public void focus() {
        Parameter[] parameters = new Parameter[0];
        Automation.invokeDispatch(this, "focus", parameters, void.class);
    }

    public void setAccessKey(BStr param1) {
        Parameter[] parameters = new Parameter[] { param1 == null ? (Parameter) PTR_NULL : param1 };
        Automation.setDispatchProperty(this, "accessKey", parameters);
    }

    public BStr getAccessKey() {
        Parameter[] parameters = new Parameter[0];
        return (BStr) Automation.getDispatchProperty(this, "accessKey", parameters, BStr.class);
    }

    public void setOnblur(Variant param1) {
        Parameter[] parameters = new Parameter[] { param1 };
        Automation.setDispatchProperty(this, "onblur", parameters);
    }

    public Variant getOnblur() {
        Parameter[] parameters = new Parameter[0];
        return (Variant) Automation.getDispatchProperty(this, "onblur", parameters, Variant.class);
    }

    public void setOnfocus(Variant param1) {
        Parameter[] parameters = new Parameter[] { param1 };
        Automation.setDispatchProperty(this, "onfocus", parameters);
    }

    public Variant getOnfocus() {
        Parameter[] parameters = new Parameter[0];
        return (Variant) Automation.getDispatchProperty(this, "onfocus", parameters, Variant.class);
    }

    public void setOnresize(Variant param1) {
        Parameter[] parameters = new Parameter[] { param1 };
        Automation.setDispatchProperty(this, "onresize", parameters);
    }

    public Variant getOnresize() {
        Parameter[] parameters = new Parameter[0];
        return (Variant) Automation.getDispatchProperty(this, "onresize", parameters, Variant.class);
    }

    public void blur() {
        Parameter[] parameters = new Parameter[0];
        Automation.invokeDispatch(this, "blur", parameters, void.class);
    }

    public void addFilter(IUnknown pUnk) {
        Parameter[] parameters = new Parameter[] { pUnk == null ? (Parameter) PTR_NULL : (Parameter) pUnk };
        Automation.invokeDispatch(this, "addFilter", parameters, void.class);
    }

    public void removeFilter(IUnknown pUnk) {
        Parameter[] parameters = new Parameter[] { pUnk == null ? (Parameter) PTR_NULL : (Parameter) pUnk };
        Automation.invokeDispatch(this, "removeFilter", parameters, void.class);
    }

    public Int32 getClientHeight() {
        Parameter[] parameters = new Parameter[0];
        return (Int32) Automation.getDispatchProperty(this, "clientHeight", parameters, Int32.class);
    }

    public Int32 getClientWidth() {
        Parameter[] parameters = new Parameter[0];
        return (Int32) Automation.getDispatchProperty(this, "clientWidth", parameters, Int32.class);
    }

    public Int32 getClientTop() {
        Parameter[] parameters = new Parameter[0];
        return (Int32) Automation.getDispatchProperty(this, "clientTop", parameters, Int32.class);
    }

    public Int32 getClientLeft() {
        Parameter[] parameters = new Parameter[0];
        return (Int32) Automation.getDispatchProperty(this, "clientLeft", parameters, Int32.class);
    }

    public VariantBool attachEvent(BStr event, IDispatch pdisp) {
        Parameter[] parameters = new Parameter[] { event == null ? (Parameter) PTR_NULL : event, pdisp == null ? (Parameter) PTR_NULL : (Parameter) pdisp };
        Object result = Automation.invokeDispatch(this, "attachEvent", parameters, VariantBool.class);
        return (VariantBool) result;
    }

    public void detachEvent(BStr event, IDispatch pdisp) {
        Parameter[] parameters = new Parameter[] { event == null ? (Parameter) PTR_NULL : event, pdisp == null ? (Parameter) PTR_NULL : (Parameter) pdisp };
        Automation.invokeDispatch(this, "detachEvent", parameters, void.class);
    }

    public Variant getReadyState() {
        Parameter[] parameters = new Parameter[0];
        return (Variant) Automation.getDispatchProperty(this, "readyState", parameters, Variant.class);
    }

    public void setOnreadystatechange(Variant param1) {
        Parameter[] parameters = new Parameter[] { param1 };
        Automation.setDispatchProperty(this, "onreadystatechange", parameters);
    }

    public Variant getOnreadystatechange() {
        Parameter[] parameters = new Parameter[0];
        return (Variant) Automation.getDispatchProperty(this, "onreadystatechange", parameters, Variant.class);
    }

    public void setOnrowsdelete(Variant param1) {
        Parameter[] parameters = new Parameter[] { param1 };
        Automation.setDispatchProperty(this, "onrowsdelete", parameters);
    }

    public Variant getOnrowsdelete() {
        Parameter[] parameters = new Parameter[0];
        return (Variant) Automation.getDispatchProperty(this, "onrowsdelete", parameters, Variant.class);
    }

    public void setOnrowsinserted(Variant param1) {
        Parameter[] parameters = new Parameter[] { param1 };
        Automation.setDispatchProperty(this, "onrowsinserted", parameters);
    }

    public Variant getOnrowsinserted() {
        Parameter[] parameters = new Parameter[0];
        return (Variant) Automation.getDispatchProperty(this, "onrowsinserted", parameters, Variant.class);
    }

    public void setOncellchange(Variant param1) {
        Parameter[] parameters = new Parameter[] { param1 };
        Automation.setDispatchProperty(this, "oncellchange", parameters);
    }

    public Variant getOncellchange() {
        Parameter[] parameters = new Parameter[0];
        return (Variant) Automation.getDispatchProperty(this, "oncellchange", parameters, Variant.class);
    }

    public void setDir(BStr param1) {
        Parameter[] parameters = new Parameter[] { param1 == null ? (Parameter) PTR_NULL : param1 };
        Automation.setDispatchProperty(this, "dir", parameters);
    }

    public BStr getDir() {
        Parameter[] parameters = new Parameter[0];
        return (BStr) Automation.getDispatchProperty(this, "dir", parameters, BStr.class);
    }

    public IDispatch createControlRange() {
        Parameter[] parameters = new Parameter[0];
        Object result = Automation.invokeDispatch(this, "createControlRange", parameters, IDispatchImpl.class);
        return (IDispatch) result;
    }

    public Int32 getScrollHeight() {
        Parameter[] parameters = new Parameter[0];
        return (Int32) Automation.getDispatchProperty(this, "scrollHeight", parameters, Int32.class);
    }

    public Int32 getScrollWidth() {
        Parameter[] parameters = new Parameter[0];
        return (Int32) Automation.getDispatchProperty(this, "scrollWidth", parameters, Int32.class);
    }

    public void setScrollTop(Int32 param1) {
        Parameter[] parameters = new Parameter[] { param1 };
        Automation.setDispatchProperty(this, "scrollTop", parameters);
    }

    public Int32 getScrollTop() {
        Parameter[] parameters = new Parameter[0];
        return (Int32) Automation.getDispatchProperty(this, "scrollTop", parameters, Int32.class);
    }

    public void setScrollLeft(Int32 param1) {
        Parameter[] parameters = new Parameter[] { param1 };
        Automation.setDispatchProperty(this, "scrollLeft", parameters);
    }

    public Int32 getScrollLeft() {
        Parameter[] parameters = new Parameter[0];
        return (Int32) Automation.getDispatchProperty(this, "scrollLeft", parameters, Int32.class);
    }

    public void clearAttributes() {
        Parameter[] parameters = new Parameter[0];
        Automation.invokeDispatch(this, "clearAttributes", parameters, void.class);
    }

    public void setOncontextmenu(Variant param1) {
        Parameter[] parameters = new Parameter[] { param1 };
        Automation.setDispatchProperty(this, "oncontextmenu", parameters);
    }

    public Variant getOncontextmenu() {
        Parameter[] parameters = new Parameter[0];
        return (Variant) Automation.getDispatchProperty(this, "oncontextmenu", parameters, Variant.class);
    }

    public IHTMLElement insertAdjacentElement(BStr where, IHTMLElement insertedElement) {
        Parameter[] parameters = new Parameter[] { where == null ? (Parameter) PTR_NULL : where, insertedElement == null ? (Parameter) PTR_NULL : (Parameter) insertedElement };
        Object result = Automation.invokeDispatch(this, "insertAdjacentElement", parameters, IHTMLElementImpl.class);
        return (IHTMLElement) result;
    }

    public IHTMLElement applyElement(IHTMLElement apply, BStr where) {
        Parameter[] parameters = new Parameter[] { apply == null ? (Parameter) PTR_NULL : (Parameter) apply, where == null ? (Parameter) PTR_NULL : where };
        Object result = Automation.invokeDispatch(this, "applyElement", parameters, IHTMLElementImpl.class);
        return (IHTMLElement) result;
    }

    public BStr getAdjacentText(BStr where) {
        Parameter[] parameters = new Parameter[] { where == null ? (Parameter) PTR_NULL : where };
        Object result = Automation.invokeDispatch(this, "getAdjacentText", parameters, BStr.class);
        return (BStr) result;
    }

    public BStr replaceAdjacentText(BStr where, BStr newText) {
        Parameter[] parameters = new Parameter[] { where == null ? (Parameter) PTR_NULL : where, newText == null ? (Parameter) PTR_NULL : newText };
        Object result = Automation.invokeDispatch(this, "replaceAdjacentText", parameters, BStr.class);
        return (BStr) result;
    }

    public VariantBool getCanHaveChildren() {
        Parameter[] parameters = new Parameter[0];
        return (VariantBool) Automation.getDispatchProperty(this, "canHaveChildren", parameters, VariantBool.class);
    }

    public Int32 addBehavior(BStr bstrUrl, Variant pvarFactory) {
        Parameter[] parameters = new Parameter[] { bstrUrl == null ? (Parameter) PTR_NULL : bstrUrl, pvarFactory == null ? (Parameter) PTR_NULL : new Pointer(pvarFactory) };
        Object result = Automation.invokeDispatch(this, "addBehavior", parameters, Int32.class);
        return (Int32) result;
    }

    public VariantBool removeBehavior(Int32 cookie) {
        Parameter[] parameters = new Parameter[] { cookie };
        Object result = Automation.invokeDispatch(this, "removeBehavior", parameters, VariantBool.class);
        return (VariantBool) result;
    }

    public IHTMLStyle getRuntimeStyle() {
        Parameter[] parameters = new Parameter[0];
        return (IHTMLStyle) Automation.getDispatchProperty(this, "runtimeStyle", parameters, IHTMLStyleImpl.class);
    }

    public IDispatch getBehaviorUrns() {
        Parameter[] parameters = new Parameter[0];
        return (IDispatch) Automation.getDispatchProperty(this, "behaviorUrns", parameters, IDispatchImpl.class);
    }

    public void setTagUrn(BStr param1) {
        Parameter[] parameters = new Parameter[] { param1 == null ? (Parameter) PTR_NULL : param1 };
        Automation.setDispatchProperty(this, "tagUrn", parameters);
    }

    public BStr getTagUrn() {
        Parameter[] parameters = new Parameter[0];
        return (BStr) Automation.getDispatchProperty(this, "tagUrn", parameters, BStr.class);
    }

    public void setOnbeforeeditfocus(Variant param1) {
        Parameter[] parameters = new Parameter[] { param1 };
        Automation.setDispatchProperty(this, "onbeforeeditfocus", parameters);
    }

    public Variant getOnbeforeeditfocus() {
        Parameter[] parameters = new Parameter[0];
        return (Variant) Automation.getDispatchProperty(this, "onbeforeeditfocus", parameters, Variant.class);
    }

    public Int32 getReadyStateValue() {
        Parameter[] parameters = new Parameter[0];
        return (Int32) Automation.getDispatchProperty(this, "readyStateValue", parameters, Int32.class);
    }

    public IHTMLElementCollection getElementsByTagName(BStr v) {
        Parameter[] parameters = new Parameter[] { v == null ? (Parameter) PTR_NULL : v };
        Object result = Automation.invokeDispatch(this, "getElementsByTagName", parameters, IHTMLElementCollectionImpl.class);
        return (IHTMLElementCollection) result;
    }

    public void mergeAttributes(IHTMLElement mergeThis, Variant pvarFlags) {
        Parameter[] parameters = new Parameter[] { mergeThis == null ? (Parameter) PTR_NULL : (Parameter) mergeThis, pvarFlags == null ? (Parameter) PTR_NULL : new Pointer(pvarFlags) };
        Automation.invokeDispatch(this, "mergeAttributes", parameters, void.class);
    }

    public VariantBool getIsMultiLine() {
        Parameter[] parameters = new Parameter[0];
        return (VariantBool) Automation.getDispatchProperty(this, "isMultiLine", parameters, VariantBool.class);
    }

    public VariantBool getCanHaveHTML() {
        Parameter[] parameters = new Parameter[0];
        return (VariantBool) Automation.getDispatchProperty(this, "canHaveHTML", parameters, VariantBool.class);
    }

    public void setOnlayoutcomplete(Variant param1) {
        Parameter[] parameters = new Parameter[] { param1 };
        Automation.setDispatchProperty(this, "onlayoutcomplete", parameters);
    }

    public Variant getOnlayoutcomplete() {
        Parameter[] parameters = new Parameter[0];
        return (Variant) Automation.getDispatchProperty(this, "onlayoutcomplete", parameters, Variant.class);
    }

    public void setOnpage(Variant param1) {
        Parameter[] parameters = new Parameter[] { param1 };
        Automation.setDispatchProperty(this, "onpage", parameters);
    }

    public Variant getOnpage() {
        Parameter[] parameters = new Parameter[0];
        return (Variant) Automation.getDispatchProperty(this, "onpage", parameters, Variant.class);
    }

    public void setInflateBlock(VariantBool param1) {
        Parameter[] parameters = new Parameter[] { param1 };
        Automation.setDispatchProperty(this, "inflateBlock", parameters);
    }

    public VariantBool getInflateBlock() {
        Parameter[] parameters = new Parameter[0];
        return (VariantBool) Automation.getDispatchProperty(this, "inflateBlock", parameters, VariantBool.class);
    }

    public void setOnbeforedeactivate(Variant param1) {
        Parameter[] parameters = new Parameter[] { param1 };
        Automation.setDispatchProperty(this, "onbeforedeactivate", parameters);
    }

    public Variant getOnbeforedeactivate() {
        Parameter[] parameters = new Parameter[0];
        return (Variant) Automation.getDispatchProperty(this, "onbeforedeactivate", parameters, Variant.class);
    }

    public void setActive() {
        Parameter[] parameters = new Parameter[0];
        Automation.invokeDispatch(this, "setActive", parameters, void.class);
    }

    public void setContentEditable(BStr param1) {
        Parameter[] parameters = new Parameter[] { param1 == null ? (Parameter) PTR_NULL : param1 };
        Automation.setDispatchProperty(this, "contentEditable", parameters);
    }

    public BStr getContentEditable() {
        Parameter[] parameters = new Parameter[0];
        return (BStr) Automation.getDispatchProperty(this, "contentEditable", parameters, BStr.class);
    }

    public VariantBool getIsContentEditable() {
        Parameter[] parameters = new Parameter[0];
        return (VariantBool) Automation.getDispatchProperty(this, "isContentEditable", parameters, VariantBool.class);
    }

    public void setHideFocus(VariantBool param1) {
        Parameter[] parameters = new Parameter[] { param1 };
        Automation.setDispatchProperty(this, "hideFocus", parameters);
    }

    public VariantBool getHideFocus() {
        Parameter[] parameters = new Parameter[0];
        return (VariantBool) Automation.getDispatchProperty(this, "hideFocus", parameters, VariantBool.class);
    }

    public void setDisabled(VariantBool param1) {
        Parameter[] parameters = new Parameter[] { param1 };
        Automation.setDispatchProperty(this, "disabled", parameters);
    }

    public VariantBool getDisabled() {
        Parameter[] parameters = new Parameter[0];
        return (VariantBool) Automation.getDispatchProperty(this, "disabled", parameters, VariantBool.class);
    }

    public VariantBool getIsDisabled() {
        Parameter[] parameters = new Parameter[0];
        return (VariantBool) Automation.getDispatchProperty(this, "isDisabled", parameters, VariantBool.class);
    }

    public void setOnmove(Variant param1) {
        Parameter[] parameters = new Parameter[] { param1 };
        Automation.setDispatchProperty(this, "onmove", parameters);
    }

    public Variant getOnmove() {
        Parameter[] parameters = new Parameter[0];
        return (Variant) Automation.getDispatchProperty(this, "onmove", parameters, Variant.class);
    }

    public void setOncontrolselect(Variant param1) {
        Parameter[] parameters = new Parameter[] { param1 };
        Automation.setDispatchProperty(this, "oncontrolselect", parameters);
    }

    public Variant getOncontrolselect() {
        Parameter[] parameters = new Parameter[0];
        return (Variant) Automation.getDispatchProperty(this, "oncontrolselect", parameters, Variant.class);
    }

    public VariantBool fireEvent(BStr bstrEventName, Variant pvarEventObject) {
        Parameter[] parameters = new Parameter[] { bstrEventName == null ? (Parameter) PTR_NULL : bstrEventName, pvarEventObject == null ? (Parameter) PTR_NULL : new Pointer(pvarEventObject) };
        Object result = Automation.invokeDispatch(this, "fireEvent", parameters, VariantBool.class);
        return (VariantBool) result;
    }

    public void setOnresizestart(Variant param1) {
        Parameter[] parameters = new Parameter[] { param1 };
        Automation.setDispatchProperty(this, "onresizestart", parameters);
    }

    public Variant getOnresizestart() {
        Parameter[] parameters = new Parameter[0];
        return (Variant) Automation.getDispatchProperty(this, "onresizestart", parameters, Variant.class);
    }

    public void setOnresizeend(Variant param1) {
        Parameter[] parameters = new Parameter[] { param1 };
        Automation.setDispatchProperty(this, "onresizeend", parameters);
    }

    public Variant getOnresizeend() {
        Parameter[] parameters = new Parameter[0];
        return (Variant) Automation.getDispatchProperty(this, "onresizeend", parameters, Variant.class);
    }

    public void setOnmovestart(Variant param1) {
        Parameter[] parameters = new Parameter[] { param1 };
        Automation.setDispatchProperty(this, "onmovestart", parameters);
    }

    public Variant getOnmovestart() {
        Parameter[] parameters = new Parameter[0];
        return (Variant) Automation.getDispatchProperty(this, "onmovestart", parameters, Variant.class);
    }

    public void setOnmoveend(Variant param1) {
        Parameter[] parameters = new Parameter[] { param1 };
        Automation.setDispatchProperty(this, "onmoveend", parameters);
    }

    public Variant getOnmoveend() {
        Parameter[] parameters = new Parameter[0];
        return (Variant) Automation.getDispatchProperty(this, "onmoveend", parameters, Variant.class);
    }

    public void setOnmouseenter(Variant param1) {
        Parameter[] parameters = new Parameter[] { param1 };
        Automation.setDispatchProperty(this, "onmouseenter", parameters);
    }

    public Variant getOnmouseenter() {
        Parameter[] parameters = new Parameter[0];
        return (Variant) Automation.getDispatchProperty(this, "onmouseenter", parameters, Variant.class);
    }

    public void setOnmouseleave(Variant param1) {
        Parameter[] parameters = new Parameter[] { param1 };
        Automation.setDispatchProperty(this, "onmouseleave", parameters);
    }

    public Variant getOnmouseleave() {
        Parameter[] parameters = new Parameter[0];
        return (Variant) Automation.getDispatchProperty(this, "onmouseleave", parameters, Variant.class);
    }

    public void setOnactivate(Variant param1) {
        Parameter[] parameters = new Parameter[] { param1 };
        Automation.setDispatchProperty(this, "onactivate", parameters);
    }

    public Variant getOnactivate() {
        Parameter[] parameters = new Parameter[0];
        return (Variant) Automation.getDispatchProperty(this, "onactivate", parameters, Variant.class);
    }

    public void setOndeactivate(Variant param1) {
        Parameter[] parameters = new Parameter[] { param1 };
        Automation.setDispatchProperty(this, "ondeactivate", parameters);
    }

    public Variant getOndeactivate() {
        Parameter[] parameters = new Parameter[0];
        return (Variant) Automation.getDispatchProperty(this, "ondeactivate", parameters, Variant.class);
    }

    public VariantBool dragDrop() {
        Parameter[] parameters = new Parameter[0];
        Object result = Automation.invokeDispatch(this, "dragDrop", parameters, VariantBool.class);
        return (VariantBool) result;
    }

    public Int32 getGlyphMode() {
        Parameter[] parameters = new Parameter[0];
        return (Int32) Automation.getDispatchProperty(this, "glyphMode", parameters, Int32.class);
    }

    public void setOnmousewheel(Variant param1) {
        Parameter[] parameters = new Parameter[] { param1 };
        Automation.setDispatchProperty(this, "onmousewheel", parameters);
    }

    public Variant getOnmousewheel() {
        Parameter[] parameters = new Parameter[0];
        return (Variant) Automation.getDispatchProperty(this, "onmousewheel", parameters, Variant.class);
    }

    public void normalize() {
        Parameter[] parameters = new Parameter[0];
        Automation.invokeDispatch(this, "normalize", parameters, void.class);
    }

    public IHTMLDOMAttribute getAttributeNode(BStr bstrName) {
        Parameter[] parameters = new Parameter[] { bstrName == null ? (Parameter) PTR_NULL : bstrName };
        Object result = Automation.invokeDispatch(this, "getAttributeNode", parameters, IHTMLDOMAttributeImpl.class);
        return (IHTMLDOMAttribute) result;
    }

    public IHTMLDOMAttribute setAttributeNode(IHTMLDOMAttribute pattr) {
        Parameter[] parameters = new Parameter[] { pattr == null ? (Parameter) PTR_NULL : (Parameter) pattr };
        Object result = Automation.invokeDispatch(this, "setAttributeNode", parameters, IHTMLDOMAttributeImpl.class);
        return (IHTMLDOMAttribute) result;
    }

    public IHTMLDOMAttribute removeAttributeNode(IHTMLDOMAttribute pattr) {
        Parameter[] parameters = new Parameter[] { pattr == null ? (Parameter) PTR_NULL : (Parameter) pattr };
        Object result = Automation.invokeDispatch(this, "removeAttributeNode", parameters, IHTMLDOMAttributeImpl.class);
        return (IHTMLDOMAttribute) result;
    }

    public void setOnbeforeactivate(Variant param1) {
        Parameter[] parameters = new Parameter[] { param1 };
        Automation.setDispatchProperty(this, "onbeforeactivate", parameters);
    }

    public Variant getOnbeforeactivate() {
        Parameter[] parameters = new Parameter[0];
        return (Variant) Automation.getDispatchProperty(this, "onbeforeactivate", parameters, Variant.class);
    }

    public void setOnfocusin(Variant param1) {
        Parameter[] parameters = new Parameter[] { param1 };
        Automation.setDispatchProperty(this, "onfocusin", parameters);
    }

    public Variant getOnfocusin() {
        Parameter[] parameters = new Parameter[0];
        return (Variant) Automation.getDispatchProperty(this, "onfocusin", parameters, Variant.class);
    }

    public void setOnfocusout(Variant param1) {
        Parameter[] parameters = new Parameter[] { param1 };
        Automation.setDispatchProperty(this, "onfocusout", parameters);
    }

    public Variant getOnfocusout() {
        Parameter[] parameters = new Parameter[0];
        return (Variant) Automation.getDispatchProperty(this, "onfocusout", parameters, Variant.class);
    }

    public Int32 getUniqueNumber() {
        Parameter[] parameters = new Parameter[0];
        return (Int32) Automation.getDispatchProperty(this, "uniqueNumber", parameters, Int32.class);
    }

    public BStr getUniqueID() {
        Parameter[] parameters = new Parameter[0];
        return (BStr) Automation.getDispatchProperty(this, "uniqueID", parameters, BStr.class);
    }

    public Int32 getNodeType() {
        Parameter[] parameters = new Parameter[0];
        return (Int32) Automation.getDispatchProperty(this, "nodeType", parameters, Int32.class);
    }

    public IHTMLDOMNode getParentNode() {
        Parameter[] parameters = new Parameter[0];
        return (IHTMLDOMNode) Automation.getDispatchProperty(this, "parentNode", parameters, IHTMLDOMNodeImpl.class);
    }

    public VariantBool hasChildNodes() {
        Parameter[] parameters = new Parameter[0];
        Object result = Automation.invokeDispatch(this, "hasChildNodes", parameters, VariantBool.class);
        return (VariantBool) result;
    }

    public IDispatch getChildNodes() {
        Parameter[] parameters = new Parameter[0];
        return (IDispatch) Automation.getDispatchProperty(this, "childNodes", parameters, IDispatchImpl.class);
    }

    public IDispatch getAttributes() {
        Parameter[] parameters = new Parameter[0];
        return (IDispatch) Automation.getDispatchProperty(this, "attributes", parameters, IDispatchImpl.class);
    }

    public IHTMLDOMNode insertBefore(IHTMLDOMNode newChild, Variant refChild) {
        Parameter[] parameters = new Parameter[] { newChild == null ? (Parameter) PTR_NULL : (Parameter) newChild, refChild };
        Object result = Automation.invokeDispatch(this, "insertBefore", parameters, IHTMLDOMNodeImpl.class);
        return (IHTMLDOMNode) result;
    }

    public IHTMLDOMNode removeChild(IHTMLDOMNode oldChild) {
        Parameter[] parameters = new Parameter[] { oldChild == null ? (Parameter) PTR_NULL : (Parameter) oldChild };
        Object result = Automation.invokeDispatch(this, "removeChild", parameters, IHTMLDOMNodeImpl.class);
        return (IHTMLDOMNode) result;
    }

    public IHTMLDOMNode replaceChild(IHTMLDOMNode newChild, IHTMLDOMNode oldChild) {
        Parameter[] parameters = new Parameter[] { newChild == null ? (Parameter) PTR_NULL : (Parameter) newChild, oldChild == null ? (Parameter) PTR_NULL : (Parameter) oldChild };
        Object result = Automation.invokeDispatch(this, "replaceChild", parameters, IHTMLDOMNodeImpl.class);
        return (IHTMLDOMNode) result;
    }

    public IHTMLDOMNode cloneNode(VariantBool fDeep) {
        Parameter[] parameters = new Parameter[] { fDeep };
        Object result = Automation.invokeDispatch(this, "cloneNode", parameters, IHTMLDOMNodeImpl.class);
        return (IHTMLDOMNode) result;
    }

    public IHTMLDOMNode removeNode(VariantBool fDeep) {
        Parameter[] parameters = new Parameter[] { fDeep };
        Object result = Automation.invokeDispatch(this, "removeNode", parameters, IHTMLDOMNodeImpl.class);
        return (IHTMLDOMNode) result;
    }

    public IHTMLDOMNode swapNode(IHTMLDOMNode otherNode) {
        Parameter[] parameters = new Parameter[] { otherNode == null ? (Parameter) PTR_NULL : (Parameter) otherNode };
        Object result = Automation.invokeDispatch(this, "swapNode", parameters, IHTMLDOMNodeImpl.class);
        return (IHTMLDOMNode) result;
    }

    public IHTMLDOMNode replaceNode(IHTMLDOMNode replacement) {
        Parameter[] parameters = new Parameter[] { replacement == null ? (Parameter) PTR_NULL : (Parameter) replacement };
        Object result = Automation.invokeDispatch(this, "replaceNode", parameters, IHTMLDOMNodeImpl.class);
        return (IHTMLDOMNode) result;
    }

    public IHTMLDOMNode appendChild(IHTMLDOMNode newChild) {
        Parameter[] parameters = new Parameter[] { newChild == null ? (Parameter) PTR_NULL : (Parameter) newChild };
        Object result = Automation.invokeDispatch(this, "appendChild", parameters, IHTMLDOMNodeImpl.class);
        return (IHTMLDOMNode) result;
    }

    public BStr getNodeName() {
        Parameter[] parameters = new Parameter[0];
        return (BStr) Automation.getDispatchProperty(this, "nodeName", parameters, BStr.class);
    }

    public void setNodeValue(Variant param1) {
        Parameter[] parameters = new Parameter[] { param1 };
        Automation.setDispatchProperty(this, "nodeValue", parameters);
    }

    public Variant getNodeValue() {
        Parameter[] parameters = new Parameter[0];
        return (Variant) Automation.getDispatchProperty(this, "nodeValue", parameters, Variant.class);
    }

    public IHTMLDOMNode getFirstChild() {
        Parameter[] parameters = new Parameter[0];
        return (IHTMLDOMNode) Automation.getDispatchProperty(this, "firstChild", parameters, IHTMLDOMNodeImpl.class);
    }

    public IHTMLDOMNode getLastChild() {
        Parameter[] parameters = new Parameter[0];
        return (IHTMLDOMNode) Automation.getDispatchProperty(this, "lastChild", parameters, IHTMLDOMNodeImpl.class);
    }

    public IHTMLDOMNode getPreviousSibling() {
        Parameter[] parameters = new Parameter[0];
        return (IHTMLDOMNode) Automation.getDispatchProperty(this, "previousSibling", parameters, IHTMLDOMNodeImpl.class);
    }

    public IHTMLDOMNode getNextSibling() {
        Parameter[] parameters = new Parameter[0];
        return (IHTMLDOMNode) Automation.getDispatchProperty(this, "nextSibling", parameters, IHTMLDOMNodeImpl.class);
    }

    public IDispatch getOwnerDocument() {
        Parameter[] parameters = new Parameter[0];
        return (IDispatch) Automation.getDispatchProperty(this, "ownerDocument", parameters, IDispatchImpl.class);
    }

    public void setDataFld(BStr param1) {
        Parameter[] parameters = new Parameter[] { param1 == null ? (Parameter) PTR_NULL : param1 };
        Automation.setDispatchProperty(this, "dataFld", parameters);
    }

    public BStr getDataFld() {
        Parameter[] parameters = new Parameter[0];
        return (BStr) Automation.getDispatchProperty(this, "dataFld", parameters, BStr.class);
    }

    public void setDataSrc(BStr param1) {
        Parameter[] parameters = new Parameter[] { param1 == null ? (Parameter) PTR_NULL : param1 };
        Automation.setDispatchProperty(this, "dataSrc", parameters);
    }

    public BStr getDataSrc() {
        Parameter[] parameters = new Parameter[0];
        return (BStr) Automation.getDispatchProperty(this, "dataSrc", parameters, BStr.class);
    }

    public void setDataFormatAs(BStr param1) {
        Parameter[] parameters = new Parameter[] { param1 == null ? (Parameter) PTR_NULL : param1 };
        Automation.setDispatchProperty(this, "dataFormatAs", parameters);
    }

    public BStr getDataFormatAs() {
        Parameter[] parameters = new Parameter[0];
        return (BStr) Automation.getDispatchProperty(this, "dataFormatAs", parameters, BStr.class);
    }

    public BStr getType() {
        Parameter[] parameters = new Parameter[0];
        return (BStr) Automation.getDispatchProperty(this, "type", parameters, BStr.class);
    }

    public void setValue(BStr param1) {
        Parameter[] parameters = new Parameter[] { param1 == null ? (Parameter) PTR_NULL : param1 };
        Automation.setDispatchProperty(this, "value", parameters);
    }

    public BStr getValue() {
        Parameter[] parameters = new Parameter[0];
        return (BStr) Automation.getDispatchProperty(this, "value", parameters, BStr.class);
    }

    public void setName(BStr param1) {
        Parameter[] parameters = new Parameter[] { param1 == null ? (Parameter) PTR_NULL : param1 };
        Automation.setDispatchProperty(this, "name", parameters);
    }

    public BStr getName() {
        Parameter[] parameters = new Parameter[0];
        return (BStr) Automation.getDispatchProperty(this, "name", parameters, BStr.class);
    }

    public void setStatus(Variant param1) {
        Parameter[] parameters = new Parameter[] { param1 };
        Automation.setDispatchProperty(this, "status", parameters);
    }

    public Variant getStatus() {
        Parameter[] parameters = new Parameter[0];
        return (Variant) Automation.getDispatchProperty(this, "status", parameters, Variant.class);
    }

    public IHTMLFormElement getForm() {
        Parameter[] parameters = new Parameter[0];
        return (IHTMLFormElement) Automation.getDispatchProperty(this, "form", parameters, IHTMLFormElementImpl.class);
    }

    public void setDefaultValue(BStr param1) {
        Parameter[] parameters = new Parameter[] { param1 == null ? (Parameter) PTR_NULL : param1 };
        Automation.setDispatchProperty(this, "defaultValue", parameters);
    }

    public BStr getDefaultValue() {
        Parameter[] parameters = new Parameter[0];
        return (BStr) Automation.getDispatchProperty(this, "defaultValue", parameters, BStr.class);
    }

    public void select() {
        Parameter[] parameters = new Parameter[0];
        Automation.invokeDispatch(this, "select", parameters, void.class);
    }

    public void setOnchange(Variant param1) {
        Parameter[] parameters = new Parameter[] { param1 };
        Automation.setDispatchProperty(this, "onchange", parameters);
    }

    public Variant getOnchange() {
        Parameter[] parameters = new Parameter[0];
        return (Variant) Automation.getDispatchProperty(this, "onchange", parameters, Variant.class);
    }

    public void setOnselect(Variant param1) {
        Parameter[] parameters = new Parameter[] { param1 };
        Automation.setDispatchProperty(this, "onselect", parameters);
    }

    public Variant getOnselect() {
        Parameter[] parameters = new Parameter[0];
        return (Variant) Automation.getDispatchProperty(this, "onselect", parameters, Variant.class);
    }

    public void setReadOnly(VariantBool param1) {
        Parameter[] parameters = new Parameter[] { param1 };
        Automation.setDispatchProperty(this, "readOnly", parameters);
    }

    public VariantBool getReadOnly() {
        Parameter[] parameters = new Parameter[0];
        return (VariantBool) Automation.getDispatchProperty(this, "readOnly", parameters, VariantBool.class);
    }

    public void setRows(Int32 param1) {
        Parameter[] parameters = new Parameter[] { param1 };
        Automation.setDispatchProperty(this, "rows", parameters);
    }

    public Int32 getRows() {
        Parameter[] parameters = new Parameter[0];
        return (Int32) Automation.getDispatchProperty(this, "rows", parameters, Int32.class);
    }

    public void setCols(Int32 param1) {
        Parameter[] parameters = new Parameter[] { param1 };
        Automation.setDispatchProperty(this, "cols", parameters);
    }

    public Int32 getCols() {
        Parameter[] parameters = new Parameter[0];
        return (Int32) Automation.getDispatchProperty(this, "cols", parameters, Int32.class);
    }

    public void setWrap(BStr param1) {
        Parameter[] parameters = new Parameter[] { param1 == null ? (Parameter) PTR_NULL : param1 };
        Automation.setDispatchProperty(this, "wrap", parameters);
    }

    public BStr getWrap() {
        Parameter[] parameters = new Parameter[0];
        return (BStr) Automation.getDispatchProperty(this, "wrap", parameters, BStr.class);
    }

    public IHTMLTxtRange createTextRange() {
        Parameter[] parameters = new Parameter[0];
        Object result = Automation.invokeDispatch(this, "createTextRange", parameters, IHTMLTxtRangeImpl.class);
        return (IHTMLTxtRange) result;
    }

    public IID getIID() {
        return _iid;
    }

    public Object clone() {
        return new DispHTMLTextAreaElementImpl(this);
    }
}
