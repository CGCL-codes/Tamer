package opt.base;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JTextField;

public class SingleDirOption extends SingleStringOption {

    private JButton browseButton;

    /**
	 * 
	 */
    public SingleDirOption() {
        super();
        initialize();
    }

    /**
	 * @param valueKey
	 * @param displayLabel
	 * @param defaultValue
	 * @param optionValue
	 */
    public SingleDirOption(String valueKey, String displayLabel, String defaultValue, String optionValue) {
        super(valueKey, displayLabel, defaultValue, optionValue);
        initialize();
    }

    /**
	 * @param valueKey
	 * @param displayLabel
	 * @param defaultValue
	 */
    public SingleDirOption(String valueKey, String displayLabel, String defaultValue) {
        super(valueKey, displayLabel, defaultValue);
        initialize();
    }

    private void initialize() {
        browseButton = new JButton("...");
        this.browseButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                executeBrowse();
            }
        });
    }

    private void executeBrowse() {
        JTextField jtb = (JTextField) getGUIObject();
        File newDir = chooseDir(jtb.getText());
        if (newDir != null) jtb.setText(newDir.getPath());
    }

    private File chooseDir(String currentDir) {
        int returnVal = 0;
        File retFile;
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setCurrentDirectory(new File(currentDir));
        returnVal = chooser.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            retFile = chooser.getSelectedFile();
        } else {
            retFile = null;
        }
        return (retFile);
    }

    public String getDatatype() {
        return ("directory");
    }

    public JButton getBrowseButton() {
        return (browseButton);
    }
}
