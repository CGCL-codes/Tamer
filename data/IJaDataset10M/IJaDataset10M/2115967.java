package org.tritonus.test;

import junit.framework.TestCase;
import java.util.HashMap;
import java.util.Map;
import org.tritonus.share.sampled.file.TAudioFileFormat;
import javax.sound.sampled.AudioSystem;

public class TAudioFileFormatTestCase extends TestCase {

    public TAudioFileFormatTestCase(String strName) {
        super(strName);
    }

    public void testEmptyMap() {
        Map<String, Object> prop = new HashMap<String, Object>();
        TAudioFileFormat fileFormat = new TAudioFileFormat(null, null, AudioSystem.NOT_SPECIFIED, AudioSystem.NOT_SPECIFIED, prop);
        Map<String, Object> propReturn = fileFormat.properties();
        assertTrue(propReturn.isEmpty());
        Object result = propReturn.get("bitrate");
        assertNull(result);
    }

    public void testCopying() {
        Map<String, Object> prop = new HashMap<String, Object>();
        prop.put("bitrate", new Float(22.5F));
        TAudioFileFormat fileFormat = new TAudioFileFormat(null, null, AudioSystem.NOT_SPECIFIED, AudioSystem.NOT_SPECIFIED, prop);
        Map<String, Object> propReturn = fileFormat.properties();
        assertTrue(prop != propReturn);
        prop.put("bitrate", new Float(42.5F));
        Object result = propReturn.get("bitrate");
        assertEquals(new Float(22.5F), result);
    }

    public void testUnmodifiable() {
        Map<String, Object> prop = new HashMap<String, Object>();
        TAudioFileFormat fileFormat = new TAudioFileFormat(null, null, AudioSystem.NOT_SPECIFIED, AudioSystem.NOT_SPECIFIED, prop);
        Map<String, Object> propReturn = fileFormat.properties();
        try {
            propReturn.put("author", "Matthias Pfisterer");
            fail("returned Map allows modifications");
        } catch (UnsupportedOperationException e) {
        }
    }

    public void testGet() {
        Map<String, Object> prop = new HashMap<String, Object>();
        prop.put("bitrate", new Float(22.5F));
        prop.put("author", "Matthias Pfisterer");
        TAudioFileFormat fileFormat = new TAudioFileFormat(null, null, AudioSystem.NOT_SPECIFIED, AudioSystem.NOT_SPECIFIED, prop);
        Map<String, Object> propReturn = fileFormat.properties();
        assertEquals(new Float(22.5F), propReturn.get("bitrate"));
        assertEquals("Matthias Pfisterer", propReturn.get("author"));
    }
}
