package com.frijole.htmlParser;

import javax.swing.text.html.*;
import java.io.*;
import java.net.*;

public class htmlDriver {

    public static void bMain(URL u) {
        ParserGetter kit = new ParserGetter();
        HTMLEditorKit.Parser parser = kit.getParser();
        HTMLEditorKit.ParserCallback callback = new TagParser(new OutputStreamWriter(System.out));
        try {
            InputStream in = u.openStream();
            InputStreamReader r = new InputStreamReader(in);
            parser.parse(r, callback, false);
        } catch (IOException e) {
            System.err.println(e);
        }
    }
}
