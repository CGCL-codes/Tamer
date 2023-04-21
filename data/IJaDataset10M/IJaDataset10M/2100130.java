package com.itstherules.mediacentre.model;

import java.io.File;
import java.io.StringWriter;
import com.itstherules.io.IOHelper;
import com.itstherules.metadata.FlvHeader;
import com.itstherules.parser.MetaDataParser;

public class MetaDataScalar {

    public String value(File file) {
        IOHelper ioHelper = new IOHelper(file);
        new FlvHeader(ioHelper);
        MetaDataParser parseMeta = new MetaDataParser(ioHelper);
        parseMeta.findMetaTag();
        StringWriter writer = new StringWriter();
        try {
            parseMeta.writeMetaData(writer);
        } catch (Exception e1) {
        }
        return writer.toString();
    }
}
