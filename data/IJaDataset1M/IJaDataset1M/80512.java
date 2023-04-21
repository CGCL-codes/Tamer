package riafswing;

import java.awt.Component;
import java.awt.Container;
import java.io.File;
import java.util.Arrays;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.filechooser.FileFilter;
import riaf.facade.IComponent;
import riaf.facade.IContainer;
import riaf.facade.IFileChooser;
import riaf.facade.IStyle;
import sun.swing.FilePane;

/**
 * The Class RFileChooser binds Swing's JFileChooser.
 */
public class RFileChooser extends RComponent implements IFileChooser {

    /**
	 * @param id
	 * @param parent
	 */
    public RFileChooser(String id, IContainer parent) {
        super(id, parent);
        JFileChooser res = new JFileChooser();
        res.setDialogType(JFileChooser.CUSTOM_DIALOG);
        res.setAcceptAllFileFilterUsed(false);
        setComponent(res);
        setFilter(null, null);
    }

    @Override
    public String getName() {
        return IFileChooser.name;
    }

    @Override
    public JFileChooser getImpl() {
        return (JFileChooser) super.getImpl();
    }

    @Override
    protected ProcessResult setStyle(IStyle style) {
        if (style == null) return ProcessResult.Failed;
        applyToComponent(style, getImpl());
        setStyle(getImpl(), style);
        setDirty(Boolean.FALSE);
        getImpl().invalidate();
        getImpl().repaint();
        return ProcessResult.Success;
    }

    private void setStyle(Container container, IStyle style) {
        Component[] comps = container.getComponents();
        for (Component c : comps) {
            if (c instanceof JPanel) {
                ((JPanel) c).setOpaque(false);
            } else if (c instanceof Box.Filler) {
                ((Box.Filler) c).setOpaque(false);
            } else if (c instanceof JToolBar) {
                ((JToolBar) c).setOpaque(false);
                JToolBar jtb = (JToolBar) c;
                Component[] tbComps = jtb.getComponents();
                for (Component tbComp : tbComps) {
                    if (tbComp instanceof JComponent) ((JComponent) tbComp).setOpaque(false);
                }
            } else if (c instanceof JLabel) {
                ((JLabel) c).setOpaque(false);
            } else if (c instanceof JButton) {
                applyToComponent(style, c);
            }
            if (c instanceof Container && !(c instanceof FilePane)) {
                setStyle((Container) c, style);
            }
        }
    }

    @Override
    protected void setImplContent(String content) {
        getImpl().setApproveButtonText(removeMnemonicIndicator(content));
    }

    @Override
    public String[] showDialog(IComponent parent, String approveButtonText) {
        int dialogResult;
        String[] result = null;
        if (parent != null) dialogResult = getImpl().showDialog((Component) parent.getImpl(), approveButtonText); else dialogResult = getImpl().showDialog(null, approveButtonText);
        if (dialogResult == JFileChooser.APPROVE_OPTION) {
            if (isMultiSelectionEnabled()) {
                File[] files = getSelectedFiles();
                if (files != null) {
                    result = new String[files.length];
                    for (int i = 0; i < files.length; i++) {
                        result[i] = files[i].getAbsolutePath();
                    }
                }
            } else {
                if (getSelectedFile() != null) {
                    result = new String[1];
                    result[0] = getSelectedFile().getAbsolutePath();
                } else result = new String[0];
            }
        }
        return result;
    }

    @Override
    public String[] showDialog(String approveButtonText) {
        return showDialog(getParent(), approveButtonText);
    }

    @Override
    public void setFilter(String[] extensions, String filterDescription) {
        FileFilter[] filters = getImpl().getChoosableFileFilters();
        for (FileFilter f : filters) {
            getImpl().removeChoosableFileFilter(f);
        }
        RFileFilter filter = new RFileFilter(extensions, filterDescription);
        getImpl().setFileFilter(filter);
    }

    @Override
    public void setSelectedFile(File... file) {
        if (file != null && file.length > 1 && !isMultiSelectionEnabled()) {
            getImpl().setSelectedFile(file[0]);
        } else {
            getImpl().setSelectedFiles(file);
        }
    }

    @Override
    public void setSelectedFile(String... file) {
        if (file == null || file.length == 0) {
            setSelectedFile((File[]) null);
        } else {
            File[] files = new File[file.length];
            for (int i = 0; i < file.length; i++) {
                files[i] = new File(file[i]);
            }
            setSelectedFile(files);
        }
    }

    @Override
    public File getSelectedFile() {
        return getImpl().getSelectedFile();
    }

    @Override
    public File[] getSelectedFiles() {
        return getImpl().getSelectedFiles();
    }

    @Override
    public int getFileSelectionMode() {
        int mode = getImpl().getFileSelectionMode();
        if (mode == JFileChooser.FILES_ONLY) return IFileChooser.FILES_ONLY; else if (mode == JFileChooser.DIRECTORIES_ONLY) return IFileChooser.DIRECTORIES_ONLY; else return IFileChooser.FILES_AND_DIRECTORIES;
    }

    @Override
    public void setFileSelectionMode(int mode) {
        if (mode == FILES_ONLY) getImpl().setFileSelectionMode(JFileChooser.FILES_ONLY); else if (mode == DIRECTORIES_ONLY) getImpl().setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); else if (mode == FILES_AND_DIRECTORIES) getImpl().setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
    }

    @Override
    public boolean isMultiSelectionEnabled() {
        return getImpl().isMultiSelectionEnabled();
    }

    @Override
    public void setMultiSelectionEnabled(boolean b) {
        getImpl().setMultiSelectionEnabled(b);
    }

    @Override
    public void setCurrentDirectory(String directoryPath) {
        getImpl().setCurrentDirectory(new File(directoryPath));
    }

    @Override
    public String getCurrentDirectory() {
        return getImpl().getCurrentDirectory().getPath().replace('\\', '/');
    }

    /**
	 * Gets the extension of a file.
	 * 
	 * @param f
	 *            the file.
	 * @return the extension of the file or <code>null</code> if none.
	 */
    protected static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');
        if (i > 0 && i < s.length() - 1) {
            ext = s.substring(i + 1).toLowerCase();
        }
        return ext;
    }

    protected static class RFileFilter extends FileFilter {

        private String[] extensions;

        private String description;

        /**
		 * Creates a empty filter that accepts all files.
		 */
        public RFileFilter() {
            this(null, null);
        }

        /**
		 * Creates a file filter which accepts only files whose extension is
		 * among the specified extensions.
		 * 
		 * @param extensions
		 *            the extensions to accept.
		 */
        public RFileFilter(String[] extensions) {
            this(extensions, null);
        }

        /**
		 * Creates a file filter which accepts only files whose extension is
		 * among the specified extensions.
		 * 
		 * @param extensions
		 *            the extensions to accept.
		 * @param description
		 *            the description of this file filter.
		 */
        public RFileFilter(String[] extensions, String description) {
            if (extensions != null) this.extensions = Arrays.copyOf(extensions, extensions.length);
            if (description != null) this.description = description; else if (this.extensions != null && extensions.length > 0) {
                StringBuilder builder = new StringBuilder();
                for (String ext : this.extensions) builder.append("*." + ext + ",");
                builder.deleteCharAt(builder.length() - 1);
                this.description = builder.toString();
            }
        }

        @Override
        public boolean accept(File f) {
            if (f.isDirectory() || extensions == null) return true;
            String ext = getExtension(f);
            if (ext == null) return false;
            for (int i = 0; i < extensions.length; i++) {
                if (extensions[i].equalsIgnoreCase(ext)) return true;
            }
            return false;
        }

        @Override
        public String getDescription() {
            return description;
        }
    }
}
