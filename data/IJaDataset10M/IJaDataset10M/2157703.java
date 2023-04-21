package net.sf.japi.swing;

import java.awt.BorderLayout;
import java.io.File;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.JFileChooser;

/** A class that displays a textfield for a file and a button for choosing the file.
 * TODO
 * @author <a href="mailto:Christian.Hujer@itcqis.com">Christian Hujer</a>
 */
public final class FileField extends JComponent {

    /** The JTextField used to display the filename.
     * @serial include
     */
    private final JTextField textField;

    /** Create a FileField.
     * @param size Number of columns for the textfield part.
     */
    public FileField(final int size) {
        textField = new JTextField(size);
        final JFileChooserButton button = new JFileChooserButton(textField, JFileChooser.FILES_ONLY);
        setLayout(new BorderLayout());
        add(textField, BorderLayout.CENTER);
        add(button, BorderLayout.LINE_END);
    }

    /** Create a FileField.
     * @param size Number of columns for the textfield part.
     */
    public FileField(final JFileChooser fileChooser, final int size) {
        textField = new JTextField(size);
        final JFileChooserButton button = new JFileChooserButton(fileChooser, textField, JFileChooser.FILES_ONLY);
        setLayout(new BorderLayout());
        add(textField, BorderLayout.CENTER);
        add(button, BorderLayout.LINE_END);
    }

    /** Return the selected file.
     * @return selected file
     */
    public File getSelectedFile() {
        return new File(textField.getText());
    }

    /** Return the selected filename.
     * @return selected filename
     */
    public String getSelectedFilename() {
        return textField.getText();
    }
}
