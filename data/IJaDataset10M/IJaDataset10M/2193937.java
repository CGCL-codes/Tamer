package at.langegger.xlwrap.spreadsheet.csv;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import at.langegger.xlwrap.common.XLWrapException;
import at.langegger.xlwrap.spreadsheet.Sheet;
import at.langegger.xlwrap.spreadsheet.Workbook;

/**
 * @author dorgon
 *
 */
public class CSVWorkbook implements Workbook {

    private static final Logger log = LoggerFactory.getLogger(CSVWorkbook.class);

    private final BufferedReader in;

    private final String file;

    /**
	 * @param stream
	 * @param fileName
	 */
    public CSVWorkbook(InputStream stream, String fileName) {
        in = new BufferedReader(new InputStreamReader(stream));
        this.file = fileName;
    }

    @Override
    public boolean supportsMultipleSheets() {
        return false;
    }

    @Override
    public void close() {
        try {
            in.close();
        } catch (IOException e) {
            log.error("Failed to close open CSV input stream.", e);
        }
    }

    @Override
    public Sheet getSheet(int sheet) throws XLWrapException {
        return new CSVSheet(in, file, null, null);
    }

    @Override
    public Sheet getSheet(String sheetName) throws XLWrapException {
        return new CSVSheet(in, file, null, null);
    }

    @Override
    public String[] getSheetNames() {
        return new String[] { "Default" };
    }

    @Override
    public String getWorkbookInfo() {
        return file;
    }
}
