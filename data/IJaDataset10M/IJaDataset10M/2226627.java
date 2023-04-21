package esra.io.mol2;

import java.io.IOException;
import esra.core.Particles;
import esra.core.SimSystem;
import junit.framework.TestCase;

/**
 * @author mika
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class InMol2Test extends TestCase {

    public void testInMol2() {
    }

    public void testInMol2String() {
    }

    public void testReadMol2fromfile() throws IOException {
        InMol2 in = new InMol2("testData/CPCOHA.mol2");
        in.readMol2fromfile();
        Particles particles = in.parseParticles();
        SimSystem system = in.parseSystem();
        for (int i = 0; i < particles.number; ++i) {
            System.out.println(particles.anum[i] + " " + particles.name[i] + " " + particles.pos[i][0] + " " + particles.pos[i][1] + " " + particles.pos[i][2] + " " + particles.charge[i]);
        }
        for (int i = 0; i < system.bond.number; ++i) {
            System.out.println(system.bond.I[i] + " " + system.bond.J[i]);
        }
    }

    public void testParsefile() {
    }
}
