package gilgamesh.annotations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.containsString;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

public class ToStringTest {

    private static final Log logger = LogFactory.getLog(ToStringTest.class);

    @Test
    public void testSimpleBean() {
        final SimpleBean bean = new SimpleBean();
        bean.setId(2);
        bean.setName("simpleBean2");
        logger.info(bean);
        assertEquals("SimpleBean[id=2,name=simpleBean2]", bean.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void illegalJPA() {
        IllegalJPABean bean = new IllegalJPABean();
        bean.toString();
    }

    @Test
    public void jpaBean() {
        JPABean bean = new JPABean();
        bean.id = 2;
        logger.info(bean);
        assertEquals("JPABean[id=2]", bean.toString());
    }

    @Test
    public void jpaMissingId() {
        JPAMissingIdBean bean = new JPAMissingIdBean();
        bean.id = 2;
        logger.info(bean);
        assertEquals("JPAMissingIdBean[]", bean.toString());
    }

    @Test
    public void missingOverride() {
        MissingOverrideBean bean = new MissingOverrideBean();
        bean.id = 2;
        logger.info(bean);
        assertThat(bean.toString(), containsString("gilgamesh.annotations.MissingOverrideBean@"));
    }
}
