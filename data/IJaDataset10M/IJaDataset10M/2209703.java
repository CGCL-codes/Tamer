package evolaris.framework.sys;

import evolaris.framework.sys.business.IdCodec;
import junit.framework.TestCase;

public class IdCodecTest extends TestCase {

    public void testEncodeId() {
        System.out.println(IdCodec.encodeId(3));
    }
}
