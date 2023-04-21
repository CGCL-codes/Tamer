package ch.elca.el4ant.taskdefs;

import java.net.MalformedURLException;
import java.net.URL;
import org.apache.tools.ant.AntClassLoader;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.condition.Os;
import org.apache.tools.ant.types.Path;

/**
 * This task enables to get the file name of a jar in the ClassPath that
 * contains a resource. It is a raw copy of Ant WhichResource because of
 * private fields. It expects the resulting URL has protocol "jar:file:".
 *
 * <script type="text/javascript">printFileStatus
 *   ("$URL: http://el4ant.svn.sourceforge.net/svnroot/el4ant/trunk/buildsystem/core/java/ch/elca/el4ant/taskdefs/WhichResourceFile.java $",
 *   "$Revision: 288 $", "$Date: 2005-10-31 06:47:01 -0500 (Mon, 31 Oct 2005) $", "$Author: yma $"
 * );</script>
 *
 * @author Yves Martin (YMA)
 * @version $Revision: 288 $
 */
public class WhichResourceFile extends Task {

    /**
     * our classpath.
     */
    private Path classpath;

    /**
     * class to look for.
     */
    private String classname;

    /**
     * resource to look for.
     */
    private String resource;

    /**
     * property to set.
     */
    private String property;

    /**
     * Set the classpath to be used for this compilation.
     * @param cp the classpath to be used.
     */
    public void setClasspath(Path cp) {
        if (classpath == null) {
            classpath = cp;
        } else {
            classpath.append(cp);
        }
    }

    /**
     * Adds a path to the classpath.
     * @return a classpath to be configured.
     */
    public Path createClasspath() {
        if (classpath == null) {
            classpath = new Path(getProject());
        }
        return classpath.createPath();
    }

    /**
     * validate.
     */
    private void validate() {
        int setcount = 0;
        if (classname != null) {
            setcount++;
        }
        if (resource != null) {
            setcount++;
        }
        if (setcount == 0) {
            throw new BuildException("One of classname or resource must be specified");
        }
        if (setcount > 1) {
            throw new BuildException("Only one of classname or resource can be specified");
        }
        if (property == null) {
            throw new BuildException("No property defined");
        }
    }

    /**
     * execute it.
     * @throws BuildException
     */
    public void execute() throws BuildException {
        validate();
        if (classpath != null) {
            getProject().log("using user supplied classpath: " + classpath, Project.MSG_DEBUG);
            classpath = classpath.concatSystemClasspath("ignore");
        } else {
            classpath = new Path(getProject());
            classpath = classpath.concatSystemClasspath("only");
            getProject().log("using system classpath: " + classpath, Project.MSG_DEBUG);
        }
        AntClassLoader loader;
        loader = new AntClassLoader(getProject().getCoreLoader(), getProject(), classpath, false);
        if (classname != null) {
            resource = classname.replace('.', '/') + ".class";
        }
        if (resource == null) {
            throw new BuildException("One of class or resource is required");
        }
        if (resource.startsWith("/")) {
            resource = resource.substring(1);
        }
        log("Searching for " + resource, Project.MSG_VERBOSE);
        URL url;
        url = loader.getResource(resource);
        if (url != null) {
            String result = null;
            if ("jar".equals(url.getProtocol())) {
                String jarSpec = url.getFile();
                int mark = jarSpec.indexOf('!');
                if (mark == 0) {
                    result = jarSpec;
                } else {
                    try {
                        URL fileURL = new URL(jarSpec.substring(0, mark));
                        if (Os.isFamily("windows")) {
                            result = fileURL.getFile().substring(1);
                        } else {
                            result = fileURL.getFile();
                        }
                    } catch (MalformedURLException mue) {
                        throw new BuildException(mue);
                    }
                    result = result.replaceAll("%20", " ");
                }
            }
            getProject().setNewProperty(property, result);
        }
    }

    /**
     * name the resource to look for.
     * @param newResource the name of the resource to look for.
     */
    public void setResource(String newResource) {
        this.resource = newResource;
    }

    /**
     * name the class to look for.
     * @param newClassname the name of the class to look for.
     */
    public void setClass(String newClassname) {
        this.classname = newClassname;
    }

    /**
     * the property to fill with the URL of the resource or class.
     * @param newProperty the property to be set.
     */
    public void setProperty(String newProperty) {
        this.property = newProperty;
    }
}
