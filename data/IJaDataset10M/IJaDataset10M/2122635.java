package pt.utl.ist.lucene.treceval.handlers.collections;

import org.apache.log4j.Logger;
import pt.utl.ist.lucene.treceval.IndexFilesCallBack;
import pt.utl.ist.lucene.treceval.handlers.ResourceHandler;
import java.io.IOException;
import java.io.InputStream;
import java.io.File;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Open a Zip file and create handler for internal files
 * 
 * @author Jorge Machado
 * @date 21/Ago/2008
 * @see pt.utl.ist.lucene.treceval.handlers
 */
public class CZipHandler implements CDocumentHandler {

    private static Logger logger = Logger.getLogger(CDirectory.class);

    public void handle(InputStream stream, String filePath, ResourceHandler handler, IndexFilesCallBack callBack, Properties filehandlers) throws IOException {
        ZipInputStream zipInputStream = new ZipInputStream(stream);
        ZipEntry ze;
        while ((ze = zipInputStream.getNextEntry()) != null) {
            logger.info("zip handling:" + ze.getName());
            CDocumentHandler documentHandler = CFileTypeFactory.createHandler(ze.getName(), null);
            if (documentHandler == null) {
                logger.error("Cant handle document:" + ze.getName());
            } else {
                documentHandler.handle(zipInputStream, filePath + File.separatorChar + ze.getName(), handler, callBack, filehandlers);
            }
        }
    }
}
