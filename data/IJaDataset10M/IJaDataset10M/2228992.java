package net.cattaka.rdbassistant.util;

import java.awt.Frame;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import javax.swing.JDialog;
import javax.swing.JTextArea;
import net.cattaka.swing.StdScrollPane;
import net.cattaka.util.MessageBundle;

/**
 * @author cattaka
 */
public class DocumentDialog extends JDialog {

    private static final long serialVersionUID = 9080140024027339670L;

    private StdScrollPane textPane;

    private JTextArea textArea;

    public DocumentDialog(Frame frame, String title, String document) {
        setTitle(title);
        textArea = new JTextArea();
        textPane = new StdScrollPane(textArea, StdScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, StdScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        textArea.setEditable(false);
        getContentPane().add(textPane);
        URL url = DocumentDialog.class.getClassLoader().getResource(document);
        try {
            StringBuilder sb = new StringBuilder();
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
            String buildNumber = MessageBundle.getBuildNumber();
            String releaseNumber = MessageBundle.getReleaseNumber();
            String tmp;
            while ((tmp = in.readLine()) != null) {
                tmp = tmp.replace("${build_number}", buildNumber);
                tmp = tmp.replace("${release_number}", releaseNumber);
                sb.append(tmp + "\n");
            }
            textArea.setText(sb.toString());
            textArea.setCaretPosition(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setVisible(boolean b) {
        super.setVisible(b);
    }
}
