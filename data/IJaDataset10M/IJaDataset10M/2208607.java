package ch.elca.leaf.buildsystem.propgen;

import java.io.File;
import java.util.Iterator;
import java.util.Properties;
import ch.elca.leaf.buildsystem.model.Module;
import ch.elca.leaf.buildsystem.model.ProjectRepository;
import ch.elca.leaf.buildsystem.propgen.PropertyGenerator;

/**
 * This generator creates compile specific module properties.
 *
 * <script type="text/javascript">printFileStatus
 *   ("$Source$",
 *   "$Revision: 2 $", "$Date: 2004-11-25 03:35:35 -0500 (Thu, 25 Nov 2004) $", "$Author: yma $"
 * );</script>
 *
 * @author Yves Martin (YMA)
 * @version $Revision: 2 $
 */
public class CompilePropertyGenerator implements PropertyGenerator {

    /** Prefix for each module classpath */
    public static final String COMPILE_CLASSPATH = "compile.classpath.";

    private void appendModuleClassPath(Module m, ListProperty moduleCP) {
        File moduleClasses = new File(m.getFilePath(), "classes");
        moduleCP.addUniqueItem(moduleClasses.getAbsolutePath());
        appendModuleJars(m, moduleCP);
    }

    private void appendModuleJars(Module m, ListProperty moduleCP) {
        Iterator jarIt = m.getModuleJars();
        while (jarIt.hasNext()) {
            File aJar = (File) jarIt.next();
            moduleCP.addUniqueItem(aJar);
        }
    }

    /**
     * Generate module compilation properties.
     *
     * @param p the project <code>Properties</code>
     * @return <code>true</code> (no condition)
     */
    public boolean generate(Properties p) {
        Iterator moduleIt = ProjectRepository.getInstance().getModuleIterator();
        while (moduleIt.hasNext()) {
            Module aModule = (Module) moduleIt.next();
            ListProperty moduleCP = new ListProperty();
            appendModuleJars(aModule, moduleCP);
            Iterator depIt = aModule.getCompleteDependencies();
            while (depIt.hasNext()) {
                Module aDep = (Module) depIt.next();
                appendModuleClassPath(aDep, moduleCP);
            }
            p.setProperty(COMPILE_CLASSPATH + aModule.getName(), moduleCP.toString(CLASSPATH_SEPARATOR));
        }
        return true;
    }
}
