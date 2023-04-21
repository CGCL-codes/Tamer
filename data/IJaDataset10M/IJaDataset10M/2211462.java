package plp_converter.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * This listener class exports PLP files, by first invoking the preview
 * listener, copying its output and then writing it to a file.
 */
public class PLPExportListener implements ActionListener {

    private JButton previewbutton = null;

    private JTextArea previewarea = null;

    private JTextField out = null;

    public PLPExportListener(JButton jb, JTextArea jta, JTextField jtf) {
        previewbutton = jb;
        previewarea = jta;
        out = jtf;
    }

    public void actionPerformed(ActionEvent ae) {
        try {
            previewbutton.doClick();
            if ((previewarea.getText().compareTo("") == 0) || (out.getText().compareTo("PLP file to export to") == 0)) JOptionPane.showMessageDialog(null, "Please do properly select your input/output paths.", "File path(s) missing", JOptionPane.ERROR_MESSAGE); else {
                StringBuffer sb = new StringBuffer(previewarea.getText());
                int length = sb.length();
                FileOutputStream fos = new FileOutputStream(out.getText());
                BufferedOutputStream bos = new BufferedOutputStream(fos);
                for (int i = 0; i < length; i++) {
                    if (sb.charAt(i) != '\n') {
                        bos.write((int) sb.charAt(i));
                        bos.write(0);
                    } else {
                        bos.write((int) '\r');
                        bos.write(0);
                        bos.write((int) '\n');
                        bos.write(0);
                    }
                }
                bos.close();
                fos.close();
                JOptionPane.showMessageDialog(null, "PLP file exported.", "Success!", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (IOException ioe) {
            JOptionPane.showMessageDialog(null, "Failed to write to selected file.", "IOException", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Unknown Error.", "Exception", JOptionPane.ERROR_MESSAGE);
        }
    }
}
