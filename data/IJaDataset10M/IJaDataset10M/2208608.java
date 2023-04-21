package TextEditTest;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.ReplicateScaleFilter;

public class TextEditFrame extends JFrame {

    private JTextField from;

    private int default_width = 300;

    private int default_height = 300;

    private JTextField to;

    private JTextArea textArea;

    public TextEditFrame() {
        setTitle("Text edit frame");
        setSize(default_width, default_height);
        Container contentPane = getContentPane();
        JPanel panel = new JPanel();
        JButton replaceButton = new JButton("Replace");
        panel.add(replaceButton);
        replaceButton.addActionListener(new ReplaceAction());
        from = new JTextField("brown", 8);
        panel.add(from);
        panel.add(new JLabel("with"));
        to = new JTextField("purple", 8);
        panel.add(to);
        contentPane.add(panel, BorderLayout.SOUTH);
        textArea = new JTextArea(8, 40);
        textArea.setText("Text");
        JScrollPane scrollPane = new JScrollPane(textArea);
        contentPane.add(scrollPane, BorderLayout.CENTER);
    }

    private class ReplaceAction implements ActionListener {

        public void actionPerformed(ActionEvent event) {
            String f = from.getText();
            int n = textArea.getText().indexOf(f);
            if (n > 0 && f.length() > 0) ;
            textArea.replaceRange(to.getText(), n, n + f.length());
        }
    }
}
