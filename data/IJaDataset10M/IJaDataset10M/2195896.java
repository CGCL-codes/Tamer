package yarfraw.atom10;

import java.util.Locale;
import junit.framework.TestCase;
import org.junit.Test;
import yarfraw.core.datamodel.FeedFormat;
import yarfraw.core.datamodel.Id;

public class CoverageTest extends TestCase {

    @Test
    public void testAtom() throws Exception {
        Id id = new Id();
        id.setIdValue("http://uri");
        id.setLang(Locale.ENGLISH);
        id.validate(FeedFormat.ATOM10);
        try {
            id.setIdValue(" ");
            id.validate(FeedFormat.ATOM10);
            fail("this is supposed to fail");
        } catch (Exception e) {
        }
    }
}
