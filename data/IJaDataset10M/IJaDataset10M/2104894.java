package org.apache.tools.ant.taskdefs.optional.dependencies;

import junit.framework.TestSuite;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.PatternSet;
import java.io.BufferedReader;
import java.io.File;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

/**
 *
 * @author <a href="mailto:russgold@gmail.com">Russell Gold</a>
 **/
public class DependenciesTestCase extends AbstractDependenciesTestCase {

    public static boolean isUnixStyle = File.pathSeparatorChar == ':';

    private static final File ALTERNATE_CACHE = new File(USER_HOME, "cache");

    private static final String ALTERNATE_REPOSITORY = "http://someplace.com/maven/";

    public static TestSuite suite() {
        return new TestSuite(DependenciesTestCase.class);
    }

    /**
     * Verifies that the task will use an alternative cache location if the system property "ant.dependencies.cache" is set.
     * The simulated task is:
     * <dependencies path=all.jars>
     *     <dependency group="httpunit" version="1.5.4" />
     * </dependencies>
     * @throws java.lang.Exception
     */
    public void testUseExplicitCacheLocationBySystemProperty() throws Exception {
        System.setProperty("ant.dependencies.cache", ALTERNATE_CACHE.getAbsolutePath());
        Dependency d = _dependencies.createDependency();
        d.setGroup("httpunit");
        d.setVersion("1.5.4");
        expectLoadFile(ALTERNATE_CACHE, "httpunit/jars/httpunit-1.5.4.jar", "httpunit/jars/httpunit-1.5.4.jar", MavenRepository.DEFAULT_REMOTE_REPOSITORY);
        _dependencies.execute();
        _stub.verify();
    }

    /**
     * Verifies that the task will use an alternative cache location if the system property "ant.dependencies.cache" is set.
     * The simulated task is:
     * <property name="ant.dependencies.cache" value="${user.home}/cache"/>
     * <dependencies path=all.jars>
     *     <dependency group="httpunit" version="1.5.4" />
     * </dependencies>
     * @throws java.lang.Exception
     */
    public void testUseExplicitCacheLocation() throws Exception {
        _dependencies.getProject().setProperty("ant.dependencies.cache", ALTERNATE_CACHE.getAbsolutePath());
        Dependency d = _dependencies.createDependency();
        d.setGroup("httpunit");
        d.setVersion("1.5.4");
        expectLoadFile(ALTERNATE_CACHE, "httpunit/jars/httpunit-1.5.4.jar", "httpunit/jars/httpunit-1.5.4.jar", MavenRepository.DEFAULT_REMOTE_REPOSITORY);
        _dependencies.execute();
        _stub.verify();
    }

    /**
     * Verifies that any dashes in the version will be handled properly. The simulated task is:
     * <dependencies path=all.jars>
     *     <dependency group="httpunit" version="1.5-04" />
     * </dependencies>
     * @throws java.lang.Exception
     */
    public void testFetchJarWithDashedVersion() throws Exception {
        Dependency d = _dependencies.createDependency();
        d.setGroup("httpunit");
        d.setVersion("1.5-04");
        expectLoadFile("httpunit/jars/httpunit-1.5-04.jar", "httpunit/jars/httpunit-1.5-04.jar", MavenRepository.DEFAULT_REMOTE_REPOSITORY);
        _dependencies.execute();
        _stub.verify();
    }

    /**
     * Verifies that the task will use the artifact attribute if specified. The simulated task is:
     * <dependencies>
     *     <dependency group="xdoclet" artifact="xjavadoc" version="1.0.3" />
     * </dependencies>
     * @throws java.lang.Exception
     */
    public void testFetchJarWithSpecifiedArtifactName() throws Exception {
        Dependency d = _dependencies.createDependency();
        d.setGroup("xdoclet");
        d.setArtifact("xjavadoc");
        d.setVersion("1.0.3");
        expectLoadFile("xdoclet/jars/xjavadoc-1.0.3.jar");
        _dependencies.execute();
        _stub.verify();
    }

    /**
     * Verifies that the task will copy a single non-jar dependency. The simulated task is:
     * <dependencies>
     *     <dependency group="struts" type="tld" artifact="struts-html" version="1.1" />
     * </dependencies>
     * @throws java.lang.Exception
     */
    public void testFetchNonJarDependency() throws Exception {
        Dependency d = _dependencies.createDependency();
        d.setGroup("struts");
        d.setType("tld");
        d.setArtifact("struts-html");
        d.setVersion("1.1");
        expectLoadFile("struts/tlds/struts-html-1.1.tld");
        _dependencies.execute();
        _stub.verify();
        expectFileInRepository("struts/tlds/struts-html-1.1.tld");
    }

    /**
     * Verifies that the task will only load missing files. The simulated task is:
     * <dependencies>
     *     <dependency group="rhino"      version="1.5R4.1" />
     *     <dependency group="junit"      version="3.8.1" />
     *     <dependency group="nekohtml"   version="0.8.1" />
     * </dependencies>
     */
    public void testLoadMissingFilesOnly() throws Exception {
        addDependency("rhino", "1.5R4.1");
        addDependency("junit", "3.8.1");
        addDependency("nekohtml", "0.8.1");
        createDummyFile(getLocalFile("rhino/jars/rhino-1.5R4.1.jar"));
        expectLoadFile("junit/jars/junit-3.8.1.jar");
        createDummyFile(getLocalFile("nekohtml/jars/nekohtml-0.8.1.jar"));
        _dependencies.execute();
        _stub.verify();
        assertLogContains(Project.MSG_VERBOSE, "Found rhino-1.5R4.1.jar in local repository. Will not download it.");
        assertLogContains(Project.MSG_INFO, "Downloaded junit-3.8.1.jar from " + MavenRepository.DEFAULT_REMOTE_REPOSITORY);
        assertLogContains(Project.MSG_VERBOSE, "Found nekohtml-0.8.1.jar in local repository. Will not download it.");
        expectFileInRepository("rhino/jars/rhino-1.5R4.1.jar");
        expectFileInRepository("junit/jars/junit-3.8.1.jar");
        expectFileInRepository("nekohtml/jars/nekohtml-0.8.1.jar");
    }

    /**
     * Verifies that the task will use an alternative repository if the first does not have the artifact. The simulated task is:
     * <dependencies>
     *     <dependency group="rhino"      version="1.5R4.1" />
     *     <dependency group="junit"      version="3.8.1" />
     *     <dependency group="nekohtml"   version="0.8.1" />
     * </dependencies>
     */
    public void testMultipleRepositories() throws Exception {
        System.setProperty(Dependencies.REMOTE_REPOSITORY_PROPERTY, MavenRepository.DEFAULT_REMOTE_REPOSITORY + ',' + ALTERNATE_REPOSITORY);
        addDependency("rhino", "1.5R4.1");
        addDependency("junit", "3.8.1");
        addDependency("nekohtml", "0.8.1");
        expectLoadFile("rhino/jars/rhino-1.5R4.1.jar");
        rejectLoadFile("junit/jars/junit-3.8.1.jar", "junit/jars/junit-3.8.1.jar not found");
        expectLoadFile("junit/jars/junit-3.8.1.jar", ALTERNATE_REPOSITORY);
        expectLoadFile("nekohtml/jars/nekohtml-0.8.1.jar");
        _dependencies.execute();
        _stub.verify();
    }

    /**
      * Verifies that the task will use an alternative repository if the first does not have the artifact. The simulated task is:
      * <dependencies repositoryList="http://www.ibiblio.com/maven</repository,http://someplace.com/maven">
      *     <dependency group="rhino"      version="1.5R4.1" />
      *     <dependency group="junit"      version="3.8.1" />
      *     <dependency group="nekohtml"   version="0.8.1" />
      * </dependencies>
      */
    public void testRepositoriesFromAttribute() throws Exception {
        _dependencies.setRepositoryList(MavenRepository.DEFAULT_REMOTE_REPOSITORY + ',' + ALTERNATE_REPOSITORY);
        addDependency("rhino", "1.5R4.1");
        addDependency("junit", "3.8.1");
        addDependency("nekohtml", "0.8.1");
        expectLoadFile("rhino/jars/rhino-1.5R4.1.jar");
        rejectLoadFile("junit/jars/junit-3.8.1.jar", "junit/jars/junit-3.8.1.jar not found");
        expectLoadFile("junit/jars/junit-3.8.1.jar", ALTERNATE_REPOSITORY);
        expectLoadFile("nekohtml/jars/nekohtml-0.8.1.jar");
        _dependencies.execute();
        _stub.verify();
    }

    /**
      * Verifies that the task will use an alternative repository if the first does not have the artifact. The simulated task is:
      * <dependencies>
      *     <maven-repository>
      *     <maven-repository url="http://someplace.com/maven" />
      *     <dependency group="rhino"      version="1.5R4.1" />
      *     <dependency group="junit"      version="3.8.1" />
      *     <dependency group="nekohtml"   version="0.8.1" />
      * </dependencies>
      */
    public void testRepositoriesFromEmbeddedType() throws Exception {
        _dependencies.add(new MavenRepository());
        MavenRepository mavenRepository = new MavenRepository();
        mavenRepository.setUrl(new URL(ALTERNATE_REPOSITORY));
        _dependencies.add(mavenRepository);
        addDependency("rhino", "1.5R4.1");
        addDependency("junit", "3.8.1");
        addDependency("nekohtml", "0.8.1");
        expectLoadFile("rhino/jars/rhino-1.5R4.1.jar");
        rejectLoadFile("junit/jars/junit-3.8.1.jar", "junit/jars/junit-3.8.1.jar not found");
        expectLoadFile("junit/jars/junit-3.8.1.jar", ALTERNATE_REPOSITORY);
        expectLoadFile("nekohtml/jars/nekohtml-0.8.1.jar");
        _dependencies.execute();
        _stub.verify();
    }

    /**
      * Verifies that a custom repository will be used if specified. The simulated task is:
      * <dependencies>
      *     <funky-repository/>
      *     <dependency group="rhino"      version="1.5R4.1" />
      *     <dependency group="junit"      version="3.8.1" />
      *     <dependency group="nekohtml"   version="0.8.1" />
      * </dependencies>
      */
    public void testAlternativeRepositoryType() throws Exception {
        _dependencies.add(new FunkyRepository());
        addDependency("rhino", "1.5R4.1");
        addDependency("junit", "3.8.1");
        Dependency d = _dependencies.createDependency();
        d.setGroup("xdoclet");
        d.setArtifact("xjavadoc");
        d.setVersion("1.0.3");
        expectNonMavenLoadFile("rhino/jars/rhino-1.5R4.1.jar", "1.5R4.1/rhino/rhino.jar", FunkyRepository.REPOSITORY);
        expectNonMavenLoadFile("junit/jars/junit-3.8.1.jar", "3.8.1/junit/junit.jar", FunkyRepository.REPOSITORY);
        expectNonMavenLoadFile("xdoclet/jars/xjavadoc-1.0.3.jar", "1.0.3/xdoclet/xjavadoc.jar", FunkyRepository.REPOSITORY);
        _dependencies.execute();
        _stub.verify();
    }

    /**
     * Verifies that the task will report an error if no dependency group attribute was specified. The simulated task is:
     * <dependencies>
     *     <dependency/>
     * </dependencies>
     */
    public void testNoGroupSpecified() throws Exception {
        System.setProperty(Dependencies.REMOTE_REPOSITORY_PROPERTY, MavenRepository.DEFAULT_REMOTE_REPOSITORY + ',' + ALTERNATE_REPOSITORY);
        _dependencies.createDependency();
        try {
            _dependencies.execute();
            fail("Did not report missing dependency group attribute");
        } catch (BuildException e) {
            assertEquals("Error from build", "No dependency group attribute specified", e.getMessage());
        }
    }

    /**
     * Verifies that the task will report an error if a dependency does not specify either the version or latest attribute.
     *  The simulated task is:
     * <dependencies>
     *     <dependency group="something" />
     * </dependencies>
     */
    public void testNoVersionOrLatestSpecified() throws Exception {
        System.setProperty(Dependencies.REMOTE_REPOSITORY_PROPERTY, MavenRepository.DEFAULT_REMOTE_REPOSITORY + ',' + ALTERNATE_REPOSITORY);
        Dependency dependency = _dependencies.createDependency();
        dependency.setGroup("something");
        try {
            _dependencies.execute();
            fail("Did not report missing dependency version attribute");
        } catch (BuildException e) {
            assertEquals("Error from build", "No dependency version attribute specified", e.getMessage());
        }
    }

    /**
     * Verifies that the task will report errors for any files it cannot download. The simulated task is:
     * <dependencies>
     *     <dependency group="rhino"      version="1.5R4.1" />
     *     <dependency group="nekohtml"   version="0.8.1" />
     *     <dependency group="junit"      version="3.8.1" />
     * </dependencies>
     */
    public void testFilesNotLoadedError() throws Exception {
        addDependency("rhino", "1.5R4.1", new URL("http://www.rhino.org"));
        addDependency("nekohtml", "0.8.1");
        addDependency("junit", "3.8.1");
        rejectLoadFile("rhino/jars/rhino-1.5R4.1.jar", "rhino.jar not found");
        expectLoadFile("nekohtml/jars/nekohtml-0.8.1.jar");
        rejectLoadFile("junit/jars/junit-3.8.1.jar", "junit/jars/junit-3.8.1.jar not found");
        try {
            _dependencies.execute();
            fail("Did not report failure to load files");
        } catch (BuildException e) {
            StringReader sr = new StringReader(e.getMessage());
            BufferedReader br = new BufferedReader(sr);
            assertEquals("error message line 1", "Unable to download the following dependencies:", br.readLine());
            assertEquals("error message line 2", " rhino-1.5R4.1.jar. Try downloading it manually from <http://www.rhino.org>", br.readLine());
            assertEquals("error message line 3", " junit-3.8.1.jar.", br.readLine());
        }
    }

    /**
     * Verifies that the task will define a path containing all of the listed dependencies. The simulated task is:
     * <dependencies pathId="all.jars">
     *     <dependency group="rhino"      version="1.5R4.1" />
     *     <dependency group="junit"      version="3.8.1" unless="something.defined" />
     *     <dependency group="nekohtml"   version="0.8.1" />
     * </dependencies>
     */
    public void testCreatePath() throws Exception {
        addDependency("rhino", "1.5R4.1");
        addDependency("junit", "3.8.1").setUnless("something.defined");
        addDependency("nekohtml", "0.8.1");
        _dependencies.setPathId("all.jars");
        expectLoadFile("rhino/jars/rhino-1.5R4.1.jar");
        expectLoadFile("junit/jars/junit-3.8.1.jar");
        expectLoadFile("nekohtml/jars/nekohtml-0.8.1.jar");
        _dependencies.execute();
        verifyPath("all.jars", new String[] { "rhino/jars/rhino-1.5R4.1.jar", "junit/jars/junit-3.8.1.jar", "nekohtml/jars/nekohtml-0.8.1.jar" });
    }

    /**
     * Verifies that the task will define a path containing all of the listed dependencies, excluding some based on
     * properties. The simulated task is:
     * <dependencies pathId="all.jars">
     *     <dependency group="rhino"      version="1.5R4.1" unless="use.jtidy"/>
     *     <dependency group="junit"      version="3.8.1" />
     *     <dependency group="nekohtml"   version="0.8.1" />
     * </dependencies>
     */
    public void testCreatePathUnless() throws Exception {
        addDependency("rhino", "1.5R4.1").setUnless("use.jtidy");
        addDependency("junit", "3.8.1");
        addDependency("nekohtml", "0.8.1");
        _dependencies.setPathId("all.jars");
        expectLoadFile("rhino/jars/rhino-1.5R4.1.jar");
        expectLoadFile("junit/jars/junit-3.8.1.jar");
        expectLoadFile("nekohtml/jars/nekohtml-0.8.1.jar");
        _project.setProperty("use.jtidy", "x");
        _dependencies.execute();
        verifyPath("all.jars", new String[] { "junit/jars/junit-3.8.1.jar", "nekohtml/jars/nekohtml-0.8.1.jar" });
    }

    /**
     * Verifies that the task will define a path containing all of the listed dependencies, excluding some based on
     * properties. The simulated task is:
     * <dependencies pathId="all.jars">
     *     <dependency group="rhino"      version="1.5R4.1"/>
     *     <dependency group="junit"      version="3.8.1"  if="use.junit"/>
     *     <dependency group="nekohtml"   version="0.8.1" />
     * </dependencies>
     */
    public void testCreatePathIf() throws Exception {
        addDependency("rhino", "1.5R4.1");
        addDependency("junit", "3.8.1").setIf("use.junit");
        addDependency("nekohtml", "0.8.1");
        _dependencies.setPathId("all.jars");
        expectLoadFile("rhino/jars/rhino-1.5R4.1.jar");
        expectLoadFile("nekohtml/jars/nekohtml-0.8.1.jar");
        _dependencies.execute();
        _stub.verify();
        verifyPath("all.jars", new String[] { "rhino/jars/rhino-1.5R4.1.jar", "nekohtml/jars/nekohtml-0.8.1.jar" });
        _project.setProperty("use.junit", "x");
        expectLoadFile("junit/jars/junit-3.8.1.jar");
        _dependencies.execute();
        _stub.verify();
        verifyPath("all.jars", new String[] { "rhino/jars/rhino-1.5R4.1.jar", "junit/jars/junit-3.8.1.jar", "nekohtml/jars/nekohtml-0.8.1.jar" });
    }

    private void verifyPath(String pathId, String[] expected) {
        Object o = _project.getReference(pathId);
        assertNotNull("No data item 'all.jars' was defined", o);
        assertTrue("referenced item 'all.jars' is not a Path", o instanceof Path);
        String[] list = ((Path) o).list();
        assertEquals("Number of elements in '" + pathId + "'", expected.length, list.length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals("Path element " + i, new File(DEFAULT_CACHE, expected[i]), new File(list[i]));
        }
    }

    /**
     * Verifies that the task will define a fileset containing all of the listed dependencies. The simulated task is:
     * <dependencies fileSetId="all.jars">
     *     <dependency group="rhino"      version="1.5R4.1" />
     *     <dependency group="junit"      version="3.8.1" />
     *     <dependency group="nekohtml"   version="0.8.1" />
     * </dependencies>
     */
    public void testCreateFileSet() throws Exception {
        String fileSetId = "all.jars";
        addDependency("rhino", "1.5R4.1");
        addDependency("junit", "3.8.1");
        addDependency("nekohtml", "0.8.1");
        _dependencies.setFileSetId(fileSetId);
        expectLoadFile("rhino/jars/rhino-1.5R4.1.jar");
        expectLoadFile("junit/jars/junit-3.8.1.jar");
        expectLoadFile("nekohtml/jars/nekohtml-0.8.1.jar");
        _dependencies.execute();
        verifyFileSet(fileSetId, "rhino/jars/rhino-1.5R4.1.jar junit/jars/junit-3.8.1.jar nekohtml/jars/nekohtml-0.8.1.jar");
    }

    private void verifyFileSet(String fileSetId, String expectedPaths) {
        Object o = _project.getReference(fileSetId);
        assertNotNull("No data item '" + fileSetId + "' was defined", o);
        assertTrue("referenced item '" + fileSetId + "' is not a FileSet", o instanceof FileSet);
        FileSet fileSet = ((FileSet) o);
        File dir = fileSet.getDir(_project);
        assertEquals("FileSet directory", DEFAULT_CACHE, dir);
        fileSet.getSelectors(_project);
        DirectoryScanner ds = fileSet.getDirectoryScanner(_project);
        List actualPaths = Arrays.asList(ds.getIncludedFiles());
        StringTokenizer st = new StringTokenizer(expectedPaths);
        assertEquals("Number of elements in '" + fileSetId + "'", st.countTokens(), actualPaths.size());
        while (st.hasMoreTokens()) {
            String expectedPath = st.nextToken();
            assertTrue("Did not find " + expectedPath, actualPaths.contains(convertRelativePath(expectedPath)));
        }
    }

    /**
      * Verifies that the latest flag is passed to the repository. The simulated task is:
      * <dependencies pathId="all.jars" fileSetId="all.files">
      *     <versioned-repository/>
      *     <dependency group="rhino" latest="true" />
      *     <dependency group="junit" version="3.8.1" />
      * </dependencies>
      */
    public void testRequestLatestVersion() throws Exception {
        VersionedRepository repository = new VersionedRepository();
        _dependencies.add(repository);
        repository.useVersion("1.5R4.1");
        Dependency d = _dependencies.createDependency();
        d.setGroup("rhino");
        d.setVersion("1.5R4.0");
        d.setLatest(true);
        addDependency("junit", "3.8.1");
        _dependencies.setPathId("all.jars");
        _dependencies.setFileSetId("all.files");
        createDummyFile(getLocalFile("rhino/jars/rhino-1.5R4.0.jar"));
        expectNonMavenLoadFile("rhino/jars/rhino-1.5R4.1.jar");
        expectNonMavenLoadFile("junit/jars/junit-3.8.1.jar");
        _dependencies.execute();
        _stub.verify();
        verifyPath("all.jars", new String[] { "rhino/jars/rhino-1.5R4.1.jar", "junit/jars/junit-3.8.1.jar" });
        verifyFileSet("all.files", "rhino/jars/rhino-1.5R4.1.jar junit/jars/junit-3.8.1.jar");
    }

    /**
      * Verifies that an error is reported if multiple repositories are used and the latest flag is specified. The simulated task is:
      * <dependencies>
      *     <maven-repository>
      *     <versioned-repository/>
      *     <dependency group="rhino" latest="true" />
      * </dependencies>
      */
    public void testLatestVersionMultipleRepositories() throws Exception {
        _dependencies.add(new MavenRepository());
        _dependencies.add(new VersionedRepository());
        Dependency d = _dependencies.createDependency();
        d.setGroup("rhino");
        d.setVersion("1.5R4.0");
        d.setLatest(true);
        try {
            _dependencies.execute();
            fail("Did not report conflict between multiple repositories and latest attribute");
        } catch (BuildException e) {
            assertEquals("Error from build", "The 'latest' attribute may not be used with multiple repositories", e.getMessage());
        }
    }

    /**
      * Verifies that contents of an archive will be extracted if requested. The simulated task is:
      * <dependencies fileSetId="aPath">
      *     <dependency group="imaginary" type="zip" version="1.6" >
      *         <patternset>
      *             <include name="classes/com/somewhere/one.class"/>
      *         </patternset>
      *     </dependency>
      * </dependencies>
      */
    public void testArchiveDependency() throws Exception {
        _dependencies.setFileSetId("aPath");
        Dependency d = _dependencies.createDependency();
        d.setGroup("imaginary");
        d.setVersion("1.6");
        d.setType("zip");
        PatternSet set = new PatternSet();
        PatternSet.NameEntry include = set.createInclude();
        include.setName("classes/com/somewhere/one.class");
        d.addPatternSet(set);
        expectLoadFile("imaginary/zips/imaginary-1.6.zip");
        expectExpand("imaginary/zips/imaginary-1.6.zip", "imaginary/zips/imaginary-1.6/", set);
        _dependencies.execute();
        _stub.verify();
        verifyFileSet("aPath", "imaginary/zips/imaginary-1.6/" + "classes/com/somewhere/one.class");
    }

    /**
      * Verifies that contents of an archive will be extracted even if it is already in the cache. The simulated task is:
      * <dependencies pathId="aPath">
      *     <dependency group="imaginary" type="zip" version="1.6" >
      *         <patternset>
      *             <include name="lib/rapid.jar"/>
      *         </patternset>
      *     </dependency>
      * </dependencies>
      */
    public void testAlreadyLoadedArchiveDependency() throws Exception {
        _dependencies.setPathId("aPath");
        Dependency d = _dependencies.createDependency();
        d.setGroup("imaginary");
        d.setVersion("1.6");
        d.setType("zip");
        PatternSet set = new PatternSet();
        PatternSet.NameEntry include = set.createInclude();
        include.setName("lib/rapid.jar");
        d.addPatternSet(set);
        getLocalFile("imaginary/zips/imaginary-1.6.zip").mkdirs();
        expectExpand("imaginary/zips/imaginary-1.6.zip", "imaginary/zips/imaginary-1.6/", set);
        _dependencies.execute();
        _stub.verify();
        verifyPath("aPath", new String[] { "imaginary/zips/imaginary-1.6/" + "lib/rapid.jar" });
    }

    private void expectNonMavenLoadFile(String localPath) {
        expectNonMavenLoadFile(localPath, localPath, MavenRepository.DEFAULT_REMOTE_REPOSITORY);
    }

    private void expectNonMavenLoadFile(String localPath, String artifactPath, String fromRepository) {
        _stub.expectLoadFile(getLocalFile(DEFAULT_CACHE, localPath), fromRepository + artifactPath);
    }

    private void rejectLoadFile(String artifactPath, String reason) {
        rejectLoadFile(artifactPath, MavenRepository.DEFAULT_REMOTE_REPOSITORY, reason);
    }

    private void rejectLoadFile(String artifactPath, String fromRepository, String reason) {
        _stub.rejectLoadFile(getLocalFile(artifactPath), fromRepository + artifactPath, reason);
    }

    private void expectExpand(String archive, String dest, PatternSet set) {
        _stub.expectExpand(getLocalFile(archive), getLocalFile(dest), set);
    }

    private static String convertRelativePath(String unixPath) {
        if (isUnixStyle) {
            return unixPath;
        } else {
            return unixPath.replace('/', '\\');
        }
    }

    private void addDependency(String group, String version, URL url) {
        Dependency d = _dependencies.createDependency();
        d.setGroup(group);
        d.setVersion(version);
        d.setWebSite(url);
    }

    static class FunkyRepository extends HttpRepository {

        static final String REPOSITORY = "http://neverneverland/";

        public FunkyRepository() throws MalformedURLException {
            super(getRepositoryUrl());
        }

        protected String getRelativeDependencyPath(String group, String type, String artifact, String version) {
            return version + '/' + group + '/' + artifact + '.' + type;
        }

        public static URL getRepositoryUrl() throws MalformedURLException {
            return new URL(REPOSITORY);
        }
    }

    static class VersionedRepository extends HttpRepository {

        private String _version;

        VersionedRepository() throws MalformedURLException {
            super(new URL(MavenRepository.DEFAULT_REMOTE_REPOSITORY));
        }

        protected String getRelativeDependencyPath(String group, String type, String artifact, String version) {
            return group + '/' + type + "s/" + artifact + '-' + version + '.' + type;
        }

        void useVersion(String version) {
            _version = version;
        }

        public String getLatestVersion(Dependencies task, String group, String type, String artifact) {
            return _version;
        }
    }
}
