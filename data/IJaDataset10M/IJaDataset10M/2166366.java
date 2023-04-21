package net.sourceforge.ondex.parser.aracyc2.misc;

import java.io.FileNotFoundException;
import junit.framework.TestCase;
import net.sourceforge.ondex.parser.aracyc2.parser.concret.ProteinParser;
import net.sourceforge.ondex.parser.aracyc2.parser.util.DatFileReader;
import net.sourceforge.ondex.parser.aracyc2.sink.Protein;

public class ProteinParserTest extends TestCase {

    DatFileReader fileReader;

    public void setUp() throws Exception {
        fileReader = new DatFileReader("data/importdata/aracyc2/proteins.dat", new ProteinParser());
    }

    public void testSynonym() throws Exception {
        while (fileReader.hasNext()) {
        }
    }

    public void testUniqueIdRead() throws FileNotFoundException {
        String[] UniqueId = { "ENTB-CPLX", "APO-ENTB", "CPLX-2", "CPLX-1" };
        int i = 0;
        while (fileReader.hasNext()) {
            System.out.println(i++);
            Protein protein = (Protein) fileReader.next();
            if (i < 4) {
                assertEquals(UniqueId[i], protein.getUniqueId());
                i++;
            }
        }
    }

    public void testReadProteins() {
        int i = 0;
        while (fileReader.hasNext()) {
            i++;
        }
        assertEquals(i, 6191);
    }

    public void testDistribute() throws Exception {
        ProteinParser p = new ProteinParser();
        p.start("PROTEIN-123");
        p.distribute("CATALYZES", "ENZYME-123");
        Protein protein2 = (Protein) p.getNode();
        p.start("PROTEIN-1234");
        p.distribute("CATALYZES", "ENZYME-123");
        Protein protein1 = (Protein) p.getNode();
        assertEquals(protein1.getIsMemberOf().size(), 1);
        assertEquals(protein1.getIsMemberOf().get(0).getUniqueId(), "ENZYME-123");
        assertEquals(protein2.getIsMemberOf().get(0).getUniqueId(), "ENZYME-123");
        assertEquals(protein2.getUniqueId(), "PROTEIN-123");
        assertEquals(protein1.getUniqueId(), "PROTEIN-1234");
        p.distribute("COMPONENT-OF", "PROTEIN-123");
        assertEquals(protein1.getComponentOf().get(0).getUniqueId(), "PROTEIN-123");
    }
}
