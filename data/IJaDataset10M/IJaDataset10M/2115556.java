package net.sf.joafip.store.service.objectio.manager;

import net.sf.joafip.heapfile.service.HeapException;
import net.sf.joafip.store.entity.objectio.ObjectAndPersistInfo;
import net.sf.joafip.store.entity.objectio.manager.WriteResult;
import net.sf.joafip.store.service.binary.HelperBinaryConversion;
import net.sf.joafip.store.service.classinfo.ClassInfoException;
import net.sf.joafip.store.service.objectfortest.BobSerializable;
import net.sf.joafip.store.service.objectio.ObjectIOClassNotFoundException;
import net.sf.joafip.store.service.objectio.ObjectIODataCorruptedException;
import net.sf.joafip.store.service.objectio.ObjectIODataRecordNotFoundException;
import net.sf.joafip.store.service.objectio.ObjectIOException;
import net.sf.joafip.store.service.objectio.ObjectIOInvalidClassException;
import net.sf.joafip.store.service.objectio.ObjectIONotSerializableException;

/**
 * 
 * @author luc peuvrier
 * 
 */
public class TestObjectIoSerializeCase extends AbstractBinaryDump {

    public void testBobSerializeDefaultWrite() throws HeapException, ClassInfoException, ObjectIOException, ObjectIODataCorruptedException, ObjectIOInvalidClassException, ObjectIONotSerializableException, ObjectIODataRecordNotFoundException, ObjectIOClassNotFoundException {
        BobSerializable.defaultWrite = true;
        final BobSerializable bob = new BobSerializable();
        bob.setObject(Integer.valueOf(1));
        bob.setValue(1);
        bob.setBoolean1(Boolean.TRUE);
        final ObjectAndPersistInfo objectAndItsClassInfo = createObjectAndItsClassInfo(bob);
        WriteResult writeResult = write(objectAndItsClassInfo);
        assertValueChangeForFirstWrite(writeResult);
        assertType(new int[] { HelperBinaryConversion.REFERENCE_TYPE, HelperBinaryConversion.INTEGER_TYPE, HelperBinaryConversion.BOOLEAN_TYPE }, writeResult);
        assertReferencedSons(new Class[] { Object.class }, new Class[] { Integer.class }, writeResult);
        assertReferenceChange(objectAndItsClassInfo, new Class[] { null }, new Class[] { Integer.class }, writeResult);
        writeResult = write(objectAndItsClassInfo);
        assertValueChangeForSameStateWrite(writeResult);
        bob.setObject(Integer.valueOf(2));
        bob.setBoolean1(Boolean.FALSE);
        writeResult = write(objectAndItsClassInfo);
        assertValueChangeSinceStateChange(writeResult);
        assertReferencedSons(new Class[] { Object.class }, new Class[] { Integer.class }, writeResult);
        assertReferenceChange(objectAndItsClassInfo, new Class[] { null }, new Class[] { Integer.class }, writeResult);
        bob.setValue(2);
        writeResult = write(objectAndItsClassInfo);
        assertValueChangeSinceStateChange(writeResult);
        assertReferencedSons(new Class[] { Object.class }, new Class[] { Integer.class }, writeResult);
        assertReferenceChange(objectAndItsClassInfo, new Class[] { null }, new Class[] { Integer.class }, writeResult);
    }
}
