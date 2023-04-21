package test.com.gestioni.adoc.aps.system.services.repository.documento.personale;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import test.com.gestioni.adoc.aps.JcrRepositoryBaseTestCase;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.keygenerator.IKeyGeneratorManager;
import com.gestioni.adoc.aps.system.AdocSystemConstants;
import com.gestioni.adoc.aps.system.services.documento.personale.DocumentoPersonale;
import com.gestioni.adoc.aps.system.services.repository.documento.personale.IRepositoryDocumentoPersonaleManager;

public class TestRepositoryDocumentoPersonaleManager extends JcrRepositoryBaseTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.init();
    }

    public void testInsertDocument() throws Throwable {
        String uuid = null;
        InputStream in = null;
        try {
            DocumentoPersonale documento = this.createDocumentoPersonale("DELETE_TITOLO", String.valueOf(ADD_GIALLI_FORMAZ), null);
            documento.setId(String.valueOf(_keyManager.getUniqueKeyCurrentValue()));
            File file = new File(FILE_DOCUMENT_JPG_PATHNAME);
            assertTrue(file.length() > 0);
            uuid = _repositoryDocPersonaleManager.add(String.valueOf(ADD_GIALLI_FORMAZ), documento, file, file.getName());
            assertNotNull(uuid);
            in = _repositoryDocPersonaleManager.getInputStreamByDocumentUUID(String.valueOf(ADD_GIALLI_FORMAZ), uuid);
            assertTrue(compareAndRemoveTempFile(file, in, FILE_DOCUMENT_JPG_TEMP_PATHNAME));
            String nome = _repositoryDocPersonaleManager.getFileNameByDocumentUUID(String.valueOf(ADD_GIALLI_FORMAZ), uuid);
            assertEquals(file.getName(), nome);
        } finally {
            if (null != uuid) {
                this._repositoryDocPersonaleManager.removeByUUID(String.valueOf(ADD_GIALLI_FORMAZ), uuid);
            }
            if (null != in) {
                in.close();
            }
        }
    }

    public void testUpdateDocument() throws Throwable {
        String uuid = null;
        File out_txt = null;
        File out_jpg = null;
        InputStream in = null;
        try {
            DocumentoPersonale documento = this.createDocumentoPersonale("DELETE_TITOLO", String.valueOf(ADD_GIALLI_FORMAZ), null);
            documento.setId(String.valueOf(_keyManager.getUniqueKeyCurrentValue()));
            File file_jpg = new File(FILE_DOCUMENT_JPG_PATHNAME);
            uuid = _repositoryDocPersonaleManager.add(String.valueOf(ADD_GIALLI_FORMAZ), documento, file_jpg, file_jpg.getName());
            assertNotNull(uuid);
            in = _repositoryDocPersonaleManager.getInputStreamByDocumentUUID(String.valueOf(ADD_GIALLI_FORMAZ), uuid);
            assertTrue(compareAndRemoveTempFile(file_jpg, in, FILE_DOCUMENT_JPG_TEMP_PATHNAME));
            documento.setTitolo("DELETE_TITOLO_update");
            File file_txt_update = new File(FILE_DOCUMENT_TXT_PATHNAME);
            _repositoryDocPersonaleManager.update(String.valueOf(ADD_GIALLI_FORMAZ), documento, file_txt_update, file_txt_update.getName());
            in = _repositoryDocPersonaleManager.getInputStreamByDocumentUUID(String.valueOf(ADD_GIALLI_FORMAZ), uuid);
            assertTrue(compareAndRemoveTempFile(file_txt_update, in, FILE_DOCUMENT_TXT_PATHNAME));
            String nome = _repositoryDocPersonaleManager.getFileNameByDocumentUUID(String.valueOf(ADD_GIALLI_FORMAZ), uuid);
            assertEquals(file_txt_update.getName(), nome);
            _repositoryDocPersonaleManager.removeById(String.valueOf(ADD_GIALLI_FORMAZ), documento.getId());
            DocumentoPersonale docPers = _repositoryDocPersonaleManager.getDocumentoPersonale(String.valueOf(ADD_GIALLI_FORMAZ), uuid);
            assertNull(docPers);
        } finally {
            if (null != uuid) {
                this._repositoryDocPersonaleManager.removeByUUID(String.valueOf(ADD_GIALLI_FORMAZ), uuid);
            }
            if (null != out_jpg) {
                out_jpg.delete();
            }
            if (null != out_txt) {
                out_txt.delete();
            }
            if (null != in) {
                in.close();
            }
        }
    }

    private boolean compareAndRemoveTempFile(File origin_file_jpg, InputStream fileToCompare, String temp_pathname) throws Throwable {
        boolean isEquals = false;
        File out_jpg = null;
        OutputStream outStream = null;
        try {
            out_jpg = new File(FILE_DOCUMENT_JPG_TEMP_PATHNAME);
            ;
            outStream = new FileOutputStream(out_jpg);
            byte buf[] = new byte[1024];
            int len;
            while ((len = fileToCompare.read(buf)) > 0) {
                outStream.write(buf, 0, len);
            }
            outStream.close();
            fileToCompare.close();
            if (out_jpg.length() != 0 && origin_file_jpg.length() == out_jpg.length()) {
                isEquals = true;
            }
            out_jpg.delete();
        } catch (Exception e) {
            throw e;
        } finally {
            if (out_jpg != null) {
                out_jpg.delete();
            }
            if (outStream != null) {
                outStream.close();
            }
        }
        return isEquals;
    }

    /**
	 * Crea un Documento con dei parametri di dafault. vedi this.createMetadata e this.createAttributesForTES
	 * @param titolo
	 * @param autore
	 * @param metadata fa l'override dei valori di uno o più metadati
	 * @param attributes fa l'override dei valori di uno o più attributi
	 * @return
	 */
    private DocumentoPersonale createDocumentoPersonale(String titolo, String autore, Map<String, Object> metadata) {
        DocumentoPersonale documento = new DocumentoPersonale();
        if (null == metadata) {
            metadata = this.createMetadata(null);
        } else {
            metadata = this.createMetadata(metadata);
        }
        documento.setAutore(autore);
        documento.setVersione((String) metadata.get("DELETE_versione"));
        documento.setTitolo((String) metadata.get(titolo));
        documento.setSunto((String) metadata.get("DELETE_sunto"));
        documento.setDettaglio((String) metadata.get("dettaglio"));
        documento.setTags((String) metadata.get("tags"));
        return documento;
    }

    /**
	 * Genera una mappa contenente i valori di default per i metadati
	 * @param params
	 * @return
	 */
    private Map<String, Object> createMetadata(Map<String, Object> params) {
        Map<String, Object> defaultParams = new HashMap<String, Object>();
        defaultParams.put("versione", "nd");
        defaultParams.put("puntatoreJackRabbit", "nd");
        defaultParams.put("titolo", "default_titolo");
        defaultParams.put("sunto", "default_sunto");
        defaultParams.put("dettaglio", "default_dettaglio");
        defaultParams.put("tags", "tag1,tag2");
        if (null != params && params.size() > 0) {
            Iterator it = params.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pairs = (Map.Entry) it.next();
                String key = (String) pairs.getKey();
                if (defaultParams.containsKey(key)) {
                    defaultParams.put(key, pairs.getValue());
                }
            }
        }
        return defaultParams;
    }

    private void init() throws ApsSystemException {
        _repositoryDocPersonaleManager = (IRepositoryDocumentoPersonaleManager) this.getService(AdocSystemConstants.REPOSITORY_DOCUMENTO_PERSONALE_MANAGER);
        _keyManager = (IKeyGeneratorManager) this.getService(SystemConstants.KEY_GENERATOR_MANAGER);
    }

    private IKeyGeneratorManager _keyManager;

    private IRepositoryDocumentoPersonaleManager _repositoryDocPersonaleManager;

    private static final String FILE_DOCUMENT_TXT_PATHNAME = "./admin/test/documentsFile/test.txt";

    private static final String FILE_DOCUMENT_JPG_PATHNAME = "./admin/test/documentsFile/test.jpg";

    private static final String FILE_DOCUMENT_JPG_TEMP_PATHNAME = "./admin/test/documentsFile/temp.jpg";
}
