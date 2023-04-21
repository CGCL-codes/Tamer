package org.openscience.cdk.tools;

import org.junit.Assert;
import org.junit.Test;
import org.openscience.cdk.Atom;
import org.openscience.cdk.Bond;
import org.openscience.cdk.Molecule;
import org.openscience.cdk.CDKTestCase;
import org.openscience.cdk.interfaces.IBond;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * @cdk.module test-smiles
 */
public class NormalizerTest extends CDKTestCase {

    @Test
    public void testNormalize() throws Exception {
        Molecule ac = new Molecule();
        ac.addAtom(new Atom("C"));
        ac.addAtom(new Atom("N"));
        ac.addAtom(new Atom("O"));
        ac.addAtom(new Atom("O"));
        ac.addBond(new Bond(ac.getAtom(0), ac.getAtom(1)));
        ac.addBond(new Bond(ac.getAtom(1), ac.getAtom(2), IBond.Order.DOUBLE));
        ac.addBond(new Bond(ac.getAtom(1), ac.getAtom(3), IBond.Order.DOUBLE));
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.newDocument();
        Element set = doc.createElement("replace-set");
        doc.appendChild(set);
        Element replace = doc.createElement("replace");
        set.appendChild(replace);
        replace.appendChild(doc.createTextNode("O=N=O"));
        Element replacement = doc.createElement("replacement");
        set.appendChild(replacement);
        replacement.appendChild(doc.createTextNode("[O-][N+]=O"));
        Normalizer.normalize(ac, doc);
        Assert.assertTrue(ac.getBond(1).getOrder() == IBond.Order.SINGLE ^ ac.getBond(2).getOrder() == IBond.Order.SINGLE);
    }
}
