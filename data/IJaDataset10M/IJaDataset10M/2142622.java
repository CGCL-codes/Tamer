package name.huzhenbo.java.io;

import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.After;
import java.io.*;

/**
 * An OutputStreamWriter is a bridge from character streams to byte streams: Characters written to it are encoded into
 * bytes using a specified charset. The charset that it uses may be specified by name or may be given explicitly, or the
 * platform's default charset may be accepted.
 * <p/>
 * Each invocation of a write() method causes the encoding converter to be invoked on the given character(s). The resulting
 * bytes are accumulated in a buffer before being written to the underlying output stream. The size of this buffer may be
 * specified, but by default it is large enough for most purposes. Note that the characters passed to the write() methods
 * are not buffered.
 * <p/>
 * For top efficiency, consider wrapping an OutputStreamWriter within a BufferedWriter so as to avoid frequent converter
 * invocations. For example:
 * <p/>
 * Writer out = new BufferedWriter(new OutputStreamWriter(System.out));
 * <p/>
 * A surrogate pair is a character represented by a sequence of two char values: A high surrogate in the range '?' 
 * to '?' followed by a low surrogate in the range '?' to '?'. If the character represented by a 
 * surrogate pair cannot be encoded by a given charset then a charset-dependent substitution sequence is written to the 
 * output stream.
 * <p/>
 * A malformed surrogate element is a high surrogate that is not followed by a low surrogate or a low surrogate that is 
 * not preceded by a high surrogate. It is illegal to attempt to write a character stream containing malformed surrogate 
 * elements. The behavior of an instance of this class when a malformed surrogate element is written is not specified.
 */
public class OutputStreamWriterTest {

    @Test
    public void should_write() throws IOException {
        BufferedReader streamReader = new BufferedReader(new InputStreamReader(new FileInputStream("res/input.data"), "utf-8"));
        String s = streamReader.readLine();
        assertTrue(s.startsWith("《藏地密码》"));
        streamReader.close();
        OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream("res/notexist.file"), "utf-8");
        writer.write(s);
        writer.close();
        streamReader = new BufferedReader(new InputStreamReader(new FileInputStream("res/notexist.file"), "utf-8"));
        assertTrue(streamReader.readLine().startsWith("《藏地密码》"));
        streamReader.close();
    }

    @After
    public void teardown() {
        assertTrue(new File("res/notexist.file").delete());
    }
}
