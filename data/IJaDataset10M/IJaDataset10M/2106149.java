package rene.gui;

import java.awt.TextField;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/**
 * A TextField with a modifyable background and font.
 */
public class MyTextField extends TextField implements FocusListener {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public MyTextField(final String s, final int n) {
        super(s, n);
        if (Global.NormalFont != null) setFont(Global.NormalFont);
        addFocusListener(this);
    }

    public MyTextField(final String s) {
        super(s);
        if (Global.NormalFont != null) setFont(Global.NormalFont);
        addFocusListener(this);
    }

    public MyTextField() {
        if (Global.NormalFont != null) setFont(Global.NormalFont);
        addFocusListener(this);
    }

    public void focusGained(final FocusEvent e) {
        setSelectionStart(0);
    }

    public void focusLost(final FocusEvent e) {
        setSelectionStart(0);
        setSelectionEnd(0);
    }
}
