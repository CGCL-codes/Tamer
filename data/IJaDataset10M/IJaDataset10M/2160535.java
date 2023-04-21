package net.cyan.activex.word;

import net.cyan.activex.ActiveXObject;
import com.jacob.com.Dispatch;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author ccs
 * @version 1.0
 */
public abstract class ParagraphFormat implements ActiveXObject {

    public abstract boolean isAddSpaceBetweenFarEastAndAlpha();

    public abstract void setAddSpaceBetweenFarEastAndAlpha(boolean addSpaceBetweenFarEastAndAlpha);

    public abstract boolean isAddSpaceBetweenFarEastAndDigit();

    public abstract void setAddSpaceBetweenFarEastAndDigit(boolean addSpaceBetweenFarEastAndDigit);

    public abstract void setAlignment(int alignment);

    public abstract int getAlignment();

    public abstract boolean isAutoAdjustRightIndent();

    public abstract void setAutoAdjustRightIndent(boolean autoAdjustRightIndent);

    public abstract int getBaseLineAlignment();

    public abstract void setBaseLineAlignment(int baseLineAlignment);

    public abstract Borders getBorders();

    public abstract int getCharacterUnitFirstLineIndent();

    public abstract void setCharacterUnitFirstLineIndent(int characterUnitFirstLineIndent);

    public abstract int getCharacterUnitLeftIndent();

    public abstract void setCharacterUnitLeftIndent(int characterUnitLeftIndent);

    public abstract int getCharacterUnitRightIndent();

    public abstract void setCharacterUnitRightIndent(int characterUnitRightIndent);

    public abstract boolean isDisableLineHeightGrid();

    public abstract void setDisableLineHeightGrid(boolean disableLineHeightGrid);

    public abstract boolean isFarEastLineBreakControl();

    public abstract void setFarEastLineBreakControl(boolean farEastLineBreakControl);

    public abstract int getFirstLineIndent();

    public abstract void setFirstLineIndent(boolean firstLineIndent);

    public abstract boolean isHalfWidthPunctuationOnTopOfLine();

    public abstract void setHalfWidthPunctuationOnTopOfLine(boolean halfWidthPunctuationOnTopOfLine);

    public abstract boolean isHangingPunctuation();

    public abstract void setHangingPunctuation(boolean hangingPunctuation);

    public abstract boolean isHyphenation();

    public abstract void setHyphenation(boolean hyphenation);

    public abstract boolean isKeepTogether();

    public abstract void setKeepTogether(boolean keepTogether);

    public abstract boolean isKeepWithNext();

    public abstract void setKeepWithNext(boolean keepWithNext);

    public abstract int getLeftIndent();

    public abstract void setLeftIndent(int leftIndent);

    public abstract int getLineSpacing();

    public abstract void setLineSpacing(int lineSpacing);

    public abstract int getLineSpacingRule();

    public abstract void setLineSpacingRule(int lineSpacingRule);

    public abstract int getLineUnitAfter();

    public abstract void setLineUnitAfter(int lineUnitAfter);

    public abstract int getLineUnitBefore();

    public abstract void setLineUnitBefore(int lineUnitBefore);

    public abstract boolean isNoLineNumber();

    public abstract void setNoLineNumber(boolean noLineNumber);

    public abstract int getOutlineLevel();

    public abstract void setOutlineLevel(int outlineLevel);

    public abstract boolean isPageBreakBefore();

    public abstract void setPageBreakBefore(boolean pageBreakBefore);

    public abstract int getReadingOrder();

    public abstract void setReadingOrder(boolean readingOrder);

    public abstract int getRightIndent();

    public abstract void setRightIndent(int rightIndent);

    public abstract Shading getShading();

    public abstract int getSpaceAfter();

    public abstract void setSpaceAfter(int spaceAfter);

    public abstract boolean isSpaceAfterAuto();

    public abstract void setSpaceAfterAuto(boolean spaceAfterAuto);

    public abstract int getSpaceBefore();

    public abstract void setSpaceBefore(int spaceBefore);

    public abstract boolean isSpaceBeforeAuto();

    public abstract void setSpaceBeforeAuto(boolean spaceBeforeAuto);

    public abstract boolean isWidowControl();

    public abstract void setWidowControl(int widowControl);

    public abstract boolean isWordWrap();

    public abstract void setWordWrap(boolean wordWrap);

    public abstract TabStops getTabStops();

    public abstract void clearUp();

    public abstract void indentCharWidth(int count);

    public abstract void indentFirstLineCharWidth(int count);

    public abstract void openOrCloseUp();

    public abstract void openUp();

    public abstract void reset();

    public abstract void space1();

    public abstract void space15();

    public abstract void space2();

    public abstract void tabHangingIndent(int count);

    public abstract void tabIndent(int count);
}
