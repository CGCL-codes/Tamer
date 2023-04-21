package net.sf.opendf.plugin.causation;

import javax.swing.JFileChooser;
import net.sf.opendf.plugin.InterpreterFactory;
import net.sf.opendf.plugin.PluginClassLoader;

/**
 * CausationClassLoader gives the CalInterpreterCT class pointer
 * @author Samuel Keller EPFL
 */
public class CausationClassLoader implements PluginClassLoader {

    private JFileChooser dirchooser = new JFileChooser();

    /**
	 * Default constructor
	 */
    public CausationClassLoader() {
        dirchooser.setCurrentDirectory(new java.io.File("."));
        dirchooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        dirchooser.setAcceptAllFileFilterUsed(false);
        dirchooser.setDialogTitle("Select output folder");
    }

    /**
	 * Return if the "Interpreter" class is requested CalInterpreterCT class
	 * @param kind Kind of core Plug-in requested
	 * @return CalInterpreterCT class pointer
	 */
    public Class<?> getPluginClass(String kind) {
        if (kind.equals(InterpreterFactory.kind)) return CalInterpreterCT.class;
        return null;
    }

    /**
	 * Open output folder selection on activation
	 * @param active New status of activation
	 */
    public void activate(boolean active) {
        if (active) {
            String path = "";
            if (dirchooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) path = dirchooser.getSelectedFile().getPath();
            CalInterpreterCT.setPath(path);
            XmlCausationTraceBuilder.setPath(path);
        }
    }
}
