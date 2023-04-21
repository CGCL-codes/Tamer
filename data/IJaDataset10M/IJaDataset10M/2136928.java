package net.sf.myra.datamining.util;

import java.io.IOException;
import java.io.InputStreamReader;
import junit.framework.TestCase;
import net.sf.myra.datamining.data.Dataset;
import net.sf.myra.datamining.io.ArffHelper;
import net.sf.myra.datamining.util.FlatCrossValidation;
import net.sf.myra.datamining.util.FlatCrossValidation.Mode;
import net.sf.myra.datamining.util.FlatCrossValidation.Partition;

/**
 * @author Fernando Esteban Barril Otero
 * @version $Revision: 38 $ $Date: 2008-05-29 01:07:35 +0100 (Thu, 29 May 2008) $
 */
public class FlatCrossValidationTest extends TestCase {

    public void testSplit() {
        try {
            Dataset dataset = new ArffHelper().read(new InputStreamReader(getClass().getResourceAsStream("/sample.arff")));
            Partition[] partitions = FlatCrossValidation.split(dataset, 2, Mode.STRATIFIED);
            assertEquals(7, partitions[0].getSize());
            assertEquals(7, partitions[1].getSize());
        } catch (IOException e) {
            fail(e.toString());
        }
    }
}
