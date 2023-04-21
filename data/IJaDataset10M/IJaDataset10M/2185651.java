package jmax.editors.about;

import java.util.*;
import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import javax.swing.border.*;
import org.jdesktop.swingx.*;
import jmax.registry.*;
import jmax.ui.*;

/** A simple text panel showing the about text. */
class AboutPanel extends JPanel {

    public static final int SCREEN_WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width;

    public static final int SCREEN_HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height;

    public static final String aboutJMax = "<html><body>jMax Phoenix version 0.5 beta 1</body></html>";

    private JEditorPane aboutText;

    private JScrollPane scrollerView;

    private UIContext uiContext;

    AboutPanel(UIContext uiContext) {
        this.uiContext = uiContext;
        setLayout(new BorderLayout());
        aboutText = new JEditorPane();
        aboutText.setEditable(false);
        try {
            String filename = Registry.resolveJMaxURI("jmax://core/docs/about.html");
            aboutText.setPage(new URL("file://" + filename));
        } catch (java.io.IOException e) {
            aboutText.setText(aboutJMax);
        } catch (RegistryException e) {
            aboutText.setText(aboutJMax);
        }
        scrollerView = new JScrollPane();
        scrollerView.setViewportView(aboutText);
        scrollerView.getHorizontalScrollBar().setUnitIncrement(10);
        scrollerView.getVerticalScrollBar().setUnitIncrement(10);
        scrollerView.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollerView.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        add(scrollerView, BorderLayout.CENTER);
        setMinimumSize(new Dimension(0, 0));
        setPreferredSize(new Dimension(SCREEN_WIDTH / 10, SCREEN_HEIGHT / 10));
        setVisible(true);
    }

    public JComponent getContentComponent() {
        return aboutText;
    }
}
