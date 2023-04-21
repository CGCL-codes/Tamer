package edu.usc.epigenome.uecgatk.YapingWriter;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

public class cpgReadsWriterImp extends FormatWriterBase {

    public cpgReadsWriterImp(File location) {
        super(location);
    }

    public cpgReadsWriterImp(OutputStream output) {
        super(output);
    }

    public cpgReadsWriterImp(File location, OutputStream output) {
        super(location, output);
    }

    @Override
    public void add(genomeObject obj) {
        String readsLine = String.format("%s\t%d\t%c\t%d\t%c\t%d\n", obj.getChr(), obj.getStart(), ((cpgReads) obj).getMethyStatus(), ((cpgReads) obj).getbaseQ(), ((cpgReads) obj).getstrand(), ((cpgReads) obj).getEncryptID());
        try {
            mWriter.write(readsLine);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addHeader(Object o) {
        String header = "#chr\tpos\tmethyStatus\tbaseQ\tstrand\treadID\n";
        try {
            mWriter.write(header);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
