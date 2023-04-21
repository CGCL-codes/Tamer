package onepoint.project.module;

import java.text.MessageFormat;

/**
 * Exception class indicating that there either a cyclic dependency exists between 2 modules,
 * or a dependent module is not present.
 *
 * @author horia.chiorean
 */
public class OpModuleDependencyException extends RuntimeException {

    private static final String CYCLIC_DEPENDENCY = "There is a cyclic dependency between the modules {0} and {1} ";

    private static final String MISSING_DEPENDENCY = "The module {0} on which module {1} depends was not found ";

    /**
    *  @see Throwable#Throwable(String)
    */
    private OpModuleDependencyException(String message) {
        super(message);
    }

    /**
    * Creates a new <code>OpModuleDependencyException</code> indicating a cyclic dependency between 2 modules.
    * @param module1Name a <code>String</code> representing the name of the 1st module.
    * @param module2Name a <code>String</code> representing the name of the 2st module.
    * @return  a <code>OpModuleDependencyException</code> instance.
    */
    public static OpModuleDependencyException createCyclincDependecyException(String module1Name, String module2Name) {
        String message = MessageFormat.format(CYCLIC_DEPENDENCY, module1Name, module2Name);
        return new OpModuleDependencyException(message);
    }

    /**
    * Creates a new <code>OpModuleDependencyException</code> indicating a cyclic dependency between 2 modules.
    * @param dependencyModuleName a <code>String</code> representing the name of the module from the dependency.
    * @param dependentModuleName a <code>String</code> representing the name of dependent module..
    * @return  a <code>OpModuleDependencyException</code> instance.
    */
    public static OpModuleDependencyException createMissingDependecyException(String dependencyModuleName, String dependentModuleName) {
        String message = MessageFormat.format(MISSING_DEPENDENCY, dependencyModuleName, dependentModuleName);
        return new OpModuleDependencyException(message);
    }
}
