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
 * Adapter for server implementation of DispHTMLStyle
 */
public class DispHTMLStyleServer extends IDispatchServer implements DispHTMLStyle {

    public DispHTMLStyleServer(CoClassMetaInfo classImpl) {
        super(classImpl);
        registerMethods(DispHTMLStyle.class);
    }

    public void setFontFamily(BStr param1) {
    }

    public BStr getFontFamily() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setFontStyle(BStr param1) {
    }

    public BStr getFontStyle() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setFontVariant(BStr param1) {
    }

    public BStr getFontVariant() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setFontWeight(BStr param1) {
    }

    public BStr getFontWeight() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setFontSize(Variant param1) {
    }

    public Variant getFontSize() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setFont(BStr param1) {
    }

    public BStr getFont() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setColor(Variant param1) {
    }

    public Variant getColor() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setBackground(BStr param1) {
    }

    public BStr getBackground() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setBackgroundColor(Variant param1) {
    }

    public Variant getBackgroundColor() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setBackgroundImage(BStr param1) {
    }

    public BStr getBackgroundImage() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setBackgroundRepeat(BStr param1) {
    }

    public BStr getBackgroundRepeat() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setBackgroundAttachment(BStr param1) {
    }

    public BStr getBackgroundAttachment() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setBackgroundPosition(BStr param1) {
    }

    public BStr getBackgroundPosition() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setBackgroundPositionX(Variant param1) {
    }

    public Variant getBackgroundPositionX() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setBackgroundPositionY(Variant param1) {
    }

    public Variant getBackgroundPositionY() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setWordSpacing(Variant param1) {
    }

    public Variant getWordSpacing() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setLetterSpacing(Variant param1) {
    }

    public Variant getLetterSpacing() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setTextDecoration(BStr param1) {
    }

    public BStr getTextDecoration() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setTextDecorationNone(VariantBool param1) {
    }

    public VariantBool getTextDecorationNone() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setTextDecorationUnderline(VariantBool param1) {
    }

    public VariantBool getTextDecorationUnderline() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setTextDecorationOverline(VariantBool param1) {
    }

    public VariantBool getTextDecorationOverline() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setTextDecorationLineThrough(VariantBool param1) {
    }

    public VariantBool getTextDecorationLineThrough() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setTextDecorationBlink(VariantBool param1) {
    }

    public VariantBool getTextDecorationBlink() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setVerticalAlign(Variant param1) {
    }

    public Variant getVerticalAlign() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setTextTransform(BStr param1) {
    }

    public BStr getTextTransform() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setTextAlign(BStr param1) {
    }

    public BStr getTextAlign() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setTextIndent(Variant param1) {
    }

    public Variant getTextIndent() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setLineHeight(Variant param1) {
    }

    public Variant getLineHeight() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setMarginTop(Variant param1) {
    }

    public Variant getMarginTop() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setMarginRight(Variant param1) {
    }

    public Variant getMarginRight() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setMarginBottom(Variant param1) {
    }

    public Variant getMarginBottom() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setMarginLeft(Variant param1) {
    }

    public Variant getMarginLeft() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setMargin(BStr param1) {
    }

    public BStr getMargin() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setPaddingTop(Variant param1) {
    }

    public Variant getPaddingTop() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setPaddingRight(Variant param1) {
    }

    public Variant getPaddingRight() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setPaddingBottom(Variant param1) {
    }

    public Variant getPaddingBottom() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setPaddingLeft(Variant param1) {
    }

    public Variant getPaddingLeft() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setPadding(BStr param1) {
    }

    public BStr getPadding() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setBorder(BStr param1) {
    }

    public BStr getBorder() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setBorderTop(BStr param1) {
    }

    public BStr getBorderTop() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setBorderRight(BStr param1) {
    }

    public BStr getBorderRight() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setBorderBottom(BStr param1) {
    }

    public BStr getBorderBottom() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setBorderLeft(BStr param1) {
    }

    public BStr getBorderLeft() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setBorderColor(BStr param1) {
    }

    public BStr getBorderColor() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setBorderTopColor(Variant param1) {
    }

    public Variant getBorderTopColor() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setBorderRightColor(Variant param1) {
    }

    public Variant getBorderRightColor() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setBorderBottomColor(Variant param1) {
    }

    public Variant getBorderBottomColor() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setBorderLeftColor(Variant param1) {
    }

    public Variant getBorderLeftColor() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setBorderWidth(BStr param1) {
    }

    public BStr getBorderWidth() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setBorderTopWidth(Variant param1) {
    }

    public Variant getBorderTopWidth() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setBorderRightWidth(Variant param1) {
    }

    public Variant getBorderRightWidth() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setBorderBottomWidth(Variant param1) {
    }

    public Variant getBorderBottomWidth() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setBorderLeftWidth(Variant param1) {
    }

    public Variant getBorderLeftWidth() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setBorderStyle(BStr param1) {
    }

    public BStr getBorderStyle() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setBorderTopStyle(BStr param1) {
    }

    public BStr getBorderTopStyle() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setBorderRightStyle(BStr param1) {
    }

    public BStr getBorderRightStyle() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setBorderBottomStyle(BStr param1) {
    }

    public BStr getBorderBottomStyle() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setBorderLeftStyle(BStr param1) {
    }

    public BStr getBorderLeftStyle() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setWidth(Variant param1) {
    }

    public Variant getWidth() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setHeight(Variant param1) {
    }

    public Variant getHeight() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setStyleFloat(BStr param1) {
    }

    public BStr getStyleFloat() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setClear(BStr param1) {
    }

    public BStr getClear() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setDisplay(BStr param1) {
    }

    public BStr getDisplay() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setVisibility(BStr param1) {
    }

    public BStr getVisibility() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setListStyleType(BStr param1) {
    }

    public BStr getListStyleType() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setListStylePosition(BStr param1) {
    }

    public BStr getListStylePosition() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setListStyleImage(BStr param1) {
    }

    public BStr getListStyleImage() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setListStyle(BStr param1) {
    }

    public BStr getListStyle() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setWhiteSpace(BStr param1) {
    }

    public BStr getWhiteSpace() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setTop(Variant param1) {
    }

    public Variant getTop() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setLeft(Variant param1) {
    }

    public Variant getLeft() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setZIndex(Variant param1) {
    }

    public Variant getZIndex() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setOverflow(BStr param1) {
    }

    public BStr getOverflow() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setPageBreakBefore(BStr param1) {
    }

    public BStr getPageBreakBefore() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setPageBreakAfter(BStr param1) {
    }

    public BStr getPageBreakAfter() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setCssText(BStr param1) {
    }

    public BStr getCssText() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setPixelTop(Int32 param1) {
    }

    public Int32 getPixelTop() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setPixelLeft(Int32 param1) {
    }

    public Int32 getPixelLeft() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setPixelWidth(Int32 param1) {
    }

    public Int32 getPixelWidth() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setPixelHeight(Int32 param1) {
    }

    public Int32 getPixelHeight() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setPosTop(SingleFloat param1) {
    }

    public SingleFloat getPosTop() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setPosLeft(SingleFloat param1) {
    }

    public SingleFloat getPosLeft() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setPosWidth(SingleFloat param1) {
    }

    public SingleFloat getPosWidth() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setPosHeight(SingleFloat param1) {
    }

    public SingleFloat getPosHeight() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setCursor(BStr param1) {
    }

    public BStr getCursor() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setClip(BStr param1) {
    }

    public BStr getClip() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setFilter(BStr param1) {
    }

    public BStr getFilter() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setAttribute(BStr strAttributeName, Variant AttributeValue, Int32 lFlags) {
    }

    public Variant getAttribute(BStr strAttributeName, Int32 lFlags) {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public VariantBool removeAttribute(BStr strAttributeName, Int32 lFlags) {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public BStr invokeToString() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setTableLayout(BStr param1) {
    }

    public BStr getTableLayout() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setBorderCollapse(BStr param1) {
    }

    public BStr getBorderCollapse() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setDirection(BStr param1) {
    }

    public BStr getDirection() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setBehavior(BStr param1) {
    }

    public BStr getBehavior() {
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

    public void setPosition(BStr param1) {
    }

    public BStr getPosition() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setUnicodeBidi(BStr param1) {
    }

    public BStr getUnicodeBidi() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setBottom(Variant param1) {
    }

    public Variant getBottom() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setRight(Variant param1) {
    }

    public Variant getRight() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setPixelBottom(Int32 param1) {
    }

    public Int32 getPixelBottom() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setPixelRight(Int32 param1) {
    }

    public Int32 getPixelRight() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setPosBottom(SingleFloat param1) {
    }

    public SingleFloat getPosBottom() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setPosRight(SingleFloat param1) {
    }

    public SingleFloat getPosRight() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setImeMode(BStr param1) {
    }

    public BStr getImeMode() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setRubyAlign(BStr param1) {
    }

    public BStr getRubyAlign() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setRubyPosition(BStr param1) {
    }

    public BStr getRubyPosition() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setRubyOverhang(BStr param1) {
    }

    public BStr getRubyOverhang() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setLayoutGridChar(Variant param1) {
    }

    public Variant getLayoutGridChar() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setLayoutGridLine(Variant param1) {
    }

    public Variant getLayoutGridLine() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setLayoutGridMode(BStr param1) {
    }

    public BStr getLayoutGridMode() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setLayoutGridType(BStr param1) {
    }

    public BStr getLayoutGridType() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setLayoutGrid(BStr param1) {
    }

    public BStr getLayoutGrid() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setWordBreak(BStr param1) {
    }

    public BStr getWordBreak() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setLineBreak(BStr param1) {
    }

    public BStr getLineBreak() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setTextJustify(BStr param1) {
    }

    public BStr getTextJustify() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setTextJustifyTrim(BStr param1) {
    }

    public BStr getTextJustifyTrim() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setTextKashida(Variant param1) {
    }

    public Variant getTextKashida() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setTextAutospace(BStr param1) {
    }

    public BStr getTextAutospace() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setOverflowX(BStr param1) {
    }

    public BStr getOverflowX() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setOverflowY(BStr param1) {
    }

    public BStr getOverflowY() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setAccelerator(BStr param1) {
    }

    public BStr getAccelerator() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setLayoutFlow(BStr param1) {
    }

    public BStr getLayoutFlow() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setZoom(Variant param1) {
    }

    public Variant getZoom() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setWordWrap(BStr param1) {
    }

    public BStr getWordWrap() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setTextUnderlinePosition(BStr param1) {
    }

    public BStr getTextUnderlinePosition() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setScrollbarBaseColor(Variant param1) {
    }

    public Variant getScrollbarBaseColor() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setScrollbarFaceColor(Variant param1) {
    }

    public Variant getScrollbarFaceColor() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setScrollbar3dLightColor(Variant param1) {
    }

    public Variant getScrollbar3dLightColor() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setScrollbarShadowColor(Variant param1) {
    }

    public Variant getScrollbarShadowColor() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setScrollbarHighlightColor(Variant param1) {
    }

    public Variant getScrollbarHighlightColor() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setScrollbarDarkShadowColor(Variant param1) {
    }

    public Variant getScrollbarDarkShadowColor() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setScrollbarArrowColor(Variant param1) {
    }

    public Variant getScrollbarArrowColor() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setScrollbarTrackColor(Variant param1) {
    }

    public Variant getScrollbarTrackColor() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setWritingMode(BStr param1) {
    }

    public BStr getWritingMode() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setTextAlignLast(BStr param1) {
    }

    public BStr getTextAlignLast() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setTextKashidaSpace(Variant param1) {
    }

    public Variant getTextKashidaSpace() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setTextOverflow(BStr param1) {
    }

    public BStr getTextOverflow() {
        throw new ComException(HResult.E_NOTIMPL);
    }

    public void setMinHeight(Variant param1) {
    }

    public Variant getMinHeight() {
        throw new ComException(HResult.E_NOTIMPL);
    }
}
