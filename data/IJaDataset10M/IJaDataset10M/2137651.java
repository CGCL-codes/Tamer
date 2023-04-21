package com.bluebrim.text.impl.shared;

import java.awt.*;
import java.awt.geom.*;
import javax.swing.text.*;
import com.bluebrim.base.shared.*;
import com.bluebrim.font.shared.*;
import com.bluebrim.paint.impl.shared.*;
import com.bluebrim.paint.shared.*;
import com.bluebrim.text.shared.*;

/**
 * Utility class for read only access to text attributes in an attribute set
 * The resolver path is traversed and if not value is found a default value is returned.
 *
 * Also see CoStyleConstants.
 *
 * @author: Dennis Malmstr�m
 */
public class CoViewStyleConstants implements CoTextConstants {

    private static AffineTransform m_transform = new AffineTransform();

    private static final float m_defaultRegularTabStopInterval = (float) CoLengthUnit.CM.from(2);

    private static final CoLineBreakerIF m_defaultLineBreaker = CoLiangLineBreaker.create();

    private static CoColorIF m_defaultForegroundColor;

    private static CoColorIF m_defaultShadeColor;

    private static boolean m_dontEvenThinkAboutRenderingType1Fonts = true;

    static {
        try {
            Class.forName("CoDontEvenThinkAboutRenderingType1Fonts");
        } catch (ClassNotFoundException ex) {
            m_dontEvenThinkAboutRenderingType1Fonts = false;
        }
        if (m_dontEvenThinkAboutRenderingType1Fonts) {
            System.err.println("Type1 font rendering disabled, all text will be rendered using Arial.");
        }
    }

    public static boolean getAdjustToBaseLineGrid(AttributeSet attributeSet) {
        Boolean state = (Boolean) attributeSet.getAttribute(ADJUST_TO_BASE_LINE_GRID);
        return (state != null) ? state.booleanValue() : getDefaultAdjustToBaseLineGrid();
    }

    /**
	 * Gets the alignment setting.
	 *
	 * @param a the attribute set
	 * @returns the value
	 */
    public static CoEnumValue getAlignment(AttributeSet a) {
        CoEnumValue align = (CoEnumValue) a.getAttribute(ALIGNMENT);
        if (align != null) return align; else return getDefaultAlignment();
    }

    public static float getBaselineOffset(AttributeSet attributeSet) {
        Float blof = (Float) attributeSet.getAttribute(BASELINE_OFFSET);
        return (blof != null) ? blof.floatValue() : getDefaultBaselineOffset();
    }

    public static CoEnumValue getBottomRulerAlignment(AttributeSet a) {
        CoEnumValue align = (CoEnumValue) a.getAttribute(BOTTOM_RULER_ALIGNMENT);
        if (align != null) return align; else return getDefaultRulerAlignment();
    }

    public static float getBottomRulerFixedWidth(AttributeSet a) {
        Float f = (Float) a.getAttribute(BOTTOM_RULER_FIXED_WIDTH);
        if (f != null) {
            return f.floatValue();
        }
        return getDefaultRulerFixedWidth();
    }

    public static float getBottomRulerleftInset(AttributeSet a) {
        Float f = (Float) a.getAttribute(BOTTOM_RULER_LEFT_INSET);
        if (f != null) {
            return f.floatValue();
        }
        return getDefaultRulerleftInset();
    }

    public static float getBottomRulerPosition(AttributeSet a) {
        Float f = (Float) a.getAttribute(BOTTOM_RULER_POSITION);
        if (f != null) {
            return f.floatValue();
        }
        return getDefaultRulerPosition();
    }

    public static float getBottomRulerRightInset(AttributeSet a) {
        Float f = (Float) a.getAttribute(BOTTOM_RULER_RIGHT_INSET);
        if (f != null) {
            return f.floatValue();
        }
        return getDefaultRulerRightInset();
    }

    public static CoEnumValue getBottomRulerSpan(AttributeSet a) {
        CoEnumValue align = (CoEnumValue) a.getAttribute(BOTTOM_RULER_SPAN);
        if (align != null) return align; else return getDefaultRulerSpan();
    }

    public static float getBottomRulerThickness(AttributeSet a) {
        Float f = (Float) a.getAttribute(BOTTOM_RULER_THICKNESS);
        if (f != null) {
            return f.floatValue();
        }
        return getDefaultRulerThickness();
    }

    public static CoFont getCoFont(AttributeSet attr) {
        return getCoFont(attr, 1);
    }

    public static CoFont getCoFont(AttributeSet attr, float scale) {
        String family = getFontFamily(attr);
        CoFontAttribute weight = getWeight(attr);
        CoFontAttribute style = getStyle(attr);
        CoFontAttribute variant = CoFontAttribute.NORMAL_VARIANT;
        CoFontAttribute stretch = CoFontAttribute.NORMAL_STRETCH;
        float size = getFontSize(attr) * scale;
        if (isSuperior(attr)) size /= 2.0;
        float sx = getHorizontalScale(attr) / 100.0f;
        float sy = getVerticalScale(attr) / 100.0f;
        CoFontFace face = CoAbstractFontMapper.getFontMapper().getFontFaceOrFallback(new CoFontFaceSpec(family, weight, style, variant, stretch));
        CoFont f = face.getFont(size, sx, sy);
        return f;
    }

    public static boolean getDefaultAdjustToBaseLineGrid() {
        return false;
    }

    /**
	 * Gets the alignment setting.
	 *
	 * @param a the attribute set
	 * @returns the value
	 */
    public static CoEnumValue getDefaultAlignment() {
        return ALIGN_LEFT;
    }

    public static float getDefaultBaselineOffset() {
        return 0.0f;
    }

    public static int getDefaultDropCapsCharacterCount() {
        return 1;
    }

    public static int getDefaultDropCapsLineCount() {
        return 2;
    }

    public static float getDefaultFirstLineIndent() {
        return 0;
    }

    public static String getDefaultFontFamily() {
        return (String) CoAbstractFontMapper.getFontMapper().getFallbackFontFamily();
    }

    public static float getDefaultFontSize() {
        return 12.0f;
    }

    public static CoColorIF getDefaultForegroundColor() {
        if (m_defaultForegroundColor == null) {
            m_defaultForegroundColor = (CoColorIF) CoFactoryManager.createObject(CoProcessBlackIF.PROCESS_BLACK);
        }
        return m_defaultForegroundColor;
    }

    public static String getDefaultForegroundColorName() {
        return getDefaultForegroundColor().getName();
    }

    public static float getDefaultForegroundShade() {
        return 100.0f;
    }

    public static float getDefaultHorizontalScale() {
        return 100;
    }

    public static String getDefaultHyphenation() {
        return null;
    }

    public static float getDefaultLeftIndent() {
        return 0;
    }

    public static CoLineBreakerIF getDefaultLineBreaker() {
        return m_defaultLineBreaker;
    }

    public static float getDefaultMinimumSpaceWidth() {
        return 50f;
    }

    public static float getDefaultOptimumSpaceWidth() {
        return 80f;
    }

    public static float getDefaultRegularTabStopInterval() {
        return m_defaultRegularTabStopInterval;
    }

    public static float getDefaultRightIndent() {
        return 0;
    }

    public static CoEnumValue getDefaultRulerAlignment() {
        return ALIGN_CENTER;
    }

    public static float getDefaultRulerFixedWidth() {
        return 50;
    }

    public static float getDefaultRulerleftInset() {
        return 0;
    }

    public static float getDefaultRulerPosition() {
        return 50;
    }

    public static float getDefaultRulerRightInset() {
        return 0;
    }

    public static CoEnumValue getDefaultRulerSpan() {
        return RULER_SPAN_TEXT;
    }

    public static float getDefaultRulerThickness() {
        return 0;
    }

    public static float getDefaultShadowAngle() {
        return 45;
    }

    public static CoColorIF getDefaultShadowColor() {
        if (m_defaultShadeColor == null) {
            m_defaultShadeColor = (CoColorIF) CoFactoryManager.createObject(CoProcessBlackIF.PROCESS_BLACK);
        }
        return m_defaultShadeColor;
    }

    public static String getDefaultShadowColorName() {
        return getDefaultShadowColor().getName();
    }

    public static float getDefaultShadowOffset() {
        return Float.NaN;
    }

    public static float getDefaultShadowShade() {
        return 30.0f;
    }

    public static float getDefaultSpaceAbove() {
        return 0;
    }

    public static float getDefaultSpaceBelow() {
        return 0;
    }

    public static CoFontAttribute getDefaultStretch() {
        return CoFontAttribute.NORMAL_STRETCH;
    }

    public static CoFontAttribute getDefaultStyle() {
        return CoFontAttribute.NORMAL_STYLE;
    }

    public static float getDefaultTrackAmount() {
        return 0.0f;
    }

    public static float getDefaultTrailingLinesIndent() {
        return 0;
    }

    public static CoEnumValue getDefaultUnderline() {
        return UNDERLINE_NONE;
    }

    public static CoFontAttribute getDefaultVariant() {
        return CoFontAttribute.NORMAL_VARIANT;
    }

    public static CoFontAttribute getDefaultWeight() {
        return CoFontAttribute.NORMAL_WEIGHT;
    }

    public static CoEnumValue getDefaultVerticalPosition() {
        return VERTICAL_POSITION_NONE;
    }

    public static float getDefaultVerticalScale() {
        return 100;
    }

    public static int getDropCapsCharacterCount(AttributeSet attributeSet) {
        Integer count = (Integer) attributeSet.getAttribute(DROP_CAPS_COUNT);
        return (count != null) ? count.intValue() : getDefaultDropCapsCharacterCount();
    }

    public static int getDropCapsLineCount(AttributeSet attributeSet) {
        Integer count = (Integer) attributeSet.getAttribute(DROP_CAPS_HEIGHT);
        return (count != null) ? count.intValue() : getDefaultDropCapsLineCount();
    }

    public static float getFirstLineIndent(AttributeSet a) {
        Float indent = (Float) a.getAttribute(FIRST_LINE_INDENT);
        if (indent != null) {
            return indent.floatValue();
        }
        return getDefaultFirstLineIndent();
    }

    public static Font getFont(AttributeSet attr) {
        return getFont(attr, 1);
    }

    public static Font getFont(AttributeSet attr, float scale) {
        return getCoFont(attr, scale).getAwtFont();
    }

    public static String getFontFamily(AttributeSet a) {
        String family = (String) a.getAttribute(FONT_FAMILY);
        if (m_dontEvenThinkAboutRenderingType1Fonts) {
            family = "Arial";
        }
        if (family == null) {
            family = getDefaultFontFamily();
        }
        return family;
    }

    public static float getFontSize(AttributeSet attributeSet) {
        Float size = (Float) attributeSet.getAttribute(FONT_SIZE);
        return (size != null) ? size.floatValue() : getDefaultFontSize();
    }

    public static Paint getForegroundColor(AttributeSet a, CoStyledDocumentIF doc) {
        CoColorIF color = doc.getColor(getForegroundColorName(a));
        if (color == null) color = getDefaultForegroundColor();
        float shade = getForegroundShade(a);
        Color fg = color.getShadedPreviewColor(shade);
        return fg;
    }

    public static String getForegroundColorName(AttributeSet a) {
        String colorName = (String) a.getAttribute(FOREGROUND_COLOR);
        if (colorName == null) {
            colorName = getDefaultForegroundColorName();
        }
        return colorName;
    }

    public static float getForegroundShade(AttributeSet a) {
        Float s = (Float) a.getAttribute(FOREGROUND_SHADE);
        if (s == null) {
            return getDefaultForegroundShade();
        }
        return s.floatValue();
    }

    public static float getHorizontalScale(AttributeSet a) {
        Float s = (Float) a.getAttribute(HORIZONTAL_SCALE);
        if (s == null) {
            return getDefaultHorizontalScale();
        }
        return s.floatValue();
    }

    public static String getHyphenation(AttributeSet a) {
        String tmp = (String) a.getAttribute(HYPHENATION);
        if (tmp != null) return tmp; else return getDefaultHyphenation();
    }

    /**
	 * Gets the alignment setting.
	 *
	 * @param a the attribute set
	 * @returns the value
	 */
    public static CoEnumValue getHyphenationFallbackBehavior(AttributeSet a) {
        return (CoEnumValue) a.getAttribute(HYPHENATION_FALLBACK_BEHAVIOR);
    }

    public static float getLeftIndent(AttributeSet a) {
        Float indent = (Float) a.getAttribute(LEFT_INDENT);
        if (indent != null) {
            return indent.floatValue();
        }
        return getDefaultLeftIndent();
    }

    public static CoLineBreakerIF getLineBreaker(AttributeSet a, CoStyledDocumentIF doc) {
        String name = (String) a.getAttribute(HYPHENATION);
        CoHyphenationIF hs = null;
        if (name != null) {
            hs = doc.getHyphenation(name);
        }
        if (hs == null) {
            return getDefaultLineBreaker();
        }
        CoLineBreakerIF lb = hs.getLineBreaker();
        return lb;
    }

    public static float getMinimumSpaceWidth(AttributeSet a) {
        Float f = (Float) a.getAttribute(MINIMUM_SPACE_WIDTH);
        if (f != null) {
            return f.floatValue();
        }
        return getDefaultMinimumSpaceWidth();
    }

    public static float getOptimumSpaceWidth(AttributeSet a) {
        Float f = (Float) a.getAttribute(OPTIMUM_SPACE_WIDTH);
        if (f != null) {
            return f.floatValue();
        }
        return getDefaultOptimumSpaceWidth();
    }

    public static float getRegularTabStopInterval(AttributeSet a) {
        Float l = (Float) a.getAttribute(REGULAR_TAB_STOP_INTERVAL);
        if (l != null) {
            return l.floatValue();
        }
        return getDefaultRegularTabStopInterval();
    }

    public static float getRightIndent(AttributeSet a) {
        Float indent = (Float) a.getAttribute(RIGHT_INDENT);
        if (indent != null) {
            return indent.floatValue();
        }
        return getDefaultRightIndent();
    }

    public static float getShadowAngle(AttributeSet attributeSet) {
        Float sof = (Float) attributeSet.getAttribute(SHADOW_ANGLE);
        return (sof != null) ? sof.floatValue() : getDefaultShadowAngle();
    }

    public static String getShadowColor(AttributeSet a) {
        String colorName = (String) a.getAttribute(SHADOW_COLOR);
        if (colorName == null) {
            colorName = getDefaultShadowColorName();
        }
        return colorName;
    }

    public static Paint getShadowColor(AttributeSet a, CoStyledDocumentIF doc) {
        CoColorIF color = doc.getColor(getShadowColor(a));
        if (color == null) color = getDefaultShadowColor();
        float shade = getShadowShade(a);
        Color fg = color.getShadedPreviewColor(shade);
        return fg;
    }

    public static float getShadowOffset(AttributeSet attributeSet) {
        Float sof = (Float) attributeSet.getAttribute(SHADOW_OFFSET);
        return (sof != null) ? sof.floatValue() : getDefaultShadowOffset();
    }

    public static float getShadowShade(AttributeSet a) {
        Float s = (Float) a.getAttribute(SHADOW_SHADE);
        if (s == null) {
            return getDefaultShadowShade();
        }
        return s.floatValue();
    }

    public static float getSpaceAbove(AttributeSet a) {
        Float space = (Float) a.getAttribute(SPACE_ABOVE);
        if (space != null) {
            return space.floatValue();
        }
        return getDefaultSpaceAbove();
    }

    public static float getSpaceBelow(AttributeSet a) {
        Float space = (Float) a.getAttribute(SPACE_BELOW);
        if (space != null) {
            return space.floatValue();
        }
        return getDefaultSpaceBelow();
    }

    public static CoFontAttribute getStyle(AttributeSet a) {
        CoFontAttribute s = (CoFontAttribute) a.getAttribute(STYLE);
        if (s != null) {
            return s;
        }
        return getDefaultStyle();
    }

    public static CoTabSetIF getTabSet(AttributeSet a) {
        return (CoTabSetIF) a.getAttribute(TAB_SET);
    }

    public static CoEnumValue getTopRulerAlignment(AttributeSet a) {
        CoEnumValue align = (CoEnumValue) a.getAttribute(TOP_RULER_ALIGNMENT);
        if (align != null) return align; else return getDefaultRulerAlignment();
    }

    public static float getTopRulerFixedWidth(AttributeSet a) {
        Float f = (Float) a.getAttribute(TOP_RULER_FIXED_WIDTH);
        if (f != null) {
            return f.floatValue();
        }
        return getDefaultRulerFixedWidth();
    }

    public static float getTopRulerleftInset(AttributeSet a) {
        Float f = (Float) a.getAttribute(TOP_RULER_LEFT_INSET);
        if (f != null) {
            return f.floatValue();
        }
        return getDefaultRulerleftInset();
    }

    public static float getTopRulerPosition(AttributeSet a) {
        Float f = (Float) a.getAttribute(TOP_RULER_POSITION);
        if (f != null) {
            return f.floatValue();
        }
        return getDefaultRulerPosition();
    }

    public static float getTopRulerRightInset(AttributeSet a) {
        Float f = (Float) a.getAttribute(TOP_RULER_RIGHT_INSET);
        if (f != null) {
            return f.floatValue();
        }
        return getDefaultRulerRightInset();
    }

    public static CoEnumValue getTopRulerSpan(AttributeSet a) {
        CoEnumValue align = (CoEnumValue) a.getAttribute(TOP_RULER_SPAN);
        if (align != null) return align; else return getDefaultRulerSpan();
    }

    public static float getTopRulerThickness(AttributeSet a) {
        Float f = (Float) a.getAttribute(TOP_RULER_THICKNESS);
        if (f != null) {
            return f.floatValue();
        }
        return getDefaultRulerThickness();
    }

    public static float getTrackAmount(AttributeSet attributeSet) {
        Float trackAmount = (Float) attributeSet.getAttribute(TRACK_AMOUNT);
        return (trackAmount != null) ? trackAmount.floatValue() : getDefaultTrackAmount();
    }

    public static float getTrailingLinesIndent(AttributeSet a) {
        Float indent = (Float) a.getAttribute(TRAILING_LINES_INDENT);
        if (indent != null) {
            return indent.floatValue();
        }
        return getDefaultTrailingLinesIndent();
    }

    public static CoEnumValue getUnderline(AttributeSet a) {
        CoEnumValue underline = (CoEnumValue) a.getAttribute(UNDERLINE);
        if (underline != null) {
            return underline;
        }
        return getDefaultUnderline();
    }

    public static CoFontAttribute getWeight(AttributeSet a) {
        CoFontAttribute bold = (CoFontAttribute) a.getAttribute(WEIGHT);
        if (bold != null) {
            return bold;
        }
        return getDefaultWeight();
    }

    public static CoEnumValue getVerticalPosition(AttributeSet a) {
        CoEnumValue vp = (CoEnumValue) a.getAttribute(VERTICAL_POSITION);
        if (vp != null) {
            return vp;
        }
        return getDefaultVerticalPosition();
    }

    public static float getVerticalScale(AttributeSet a) {
        Float s = (Float) a.getAttribute(VERTICAL_SCALE);
        if (s == null) {
            return getDefaultVerticalScale();
        }
        return s.floatValue();
    }

    public static boolean hasDefaultAutoLeading() {
        return true;
    }

    public static boolean hasDefaultDropCaps() {
        return false;
    }

    public static boolean hasDefaultKeepLinesTogether() {
        return false;
    }

    public static boolean hasDropCaps(AttributeSet attributeSet) {
        Boolean state = (Boolean) attributeSet.getAttribute(DROP_CAPS);
        return (state != null) ? state.booleanValue() : hasDefaultDropCaps();
    }

    public static boolean hasKeepLinesTogether(AttributeSet attributeSet) {
        Boolean state = (Boolean) attributeSet.getAttribute(LINES_TOGETHER);
        return (state != null) ? state.booleanValue() : hasDefaultKeepLinesTogether();
    }

    public static boolean isAllCaps(AttributeSet attributeSet) {
        Boolean state = (Boolean) attributeSet.getAttribute(ALL_CAPS);
        return (state != null) ? state.booleanValue() : isDefaultAllCaps();
    }

    public static boolean isDefaultAllCaps() {
        return false;
    }

    public static boolean isDefaultLastInColumn() {
        return false;
    }

    public static boolean isDefaultOutline() {
        return false;
    }

    public static boolean isDefaultShadow() {
        return false;
    }

    public static boolean isDefaultSmallCaps() {
        return false;
    }

    public static boolean isDefaultStrikeThru() {
        return false;
    }

    public static boolean isDefaultSuperior() {
        return false;
    }

    public static boolean isDefaultTopOfColumn() {
        return false;
    }

    public static boolean isLastInColumn(AttributeSet a) {
        Boolean b = (Boolean) a.getAttribute(LAST_IN_COLUMN);
        if (b != null) {
            return b.booleanValue();
        }
        return isDefaultLastInColumn();
    }

    public static boolean isShadow(AttributeSet attributeSet) {
        Boolean state = (Boolean) attributeSet.getAttribute(SHADOW);
        return (state != null) ? state.booleanValue() : isDefaultShadow();
    }

    public static boolean isStrikeThru(AttributeSet attributeSet) {
        Boolean state = (Boolean) attributeSet.getAttribute(STRIKE_THRU);
        return (state != null) ? state.booleanValue() : isDefaultStrikeThru();
    }

    public static boolean isSuperior(AttributeSet attributeSet) {
        Boolean state = (Boolean) attributeSet.getAttribute(SUPERIOR);
        return (state != null) ? state.booleanValue() : isDefaultSuperior();
    }

    public static boolean isTopOfColumn(AttributeSet a) {
        Boolean b = (Boolean) a.getAttribute(TOP_OF_COLUMN);
        if (b != null) {
            return b.booleanValue();
        }
        return isDefaultTopOfColumn();
    }

    private static final CoLeading m_defaultLeading = new CoLeading();

    public static CoLeading getDefaultLeading() {
        return m_defaultLeading;
    }

    public static CoLeading getLeading(AttributeSet a) {
        CoLeading l = (CoLeading) a.getAttribute(LEADING);
        if (l != null) {
            return l;
        }
        return getDefaultLeading();
    }
}
