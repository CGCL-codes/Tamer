package net.sourceforge.transumanza.reader.file.csv;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import net.sourceforge.transumanza.reader.file.LineParser;

public class CsvLineParserImpl1 implements LineParser {

    private String delimiter = ";";

    public String[] parse(String str) throws Exception {
        StringTokenizer parser = new StringTokenizer(str, delimiter);
        List app = new ArrayList();
        while (parser.hasMoreTokens()) {
            String token = parser.nextToken();
            System.out.println("tok " + token);
            app.add(token);
        }
        return (String[]) app.toArray(new String[app.size()]);
    }

    public String getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }
}
