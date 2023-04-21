package de.miij.ui.comp;

import java.awt.Color;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import javax.swing.JTextArea;
import de.miij.language.ILanguageSupport;

/**
 * In der Klassenvariable Actions sind Connector-Objekte zu hinterlegen,
 * welche bei einem Rechtsklick auf diese Komponente ein Popup-Men� �ffnen.
 * 
 * @author Mirhec
 */
public class MTextArea extends JTextArea implements ILanguageSupport {

    public boolean highlight = true;

    public Connector focusGained = null;

    public Connector focusLost = null;

    public Connector mousePressed = null;

    public Connector mouseReleased = null;

    public Connector mouseEntered = null;

    public Connector mouseClicked = null;

    public Connector mouseExited = null;

    public Connector mouseDragged = null;

    public Connector mouseMoved = null;

    public ArrayList Actions = new ArrayList();

    /**
	 * @param arg0
	 */
    public MTextArea(String arg0) {
        super(arg0);
        init();
    }

    public MTextArea(boolean highlight) {
        this.highlight = highlight;
        init();
    }

    public MTextArea() {
        this.highlight = true;
        init();
    }

    public MTextArea setHighlighted(boolean highlight) {
        this.highlight = highlight;
        return this;
    }

    private void init() {
        addFocusListener(new FocusAdapter() {

            public void focusGained(FocusEvent e) {
                if (highlight) MTextArea.this.setBackground(new Color(255, 255, 160));
                if (focusGained != null) focusGained.action(e);
            }

            public void focusLost(FocusEvent e) {
                if (highlight) MTextArea.this.setBackground(Color.WHITE);
                if (focusLost != null) focusLost.action(e);
            }
        });
        addMouseListener(new MouseListener() {

            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    Connector.popup(e, Actions);
                }
                if (mouseClicked != null) mouseClicked.action(e);
            }

            public void mouseEntered(MouseEvent e) {
                if (mouseEntered != null) mouseEntered.action(e);
            }

            public void mouseExited(MouseEvent e) {
                if (mouseExited != null) mouseExited.action(e);
            }

            public void mousePressed(MouseEvent e) {
                if (mousePressed != null) mousePressed.action(e);
            }

            public void mouseReleased(MouseEvent e) {
                if (mouseReleased != null) mouseReleased.action(e);
            }
        });
        addMouseMotionListener(new MouseMotionListener() {

            public void mouseDragged(MouseEvent e) {
                if (mouseDragged != null) mouseDragged.action(e);
            }

            public void mouseMoved(MouseEvent e) {
                if (mouseMoved != null) mouseMoved.action(e);
            }
        });
    }

    public MTextArea focusGained(Connector c) {
        focusGained = c;
        return this;
    }

    public MTextArea focusLost(Connector c) {
        focusLost = c;
        return this;
    }

    public MTextArea mousePressed(Connector c) {
        mousePressed = c;
        return this;
    }

    public MTextArea mouseEntered(Connector c) {
        mouseEntered = c;
        return this;
    }

    public MTextArea mouseClicked(Connector c) {
        mouseClicked = c;
        return this;
    }

    public MTextArea mouseExited(Connector c) {
        mouseExited = c;
        return this;
    }

    public MTextArea mouseDragged(Connector c) {
        mouseDragged = c;
        return this;
    }

    public MTextArea mouseMoved(Connector c) {
        mouseMoved = c;
        return this;
    }

    public MTextArea mouseReleased(Connector c) {
        mouseReleased = c;
        return this;
    }

    public String text() {
        return getText();
    }

    public MTextArea text(String text) {
        setText(text);
        return this;
    }

    /**
	 * F�gt der Action-Liste einen neuen neuen Eintrag hinzu
	 * (also einen neuen Popup-Men�-Eintrag).
	 * 
	 * @param c
	 * @return
	 */
    public MTextArea popupItem(Connector c) {
        Actions.add(c);
        return this;
    }
}
