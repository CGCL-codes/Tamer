package uk.ac.ebi.metabolights.metabolightsuploader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import junit.framework.Assert;
import org.apache.commons.io.FileUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.AnnotationConfiguration;
import org.isatools.tablib.utils.logging.TabLoggingEventWrapper;
import org.junit.BeforeClass;
import org.junit.Test;
import uk.ac.ebi.bioinvindex.model.Study;
import uk.ac.ebi.bioinvindex.model.VisibilityStatus;
import uk.ac.ebi.bioinvindex.model.security.User;
import uk.ac.ebi.metabolights.utils.FileUtil;
import uk.ac.ebi.metabolights.utils.Zipper;

public class IsaTabUploaderTest {

    private static final String ISA_TAB_CONFIG_FOLDER = "config/";

    static final String FOLDER_TEST_COPYTO_PUBLIC = "./src/test/resources/copyTo/public/";

    static final String FOLDER_TEST_COPYTO_PRIVATE = "./src/test/resources/copyTo/private/";

    static String lastStudyUploaded;

    static String userName;

    @BeforeClass
    public static void prepareTests() {
        File out = new File(IsaTabUploaderTest.FOLDER_TEST_COPYTO_PUBLIC);
        FileUtil.deleteDir(out);
        out.mkdir();
        out = new File(IsaTabUploaderTest.FOLDER_TEST_COPYTO_PRIVATE);
        FileUtil.deleteDir(out);
        out.mkdir();
    }

    @Test
    public void testSetters() {
        IsaTabUploader itu = new IsaTabUploader();
        itu.setIsaTabFile("foo");
        assertEquals("foo", itu.getIsaTabFile());
        itu.setUnzipFolder("unzip");
        assertEquals("unzip", itu.getUnzipFolder());
        itu.setOwner("owner");
        assertEquals("owner", itu.getOwner());
        itu.setCopyToPublicFolder("public/");
        assertEquals("public/", itu.getCopyToPublicFolder());
        itu.setCopyToPrivateFolder("private/");
        assertEquals("private/", itu.getCopyToPrivateFolder());
        assertEquals(VisibilityStatus.PUBLIC, itu.getStatus());
        assertEquals("public/MYSTUDY.zip", itu.getStudyFilePath("MYSTUDY", VisibilityStatus.PUBLIC));
        itu.setStatus(VisibilityStatus.PRIVATE);
        assertEquals(VisibilityStatus.PRIVATE, itu.getStatus());
        assertEquals("private/MYSTUDY.zip", itu.getStudyFilePath("MYSTUDY", VisibilityStatus.PRIVATE));
        itu.setDBConfigPath("configPath");
        assertEquals(new File("config").getAbsoluteFile() + "/", itu.getDBConfigPath());
        assertTrue("Test getOtherStatus with PUBLIC", itu.getOtherStatus(VisibilityStatus.PUBLIC) == VisibilityStatus.PRIVATE);
        assertTrue("Test getOtherStatus with PRIVATE", itu.getOtherStatus(VisibilityStatus.PRIVATE) == VisibilityStatus.PUBLIC);
        itu.setSubmissionDate("hello");
        assertEquals("Test Submission date property", "hello", itu.getSubmissionDate());
        itu.setPublicDate("byebye");
        assertEquals("Test public date property", "byebye", itu.getPublicDate());
    }

    @Test
    public void testConstructor() {
        IsaTabUploader itu = new IsaTabUploader("foo", "moo", "loo", "PUBLIC", "PRIVATE", VisibilityStatus.PRIVATE, "src", "2011-02-02", "2011-01-01");
        assertEquals("foo", itu.getIsaTabFile());
        assertEquals("moo", itu.getUnzipFolder());
        assertEquals("loo", itu.getOwner());
        assertEquals("PUBLIC", itu.getCopyToPublicFolder());
        assertEquals("PRIVATE", itu.getCopyToPrivateFolder());
        assertEquals(VisibilityStatus.PRIVATE, itu.getStatus());
        assertEquals("src", itu.getDBConfigPath());
    }

    @Test
    public void testChangeStatus() throws IOException {
        IsaTabUploader itu = new IsaTabUploader();
        File statusfile = new File(FOLDER_TEST_COPYTO_PUBLIC + "status.txt");
        statusfile.createNewFile();
        itu.changeFilePermissions(statusfile.getAbsolutePath(), VisibilityStatus.PUBLIC);
        testFilePermissions(statusfile.getAbsolutePath(), VisibilityStatus.PUBLIC);
    }

    @Test
    public void testChangeStudyFields() throws Exception {
        final String UNZIP_FOLDER = "src/test/resources/outputfiles/changestudyfields/";
        final String ZIP_FILE = FOLDER_TEST_COPYTO_PRIVATE + "STUDY1.zip";
        FileUtils.copyFile(new File("src/test/resources/inputfiles/isatab1.zip"), new File(ZIP_FILE));
        IsaTabUploader itu = new IsaTabUploader();
        itu.setCopyToPrivateFolder(FOLDER_TEST_COPYTO_PRIVATE);
        itu.setCopyToPublicFolder(FOLDER_TEST_COPYTO_PUBLIC);
        itu.setUnzipFolder(UNZIP_FOLDER);
        HashMap<String, String> replacement = new HashMap<String, String>();
        replacement.put("Study Public Release Date", "09/09/2010");
        itu.changeStudyFields("STUDY1", replacement);
        FileUtils.deleteDirectory(new File(UNZIP_FOLDER));
        Zipper.unzip2(ZIP_FILE, UNZIP_FOLDER);
        String investigationfile = FileUtil.file2String(UNZIP_FOLDER + "i_Investigation.txt");
        assertTrue("ChangeStudyFields test. Investigation file under " + UNZIP_FOLDER + " should have a Study Public Release Date of 09/09/2010", investigationfile.indexOf("Study Public Release Date\t\"09/09/2010\"") != -1);
    }

    @Test
    public void testGetStudyFields() throws Exception {
        final String UNZIP_FOLDER = "src/test/resources/outputfiles/getstudyfields/";
        final String ZIP_FILE = FOLDER_TEST_COPYTO_PRIVATE + "GETFIELDS.zip";
        FileUtils.copyFile(new File("src/test/resources/inputfiles/isatab1.zip"), new File(ZIP_FILE));
        IsaTabUploader itu = new IsaTabUploader();
        itu.setCopyToPrivateFolder(FOLDER_TEST_COPYTO_PRIVATE);
        itu.setCopyToPublicFolder(FOLDER_TEST_COPYTO_PUBLIC);
        itu.setUnzipFolder(UNZIP_FOLDER);
        Map<String, String> result = itu.getStudyFields("GETFIELDS", new String[] { "Study Identifier", "Study PubMed ID" });
        assertEquals("Number of values found in file.", 2, result.size());
        assertEquals("Study Identifier value test", "BII-S-6", result.get("Study Identifier"));
        assertEquals("Study PubMed ID value test", "17203948", result.get("Study PubMed ID"));
        result = itu.getStudyFields(new File(ZIP_FILE), new String[] { "Study Submission Date", "Study File Name" });
        assertEquals("Number of values found in file 2.", 2, result.size());
        assertEquals("Study Submission Date", "12/10/2004", result.get("Study Submission Date"));
        assertEquals("Study File Name", "s_BII-S-6.txt", result.get("Study File Name"));
    }

    private void testFilePermissions(String file, VisibilityStatus status) throws IOException {
        String permissions = getFilePesmissions(file);
        String mask = (status == VisibilityStatus.PRIVATE) ? "rwxr--r--" : "rwxr-xr-x";
        assertEquals("Testing mask of " + file, mask, permissions);
    }

    private String getFilePesmissions(String file) throws IOException {
        String s = null;
        Process p = Runtime.getRuntime().exec("ls -l " + file);
        BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
        s = stdInput.readLine();
        s = s.substring(1, 10);
        return s;
    }

    @Test
    public void testValidationOfValidISAFile() {
        final String ISA_ARCHIVE_FOLDER = "src/test/resources/inputfiles/ISACREATOR1.4_archive_FAST_valid/";
        IsaTabUploader itu = new IsaTabUploader();
        itu.setDBConfigPath(ISA_TAB_CONFIG_FOLDER);
        try {
            itu.validate(ISA_ARCHIVE_FOLDER);
        } catch (IsaTabException e) {
            fail("validate method raised an error: " + e.getMessage());
        }
    }

    @Test
    public void testValidationIncorrect() {
        IsaTabUploader itu = new IsaTabUploader();
        try {
            itu.validate("src/test/resources/inputfiles/foo/");
            fail("validate did not raised an error and it should do it with a wrong ISA TAB file");
        } catch (Exception e) {
        }
    }

    @Test
    public void testValidationIncorrectWithRealFile() {
        IsaTabUploader itu = new IsaTabUploader();
        try {
            itu.validate("src/test/resources/inputfiles/ISACREATOR1.4_archive_FAST_invalid/");
            fail("validate did not raised an error and it should do it with a wrong ISA TAB file");
        } catch (Exception e) {
        }
    }

    /**
	 * Tests loading an Isatab set of files into the database schema.
	 * You need to have a number of files and settings to make this work:
	 * <ul>
	 * <li>config/hibernate.properties is used for the target database details. Fill in test database details and
	 * mind the setting for hibernate.hbm2ddl.auto. This properties file also defines the Lucene index location. </li>
	 * <li>src/test/resources/am_hibernate.cfg.xml is redundantly used as well for the database details, like hibernate.properties</li>
	 * <li>config/data_locations.xml is used for the output of the uploaded file set</li>
	 * </ul>
	 * 
	 *  questions:
	 *   -there is an unzip folder. should that not be a UNIQUE folder.. to prevent concurrency problems. is that done in the webshite? 
	 *   -should the unzip folder content not be removed afterwards ... ?!
	 *   -what with data_locations.xml? why not just keep the zip file. or are these different files.. what's for download?
	 *   -should we perhaps dig through directories? how does the zip file look like, can it have multiple directories...? 
	 */
    @Test
    public void testUpload() {
        SessionFactory fac = new AnnotationConfiguration().configure().buildSessionFactory();
        Session session = fac.openSession();
        Transaction t = session.beginTransaction();
        userName = "UNSET";
        Query q = session.createQuery("FROM User");
        List<User> users = (List<User>) q.list();
        if (users.size() != 0) {
            userName = users.get(0).getUserName();
        }
        t.rollback();
        String path = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
        final String ISA_ARCHIVE = path + "inputfiles/zuker.zip";
        final String UNZIP_FOLDER = path + "outputfiles/zuker/";
        File folder = new File(UNZIP_FOLDER);
        FileUtil.deleteDir(folder);
        IsaTabUploader itu = new IsaTabUploader(ISA_ARCHIVE, UNZIP_FOLDER, userName, FOLDER_TEST_COPYTO_PUBLIC, FOLDER_TEST_COPYTO_PRIVATE, VisibilityStatus.PRIVATE, ISA_TAB_CONFIG_FOLDER, "2011-02-02", "2011-01-01");
        HashMap<String, String> ids = null;
        try {
            File unzipFolder = new File(UNZIP_FOLDER);
            if (!unzipFolder.exists()) unzipFolder.mkdir();
            File foo = new File(UNZIP_FOLDER + "supercalifrajilisticoespialidoso.txt");
            foo.createNewFile();
            ids = itu.Upload();
            assertTrue("testUpload successful", true);
            assertEquals("Study id's assigned ", 1, ids.size());
            for (String key : ids.keySet()) {
                assertEquals("Original ID ", "I00649", key);
                lastStudyUploaded = ids.get(key);
            }
            t = session.beginTransaction();
            q = session.createQuery("FROM Study WHERE acc = :acc");
            q.setString("acc", lastStudyUploaded);
            Study study = (Study) q.uniqueResult();
            Assert.assertEquals("Title", "Zuker rat urine NMR study", study.getTitle());
            Assert.assertEquals("Description", "Zuker rat urine study by NMR", study.getDescription());
            for (User owner : study.getUsers()) {
                Assert.assertEquals("Owner", userName, owner.getUserName());
            }
            t.rollback();
            assertTrue("Clear unzip folder before extraction", foo.exists() == false);
            Collection<String> studyCol = ids.values();
            lastStudyUploaded = studyCol.iterator().next();
            String outputzip = itu.getStudyFilePath(lastStudyUploaded, itu.getStatus());
            assertTrue("Check if hte new zip file has been created and copy to the right folder", new File(outputzip).exists());
        } catch (Exception e) {
            fail("testupload threw an exception: " + e.getMessage());
        }
    }

    /**
	 * This needs to be tested after testUpload, where lastStudyUploaded is set with a valid study.
	 * So initially we have lastStudyUploaded and public.
	 * @throws Exception 
	 */
    @Test
    public void testChangeStudyStatus() throws Exception {
        IsaTabUploader itu = new IsaTabUploader();
        itu.setCopyToPublicFolder(FOLDER_TEST_COPYTO_PUBLIC);
        itu.setCopyToPrivateFolder(FOLDER_TEST_COPYTO_PRIVATE);
        itu.setDBConfigPath(ISA_TAB_CONFIG_FOLDER);
        itu.setOwner(userName);
        Document studyInLucene = getLuceneStudyDocument(lastStudyUploaded);
        String privateStudyFilePath = itu.getStudyFilePath(lastStudyUploaded, VisibilityStatus.PRIVATE);
        assertEquals("Study " + lastStudyUploaded + " shuld be initially PRIVATE.", "PRIVATE", studyInLucene.getValues("status")[0]);
        testFilePermissions(privateStudyFilePath, VisibilityStatus.PRIVATE);
        itu.PublishStudy(lastStudyUploaded);
        studyInLucene = getLuceneStudyDocument(lastStudyUploaded);
        assertEquals("Study " + lastStudyUploaded + " shuld be now PUBLIC.", "PUBLIC", studyInLucene.getValues("status")[0]);
        String publicStudyFilePath = itu.getStudyFilePath(lastStudyUploaded, VisibilityStatus.PUBLIC);
        testFilePermissions(publicStudyFilePath, VisibilityStatus.PUBLIC);
        assertTrue("Zip file (" + privateStudyFilePath + ") must have been removed from the private folder", (new File(privateStudyFilePath).exists() == false));
    }

    /**
	 * This needs to be tested after testUpload, where lastStudyUploaded is set with a valid study.
	 * So initially we have lastStudyUploaded and public.
	 * @throws IsaTabException 
	 */
    @Test
    public void testRemoveStudy() throws IsaTabException {
        IsaTabUploader itu = new IsaTabUploader();
        itu.setCopyToPublicFolder(FOLDER_TEST_COPYTO_PUBLIC);
        itu.setCopyToPrivateFolder(FOLDER_TEST_COPYTO_PRIVATE);
        itu.setDBConfigPath(ISA_TAB_CONFIG_FOLDER);
        itu.setOwner(userName);
        itu.unloadISATabFile(lastStudyUploaded);
        String path = itu.getStudyFilePath(lastStudyUploaded, VisibilityStatus.PUBLIC);
        assertTrue("Zip file (" + path + ") must have been removed after the unload process.", (new File(path).exists() == false));
    }

    private String IsaTabLogToString(List<TabLoggingEventWrapper> log) {
        String result = "";
        for (TabLoggingEventWrapper line : log) {
            result = result + line.getFormattedMessage() + "\n";
        }
        return result;
    }

    @SuppressWarnings("deprecation")
    private Document getLuceneStudyDocument(String study) throws ParseException, IOException {
        Properties isaTabProps = new Properties();
        isaTabProps.load(new FileInputStream(ISA_TAB_CONFIG_FOLDER + "hibernate.properties"));
        String luceneDirectory = isaTabProps.getProperty("hibernate.search.default.indexBase");
        Directory directory = FSDirectory.getDirectory(luceneDirectory + "/bii", false);
        IndexSearcher searcher = new IndexSearcher(directory);
        org.apache.lucene.search.Query query = new TermQuery(new Term("acc", study));
        TopDocs rs = searcher.search(query, null, 10);
        Document firstHit = searcher.doc(rs.scoreDocs[0].doc);
        return firstHit;
    }

    @Test
    public void testReindex() {
        IsaTabUploader itu = new IsaTabUploader();
        itu.setDBConfigPath(ISA_TAB_CONFIG_FOLDER);
        try {
            itu.reindex();
        } catch (Exception e) {
            fail("Reindex test failed: " + e.getMessage());
        }
    }
}
