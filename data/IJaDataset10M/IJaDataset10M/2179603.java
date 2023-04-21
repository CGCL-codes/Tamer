package watij.mshtml.server;

import com.jniwrapper.*;
import com.jniwrapper.win32.*;
import com.jniwrapper.win32.automation.*;
import com.jniwrapper.win32.automation.impl.*;
import com.jniwrapper.win32.automation.types.*;
import com.jniwrapper.win32.com.*;
import com.jniwrapper.win32.com.impl.*;
import com.jniwrapper.win32.com.server.*;
import com.jniwrapper.win32.com.types.*;
import watij.mshtml.*;
import watij.mshtml.impl.*;

/**
 * Adapter for server implementation of DispHTMLMetaElement
 */
public class DispHTMLMetaElementServer extends IDispatchServer implements DispHTMLMetaElement {

    public DispHTMLMetaElementServer(CoClassMetaInfo classImpl) {
        super(classImpl);
        registerMethods(DispHTMLMetaElement.class);
    }

    public void setAttribute(BStr strAttributeName, Variant AttributeValue, Int32 lFlags) {
    }

    public Variant getAttribute(BStr strAttributeName, Int32 lFlags) {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public VariantBool removeAttribute(BStr strAttributeName, Int32 lFlags) {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setClassName(BStr param1) {
    }

    public BStr getClassName() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setId(BStr param1) {
    }

    public BStr getId() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public BStr getTagName() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public IHTMLElement getParentElement() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public IHTMLStyle getStyle() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setOnhelp(Variant param1) {
    }

    public Variant getOnhelp() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setOnclick(Variant param1) {
    }

    public Variant getOnclick() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setOndblclick(Variant param1) {
    }

    public Variant getOndblclick() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setOnkeydown(Variant param1) {
    }

    public Variant getOnkeydown() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setOnkeyup(Variant param1) {
    }

    public Variant getOnkeyup() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setOnkeypress(Variant param1) {
    }

    public Variant getOnkeypress() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setOnmouseout(Variant param1) {
    }

    public Variant getOnmouseout() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setOnmouseover(Variant param1) {
    }

    public Variant getOnmouseover() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setOnmousemove(Variant param1) {
    }

    public Variant getOnmousemove() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setOnmousedown(Variant param1) {
    }

    public Variant getOnmousedown() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setOnmouseup(Variant param1) {
    }

    public Variant getOnmouseup() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public IDispatch getDocument() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setTitle(BStr param1) {
    }

    public BStr getTitle() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setLanguage(BStr param1) {
    }

    public BStr getLanguage() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setOnselectstart(Variant param1) {
    }

    public Variant getOnselectstart() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void scrollIntoView(Variant varargStart) {
    }

    public VariantBool contains(IHTMLElement pChild) {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public Int32 getSourceIndex() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public Variant getRecordNumber() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setLang(BStr param1) {
    }

    public BStr getLang() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public Int32 getOffsetLeft() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public Int32 getOffsetTop() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public Int32 getOffsetWidth() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public Int32 getOffsetHeight() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public IHTMLElement getOffsetParent() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setInnerHTML(BStr param1) {
    }

    public BStr getInnerHTML() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setInnerText(BStr param1) {
    }

    public BStr getInnerText() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setOuterHTML(BStr param1) {
    }

    public BStr getOuterHTML() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setOuterText(BStr param1) {
    }

    public BStr getOuterText() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void insertAdjacentHTML(BStr where, BStr html) {
    }

    public void insertAdjacentText(BStr where, BStr text) {
    }

    public IHTMLElement getParentTextEdit() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public VariantBool getIsTextEdit() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void click() {
    }

    public IHTMLFiltersCollection getFilters() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setOndragstart(Variant param1) {
    }

    public Variant getOndragstart() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public BStr invokeToString() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setOnbeforeupdate(Variant param1) {
    }

    public Variant getOnbeforeupdate() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setOnafterupdate(Variant param1) {
    }

    public Variant getOnafterupdate() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setOnerrorupdate(Variant param1) {
    }

    public Variant getOnerrorupdate() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setOnrowexit(Variant param1) {
    }

    public Variant getOnrowexit() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setOnrowenter(Variant param1) {
    }

    public Variant getOnrowenter() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setOndatasetchanged(Variant param1) {
    }

    public Variant getOndatasetchanged() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setOndataavailable(Variant param1) {
    }

    public Variant getOndataavailable() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setOndatasetcomplete(Variant param1) {
    }

    public Variant getOndatasetcomplete() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setOnfilterchange(Variant param1) {
    }

    public Variant getOnfilterchange() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public IDispatch getChildren() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public IDispatch getAll() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public BStr getScopeName() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setCapture(VariantBool containerCapture) {
    }

    public void releaseCapture() {
    }

    public void setOnlosecapture(Variant param1) {
    }

    public Variant getOnlosecapture() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public BStr componentFromPoint(Int32 x, Int32 y) {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void doScroll(Variant component) {
    }

    public void setOnscroll(Variant param1) {
    }

    public Variant getOnscroll() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setOndrag(Variant param1) {
    }

    public Variant getOndrag() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setOndragend(Variant param1) {
    }

    public Variant getOndragend() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setOndragenter(Variant param1) {
    }

    public Variant getOndragenter() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setOndragover(Variant param1) {
    }

    public Variant getOndragover() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setOndragleave(Variant param1) {
    }

    public Variant getOndragleave() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setOndrop(Variant param1) {
    }

    public Variant getOndrop() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setOnbeforecut(Variant param1) {
    }

    public Variant getOnbeforecut() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setOncut(Variant param1) {
    }

    public Variant getOncut() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setOnbeforecopy(Variant param1) {
    }

    public Variant getOnbeforecopy() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setOncopy(Variant param1) {
    }

    public Variant getOncopy() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setOnbeforepaste(Variant param1) {
    }

    public Variant getOnbeforepaste() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setOnpaste(Variant param1) {
    }

    public Variant getOnpaste() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public IHTMLCurrentStyle getCurrentStyle() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setOnpropertychange(Variant param1) {
    }

    public Variant getOnpropertychange() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public IHTMLRectCollection getClientRects() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public IHTMLRect getBoundingClientRect() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setExpression(BStr propname, BStr expression, BStr language) {
    }

    public Variant getExpression(BStr propname) {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public VariantBool removeExpression(BStr propname) {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setTabIndex(Int16 param1) {
    }

    public Int16 getTabIndex() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void focus() {
    }

    public void setAccessKey(BStr param1) {
    }

    public BStr getAccessKey() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setOnblur(Variant param1) {
    }

    public Variant getOnblur() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setOnfocus(Variant param1) {
    }

    public Variant getOnfocus() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setOnresize(Variant param1) {
    }

    public Variant getOnresize() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void blur() {
    }

    public void addFilter(IUnknown pUnk) {
    }

    public void removeFilter(IUnknown pUnk) {
    }

    public Int32 getClientHeight() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public Int32 getClientWidth() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public Int32 getClientTop() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public Int32 getClientLeft() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public VariantBool attachEvent(BStr event, IDispatch pdisp) {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void detachEvent(BStr event, IDispatch pdisp) {
    }

    public Variant getReadyState() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setOnreadystatechange(Variant param1) {
    }

    public Variant getOnreadystatechange() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setOnrowsdelete(Variant param1) {
    }

    public Variant getOnrowsdelete() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setOnrowsinserted(Variant param1) {
    }

    public Variant getOnrowsinserted() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setOncellchange(Variant param1) {
    }

    public Variant getOncellchange() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setDir(BStr param1) {
    }

    public BStr getDir() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public IDispatch createControlRange() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public Int32 getScrollHeight() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public Int32 getScrollWidth() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setScrollTop(Int32 param1) {
    }

    public Int32 getScrollTop() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setScrollLeft(Int32 param1) {
    }

    public Int32 getScrollLeft() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void clearAttributes() {
    }

    public void setOncontextmenu(Variant param1) {
    }

    public Variant getOncontextmenu() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public IHTMLElement insertAdjacentElement(BStr where, IHTMLElement insertedElement) {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public IHTMLElement applyElement(IHTMLElement apply, BStr where) {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public BStr getAdjacentText(BStr where) {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public BStr replaceAdjacentText(BStr where, BStr newText) {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public VariantBool getCanHaveChildren() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public Int32 addBehavior(BStr bstrUrl, Variant pvarFactory) {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public VariantBool removeBehavior(Int32 cookie) {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public IHTMLStyle getRuntimeStyle() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public IDispatch getBehaviorUrns() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setTagUrn(BStr param1) {
    }

    public BStr getTagUrn() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setOnbeforeeditfocus(Variant param1) {
    }

    public Variant getOnbeforeeditfocus() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public Int32 getReadyStateValue() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public IHTMLElementCollection getElementsByTagName(BStr v) {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void mergeAttributes(IHTMLElement mergeThis, Variant pvarFlags) {
    }

    public VariantBool getIsMultiLine() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public VariantBool getCanHaveHTML() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setOnlayoutcomplete(Variant param1) {
    }

    public Variant getOnlayoutcomplete() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setOnpage(Variant param1) {
    }

    public Variant getOnpage() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setInflateBlock(VariantBool param1) {
    }

    public VariantBool getInflateBlock() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setOnbeforedeactivate(Variant param1) {
    }

    public Variant getOnbeforedeactivate() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setActive() {
    }

    public void setContentEditable(BStr param1) {
    }

    public BStr getContentEditable() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public VariantBool getIsContentEditable() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setHideFocus(VariantBool param1) {
    }

    public VariantBool getHideFocus() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setDisabled(VariantBool param1) {
    }

    public VariantBool getDisabled() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public VariantBool getIsDisabled() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setOnmove(Variant param1) {
    }

    public Variant getOnmove() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setOncontrolselect(Variant param1) {
    }

    public Variant getOncontrolselect() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public VariantBool fireEvent(BStr bstrEventName, Variant pvarEventObject) {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setOnresizestart(Variant param1) {
    }

    public Variant getOnresizestart() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setOnresizeend(Variant param1) {
    }

    public Variant getOnresizeend() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setOnmovestart(Variant param1) {
    }

    public Variant getOnmovestart() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setOnmoveend(Variant param1) {
    }

    public Variant getOnmoveend() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setOnmouseenter(Variant param1) {
    }

    public Variant getOnmouseenter() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setOnmouseleave(Variant param1) {
    }

    public Variant getOnmouseleave() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setOnactivate(Variant param1) {
    }

    public Variant getOnactivate() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setOndeactivate(Variant param1) {
    }

    public Variant getOndeactivate() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public VariantBool dragDrop() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public Int32 getGlyphMode() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setOnmousewheel(Variant param1) {
    }

    public Variant getOnmousewheel() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void normalize() {
    }

    public IHTMLDOMAttribute getAttributeNode(BStr bstrName) {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public IHTMLDOMAttribute setAttributeNode(IHTMLDOMAttribute pattr) {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public IHTMLDOMAttribute removeAttributeNode(IHTMLDOMAttribute pattr) {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setOnbeforeactivate(Variant param1) {
    }

    public Variant getOnbeforeactivate() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setOnfocusin(Variant param1) {
    }

    public Variant getOnfocusin() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setOnfocusout(Variant param1) {
    }

    public Variant getOnfocusout() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public Int32 getUniqueNumber() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public BStr getUniqueID() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public Int32 getNodeType() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public IHTMLDOMNode getParentNode() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public VariantBool hasChildNodes() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public IDispatch getChildNodes() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public IDispatch getAttributes() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public IHTMLDOMNode insertBefore(IHTMLDOMNode newChild, Variant refChild) {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public IHTMLDOMNode removeChild(IHTMLDOMNode oldChild) {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public IHTMLDOMNode replaceChild(IHTMLDOMNode newChild, IHTMLDOMNode oldChild) {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public IHTMLDOMNode cloneNode(VariantBool fDeep) {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public IHTMLDOMNode removeNode(VariantBool fDeep) {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public IHTMLDOMNode swapNode(IHTMLDOMNode otherNode) {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public IHTMLDOMNode replaceNode(IHTMLDOMNode replacement) {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public IHTMLDOMNode appendChild(IHTMLDOMNode newChild) {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public BStr getNodeName() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setNodeValue(Variant param1) {
    }

    public Variant getNodeValue() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public IHTMLDOMNode getFirstChild() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public IHTMLDOMNode getLastChild() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public IHTMLDOMNode getPreviousSibling() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public IHTMLDOMNode getNextSibling() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public IDispatch getOwnerDocument() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setHttpEquiv(BStr param1) {
    }

    public BStr getHttpEquiv() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setContent(BStr param1) {
    }

    public BStr getContent() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setName(BStr param1) {
    }

    public BStr getName() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setUrl(BStr param1) {
    }

    public BStr getUrl() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setCharset(BStr param1) {
    }

    public BStr getCharset() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setScheme(BStr param1) {
    }

    public BStr getScheme() {
        throw new ComException(HResult.E_NOTIMPL);
    }
}
