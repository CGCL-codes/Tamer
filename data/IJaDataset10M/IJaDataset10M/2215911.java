package au.edu.diasb.annotation.danno.test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Arrays;
import junit.framework.TestCase;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import au.edu.diasb.annotation.danno.DannoControllerConfig;
import au.edu.diasb.annotation.danno.db.RDFDBContainerPool;
import au.edu.diasb.annotation.danno.importer.FileUploadBean;
import au.edu.diasb.annotation.danno.importer.FileUploadValidator;
import au.edu.diasb.annotation.danno.importer.ImporterRegistry;
import au.edu.diasb.annotation.danno.model.TripleStoreException;
import au.edu.diasb.danno.constants.DannoMimeTypes;

/**
 * Unit tests for the FileUploadValidator class.
 * 
 * @author scrawley
 */
public abstract class FileUploadValidatorTestBase extends TestCase {

    private static ImporterRegistry importers = new ImporterRegistry();

    static {
        importers.setEntries(Arrays.asList(new String[] { "application/X-pliny-annotations=" + "au.edu.diasb.annotation.danno.importer.pliny.PlinyAnnoteaImporterFactory" }));
    }

    private RDFDBContainerPool pool;

    private DannoControllerConfig config;

    public void testNoCommand() throws Exception {
        FileUploadBean bean = createFileUpload("/pliny-sample-1.pla", null, DannoMimeTypes.PLINY_MIME_TYPE, null, "http://fred", "me");
        Errors errors = runTest(bean);
        checkError(errors, "command", "'command' parameter is missing");
    }

    private Errors runTest(FileUploadBean bean) {
        Errors errors = new BindException(bean, "test");
        FileUploadValidator validator = new FileUploadValidator();
        validator.setImporters(importers);
        validator.setConfig(config);
        validator.validate(bean, errors);
        return errors;
    }

    private void checkError(Errors errors, String field, String message) {
        FieldError fe = errors.getFieldError(field);
        assertNotNull(fe);
        assertEquals(message, fe.getDefaultMessage());
    }

    public void testBadCommand() throws Exception {
        FileUploadBean bean = createFileUpload("/pliny-sample-1.pla", "weeble", DannoMimeTypes.PLINY_MIME_TYPE, null, "http://fred", "me");
        Errors errors = runTest(bean);
        checkError(errors, "command", "unrecognized 'command' parameter");
    }

    public void testNoMimeType() throws Exception {
        FileUploadBean bean = createFileUpload("/pliny-sample-1.pla", "import", null, null, "http://fred", "me");
        Errors errors = runTest(bean);
        checkError(errors, "mimeType", "'mimeType' parameter is required for this request");
    }

    public void testBadMimeType() throws Exception {
        FileUploadBean bean = createFileUpload("/pliny-sample-1.pla", "import", "marcel marceau", null, "http://fred", "me");
        Errors errors = runTest(bean);
        checkError(errors, "mimeType", "mimeType is not importable");
    }

    public void testMissingFile() throws Exception {
        FileUploadBean bean = createFileUpload(null, "import", DannoMimeTypes.PLINY_MIME_TYPE, null, "http://fred", "me");
        Errors errors = runTest(bean);
        checkError(errors, "file", "'file' parameter is required for this request");
    }

    public void testMissingCreator() throws Exception {
        FileUploadBean bean = createFileUpload(null, "import", DannoMimeTypes.PLINY_MIME_TYPE, null, "http://fred", null);
        Errors errors = runTest(bean);
        checkError(errors, "creator", "'creator' parameter is missing");
    }

    public void testEmptyFile() throws Exception {
        FileUploadBean bean = createFileUpload("/empty", "import", DannoMimeTypes.PLINY_MIME_TYPE, null, "http://fred", "me");
        Errors errors = runTest(bean);
        checkError(errors, "file", "the supplied file is empty");
    }

    public void testMissingCollectionInfo() throws Exception {
        FileUploadBean bean = createFileUpload("/pliny-sample-1.pla", "import", DannoMimeTypes.PLINY_MIME_TYPE, null, null, "me");
        Errors errors = runTest(bean);
        assertEquals(0, errors.getErrorCount());
    }

    public void testMissingCollectionInfo2() throws Exception {
        FileUploadBean bean = createFileUpload(null, "update", DannoMimeTypes.PLINY_MIME_TYPE, null, null, "me");
        Errors errors = runTest(bean);
        checkError(errors, "collection", "either a 'collection' or 'collectionId' is required");
        checkError(errors, "collectionId", "either a 'collection' or 'collectionId' is required");
    }

    private FileUploadBean createFileUpload(String path, String command, String mimeType, String collection, String collectionId, String creator) throws IOException {
        FileUploadBean bean = new FileUploadBean();
        bean.setMimeType(mimeType);
        bean.setCommand(command);
        bean.setCollection(collection);
        bean.setCollectionId(collectionId);
        bean.setCreator(creator);
        if (path != null) {
            InputStream is = TestUtils.getResourceAsStream(path);
            try {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                byte[] buffer = new byte[8192];
                int nosRead;
                while ((nosRead = is.read(buffer)) > 0) {
                    bos.write(buffer, 0, nosRead);
                }
                bean.setFile(new MockMultipartFile("theFile", bos.toByteArray()));
            } finally {
                is.close();
            }
        }
        return bean;
    }

    @Override
    protected void setUp() throws Exception {
        if (pool == null) {
            pool = createPool();
        }
        pool.eraseAllState();
        config = new DannoControllerConfig();
        config.setContainerFactory(pool);
        config.setProperties(TestProperties.getProperties());
    }

    public void tearDown() {
        pool.shutdown();
        pool = null;
    }

    protected abstract RDFDBContainerPool createPool() throws SQLException, TripleStoreException;
}
