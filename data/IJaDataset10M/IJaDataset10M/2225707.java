package xdoclet.modules.web;

import org.apache.tools.ant.BuildException;
import xdoclet.DocletTask;

/**
 * This task executes various web-specific sub-tasks. Make sure to include the jar file containing Sun's javax.servlet.*
 * classes on the taskdef's classpath.
 *
 * @author        Ara Abrahamian (ara_e@email.com)
 * @created       July 2, 2001
 * @ant.element   name="webdoclet" display-name="Web Task"
 * @version       $Revision: 1.5 $
 */
public class WebDocletTask extends DocletTask {

    protected void validateOptions() throws BuildException {
        super.validateOptions();
        checkClass("javax.servlet.Servlet");
    }
}
