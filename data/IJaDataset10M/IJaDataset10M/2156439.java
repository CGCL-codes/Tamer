package repositorytests.org.deft.repository.repositorymanager.impl;

import static org.junit.Assert.assertTrue;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import junit.JUnitUtil;
import org.deft.operation.OperationChain;
import org.deft.operationchain.code2styledcode.CodeToStyledCodeOperationChain;
import org.deft.repository.artifacttype.MultiArtifactDependencies;
import org.deft.repository.chaptertype.ChapterAwareResourceHandlerFactory;
import org.deft.repository.chaptertype.ChapterAwareResourceHandlerProvider;
import org.deft.repository.chaptertype.ChapterType;
import org.deft.repository.datamodel.Artifact;
import org.deft.repository.datamodel.ArtifactReference;
import org.deft.repository.datamodel.Chapter;
import org.deft.repository.datamodel.Project;
import org.deft.repository.datamodel.adapter.ecore.EcoreWrapper;
import org.deft.repository.fragmentsource.FragmentSource;
import org.deft.repository.fragmentsource.FragmentSourceRegistry;
import org.deft.repository.fragmentsource.OriginalFragmentLocation;
import org.deft.repository.fragmentsource.filesystem.FileSystemFragmentLocation;
import org.deft.repository.fragmentsource.filesystem.FileSystemFragmentSource;
import org.deft.repository.repositorymanager.ContentManager;
import org.deft.repository.repositorymanager.DataStorageManager;
import org.deft.repository.repositorymanager.OptionsService;
import org.deft.repository.repositorymanager.impl.ContentManagerEcoreDataModelImpl;
import org.deft.repository.repositorymanager.impl.DataStorageManagerFileSystemImpl;
import org.deft.repository.repositorymanager.impl.EcoreDataModelImplCore;
import org.deft.repository.repositorymanager.impl.OptionsServiceImpl;
import org.deft.repository.service.ExtensionService;
import org.deft.repository.service.impl.ExtensionServiceImpl;
import org.deft.resource.ResourceHandler;
import org.deft.resource.ResourceHandlerFactory;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import repositorytests.org.deft.repository.mock.DummyIntegrator;
import repositorytests.org.deft.repository.mock.MockRepository;
import repositorytests.org.deft.repository.mock.SimpleJavaFragmentSourceRegistry;

public class DataStorageManagerFileSystemImplTest {

    private EcoreWrapper wrapper = new EcoreWrapper(new MockRepository());

    private EcoreDataModelImplCore core = new EcoreDataModelImplCore();

    private ContentManager contentManager = new ContentManagerEcoreDataModelImpl(core, wrapper);

    private OptionsService options = new OptionsServiceImpl();

    private static ChapterAwareResourceHandlerProvider resourceHandlerProvider = new ChapterAwareResourceHandlerProvider();

    private FragmentSource fileSystemSource = new FileSystemFragmentSource(null);

    private FragmentSourceRegistry fragmentSourceRegistry = new SimpleJavaFragmentSourceRegistry();

    private ExtensionService extensionService = new ExtensionServiceImpl(fragmentSourceRegistry, null, null, null, null, null, null);

    private String artifactType = "org.deft.repository.artifacttype.codefile.csharp";

    private DataStorageManager storageManager = new DataStorageManagerFileSystemImpl(contentManager, options, resourceHandlerProvider, extensionService);

    private IPath repositoryPath = new Path("testdata/Repository/repositorydir");

    /**
	 * Returns the absolute path of the repositoryPath
	 * @return the absolute path
	 */
    private IPath getRepositoryPath() {
        String sAbsolutePath = repositoryPath.toFile().getAbsolutePath();
        IPath ipAbsolutePath = new Path(sAbsolutePath);
        return ipAbsolutePath;
    }

    private File getLocationForCSharpFile(String fileName) {
        IPath path = new Path("testdata/Repository/csharpfiles");
        path = path.append(fileName);
        File location = path.toFile();
        return location;
    }

    private String asString(File file) {
        try {
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
            byte[] contents = new byte[bis.available()];
            bis.read(contents, 0, contents.length);
            bis.close();
            String string = new String(contents);
            return string;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private MultiArtifactDependencies dep(OriginalFragmentLocation location) {
        return new MultiArtifactDependencies(location);
    }

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        JUnitUtil.clearTestRepositoryDirectory();
        ChapterType chapterType = new ChapterType("org.deft.projecttype.texlipse", "LaTeX", "resources", "chapter_template.tex", "latex");
        ResourceHandler latexResourceHandler = ChapterAwareResourceHandlerFactory.createChapterTypeResourceHandler(chapterType, ResourceHandlerFactory.JAVA);
        resourceHandlerProvider.registerResourceHandler(latexResourceHandler);
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
        File location = repositoryPath.toFile();
        options.setRepositoryLocation(location.getAbsolutePath());
    }

    @After
    public void tearDown() throws Exception {
        JUnitUtil.clearTestRepositoryDirectory();
    }

    /**
     * Creates a Project.
     * Checks if the according directory in the repository is created.
     * 
     * @throws IOException
     */
    @Test
    public void testCreateProject() throws IOException {
        Project project = contentManager.createProject("testproject", "latex");
        storageManager.createProject(project);
        IPath ipDirInRepository = getRepositoryPath().append(project.getId());
        Assert.assertTrue(ipDirInRepository.toFile().exists());
    }

    /**
     * Creates a Project, Chapter, Artifact, and a Reference.
     * Then deletes them.
     * Checks if the according Project directory is really deleted.
     * 
     * @throws IOException
     */
    @Test
    public void testRemoveProject() throws IOException {
        String FILE = "Auto.cs";
        Project project = contentManager.createProject("testproject", "latex");
        storageManager.createProject(project);
        File file = getLocationForCSharpFile(FILE);
        OriginalFragmentLocation location = new FileSystemFragmentLocation(file);
        Artifact artifact = contentManager.createArtifact(project, fileSystemSource.getId(), location.getFullLocation(), artifactType, 1, dep(location), "Auto");
        storageManager.createArtifactContent(artifact, 1, location.getFullLocation(), dep(location));
        Chapter chapter = contentManager.createChapter(project, "chap", "latex");
        storageManager.createChapterContent(chapter);
        OperationChain chain = new CodeToStyledCodeOperationChain();
        ArtifactReference ref = contentManager.createArtifactReference(chapter, artifact, chain.getId(), "integratorId");
        storageManager.createArtifactReferenceContent(ref, chain, new DummyIntegrator());
        storageManager.removeArtifactReferenceContent(ref);
        storageManager.removeArtifactContent(artifact);
        storageManager.removeChapterContent(chapter);
        boolean removed = storageManager.removeProject(project);
        assertTrue(removed);
    }

    /**
     * Creates a project and an Artifact in that project.
     * Checks if the file representing the Artifact is actually
     * copied into the repository and has the right filename.
     * 
     * @throws IOException
     */
    @Test
    public void testCreateArtifactContent() throws IOException {
        String FILE = "Auto.cs";
        int REV = 3;
        Project project = contentManager.createProject("testproject", "latex");
        storageManager.createProject(project);
        File file = getLocationForCSharpFile(FILE);
        OriginalFragmentLocation location = new FileSystemFragmentLocation(file);
        Artifact artifact = contentManager.createArtifact(project, fileSystemSource.getId(), location.getFullLocation(), artifactType, REV, dep(location), FILE);
        storageManager.createArtifactContent(artifact, REV, location.getFullLocation(), dep(location));
        IPath ipDirInRepository = getRepositoryPath().append(project.getId()).append(artifact.getId());
        IPath ipFileInRepository = ipDirInRepository.append("rev" + Integer.toString(REV)).append(FILE);
        Assert.assertTrue(ipFileInRepository.toFile().exists());
        Assert.assertEquals(asString(file), asString(ipFileInRepository.toFile()));
    }

    /**
     * Creates a project and an Artifact in that project and then deletes the
     * Artifact.
     * Checks if the file representing the Artifact is actually deleted from the
     * repository
     * 
     * @throws IOException
     */
    @Test
    public void testRemoveArtifactContent() throws IOException {
        String FILE = "Auto.cs";
        int REV = 3;
        Project project = contentManager.createProject("testproject", "latex");
        storageManager.createProject(project);
        IPath ipProjectDir = getRepositoryPath().append(project.getId());
        Assert.assertEquals(0, ipProjectDir.toFile().listFiles().length);
        File file = getLocationForCSharpFile(FILE);
        OriginalFragmentLocation location = new FileSystemFragmentLocation(file);
        Artifact artifact = contentManager.createArtifact(project, fileSystemSource.getId(), location.getFullLocation(), artifactType, REV, dep(location), FILE);
        storageManager.createArtifactContent(artifact, REV, location.getFullLocation(), dep(location));
        Assert.assertEquals(1, ipProjectDir.toFile().listFiles().length);
        storageManager.removeArtifactContent(artifact);
        Assert.assertEquals(0, ipProjectDir.toFile().listFiles().length);
    }

    /**
     * Creates a project and a Chapter in that project.
     * Checks if the file representing the Chapter is actually
     * copied into the repository and has the right filename.
     * 
     * @throws IOException
     */
    @Test
    public void testCreateChapterContent() throws IOException {
        Project project = contentManager.createProject("testproject", "latex");
        storageManager.createProject(project);
        Chapter chapter = contentManager.createChapter(project, "chap", "latex");
        storageManager.createChapterContent(chapter);
        IPath ipFileInRepository = getRepositoryPath().append(project.getId()).append(chapter.getId());
        Assert.assertTrue(ipFileInRepository.toFile().exists());
    }

    /**
     * Creates a project and a Chapter in that project and then deletes the
     * Chapter.
     * Checks if the file representing the Chapter is actually deleted from the
     * repository.
     * 
     * @throws IOException
     */
    @Test
    public void testRemoveChapterContent() throws IOException {
        Project project = contentManager.createProject("testproject", "latex");
        storageManager.createProject(project);
        IPath ipProjectDir = getRepositoryPath().append(project.getId());
        Assert.assertEquals(0, ipProjectDir.toFile().listFiles().length);
        Chapter chapter = contentManager.createChapter(project, "chap", "latex");
        storageManager.createChapterContent(chapter);
        Assert.assertEquals(1, ipProjectDir.toFile().listFiles().length);
        storageManager.removeChapterContent(chapter);
        Assert.assertEquals(0, ipProjectDir.toFile().listFiles().length);
    }

    @Test
    public void testCreateArtifactReferenceContent() {
        String FILE = "Auto.cs";
        Project project = contentManager.createProject("testproject", "latex");
        storageManager.createProject(project);
        File file = getLocationForCSharpFile(FILE);
        OriginalFragmentLocation location = new FileSystemFragmentLocation(file);
        Artifact artifact = contentManager.createArtifact(project, fileSystemSource.getId(), location.getFullLocation(), artifactType, 1, dep(location), "Auto");
        Chapter chapter = contentManager.createChapter(project, "chap", "latex");
        OperationChain chain = new CodeToStyledCodeOperationChain();
        ArtifactReference ref = contentManager.createArtifactReference(chapter, artifact, chain.getId(), "integratorId");
        storageManager.createArtifactReferenceContent(ref, chain, new DummyIntegrator());
        IPath chainConfigFileInRepository = getRepositoryPath().append(project.getId()).append("references").append(ref.getId() + "-chain");
        Assert.assertTrue(chainConfigFileInRepository.toFile().exists());
        IPath integratorConfigFileInRepository = getRepositoryPath().append(project.getId()).append("references").append(ref.getId() + "-integrator");
        Assert.assertTrue(integratorConfigFileInRepository.toFile().exists());
    }

    @Test
    public void testRemoveArtifactReferenceContent() {
        String FILE1 = "Auto.cs";
        String FILE2 = "HelloWorld.cs";
        Project project = contentManager.createProject("testproject", "latex");
        storageManager.createProject(project);
        File file1 = getLocationForCSharpFile(FILE1);
        OriginalFragmentLocation location1 = new FileSystemFragmentLocation(file1);
        Artifact artifact1 = contentManager.createArtifact(project, fileSystemSource.getId(), location1.getFullLocation(), artifactType, 1, dep(location1), "Auto");
        File file2 = getLocationForCSharpFile(FILE2);
        OriginalFragmentLocation location2 = new FileSystemFragmentLocation(file2);
        Artifact artifact2 = contentManager.createArtifact(project, fileSystemSource.getId(), location2.getFullLocation(), artifactType, 2, dep(location2), "HelloWorld");
        Chapter chapter = contentManager.createChapter(project, "chap", "latex");
        OperationChain chain = new CodeToStyledCodeOperationChain();
        ArtifactReference ref1 = contentManager.createArtifactReference(chapter, artifact1, chain.getId(), "integratorId");
        storageManager.createArtifactReferenceContent(ref1, chain, new DummyIntegrator());
        ArtifactReference ref2 = contentManager.createArtifactReference(chapter, artifact2, chain.getId(), "integratorId");
        storageManager.createArtifactReferenceContent(ref2, chain, new DummyIntegrator());
        IPath referencesDir = getRepositoryPath().append(project.getId()).append("references");
        Assert.assertEquals(4, referencesDir.toFile().listFiles().length);
        storageManager.removeArtifactReferenceContent(ref1);
        Assert.assertEquals(2, referencesDir.toFile().listFiles().length);
        boolean success = storageManager.removeArtifactReferenceContent(ref2);
        assertTrue(success);
        Assert.assertFalse(referencesDir.toFile().exists());
    }

    /**
     * Creates a Project and a Chapter, opens two streams on the Chapter, closes
     * one of the streams and then deletes the Chapter.
     * Checks if the storageManager closes the remaining stream and deletes
     * the chapter file.
     * 
     * @throws IOException
     */
    @Test
    public void testOpenAndCloseChapter() throws IOException {
        Project project = contentManager.createProject("testproject", "latex");
        storageManager.createProject(project);
        Chapter chapter = contentManager.createChapter(project, "chapter", "latex");
        storageManager.createChapterContent(chapter);
        IPath ipProjectDir = getRepositoryPath().append(project.getId());
        InputStream is = storageManager.getInputStream(chapter);
        InputStream is2 = storageManager.getInputStream(chapter);
        Assert.assertTrue(is.available() > 0);
        Assert.assertTrue(is2.available() > 0);
        is2.close();
        boolean removed = storageManager.removeChapterContent(chapter);
        Assert.assertTrue(removed);
        Assert.assertEquals(0, ipProjectDir.toFile().listFiles().length);
    }

    /**
     * Creates a Project and an Artifact, opens two streams on the Artifact
     * in different revisions, closes one of the streams and then deletes the Artifact.
     * Checks if the storageManager closes the remaining stream and deletes
     * the artifact files.
     * 
     * @throws IOException
     */
    @Test
    public void testOpenAndCloseArtifact() throws IOException {
        String FILE = "Auto.cs";
        Project project = contentManager.createProject("testproject", "latex");
        storageManager.createProject(project);
        File file = getLocationForCSharpFile(FILE);
        OriginalFragmentLocation location = new FileSystemFragmentLocation(file);
        Artifact artifact = contentManager.createArtifact(project, fileSystemSource.getId(), location.getFullLocation(), artifactType, 1, dep(location), "Auto");
        storageManager.createArtifactContent(artifact, 1, location.getFullLocation(), dep(location));
        storageManager.createArtifactContent(artifact, 2, location.getFullLocation(), dep(location));
        storageManager.createArtifactContent(artifact, 3, location.getFullLocation(), dep(location));
        IPath ipDirInRepository = getRepositoryPath().append(project.getId()).append(artifact.getId());
        InputStream is = storageManager.getInputStream(artifact, 1);
        InputStream is2 = storageManager.getInputStream(artifact, 2);
        InputStream is3 = storageManager.getInputStream(artifact, 3);
        Assert.assertTrue(is.available() > 0);
        Assert.assertTrue(is2.available() > 0);
        Assert.assertTrue(is3.available() > 0);
        is2.close();
        boolean removed = storageManager.removeArtifactContent(artifact);
        Assert.assertTrue(removed);
        Assert.assertFalse(ipDirInRepository.toFile().exists());
    }
}
