package com.liferay.util.ant;

import org.apache.tools.ant.Project;

/**
 * <a href="AntUtil.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Unknown
 * @version $Rev: $
 *
 */
public class AntUtil {

    public static Project getProject() {
        Project project = new Project();
        SystemLogger logger = new SystemLogger();
        logger.setMessageOutputLevel(Project.MSG_INFO);
        logger.setOutputPrintStream(System.out);
        logger.setErrorPrintStream(System.err);
        project.addBuildListener(logger);
        return project;
    }
}
