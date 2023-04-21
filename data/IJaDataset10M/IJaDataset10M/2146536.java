package pspdash.data;

import java.io.*;

public class CppFilterReader extends StringReader {

    public CppFilterReader(BufferedReader in) throws IOException {
        super(filter(in, null));
    }

    public CppFilterReader(BufferedReader in, String preDefinitions) throws IOException {
        super(filter(in, preDefinitions));
    }

    private static String filter(BufferedReader in, String preDefinitions) throws IOException {
        StringBuffer result = new StringBuffer();
        String line;
        CppFilter filt = null;
        try {
            filt = new CppFilter(in, preDefinitions);
            while ((line = filt.readLine()) != null) result.append(line).append("\n");
        } finally {
            if (filt != null) filt.dispose();
        }
        return result.toString();
    }
}
