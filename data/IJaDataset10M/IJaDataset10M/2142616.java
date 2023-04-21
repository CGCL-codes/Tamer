package com.familywebscape.ootpj.dataimport;

import java.io.*;
import com.familywebscape.ootpj.db.SimLeague;
import org.objectstyle.cayenne.access.*;
import org.apache.log4j.*;
import com.Ostermiller.*;
import com.Ostermiller.util.*;

public class PitchGameDataImport implements DataImport {

    public static Logger log = Logger.getLogger(PitchGameDataImport.class);

    public void processImport(String filename, SimLeague simLeague, DataContext dataContext) throws IOException {
        log.debug("in PitchGameDataImport.processImport");
        ClassLoader loader = ClassLoader.getSystemClassLoader();
        InputStream inputStream = loader.getResourceAsStream(filename);
        if (inputStream == null) {
            log.debug("inputStream is null.");
        }
        CSVParser parser = new CSVParser(inputStream);
        String[] data = null;
        int pitchGameRecordCount = 0;
        parser.getLine();
        while ((data = parser.getLine()) != null) {
            log.debug("********* PROCESSING PITCH GAME RECORD ***************");
            pitchGameRecordCount++;
        }
    }
}
