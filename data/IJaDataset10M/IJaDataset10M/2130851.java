package tests;

import java.io.File;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.SortedMap;
import java.util.Map.Entry;
import net.hncu.jzhcoder.translate.FileCharsetTranslator;
import org.junit.Test;

public class TestFileTranslator {

    @Test
    public void getAllCharsets() {
        SortedMap<String, Charset> charsets = Charset.availableCharsets();
        Iterator<Entry<String, Charset>> it = charsets.entrySet().iterator();
        while (it.hasNext()) {
            Entry<?, ?> entry = it.next();
            System.out.println("key = " + entry.getKey() + " , value = " + entry.getValue());
            ;
        }
    }
}
