package corner.orm.tapestry.validator;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jun.tsai@bjmaxinfo.com">Jun Tsai</a>
 * @version $Revision: 2477 $
 * @since 2.3
 */
public class NumPatternTest extends Assert {

    @Test
    public void test_getMessage() {
        NumPattern pattern = new NumPattern();
        pattern.setNumPattern("{6:2}");
        assertEquals(pattern.getMessage(), "错误的数字格式，正确的为：小数点前面至多6位，后面至多2位.");
    }
}
