package net.sf.myra.antree;

import java.io.InputStreamReader;
import junit.framework.TestCase;
import net.sf.myra.antree.ThresholdHelper;
import net.sf.myra.datamining.data.ContinuousAttribute;
import net.sf.myra.datamining.data.Dataset;
import net.sf.myra.datamining.data.EntropyIntervalBuilder;
import net.sf.myra.datamining.data.IntervalBuilder;
import net.sf.myra.datamining.data.MDLEntropyIntervalBuilder;
import net.sf.myra.datamining.data.Metadata;
import net.sf.myra.datamining.data.Term;
import net.sf.myra.datamining.io.ArffHelper;

/**
 * @author Fernando Esteban Barril Otero
 * @version $Revision: 2336 $ $Date:: 2011-08-09 17:39:15#$
 */
public class ThresholdHelperTest extends TestCase {

    public void testCreateEntropy() throws Exception {
        Dataset dataset = new ArffHelper().read(new InputStreamReader(getClass().getResourceAsStream("/sample-continuous.arff")));
        dataset.computeDomains();
        Metadata metadata = dataset.getMetadata();
        ContinuousAttribute attribute = (ContinuousAttribute) metadata.get("temperature");
        System.setProperty(IntervalBuilder.BUILDER, EntropyIntervalBuilder.class.getName());
        Term[] terms = ThresholdHelper.create(metadata, attribute, dataset.getInstances());
        assertEquals(2, terms.length);
    }

    public void testCreateMDLEntropy() throws Exception {
        Dataset dataset = new ArffHelper().read(new InputStreamReader(getClass().getResourceAsStream("/sample-continuous.arff")));
        dataset.computeDomains();
        Metadata metadata = dataset.getMetadata();
        ContinuousAttribute attribute = (ContinuousAttribute) metadata.get("temperature");
        System.setProperty(IntervalBuilder.BUILDER, MDLEntropyIntervalBuilder.class.getName());
        Term[] terms = ThresholdHelper.create(metadata, attribute, dataset.getInstances());
        assertEquals(3, terms.length);
    }
}
