package com.esl.service.io;

import java.io.BufferedReader;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.esl.dao.practice.IIrregularVerbDAO;
import com.esl.entity.practice.qa.IrregularVerb;

public class IrregularVerbCSVImporter implements ICSVImporter {

    private static Logger logger = LoggerFactory.getLogger("ESL");

    private String filePath = "f:/upload/irregularverb.csv";

    private IIrregularVerbDAO irregularVerbDAO;

    @Override
    public int startImport(BufferedReader reader) {
        String line;
        int totalProceed = 0;
        try {
            while ((line = reader.readLine()) != null) {
                String[] strArray = line.split(SEPARATOR);
                if (strArray.length != 4) {
                    logger.warn("Line [{}] cannot be process", line);
                    continue;
                }
                IrregularVerb verb = new IrregularVerb(strArray[0].toLowerCase(), strArray[1].toLowerCase(), strArray[2].toLowerCase(), strArray[3].toLowerCase());
                irregularVerbDAO.persist(verb);
                logger.debug("Irregular Verb [{}] persisted", verb);
                totalProceed++;
            }
        } catch (IOException e) {
            logger.warn("IOException during read line", e);
        }
        return totalProceed;
    }

    @Override
    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public IIrregularVerbDAO getIrregularVerbDAO() {
        return irregularVerbDAO;
    }

    public void setIrregularVerbDAO(IIrregularVerbDAO irregularVerbDAO) {
        this.irregularVerbDAO = irregularVerbDAO;
    }
}
