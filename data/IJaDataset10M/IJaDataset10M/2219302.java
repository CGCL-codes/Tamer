package org.arastreju.api.common;

import java.util.HashSet;
import java.util.Set;
import junit.framework.TestCase;
import org.arastreju.api.terminology.attributes.Casus;
import org.arastreju.api.terminology.attributes.WordClass;
import de.lichtflut.infra.logging.Log;

/**
 * 
 * 
 * @author Oliver Tigges
 * 
 * Created: 27.04.2009
 *
 */
public class GrammaticalConstraintTest extends TestCase {

    public void testConstraint() {
        GrammaticalCharacteristic gc1 = new GrammaticalCharacteristic();
        gc1.setAttribute(GrammaticalAttribute.WORD_CLASS, WordClass.PRONOUN);
        gc1.setAttribute(GrammaticalAttribute.CASUS, Casus.AKKUSATIV, Casus.DATIV);
        GrammaticalCharacteristic gc2 = new GrammaticalCharacteristic();
        gc2.setAttribute(GrammaticalAttribute.WORD_CLASS, WordClass.NOUN);
        gc2.setAttribute(GrammaticalAttribute.CASUS, Casus.AKKUSATIV, Casus.NOMINATIV);
        Set<GrammaticalCharacteristic> gcs = new HashSet<GrammaticalCharacteristic>();
        gcs.add(gc1);
        gcs.add(gc2);
        GrammaticalProperties gp = new GrammaticalProperties(gcs);
        GrammaticalConstraint c1 = new GrammaticalConstraint();
        c1.addConstraint(GrammaticalAttribute.WORD_CLASS, WordClass.PRONOUN);
        assertTrue(c1.isValid(gc1));
        assertFalse(c1.isValid(gc2));
        assertTrue(c1.isValid(gp));
    }

    public void testToGP() {
        GrammaticalConstraint c1 = new GrammaticalConstraint();
        c1.addConstraint(GrammaticalAttribute.WORD_CLASS, WordClass.PRONOUN);
        GrammaticalProperties gp = c1.toSampleProperties();
        Log.debug(this, "gp rep: " + gp);
    }
}
