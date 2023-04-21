package net.myphpshop.admin.util;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import javax.swing.JTextField;

/**
 * @author Glenn Ganz
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DoubleTextField extends JTextField {

    public DoubleTextField() {
        super();
        addKeyListener(new KeyAdapter() {

            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!((Character.isDigit(c) || (c == KeyEvent.VK_BACK_SPACE) || (c == KeyEvent.VK_DELETE) || (c == '.')))) {
                    getToolkit().beep();
                    e.consume();
                }
                if (c == '.') {
                    if (getText().indexOf(".") != -1) {
                        getToolkit().beep();
                        e.consume();
                    }
                }
            }
        });
    }

    public BigDecimal getBigDecimal() {
        String text = getText();
        if (text.equals("")) {
            return new BigDecimal(0.00F);
        }
        return new BigDecimal(text);
    }
}
