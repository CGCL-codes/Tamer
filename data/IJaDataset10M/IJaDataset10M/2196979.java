package common.persistence.io.xmlio;

import org.apache.xml.serialize.XMLSerializer;
import org.apache.xml.serialize.OutputFormat;
import java.io.*;
import org.w3c.dom.*;

public class TreeWriterXML implements ITreeWriterConstants {

    private String szFilename;

    public TreeWriterXML(String _filename) {
        this.szFilename = _filename;
    }

    public void writeTree(Document _doc) throws IOException, FileNotFoundException {
        try {
            OutputFormat objFormat = new OutputFormat(_doc);
            objFormat.setLineWidth(70);
            objFormat.setIndenting(true);
            objFormat.setEncoding(XML_ENCODING);
            objFormat.setVersion(XML_VERSION);
            File fileObject = new File(szFilename);
            fileObject.createNewFile();
            if (!fileObject.isFile() || !fileObject.canWrite()) {
                System.out.println(LOG_FILE_ERROR + fileObject.getPath());
                throw (new IOException());
            }
            FileWriter fileWriter = new FileWriter(fileObject);
            BufferedWriter bWriter = new BufferedWriter(fileWriter);
            XMLSerializer serializer = new XMLSerializer(bWriter, objFormat);
            serializer.serialize(_doc);
            bWriter.flush();
            bWriter.close();
            System.out.println(LOG_FILE_SUCCESS);
        } catch (FileNotFoundException e) {
            System.out.println(LOG_FILE_ERROR);
            throw e;
        } catch (IOException e) {
            System.out.println(LOG_FILE_ERROR);
            throw e;
        }
    }
}
