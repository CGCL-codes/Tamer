package corner.util;

import java.util.Arrays;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jun.tsai@bjmaxinfo.com">Jun Tsai</a>
 * @version $Revision: 2096 $
 * @since 2.2.2
 */
public class RegexTest {

    @Test
    public void test_UniqueReg() {
        String str = "{className:propertyName}";
        String[] strs = str.split("\\{(^:):(^})\\}");
        System.out.println(Arrays.asList(strs));
    }
}
