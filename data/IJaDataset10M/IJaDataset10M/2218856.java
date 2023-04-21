package devbureau.fstore.common.load;

import devbureau.fstore.common.load.impl.ExcelFileWriter;
import devbureau.fstore.common.load.impl.WordFileWriter;

/**
 *
 * @author Vladimir
 */
public class FileWriterFactory {

    public static final String XLS = ".xls";

    public static final String DOC = ".doc";

    public static FileWriter getFileWriter(String fileFormat) throws Exception {
        FileWriter writer = null;
        if (fileFormat.equals(XLS)) {
            writer = new ExcelFileWriter(fileFormat);
        } else if (fileFormat.equals(DOC)) {
            writer = new WordFileWriter(fileFormat);
        }
        return writer;
    }
}
