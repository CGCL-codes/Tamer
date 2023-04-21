package com.pbonhomme.xf.xml.writer.pdf;

import java.util.Map;
import java.util.Properties;
import com.pbonhomme.xf.xml.writer.AbstractWriter;
import com.pbonhomme.xf.xml.writer.WriterException;

public class XepPDFWriter extends AbstractWriter {

    @Override
    public void init(Properties properties) throws WriterException {
    }

    @Override
    public int write(String inputFile, String outputFile) throws WriterException {
        return 0;
    }

    @Override
    public int write(String inputFile, String outputFile, Map<String, Object> documentProperties) throws WriterException {
        return 0;
    }
}
