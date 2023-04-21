package org.openscience.cdk.test.io;

import java.io.StringReader;
import java.io.StringWriter;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.Reaction;
import org.openscience.cdk.interfaces.IChemObjectBuilder;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.interfaces.IReaction;
import org.openscience.cdk.io.MDLRXNReader;
import org.openscience.cdk.io.MDLRXNWriter;
import org.openscience.cdk.test.CDKTestCase;

/**
 * TestCase for the writer MDL rxn files using one test file.
 *
 * @cdk.module test-io
 *
 * @see org.openscience.cdk.io.MDLRXNWriter
 */
public class MDLRXNWriterTest extends CDKTestCase {

    private IChemObjectBuilder builder;

    public MDLRXNWriterTest(String name) {
        super(name);
    }

    public void setUp() {
        builder = DefaultChemObjectBuilder.getInstance();
    }

    public static Test suite() {
        return new TestSuite(MDLRXNWriterTest.class);
    }

    public void testAccepts() throws Exception {
        MDLRXNWriter reader = new MDLRXNWriter();
        assertTrue(reader.accepts(Reaction.class));
    }

    public void testRoundtrip() throws Exception {
        IReaction reaction = builder.newReaction();
        IMolecule hydroxide = builder.newMolecule();
        hydroxide.addAtom(builder.newAtom("O"));
        reaction.addReactant(hydroxide);
        IMolecule proton = builder.newMolecule();
        proton.addAtom(builder.newAtom("H"));
        reaction.addReactant(proton);
        IMolecule water = builder.newMolecule();
        water.addAtom(builder.newAtom("O"));
        reaction.addProduct(water);
        StringWriter writer = new StringWriter(10000);
        String file = "";
        MDLRXNWriter mdlWriter = new MDLRXNWriter(writer);
        mdlWriter.write(reaction);
        mdlWriter.close();
        file = writer.toString();
        assertTrue(file.length() > 0);
        IReaction reaction2 = builder.newReaction();
        MDLRXNReader reader = new MDLRXNReader(new StringReader(file));
        reaction2 = (IReaction) reader.read(reaction2);
        reader.close();
        assertEquals(2, reaction2.getReactantCount());
        assertEquals(1, reaction2.getProductCount());
    }
}
