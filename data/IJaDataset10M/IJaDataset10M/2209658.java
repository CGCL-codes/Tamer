package com.enerjy.common.jdt;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.WorkingCopyOwner;
import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTRequestor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.formatter.DefaultCodeFormatterConstants;
import org.eclipse.jdt.internal.compiler.CompilationResult;
import org.eclipse.jdt.internal.compiler.ICompilerRequestor;
import org.eclipse.jdt.internal.compiler.IErrorHandlingPolicy;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFileConstants;
import org.eclipse.jdt.internal.compiler.env.INameEnvironment;
import org.eclipse.jdt.internal.compiler.impl.CompilerOptions;
import org.eclipse.jdt.internal.core.CancelableProblemFactory;
import org.eclipse.jdt.internal.core.JavaModelManager;
import com.enerjy.common.EnerjyException;

/**
 * A collection of utilities for dealing with JDT compilation.
 */
@SuppressWarnings("restriction")
public class JdtUtils {

    /** Property name for the original ICompilationUnit attached to the CompilationUnit ASTNode */
    public static final String COMPILATION_UNIT = "com.enerjy.common.jdt.compilationUnit";

    /** Property name for the compilation options map attached to the CompilationUnit ASTNode */
    public static final String COMPILATION_OPTIONS = "com.enerjy.common.jdt.compilerOptions";

    /** Property name for the external binding resolve attached to the CompilationUnit ASTNode */
    public static final String EXTERNAL_RESOLVER = "com.enerjy.common.jdt.externalResolver";

    /**
     * Compile a set of files.
     * 
     * @param project Project that describes the files to compile.
     * @param requestor Requester to notify as units are compiled.
     * @param monitor Progress monitor to notify during compilation.
     */
    @SuppressWarnings("unchecked")
    public static void compileToAst(IJdtProject project, ICompilationUnitRequestor requestor, IProgressMonitor monitor) {
        IJavaProject javaProject = project.getJavaProject();
        Map<String, String> projectOptions = project.getCompilerOptions();
        Map options = new HashMap<Object, Object>();
        options.put(CompilerOptions.OPTION_Source, projectOptions.get(CompilerOptions.OPTION_Source));
        options.put(CompilerOptions.OPTION_Compliance, projectOptions.get(CompilerOptions.OPTION_Compliance));
        options.put(CompilerOptions.OPTION_TargetPlatform, projectOptions.get(CompilerOptions.OPTION_TargetPlatform));
        options.put(CompilerOptions.OPTION_DocCommentSupport, CompilerOptions.ENABLED);
        options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_SIZE, projectOptions.get(DefaultCodeFormatterConstants.FORMATTER_TAB_SIZE));
        ForwardingAstRequestor astRequestor = new ForwardingAstRequestor(requestor, options);
        if (null == javaProject) {
            JavaModelManager.doNotUse();
            resolve(project.getCompilationUnits(), project.getNameEnvironment(), new String[0], astRequestor, AST.JLS3, options, null, false, monitor);
        } else {
            ASTParser parser = ASTParser.newParser(AST.JLS3);
            parser.setProject(javaProject);
            parser.setResolveBindings(true);
            parser.setCompilerOptions(options);
            parser.createASTs(project.getCompilationUnits(), new String[0], astRequestor, monitor);
        }
    }

    /**
     * Determine the source level constant for a compilation unit.
     * 
     * @param unit Compilation unit to check
     * @return One of the ClassFileConstants JDK levels for the source level of the given unit.
     * @throws IllegalArgumentException if the compilation unit was not created by JdtUtils.
     */
    @SuppressWarnings("unchecked")
    public static long getSourceLevel(CompilationUnit unit) {
        Map<Object, Object> options = (Map<Object, Object>) unit.getProperty(COMPILATION_OPTIONS);
        if (null == options) {
            throw new IllegalArgumentException("Compilation unit did not come from JdtUtils");
        }
        return CompilerOptions.versionToJdkLevel(options.get(CompilerOptions.OPTION_Source));
    }

    /**
     * Obtain the original source code for an AST compilation unit.
     * 
     * @param unit Compilation unit to query.
     * @return Source code of the compilation unit.
     * @throws IllegalArgumentException if the compilation unit was not created by JdtUtils.
     */
    public static char[] getSourceCode(CompilationUnit unit) {
        org.eclipse.jdt.internal.core.CompilationUnit originalCu = (org.eclipse.jdt.internal.core.CompilationUnit) unit.getProperty(COMPILATION_UNIT);
        if (null == originalCu) {
            throw new IllegalArgumentException("Compilation unit did not come from JdtUtils");
        }
        return originalCu.getContents();
    }

    /**
     * Obtain the original source code's tab width for an AST compilation unit.
     * 
     * @param unit Compilation unit to query.
     * @return Source code's tab width.
     * @throws IllegalArgumentException if the compilation unit was not created by JdtUtils.
     */
    @SuppressWarnings("unchecked")
    public static int getSourceTabWidth(CompilationUnit unit) {
        Map<Object, Object> options = (Map<Object, Object>) unit.getProperty(COMPILATION_OPTIONS);
        if (null == options) {
            throw new IllegalArgumentException("Compilation unit did not come from JdtUtils");
        }
        Object foo = options.get(DefaultCodeFormatterConstants.FORMATTER_TAB_SIZE);
        return Integer.valueOf(foo.toString());
    }

    /**
     * Determine if a node was created in a Generics-compatible environment (JDK level &gt;= 1.5).
     * 
     * @param node Node to verify.
     * @return Whether or not the compilation unit supports generics.
     */
    public static boolean isGenericsCapable(ASTNode node) {
        ASTNode root = node.getRoot();
        if (ASTNode.COMPILATION_UNIT != root.getNodeType()) {
            throw new IllegalArgumentException("Invalid ASTNode - must be rooted by a CompilationUnit");
        }
        long lvl = getSourceLevel((CompilationUnit) root);
        return (ClassFileConstants.JDK1_5 <= lvl);
    }

    @SuppressWarnings("unchecked")
    private static void resolve(ICompilationUnit[] compilationUnits, INameEnvironment environment, String[] bindingKeys, ASTRequestor requestor, int apiLevel, Map options, WorkingCopyOwner owner, boolean statementsRecovery, IProgressMonitor monitor) {
        CancelableProblemFactory problemFactory = null;
        try {
            if (monitor != null) {
                int amountOfWork = (compilationUnits.length + bindingKeys.length) * 2;
                monitor.beginTask("", amountOfWork);
            }
            problemFactory = new CancelableProblemFactory(monitor);
            CompilerOptions compilerOptions = new CompilerOptions(options);
            compilerOptions.performStatementsRecovery = statementsRecovery;
            compilerOptions.parseLiteralExpressionsAsConstants = false;
            compilerOptions.storeAnnotations = true;
            try {
                Class clazz = Class.forName("org.eclipse.jdt.core.dom.CompilationUnitResolver");
                Constructor c = clazz.getDeclaredConstructors()[0];
                Method m = null;
                boolean withFlags = false;
                try {
                    m = clazz.getDeclaredMethod("resolve", ICompilationUnit[].class, String[].class, ASTRequestor.class, Integer.TYPE, Map.class, WorkingCopyOwner.class);
                } catch (NoSuchMethodException e) {
                    m = clazz.getDeclaredMethod("resolve", ICompilationUnit[].class, String[].class, ASTRequestor.class, Integer.TYPE, Map.class, WorkingCopyOwner.class, Integer.TYPE);
                    withFlags = true;
                }
                c.setAccessible(true);
                Object instance = c.newInstance(environment, new IErrorHandlingPolicy() {

                    public boolean stopOnFirstError() {
                        return false;
                    }

                    public boolean proceedOnErrors() {
                        return false;
                    }
                }, compilerOptions, new ICompilerRequestor() {

                    public void acceptResult(CompilationResult compilationResult) {
                    }
                }, problemFactory, monitor);
                m.setAccessible(true);
                if (withFlags) {
                    m.invoke(instance, compilationUnits, bindingKeys, requestor, apiLevel, options, owner, 0);
                } else {
                    m.invoke(instance, compilationUnits, bindingKeys, requestor, apiLevel, options, owner);
                }
                Class requestorClass = Class.forName("org.eclipse.jdt.core.dom.ASTRequestor");
                Field field = requestorClass.getDeclaredField("compilationUnitResolver");
                field.setAccessible(true);
                field.set(requestor, instance);
            } catch (ClassNotFoundException e) {
                throw new EnerjyException("Oops", e);
            } catch (NoSuchMethodException e) {
                throw new EnerjyException("Oops", e);
            } catch (IllegalAccessException e) {
                throw new EnerjyException("Oops", e);
            } catch (InvocationTargetException e) {
                throw new EnerjyException("Oops", e);
            } catch (InstantiationException e) {
                throw new EnerjyException("Oops", e);
            } catch (NoSuchFieldException e) {
                throw new EnerjyException("Oops", e);
            }
        } finally {
            if (monitor != null) {
                monitor.done();
            }
        }
    }

    /**
     * Obtain all error-level problems from a compilation unit.
     * 
     * @param cu Compilation unit to query.
     * @return Array (possibly empty) of errors in the compilation unit.
     */
    public static IProblem[] getASTErrors(CompilationUnit cu) {
        IProblem[] problems = cu.getProblems();
        if ((null == problems) || (0 == problems.length)) {
            return new IProblem[0];
        }
        List<IProblem> answer = new ArrayList<IProblem>();
        for (IProblem element : problems) {
            if (element.isError()) {
                answer.add(element);
            }
        }
        return answer.toArray(new IProblem[answer.size()]);
    }

    private JdtUtils() {
    }
}
