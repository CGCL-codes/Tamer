package edu.ucdavis.genomics.metabolomics.binbase.quality.server.util;

import static org.junit.Assert.assertTrue;
import java.util.Collection;
import java.util.Vector;
import org.junit.Test;

/**
 * simple test for the quality standard
 * 
 * @author wohlgemuth
 * 
 */
public class QualityStandardTest {

    @Test
    public void testGetPattern() {
        QualityStandard standard = new QualityStandard();
        standard.setPattern("dad");
        assertTrue(standard.getPattern().equals("dad"));
        standard.setPattern("set");
        assertTrue(standard.getPattern().equals("set"));
    }

    @Test
    public void testGetPosition() {
        QualityStandard standard = new QualityStandard();
        standard.setPosition(1);
        assertTrue(standard.getPosition() == 1);
        standard.setPosition(12);
        assertTrue(standard.getPosition() == 12);
    }

    @Test
    public void testEqualsObject() {
        QualityStandard a = new QualityStandard();
        QualityStandard b = new QualityStandard();
        assertTrue(a.equals(b));
        a.setPattern("dadasda");
        assertTrue(a.equals(b) == false);
        b.setPattern("dadasda");
        assertTrue(a.equals(b) == true);
        a.setPosition(1);
        assertTrue(a.equals(b) == false);
        b.setPosition(1);
        assertTrue(a.equals(b) == true);
        a.setConcentration(3.0);
        assertTrue(a.equals(b) == false);
        b.setConcentration(3.0);
        assertTrue(a.equals(b) == true);
    }

    @Test
    public void testAddToCollection() {
        Collection<QualityStandard> del = new Vector<QualityStandard>();
        del.add(new QualityStandard("[0-9]{6}[a-z]{3}qc10_1", 0, 10));
        del.add(new QualityStandard("[0-9]{6}[a-z]{3}qc25_1", 1, 25));
        del.add(new QualityStandard("[0-9]{6}[a-z]{3}qc50_1", 2, 50));
        del.add(new QualityStandard("[0-9]{6}[a-z]{3}qc100_1", 3, 100));
        del.add(new QualityStandard("[0-9]{6}[a-z]{3}qc250_1", 4, 250));
        assertTrue(del.contains(new QualityStandard("[0-9]{6}[a-z]{3}qc10_1", 0, 10)));
        assertTrue(del.contains(new QualityStandard("[0-9]{6}[a-z]{3}qc10_1", 0, 15)) == false);
    }
}
