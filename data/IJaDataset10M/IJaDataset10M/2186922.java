package org.proteusframework.platformservice.persistence.basic.tests;

import org.junit.Before;
import org.junit.Test;
import org.proteusframework.core.base.Namespace;
import org.proteusframework.platformservice.*;
import org.proteusframework.platformservice.persistence.api.IProjectDescriptor;
import org.proteusframework.platformservice.persistence.api.IVFSFile;
import org.proteusframework.platformservice.persistence.basic.DefaultMessageBeanManager;
import org.proteusframework.platformservice.persistence.basic.DefaultPersistenceService;
import org.proteusframework.platformservice.persistence.project.ProjectDescriptor;
import org.proteusframework.platformservice.persistence.vfs.FilePath;
import org.proteusframework.test.UnitTestAppConfigService;
import org.proteusframework.platformservice.UnitTestDelegate;
import org.proteusframework.test.UnitTestLifecycleService;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class VFSManagerTest extends DefaultPersistenceService {

    private static Logger logger = Logger.getLogger(MessageBeanManagerTest.class.getName());

    private final UnitTestAppConfigService appConfigService = new UnitTestAppConfigService();

    private final UnitTestLifecycleService appLifecycleService = new UnitTestLifecycleService();

    @Before
    public void testProjectCreation() {
        try {
            final boolean deleteOnExitFlag = appConfigService.getMetadata().getProperty(UnitTestAppConfigService.TEMP_DIR_DELETE).equals("true");
            logger.config("Delete Tmp Dir on Exit: " + deleteOnExitFlag);
            if (deleteOnExitFlag) {
                File f = new File(appConfigService.getDefaultProjectsDirectory());
                f.deleteOnExit();
            }
            IExtendedPlatformDelegate delegate = new UnitTestDelegate(appConfigService, this, appLifecycleService);
            logger.fine("Injected test version of IExtendedPlatformDelegate");
            setPlatformDelegate(delegate, IExtendedPlatformDelegate.class);
            injectMessageBeanManager(new DefaultMessageBeanManager());
            createVFSManager(false);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    @Test
    public void testSupport() {
        boolean support = supportsVFSManagement();
        assertTrue(support);
    }

    @Test(expected = IllegalStateException.class)
    public void testCreateFileNoProjectOpen() {
        FilePath fp = new FilePath("test.txt");
        createFile(fp);
    }

    @Test(expected = IllegalStateException.class)
    public void testHasFileNoProjectOpen() {
        hasFile(new FilePath("test.txt"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetFilesNoProjectOpen() {
        getFiles(new FilePath("test"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetFileNoProjectOpen() {
        getFile(new FilePath("test.txt"));
    }

    @Test(expected = IllegalStateException.class)
    public void testDeleteFileFPNoProjectOpen() {
        deleteFile(new FilePath("test.txt"));
    }

    @Test(expected = IllegalStateException.class)
    public void testDeleteFileIVFSFNoProjectOpen() {
        IVFSFile vfsFile = new IVFSFile() {

            @Override
            public File getFile() {
                return null;
            }

            @Override
            public long length() {
                return 0;
            }

            @Override
            public boolean canExecute() {
                return false;
            }

            @Override
            public boolean canRead() {
                return false;
            }

            @Override
            public boolean canWrite() {
                return false;
            }

            @Override
            public int compareTo(File pathname) {
                return 0;
            }

            @Override
            public int compareTo(IVFSFile file) {
                return 0;
            }

            @Override
            public boolean exists() {
                return false;
            }

            @Override
            public String getName() {
                return null;
            }

            @Override
            public FilePath getParentFilePath() {
                return null;
            }

            @Override
            public String getPath() {
                return null;
            }

            @Override
            public FilePath getFilePath() {
                return new FilePath("empty");
            }

            @Override
            public String getAbsolutePath() {
                return null;
            }

            @Override
            public boolean isDirectory() {
                return false;
            }

            @Override
            public boolean isFile() {
                return false;
            }

            @Override
            public boolean isHidden() {
                return false;
            }

            @Override
            public long lastModified() {
                return 0;
            }

            @Override
            public boolean setExecutable(boolean executableFlag) {
                return false;
            }

            @Override
            public boolean setExecutable(boolean executableFlag, boolean ownerOnlyFlag) {
                return false;
            }

            @Override
            public boolean setLastModified(long time) {
                return false;
            }

            @Override
            public boolean setReadable(boolean readableFlag) {
                return false;
            }

            @Override
            public boolean setReadable(boolean readableFlag, boolean ownerOnlyFlag) {
                return false;
            }

            @Override
            public boolean setReadOnly() {
                return false;
            }

            @Override
            public boolean setWritable(boolean writableFlag) {
                return false;
            }

            @Override
            public boolean setWritable(boolean writableFlag, boolean ownerOnlyFlag) {
                return false;
            }

            @Override
            public boolean supportsMetadata() {
                return false;
            }

            @Override
            public boolean supportsIcon() {
                return false;
            }

            @Override
            public URL getGlyphURL() {
                return null;
            }

            @Override
            public Properties getMetadata() {
                return null;
            }

            @Override
            public String getFamily() {
                return null;
            }

            @Override
            public String getId() {
                return null;
            }

            @Override
            public String getRefId() {
                return null;
            }
        };
        deleteFile(vfsFile);
    }

    @Test
    public void createFile() throws ServiceInitializationException {
        IProjectDescriptor descriptor = new ProjectDescriptor(new Namespace(appConfigService.getDefaultProjectsDirectory(), "VFS1"));
        try {
            create(descriptor);
            boolean isOpen = isProjectOpen();
            assertTrue(isOpen);
            FilePath fp = new FilePath("test1.txt");
            createFile(fp);
            close();
        } catch (org.proteusframework.platformservice.persistence.project.ProjectInitializationException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void hasFile() throws ServiceInitializationException, IOException {
        IProjectDescriptor descriptor = new ProjectDescriptor(new Namespace(appConfigService.getDefaultProjectsDirectory(), "VFS2"));
        try {
            create(descriptor);
            boolean isOpen = isProjectOpen();
            assertTrue(isOpen);
            FilePath fp = new FilePath("test2.txt");
            IVFSFile file = createFile(fp);
            writeDummyData(file);
            boolean exists = hasFile(fp);
            assertTrue(exists);
            close();
        } catch (org.proteusframework.platformservice.persistence.project.ProjectInitializationException e) {
            e.printStackTrace();
        }
    }

    private void writeDummyData(IVFSFile file) throws IOException {
        FileWriter writer = null;
        try {
            writer = new FileWriter(file.getFile());
            writer.write("This is a test file");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                if (null != writer) {
                    writer.close();
                }
            } catch (IOException ignore) {
            }
        }
    }

    @Test
    public void getFiles() throws ServiceInitializationException, IOException {
        IProjectDescriptor descriptor = new ProjectDescriptor(new Namespace(appConfigService.getDefaultProjectsDirectory(), "VFS3"));
        try {
            create(descriptor);
            boolean isOpen = isProjectOpen();
            assertTrue(isOpen);
            FilePath fp1 = new FilePath("test1.txt");
            writeDummyData(createFile(fp1));
            FilePath fp2 = new FilePath("test2.txt");
            writeDummyData(createFile(fp2));
            FilePath fp3 = new FilePath("test3.txt");
            writeDummyData(createFile(fp3));
            List<IVFSFile> files = getFiles(new FilePath("/"));
            assertTrue(files.size() == 3);
            close();
        } catch (org.proteusframework.platformservice.persistence.project.ProjectInitializationException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getFile() throws ServiceInitializationException, IOException {
        IProjectDescriptor descriptor = new ProjectDescriptor(new Namespace(appConfigService.getDefaultProjectsDirectory(), "VFS4"));
        try {
            create(descriptor);
            boolean isOpen = isProjectOpen();
            assertTrue(isOpen);
            FilePath fp1 = new FilePath("test1.txt");
            writeDummyData(createFile(fp1));
            IVFSFile file = getFile(new FilePath("test1.txt"));
            assertTrue(new FilePath(file.getPath()).equals(fp1.getPath()));
            close();
        } catch (org.proteusframework.platformservice.persistence.project.ProjectInitializationException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void deleteVFSFile() throws ServiceInitializationException, IOException {
        IProjectDescriptor descriptor = new ProjectDescriptor(new Namespace(appConfigService.getDefaultProjectsDirectory(), "VFS5"));
        try {
            create(descriptor);
            boolean isOpen = isProjectOpen();
            assertTrue(isOpen);
            FilePath fp1 = new FilePath("test1.txt");
            writeDummyData(createFile(fp1));
            IVFSFile file = getFile(new FilePath("test1.txt"));
            assertTrue(new FilePath(file.getPath()).equals(fp1.getPath()));
            deleteFile(file);
            boolean exists = hasFile(fp1.getPath());
            assertFalse(exists);
            close();
        } catch (org.proteusframework.platformservice.persistence.project.ProjectInitializationException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void deleteFPFile() throws ServiceInitializationException, IOException {
        IProjectDescriptor descriptor = new ProjectDescriptor(new Namespace(appConfigService.getDefaultProjectsDirectory(), "VFS5"));
        try {
            create(descriptor);
            boolean isOpen = isProjectOpen();
            assertTrue(isOpen);
            FilePath fp1 = new FilePath("test1.txt");
            writeDummyData(createFile(fp1));
            IVFSFile file = getFile(new FilePath("test1.txt"));
            assertTrue(new FilePath(file.getPath()).equals(fp1.getPath()));
            deleteFile(fp1);
            boolean exists = hasFile(fp1.getPath());
            assertFalse(exists);
            close();
        } catch (org.proteusframework.platformservice.persistence.project.ProjectInitializationException e) {
            e.printStackTrace();
        }
    }
}
