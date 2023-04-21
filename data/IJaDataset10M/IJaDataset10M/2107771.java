package svc.core.devices.console;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * A simple console dumps information and receive keyboard inputs.
 * 
 */
public class Console extends JFrame {

    private static final long serialVersionUID = -6711420234061440072L;

    private JTextArea contentArea;

    private JScrollPane wrapper;

    public Console() {
        initUI();
    }

    private void initUI() {
        setTitle("Simple Console");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(700, 400);
        setLayout(new BorderLayout());
        contentArea = new JTextArea();
        contentArea.setLineWrap(true);
        contentArea.setEditable(false);
        wrapper = new JScrollPane(contentArea);
        wrapper.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(wrapper, BorderLayout.CENTER);
    }

    public void open() {
        setVisible(true);
    }

    public void append(String text) {
        contentArea.append(text);
        contentArea.select(contentArea.getDocument().getLength(), contentArea.getDocument().getLength());
    }

    void append(byte[] b) {
        contentArea.append(new String(b));
        contentArea.select(contentArea.getDocument().getLength(), contentArea.getDocument().getLength());
    }

    void append(byte[] b, int off, int len) {
        byte[] temp = new byte[len];
        System.arraycopy(b, off, temp, 0, len);
        contentArea.append(new String(temp));
        contentArea.select(contentArea.getDocument().getLength(), contentArea.getDocument().getLength());
    }

    void append(int i) {
        contentArea.append(String.valueOf(i));
        contentArea.select(contentArea.getDocument().getLength(), contentArea.getDocument().getLength());
    }
}
