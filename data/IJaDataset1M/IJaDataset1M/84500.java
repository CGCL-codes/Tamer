package net.sourceforge.bulkmailer;

import java.io.*;
import java.util.*;
import java.text.*;

public class ProcessedLogWriter {

    private CsvWriter csvWriter;

    DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS z");

    public ProcessedLogWriter(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            csvWriter = initializeFile(file);
        } else {
            csvWriter = new CsvWriter(file);
        }
    }

    public void write(Date now, Card card) throws IOException {
        String emailAddress = "";
        String displayName = "";
        if (card != null) {
            emailAddress = card.getEmailAddress();
            displayName = card.getDisplayName();
        }
        csvWriter.writeLine(dateFormatter.format(now), emailAddress, displayName);
        csvWriter.flush();
    }

    private static CsvWriter initializeFile(File file) throws IOException {
        file.createNewFile();
        CsvWriter csvWriter = new CsvWriter(file);
        csvWriter.writeLine("Date", "Email Address", "Display Name");
        return csvWriter;
    }

    public void close() throws IOException {
        this.csvWriter.close();
    }
}
