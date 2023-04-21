package pl.edu.pjwstk.net.serviceproto;

import java.io.InputStream;
import pl.edu.pjwstk.net.ProtocolObject;
import pl.edu.pjwstk.types.ExtendedBitSet;

public class ServiceObject extends ProtocolObject {

    @Override
    public boolean tryParse(int fromPosition, ExtendedBitSet ebs) {
        return false;
    }

    @Override
    public boolean tryParse(byte[] bytes) {
        return false;
    }

    @Override
    public boolean tryParse(InputStream inputStream) {
        return false;
    }
}
