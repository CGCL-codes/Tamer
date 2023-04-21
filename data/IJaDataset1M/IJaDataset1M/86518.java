package net.sf.cybowmodeller.modelcomposer.view;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author SHIMAYOSHI Takao
 * @version $Revision: 10 $
 */
public final class URLInputPane extends JOptionPane {

    private final JPanel panel = new JPanel();

    private final JTextField textField = new JTextField(32);

    private final JButton browseButton = new JButton("Browse");

    private final JFileChooser chooser = new JFileChooser();

    public URLInputPane() {
        super(null, PLAIN_MESSAGE, OK_CANCEL_OPTION, null);
        browseButton.addActionListener(new BrowseActionListner());
        panel.add(textField);
        panel.add(browseButton);
        setMessage(panel);
        chooser.setCurrentDirectory(new File("."));
        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        chooser.setMultiSelectionEnabled(false);
    }

    public int showDialog(Component parentComponent, String title) {
        final JDialog dialog = createDialog(parentComponent, title);
        dialog.setVisible(true);
        dialog.dispose();
        return ((Integer) getValue()).intValue();
    }

    public URI getURI() throws URISyntaxException {
        return new URI(textField.getText());
    }

    private void browseFileSystem() {
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            textField.setText(chooser.getSelectedFile().toURI().toString());
        }
    }

    private final class BrowseActionListner implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            browseFileSystem();
        }
    }
}
