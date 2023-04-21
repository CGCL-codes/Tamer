package com.loribel.commons.business.ini;

import java.io.IOException;
import java.io.Writer;
import com.loribel.commons.business.GB_BOStringConvertors;
import com.loribel.commons.business.abstraction.GB_SimpleBusinessObject;
import com.loribel.commons.util.CTools;

/**
 * Write an BusinessObject to an properties file.
 * This class doesn't support all options of business object
 */
public class GB_IniBOWriter {

    private static GB_BOStringConvertors cvt = GB_BOStringConvertors.getInstance();

    public GB_IniBOWriter() {
    }

    /**
     * Build the two maps mapValue and mapMeteData according to the content of the var content.
     * @throws IOException
     */
    public void write(GB_SimpleBusinessObject a_bo, Writer a_writer) throws IOException {
        String[] l_names = a_bo.getPropertyNames();
        int len = CTools.getSize(l_names);
        for (int i = 0; i < len; i++) {
            write(a_bo, l_names[i], a_writer);
        }
    }

    /**
     * Build the two maps mapValue and mapMeteData according to the content of the var content.
     * @throws IOException
     */
    public void write(GB_SimpleBusinessObject a_bo, String a_propertyName, Writer a_writer) throws IOException {
        String l_valueStr = cvt.valueAsString(a_bo, a_propertyName);
        a_writer.write(a_propertyName + "=" + l_valueStr + AA.SL);
    }
}
