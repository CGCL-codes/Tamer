package com.liferay.util.ant;

import java.io.File;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.War;

/**
 * <a href="WarTask.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Unknown
 * @version $Rev: $
 *
 */
public class WarTask {

    public static void war(String baseDir, String destination, String excludes, String webxml) {
        war(new File(baseDir), new File(destination), excludes, new File(webxml));
    }

    public static void war(File baseDir, File destination, String excludes, File webxml) {
        Project project = AntUtil.getProject();
        War war = new War();
        war.setProject(project);
        war.setBasedir(baseDir);
        war.setDestFile(destination);
        war.setExcludes(excludes);
        war.setWebxml(webxml);
        war.execute();
    }
}
