package voxspellcheck;

import javax.swing.*;
import java.awt.Graphics;
import java.awt.geom.*;
import java.awt.Rectangle;
import java.awt.FontMetrics;
import java.awt.Point;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Font;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.Insets;
import java.awt.EventQueue;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.gjt.sp.jedit.AbstractOptionPane;
import org.gjt.sp.jedit.jEdit;
import org.gjt.sp.jedit.View;
import org.gjt.sp.util.Log;

public class VoxSpellOptionPane extends AbstractOptionPane {

    private JTextField all_text_modes;

    private JTextField non_markup_modes;

    private JCheckBox start_checking_on_activate;

    private JCheckBox use_custom_color;

    private Color color;

    private static String color_button_text = "Click here to select a new underline color";

    private class CustomButton extends JButton {

        protected Font font;

        protected int width, height;

        protected int x, y;

        public CustomButton(String title) {
            super(title);
            String s;
            setBackground(jEdit.getColorProperty("view.bgColor"));
            setForeground(jEdit.getColorProperty("view.fgColor"));
            s = jEdit.getProperty("view.font");
            font = new Font(s, 0, getFont().getSize());
            setFont(font);
            Rectangle2D r = font.getStringBounds(color_button_text, new FontRenderContext(null, true, true));
            width = (int) r.getWidth();
            height = (int) r.getHeight();
            this.setSize((int) width + 10, getHeight());
        }

        protected void paintComponent(Graphics g) {
            Graphics2D gfx = (Graphics2D) g;
            gfx.setFont(font);
            super.paintComponent(gfx);
            Rectangle2D font_rect = font.getStringBounds(color_button_text, gfx.getFontRenderContext());
            width = (int) font_rect.getWidth();
            height = (int) font_rect.getHeight();
            x = (int) ((getBounds(null).width / 2.0) - (width / 2.0));
            y = (int) ((getBounds(null).height / 2.0) + (height / 2.0));
            gfx.setColor(color);
            Rectangle r = new Rectangle(x, y, width, 1);
            gfx.fill(r);
        }
    }

    public VoxSpellOptionPane() {
        super("VoxSpellOptionPane");
    }

    public void _init() {
        String s;
        boolean b;
        s = jEdit.getProperty("options.voxspellcheck.all_text_modes");
        all_text_modes = new JTextField(s);
        addComponent(new JLabel("Modes where all text is checked: "), all_text_modes);
        s = jEdit.getProperty("options.voxspellcheck.non_markup_modes");
        non_markup_modes = new JTextField(s);
        addComponent(new JLabel("Modes where only plain text (not syntax highlighted) is checked: "), non_markup_modes);
        s = jEdit.getProperty("options.voxspellcheck.start_checking_on_activate");
        b = s.equals("true");
        s = jEdit.getProperty("options.voxspellcheck.start_checking_on_activate.title");
        start_checking_on_activate = new JCheckBox(s, b);
        addComponent(start_checking_on_activate);
        JPanel panel = new JPanel(new FlowLayout());
        s = jEdit.getProperty("options.voxspellcheck.use_custom_color");
        b = s.equals("true");
        s = jEdit.getProperty("options.voxspellcheck.use_custom_color.title");
        use_custom_color = new JCheckBox(s, b);
        use_custom_color.setActionCommand("enable");
        addComponent(use_custom_color);
        final CustomButton color_button = new CustomButton(color_button_text);
        if (!b) color_button.setEnabled(false);
        addComponent(color_button);
        use_custom_color.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                if (use_custom_color.isSelected()) color_button.setEnabled(true); else color_button.setEnabled(false);
            }
        });
        color = VoxSpellPainter.getUnderlineColor();
        color_button.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                Color tmp_color = JColorChooser.showDialog(color_button, "Select underline color", color);
                if (tmp_color != null) color = tmp_color;
            }
        });
        addComponent(panel);
    }

    public void _save() {
        String s;
        s = all_text_modes.getText();
        jEdit.setProperty("options.voxspellcheck.all_text_modes", s);
        s = non_markup_modes.getText();
        jEdit.setProperty("options.voxspellcheck.non_markup_modes", s);
        s = String.valueOf(start_checking_on_activate.isSelected());
        jEdit.setProperty("options.voxspellcheck.start_checking_on_activate", s);
        s = String.valueOf(use_custom_color.isSelected());
        jEdit.setProperty("options.voxspellcheck.use_custom_color", s);
        jEdit.setColorProperty("options.voxspellcheck.custom_color", color);
    }
}
