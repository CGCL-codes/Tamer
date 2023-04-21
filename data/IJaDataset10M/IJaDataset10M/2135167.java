package ogdl;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.io.File;
import java.io.FileInputStream;
import java.io.StringReader;
import ogdl.support.*;

/** This class contains some easy functions to parse OGDL (convert text or binary OGDL to
 * a Graph object). 
 */
public class Ogdl {

    /** Read an OGDL file and return it as Graph */
    public static IGraph parse(String file) throws Exception {
        File f = new File(file);
        if (!f.canRead()) return null;
        Reader reader = new InputStreamReader(new FileInputStream(f), "UTF-8");
        EventHandlerGraph handler = new EventHandlerGraph();
        OgdlParser p = new OgdlParser(reader, handler);
        p.parse();
        reader.close();
        return handler.get();
    }

    /** Read an OGDL string and return it as Graph */
    public static IGraph parseString(String s) throws Exception {
        if (s == null) return null;
        Reader reader = new StringReader(s);
        EventHandlerGraph handler = new EventHandlerGraph();
        OgdlParser p = new OgdlParser(reader, handler);
        p.parse();
        reader.close();
        return handler.get();
    }

    /** Read OGDL text from an InputStream and return it as Graph */
    public static IGraph parse(InputStream in) throws Exception {
        Reader reader = new java.io.InputStreamReader(in);
        EventHandlerGraph handler = new EventHandlerGraph();
        OgdlParser p = new OgdlParser(reader, handler);
        p.parse();
        reader.close();
        return handler.get();
    }

    /** Parse a binary OGDL stream. 
     * 
     * This function does not close the input stream
     */
    public static IGraph parseBinary(InputStream in) throws Exception {
        EventHandlerGraph handler = new EventHandlerGraph();
        OgdlBinaryParser p = new OgdlBinaryParser(in, handler);
        p.parse();
        return handler.get();
    }

    /** Parse a binary OGDL stream from a RandomAccessFile. 
     */
    public static IGraph parseBinary(RandomAccessFile in) throws Exception {
        EventHandlerGraph handler = new EventHandlerGraph();
        OgdlBinaryParser p = new OgdlBinaryParser(in, handler);
        p.parse();
        return handler.get();
    }
}
