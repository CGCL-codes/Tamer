package de.fhg.igd.semoa.bin.starter;

import java.io.File;
import javax.swing.border.TitledBorder;
import javax.swing.BorderFactory;
import javax.swing.filechooser.FileFilter;
import javax.swing.JFileChooser;

/**
 * This component allows users to select a {@link SemoaLogDirectory}.
 * This can be accomplished by either directly using the component's text
 * input field or by means of the provided file browser.
 *
 * @author Matthias Pressfreund
 * @version "$Id: SemoaLogDirectoryChooser.java 1563 2005-03-24 17:47:32Z jpeters $"
 */
public class SemoaLogDirectoryChooser extends InputFileChooser {

    /**
     * Create a <code>SemoaLogDirectoryChooser</code>.
     */
    public SemoaLogDirectoryChooser() {
        super();
        TitledBorder border;
        border = BorderFactory.createTitledBorder("Log Directory");
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
                SemoaLogDirectory slog;
                String str;
                try {
                    str = ConverterPanel.DEFAULT_CONVERTER_.convert(o).toString();
                    slog = str.length() > 0 ? SemoaLogDirectory.create(str) : null;
                } catch (IllegalArgumentException e) {
                    throw new ConverterException("No SeMoA Logging Directory", "The selected directory could not be" + "\nidentified as a SeMoA logging directory");
                }
                return slog;
            }
        });
        setSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    }

    protected String getTextFieldHelp() {
        return ("<html>" + "<center>" + "<p><b>The SeMoA Log Directory Input Field</b></p>" + "</center>" + "<p>Enter the path to the <i>SeMoA</i> Log Directory.</p>" + "<p>The input will be accessible via the<br>" + "<i>" + SemoaLogDirectory.PROPERTY + "</i> " + "system property and may be<br>" + "used for dynamic path definitions in the<br>" + "logging configuration file.</p>" + super.getTextFieldHelp() + "</html>");
    }

    protected String getBrowseButtonHelp() {
        return ("<html>" + "<center>" + "<p><b>The SeMoA Log Directory Browser</b></p>" + "</center>" + "<p>Browse the file system for the <i>SeMoA</i> Log<br>" + "Directory.</p>" + "</html>");
    }
}
