package net.sourceforge.picdev.components;

import net.sourceforge.picdev.core.Component;
import net.sourceforge.picdev.core.SharedComponentView;
import net.sourceforge.picdev.ui.NoEditTextArea;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileInputStream;

public abstract class DebugToolWindow implements MouseListener, ActionListener, SharedComponentView {

    NoEditTextArea textarea;

    NoEditTextArea adresses;

    NoEditTextArea adresses2;

    JInternalFrame changer;

    JTextField text;

    JButton ok;

    int adress;

    protected JPanel content;

    long firstClickTime;

    private PIC pic;

    public DebugToolWindow() {
    }

    public void createWindow() {
        content = new JPanel();
        content.setBackground(Color.white);
        content.setLayout(new BorderLayout());
        Font font;
        font = new Font("Courier New", Font.PLAIN, 12);
        try {
            try {
                Font temp = Font.createFont(Font.TRUETYPE_FONT, new FileInputStream("fonts/cour.ttf"));
                font = temp.deriveFont(Font.PLAIN, 12);
            } catch (FontFormatException ffe) {
                System.out.println("Font problem");
            }
        } catch (java.io.IOException fnfe) {
            System.out.println("Fontfil problem");
        }
        textarea = new NoEditTextArea(" ");
        textarea.setForeground(Color.blue);
        textarea.setFont(font);
        textarea.setEnabled(false);
        textarea.setDisabledTextColor(Color.blue);
        adresses = new NoEditTextArea();
        adresses2 = new NoEditTextArea();
        adresses.setMargin(new Insets(0, 5, 0, 0));
        adresses2.setMargin(new Insets(0, 5, 0, 0));
        adresses.setFont(font);
        adresses2.setFont(font);
        adresses.setBackground(Color.gray);
        adresses2.setBackground(Color.gray);
        adresses.setEnabled(false);
        adresses2.setEnabled(false);
        adresses.setDisabledTextColor(Color.black);
        adresses2.setDisabledTextColor(Color.black);
        textarea.addMouseListener(this);
    }

    public void display(String output) {
        textarea.setText(output);
    }

    public void mouseClicked(MouseEvent evt) {
        long clickTime = System.currentTimeMillis();
        firstClickTime = clickTime;
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == ok) {
            try {
                int data = Integer.parseInt(text.getText());
                if (data < 256) {
                    changer.setVisible(false);
                }
            } catch (NumberFormatException e) {
            }
        }
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public synchronized PIC getPic() {
        return pic;
    }

    public synchronized Component getComponent() {
        return pic;
    }

    public JComponent getJComponent() {
        return content;
    }

    public synchronized boolean setComponent(Component component) {
        if (component instanceof PIC) {
            this.pic = (PIC) component;
            return true;
        } else {
            return false;
        }
    }
}
