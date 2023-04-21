package org.fudaa.dodico.crue.io.common;

import java.util.ArrayList;
import java.util.List;
import org.fudaa.dodico.crue.common.AbstractTestCase;
import org.fudaa.dodico.crue.config.TestCrueConfigMetierLoaderDefault;
import org.fudaa.dodico.crue.config.CrueOptions;
import org.fudaa.dodico.crue.metier.emh.Loi;
import org.fudaa.dodico.crue.metier.emh.LoiDF;

/**
 * @author deniger
 */
public class TestCrueDataImpl extends AbstractTestCase {

    /**
   * Test du conteneur de lois.
   */
    public void testAddLoi() {
        final CrueData impl = new CrueDataImpl(TestCrueConfigMetierLoaderDefault.DEFAULT, new CrueOptions(false));
        final List<Loi> lois = new ArrayList<Loi>();
        lois.add(new LoiDF());
        impl.getLoiConteneur().addAllLois(lois);
        final List<Loi> savedLoi = impl.getLois();
        assertEquals(savedLoi.get(0), lois.get(0));
    }
}
