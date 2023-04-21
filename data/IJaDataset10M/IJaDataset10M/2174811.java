package org.authorsite.bib.loader.ris;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Parser {

    private static final String tagStartPattern = "^[A-Z0-9]{2}  -";

    private ArrayList<RISEntry> entries = new ArrayList<RISEntry>();

    private RISEntry currentEntry;

    public boolean lineIsTagged(String line) {
        if (line == null) {
            return false;
        }
        if (line.length() >= 5) {
            String tagPart = line.substring(0, 5);
            if (tagPart.matches(tagStartPattern)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public void readFile(String fileName) throws IOException, RISException {
        File f = new File(fileName);
        if (f.exists()) {
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF8"));
            String str;
            int lineNumber = 0;
            while ((str = in.readLine()) != null) {
                lineNumber++;
                processLine(str, lineNumber);
            }
            in.close();
        } else {
            System.err.println("No such file as " + fileName);
        }
        for (RISEntry entry : this.entries) {
            RISEntryHandler handler = RISEntryHandlerFactory.getInstance().getHandler(entry);
            handler.handleEntry(entry);
        }
    }

    private void processLine(String str, int lineNumber) {
        try {
            String trimmed = str.trim();
            if (trimmed.length() == 0) {
                return;
            }
            if (this.lineIsTagged(str)) {
                RISEntryLine line = null;
                try {
                    line = new RISEntryLine(str);
                } catch (RISException e) {
                    throw new RISException("Exception at line " + lineNumber, e);
                }
                if (line.getKey().equals("ER")) {
                    if (this.currentEntry != null) {
                        this.entries.add(currentEntry);
                        this.currentEntry = null;
                    } else {
                        throw new RISException("encountered end of record marker at line " + lineNumber + ", without one having been started");
                    }
                    return;
                }
                if (line.getKey().equals("ID")) {
                    System.out.println("Reading ID  " + line.getValue());
                    return;
                }
                if (line.getKey().equals("TY")) {
                    if (this.currentEntry == null) {
                        this.currentEntry = new RISEntry();
                        this.currentEntry.addEntryLine(line);
                    } else {
                        throw new RISException("encountered start of record marker at line " + lineNumber + ", without the previous one having been finished");
                    }
                    return;
                }
                if (this.currentEntry == null) {
                    throw new RISException("at line " + lineNumber + " encountered RIS before a TY record start marker tag");
                }
                this.currentEntry.addEntryLine(line);
            } else {
                this.currentEntry.getMostRecentEntry().appendToValue(str);
            }
        } catch (RISException e) {
            System.err.println("RIS Exception on line " + lineNumber);
            e.printStackTrace();
        }
    }

    public void exportSqlToFile(String fileName) throws IOException {
        throw new UnsupportedOperationException("not built yet");
    }
}
