package net.sf.catchup.client.ui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;
import java.text.MessageFormat;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Hyperlink extends JLabel implements MouseListener {

    public enum IconTextAlignment {

        HORIZONTAL, VERTICAL
    }

    ;

    private Color highlightedColor;

    private Color normalColor;

    private ActionListener listener;

    private String command;

    private static final String HTML_PATTERN = "<html><center><img src=\"{0}\"></img><br>{1}</center></html>";

    public Hyperlink(final String text, final URL iconURL, final String toolTipText, IconTextAlignment align) {
        setToolTipText(toolTipText);
        switch(align) {
            case HORIZONTAL:
                setText(text);
                setIcon(new ImageIcon(iconURL));
                break;
            case VERTICAL:
                if (iconURL == null) {
                    setText(text);
                } else {
                    setText(MessageFormat.format(HTML_PATTERN, iconURL.toExternalForm(), text == null ? "" : text));
                }
                break;
        }
        addMouseListener(this);
    }

    public void setActionCommand(String command) {
        this.command = command;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (listener != null) {
            listener.actionPerformed(new ActionEvent(this, 0, command));
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setForeground(highlightedColor == null ? Color.BLUE : highlightedColor);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        setForeground(normalColor == null ? Color.BLACK : normalColor);
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    public void addActionListener(ActionListener listener) {
        this.listener = listener;
    }
}
