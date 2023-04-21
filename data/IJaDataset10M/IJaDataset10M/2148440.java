package be.kuleuven.cs.mop.gui.util;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

/**
 * {@link JFileChooser} extension for XML files
 */
public class XMLFileChooser extends JFileChooser {

    private static final long serialVersionUID = 1L;

    public XMLFileChooser() {
        this(new File("."));
    }

    public XMLFileChooser(final File dir) {
        super(dir);
        setFileFilter(new Filter());
    }

    private static final class Filter extends FileFilter {

        @Override
        public final boolean accept(final File file) {
            return (file.isDirectory() || (file.isFile() && file.getName().endsWith(".xml")));
        }

        @Override
        public final String getDescription() {
            return "XML Files";
        }
    }
}
