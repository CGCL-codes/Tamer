package grape.frontend.view.dialogs;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class TeXportDialog extends JDialog implements ActionListener {

    private static final long serialVersionUID = 1L;

    String code;

    JButton done;

    JButton write;

    public TeXportDialog(JFrame owner, String code) {
        super(owner, "Export TeX code", true);
        setLayout(new BorderLayout());
        this.code = code;
        JTextArea txt = new JTextArea(code, 25, 40);
        txt.setEditable(false);
        JScrollPane p = new JScrollPane(txt);
        add(p, BorderLayout.CENTER);
        JPanel buttonpanel = new JPanel();
        done = new JButton("Done");
        done.addActionListener(this);
        buttonpanel.add(done);
        write = new JButton("Write to File...");
        write.addActionListener(this);
        buttonpanel.add(write);
        add(buttonpanel, BorderLayout.SOUTH);
        pack();
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == done) setVisible(false);
        if (src == write) write();
    }

    private void write() {
        try {
            String filename = "";
            while ((filename != null) && (filename.equals(""))) {
                filename = JOptionPane.showInputDialog(this, "Enter the name of the file to export to: ", "Export File", JOptionPane.PLAIN_MESSAGE);
            }
            OutputStreamWriter w = new OutputStreamWriter(new FileOutputStream(filename));
            w.write(code);
            w.flush();
            JOptionPane.showMessageDialog(this, "TeX export finished!", "Export Finished", JOptionPane.INFORMATION_MESSAGE);
        } catch (FileNotFoundException fnfe) {
            JOptionPane.showMessageDialog(this, "The specified file could not be opened for writing.", "Unable to Open File", JOptionPane.ERROR_MESSAGE);
        } catch (IOException ioe) {
            JOptionPane.showMessageDialog(this, "An error occured while writing to the file.", "I/O Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
