package org.anuta.xmltv.export;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.anuta.xmltv.exceptions.ExportException;
import org.apache.xmlbeans.XmlObject;

public class FileSystemExport implements Export {

    private String fileName;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void export(XmlObject xml) throws ExportException {
        try {
            File f = new File(getFileName());
            xml.save(f);
        } catch (FileNotFoundException e) {
            throw new ExportException("File not found exception " + e.getMessage());
        } catch (IOException e) {
            throw new ExportException("IO exception " + e.getMessage());
        }
    }
}
