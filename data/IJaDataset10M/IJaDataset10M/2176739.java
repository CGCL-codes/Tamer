package genomeMap.dao.file;

import genomeMap.dao.BarratModelDAO;
import genomicMap.data.BarratModelData;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javautil.io.IOUtil;
import org.apache.log4j.Logger;

/**
 * The file is expected to have only 2 columns. But having more than 2 columns does not interrupt the
 * process as long as the first data correspond to the physical distance and the second to the 
 * recombination distance.
 * @author Susanta Tewari <stewari@yahoo.com>
 */
public class BarratModelFileDAO implements BarratModelDAO {

    private String filePath = "resources/barrat_model_data.txt";

    /**
     * By default, default delimiter set, which is " \t\n\r\f": the space character, the tab character, 
     * the newline character, the carriage-return character, and the form-feed character.
     * However you can set a different one via the setter method.
     */
    private String fileDelim = null;

    static Logger logger = Logger.getLogger(BarratModelFileDAO.class);

    public BarratModelFileDAO() {
    }

    public BarratModelData getBarratModelData() {
        BarratModelData barratModelData = new BarratModelData();
        try {
            BufferedReader inFile = new BufferedReader(new FileReader(new File(filePath)));
            String line = "";
            boolean firstTime = true;
            int lineCount = 0;
            while ((line = inFile.readLine()) != null) {
                lineCount++;
                String[] vals = IOUtil.getTokens(line, fileDelim);
                if (vals.length != 2) {
                    if (firstTime) {
                        logger.warn("Barrat Model data has more than 2 columns ! line number: " + lineCount);
                        firstTime = false;
                    }
                }
                barratModelData.addData(vals[0], vals[1]);
            }
            inFile.close();
        } catch (IOException ex) {
            logger.error(ex);
        }
        return barratModelData;
    }

    public void setFileDelim(String delim) {
        this.fileDelim = delim;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
