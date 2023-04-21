package com.luzan.parser.map;

import org.apache.log4j.Logger;
import java.io.*;

public abstract class Parser<T> {

    static class ParserException extends Exception {

        public ParserException() {
            super();
        }

        public ParserException(String message) {
            super(message);
        }

        public ParserException(String message, Throwable cause) {
            super(message, cause);
        }

        public ParserException(Throwable cause) {
            super(cause);
        }
    }

    protected static Logger logger = Logger.getLogger(Parser.class);

    protected BufferedReader reader;

    public Parser(Reader reader) {
        this.reader = new BufferedReader(reader);
    }

    public Parser(final InputStream is, final String cp) throws IOException {
        reader = new BufferedReader(new InputStreamReader(is, cp));
    }

    public Parser(final String data) {
        this(new BufferedReader(new StringReader(data)));
    }

    public Parser(final File file, final String cp) throws IOException {
        this(new FileInputStream(file), cp);
    }

    public abstract T parse() throws ParserException;

    public static void main(String[] args) throws ParserException, IOException {
        Parser parser = null;
        if (args[0].endsWith(".wpt")) parser = new WPTPointParser(new File(args[0]), "Cp1251"); else if (args[0].endsWith(".plt")) parser = new PLTTrackParser(new File(args[0]), "Cp1251"); else if (args[0].endsWith(".kml")) parser = new KMLParser(new File(args[0]), "UTF-8");
        if (parser != null) parser.parse();
    }
}
