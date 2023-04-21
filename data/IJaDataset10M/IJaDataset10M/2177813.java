package org.armedbear.j;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;

public final class PasswordDialog extends JDialog implements FocusListener, KeyListener {

    private final Editor editor;

    private final PasswordField textField;

    private String input;

    private PasswordDialog(Editor editor, String prompt, String title) {
        super(editor.getFrame(), title, true);
        this.editor = editor;
        input = null;
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(5, 5, 5, 5));
        Label label = new Label(prompt);
        panel.add(label);
        textField = new PasswordField(20);
        textField.setAlignmentX(LEFT_ALIGNMENT);
        textField.addKeyListener(this);
        panel.add(textField);
        getContentPane().add(panel, BorderLayout.CENTER);
        pack();
        addFocusListener(this);
    }

    public void keyPressed(KeyEvent e) {
        switch(e.getKeyCode()) {
            case KeyEvent.VK_ENTER:
                input = textField.getText();
                dispose();
                return;
            case KeyEvent.VK_ESCAPE:
                dispose();
                return;
        }
    }

    public void keyReleased(KeyEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }

    public static String showPasswordDialog(Editor editor, String prompt, String title) {
        PasswordDialog d = new PasswordDialog(editor, prompt, title);
        editor.centerDialog(d);
        d.show();
        return d.input;
    }

    public void dispose() {
        super.dispose();
        editor.restoreFocus();
    }

    public void focusGained(FocusEvent e) {
        textField.requestFocus();
    }

    public void focusLost(FocusEvent e) {
    }

    private static class PasswordField extends JPasswordField {

        public PasswordField(int columns) {
            super(columns);
            final Preferences preferences = Editor.preferences();
            final String fontName = preferences.getStringProperty(Property.TEXT_FIELD_FONT_NAME);
            if (fontName != null) {
                int fontSize = preferences.getIntegerProperty(Property.TEXT_FIELD_FONT_SIZE);
                if (fontSize == 0) fontSize = preferences.getIntegerProperty(Property.DIALOG_FONT_SIZE);
                setFont(new Font(fontName, Font.PLAIN, fontSize));
            }
        }

        public Dimension getPreferredSize() {
            Dimension size = super.getPreferredSize();
            size.width = getColumns() * 11;
            return size;
        }

        public void paintComponent(Graphics g) {
            Display.setRenderingHints(g);
            super.paintComponent(g);
        }
    }
}
