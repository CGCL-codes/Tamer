package com.jgoodies.forms.util;

import com.jgoodies.forms.layout.ConstantSize;
import com.jgoodies.forms.layout.Size;
import com.jgoodies.forms.layout.Sizes;

/**
 * A {@link LayoutStyle} that aims to provide layout constants as defined by
 * Microsoft's <i>Design Specifications and Guidelines - Visual Design</i>.
 *
 * @author Karsten Lentzsch
 * @version $Revision: 1.2 $
 */
final class WindowsLayoutStyle extends LayoutStyle {

    static final WindowsLayoutStyle INSTANCE = new WindowsLayoutStyle();

    private WindowsLayoutStyle() {
    }

    private static final Size BUTTON_WIDTH = Sizes.dluX(50);

    private static final Size BUTTON_HEIGHT = Sizes.dluY(14);

    private static final ConstantSize DIALOG_MARGIN_X = Sizes.DLUX7;

    private static final ConstantSize DIALOG_MARGIN_Y = Sizes.DLUY7;

    private static final ConstantSize TABBED_DIALOG_MARGIN_X = Sizes.DLUX4;

    private static final ConstantSize TABBED_DIALOG_MARGIN_Y = Sizes.DLUY4;

    private static final ConstantSize LABEL_COMPONENT_PADX = Sizes.DLUX3;

    private static final ConstantSize RELATED_COMPONENTS_PADX = Sizes.DLUX4;

    private static final ConstantSize UNRELATED_COMPONENTS_PADX = Sizes.DLUX7;

    private static final ConstantSize RELATED_COMPONENTS_PADY = Sizes.DLUY4;

    private static final ConstantSize UNRELATED_COMPONENTS_PADY = Sizes.DLUY7;

    private static final ConstantSize NARROW_LINE_PAD = Sizes.DLUY2;

    private static final ConstantSize LINE_PAD = Sizes.DLUY3;

    private static final ConstantSize PARAGRAPH_PAD = Sizes.DLUY9;

    private static final ConstantSize BUTTON_BAR_PAD = Sizes.DLUY5;

    /**
     * Returns this style's default button width.
     * 
     * @return the default button width
     * 
     * @see #getDefaultButtonHeight()
     */
    public Size getDefaultButtonWidth() {
        return BUTTON_WIDTH;
    }

    /**
     * Returns this style's default button height.
     * 
     * @return the default button height
     * 
     * @see #getDefaultButtonWidth()
     */
    public Size getDefaultButtonHeight() {
        return BUTTON_HEIGHT;
    }

    /**
     * Returns this style's horizontal margin for general dialogs.
     * 
     * @return the horizontal margin for general dialogs
     * 
     * @see #getDialogMarginY()
     * @see #getTabbedDialogMarginX()
     */
    public ConstantSize getDialogMarginX() {
        return DIALOG_MARGIN_X;
    }

    /**
     * Returns this style's vertical margin for general dialogs.
     * 
     * @return the vertical margin for general dialogs
     * 
     * @see #getDialogMarginX()
     * @see #getTabbedDialogMarginY()
     */
    public ConstantSize getDialogMarginY() {
        return DIALOG_MARGIN_Y;
    }

    /**
     * Returns this style's horizontal margin for dialogs that consist of 
     * a tabbed pane.
     * 
     * @return the horizontal margin for dialogs that consist of a tabbed pane
     * @since 1.0.3
     * 
     * @see #getTabbedDialogMarginY()
     * @see #getDialogMarginX()
     */
    public ConstantSize getTabbedDialogMarginX() {
        return TABBED_DIALOG_MARGIN_X;
    }

    /**
     * Returns this style's vertical margin for dialogs that consist of
     * a tabbed pane.
     * 
     * @return the vertical margin for dialogs that consist of a tabbed pane
     * @since 1.0.3
     * 
     * @see #getTabbedDialogMarginX()
     * @see #getDialogMarginY()
     */
    public ConstantSize getTabbedDialogMarginY() {
        return TABBED_DIALOG_MARGIN_Y;
    }

    /**
     * Returns a gap used to separate a label and associated control.
     * 
     * @return a gap between label and associated control
     * 
     * @see #getRelatedComponentsPadX()
     * @see #getUnrelatedComponentsPadX()
     */
    public ConstantSize getLabelComponentPadX() {
        return LABEL_COMPONENT_PADX;
    }

    /**
     * Returns a horizontal gap used to separate related controls.
     * 
     * @return a horizontal gap between related controls
     * 
     * @see #getLabelComponentPadX()
     * @see #getRelatedComponentsPadY()
     * @see #getUnrelatedComponentsPadX()
     */
    public ConstantSize getRelatedComponentsPadX() {
        return RELATED_COMPONENTS_PADX;
    }

    /**
     * Returns a vertical gap used to separate related controls.
     * 
     * @return a vertical gap between related controls
     * 
     * @see #getRelatedComponentsPadX()
     * @see #getUnrelatedComponentsPadY()
     */
    public ConstantSize getRelatedComponentsPadY() {
        return RELATED_COMPONENTS_PADY;
    }

    /**
     * Returns a horizontal gap used to separate unrelated controls.
     * 
     * @return a horizontal gap between unrelated controls
     * 
     * @see #getLabelComponentPadX()
     * @see #getUnrelatedComponentsPadY()
     * @see #getRelatedComponentsPadX()
     */
    public ConstantSize getUnrelatedComponentsPadX() {
        return UNRELATED_COMPONENTS_PADX;
    }

    /**
     * Returns a vertical gap used to separate unrelated controls.
     * 
     * @return a vertical gap between unrelated controls
     * 
     * @see #getUnrelatedComponentsPadX()
     * @see #getRelatedComponentsPadY()
     */
    public ConstantSize getUnrelatedComponentsPadY() {
        return UNRELATED_COMPONENTS_PADY;
    }

    /**
     * Returns a narrow vertical pad used to separate lines.
     * 
     * @return a narrow vertical pad used to separate lines
     * 
     * @see #getLinePad()
     * @see #getParagraphPad()
     */
    public ConstantSize getNarrowLinePad() {
        return NARROW_LINE_PAD;
    }

    /**
     * Returns a narrow vertical pad used to separate lines.
     * 
     * @return a vertical pad used to separate lines
     * 
     * @see #getNarrowLinePad()
     * @see #getParagraphPad()
     */
    public ConstantSize getLinePad() {
        return LINE_PAD;
    }

    /**
     * Returns a pad used to separate paragraphs.
     * 
     * @return a vertical pad used to separate paragraphs
     * 
     * @see #getNarrowLinePad()
     * @see #getLinePad()
     */
    public ConstantSize getParagraphPad() {
        return PARAGRAPH_PAD;
    }

    /**
     * Returns a pad used to separate a button bar from a component.
     * 
     * @return a vertical pad used to separate paragraphs
     * @since 1.0.3
     * 
     * @see #getRelatedComponentsPadY()
     * @see #getUnrelatedComponentsPadY()
     */
    public ConstantSize getButtonBarPad() {
        return BUTTON_BAR_PAD;
    }

    /**
     * Checks and answers whether buttons are typically ordered from 
     * left to right or from right to left. Useful for building button bars 
     * that shall comply with the platform's layout style guide.<p>
     * 
     * For example the Windows style guide recommends to layout out
     * <em>OK, Cancel, Apply</em> from left to right, where the 
     * Mac Aqua style guide recommends to layout out these buttons
     * from right to left.<p>
     * 
     * Although most button sequences shall honor this order
     * some buttons require a left to right order. For example
     * <em>Back, Next</em> or <em>Move Left, Move Right</em>.<p>
     * 
     * @return true if buttons are typically ordered from left to right
     * @since 1.0.3
     * 
     * @see com.jgoodies.forms.builder.ButtonBarBuilder
     * @see com.jgoodies.forms.factories.ButtonBarFactory
     */
    public boolean isLeftToRightButtonOrder() {
        return true;
    }
}
