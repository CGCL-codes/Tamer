package vobs.store;

import java.io.*;
import vobs.dbaccess.DataStoreException;
import wdc.settings.*;
import org.apache.log4j.*;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.jdom.Element;

public class FileStoreSave {

    private static Logger logger = Logger.getLogger(FileStoreSave.class);

    public static void storeFile(InputStream fileData, String fileName, String fileType, String fileSize, String ID, String actionId) throws DataStoreException {
        String dataStoreDir = Settings.get("vo_store." + actionId + ".exportDir");
        logger.debug("Method FileStoreSave.storeFile is called");
        int curFileSize = 0;
        try {
            FileOutputStream fs = new FileOutputStream(dataStoreDir + "/" + ID, false);
            int bufLen = 1024;
            byte[] buffer = new byte[bufLen];
            int bytesRead = fileData.read(buffer, 0, bufLen);
            while (bytesRead > 0) {
                fs.write(buffer, 0, bytesRead);
                bytesRead = fileData.read(buffer, 0, bufLen);
                curFileSize += bytesRead;
            }
            fs.close();
            fileData.close();
        } catch (FileNotFoundException ex) {
            throw new DataStoreException(ex);
        } catch (IOException ex) {
            throw new DataStoreException(ex);
        }
        if (null == fileSize || fileSize.length() == 0 || fileSize.equals("0")) {
            fileSize = curFileSize + "";
        }
        Element metaElm = new Element("metadata");
        metaElm.addContent((new Element("OBJECT_ID").setText(ID)));
        metaElm.addContent((new Element("DESCRIPTION").setText("uploaded/form")));
        metaElm.addContent((new Element("ACTION_ID").setText(actionId)));
        metaElm.addContent((new Element("fileName")).setText(fileName));
        metaElm.addContent((new Element("contentType")).setText(fileType));
        metaElm.addContent((new Element("fileSize")).setText(fileSize));
        try {
            FileOutputStream fs = new FileOutputStream(dataStoreDir + "/" + ID + ".xml", false);
            logger.debug("File " + dataStoreDir + "/" + ID + ".xml is saved");
            XMLOutputter out = new XMLOutputter(Format.getPrettyFormat());
            out.output(metaElm, fs);
            fs.close();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            throw new DataStoreException(ex);
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new DataStoreException(ex);
        }
    }
}
