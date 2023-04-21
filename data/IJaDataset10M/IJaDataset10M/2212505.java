package org.openscience.cdk.renderer.color;

import java.awt.Color;
import org.junit.Assert;
import org.junit.Test;
import org.openscience.cdk.Atom;
import org.openscience.cdk.CDKTestCase;
import org.openscience.cdk.interfaces.IAtom;

/**
 * @cdk.module test-render
 */
public class RasmolColorsTest extends CDKTestCase {

    @Test
    public void testGetAtomColor() {
        RasmolColors colors = new RasmolColors();
        Assert.assertNotNull(colors);
        IAtom sulfur = new Atom("S");
        Assert.assertEquals(new Color(255, 200, 50), colors.getAtomColor(sulfur));
        IAtom helium = new Atom("He");
        Assert.assertEquals(new Color(255, 192, 203), colors.getAtomColor(helium));
    }

    @Test
    public void testGetDefaultAtomColor() {
        RasmolColors colors = new RasmolColors();
        Assert.assertNotNull(colors);
        IAtom imaginary = new Atom("Ix");
        Assert.assertEquals(Color.ORANGE, colors.getAtomColor(imaginary, Color.ORANGE));
    }
}
