package net.sf.orexio.jdcp.storage;

import java.net.UnknownHostException;
import net.sf.orexio.jdcp.common.Item;
import net.sf.orexio.jdcp.common.ItemIdentifier;

/**
 * @author alois.cochard@gmail.com
 */
public class ContentSeal extends Item {

    /**
	 * Unique class identifier for serialization.
	 */
    private static final long serialVersionUID = 1L;

    byte[] SignatureKeyPairIdentifierChecksum = null;

    byte[] EncryptionKeyPairIdentifierChecksum = null;

    Checksum[] TaxaChecksums = null;

    Checksum[] MetadatasChecksums = null;

    Checksum[] DataChunksChecksums = null;

    ContentSeal() throws UnknownHostException {
        super();
    }

    public class Checksum {

        ItemIdentifier identifier = null;

        byte[] checksum = null;
    }
}
