package net.sourceforge.simulaeco.genetico;

import org.apache.commons.lang.math.RandomUtils;
import org.apache.log4j.Logger;
import org.junit.Test;

/**
 * @author Rafael Gonzaga Camargo
 * @date 13/03/2010 {@literal Descri��o: ???}
 */
public class CromossomoTest {

    private static final Logger LOG = Logger.getLogger(CromossomoTest.class);

    /**
     * Test method for
     * {@link net.sourceforge.simulaeco.genetico.CromossomoImpl#Cromossomo(int, java.lang.Class)}
     * .
     */
    @Test
    public void testCromossomo() {
        ICromossomo cromo = new CromossomoImpl(25, true);
        ICromossomo cromo2 = cromo.clone();
        junit.framework.Assert.assertEquals(cromo, cromo2);
        for (int i = 0; i < 10000; i++) {
            LOG.info(RandomUtils.nextInt(10));
        }
        LOG.info(cromo);
    }
}
