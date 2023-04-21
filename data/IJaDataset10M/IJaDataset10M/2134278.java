package de.kout.wlFxp.view;

import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import de.kout.wlFxp.wlFxp;

/**
 * a little editor for text files
 *
 * @author Alexander Kout
 *
 * 11. August 2003
 */
class Editor extends JFrame {

    JScrollPane scrollPane;

    JTextPane text;

    private Charset charset;

    private CharsetDecoder decoder;

    private ByteBuffer buf = ByteBuffer.allocateDirect(128000);

    /**
         * Constructor for the Editor object
         * 
         * @param arg
         *            the file to open
         * @param frame
         *            the MainFrame object
         * @param s
         *
         */
    public Editor(String arg, MainFrame frame, StringBuffer s) {
        charset = Charset.forName(wlFxp.getConfig().locale());
        decoder = charset.newDecoder();
        text = new JTextPane();
        text.setEditable(false);
        text.setFont(new Font("SansSerif", Font.PLAIN, 12));
        if (arg != null) {
            s = new StringBuffer(16000);
            try {
                FileInputStream fis = new FileInputStream(new File(arg));
                FileChannel fc = fis.getChannel();
                CharBuffer cb;
                while (fc.read(buf) != -1) {
                    buf.flip();
                    cb = decoder.decode(buf);
                    s.append(cb);
                    buf.clear();
                }
            } catch (Exception e) {
            }
        }
        text.setText(s.toString());
        scrollPane = new JScrollPane(text);
        getContentPane().add(scrollPane);
        setSize(500, 600);
        setLocationRelativeTo(frame);
        text.setCaretPosition(0);
        setTitle(arg);
        setVisible(true);
    }
}
