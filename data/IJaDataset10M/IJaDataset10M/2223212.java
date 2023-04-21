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
 * Represents COM dispinterface DispIHTMLInputFileElement.
 */
public class DispIHTMLInputFileElementImpl extends IDispatchImpl implements DispIHTMLInputFileElement {

    public static final String INTERFACE_IDENTIFIER = "{3050F542-98B5-11CF-BB82-00AA00BDCE0B}";

    private static final IID _iid = IID.create(INTERFACE_IDENTIFIER);

    public DispIHTMLInputFileElementImpl() {
    }

    protected DispIHTMLInputFileElementImpl(IUnknownImpl that) throws ComException {
        super(that);
    }

    public DispIHTMLInputFileElementImpl(IUnknown that) throws ComException {
        super(that);
    }

    public DispIHTMLInputFileElementImpl(CLSID clsid, ClsCtx dwClsContext) throws ComException {
        super(clsid, dwClsContext);
    }

    public DispIHTMLInputFileElementImpl(CLSID clsid, IUnknownImpl pUnkOuter, ClsCtx dwClsContext) throws ComException {
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

    public BStr getType() {
        Parameter[] parameters = new Parameter[0];
        return (BStr) Automation.getDispatchProperty(this, "type", parameters, BStr.class);
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

    public void setDisabled(VariantBool param1) {
        Parameter[] parameters = new Parameter[] { param1 };
        Automation.setDispatchProperty(this, "disabled", parameters);
    }

    public VariantBool getDisabled() {
        Parameter[] parameters = new Parameter[0];
        return (VariantBool) Automation.getDispatchProperty(this, "disabled", parameters, VariantBool.class);
    }

    public IHTMLFormElement getForm() {
        Parameter[] parameters = new Parameter[0];
        return (IHTMLFormElement) Automation.getDispatchProperty(this, "form", parameters, IHTMLFormElementImpl.class);
    }

    public void setSize(Int32 param1) {
        Parameter[] parameters = new Parameter[] { param1 };
        Automation.setDispatchProperty(this, "size", parameters);
    }

    public Int32 getSize() {
        Parameter[] parameters = new Parameter[0];
        return (Int32) Automation.getDispatchProperty(this, "size", parameters, Int32.class);
    }

    public void setMaxLength(Int32 param1) {
        Parameter[] parameters = new Parameter[] { param1 };
        Automation.setDispatchProperty(this, "maxLength", parameters);
    }

    public Int32 getMaxLength() {
        Parameter[] parameters = new Parameter[0];
        return (Int32) Automation.getDispatchProperty(this, "maxLength", parameters, Int32.class);
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

    public void setValue(BStr param1) {
        Parameter[] parameters = new Parameter[] { param1 == null ? (Parameter) PTR_NULL : param1 };
        Automation.setDispatchProperty(this, "value", parameters);
    }

    public BStr getValue() {
        Parameter[] parameters = new Parameter[0];
        return (BStr) Automation.getDispatchProperty(this, "value", parameters, BStr.class);
    }

    public IID getIID() {
        return _iid;
    }

    public Object clone() {
        return new DispIHTMLInputFileElementImpl(this);
    }
}
