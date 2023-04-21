package org.xhtmlrenderer.css.parser;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MakeTokens {

    private static final String EOL = System.getProperty("line.separator");

    private static final String INPUT = "C:/eclipseWorkspaceQT/xhtmlrenderer/src/java/org/xhtmlrenderer/css/parser/tokens.txt";

    public static final void main(String[] args) throws IOException {
        List tokens = new ArrayList();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(INPUT)));
            String s;
            while ((s = reader.readLine()) != null) {
                tokens.add(s);
            }
            reader.close();
            reader = null;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                }
            }
        }
        StringBuffer buf = new StringBuffer();
        int offset = 1;
        for (Iterator i = tokens.iterator(); i.hasNext(); offset++) {
            String s = (String) i.next();
            String id = s.substring(0, s.indexOf(','));
            buf.append("\tpublic static final int ");
            buf.append(id);
            buf.append(" = ");
            buf.append(offset);
            buf.append(";");
            buf.append(EOL);
        }
        buf.append(EOL);
        for (Iterator i = tokens.iterator(); i.hasNext(); offset++) {
            String s = (String) i.next();
            String id = s.substring(0, s.indexOf(','));
            String descr = s.substring(s.indexOf(',') + 1);
            buf.append("\tpublic static final Token TK_");
            buf.append(id);
            buf.append(" = new Token(");
            buf.append(id);
            buf.append(", \"");
            buf.append(id);
            buf.append("\", \"");
            buf.append(descr);
            buf.append("\");");
            buf.append(EOL);
        }
        buf.append(EOL);
        System.out.println(buf.toString());
    }
}
