package atv;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**

@author Christian Zmasek

@version 1.02 last modified: 06/20/00


*/
class ATVtextframe extends JFrame implements ActionListener {

    public String text;

    private JTextArea jtextarea;

    private JButton close_button, copy_button;

    private JPanel buttonjpanel;

    private static Color ta_text_color = new Color(0, 0, 0), ta_background_color = new Color(240, 240, 240), background_color = new Color(215, 215, 215), button_background_color = new Color(215, 215, 215), button_text_color = new Color(0, 0, 0);

    private static final Font button_font = new Font("Helvetica", Font.PLAIN, 9), ta_font = new Font("Helvetica", Font.PLAIN, 9);

    private Container contentpane;

    ATVtextframe(String s, String title) {
        setTitle("Comments " + title);
        text = s;
        close_button = new JButton("          Close          ");
        copy_button = new JButton("Copy to clipboard");
        setBackground(background_color);
        close_button.setBackground(button_background_color);
        close_button.setForeground(button_text_color);
        close_button.setFont(button_font);
        copy_button.setBackground(button_background_color);
        copy_button.setForeground(button_text_color);
        copy_button.setFont(button_font);
        close_button.addActionListener(this);
        copy_button.addActionListener(this);
        contentpane = getContentPane();
        contentpane.setLayout(new BorderLayout());
        jtextarea = new JTextArea(text);
        jtextarea.setBackground(ta_background_color);
        jtextarea.setForeground(ta_text_color);
        jtextarea.setFont(ta_font);
        jtextarea.setEditable(false);
        jtextarea.setWrapStyleWord(true);
        jtextarea.setLineWrap(true);
        contentpane.add(new JScrollPane(jtextarea), BorderLayout.CENTER);
        buttonjpanel = new JPanel();
        buttonjpanel.setBackground(background_color);
        buttonjpanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 5));
        buttonjpanel.add(close_button);
        buttonjpanel.add(copy_button);
        contentpane.add(buttonjpanel, BorderLayout.SOUTH);
        setSize(500, 400);
        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                close();
            }
        });
        setVisible(true);
    }

    void close() {
        setVisible(false);
        dispose();
    }

    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();
        if (o == close_button) {
            close();
        } else if (o == copy_button) {
            jtextarea.copy();
        }
    }
}
