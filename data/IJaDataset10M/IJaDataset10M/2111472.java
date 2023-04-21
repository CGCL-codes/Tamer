package intellibitz.sted.ui;

import intellibitz.sted.event.FontListChangeEvent;
import intellibitz.sted.fontmap.FontMap;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.io.File;

public class FontKeypad1 extends FontKeypad implements ChangeListener {

    public FontKeypad1() {
        super();
    }

    protected void setCurrentFont() {
        super.setCurrentFont(getFontMap().getFont1());
    }

    public void loadFont(File font) {
        final FontMap fontMap = getFontMap();
        if (fontMap != null) {
            fontMap.setFont1(font);
        }
    }

    protected void setCurrentFont(String font) {
        super.setCurrentFont(font);
        final FontMap fontMap = getFontMap();
        if (fontMap != null) {
            fontMap.setFont1(getCurrentFont());
        }
    }

    /**
     * Invoked when the target of the listener has changed its state.
     *
     * @param e a ChangeEvent object
     */
    public void stateChanged(ChangeEvent e) {
        if (((FontListChangeEvent) e).getFontIndex() == 1) {
            getFontSelector().stateChanged(e);
            updateUI();
        }
    }
}
