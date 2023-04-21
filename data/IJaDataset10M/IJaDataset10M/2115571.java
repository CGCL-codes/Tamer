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
 * Represents COM interface IHTMLCurrentStyle.
 */
public class IHTMLCurrentStyleImpl extends IDispatchImpl implements IHTMLCurrentStyle {

    public static final String INTERFACE_IDENTIFIER = IHTMLCurrentStyle.INTERFACE_IDENTIFIER;

    private static final IID _iid = IID.create(INTERFACE_IDENTIFIER);

    public IHTMLCurrentStyleImpl() {
    }

    protected IHTMLCurrentStyleImpl(IUnknownImpl that) throws ComException {
        super(that);
    }

    public IHTMLCurrentStyleImpl(IUnknown that) throws ComException {
        super(that);
    }

    public IHTMLCurrentStyleImpl(CLSID clsid, ClsCtx dwClsContext) throws ComException {
        super(clsid, dwClsContext);
    }

    public IHTMLCurrentStyleImpl(CLSID clsid, IUnknownImpl pUnkOuter, ClsCtx dwClsContext) throws ComException {
        super(clsid, pUnkOuter, dwClsContext);
    }

    public BStr getPosition() throws ComException {
        BStr p = new BStr();
        invokeStandardVirtualMethod(7, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public BStr getStyleFloat() throws ComException {
        BStr p = new BStr();
        invokeStandardVirtualMethod(8, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public Variant getColor() throws ComException {
        Variant p = new Variant();
        invokeStandardVirtualMethod(9, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public Variant getBackgroundColor() throws ComException {
        Variant p = new Variant();
        invokeStandardVirtualMethod(10, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public BStr getFontFamily() throws ComException {
        BStr p = new BStr();
        invokeStandardVirtualMethod(11, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public BStr getFontStyle() throws ComException {
        BStr p = new BStr();
        invokeStandardVirtualMethod(12, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public BStr getFontVariant() throws ComException {
        BStr p = new BStr();
        invokeStandardVirtualMethod(13, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public Variant getFontWeight() throws ComException {
        Variant p = new Variant();
        invokeStandardVirtualMethod(14, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public Variant getFontSize() throws ComException {
        Variant p = new Variant();
        invokeStandardVirtualMethod(15, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public BStr getBackgroundImage() throws ComException {
        BStr p = new BStr();
        invokeStandardVirtualMethod(16, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public Variant getBackgroundPositionX() throws ComException {
        Variant p = new Variant();
        invokeStandardVirtualMethod(17, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public Variant getBackgroundPositionY() throws ComException {
        Variant p = new Variant();
        invokeStandardVirtualMethod(18, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public BStr getBackgroundRepeat() throws ComException {
        BStr p = new BStr();
        invokeStandardVirtualMethod(19, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public Variant getBorderLeftColor() throws ComException {
        Variant p = new Variant();
        invokeStandardVirtualMethod(20, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public Variant getBorderTopColor() throws ComException {
        Variant p = new Variant();
        invokeStandardVirtualMethod(21, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public Variant getBorderRightColor() throws ComException {
        Variant p = new Variant();
        invokeStandardVirtualMethod(22, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public Variant getBorderBottomColor() throws ComException {
        Variant p = new Variant();
        invokeStandardVirtualMethod(23, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public BStr getBorderTopStyle() throws ComException {
        BStr p = new BStr();
        invokeStandardVirtualMethod(24, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public BStr getBorderRightStyle() throws ComException {
        BStr p = new BStr();
        invokeStandardVirtualMethod(25, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public BStr getBorderBottomStyle() throws ComException {
        BStr p = new BStr();
        invokeStandardVirtualMethod(26, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public BStr getBorderLeftStyle() throws ComException {
        BStr p = new BStr();
        invokeStandardVirtualMethod(27, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public Variant getBorderTopWidth() throws ComException {
        Variant p = new Variant();
        invokeStandardVirtualMethod(28, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public Variant getBorderRightWidth() throws ComException {
        Variant p = new Variant();
        invokeStandardVirtualMethod(29, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public Variant getBorderBottomWidth() throws ComException {
        Variant p = new Variant();
        invokeStandardVirtualMethod(30, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public Variant getBorderLeftWidth() throws ComException {
        Variant p = new Variant();
        invokeStandardVirtualMethod(31, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public Variant getLeft() throws ComException {
        Variant p = new Variant();
        invokeStandardVirtualMethod(32, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public Variant getTop() throws ComException {
        Variant p = new Variant();
        invokeStandardVirtualMethod(33, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public Variant getWidth() throws ComException {
        Variant p = new Variant();
        invokeStandardVirtualMethod(34, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public Variant getHeight() throws ComException {
        Variant p = new Variant();
        invokeStandardVirtualMethod(35, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public Variant getPaddingLeft() throws ComException {
        Variant p = new Variant();
        invokeStandardVirtualMethod(36, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public Variant getPaddingTop() throws ComException {
        Variant p = new Variant();
        invokeStandardVirtualMethod(37, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public Variant getPaddingRight() throws ComException {
        Variant p = new Variant();
        invokeStandardVirtualMethod(38, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public Variant getPaddingBottom() throws ComException {
        Variant p = new Variant();
        invokeStandardVirtualMethod(39, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public BStr getTextAlign() throws ComException {
        BStr p = new BStr();
        invokeStandardVirtualMethod(40, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public BStr getTextDecoration() throws ComException {
        BStr p = new BStr();
        invokeStandardVirtualMethod(41, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public BStr getDisplay() throws ComException {
        BStr p = new BStr();
        invokeStandardVirtualMethod(42, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public BStr getVisibility() throws ComException {
        BStr p = new BStr();
        invokeStandardVirtualMethod(43, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public Variant getZIndex() throws ComException {
        Variant p = new Variant();
        invokeStandardVirtualMethod(44, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public Variant getLetterSpacing() throws ComException {
        Variant p = new Variant();
        invokeStandardVirtualMethod(45, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public Variant getLineHeight() throws ComException {
        Variant p = new Variant();
        invokeStandardVirtualMethod(46, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public Variant getTextIndent() throws ComException {
        Variant p = new Variant();
        invokeStandardVirtualMethod(47, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public Variant getVerticalAlign() throws ComException {
        Variant p = new Variant();
        invokeStandardVirtualMethod(48, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public BStr getBackgroundAttachment() throws ComException {
        BStr p = new BStr();
        invokeStandardVirtualMethod(49, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public Variant getMarginTop() throws ComException {
        Variant p = new Variant();
        invokeStandardVirtualMethod(50, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public Variant getMarginRight() throws ComException {
        Variant p = new Variant();
        invokeStandardVirtualMethod(51, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public Variant getMarginBottom() throws ComException {
        Variant p = new Variant();
        invokeStandardVirtualMethod(52, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public Variant getMarginLeft() throws ComException {
        Variant p = new Variant();
        invokeStandardVirtualMethod(53, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public BStr getClear() throws ComException {
        BStr p = new BStr();
        invokeStandardVirtualMethod(54, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public BStr getListStyleType() throws ComException {
        BStr p = new BStr();
        invokeStandardVirtualMethod(55, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public BStr getListStylePosition() throws ComException {
        BStr p = new BStr();
        invokeStandardVirtualMethod(56, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public BStr getListStyleImage() throws ComException {
        BStr p = new BStr();
        invokeStandardVirtualMethod(57, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public Variant getClipTop() throws ComException {
        Variant p = new Variant();
        invokeStandardVirtualMethod(58, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public Variant getClipRight() throws ComException {
        Variant p = new Variant();
        invokeStandardVirtualMethod(59, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public Variant getClipBottom() throws ComException {
        Variant p = new Variant();
        invokeStandardVirtualMethod(60, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public Variant getClipLeft() throws ComException {
        Variant p = new Variant();
        invokeStandardVirtualMethod(61, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public BStr getOverflow() throws ComException {
        BStr p = new BStr();
        invokeStandardVirtualMethod(62, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public BStr getPageBreakBefore() throws ComException {
        BStr p = new BStr();
        invokeStandardVirtualMethod(63, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public BStr getPageBreakAfter() throws ComException {
        BStr p = new BStr();
        invokeStandardVirtualMethod(64, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public BStr getCursor() throws ComException {
        BStr p = new BStr();
        invokeStandardVirtualMethod(65, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public BStr getTableLayout() throws ComException {
        BStr p = new BStr();
        invokeStandardVirtualMethod(66, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public BStr getBorderCollapse() throws ComException {
        BStr p = new BStr();
        invokeStandardVirtualMethod(67, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public BStr getDirection() throws ComException {
        BStr p = new BStr();
        invokeStandardVirtualMethod(68, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public BStr getBehavior() throws ComException {
        BStr p = new BStr();
        invokeStandardVirtualMethod(69, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public Variant getAttribute(BStr strAttributeName, Int32 lFlags) throws ComException {
        Variant AttributeValue = new Variant();
        invokeStandardVirtualMethod(70, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { strAttributeName == null ? (Parameter) PTR_NULL : new Const(strAttributeName), lFlags, AttributeValue == null ? (Parameter) PTR_NULL : new Pointer(AttributeValue) });
        return AttributeValue;
    }

    public BStr getUnicodeBidi() throws ComException {
        BStr p = new BStr();
        invokeStandardVirtualMethod(71, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public Variant getRight() throws ComException {
        Variant p = new Variant();
        invokeStandardVirtualMethod(72, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public Variant getBottom() throws ComException {
        Variant p = new Variant();
        invokeStandardVirtualMethod(73, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public BStr getImeMode() throws ComException {
        BStr p = new BStr();
        invokeStandardVirtualMethod(74, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public BStr getRubyAlign() throws ComException {
        BStr p = new BStr();
        invokeStandardVirtualMethod(75, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public BStr getRubyPosition() throws ComException {
        BStr p = new BStr();
        invokeStandardVirtualMethod(76, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public BStr getRubyOverhang() throws ComException {
        BStr p = new BStr();
        invokeStandardVirtualMethod(77, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public BStr getTextAutospace() throws ComException {
        BStr p = new BStr();
        invokeStandardVirtualMethod(78, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public BStr getLineBreak() throws ComException {
        BStr p = new BStr();
        invokeStandardVirtualMethod(79, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public BStr getWordBreak() throws ComException {
        BStr p = new BStr();
        invokeStandardVirtualMethod(80, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public BStr getTextJustify() throws ComException {
        BStr p = new BStr();
        invokeStandardVirtualMethod(81, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public BStr getTextJustifyTrim() throws ComException {
        BStr p = new BStr();
        invokeStandardVirtualMethod(82, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public Variant getTextKashida() throws ComException {
        Variant p = new Variant();
        invokeStandardVirtualMethod(83, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public BStr getBlockDirection() throws ComException {
        BStr p = new BStr();
        invokeStandardVirtualMethod(84, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public Variant getLayoutGridChar() throws ComException {
        Variant p = new Variant();
        invokeStandardVirtualMethod(85, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public Variant getLayoutGridLine() throws ComException {
        Variant p = new Variant();
        invokeStandardVirtualMethod(86, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public BStr getLayoutGridMode() throws ComException {
        BStr p = new BStr();
        invokeStandardVirtualMethod(87, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public BStr getLayoutGridType() throws ComException {
        BStr p = new BStr();
        invokeStandardVirtualMethod(88, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public BStr getBorderStyle() throws ComException {
        BStr p = new BStr();
        invokeStandardVirtualMethod(89, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public BStr getBorderColor() throws ComException {
        BStr p = new BStr();
        invokeStandardVirtualMethod(90, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public BStr getBorderWidth() throws ComException {
        BStr p = new BStr();
        invokeStandardVirtualMethod(91, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public BStr getPadding() throws ComException {
        BStr p = new BStr();
        invokeStandardVirtualMethod(92, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public BStr getMargin() throws ComException {
        BStr p = new BStr();
        invokeStandardVirtualMethod(93, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public BStr getAccelerator() throws ComException {
        BStr p = new BStr();
        invokeStandardVirtualMethod(94, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public BStr getOverflowX() throws ComException {
        BStr p = new BStr();
        invokeStandardVirtualMethod(95, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public BStr getOverflowY() throws ComException {
        BStr p = new BStr();
        invokeStandardVirtualMethod(96, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public BStr getTextTransform() throws ComException {
        BStr p = new BStr();
        invokeStandardVirtualMethod(97, Function.STDCALL_CALLING_CONVENTION, new Parameter[] { p == null ? (Parameter) PTR_NULL : new Pointer(p) });
        return p;
    }

    public IID getIID() {
        return _iid;
    }

    public Object clone() {
        return new IHTMLCurrentStyleImpl(this);
    }
}
