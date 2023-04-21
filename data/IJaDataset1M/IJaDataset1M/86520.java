package ch.elca.leaf.buildsystem.propgen;

import java.util.Iterator;
import java.util.Properties;
import ch.elca.leaf.buildsystem.model.Module;
import ch.elca.leaf.buildsystem.model.ProjectRepository;

/**
 * This generator creates all generic module properties.
 *
 * <script type="text/javascript">printFileStatus
 *   ("$Source$",
 *   "$Revision: 2 $", "$Date: 2004-11-25 03:35:35 -0500 (Thu, 25 Nov 2004) $", "$Author: yma $"
 * );</script>
 *
 * @author Yves Martin (YMA)
 * @version $Revision: 2 $
 */
public class ModulesPropertyGenerator implements PropertyGenerator {

    /** List of registered modules */
    public static final String MODULE_LIST = "module.list";

    /** Prefix for each module dependency list */
    public static final String MODULE_DEP_LIST = "module.dependencies.";

    /** Prefix for each module path */
    public static final String MODULE_PATH = "module.path.";

    /** Prefix for each module eu list */
    public static final String MODULE_EU_LIST = "module.eulist.";

    /**
     * Generate module properties.
     *
     * @param p the project <code>Properties</code>
     * @return <code>true</code> (no condition)
     */
    public boolean generate(Properties p) {
        ListProperty moduleList = new ListProperty();
        Iterator moduleIt = ProjectRepository.getInstance().getModuleIterator();
        while (moduleIt.hasNext()) {
            Module aModule = (Module) moduleIt.next();
            moduleList.addItem(aModule.getName());
            p.setProperty(MODULE_PATH + aModule.getName(), aModule.getFilePath().getAbsolutePath());
            ListProperty moduleDeps = new ListProperty();
            Iterator depIt = aModule.getCompleteDependencies();
            while (depIt.hasNext()) {
                Module aDep = (Module) depIt.next();
                moduleDeps.addItem(aDep.getName());
            }
            p.setProperty(MODULE_DEP_LIST + aModule.getName(), moduleDeps.toString(LIST_SEPARATOR));
            ListProperty euList = new ListProperty();
            Iterator euIt = aModule.getEuIterator();
            while (euIt.hasNext()) {
                String anEu = (String) euIt.next();
                euList.addItem(anEu);
            }
            p.setProperty(MODULE_EU_LIST + aModule.getName(), euList.toString(LIST_SEPARATOR));
        }
        p.setProperty(MODULE_LIST, moduleList.toString(LIST_SEPARATOR));
        return true;
    }
}
