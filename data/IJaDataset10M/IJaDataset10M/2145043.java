package neissmodel.data_access;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.zip.DataFormatException;

/**
 *
 * @author Nick Malleson
 */
public class CSVFile extends FlatFile {

    public CSVFile(String fileName) throws FileNotFoundException, IOException, DataFormatException {
        super(fileName);
    }

    protected String getDelim() {
        return ",";
    }

    protected String getTextSep() {
        return "\"";
    }
}
