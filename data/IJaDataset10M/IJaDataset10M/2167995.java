package guiutil;

import config.LanguageFileReader;
import javax.swing.JFrame;
import javax.swing.JTextField;

/**
 *
 * The FileChooser sets the selected file on a JTextField
 */
public class JFileChooserJTextField extends javax.swing.JFileChooser {

    private int option;

    private JTextField jTextField;

    private LanguageFileReader languageFileReader;

    /**
 * Create new JFileChooserJTextField with the specific JFileChooserJTextField
 * @param jTextField
 */
    public JFileChooserJTextField(JTextField jTextField) {
        super();
        this.jTextField = jTextField;
        languageFileReader = new LanguageFileReader();
        setDialogTitle(languageFileReader.assign("jfilechooserjtextfield_dialogtitle"));
        setFileFilter(new TxtFileFilter());
        int option = showOpenDialog(new JFrame(languageFileReader.assign("jfilechooserjtextfield_frametitle")));
        if (APPROVE_OPTION == option) {
            jTextField.setText(getSelectedFile().getAbsolutePath() + ".txt");
        } else {
            setVisible(false);
        }
    }
}
