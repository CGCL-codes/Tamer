package com.xiledsystems.AlternateJavaBridgelib.components.altbridge;

import com.xiledsystems.AlternateJavaBridgelib.components.Component;
import com.xiledsystems.AlternateJavaBridgelib.components.altbridge.util.TextViewUtil;
import com.xiledsystems.AlternateJavaBridgelib.components.altbridge.util.ViewUtil;
import com.xiledsystems.AlternateJavaBridgelib.components.common.ComponentConstants;
import com.xiledsystems.AlternateJavaBridgelib.components.events.EventDispatcher;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;

/**
 * Underlying base class for TextBox, not directly accessible to Simple
 * programmers.
 *
 */
public abstract class TextBoxBase extends AndroidViewComponent implements OnFocusChangeListener {

    protected final EditText view;

    private int textAlignment;

    private int backgroundColor;

    private int fontTypeface;

    private boolean bold;

    private boolean italic;

    private String hint;

    private int textColor;

    private Drawable defaultTextBoxDrawable;

    /**
   * Creates a new TextBoxBase component
   *
   * @param container  container that the component will be placed in
   * @param textview   the underlying EditText object that maintains the text
   */
    public TextBoxBase(ComponentContainer container, EditText textview) {
        super(container);
        view = textview;
        view.setOnFocusChangeListener(this);
        defaultTextBoxDrawable = view.getBackground();
        container.$add(this);
        container.setChildWidth(this, ComponentConstants.TEXTBOX_PREFERRED_WIDTH);
        TextAlignment(Component.ALIGNMENT_NORMAL);
        Enabled(true);
        fontTypeface = Component.TYPEFACE_DEFAULT;
        TextViewUtil.setFontTypeface(view, fontTypeface, bold, italic);
        FontSize(Component.FONT_DEFAULT_SIZE);
        Hint("");
        Text("");
        TextColor(Component.COLOR_BLACK);
    }

    public TextBoxBase(ComponentContainer container, EditText textview, int resourceId) {
        super(container, resourceId);
        view = textview;
        view.setOnFocusChangeListener(this);
        defaultTextBoxDrawable = view.getBackground();
        Enabled(true);
        fontTypeface = Component.TYPEFACE_DEFAULT;
    }

    @Override
    public View getView() {
        return view;
    }

    /**
   * Event raised when this component is selected for input, such as by
   * the user touching it.
   */
    public void GotFocus() {
        EventDispatcher.dispatchEvent(this, "GotFocus");
    }

    /**
   * Event raised when this component is no longer selected for input, such
   * as if the user touches a different text box.
   */
    public void LostFocus() {
        EventDispatcher.dispatchEvent(this, "LostFocus");
    }

    /**
   * Returns the alignment of the textbox's text: center, normal
   * (e.g., left-justified if text is written left to right), or
   * opposite (e.g., right-justified if text is written left to right).
   *
   * @return  one of {@link Component#ALIGNMENT_NORMAL},
   *          {@link Component#ALIGNMENT_CENTER} or
   *          {@link Component#ALIGNMENT_OPPOSITE}
   */
    public int TextAlignment() {
        return textAlignment;
    }

    /**
   * Specifies the alignment of the textbox's text: center, normal
   * (e.g., left-justified if text is written left to right), or
   * opposite (e.g., right-justified if text is written left to right).
   *
   * @param alignment  one of {@link Component#ALIGNMENT_NORMAL},
   *                   {@link Component#ALIGNMENT_CENTER} or
   *                   {@link Component#ALIGNMENT_OPPOSITE}
   */
    public void TextAlignment(int alignment) {
        this.textAlignment = alignment;
        TextViewUtil.setAlignment(view, alignment, false);
    }

    /**
   * Returns the textbox's background color as an alpha-red-green-blue
   * integer.
   *
   * @return  background RGB color with alpha
   */
    public int BackgroundColor() {
        return backgroundColor;
    }

    /**
   * Specifies the textbox's background color as an alpha-red-green-blue
   * integer.
   *
   * @param argb  background RGB color with alpha
   */
    public void BackgroundColor(int argb) {
        backgroundColor = argb;
        if (argb != Component.COLOR_DEFAULT) {
            TextViewUtil.setBackgroundColor(view, argb);
        } else {
            ViewUtil.setBackgroundDrawable(view, defaultTextBoxDrawable);
        }
    }

    /**
   * Returns true if the textbox is active and useable.
   *
   * @return  {@code true} indicates enabled, {@code false} disabled
   */
    public boolean Enabled() {
        return TextViewUtil.isEnabled(view);
    }

    /**
   * Specifies whether the textbox should be active and useable.
   *
   * @param enabled  {@code true} for enabled, {@code false} disabled
   */
    public void Enabled(boolean enabled) {
        TextViewUtil.setEnabled(view, enabled);
    }

    /**
   * Returns true if the textbox's text should be bold.
   * If bold has been requested, this property will return true, even if the
   * font does not support bold.
   *
   * @return  {@code true} indicates bold, {@code false} normal
   */
    public boolean FontBold() {
        return bold;
    }

    /**
   * Specifies whether the textbox's text should be bold.
   * Some fonts do not support bold.
   *
   * @param bold  {@code true} indicates bold, {@code false} normal
   */
    public void FontBold(boolean bold) {
        this.bold = bold;
        TextViewUtil.setFontTypeface(view, fontTypeface, bold, italic);
    }

    /**
   * Returns true if the textbox's text should be italic.
   * If italic has been requested, this property will return true, even if the
   * font does not support italic.
   *
   * @return  {@code true} indicates italic, {@code false} normal
   */
    public boolean FontItalic() {
        return italic;
    }

    /**
   * Specifies whether the textbox's text should be italic.
   * Some fonts do not support italic.
   *
   * @param italic  {@code true} indicates italic, {@code false} normal
   */
    public void FontItalic(boolean italic) {
        this.italic = italic;
        TextViewUtil.setFontTypeface(view, fontTypeface, bold, italic);
    }

    /**
   * Returns the textbox's text's font size, measured in pixels.
   *
   * @return  font size in pixel
   */
    public float FontSize() {
        return TextViewUtil.getFontSize(view);
    }

    /**
   * Specifies the textbox's text's font size, measured in pixels.
   *
   * @param size  font size in pixel
   */
    public void FontSize(float size) {
        TextViewUtil.setFontSize(view, size);
    }

    /**
   * Returns the textbox's text's font face as default, serif, sans
   * serif, or monospace.
   *
   * @return  one of {@link Component#TYPEFACE_DEFAULT},
   *          {@link Component#TYPEFACE_SERIF},
   *          {@link Component#TYPEFACE_SANSSERIF} or
   *          {@link Component#TYPEFACE_MONOSPACE}
   */
    public int FontTypeface() {
        return fontTypeface;
    }

    /**
   * Specifies the textbox's text's font face as default, serif, sans
   * serif, or monospace.
   *
   * @param typeface  one of {@link Component#TYPEFACE_DEFAULT},
   *                  {@link Component#TYPEFACE_SERIF},
   *                  {@link Component#TYPEFACE_SANSSERIF} or
   *                  {@link Component#TYPEFACE_MONOSPACE}
   */
    public void FontTypeface(int typeface) {
        fontTypeface = typeface;
        TextViewUtil.setFontTypeface(view, fontTypeface, bold, italic);
    }

    /**
   * Hint property getter method.
   *
   * @return  hint text
   */
    public String Hint() {
        return hint;
    }

    /**
   * Hint property setter method.
   *
   * @param hint  hint text
   */
    public void Hint(String hint) {
        this.hint = hint;
        view.setHint(hint);
        view.invalidate();
    }

    /**
   * Returns the textbox contents.
   *
   * @return  text box contents
   */
    public String Text() {
        return TextViewUtil.getText(view);
    }

    /**
   * Specifies the textbox contents.
   *
   * @param text  new text in text box
   */
    public void Text(String text) {
        TextViewUtil.setText(view, text);
    }

    /**
   * Returns the textbox's text color as an alpha-red-green-blue
   * integer.
   *
   * @return  text RGB color with alpha
   */
    public int TextColor() {
        return textColor;
    }

    /**
   * Specifies the textbox's text color as an alpha-red-green-blue
   * integer.
   *
   * @param argb  text RGB color with alpha
   */
    public void TextColor(int argb) {
        textColor = argb;
        if (argb != Component.COLOR_DEFAULT) {
            TextViewUtil.setTextColor(view, argb);
        } else {
            TextViewUtil.setTextColor(view, Component.COLOR_BLACK);
        }
    }

    @Override
    public void onFocusChange(View previouslyFocused, boolean gainFocus) {
        if (gainFocus) {
            GotFocus();
        } else {
            LostFocus();
        }
    }

    @Override
    public void postAnimEvent() {
        EventDispatcher.dispatchEvent(this, "AnimationMiddle");
    }
}
