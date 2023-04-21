package test.org.peertrust.meta;

import java.util.Vector;
import org.peertrust.inference.prolog.minerva.MinervaProlog;
import org.peertrust.meta.Tree;
import junit.framework.*;

/**
 * $Id: TreeTest.java,v 1.4 2006/03/06 12:48:00 dolmedilla Exp $
 * Date: 05-Dec-2003
 * Last changed: $Date: 2006/03/06 12:48:00 $
 * by $Author: dolmedilla $
 * @author olmedilla
 */
public class TreeTest extends TestCase {

    public MinervaProlog engine = new MinervaProlog();

    public TreeTest(String name) {
        super(name);
    }

    public static Test suite() {
        return new TestSuite(TreeTest.class);
    }

    public void setUp() {
    }

    public void testParse1() {
        String proof = "[r(request(spanishCourse,V18725445)$alice,[policy1(request(spanishCourse),alice)],[get(spanishCourse,V18725445)])@elearn]";
        Tree tree = new Tree(1);
        Vector vector = tree.generateProofVector(proof);
        assertEquals(1, vector.size());
    }

    public void testParse2() {
        String proof = "[r(policy1(request(spanishCourse),alice),[drivingLicense(alice)@caState@alice,policeOfficer(alice)@caStatePolice@alice],[])@elearn]";
        Tree tree = new Tree(1);
        Vector vector = tree.generateProofVector(proof);
        assertEquals(1, vector.size());
    }

    public void testParse3() {
        String proof = "[]";
        Tree tree = new Tree(1);
        Vector vector = tree.generateProofVector(proof);
        assertEquals(0, vector.size());
    }

    public static void main(String[] args) {
        try {
            junit.textui.TestRunner.run(suite());
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
