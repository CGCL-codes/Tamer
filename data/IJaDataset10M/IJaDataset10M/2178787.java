package org.bresearch.websec.net.win;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

public class FrameBuilder {

    public JButton buildButton() {
        final JButton button = new JButton("Execute");
        return button;
    }

    public JTextArea buildTextArea() {
        final JTextArea area = new JTextArea("Response:");
        area.setColumns(60);
        area.setLineWrap(false);
        area.setRows(24);
        area.setEditable(false);
        area.setCaretPosition(0);
        return area;
    }

    public JTextField buildTextField() {
        final JTextField text = new JTextField("http://www.botnode.com");
        return text;
    }

    public JFrame buildFrame() {
        final ConnectFrame frame = new ConnectFrame(buildTextField(), buildTextArea(), buildButton());
        frame.setLocation(400, 400);
        frame.setResizable(false);
        frame.loadFrame();
        frame.pack();
        frame.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        return frame;
    }

    public JFrame buildFileLoadFrame() {
        final ConnectFrame frame = new ConnectFileLoadFrame(buildTextField(), buildTextArea(), buildButton());
        frame.setLocation(400, 400);
        frame.setResizable(false);
        frame.loadFrame();
        frame.pack();
        frame.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        return frame;
    }
}
