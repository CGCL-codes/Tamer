package net.sf.jdiskcatalog.analysis;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import net.sf.jdiskcatalog.api.FileAnalyser;
import net.sf.jdiskcatalog.api.Node;
import net.sf.jdiskcatalog.api.Provides;
import net.sf.jdiskcatalog.api.Requires;
import net.sf.jdiskcatalog.api.StreamAnalyser;

/**
 * Runs a StreamAnalyser on a given File.
 *
 * @author Przemek Więch <pwiech@losthive.org>
 * @version $Id$
 */
public class FileStreamAnalyser implements FileAnalyser, Provides, Requires {

    private StreamAnalyser analyser;

    public FileStreamAnalyser(StreamAnalyser analyser) {
        this.analyser = analyser;
    }

    public Map<String, Object> analyse(File file, Node node) throws IOException {
        if (!file.isFile() || !file.canRead()) return null;
        InputStream stream = new FileInputStream(file);
        try {
            return analyser.analyse(stream, node);
        } finally {
            stream.close();
        }
    }

    public String[] getProvided() {
        if (analyser instanceof Provides) return ((Provides) analyser).getProvided();
        return new String[0];
    }

    public String[] getRequired() {
        if (analyser instanceof Requires) return ((Requires) analyser).getRequired();
        return new String[0];
    }
}
