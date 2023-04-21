package watij.mshtml;

import com.jniwrapper.*;
import com.jniwrapper.win32.*;
import com.jniwrapper.win32.automation.*;
import com.jniwrapper.win32.automation.impl.*;
import com.jniwrapper.win32.automation.types.*;
import com.jniwrapper.win32.com.*;
import com.jniwrapper.win32.com.impl.*;
import com.jniwrapper.win32.com.types.*;
import watij.mshtml.impl.*;

/**
 * Represents Java interface for COM interface DispHTMLMapElement.
 */
public interface DispHTMLMapElement extends IDispatch {

    public static final String INTERFACE_IDENTIFIER = "{3050F526-98B5-11CF-BB82-00AA00BDCE0B}";

    static final int DISPID_setAttribute = -2147417611;

    static final int DISPID_getAttribute = -2147417610;

    static final int DISPID_removeAttribute = -2147417609;

    static final int DISPID_setClassName = -2147417111;

    static final int DISPID_getClassName = -2147417111;

    static final int DISPID_setId = -2147417110;

    static final int DISPID_getId = -2147417110;

    static final int DISPID_getTagName = -2147417108;

    static final int DISPID_getParentElement = -2147418104;

    static final int DISPID_getStyle = -2147418038;

    static final int DISPID_setOnhelp = -2147412099;

    static final int DISPID_getOnhelp = -2147412099;

    static final int DISPID_setOnclick = -2147412104;

    static final int DISPID_getOnclick = -2147412104;

    static final int DISPID_setOndblclick = -2147412103;

    static final int DISPID_getOndblclick = -2147412103;

    static final int DISPID_setOnkeydown = -2147412107;

    static final int DISPID_getOnkeydown = -2147412107;

    static final int DISPID_setOnkeyup = -2147412106;

    static final int DISPID_getOnkeyup = -2147412106;

    static final int DISPID_setOnkeypress = -2147412105;

    static final int DISPID_getOnkeypress = -2147412105;

    static final int DISPID_setOnmouseout = -2147412111;

    static final int DISPID_getOnmouseout = -2147412111;

    static final int DISPID_setOnmouseover = -2147412112;

    static final int DISPID_getOnmouseover = -2147412112;

    static final int DISPID_setOnmousemove = -2147412108;

    static final int DISPID_getOnmousemove = -2147412108;

    static final int DISPID_setOnmousedown = -2147412110;

    static final int DISPID_getOnmousedown = -2147412110;

    static final int DISPID_setOnmouseup = -2147412109;

    static final int DISPID_getOnmouseup = -2147412109;

    static final int DISPID_getDocument = -2147417094;

    static final int DISPID_setTitle = -2147418043;

    static final int DISPID_getTitle = -2147418043;

    static final int DISPID_setLanguage = -2147413012;

    static final int DISPID_getLanguage = -2147413012;

    static final int DISPID_setOnselectstart = -2147412075;

    static final int DISPID_getOnselectstart = -2147412075;

    static final int DISPID_scrollIntoView = -2147417093;

    static final int DISPID_contains = -2147417092;

    static final int DISPID_getSourceIndex = -2147417088;

    static final int DISPID_getRecordNumber = -2147417087;

    static final int DISPID_setLang = -2147413103;

    static final int DISPID_getLang = -2147413103;

    static final int DISPID_getOffsetLeft = -2147417104;

    static final int DISPID_getOffsetTop = -2147417103;

    static final int DISPID_getOffsetWidth = -2147417102;

    static final int DISPID_getOffsetHeight = -2147417101;

    static final int DISPID_getOffsetParent = -2147417100;

    static final int DISPID_setInnerHTML = -2147417086;

    static final int DISPID_getInnerHTML = -2147417086;

    static final int DISPID_setInnerText = -2147417085;

    static final int DISPID_getInnerText = -2147417085;

    static final int DISPID_setOuterHTML = -2147417084;

    static final int DISPID_getOuterHTML = -2147417084;

    static final int DISPID_setOuterText = -2147417083;

    static final int DISPID_getOuterText = -2147417083;

    static final int DISPID_insertAdjacentHTML = -2147417082;

    static final int DISPID_insertAdjacentText = -2147417081;

    static final int DISPID_getParentTextEdit = -2147417080;

    static final int DISPID_getIsTextEdit = -2147417078;

    static final int DISPID_click = -2147417079;

    static final int DISPID_getFilters = -2147417077;

    static final int DISPID_setOndragstart = -2147412077;

    static final int DISPID_getOndragstart = -2147412077;

    static final int DISPID_invokeToString = -2147417076;

    static final int DISPID_setOnbeforeupdate = -2147412091;

    static final int DISPID_getOnbeforeupdate = -2147412091;

    static final int DISPID_setOnafterupdate = -2147412090;

    static final int DISPID_getOnafterupdate = -2147412090;

    static final int DISPID_setOnerrorupdate = -2147412074;

    static final int DISPID_getOnerrorupdate = -2147412074;

    static final int DISPID_setOnrowexit = -2147412094;

    static final int DISPID_getOnrowexit = -2147412094;

    static final int DISPID_setOnrowenter = -2147412093;

    static final int DISPID_getOnrowenter = -2147412093;

    static final int DISPID_setOndatasetchanged = -2147412072;

    static final int DISPID_getOndatasetchanged = -2147412072;

    static final int DISPID_setOndataavailable = -2147412071;

    static final int DISPID_getOndataavailable = -2147412071;

    static final int DISPID_setOndatasetcomplete = -2147412070;

    static final int DISPID_getOndatasetcomplete = -2147412070;

    static final int DISPID_setOnfilterchange = -2147412069;

    static final int DISPID_getOnfilterchange = -2147412069;

    static final int DISPID_getChildren = -2147417075;

    static final int DISPID_getAll = -2147417074;

    static final int DISPID_getScopeName = -2147417073;

    static final int DISPID_setCapture = -2147417072;

    static final int DISPID_releaseCapture = -2147417071;

    static final int DISPID_setOnlosecapture = -2147412066;

    static final int DISPID_getOnlosecapture = -2147412066;

    static final int DISPID_componentFromPoint = -2147417070;

    static final int DISPID_doScroll = -2147417069;

    static final int DISPID_setOnscroll = -2147412081;

    static final int DISPID_getOnscroll = -2147412081;

    static final int DISPID_setOndrag = -2147412063;

    static final int DISPID_getOndrag = -2147412063;

    static final int DISPID_setOndragend = -2147412062;

    static final int DISPID_getOndragend = -2147412062;

    static final int DISPID_setOndragenter = -2147412061;

    static final int DISPID_getOndragenter = -2147412061;

    static final int DISPID_setOndragover = -2147412060;

    static final int DISPID_getOndragover = -2147412060;

    static final int DISPID_setOndragleave = -2147412059;

    static final int DISPID_getOndragleave = -2147412059;

    static final int DISPID_setOndrop = -2147412058;

    static final int DISPID_getOndrop = -2147412058;

    static final int DISPID_setOnbeforecut = -2147412054;

    static final int DISPID_getOnbeforecut = -2147412054;

    static final int DISPID_setOncut = -2147412057;

    static final int DISPID_getOncut = -2147412057;

    static final int DISPID_setOnbeforecopy = -2147412053;

    static final int DISPID_getOnbeforecopy = -2147412053;

    static final int DISPID_setOncopy = -2147412056;

    static final int DISPID_getOncopy = -2147412056;

    static final int DISPID_setOnbeforepaste = -2147412052;

    static final int DISPID_getOnbeforepaste = -2147412052;

    static final int DISPID_setOnpaste = -2147412055;

    static final int DISPID_getOnpaste = -2147412055;

    static final int DISPID_getCurrentStyle = -2147417105;

    static final int DISPID_setOnpropertychange = -2147412065;

    static final int DISPID_getOnpropertychange = -2147412065;

    static final int DISPID_getClientRects = -2147417068;

    static final int DISPID_getBoundingClientRect = -2147417067;

    static final int DISPID_setExpression = -2147417608;

    static final int DISPID_getExpression = -2147417607;

    static final int DISPID_removeExpression = -2147417606;

    static final int DISPID_setTabIndex = -2147418097;

    static final int DISPID_getTabIndex = -2147418097;

    static final int DISPID_focus = -2147416112;

    static final int DISPID_setAccessKey = -2147416107;

    static final int DISPID_getAccessKey = -2147416107;

    static final int DISPID_setOnblur = -2147412097;

    static final int DISPID_getOnblur = -2147412097;

    static final int DISPID_setOnfocus = -2147412098;

    static final int DISPID_getOnfocus = -2147412098;

    static final int DISPID_setOnresize = -2147412076;

    static final int DISPID_getOnresize = -2147412076;

    static final int DISPID_blur = -2147416110;

    static final int DISPID_addFilter = -2147416095;

    static final int DISPID_removeFilter = -2147416094;

    static final int DISPID_getClientHeight = -2147416093;

    static final int DISPID_getClientWidth = -2147416092;

    static final int DISPID_getClientTop = -2147416091;

    static final int DISPID_getClientLeft = -2147416090;

    static final int DISPID_attachEvent = -2147417605;

    static final int DISPID_detachEvent = -2147417604;

    static final int DISPID_getReadyState = -2147412996;

    static final int DISPID_setOnreadystatechange = -2147412087;

    static final int DISPID_getOnreadystatechange = -2147412087;

    static final int DISPID_setOnrowsdelete = -2147412050;

    static final int DISPID_getOnrowsdelete = -2147412050;

    static final int DISPID_setOnrowsinserted = -2147412049;

    static final int DISPID_getOnrowsinserted = -2147412049;

    static final int DISPID_setOncellchange = -2147412048;

    static final int DISPID_getOncellchange = -2147412048;

    static final int DISPID_setDir = -2147412995;

    static final int DISPID_getDir = -2147412995;

    static final int DISPID_createControlRange = -2147417056;

    static final int DISPID_getScrollHeight = -2147417055;

    static final int DISPID_getScrollWidth = -2147417054;

    static final int DISPID_setScrollTop = -2147417053;

    static final int DISPID_getScrollTop = -2147417053;

    static final int DISPID_setScrollLeft = -2147417052;

    static final int DISPID_getScrollLeft = -2147417052;

    static final int DISPID_clearAttributes = -2147417050;

    static final int DISPID_setOncontextmenu = -2147412047;

    static final int DISPID_getOncontextmenu = -2147412047;

    static final int DISPID_insertAdjacentElement = -2147417043;

    static final int DISPID_applyElement = -2147417047;

    static final int DISPID_getAdjacentText = -2147417042;

    static final int DISPID_replaceAdjacentText = -2147417041;

    static final int DISPID_getCanHaveChildren = -2147417040;

    static final int DISPID_addBehavior = -2147417032;

    static final int DISPID_removeBehavior = -2147417031;

    static final int DISPID_getRuntimeStyle = -2147417048;

    static final int DISPID_getBehaviorUrns = -2147417030;

    static final int DISPID_setTagUrn = -2147417029;

    static final int DISPID_getTagUrn = -2147417029;

    static final int DISPID_setOnbeforeeditfocus = -2147412043;

    static final int DISPID_getOnbeforeeditfocus = -2147412043;

    static final int DISPID_getReadyStateValue = -2147417028;

    static final int DISPID_getElementsByTagName = -2147417027;

    static final int DISPID_mergeAttributes = -2147417016;

    static final int DISPID_getIsMultiLine = -2147417015;

    static final int DISPID_getCanHaveHTML = -2147417014;

    static final int DISPID_setOnlayoutcomplete = -2147412039;

    static final int DISPID_getOnlayoutcomplete = -2147412039;

    static final int DISPID_setOnpage = -2147412038;

    static final int DISPID_getOnpage = -2147412038;

    static final int DISPID_setInflateBlock = -2147417012;

    static final int DISPID_getInflateBlock = -2147417012;

    static final int DISPID_setOnbeforedeactivate = -2147412035;

    static final int DISPID_getOnbeforedeactivate = -2147412035;

    static final int DISPID_setActive = -2147417011;

    static final int DISPID_setContentEditable = -2147412950;

    static final int DISPID_getContentEditable = -2147412950;

    static final int DISPID_getIsContentEditable = -2147417010;

    static final int DISPID_setHideFocus = -2147412949;

    static final int DISPID_getHideFocus = -2147412949;

    static final int DISPID_setDisabled = -2147418036;

    static final int DISPID_getDisabled = -2147418036;

    static final int DISPID_getIsDisabled = -2147417007;

    static final int DISPID_setOnmove = -2147412034;

    static final int DISPID_getOnmove = -2147412034;

    static final int DISPID_setOncontrolselect = -2147412033;

    static final int DISPID_getOncontrolselect = -2147412033;

    static final int DISPID_fireEvent = -2147417006;

    static final int DISPID_setOnresizestart = -2147412029;

    static final int DISPID_getOnresizestart = -2147412029;

    static final int DISPID_setOnresizeend = -2147412028;

    static final int DISPID_getOnresizeend = -2147412028;

    static final int DISPID_setOnmovestart = -2147412031;

    static final int DISPID_getOnmovestart = -2147412031;

    static final int DISPID_setOnmoveend = -2147412030;

    static final int DISPID_getOnmoveend = -2147412030;

    static final int DISPID_setOnmouseenter = -2147412027;

    static final int DISPID_getOnmouseenter = -2147412027;

    static final int DISPID_setOnmouseleave = -2147412026;

    static final int DISPID_getOnmouseleave = -2147412026;

    static final int DISPID_setOnactivate = -2147412025;

    static final int DISPID_getOnactivate = -2147412025;

    static final int DISPID_setOndeactivate = -2147412024;

    static final int DISPID_getOndeactivate = -2147412024;

    static final int DISPID_dragDrop = -2147417005;

    static final int DISPID_getGlyphMode = -2147417004;

    static final int DISPID_setOnmousewheel = -2147412036;

    static final int DISPID_getOnmousewheel = -2147412036;

    static final int DISPID_normalize = -2147417000;

    static final int DISPID_getAttributeNode = -2147417003;

    static final int DISPID_setAttributeNode = -2147417002;

    static final int DISPID_removeAttributeNode = -2147417001;

    static final int DISPID_setOnbeforeactivate = -2147412022;

    static final int DISPID_getOnbeforeactivate = -2147412022;

    static final int DISPID_setOnfocusin = -2147412021;

    static final int DISPID_getOnfocusin = -2147412021;

    static final int DISPID_setOnfocusout = -2147412020;

    static final int DISPID_getOnfocusout = -2147412020;

    static final int DISPID_getUniqueNumber = -2147417058;

    static final int DISPID_getUniqueID = -2147417057;

    static final int DISPID_getNodeType = -2147417066;

    static final int DISPID_getParentNode = -2147417065;

    static final int DISPID_hasChildNodes = -2147417064;

    static final int DISPID_getChildNodes = -2147417063;

    static final int DISPID_getAttributes = -2147417062;

    static final int DISPID_insertBefore = -2147417061;

    static final int DISPID_removeChild = -2147417060;

    static final int DISPID_replaceChild = -2147417059;

    static final int DISPID_cloneNode = -2147417051;

    static final int DISPID_removeNode = -2147417046;

    static final int DISPID_swapNode = -2147417044;

    static final int DISPID_replaceNode = -2147417045;

    static final int DISPID_appendChild = -2147417039;

    static final int DISPID_getNodeName = -2147417038;

    static final int DISPID_setNodeValue = -2147417037;

    static final int DISPID_getNodeValue = -2147417037;

    static final int DISPID_getFirstChild = -2147417036;

    static final int DISPID_getLastChild = -2147417035;

    static final int DISPID_getPreviousSibling = -2147417034;

    static final int DISPID_getNextSibling = -2147417033;

    static final int DISPID_getOwnerDocument = -2147416999;

    static final int DISPID_getAreas = 1002;

    static final int DISPID_setName = -2147418112;

    static final int DISPID_getName = -2147418112;

    void setAttribute(BStr strAttributeName, Variant AttributeValue, Int32 lFlags);

    Variant getAttribute(BStr strAttributeName, Int32 lFlags);

    VariantBool removeAttribute(BStr strAttributeName, Int32 lFlags);

    void setClassName(BStr param1);

    BStr getClassName();

    void setId(BStr param1);

    BStr getId();

    BStr getTagName();

    IHTMLElement getParentElement();

    IHTMLStyle getStyle();

    void setOnhelp(Variant param1);

    Variant getOnhelp();

    void setOnclick(Variant param1);

    Variant getOnclick();

    void setOndblclick(Variant param1);

    Variant getOndblclick();

    void setOnkeydown(Variant param1);

    Variant getOnkeydown();

    void setOnkeyup(Variant param1);

    Variant getOnkeyup();

    void setOnkeypress(Variant param1);

    Variant getOnkeypress();

    void setOnmouseout(Variant param1);

    Variant getOnmouseout();

    void setOnmouseover(Variant param1);

    Variant getOnmouseover();

    void setOnmousemove(Variant param1);

    Variant getOnmousemove();

    void setOnmousedown(Variant param1);

    Variant getOnmousedown();

    void setOnmouseup(Variant param1);

    Variant getOnmouseup();

    IDispatch getDocument();

    void setTitle(BStr param1);

    BStr getTitle();

    void setLanguage(BStr param1);

    BStr getLanguage();

    void setOnselectstart(Variant param1);

    Variant getOnselectstart();

    void scrollIntoView(Variant varargStart);

    VariantBool contains(IHTMLElement pChild);

    Int32 getSourceIndex();

    Variant getRecordNumber();

    void setLang(BStr param1);

    BStr getLang();

    Int32 getOffsetLeft();

    Int32 getOffsetTop();

    Int32 getOffsetWidth();

    Int32 getOffsetHeight();

    IHTMLElement getOffsetParent();

    void setInnerHTML(BStr param1);

    BStr getInnerHTML();

    void setInnerText(BStr param1);

    BStr getInnerText();

    void setOuterHTML(BStr param1);

    BStr getOuterHTML();

    void setOuterText(BStr param1);

    BStr getOuterText();

    void insertAdjacentHTML(BStr where, BStr html);

    void insertAdjacentText(BStr where, BStr text);

    IHTMLElement getParentTextEdit();

    VariantBool getIsTextEdit();

    void click();

    IHTMLFiltersCollection getFilters();

    void setOndragstart(Variant param1);

    Variant getOndragstart();

    BStr invokeToString();

    void setOnbeforeupdate(Variant param1);

    Variant getOnbeforeupdate();

    void setOnafterupdate(Variant param1);

    Variant getOnafterupdate();

    void setOnerrorupdate(Variant param1);

    Variant getOnerrorupdate();

    void setOnrowexit(Variant param1);

    Variant getOnrowexit();

    void setOnrowenter(Variant param1);

    Variant getOnrowenter();

    void setOndatasetchanged(Variant param1);

    Variant getOndatasetchanged();

    void setOndataavailable(Variant param1);

    Variant getOndataavailable();

    void setOndatasetcomplete(Variant param1);

    Variant getOndatasetcomplete();

    void setOnfilterchange(Variant param1);

    Variant getOnfilterchange();

    IDispatch getChildren();

    IDispatch getAll();

    BStr getScopeName();

    void setCapture(VariantBool containerCapture);

    void releaseCapture();

    void setOnlosecapture(Variant param1);

    Variant getOnlosecapture();

    BStr componentFromPoint(Int32 x, Int32 y);

    void doScroll(Variant component);

    void setOnscroll(Variant param1);

    Variant getOnscroll();

    void setOndrag(Variant param1);

    Variant getOndrag();

    void setOndragend(Variant param1);

    Variant getOndragend();

    void setOndragenter(Variant param1);

    Variant getOndragenter();

    void setOndragover(Variant param1);

    Variant getOndragover();

    void setOndragleave(Variant param1);

    Variant getOndragleave();

    void setOndrop(Variant param1);

    Variant getOndrop();

    void setOnbeforecut(Variant param1);

    Variant getOnbeforecut();

    void setOncut(Variant param1);

    Variant getOncut();

    void setOnbeforecopy(Variant param1);

    Variant getOnbeforecopy();

    void setOncopy(Variant param1);

    Variant getOncopy();

    void setOnbeforepaste(Variant param1);

    Variant getOnbeforepaste();

    void setOnpaste(Variant param1);

    Variant getOnpaste();

    IHTMLCurrentStyle getCurrentStyle();

    void setOnpropertychange(Variant param1);

    Variant getOnpropertychange();

    IHTMLRectCollection getClientRects();

    IHTMLRect getBoundingClientRect();

    void setExpression(BStr propname, BStr expression, BStr language);

    Variant getExpression(BStr propname);

    VariantBool removeExpression(BStr propname);

    void setTabIndex(Int16 param1);

    Int16 getTabIndex();

    void focus();

    void setAccessKey(BStr param1);

    BStr getAccessKey();

    void setOnblur(Variant param1);

    Variant getOnblur();

    void setOnfocus(Variant param1);

    Variant getOnfocus();

    void setOnresize(Variant param1);

    Variant getOnresize();

    void blur();

    void addFilter(IUnknown pUnk);

    void removeFilter(IUnknown pUnk);

    Int32 getClientHeight();

    Int32 getClientWidth();

    Int32 getClientTop();

    Int32 getClientLeft();

    VariantBool attachEvent(BStr event, IDispatch pdisp);

    void detachEvent(BStr event, IDispatch pdisp);

    Variant getReadyState();

    void setOnreadystatechange(Variant param1);

    Variant getOnreadystatechange();

    void setOnrowsdelete(Variant param1);

    Variant getOnrowsdelete();

    void setOnrowsinserted(Variant param1);

    Variant getOnrowsinserted();

    void setOncellchange(Variant param1);

    Variant getOncellchange();

    void setDir(BStr param1);

    BStr getDir();

    IDispatch createControlRange();

    Int32 getScrollHeight();

    Int32 getScrollWidth();

    void setScrollTop(Int32 param1);

    Int32 getScrollTop();

    void setScrollLeft(Int32 param1);

    Int32 getScrollLeft();

    void clearAttributes();

    void setOncontextmenu(Variant param1);

    Variant getOncontextmenu();

    IHTMLElement insertAdjacentElement(BStr where, IHTMLElement insertedElement);

    IHTMLElement applyElement(IHTMLElement apply, BStr where);

    BStr getAdjacentText(BStr where);

    BStr replaceAdjacentText(BStr where, BStr newText);

    VariantBool getCanHaveChildren();

    Int32 addBehavior(BStr bstrUrl, Variant pvarFactory);

    VariantBool removeBehavior(Int32 cookie);

    IHTMLStyle getRuntimeStyle();

    IDispatch getBehaviorUrns();

    void setTagUrn(BStr param1);

    BStr getTagUrn();

    void setOnbeforeeditfocus(Variant param1);

    Variant getOnbeforeeditfocus();

    Int32 getReadyStateValue();

    IHTMLElementCollection getElementsByTagName(BStr v);

    void mergeAttributes(IHTMLElement mergeThis, Variant pvarFlags);

    VariantBool getIsMultiLine();

    VariantBool getCanHaveHTML();

    void setOnlayoutcomplete(Variant param1);

    Variant getOnlayoutcomplete();

    void setOnpage(Variant param1);

    Variant getOnpage();

    void setInflateBlock(VariantBool param1);

    VariantBool getInflateBlock();

    void setOnbeforedeactivate(Variant param1);

    Variant getOnbeforedeactivate();

    void setActive();

    void setContentEditable(BStr param1);

    BStr getContentEditable();

    VariantBool getIsContentEditable();

    void setHideFocus(VariantBool param1);

    VariantBool getHideFocus();

    void setDisabled(VariantBool param1);

    VariantBool getDisabled();

    VariantBool getIsDisabled();

    void setOnmove(Variant param1);

    Variant getOnmove();

    void setOncontrolselect(Variant param1);

    Variant getOncontrolselect();

    VariantBool fireEvent(BStr bstrEventName, Variant pvarEventObject);

    void setOnresizestart(Variant param1);

    Variant getOnresizestart();

    void setOnresizeend(Variant param1);

    Variant getOnresizeend();

    void setOnmovestart(Variant param1);

    Variant getOnmovestart();

    void setOnmoveend(Variant param1);

    Variant getOnmoveend();

    void setOnmouseenter(Variant param1);

    Variant getOnmouseenter();

    void setOnmouseleave(Variant param1);

    Variant getOnmouseleave();

    void setOnactivate(Variant param1);

    Variant getOnactivate();

    void setOndeactivate(Variant param1);

    Variant getOndeactivate();

    VariantBool dragDrop();

    Int32 getGlyphMode();

    void setOnmousewheel(Variant param1);

    Variant getOnmousewheel();

    void normalize();

    IHTMLDOMAttribute getAttributeNode(BStr bstrName);

    IHTMLDOMAttribute setAttributeNode(IHTMLDOMAttribute pattr);

    IHTMLDOMAttribute removeAttributeNode(IHTMLDOMAttribute pattr);

    void setOnbeforeactivate(Variant param1);

    Variant getOnbeforeactivate();

    void setOnfocusin(Variant param1);

    Variant getOnfocusin();

    void setOnfocusout(Variant param1);

    Variant getOnfocusout();

    Int32 getUniqueNumber();

    BStr getUniqueID();

    Int32 getNodeType();

    IHTMLDOMNode getParentNode();

    VariantBool hasChildNodes();

    IDispatch getChildNodes();

    IDispatch getAttributes();

    IHTMLDOMNode insertBefore(IHTMLDOMNode newChild, Variant refChild);

    IHTMLDOMNode removeChild(IHTMLDOMNode oldChild);

    IHTMLDOMNode replaceChild(IHTMLDOMNode newChild, IHTMLDOMNode oldChild);

    IHTMLDOMNode cloneNode(VariantBool fDeep);

    IHTMLDOMNode removeNode(VariantBool fDeep);

    IHTMLDOMNode swapNode(IHTMLDOMNode otherNode);

    IHTMLDOMNode replaceNode(IHTMLDOMNode replacement);

    IHTMLDOMNode appendChild(IHTMLDOMNode newChild);

    BStr getNodeName();

    void setNodeValue(Variant param1);

    Variant getNodeValue();

    IHTMLDOMNode getFirstChild();

    IHTMLDOMNode getLastChild();

    IHTMLDOMNode getPreviousSibling();

    IHTMLDOMNode getNextSibling();

    IDispatch getOwnerDocument();

    IHTMLAreasCollection getAreas();

    void setName(BStr param1);

    BStr getName();
}
