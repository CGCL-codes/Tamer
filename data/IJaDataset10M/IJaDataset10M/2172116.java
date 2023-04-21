package br.gov.framework.demoiselle.util;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import br.gov.framework.demoiselle.util.Constant;

public class ConstantTest {

    @Test
    public void testConfiguratorFilename() {
        assertEquals("demoiselle.properties", Constant.FRAMEWORK_CONFIGURATOR_FILE);
    }
}
