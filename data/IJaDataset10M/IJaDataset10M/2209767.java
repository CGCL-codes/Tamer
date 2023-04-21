package ra.lajolla;

import java.util.ArrayList;
import junit.framework.TestCase;
import ra.lajolla.transformation.protein.PDBProteinTranslator;
import ra.lajolla.utilities.FileOperationsManager2;

public class LoadSequenceDBTest extends TestCase {

    /**
	 * testtesttest
	 */
    public void testLoadFromRecursivelyFromDirectory() {
        SequenceDB sequenceDB = FileOperationsManager2.generateSequenceDBRecursivelyFromDirOrFile("src/test/resources/astral_small/", new PDBProteinTranslator(10, false), 8);
    }
}
