package org.sempere.commons.xsl.transformer;

import org.junit.*;
import static junit.framework.Assert.*;

/**
 * Unit tests class for Parameter class.
 *
 * @author bsempere
 */
public class ParameterTest {

    @Test
    public void isEmptyWhenNameIsNull() throws Exception {
        Parameter bean = new Parameter();
        bean.setName(null);
        bean.setValue("value");
        assertTrue(bean.isEmpty());
    }

    @Test
    public void isEmptyWhenNameIsEmpty() throws Exception {
        Parameter bean = new Parameter();
        bean.setName("");
        bean.setValue("value");
        assertTrue(bean.isEmpty());
    }

    @Test
    public void isEmptyWhenValueIsNull() throws Exception {
        Parameter bean = new Parameter();
        bean.setName("name");
        bean.setValue(null);
        assertTrue(bean.isEmpty());
    }

    @Test
    public void isEmptyWhenValueIsEmpty() throws Exception {
        Parameter bean = new Parameter();
        bean.setName("name");
        bean.setValue("");
        assertFalse(bean.isEmpty());
    }

    @Test
    public void isNotEmpty() throws Exception {
        Parameter bean = new Parameter();
        bean.setName("name");
        bean.setValue("value");
        assertFalse(bean.isEmpty());
    }
}
