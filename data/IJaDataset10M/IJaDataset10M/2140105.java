package org.signserver.groupkeyservice.common;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import org.signserver.common.ProcessResponse;
import org.signserver.common.RequestAndResponseManager;

/**
 * Response sent after processing the PregenerateKeysRequest
 * 
 * @author Philip Vendil
 * @version $Id: PregenerateKeysResponse.java 1829 2011-08-10 11:50:45Z netmackan $
 */
public class PregenerateKeysResponse extends ProcessResponse {

    private static final long serialVersionUID = 1L;

    int numberOfKeysGenerated;

    /**
     * Default constructor used during serialization
     */
    public PregenerateKeysResponse() {
    }

    /**
     * Main constructor
     * 
     * @param numberOfKeysGenerated number of keys that were generated.
     */
    public PregenerateKeysResponse(int numberOfKeysGenerated) {
        super();
        this.numberOfKeysGenerated = numberOfKeysGenerated;
    }

    /**
     * @return number of keys that were generated.
     */
    public int getNumberOfKeysGenerated() {
        return numberOfKeysGenerated;
    }

    public void parse(DataInput in) throws IOException {
        in.readInt();
        numberOfKeysGenerated = in.readInt();
    }

    public void serialize(DataOutput out) throws IOException {
        out.writeInt(RequestAndResponseManager.RESPONSETYPE_GKS_PREGENKEYS);
        out.writeInt(numberOfKeysGenerated);
    }
}
