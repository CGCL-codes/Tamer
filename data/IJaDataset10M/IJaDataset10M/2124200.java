package fr.inria.zvtm.cluster.tests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import fr.inria.zvtm.glyphs.Glyph;
import fr.inria.zvtm.glyphs.VCircle;
import fr.inria.zvtm.cluster.ObjId;

public class ObjIdTest extends TestCase {

    public ObjIdTest(String name) {
        super(name);
    }

    public void testObjIdValidity() {
        Glyph glyph = new VCircle(2, 5, 42, 314, Color.WHITE);
        ObjId id = glyph.getObjId();
        assertTrue(id.isValid());
    }

    public void testObjIdSerialization() {
        try {
            Glyph glyph = new VCircle(2, 5, 42, 314, Color.WHITE);
            ObjId id = glyph.getObjId();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(id);
            oos.flush();
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray()));
            ObjId deserializedId = (ObjId) ois.readObject();
            assertNotSame(id, deserializedId);
            assertEquals(id, deserializedId);
        } catch (Exception ex) {
            fail();
        }
    }

    public void testObjIdSequence() {
        Glyph glyph = new VCircle(2, 5, 42, 314, Color.WHITE);
        ObjId id = glyph.getObjId();
        Glyph anotherGlyph = new VCircle(2, 5, 42, 314, Color.WHITE);
        ObjId anotherId = anotherGlyph.getObjId();
        assertTrue(!id.equals(anotherId));
    }
}
