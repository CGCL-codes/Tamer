package org.gochacha.test.retro;

import junit.framework.TestCase;
import org.apache.commons.beanutils.converters.BigDecimalConverter;
import org.gochacha.ObjectBox;
import org.gochacha.impl.DefaultConverterSet;
import org.gochacha.retro.BeanUtilsRetroConverter;
import java.math.BigDecimal;

public class RetroTest extends TestCase {

    public void testBeanUtilsConverter() {
        RetroObject obj = new RetroObject();
        ObjectBox objectBox = new ObjectBox(obj);
        DefaultConverterSet converters = (DefaultConverterSet) objectBox.getConverters();
        BeanUtilsRetroConverter retroConverter = new BeanUtilsRetroConverter(BigDecimal.class, new BigDecimalConverter());
        converters.addConverter(BigDecimal.class, retroConverter);
        objectBox.setField("bigDecimalField", "100");
        assertNotNull(obj.getBigDecimalField());
        assertEquals(100, obj.getBigDecimalField().intValue());
    }
}
