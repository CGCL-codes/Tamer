package Demos;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class TextDemo extends JPanel implements ActionListener {

    protected JTextField textField;

    protected JTextArea textArea;

    private static final String newline = "\n";

    public TextDemo() {
        super(new GridBagLayout());
        textField = new JTextField(20);
        textField.addActionListener(this);
        textArea = new JTextArea(5, 20);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        GridBagConstraints c = new GridBagConstraints();
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.fill = GridBagConstraints.HORIZONTAL;
        add(textField, c);
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;
        add(scrollPane, c);
    }

    public void actionPerformed(ActionEvent evt) {
        String text = textField.getText();
        textArea.append(text + newline);
        textField.selectAll();
        textArea.setCaretPosition(textArea.getDocument().getLength());
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event dispatch thread.
     */
    private static void createAndShowGUI() {
        JFrame frame = new JFrame("TextDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new TextDemo());
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new TextDemo().createAndShowGUI();
    }
}
