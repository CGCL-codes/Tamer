package com.apachetune.core.ui.impl;

import com.apachetune.core.ui.OutputPaneDocument;
import com.apachetune.core.utils.Utils;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import java.awt.*;
import static com.apachetune.core.utils.Utils.createRuntimeException;
import static java.awt.Color.BLACK;
import static javax.swing.text.StyleConstants.setForeground;
import static org.apache.commons.lang.StringUtils.defaultString;
import static org.apache.commons.lang.Validate.notNull;

/**
 * FIXDOC
 *
 * @author <a href="mailto:progmonster@gmail.com">Aleksey V. Katorgin</a>
 * @version 1.0
 */
public class OutputPaneDocumentImpl extends DefaultStyledDocument implements OutputPaneDocument {

    private static final long serialVersionUID = -8914774787481869235L;

    public void clear() {
        setText(null);
    }

    public void setText(String text) {
        setColoredText(text, BLACK);
    }

    public void setColoredText(String text, Color color) {
        notNull(color, "Argument color cannot be a null");
        MutableAttributeSet attr = new SimpleAttributeSet();
        setForeground(attr, color);
        try {
            replace(0, getLength(), defaultString(text), attr);
        } catch (BadLocationException e) {
            throw createRuntimeException(e);
        }
    }
}
