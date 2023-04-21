package com.goodcodeisbeautiful.archtea.config;

import java.io.File;
import java.io.FileOutputStream;
import java.util.LinkedList;
import java.util.List;
import com.goodcodeisbeautiful.archtea.util.factory.FileFactoryConfig;
import com.goodcodeisbeautiful.test.util.CommonTestCase;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author hata
 *
 */
public class DefaultFolderConfigTest extends CommonTestCase {

    private static final String SAMPLE_FILE_CACHE = "sample-file-cache-2.xml";

    private static final String SAMPLE_DATA_CONTAINER = "sample-data-container-2.xml";

    private static final String SAMPLE_ENTRY_FACTORY = "sample-entry-factory-2.xml";

    public static Test suite() {
        return new TestSuite(DefaultFolderConfigTest.class);
    }

    DefaultFolderConfig m_folder;

    protected List getSetupFilenames() {
        LinkedList l = new LinkedList();
        l.add(SAMPLE_FILE_CACHE);
        l.add(SAMPLE_DATA_CONTAINER);
        l.add(SAMPLE_ENTRY_FACTORY);
        return l;
    }

    protected void setUp() throws Exception {
        super.setUp();
        m_folder = new DefaultFolderConfig(null, new File(getWorkDir()));
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        m_folder = null;
        cleanWorkFiles();
    }

    public void testDefaultFolderConfig() {
        assertNotNull(m_folder);
    }

    public void testGetName() {
        assertNull(m_folder.getName());
        m_folder.setName("main");
        assertEquals("Test simple name.", "main", m_folder.getName());
    }

    public void testGetRootPath() throws Exception {
        assertNull(m_folder.getRootPath());
        m_folder.setRootPath(getWorkDir());
        assertEquals("Test abs path for getRootPath", getWorkDir(), m_folder.getRootPath());
        m_folder.setRootPath(".");
        assertEquals("Test relative path for getRootPath", ".", m_folder.getRootPath());
    }

    public void testGetAbsoluteRootPath() throws Exception {
        assertNull(m_folder.getRootPath());
        m_folder.setRootPath(getWorkDir());
        assertEquals("Test abs path for getAbsoluteRootPath", getWorkDir(), m_folder.getAbsoluteRootPath());
        m_folder.setRootPath(".");
        assertEquals("Test relative path for getRootPath", getWorkDir(), m_folder.getAbsoluteRootPath());
    }

    public void testGetProperty() {
        assertNull(m_folder.getProperty("key"));
        m_folder.setProperty("key", "value");
        assertEquals("test simple property.", "value", m_folder.getProperty("key"));
    }

    public void testGetBooleanProperty() {
        assertNull(m_folder.getProperty("key"));
        m_folder.setProperty("key", "true");
        assertTrue("test boolean property.", m_folder.getBooleanProperty("key", false));
        m_folder.setProperty("key", "True");
        assertTrue("test boolean property.", m_folder.getBooleanProperty("key", false));
        m_folder.setProperty("key", "TRUE");
        assertTrue("test boolean property.", m_folder.getBooleanProperty("key", false));
        m_folder.setProperty("key", "on");
        assertTrue("test boolean property.", m_folder.getBooleanProperty("key", false));
        assertFalse("test boolean property.", m_folder.getBooleanProperty("key1", false));
    }

    public void testArchteaFileCacheConfigDefaultConfig() throws Exception {
        try {
            m_folder.getArchteaFileCacheConfig();
            fail("This call should throw an exception");
        } catch (ArchteaConfigException e) {
        }
        m_folder.setArchteaFileCacheConfigPath(getWorkDir() + File.separator + "filecache-config.xml");
        assertNotNull(m_folder.getArchteaFileCacheConfig());
        try {
            m_folder.setArchteaFileCacheConfigPath(getWorkDir() + File.separator + SAMPLE_FILE_CACHE);
            fail("Cannot overwrite existing config path.");
        } catch (ArchteaConfigException e) {
        }
    }

    public void testArchteaFileCacheConfigExistConfig() throws Exception {
        try {
            m_folder.getArchteaFileCacheConfig();
            fail("This call should throw an exception");
        } catch (ArchteaConfigException e) {
        }
        m_folder.setArchteaFileCacheConfigPath(getWorkDir() + File.separator + SAMPLE_FILE_CACHE);
        ArchteaFileCacheConfig cfg = m_folder.getArchteaFileCacheConfig();
        assertNotNull(cfg);
        assertEquals("Check the config is created an existing config file.", "sample-root", cfg.getRootPath());
        try {
            m_folder.setArchteaFileCacheConfigPath(getWorkDir() + File.separator + "sample-file-cache.xml");
            fail("Cannot overwrite existing config path.");
        } catch (ArchteaConfigException e) {
        }
    }

    public void testGetDataContainerConfigDefault() throws Exception {
        try {
            m_folder.getDataContainerConfig();
            fail("This call should throw an exception");
        } catch (ArchteaConfigException e) {
        }
        m_folder.setDataContainerConfigPath(getWorkDir() + File.separator + "datacontainer-config.xml");
        assertNotNull("create default config.", m_folder.getDataContainerConfig());
        try {
            m_folder.setDataContainerConfigPath(getWorkDir() + File.separator + SAMPLE_DATA_CONTAINER);
            fail("Cannot overwrite the configured path.");
        } catch (ArchteaConfigException e) {
        }
    }

    public void testGetDataContainerConfigExist() throws Exception {
        try {
            m_folder.getDataContainerConfig();
            fail("This call should throw an exception");
        } catch (ArchteaConfigException e) {
        }
        m_folder.setDataContainerConfigPath(getWorkDir() + File.separator + SAMPLE_DATA_CONTAINER);
        ArchteaFactoryContainerConfig cfg = m_folder.getDataContainerConfig();
        assertNotNull(cfg);
        FileFactoryConfig fc = (FileFactoryConfig) cfg.getFactoryConfigs().get(0);
        fc.getMatchType();
        assertTrue("Check using existing config file.", fc.includes().contains("sample2"));
        try {
            m_folder.setDataContainerConfigPath(getWorkDir() + File.separator + "sample-data-container.xml");
            fail("Cannot overwrite the configured path.");
        } catch (ArchteaConfigException e) {
        }
    }

    public void testGetEntryFactoryConfigDefault() throws Exception {
        try {
            m_folder.getEntryFactoryConfig();
            fail("This call should throw an exception");
        } catch (ArchteaConfigException e) {
        }
        m_folder.setEntryFactoryConfigPath(getWorkDir() + File.separator + "entry-factory-config-2.xml");
        assertNotNull(m_folder.getEntryFactoryConfig());
        try {
            m_folder.setEntryFactoryConfigPath(getWorkDir() + File.separator + SAMPLE_ENTRY_FACTORY);
            fail("Cannot overwrite an existing config.");
        } catch (ArchteaConfigException e) {
        }
    }

    public void testGetEntryFactoryConfigExist() throws Exception {
        try {
            m_folder.getEntryFactoryConfig();
            fail("This call should throw an exception");
        } catch (ArchteaConfigException e) {
        }
        m_folder.setEntryFactoryConfigPath(getWorkDir() + File.separator + SAMPLE_ENTRY_FACTORY);
        ArchteaFactoryContainerConfig cfg = m_folder.getEntryFactoryConfig();
        assertNotNull(cfg);
        FileFactoryConfig fc = (FileFactoryConfig) cfg.getFactoryConfigs().get(0);
        fc.getMatchType();
        assertTrue("Check using existing config file.", fc.includes().contains("sampleEntryFactory"));
        try {
            m_folder.setEntryFactoryConfigPath(getWorkDir() + File.separator + "sample-entry-factory.xml");
            fail("Cannot overwrite an existing config.");
        } catch (ArchteaConfigException e) {
        }
    }

    public void testSetName() {
        testGetName();
    }

    public void testSetRootPath() throws Exception {
        testGetRootPath();
    }

    public void testSetProperty() {
        assertNull(m_folder.getProperty("key"));
        m_folder.setProperty("key", "value");
        assertEquals("value", m_folder.getProperty("key"));
        m_folder.setProperty("key", "value2");
        assertEquals("value2", m_folder.getProperty("key"));
    }

    public void testIsModifiedForFileCache() throws Exception {
        m_folder.setArchteaFileCacheConfigPath(getWorkDir() + File.separator + SAMPLE_FILE_CACHE);
        assertNotNull(m_folder.getArchteaFileCacheConfig());
        assertFalse(m_folder.isModified());
        Thread.sleep(1001);
        FileOutputStream out = new FileOutputStream(getWorkDir() + File.separator + SAMPLE_FILE_CACHE, true);
        out.write(' ');
        out.flush();
        out.close();
        Thread.sleep(1000);
        assertTrue(m_folder.isModified());
    }

    public void testIsModifiedForDataContainer() throws Exception {
        m_folder.setDataContainerConfigPath(getWorkDir() + File.separator + SAMPLE_DATA_CONTAINER);
        assertNotNull(m_folder.getDataContainerConfig());
        assertFalse(m_folder.isModified());
        Thread.sleep(1001);
        FileOutputStream out = new FileOutputStream(getWorkDir() + File.separator + SAMPLE_DATA_CONTAINER, true);
        out.write(' ');
        out.flush();
        out.close();
        Thread.sleep(1000);
        assertTrue(m_folder.isModified());
    }

    public void testIsModifiedForEntryFactory() throws Exception {
        m_folder.setEntryFactoryConfigPath(getWorkDir() + File.separator + SAMPLE_ENTRY_FACTORY);
        assertNotNull(m_folder.getEntryFactoryConfig());
        assertFalse(m_folder.isModified());
        Thread.sleep(2001);
        FileOutputStream out = new FileOutputStream(getWorkDir() + File.separator + SAMPLE_ENTRY_FACTORY, true);
        out.write(' ');
        out.flush();
        out.close();
        Thread.sleep(1000);
        assertTrue(m_folder.isModified());
    }
}
