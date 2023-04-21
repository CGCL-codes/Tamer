package projectviewer.views.defaultview;

import java.io.File;
import javax.swing.*;
import org.gjt.sp.jedit.View;
import org.mobix.xml.XmlWriteContext;
import org.xml.sax.SAXException;
import projectviewer.*;
import projectviewer.ui.Actions;
import projectviewer.views.BaseView;

/**
 * A file view that displays files from a root directory.
 */
public class DefaultView extends BaseView {

    private File root;

    /**
    * Create a new <code>DefaultView</code>.
    */
    public DefaultView() {
        super("Default");
        root = new File(System.getProperty("user.dir"));
    }

    /**
    * Returns <code>true</code> if the given file is a project file under this
    * view.
    *
    * <p>SPECIFIED ID: projectviewer.FileView</p>
    */
    public boolean isFileInView(File file) {
        ProjectDirectory parentDir = findDirectory(file.getParentFile());
        if (parentDir == null) return false;
        return parentDir.containsFile(file.getName());
    }

    /**
    * Find a project file for the given path, returning <code>null</code> if it
    * doesn't exist.
    */
    public ProjectFile findProjectFile(String path) {
        File file = new File(path);
        ProjectDirectory parent = findDirectory(file.getParentFile());
        if (parent == null) return null;
        return parent.getFile(file.getName());
    }

    /**
    * Configure this view.
    *
    * <p>SPECIFIED ID: projectviewer.FileView</p>
    */
    public void config(View view, Project project) {
        String value = JOptionPane.showInputDialog(view, "Specify view name");
        if (value == null) return;
        setName(value);
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Select a root directory for this view");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (chooser.showOpenDialog(view) == JFileChooser.CANCEL_OPTION) return;
        root = chooser.getSelectedFile();
        ImportAction.importFiles(view, this);
    }

    /**
    * Add a project file to this view.  This differs from {@link addFile(File)} in that
    * this method will build a directory structure that mirrors the file's physical
    * structure, starting from the view's root.
    *
    * <p>SPECIFIED IN: {@link projectviewer.views.AddFileSupport}</p>
    */
    public ProjectFile addProjectFile(File fileObj) {
        ProjectDirectory dir = ensureDirectory(fileObj.getParentFile());
        if (dir != null && !dir.containsFile(fileObj.getName())) return dir.addFile(fileObj);
        return null;
    }

    /**
    * Returns a map of actions that are available for artifacts of this view.
    * If <code>null</code> is returned, there are no additional actions.
    *
    * <p>SPECIFIED ID: projectviewer.FileView</p>
    */
    public Actions getActions() {
        Actions actions = new Actions();
        actions.addFileViewAction(new AddCurrentBufferAction());
        actions.addFileViewAction(new SynchronizeAction());
        actions.addFileViewAction(new ImportAction());
        actions.addProjectDirectoryAction(new AddProjectFileAction());
        return actions;
    }

    /**
    * Save this view.
    *
    * <p>SPECIFIED ID: projectviewer.FileView</p>
    */
    public void save(XmlWriteContext xmlWrite) throws SAXException {
        xmlWrite.startElement("view", createViewXmlAttributes());
        writeParamElement(xmlWrite, "root", root.getAbsolutePath());
        saveChildren(this, xmlWrite);
        xmlWrite.endElement("view");
    }

    /**
    * Load a parameter for this view.
    */
    public void setInitParameter(String name, String value) {
        if ("root".equals(name)) {
            root = new File(value);
        }
    }

    /**
    * Returns the project root.
    */
    protected File getProjectRoot() {
        return root;
    }

    /**
    * Set the project root directory.
    */
    protected void setProjectRoot(File aRoot) {
        root = aRoot;
    }

    /**
    * Finds a {@link ProjectDirectory} that represents the given directory, returning
    * <code>null</code> if one doesn't exist.
    */
    protected ProjectDirectory findDirectory(File dir) {
        if (dir == null) return null;
        if (root.equals(dir)) return this;
        ProjectDirectory parentDir = findDirectory(dir.getParentFile());
        if (parentDir == null) return null;
        ProjectDirectory targetDir = parentDir.getDirectory(dir.getName());
        return (targetDir == null) ? null : targetDir;
    }

    /**
    * Finds a the directory for the given path, building the directory structure
    * if necessary.
    */
    protected ProjectDirectory ensureDirectory(File dir) {
        if (dir == null) return null;
        if (root.equals(dir)) return this;
        ProjectDirectory parentDir = ensureDirectory(dir.getParentFile());
        if (parentDir == null) return null;
        ProjectDirectory targetDir = parentDir.getDirectory(dir.getName());
        if (targetDir == null) targetDir = parentDir.addDirectory(dir);
        return targetDir;
    }

    /**
    * An action to add file in the current buffer to the project.
    */
    private class AddCurrentBufferAction extends ActionBase {

        /**
       * Create a new <code>AddCurrentBufferAction</code>.
       */
        public AddCurrentBufferAction() {
            super("add-buffer");
        }

        /**
       * Perform the action.  Any {@link ProjectException}s thrown will be handled
       * appropriately.
       */
        protected void performAction() throws ProjectException {
            ProjectFile file = addProjectFile(new File(projectViewer.getView().getBuffer().getPath()));
            if (file != null) projectViewer.getTreeModel().nodeStructureChanged(file.getParent());
        }
    }

    /**
    * An action to synchronize the files in this view with the actual file in
    * the file system.
    */
    private class SynchronizeAction extends ActionBase implements ArtifactTreeWalker.Evaluator {

        /**
       * Create a new <code>ActionBase</code>.
       */
        public SynchronizeAction() {
            super("synchronize-view");
        }

        /**
       * Evaluate the visited artifact node.
       */
        public int evaluate(ProjectArtifact artifact) {
            if (artifact == DefaultView.this) return ArtifactTreeWalker.EVAL_CHILDREN;
            if (artifact instanceof ProjectFile) {
                ProjectFile file = (ProjectFile) artifact;
                if (!file.toFile().exists()) {
                    removeProjectFile(file);
                }
            } else if (artifact instanceof ProjectDirectory) {
                ProjectDirectory dir = (ProjectDirectory) artifact;
                if (!new File(dir.getPath()).exists()) {
                    removeProjectDirectory(dir);
                    return ArtifactTreeWalker.SKIP_CHILDREN;
                }
            }
            return ArtifactTreeWalker.EVAL_CHILDREN;
        }

        /**
       * Perform the action.  Any {@link ProjectException}s thrown will be handled
       * appropriately.
       */
        protected void performAction() throws ProjectException {
            ArtifactTreeWalker walker = new ArtifactTreeWalker(DefaultView.this, this);
            walker.walk();
            projectViewer.getTreeModel().nodeStructureChanged(DefaultView.this);
        }
    }
}
