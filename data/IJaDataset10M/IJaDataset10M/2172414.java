package org.openscience.cdk.tools;

import java.io.IOException;
import org.junit.Assert;
import org.junit.Test;
import org.openscience.cdk.CDKTestCase;

/**
 * @cdk.module test-qsar
 */
public class AtomicPropertiesTest extends CDKTestCase {

    @Test
    public void testGetInstance() throws IOException {
        AtomicProperties props = AtomicProperties.getInstance();
        Assert.assertNotNull(props);
        AtomicProperties props2 = AtomicProperties.getInstance();
        Assert.assertEquals(props2, props);
    }

    @Test
    public void testGetMass() throws Exception {
        AtomicProperties props = AtomicProperties.getInstance();
        double mass = props.getMass("C");
        Assert.assertTrue(mass > 0);
    }

    @Test
    public void testGetNormalizedMass() throws Exception {
        AtomicProperties props = AtomicProperties.getInstance();
        double mass = props.getNormalizedMass("C");
        Assert.assertTrue(mass > 0);
    }

    @Test
    public void testGetPolarizability() throws Exception {
        AtomicProperties props = AtomicProperties.getInstance();
        double polar = props.getPolarizability("C");
        Assert.assertTrue(polar > 0);
    }

    @Test
    public void testGetNormalizedPolarizability() throws Exception {
        AtomicProperties props = AtomicProperties.getInstance();
        double polar = props.getNormalizedPolarizability("C");
        Assert.assertTrue(polar > 0);
    }

    @Test
    public void testGetVdWVolume() throws Exception {
        AtomicProperties props = AtomicProperties.getInstance();
        double vol = props.getVdWVolume("C");
        Assert.assertTrue(vol > 0);
    }

    @Test
    public void testGetNormalizedVdWVolume() throws Exception {
        AtomicProperties props = AtomicProperties.getInstance();
        double vol = props.getNormalizedVdWVolume("C");
        Assert.assertTrue(vol > 0);
    }

    @Test
    public void testGetElectronegativity() throws Exception {
        AtomicProperties props = AtomicProperties.getInstance();
        double eneg = props.getElectronegativity("C");
        Assert.assertTrue(eneg > 0);
    }

    @Test
    public void testGetNormalizedElectronegativity() throws Exception {
        AtomicProperties props = AtomicProperties.getInstance();
        double eneg = props.getNormalizedElectronegativity("C");
        Assert.assertTrue(eneg > 0);
    }
}
