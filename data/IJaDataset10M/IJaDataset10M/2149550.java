package jistory.testPlatform.tests;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import jistory.metrics.AbstractMetric;
import jistory.metrics.cohesion.TCC_TightClassCohesion;
import junit.framework.TestCase;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.launching.IVMInstall;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.jdt.launching.LibraryLocation;

/**
 *	Tests to see if the TCC metric is correctly retrieved
 *	@author Christopher Bull
 */
public class CollectMetric_TCC extends TestCase {

    /** The Project to be created and temporarily used */
    private IProject project;

    /** The Java Project reference */
    private IJavaProject javaProject;

    /**
	 *	Construct a new test instance
	 *	@param name The test name
	 */
    public CollectMetric_TCC(String name) {
        super(name);
    }

    /**
	 *	Perform pre-test initialisation.
	 *	@throws Exception
	 *	@see TestCase#setUp()
	 */
    protected void setUp() throws Exception {
        super.setUp();
        IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
        project = root.getProject("TestProj");
        IFolder folder = project.getFolder("TestPackage");
        IFile file = folder.getFile("TestFile_TCC.java");
        if (!project.exists()) project.create(null);
        project.open(null);
        if (!folder.exists()) folder.create(IResource.NONE, true, null);
        if (!file.exists()) {
            byte[] bytes = "package lib.concurrency;import java.util.HashMap;public class ConcurrencyManager{private final static String ERROR_MESSAGE = \"Invallid Execution --- possible programming error at aspects.concurrencyControl.util.ConcurrencyManager\";private HashMap keys;public ConcurrencyManager(){keys = new HashMap();}public synchronized void beginExecution(Object key){while (keys.containsKey(key)){//System.out.println(\"You have to wait -> ConcurrencyManager\");\ntry{wait();} catch (InterruptedException ex){throw new RuntimeException(ERROR_MESSAGE + ex.getMessage());}}//System.out.println(\"You can execute -> ConcurrencyManager\");\nkeys.put(key, null);}public synchronized void endExecution(Object key){try{if(keys.containsKey(key)){keys.remove(key);}else{throw new RuntimeException(ERROR_MESSAGE);}}catch(Exception ex){System.out.println(ERROR_MESSAGE + ex.getMessage());}finally{notifyAll();}}}".getBytes();
            InputStream source = new ByteArrayInputStream(bytes);
            file.create(source, IResource.NONE, null);
            source.close();
        }
        IProjectDescription description = project.getDescription();
        String[] natures = description.getNatureIds();
        String[] newNatures = new String[natures.length + 1];
        System.arraycopy(natures, 0, newNatures, 0, natures.length);
        newNatures[natures.length] = JavaCore.NATURE_ID;
        description.setNatureIds(newNatures);
        project.setDescription(description, null);
        javaProject = JavaCore.create(project);
        Set<IClasspathEntry> entries = new HashSet<IClasspathEntry>();
        entries.addAll(Arrays.asList(javaProject.getRawClasspath()));
        IVMInstall vmInstall = JavaRuntime.getDefaultVMInstall();
        LibraryLocation[] locations = JavaRuntime.getLibraryLocations(vmInstall);
        for (LibraryLocation element : locations) {
            entries.add(JavaCore.newLibraryEntry(element.getSystemLibraryPath(), null, null));
        }
        javaProject.setRawClasspath(entries.toArray(new IClasspathEntry[entries.size()]), null);
    }

    /**
	 *	Perform post-test cleanup.
	 *	@throws Exception
	 *	@see TestCase#tearDown()
	 */
    protected void tearDown() throws Exception {
        super.tearDown();
        project.delete(true, true, null);
    }

    /**
	 *	This method tests whether the TCC value can be gathered correctly.
	 *	Note: This method name has to be prefixed with "test" for it to work 
	 *	@throws Exception for any exceptions 
	 */
    public void test_CollectMetric_TCC() throws Exception {
        TCC_TightClassCohesion tcc = new TCC_TightClassCohesion();
        assertTrue(tcc.calculate(AbstractMetric.getCompilationUnitAST(javaProject.findType("TestPackage.TestFile_TCC").getCompilationUnit(), true)) == 1.0);
    }
}
