package br.org.dbt.view;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Console extends JInternalFrame {

    private static final long serialVersionUID = 1L;

    private JTextArea textArea = new JTextArea();

    private PrintStream out;

    public Console() {
        super("Console", true, false, true, true);
        OutputStream os = new OutputStream() {

            @Override
            public void write(int b) throws IOException {
                textArea.append(String.valueOf((char) b));
            }
        };
        out = new PrintStream(os);
        System.setOut(out);
        getContentPane().add(new JScrollPane(textArea));
        setBounds(0, 440, 915, 200);
        textArea.setEditable(false);
    }
}
