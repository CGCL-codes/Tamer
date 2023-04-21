package cc.w3d.jawos2.jinn2.uid.XUidGenerator.tests;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.junit.Test;
import cc.w3d.jawos2.jinn2.uid.XUidGenerator.XUidGeneratorContainer;
import cc.w3d.jawos2.jinn2.uid.XUidGenerator.core.XUidGenerator;
import cc.w3d.jawos2.jinn2.uid.uid.Uid;
import static org.junit.Assert.*;

public class XUidGeneratorTest {

    private static final int NUM_ITEMS = 10000;

    @Test
    public void xUidGeneratorTest() throws IOException, ClassNotFoundException {
        singleTest(XUidGeneratorContainer.xUidGenerator);
        singleTest(XUidGeneratorContainer.xUidGenerator.getClone());
    }

    public void singleTest(XUidGenerator xUidGenerator) throws IOException, ClassNotFoundException {
        List<Uid> l = new ArrayList<Uid>();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(baos);
        for (int i = 0; i < NUM_ITEMS; i++) {
            Uid id = xUidGenerator.generate();
            out.writeObject(id);
            checkMult(l, id);
            l.add(id);
        }
        out.close();
        checkSerializable(l, baos.toByteArray());
    }

    private void checkMult(List<Uid> l, Uid id) {
        for (Uid tmp : l) {
            assertTrue(!tmp.equals(id));
        }
    }

    private void checkSerializable(List<Uid> l, byte[] byteArray) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bais = new ByteArrayInputStream(byteArray);
        ObjectInputStream in = new ObjectInputStream(bais);
        for (Iterator<Uid> iterator = l.iterator(); iterator.hasNext(); ) {
            Uid uidList = (Uid) iterator.next();
            Uid uidSerial = (Uid) in.readObject();
            assertTrue(uidList.equals(uidSerial));
        }
    }
}
