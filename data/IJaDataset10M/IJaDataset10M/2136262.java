package ch.tarnet.library.swing;

import java.awt.Dimension;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;

public class TDebugConsole extends JFrame {

    JTextArea area;

    public TDebugConsole() {
        super("Debug console");
        setName("debug.outConsole");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        buildGUI();
        linking();
    }

    private void linking() {
        try {
            PipedInputStream pin = new PipedInputStream();
            PipedOutputStream pout = new PipedOutputStream(pin);
            System.setOut(new PrintStream(pout, true));
            final BufferedReader outReader = new BufferedReader(new InputStreamReader(pin));
            pin = new PipedInputStream();
            pout = new PipedOutputStream(pin);
            System.setErr(new PrintStream(pout, true));
            final BufferedReader errReader = new BufferedReader(new InputStreamReader(pin));
            Thread outThread = new Thread(new Runnable() {

                @Override
                public void run() {
                    String line = null;
                    try {
                        while ((line = outReader.readLine()) != null) {
                            int offset = area.getDocument().getEndPosition().getOffset();
                            area.getDocument().insertString(offset, line + "\n", null);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (BadLocationException e) {
                        e.printStackTrace();
                    }
                }
            });
            outThread.setDaemon(true);
            outThread.start();
        } catch (Exception e) {
        }
    }

    private void buildGUI() {
        area = new JTextArea();
        area.setEditable(false);
        getContentPane().add(new JScrollPane(area));
        setPreferredSize(new Dimension(400, 300));
        pack();
    }
}
