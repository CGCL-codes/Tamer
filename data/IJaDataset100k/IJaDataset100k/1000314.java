package net.sf.stump.eclipse.util;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import junit.framework.TestCase;
import net.sf.stump.eclipse.util.BuildPathAssistant;
import net.sf.stump.eclipse.util.JarListProvider;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaModelException;
import com.laughingpanda.mocked.MockFactory;

/**
 * @author Joni Suominen
 */
public class BuildPathAssistantTest extends TestCase {

    private IJavaProject project;

    private BuildPathAssistant assistant;

    @Override
    protected void setUp() throws Exception {
        project = (IJavaProject) MockFactory.makeMock(MockJavaProject.class);
        MockClasspathEntry e1 = (MockClasspathEntry) MockFactory.makeMock(MockClasspathEntry.class);
        e1.setPath(Path.fromPortableString("/tmp/p1.jar"));
        project.setRawClasspath(new IClasspathEntry[] { e1 }, null);
        assistant = new MockBuildPathAssistant();
        assistant.setJarListProvider(new JarListProvider() {

            public List<String> getJarNames() {
                List<String> jarNames = new ArrayList<String>();
                jarNames.add("wicket-1.4.7.jar");
                jarNames.add("log4j-1.2.15.jar");
                jarNames.add("selenium-server-2.0a2.jar");
                jarNames.add("jetty-6.1.4.jar");
                jarNames.add("junit-4.8.1.jar");
                return jarNames;
            }
        });
    }

    public void testAddWicketToBuildPath() throws JavaModelException {
        assistant.addWicketToBuildPath(project);
        List<String> names = collectNames(project.getRawClasspath());
        assertEquals(assistant.getAllJars().size() + 1, names.size());
        assertTrue(names.contains("p1.jar"));
        assertTrue(names.containsAll(assistant.getAllJars()));
    }

    public void testGetAllJars() throws Exception {
        List<String> allJars = assistant.getAllJars();
        List<String> expected = new ArrayList<String>();
        expected.add("wicket-1.4.7.jar");
        expected.add("log4j-1.2.15.jar");
        expected.add("selenium-server-2.0a2.jar");
        expected.add("jetty-6.1.4.jar");
        expected.add("junit-4.8.1.jar");
        assertEquals(expected, allJars);
    }

    private List<String> collectNames(IClasspathEntry[] entries) {
        List<String> names = new ArrayList<String>();
        for (int i = 0; i < entries.length; i++) {
            IClasspathEntry entry = entries[i];
            names.add(entry.getPath().lastSegment());
        }
        return names;
    }

    private static class MockBuildPathAssistant extends BuildPathAssistant {

        @Override
        protected void setClasspath(IJavaProject project, List<IClasspathEntry> entries) {
            try {
                setClasspath(project, entries, null);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public abstract static class MockJavaProject implements IJavaProject {

        private IClasspathEntry[] entries;

        public IClasspathEntry[] getRawClasspath() {
            return entries;
        }

        public void setRawClasspath(IClasspathEntry[] entries, IProgressMonitor monitor) {
            this.entries = entries;
        }

        public boolean isOnClasspath(IJavaElement element) {
            return false;
        }
    }

    public abstract static class MockClasspathEntry implements IClasspathEntry {

        private IPath path;

        public void setPath(IPath path) {
            this.path = path;
        }

        public IPath getPath() {
            return path;
        }
    }
}
