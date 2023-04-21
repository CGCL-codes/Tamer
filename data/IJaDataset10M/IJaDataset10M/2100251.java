package test.xito.dialog;

import java.awt.*;
import java.io.*;
import java.net.*;
import javax.swing.*;
import org.xito.dialog.*;

public class CodeViewerDialog extends CustomDialog {

    protected String source;

    /** Creates a new instance of CodeViewerDialog */
    public CodeViewerDialog(Frame f, String source) {
        super(f);
        this.source = source;
        super.descriptor = new DialogDescriptor();
        descriptor.setWindowTitle("Example Code Viewer");
        descriptor.setTitle("Example Code Viewer");
        descriptor.setSubtitle("Code Source for Dialog Example");
        descriptor.setCustomPanel(new MainPanel());
        descriptor.setWidth(500);
        descriptor.setHeight(400);
        descriptor.setResizable(true);
        descriptor.setButtonTypes(new ButtonType[] { new ButtonType("Close", 1) });
        super.init();
        setModal(true);
    }

    public class MainPanel extends TablePanel {

        private JTextArea contentPane;

        public MainPanel() {
            init();
        }

        private void init() {
            try {
                setLayout(new TableLayout(CodeViewerDialog.class.getResource("dialog_test_layout.html")));
                add("description", new JLabel("<html>Code Example for Code:" + source + "</html>"));
                contentPane = new JTextArea();
                contentPane.setEditable(false);
                add("content", new JScrollPane(contentPane));
                URL sourceURL = CodeViewerDialog.class.getResource(source);
                if (sourceURL == null) System.out.println("Can't load source:" + source);
                StringBuffer strBuf = new StringBuffer();
                byte[] buf = new byte[1024];
                InputStream in = sourceURL.openStream();
                int c = in.read(buf);
                while (c != -1) {
                    strBuf.append(new String(buf, 0, c));
                    c = in.read(buf);
                }
                contentPane.setText(strBuf.toString());
            } catch (IOException ioExp) {
                ioExp.printStackTrace();
                contentPane.setText("Source content:" + source + " could not be loaded.");
            }
        }
    }
}
