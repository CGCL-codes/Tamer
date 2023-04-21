package org.expasy.jpl.insilico.bio.aa;

import static org.junit.Assert.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.expasy.jpl.insilico.bio.JPLMassAccuracy;
import org.expasy.jpl.insilico.bio.aa.JPLAAByteUndefinedException;
import org.expasy.jpl.insilico.bio.aa.JPLAACharUndefinedException;
import org.expasy.jpl.insilico.bio.aa.JPLAAEncoding;
import org.expasy.jpl.insilico.bio.aa.JPLAAResiduesSingletonClass;
import org.junit.Before;
import org.junit.Test;

public class JPLAAResiduesSingletonClassTest {

    private JPLAAResiduesSingletonClass residues;

    private double delta = 0.01;

    private static Logger logger = Logger.getAnonymousLogger();

    @Before
    public final void testGetInstance() {
        residues = JPLAAResiduesSingletonClass.getInstance();
        logger.info("Singleton constructed.");
        JPLAAResiduesSingletonClass residuesRef = JPLAAResiduesSingletonClass.getInstance();
        logger.log(Level.INFO, "checking singletons for equality");
        assertSame("do not refer the same object.", residuesRef, residues);
    }

    @Test
    public final void testGetResidueMass() throws JPLAAByteUndefinedException, JPLAACharUndefinedException {
        double expectedMassLeu = 113.08;
        double massLeu = 0;
        massLeu = residues.getResidueMass(JPLAAEncoding.toByte('L'), JPLMassAccuracy.MONOISOTOPIC);
        logger.info("checking masses for equality");
        assertEquals("both masses are equal", massLeu, expectedMassLeu, delta);
    }

    @Test
    public final void testGetResidueMassFailure() throws JPLAAByteUndefinedException, JPLAACharUndefinedException {
        double expectedMassLeu = 113.08;
        double massLeu = 0;
        massLeu = residues.getResidueMass(JPLAAEncoding.toByte('L'), JPLMassAccuracy.AVERAGE);
        assertTrue("both mass are differents.", Math.abs(massLeu - expectedMassLeu) > delta);
    }

    @Test(expected = JPLAACharUndefinedException.class)
    public final void testResidue() throws JPLAAByteUndefinedException, JPLAACharUndefinedException {
        residues.getResidueMass(JPLAAEncoding.toByte('?'), JPLMassAccuracy.AVERAGE);
    }

    @Test(expected = JPLAAMassByteUndefinedException.class)
    public final void testResidueMassAmbiguity() throws JPLAAByteUndefinedException, JPLAACharUndefinedException {
        residues.getResidueMass(JPLAAEncoding.toByte('B'), JPLMassAccuracy.AVERAGE);
    }

    @Test
    public final void testStaticClass() throws JPLAAByteUndefinedException, JPLAACharUndefinedException {
        double expectedMassLeu = 113.08;
        double massLeu = 0;
        massLeu = JPLAAResidues.getResidueMass(JPLAAEncoding.toByte('L'), JPLMassAccuracy.MONOISOTOPIC);
        assertEquals("both masses are equal", massLeu, expectedMassLeu, delta);
    }
}
