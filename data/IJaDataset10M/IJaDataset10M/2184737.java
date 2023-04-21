package net.sourceforge.javautil.developer.common.project;

/**
 * The base for most nature implementations.
 *
 * @author elponderador
 * @author $Author: ponderator $
 * @version $Id: ProjectNatureAbstract.java 2304 2010-06-16 02:46:42Z ponderator $
 */
public abstract class ProjectNatureAbstract implements IProjectNature {

    protected final IProject project;

    protected final String name;

    protected long lastLoad = -1;

    /**
	 * @param project The project the nature is for
	 * @param name The name/id of the nature
	 */
    public ProjectNatureAbstract(IProject project, String name) {
        this.name = name;
        this.project = project;
    }

    public IProject getProject() {
        return this.project;
    }

    public String getName() {
        return this.name;
    }

    public String toString() {
        return getClass().getSimpleName() + "[" + name + "]";
    }
}
