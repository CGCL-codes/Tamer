package org.kabeja.tools;

import java.io.BufferedReader;
import java.io.IOException;
import org.kabeja.dxf.DXFConstants;

/**
 * @author <a href="mailto:simon.mieth@gmx.de">Simon Mieth</a>
 *
 */
public class CodePageParser {

    public static final String CODEPAGE_CODE = "$DWGCODEPAGE";

    public static final String GROUPCODE = "3";

    private static final String[] prefix = { "ansi_", "dos" };

    private static final String javaPrefix = "Cp";

    public String parseEncoding(BufferedReader reader) {
        String encoding = "";
        try {
            String line = null;
            String code;
            String value;
            boolean next = true;
            boolean codepage = false;
            boolean key = true;
            String currentKey = null;
            while (((line = reader.readLine()) != null) && next) {
                line = line.trim();
                if (key) {
                    currentKey = line;
                    key = false;
                } else {
                    key = true;
                    if (DXFConstants.SECTION_END.equals(line)) {
                        return encoding;
                    } else if (CODEPAGE_CODE.equals(line)) {
                        codepage = true;
                    } else if (codepage && currentKey.equals("3")) {
                        return translateCodePage(line);
                    } else if (DXFConstants.SECTION_CLASSES.equals(line) || DXFConstants.SECTION_BLOCKS.equals(line) || DXFConstants.SECTION_ENTITIES.equals(line)) {
                        return encoding;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return encoding;
    }

    public String translateCodePage(String cp) {
        String c = cp.toLowerCase();
        for (int i = 0; i < prefix.length; i++) {
            if (c.startsWith(prefix[i])) {
                return javaPrefix + cp.substring(prefix[i].length());
            }
        }
        return cp;
    }
}
