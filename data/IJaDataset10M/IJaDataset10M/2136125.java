package edu.ucdavis.genomics.metabolomics.xdoclet.task.jmx;

import java.io.File;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.FileSet;
import xdoclet.DocletTask;

/**
 * needed for jmx facade beans
 * @author wohlgemuth
 *
 */
public class JMXTask extends DocletTask {

    public JMXTask() {
        super();
        this.addTemplate(new JMXFacadeBeanTask());
        this.addTemplate(new JMXFacadeBeanAccessorTask());
    }

    public static void main(String[] args) {
        JMXTask task = new JMXTask();
        task.setDestDir(new File("mytest/generate"));
        task.setForce(true);
        task.setProject(new Project());
        FileSet myset = new FileSet();
        myset.setFile(new File("mytest/DatabaseJMX.java"));
        task.addFileset(myset);
        task.execute();
    }
}
