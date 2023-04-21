package com.enerjy.common.jdt;

import java.util.Map;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.internal.compiler.env.INameEnvironment;

/**
 * Interface for projects to be compiled by JdtUtils.
 */
@SuppressWarnings("restriction")
public interface IJdtProject {

    /**
     * @return The Eclipse java project this project corresponds to. Return <code>null</code> to provide a Name Environment
     *         instead.
     */
    IJavaProject getJavaProject();

    /**
     * @return The name environment to use during compilation. JdtUtils will prefer IJavaProjects over INameEnvironments.
     */
    INameEnvironment getNameEnvironment();

    /**
     * Compiler options to use when compiling. Must specify:
     * <ul>
     * <li>CompilerOptions.OPTION_Source</li>
     * <li>CompilerOptions.OPTION_Compliance</li>
     * <li>CompilerOptions.OPTION_TargetPlatform</li>
     * <li>DefaultCodeFormatterConstants.FORMATTER_TAB_SIZE</li>
     * </ul>
     * 
     * @return Compiler options to use when compiling.
     */
    Map<String, String> getCompilerOptions();

    /**
     * @return Compilation units to compile.
     */
    ICompilationUnit[] getCompilationUnits();
}
