package configurator.task.java;

import configurator.model.rootmgr.NewModuleRootManagerComponent;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;

/**
 * Create a module library entry for a jar in IDEA's internal lib directory.
 * <p/>
 * Example: <idealibrary name="j2ee.jar"/>
 *
 * @author Justin Tomich
 */
public class IdeaLibrary extends BaseModuleLibrary {

    protected String name;

    public IdeaLibrary(Project project) {
        super(project);
    }

    public void setName(String name) {
        if (this.name != null) throw new BuildException("Set name only once.");
        this.name = name;
    }

    public Name createName() {
        return new Name();
    }

    public class Name {

        public void setName(String libName) {
            if (name != null) throw new BuildException("Set name only once.");
            name = libName;
        }
    }

    public void configure(NewModuleRootManagerComponent java) {
        configure(java, "$APPLICATION_HOME_DIR$/lib/" + name);
    }
}
