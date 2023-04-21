package net.sf.filePiper.processors;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import net.sf.sfac.gui.editor.ObjectEditor;
import net.sf.filePiper.gui.SizeAndUnitEditor;
import net.sf.filePiper.model.FileProcessorEnvironment;
import net.sf.filePiper.model.OneToOneByteFileProcessor;

/**
 * Processor copying just the x first lines (or bytes) of a file.
 * 
 * @author BEROL
 */
public class HeadProcessor extends OneToOneByteFileProcessor implements SizeAndUnit {

    private static final String HEAD_SIZE = "head.size";

    private static final String HEAD_UNITS = "head.units";

    public String getProcessorName() {
        return "Head";
    }

    public int getSize() {
        return getSettings().getIntProperty(HEAD_SIZE, 100);
    }

    public void setSize(int newSize) {
        getSettings().setIntProperty(HEAD_SIZE, newSize);
    }

    public int getUnits() {
        return getSettings().getIntProperty(HEAD_UNITS, UNIT_LINE);
    }

    public void setUnits(int newUnits) {
        getSettings().setIntProperty(HEAD_UNITS, newUnits);
    }

    public String getProposedNameSuffix() {
        return "head";
    }

    @Override
    public void process(InputStream is, OutputStream os, FileProcessorEnvironment env) throws IOException {
        if (getUnits() == UNIT_BYTE) {
            processBytes(is, os, env);
        } else {
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
            processLines(br, bw, env);
            bw.close();
        }
    }

    public void processBytes(InputStream is, OutputStream os, FileProcessorEnvironment env) throws IOException {
        int readByte;
        int count = 0;
        int size = getSize();
        while (((readByte = is.read()) >= 0) && env.shouldContinue()) {
            if (count < size) {
                os.write(readByte);
            }
            bytesProcessed(1);
            count++;
        }
    }

    public void processLines(BufferedReader in, BufferedWriter out, FileProcessorEnvironment env) throws IOException {
        String line;
        int count = 0;
        int size = getSize();
        while (((line = in.readLine()) != null) && env.shouldContinue()) {
            if (count < size) {
                out.write(line);
                out.newLine();
            }
            linesProcessed(1);
            count++;
        }
    }

    public ObjectEditor getEditor() {
        return new SizeAndUnitEditor("Output the X last first/bytes of the input file", "Head size");
    }
}
