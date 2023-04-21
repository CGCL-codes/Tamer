package kellinwood.meshi.build.tables;

import org.apache.tools.ant.Task;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.FileSet;
import kellinwood.hibernate_util.*;
import java.util.*;
import java.io.*;

/**
 *
 * @author ken
 */
public class TableConfigMakerTask {

    /**
     * Holds value of property project.
     */
    private Project project;

    /**
     * Holds value of property targetPackage.
     */
    private String targetPackage;

    /**
     * Holds value of property destDir.
     */
    private String destDir;

    /**
     * Holds value of property hibernatePropsFile.
     */
    private String hibernatePropsFile;

    private List taskTableList = new ArrayList();

    /** Creates a new instance of XMLMergeTask */
    public TableConfigMakerTask() {
    }

    public String makeAbsolute(String pathname) {
        if (pathname == null || new File(pathname).isAbsolute()) return pathname;
        return new File(project.getBaseDir().getAbsolutePath(), pathname).getAbsolutePath();
    }

    public void execute() throws BuildException {
        if (targetPackage == null || destDir == null || hibernatePropsFile == null) {
            System.out.println("Missing one or more required parameters: query, targetClass, destDir, hibernatePropsFile");
        }
        try {
            System.setProperty("hibernate.props", hibernatePropsFile);
            HibernateUtil.initHibernate();
            for (Iterator i = this.taskTableList.iterator(); i.hasNext(); ) {
                TaskTable tt = (TaskTable) i.next();
                TableConfigMaker maker = new TableConfigMaker();
                System.out.println("targetPackage=" + targetPackage);
                System.out.println("query=" + tt.getQuery());
                System.out.println("name=" + tt.getName());
                maker.makeTable(targetPackage, tt.getQuery(), tt.getName());
                maker.marshal(destDir);
            }
        } catch (Exception x) {
            if (x.getMessage() == null) {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                x.printStackTrace(pw);
                throw new BuildException(sw.toString());
            } else throw new BuildException(x.getMessage());
        }
    }

    /**
     * Setter for property project.
     * @param project New value of property project.
     */
    public void setProject(Project project) {
        this.project = project;
    }

    /**
     * Getter for property genclass.
     * @return Value of property genclass.
     */
    public String getTargetPackage() {
        return this.targetPackage;
    }

    /**
     * Setter for property genclass.
     * @param genclass New value of property genclass.
     */
    public void setTargetPackage(String targetPackage) {
        this.targetPackage = targetPackage;
    }

    /**
     * Getter for property toXML.
     * @return Value of property toXML.
     */
    public String getDestDir() {
        return this.destDir;
    }

    /**
     * Setter for property toXML.
     * @param toXML New value of property toXML.
     */
    public void setDestDir(String destDir) {
        this.destDir = makeAbsolute(destDir);
    }

    /**
     * Getter for property hibernateProperties.
     * @return Value of property hibernateProperties.
     */
    public String getHibernatePropsFile() {
        return this.hibernatePropsFile;
    }

    /**
     * Setter for property hibernateProperties.
     * @param hibernateProperties New value of property hibernateProperties.
     */
    public void setHibernatePropsFile(String hibernatePropsFile) {
        this.hibernatePropsFile = makeAbsolute(hibernatePropsFile);
    }

    public void addTable(TaskTable table) {
        taskTableList.add(table);
    }
}
