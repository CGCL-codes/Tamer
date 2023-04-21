package org.abisso.PortableNotary.GUI;

import java.io.FilterWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import javax.swing.JFrame;
import javax.swing.JTextArea;

public class MyFrameWithConsole extends JFrame {

    private static final long serialVersionUID = 4081975365906005630L;

    private TextAreaPrintStream taps;

    public MyFrameWithConsole(String title) {
        JTextArea consoleTextArea = new JTextArea();
        add(consoleTextArea);
        setTitle(title);
        setSize(500, 200);
        taps = new TextAreaPrintStream(consoleTextArea);
    }

    public TextAreaPrintStream getWriter() {
        return taps;
    }

    private static class TextAreaPrintStream extends FilterWriter {

        private JTextArea consoleTextArea;

        TextAreaPrintStream(JTextArea consoleTextArea) {
            super(new OutputStreamWriter(System.out));
            this.consoleTextArea = consoleTextArea;
        }

        @Override
        public void write(char[] cbuf, int off, int len) throws IOException {
            super.write(cbuf, off, len);
            consoleTextArea.append(cbuf.toString().substring(off, len));
        }

        @Override
        public void write(String str, int off, int len) throws IOException {
            super.write(str, off, len);
            consoleTextArea.append(str.substring(off, len));
        }
    }
}
