package uk.org.sgj.OHCApparatus.ImportExport;

import uk.org.sgj.OHCApparatus.*;
import uk.org.sgj.OHCApparatus.Records.*;
import java.io.*;

abstract class OHCProjectImportVersionAbstract {

    protected OHCApparatusProject project;

    protected BufferedReader reader;

    OHCProjectImportVersionAbstract(BufferedReader r) {
        reader = r;
        project = new OHCApparatusProject();
    }

    protected final String readLine() throws EOFException, IOException {
        return (readLine(false));
    }

    protected final String readFieldLine() throws EOFException, IOException {
        return (readLine(true));
    }

    private String readLine(boolean canBeBlank) throws EOFException, IOException {
        String line;
        do {
            line = reader.readLine();
            if (canBeBlank) {
                break;
            }
        } while (line.isEmpty());
        line = OHCTextUtil.correctTrailingRTLString(line);
        return (line);
    }

    protected abstract OHCApparatusProject readProject();

    protected abstract OHCBasicRecord readSingleRecord();

    protected abstract OHCBasicRecord getRecordFromType(String typeLine);

    protected abstract void pipeRecordFromFileToRecord(OHCBasicRecord record);
}
