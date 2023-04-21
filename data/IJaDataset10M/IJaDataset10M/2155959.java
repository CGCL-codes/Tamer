package DE.FhG.IGD.semoa.bin.starter;

import java.io.File;
import javax.swing.border.TitledBorder;
import javax.swing.BorderFactory;
import javax.swing.filechooser.FileFilter;
import javax.swing.JFileChooser;

/**
 * This component allows users to select a {@link SemoaBaseDirectory}.
 * This can be accomplished by either using the component's text input field,
 * directly or by means of the file browser, or by selecting one of
 * the previously validated entries in the drop-down menu.
 *
 * @author Matthias Pressfreund
 * @version "$Id$"
 */
public class SemoaBaseChooser extends ComboInputFileChooser {

    /**
     * Create a <code>SemoaBaseChooser</code>.
     *
     * @param dirs The directories that will preloaded into the
     *   drop-down menu
     */
    public SemoaBaseChooser(SemoaBaseDirectory[] dirs) {
        super(dirs);
        TitledBorder border;
        border = BorderFactory.createTitledBorder("SeMoA Base Directory");
        border.setTitleJustification(TitledBorder.CENTER);
        setBorder(border);
        setFileFilters(new FileFilter[] { new FileFilter() {

            public boolean accept(File f) {
                return f.isDirectory();
            }

            public String getDescription() {
                return "Directories Only";
            }
        } });
        setConverter(new Converter() {

            public Object convert(Object o) throws ConverterException {
                SemoaBaseDirectory sbase;
                String str;
                try {
                    str = ConverterPanel.DEFAULT_CONVERTER_.convert(o).toString();
                    sbase = str.length() > 0 ? SemoaBaseDirectory.create(str, true) : null;
                } catch (IllegalArgumentException e) {
                    throw new ConverterException("No SeMoA Base Directory", "The selected directory could not be" + "\nidentified as a SeMoA base directory");
                }
                return sbase;
            }
        });
        setSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    }

    protected String getTextFieldHelp() {
        return ("<html>" + "<center>" + "<p><b>The SeMoA Base Input Field</b></p>" + "</center>" + "<p>Enter the path to the <i>SeMoA</i> Base directory.</p>" + "<p>Valid directories contain at least the following<br>" + "required sub-directories: " + "<tt>etc</tt>, <tt>ext</tt> and <tt>lib</tt>.</p>" + super.getTextFieldHelp() + "</html>");
    }

    protected String getBrowseButtonHelp() {
        return ("<html>" + "<center>" + "<p><b>The SeMoA Base Browser</b></p>" + "</center>" + "<p>Browse the file system for the <i>SeMoA</i> Base<br>" + "directory.</p>" + "<p>Valid directories contain at least the following<br>" + "required sub-directories: " + "<tt>etc</tt>, <tt>ext</tt> and <tt>lib</tt>.</p>" + "</html>");
    }

    protected String getComboBoxHelp() {
        return ("<html>" + "<center><p><b>The SeMoA Base Dropdown</b></p></center>" + "<p>Choose from previously detected and<br>" + "validated <i>SeMoA</i> Base directories.</p>" + super.getComboBoxHelp() + "</html>");
    }
}
