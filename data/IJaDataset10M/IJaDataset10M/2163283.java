package astcentric.structure.basic;

import astcentric.structure.basic.id.ASTID;
import junit.framework.TestCase;

public class ASTIDMapperTest extends TestCase {

    public void test() {
        ASTID id = new ASTID("a2-a3.b.c", "%20t8_u9@b.c", 63);
        String mappedID = ASTIDMapper.mapToFileName(id);
        assertEquals("a2_ma3_db_dc_s_p20t8__u9_ab_dc_s1v", mappedID);
    }
}
