package net.sf.joafip.store.service.objectio.serialize.output;

import net.sf.joafip.NotStorableClass;
import net.sf.joafip.kvstore.record.entity.DataRecordIdentifier;
import net.sf.joafip.store.entity.objectio.ObjectAndPersistInfo;
import net.sf.joafip.store.service.binary.HelperBinaryConversion;
import net.sf.joafip.store.service.objectio.ObjectIODataCorruptedException;
import net.sf.joafip.store.service.objectio.ObjectIOException;
import net.sf.joafip.store.service.objectio.manager.IObjectIOManagerForObjectIO;

/**
 * 
 * @author luc peuvrier
 * 
 */
@NotStorableClass
public class ReferenceOutput {

    private final IObjectIOManagerForObjectIO objectIOManager;

    private final HelperBinaryConversion helperBinaryConversion;

    public ReferenceOutput(final IObjectIOManagerForObjectIO objectIOManager, final HelperBinaryConversion helperBinaryConversion) {
        super();
        this.objectIOManager = objectIOManager;
        this.helperBinaryConversion = helperBinaryConversion;
    }

    /**
	 * add a reference to binary data record
	 * 
	 * @param objectAndPersistInfo
	 * @param binary
	 *            where write the reference
	 * @param offset
	 *            where write reference in binary
	 * @return offset in binary after the wrote data
	 * @throws ObjectIOException
	 * @throws ObjectIODataCorruptedException
	 */
    public int toBinary(final ObjectAndPersistInfo objectAndPersistInfo, final byte[] binary, final int offset) throws ObjectIOException, ObjectIODataCorruptedException {
        final Object object = objectAndPersistInfo.getObject();
        final DataRecordIdentifier identifier;
        if (object == null) {
            identifier = null;
        } else {
            identifier = objectIOManager.getOrCreateDataRecordIdentifierAssociatedToObject(objectAndPersistInfo);
            assert identifier != null : "identifier must be defined";
        }
        return helperBinaryConversion.nullableAndTypedReferenceConverter.toBinary(binary, offset, true, identifier);
    }
}
