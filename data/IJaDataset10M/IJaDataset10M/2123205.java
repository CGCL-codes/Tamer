package net.bpfurtado.tas.builder;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import javax.swing.BorderFactory;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import net.bpfurtado.tas.view.TextComponentUtils;

public class TextAreaWidgetFactory {

    static ScrollTextArea create(Font FONT, final Toolkit toolkit) {
        ScrollTextArea sta = new ScrollTextArea();
        final JTextArea ta = new JTextArea();
        ta.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        ta.setFont(FONT);
        ta.setLineWrap(true);
        ta.setWrapStyleWord(true);
        JScrollPane sceneTAScrollPane = new JScrollPane(ta);
        sceneTAScrollPane.setPreferredSize(new Dimension(690, 800));
        JPopupMenu popup = TextComponentUtils.addCopyAndPaste(toolkit, ta);
        sta.textArea = ta;
        sta.scrollPane = sceneTAScrollPane;
        sta.popup = popup;
        return sta;
    }
}

class ScrollTextArea {

    JTextArea textArea;

    JScrollPane scrollPane;

    JPopupMenu popup;
}
