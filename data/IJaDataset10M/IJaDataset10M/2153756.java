package org.gjt.sp.jedit.options;

import javax.swing.*;
import java.awt.*;
import org.gjt.sp.jedit.gui.FontSelector;
import org.gjt.sp.jedit.*;

public class TextAreaOptionPane extends AbstractOptionPane {

    public TextAreaOptionPane() {
        super("textarea");
    }

    public void _init() {
        addSeparator("options.textarea.textarea");
        String _fontFamily = jEdit.getProperty("view.font");
        int _fontStyle;
        try {
            _fontStyle = Integer.parseInt(jEdit.getProperty("view.fontstyle"));
        } catch (NumberFormatException nf) {
            _fontStyle = Font.PLAIN;
        }
        int _fontSize;
        try {
            _fontSize = Integer.parseInt(jEdit.getProperty("view.fontsize"));
        } catch (NumberFormatException nf) {
            _fontSize = 14;
        }
        font = new FontSelector(new Font(_fontFamily, _fontStyle, _fontSize));
        addComponent(jEdit.getProperty("options.textarea.font"), font);
        lineHighlight = new JCheckBox(jEdit.getProperty("options.textarea" + ".lineHighlight"));
        lineHighlight.setSelected(jEdit.getBooleanProperty("view.lineHighlight"));
        addComponent(lineHighlight);
        bracketHighlight = new JCheckBox(jEdit.getProperty("options.textarea" + ".bracketHighlight"));
        bracketHighlight.setSelected(jEdit.getBooleanProperty("view.bracketHighlight"));
        addComponent(bracketHighlight);
        eolMarkers = new JCheckBox(jEdit.getProperty("options.textarea" + ".eolMarkers"));
        eolMarkers.setSelected(jEdit.getBooleanProperty("view.eolMarkers"));
        addComponent(eolMarkers);
        paintInvalid = new JCheckBox(jEdit.getProperty("options.textarea" + ".paintInvalid"));
        paintInvalid.setSelected(jEdit.getBooleanProperty("view.paintInvalid"));
        addComponent(paintInvalid);
        blinkCaret = new JCheckBox(jEdit.getProperty("options.textarea" + ".blinkCaret"));
        blinkCaret.setSelected(jEdit.getBooleanProperty("view.caretBlink"));
        addComponent(blinkCaret);
        blockCaret = new JCheckBox(jEdit.getProperty("options.textarea" + ".blockCaret"));
        blockCaret.setSelected(jEdit.getBooleanProperty("view.blockCaret"));
        addComponent(blockCaret);
        electricBorders = new JCheckBox(jEdit.getProperty("options.textarea" + ".electricBorders"));
        electricBorders.setSelected(!"0".equals(jEdit.getProperty("view.electricBorders")));
        addComponent(electricBorders);
        homeEnd = new JCheckBox(jEdit.getProperty("options.textarea" + ".homeEnd"));
        homeEnd.setSelected(jEdit.getBooleanProperty("view.homeEnd"));
        addComponent(homeEnd);
        addSeparator("options.textarea.gutter");
        _fontFamily = jEdit.getProperty("view.gutter.font");
        try {
            _fontStyle = Integer.parseInt(jEdit.getProperty("view.gutter.fontstyle"));
        } catch (NumberFormatException nf) {
            _fontStyle = Font.PLAIN;
        }
        try {
            _fontSize = Integer.parseInt(jEdit.getProperty("view.gutter.fontsize"));
        } catch (NumberFormatException nf) {
            _fontSize = 14;
        }
        gutterFont = new FontSelector(new Font(_fontFamily, _fontStyle, _fontSize));
        addComponent(jEdit.getProperty("options.textarea.gutter.font"), gutterFont);
        gutterWidth = new JTextField(jEdit.getProperty("view.gutter.width"));
        addComponent(jEdit.getProperty("options.textarea.gutter.width"), gutterWidth);
        gutterBorderWidth = new JTextField(jEdit.getProperty("view.gutter.borderWidth"));
        addComponent(jEdit.getProperty("options.textarea.gutter.borderWidth"), gutterBorderWidth);
        gutterHighlightInterval = new JTextField(jEdit.getProperty("view.gutter.highlightInterval"));
        addComponent(jEdit.getProperty("options.textarea.gutter.interval"), gutterHighlightInterval);
        String[] alignments = new String[] { "Left", "Center", "Right" };
        gutterNumberAlignment = new JComboBox(alignments);
        String alignment = jEdit.getProperty("view.gutter.numberAlignment");
        if ("right".equals(alignment)) gutterNumberAlignment.setSelectedIndex(2); else if ("center".equals(alignment)) gutterNumberAlignment.setSelectedIndex(1); else gutterNumberAlignment.setSelectedIndex(0);
        addComponent(jEdit.getProperty("options.textarea.gutter.numberAlignment"), gutterNumberAlignment);
        gutterExpanded = new JCheckBox(jEdit.getProperty("options.textarea.gutter.expanded"));
        gutterExpanded.setSelected(!jEdit.getBooleanProperty("view.gutter.collapsed"));
        addComponent(gutterExpanded);
        lineNumbersEnabled = new JCheckBox(jEdit.getProperty("options.textarea.gutter.lineNumbers"));
        lineNumbersEnabled.setSelected(jEdit.getBooleanProperty("view.gutter.lineNumbers"));
        addComponent(lineNumbersEnabled);
        gutterCurrentLineHighlightEnabled = new JCheckBox(jEdit.getProperty("options.textarea.gutter.currentLineHighlight"));
        gutterCurrentLineHighlightEnabled.setSelected(jEdit.getBooleanProperty("view.gutter.highlightCurrentLine"));
        addComponent(gutterCurrentLineHighlightEnabled);
    }

    public void _save() {
        Font _font = font.getFont();
        jEdit.setProperty("view.font", _font.getFamily());
        jEdit.setProperty("view.fontsize", String.valueOf(_font.getSize()));
        jEdit.setProperty("view.fontstyle", String.valueOf(_font.getStyle()));
        jEdit.setBooleanProperty("view.lineHighlight", lineHighlight.isSelected());
        jEdit.setBooleanProperty("view.bracketHighlight", bracketHighlight.isSelected());
        jEdit.setBooleanProperty("view.eolMarkers", eolMarkers.isSelected());
        jEdit.setBooleanProperty("view.paintInvalid", paintInvalid.isSelected());
        jEdit.setBooleanProperty("view.caretBlink", blinkCaret.isSelected());
        jEdit.setBooleanProperty("view.blockCaret", blockCaret.isSelected());
        jEdit.setProperty("view.electricBorders", electricBorders.isSelected() ? "3" : "0");
        jEdit.setBooleanProperty("view.homeEnd", homeEnd.isSelected());
        _font = gutterFont.getFont();
        jEdit.setProperty("view.gutter.font", _font.getFamily());
        jEdit.setProperty("view.gutter.fontsize", String.valueOf(_font.getSize()));
        jEdit.setProperty("view.gutter.fontstyle", String.valueOf(_font.getStyle()));
        jEdit.setProperty("view.gutter.width", gutterWidth.getText());
        jEdit.setProperty("view.gutter.borderWidth", gutterBorderWidth.getText());
        jEdit.setProperty("view.gutter.highlightInterval", gutterHighlightInterval.getText());
        String alignment = null;
        switch(gutterNumberAlignment.getSelectedIndex()) {
            case 2:
                alignment = "right";
                break;
            case 1:
                alignment = "center";
                break;
            case 0:
            default:
                alignment = "left";
        }
        jEdit.setProperty("view.gutter.numberAlignment", alignment);
        jEdit.setBooleanProperty("view.gutter.collapsed", !gutterExpanded.isSelected());
        jEdit.setBooleanProperty("view.gutter.lineNumbers", lineNumbersEnabled.isSelected());
        jEdit.setBooleanProperty("view.gutter.highlightCurrentLine", gutterCurrentLineHighlightEnabled.isSelected());
    }

    private FontSelector font;

    private JCheckBox lineHighlight;

    private JCheckBox bracketHighlight;

    private JCheckBox eolMarkers;

    private JCheckBox paintInvalid;

    private JCheckBox blinkCaret;

    private JCheckBox blockCaret;

    private JCheckBox electricBorders;

    private JCheckBox homeEnd;

    private FontSelector gutterFont;

    private JTextField gutterWidth;

    private JTextField gutterBorderWidth;

    private JTextField gutterHighlightInterval;

    private JComboBox gutterNumberAlignment;

    private JCheckBox gutterExpanded;

    private JCheckBox lineNumbersEnabled;

    private JCheckBox gutterCurrentLineHighlightEnabled;
}
