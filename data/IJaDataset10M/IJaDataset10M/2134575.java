package ssgen.sql.common.writer.function;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import ssgen.core.element.LiteralElement;
import ssgen.sql.common.element.function.SetFunction;
import ssgen.sql.common.writer.WriterTestUtils;

/**
 * @Author Tadaya Tsuyukubo
 * <p/>
 * $Id$
 */
public class SetFunctionWriterTest {

    @Test
    public void testWriteWithDistinct() {
        SetFunctionWriter writer = WriterTestUtils.prepareWriter(new SetFunctionWriter("avg"));
        SetFunction elem = new SetFunction();
        elem.setDistinct(true);
        elem.setElement(new LiteralElement("foo"));
        String sql = writer.write(elem);
        assertEquals("avg(distinct foo)", sql);
    }

    @Test
    public void testWriteWithAll() {
        SetFunctionWriter writer = WriterTestUtils.prepareWriter(new SetFunctionWriter("avg"));
        SetFunction elem = new SetFunction();
        elem.setAll(true);
        elem.setElement(new LiteralElement("foo"));
        String sql = writer.write(elem);
        assertEquals("avg(all foo)", sql);
    }

    @Test
    public void testAVG() {
        SetFunctionWriter writer = WriterTestUtils.prepareWriter(new SetFunctionWriter("avg"));
        SetFunction elem = new SetFunction();
        elem.setElement(new LiteralElement("foo"));
        String sql = writer.write(elem);
        assertEquals("avg(foo)", sql);
    }
}
