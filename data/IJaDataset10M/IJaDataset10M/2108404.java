package net.sourceforge.javautil.developer.common.project;

/**
 * This is the basic contract/interface for project natures, the concept
 * that a project has a type of ability or feature. All natures must provide
 * a two argument constructor like {@link ProjectNatureAbstract#ProjectNatureAbstract(IProject, String)}.
 *
 * @author elponderador
 * @author $Author: ponderator $
 * @version $Id: IProjectNature.java 2304 2010-06-16 02:46:42Z ponderator $
 */
public interface IProjectNature {

    /**
	 * @return The project the nature is for
	 */
    IProject getProject();

    /**
	 * @return The unique name of this nature
	 */
    String getName();

    /**
	 * Called by the {@link IProject} to indicate that it must update its resources
	 * 
	 * @return True if this nature was actually refreshed, otherwise false
	 */
    boolean refresh();
}
