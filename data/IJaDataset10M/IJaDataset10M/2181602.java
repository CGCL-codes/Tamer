package net.sourceforge.transumanza.reader.file;

public class IdentityLineParser implements LineParser {

    public String[] parse(String str) throws Exception {
        String[] app = new String[1];
        app[0] = str;
        return app;
    }
}
