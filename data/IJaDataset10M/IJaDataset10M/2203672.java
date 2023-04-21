package bgu.nlp.utils;

import java.io.BufferedWriter;
import java.io.IOException;

/**
 * @author Guz
 * 
 */
public class CSVFileWriter extends BufferedWriter {

    private boolean firstInLine;

    public CSVFileWriter(final String filePath) {
        super(FileUtils.buildWriter(filePath));
        firstInLine = true;
    }

    @Override
    public void newLine() throws IOException {
        super.newLine();
        firstInLine = true;
    }

    @Override
    public void write(String str) throws IOException {
        if (firstInLine) {
            firstInLine = false;
        } else {
            super.write(",");
        }
        super.write(str);
    }

    public void write(String[] array) throws IOException {
        for (String str : array) write(str);
    }
}
