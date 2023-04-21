package uk.ac.ebi.intact.uniprot.model;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 * UniprotSpliceVariant Tester.
 *
 * @author Samuel Kerrien (skerrien@ebi.ac.uk)
 * @version $Id$
 * @since 2.0.0
 */
public class UniprotSpliceVariantTest {

    @Test
    public void PrimaryAc() throws Exception {
        UniprotSpliceVariant sv = new UniprotSpliceVariant("P12345-1", new Organism(1), "ABCD");
        assertEquals("P12345-1", sv.getPrimaryAc());
        try {
            new UniprotSpliceVariant("", new Organism(1), "ABCD");
            fail();
        } catch (Exception e) {
        }
        try {
            new UniprotSpliceVariant(null, new Organism(1), "ABCD");
            fail();
        } catch (Exception e) {
        }
    }

    @Test
    public void Sequence() throws Exception {
        UniprotSpliceVariant sv = new UniprotSpliceVariant("P12345-1", new Organism(1), "ABCD");
        assertEquals("ABCD", sv.getSequence());
        sv.setSequence("ACBEDFG");
        assertEquals("ACBEDFG", sv.getSequence());
    }

    @Test
    public void Organism() throws Exception {
        UniprotSpliceVariant sv = new UniprotSpliceVariant("P12345-1", new Organism(1), "ABCD");
        assertEquals(new Organism(1), sv.getOrganism());
        try {
            new UniprotSpliceVariant("PRO_123", null, "ABCD");
            fail();
        } catch (Exception e) {
        }
        try {
            sv.setOrganism(null);
            fail();
        } catch (Exception e) {
        }
    }

    @Test
    public void Start() throws Exception {
        UniprotSpliceVariant sv = new UniprotSpliceVariant("P12345-1", new Organism(1), "ABCD");
        sv.setStart(2);
        assertEquals(new Integer(2), sv.getStart());
        try {
            sv.setStart(-1);
            fail();
        } catch (Exception e) {
        }
        try {
            sv.setEnd(4);
            sv.setStart(5);
            fail();
        } catch (Exception e) {
        }
    }

    @Test
    public void End() throws Exception {
        UniprotSpliceVariant sv = new UniprotSpliceVariant("P12345-1", new Organism(1), "ABCD");
        sv.setEnd(5);
        assertEquals(new Integer(5), sv.getEnd());
        try {
            sv.setEnd(-1);
            fail();
        } catch (Exception e) {
        }
        try {
            sv.setStart(3);
            sv.setEnd(2);
            fail();
        } catch (Exception e) {
        }
    }

    @Test
    public void GetSecondaryAcs() throws Exception {
        UniprotSpliceVariant sv = new UniprotSpliceVariant("P12345-1", new Organism(1), "ABCD");
        assertNotNull(sv.getSecondaryAcs());
        assertEquals(0, sv.getSecondaryAcs().size());
        sv.getSecondaryAcs().add("Q99999");
        assertEquals(1, sv.getSecondaryAcs().size());
        assertTrue(sv.getSecondaryAcs().contains("Q99999"));
    }

    @Test
    public void GetSynomyms() throws Exception {
        UniprotSpliceVariant sv = new UniprotSpliceVariant("P12345-1", new Organism(1), "ABCD");
        assertNotNull(sv.getSynomyms());
        assertEquals(0, sv.getSynomyms().size());
        sv.getSynomyms().add("bla");
        assertEquals(1, sv.getSynomyms().size());
        assertTrue(sv.getSynomyms().contains("bla"));
    }

    @Test
    public void Note() throws Exception {
        UniprotSpliceVariant sv = new UniprotSpliceVariant("P12345-1", new Organism(1), "ABCD");
        assertNull(sv.getNote());
        sv.setNote("a note");
        assertEquals("a note", sv.getNote());
    }
}
