package org.jmlspecs.jml6.core.test.javacontract;

import java.util.ArrayList;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTMatcher;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MemberValuePair;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.jmlspecs.jml6.core.annotation.JmlAnnotationEncoder;
import org.jmlspecs.jml6.core.util.ASTHelper;
import org.junit.After;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/** Compares encoded files in a test project and compares with desired results.
 * IMPORTANT:
 * Before using this test. In your test workspace, you must have a project of the same name as PROJECT_NAME.
 * This project must have the files to be encoded in the "original" package and the already encoded file
 * in the "results" package. All dependencies must also be configured, i.e. you must import the projects
 * with the JML annotations as well as the JavaContract classes.
 * 
 * @author Tristan St-Cyr
 */
@Deprecated
public class JmlAnnotationEncoderTest {

    private static String PROJECT_NAME = "org.jmlspecs.jml6.core.test.testproject";

    private static ArrayList<String> classes = new ArrayList<String>();

    private static IJavaProject javaProject;

    private static IProject project;

    public static void classSetUp() throws CoreException {
        IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
        project = root.getProject(PROJECT_NAME);
        project.open(null);
        javaProject = JavaCore.create(project);
        IFolder f = project.getFolder("/src/original/");
        f.accept(new MyResourceVisitor());
    }

    public void testAll() throws CoreException {
        for (String className : classes) assertMatch(className);
    }

    public void tearDown() throws Exception {
    }

    private CompilationUnit runEncoder(String filepath) throws JavaModelException, IllegalArgumentException {
        IFile file = project.getFile(filepath);
        Assert.assertTrue("file " + filepath + " does not exist", file.exists());
        ICompilationUnit icu = JmlAnnotationEncoder.encode(file);
        ASTParser parser = ASTParser.newParser(AST.JLS3);
        parser.setResolveBindings(true);
        parser.setBindingsRecovery(true);
        parser.setProject(icu.getJavaProject());
        parser.setSource(icu);
        return (CompilationUnit) parser.createAST(null);
    }

    private CompilationUnit getAST(String filepath) {
        IFile file = project.getFile(filepath);
        ASTParser parser = ASTParser.newParser(AST.JLS3);
        parser.setResolveBindings(true);
        parser.setBindingsRecovery(true);
        parser.setProject(javaProject);
        parser.setSource((ICompilationUnit) JavaCore.create(file));
        return (CompilationUnit) parser.createAST(null);
    }

    private void assertMatch(String name) throws JavaModelException, IllegalArgumentException {
        System.out.println("Comparing for " + name);
        CompilationUnit originalAST = runEncoder("/src/original/" + name + ".java");
        CompilationUnit modifiedAST = getAST("/src/results/" + name + ".java");
        TypeDeclaration tdOriginal = (TypeDeclaration) ASTHelper.getTopLevelType(originalAST, name);
        TypeDeclaration tdModified = (TypeDeclaration) ASTHelper.getTopLevelType(modifiedAST, name);
        MyMatcher matcher = new MyMatcher();
        if (tdOriginal.bodyDeclarations().size() != tdModified.bodyDeclarations().size()) Assert.fail("Trees do not match.");
        for (int i = 0; i < tdOriginal.bodyDeclarations().size(); i++) {
            ASTNode node1 = (ASTNode) tdOriginal.bodyDeclarations().get(i);
            ASTNode node2 = (ASTNode) tdModified.bodyDeclarations().get(i);
            if (!node1.subtreeMatch(matcher, node2)) {
                Assert.fail(node1 + " does not match " + node2);
            }
        }
    }

    /**
	 * Does not match "date" in the "@Generated" annotation.
	 * @author Tristan St-Cyr
	 *
	 */
    class MyMatcher extends ASTMatcher {

        public boolean match(NormalAnnotation node, Object other) {
            if (!node.getTypeName().toString().equals("Generated")) return super.match(node, other);
            if (!(other instanceof NormalAnnotation)) return false;
            NormalAnnotation otherAnnotation = (NormalAnnotation) other;
            if (!otherAnnotation.getTypeName().toString().endsWith("Generated")) return false;
            if (otherAnnotation.values().size() != node.values().size()) return false;
            for (int i = 0; i < node.values().size(); i++) {
                MemberValuePair mvp = (MemberValuePair) node.values().get(i);
                if (!mvp.getName().toString().equals("date") && !mvp.subtreeMatch(this, otherAnnotation.values().get(i))) {
                    return false;
                }
            }
            return true;
        }
    }

    static class MyResourceVisitor implements IResourceVisitor {

        @Override
        public boolean visit(IResource resource) throws CoreException {
            if (resource instanceof IFile) {
                IFile f = (IFile) resource;
                String name = f.getName().toString();
                classes.add(name.replace(".java", ""));
            }
            return true;
        }
    }
}
