package rcp;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.text.html.*;

public class SwingTest {

    private static void createPanel(Container pane) {
        final HTMLDocument doc = new HTMLDocument();
        JEditorPane editor = new JEditorPane();
        HTMLEditorKit kit = new HTMLEditorKit();
        editor.setEditorKit(kit);
        editor.setDocument(doc);
        final JLabel label = new JLabel("<html><i>Qwerty</i></html>");
        Button btnOk = new Button("Ok");
        btnOk.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    label.setText(doc.getText(0, 10));
                } catch (BadLocationException e1) {
                    e1.printStackTrace();
                }
            }
        });
        pane.setLayout(new BorderLayout());
        pane.add(BorderLayout.CENTER, editor);
        pane.add(BorderLayout.NORTH, label);
        pane.add(BorderLayout.SOUTH, btnOk);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Test input");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        createPanel(frame.getContentPane());
        frame.setVisible(true);
    }
}
