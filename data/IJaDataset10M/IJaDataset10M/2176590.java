package com.tredart.dataimport.datasources;

import java.io.FileInputStream;
import java.io.IOException;
import org.apache.log4j.Logger;
import com.tredart.parser.IDataSource;

/**
 * Parses a file containing prices information in a specific tabular form.
 * <p>
 * This is meant to be used temporarily to import old archived prices.
 * 
 * @author cuprum
 * @author fdegrazia
 * @author csmith
 */
public final class FileDataSource implements IDataSource<String> {

    private static final Logger LOGGER = Logger.getLogger(FileDataSource.class);

    private String filePath;

    /**
     * {@inheritDoc}
     */
    public String getData() {
        String result = "";
        try {
            FileInputStream file = new FileInputStream(filePath);
            byte[] data = new byte[file.available()];
            file.read(data);
            file.close();
            result = new String(data);
        } catch (IOException e) {
            LOGGER.error(e);
        }
        return result;
    }

    /**
     * Returns the file path.
     * 
     * @return the file path
     */
    public String getFilePath() {
        return filePath;
    }

    /**
     * Sets the file path.
     * 
     * @param theFilePath
     *            the file path
     */
    public void setFilePath(final String theFilePath) {
        this.filePath = theFilePath;
    }
}
