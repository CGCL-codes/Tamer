package eg.nileu.cis.nilestore.redundancy.port;

import se.sics.kompics.PortType;

/**
 * The Class Redundancy.
 * 
 * @author Mahmoud Ismail <mahmoudahmedismail@gmail.com>
 */
public class Redundancy extends PortType {

    {
        negative(Encode.class);
        positive(EncodeResponse.class);
        negative(Decode.class);
        positive(DecodeResponse.class);
    }
}
