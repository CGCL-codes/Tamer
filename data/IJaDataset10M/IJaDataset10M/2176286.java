package org.digitall.lib.components.basic;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.Format;
import java.text.ParseException;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.JFormattedTextField;
import javax.swing.text.MaskFormatter;

public class BasicTextInput extends JFormattedTextField {

    private Color backgroundColor = BasicConfig.TEXTFIELD_ENABLED_BACKGROUND_COLOR;

    private Color disabledBackgroundColor = BasicConfig.TEXTFIELD_DISABLED_BACKGROUND_COLOR;

    private Color foregroundColor = BasicConfig.TEXTFIELD_ENABLED_FOREGROUND_COLOR;

    private Color disabledforegroundColor = BasicConfig.TEXTFIELD_DISABLED_FOREGROUND_COLOR;

    private Color uneditableBackgroundColor = BasicConfig.TEXTFIELD_UNEDITABLE_BACKGROUND_COLOR;

    private Color uneditableForegroundColor = BasicConfig.TEXTFIELD_UNEDITABLE_FOREGROUND_COLOR;

    public BasicTextInput() {
        super();
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public BasicTextInput(int _int) {
        super(_int);
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public BasicTextInput(String _format) throws ParseException {
        super(new MaskFormatter(_format));
        ((MaskFormatter) getFormatter()).setPlaceholderCharacter('_');
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public BasicTextInput(AbstractFormatter _format) {
        super(_format);
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public BasicTextInput(Format _format) {
        super(_format);
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public BasicTextInput(Date _date) {
        super(_date);
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        setForegroundColor(foregroundColor);
        setDisabledTextColor(disabledforegroundColor);
        addFocusListener(new FocusListener() {

            public void focusGained(FocusEvent focusEvent) {
                setBorder(BorderFactory.createLineBorder(new Color(199, 0, 0), 2));
            }

            public void focusLost(FocusEvent focusEvent) {
                try {
                    if (isEnabled()) {
                        setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0), 1));
                        commitEdit();
                    }
                } catch (ParseException e) {
                    setBorder(BorderFactory.createLineBorder(new Color(255, 0, 0), 1));
                }
            }
        });
        setEnabled(true);
    }

    public void setEnabled(boolean _enabled) {
        super.setEnabled(_enabled);
        updateColors();
    }

    public void setEditable(boolean _editable) {
        super.setEditable(_editable);
        updateColors();
    }

    public void updateColors() {
        if (isEnabled()) {
            if (isEditable()) {
                setBorder(BorderFactory.createLineBorder(BasicConfig.TEXTFIELD_ENABLED_BORDER_COLOR, 1));
                setBackground(backgroundColor);
                setForeground(foregroundColor);
                setCaretColor(foregroundColor);
            } else {
                setBorder(BorderFactory.createLineBorder(BasicConfig.TEXTFIELD_UNEDITABLE_BORDER_COLOR, 1));
                setBackground(uneditableBackgroundColor);
                setForeground(uneditableForegroundColor);
                setCaretColor(uneditableForegroundColor);
            }
        } else {
            setBorder(BorderFactory.createLineBorder(BasicConfig.TEXTFIELD_DISABLED_BORDER_COLOR, 1));
            setBackground(disabledBackgroundColor);
            setForeground(disabledforegroundColor);
        }
    }

    public void setFormatter(AbstractFormatter _formatter) {
        super.setFormatter(_formatter);
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        updateColors();
    }

    public void setDisabledBackgroundColor(Color disabledBackgroundColor) {
        this.disabledBackgroundColor = disabledBackgroundColor;
        updateColors();
    }

    public void setForegroundColor(Color foregroundColor) {
        this.foregroundColor = foregroundColor;
        updateColors();
    }

    public void setDisabledforegroundColor(Color disabledforegroundColor) {
        this.disabledforegroundColor = disabledforegroundColor;
        updateColors();
    }

    public void setUneditableBackgroundColor(Color uneditableBackgroundColor) {
        this.uneditableBackgroundColor = uneditableBackgroundColor;
        updateColors();
    }

    public void setUneditableForegroundColor(Color uneditableForegroundColor) {
        this.uneditableForegroundColor = uneditableForegroundColor;
        updateColors();
    }
}
