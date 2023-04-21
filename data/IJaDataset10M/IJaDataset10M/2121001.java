package net.ddaniels.json;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import org.codehaus.jettison.json.JSONException;
import com.thoughtworks.xstream.io.HierarchicalStreamDriver;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.StreamException;

/**
 * A driver for JSON that writes optimized JSON format, but is not able to
 * deserialize the result.
 * 
 * @author Paul Hammant
 * @since 1.2
 */
public class JsonHierarchicalStreamReadWriteDriver implements HierarchicalStreamDriver {

    public HierarchicalStreamReader createReader(Reader in) throws RuntimeException {
        JsonReader reader = null;
        try {
            reader = new JsonReader(in);
        } catch (IOException e) {
            new RuntimeException(e);
        } catch (JSONException e) {
            new RuntimeException(e);
        }
        return reader;
    }

    public HierarchicalStreamReader createReader(InputStream in) throws RuntimeException {
        try {
            return createReader(new InputStreamReader(in));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
	 * Create a HierarchicalStreamWriter that writes JSON.
	 */
    public HierarchicalStreamWriter createWriter(Writer out) {
        return new JsonTypeInformationWriter(out);
    }

    public HierarchicalStreamWriter createWriter(OutputStream out) {
        try {
            return createWriter(new OutputStreamWriter(out, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new StreamException(e);
        }
    }
}
