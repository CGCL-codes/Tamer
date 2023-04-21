package ar.com.coonocer.CodingJoy.model;

import org.apache.log4j.Logger;
import org.hibernate.util.EqualsHelper;

public class Project {

    private static Logger logger = Logger.getLogger(Project.class);

    private TreeNode model = null;

    private TreeNode plugins = null;

    private String folderName = null;

    public Project(TreeNode model, TreeNode plugins) {
        this.model = model;
        this.plugins = plugins;
    }

    public TreeNode getModel() {
        return model;
    }

    public void setModel(TreeNode model) {
        this.model = model;
    }

    public int hashCode() {
        return model != null ? model.hashCode() : 0;
    }

    public boolean equals(Object o) {
        if (!(o instanceof Project)) {
            return false;
        }
        Project oo = (Project) o;
        if (!EqualsHelper.equals(model, oo.model)) {
            logger.debug("TreeNode: Different Models");
            return false;
        }
        return true;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public TreeNode getPlugins() {
        return plugins;
    }

    public void setPlugins(TreeNode plugins) {
        this.plugins = plugins;
    }
}
