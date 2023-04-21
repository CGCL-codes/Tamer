package org.timepedia.chronoscope.client.data;

import junit.framework.TestCase;
import org.timepedia.chronoscope.client.Dataset;
import org.timepedia.chronoscope.client.MutableDataset;
import org.timepedia.chronoscope.client.util.Array1D;
import org.timepedia.chronoscope.client.util.Interval;
import org.timepedia.chronoscope.client.util.MathUtil;
import org.timepedia.chronoscope.client.util.junit.OODoubleArray;

/**
 * @author chad takahashi
 */
public class ArrayDataset2DTest extends TestCase {

    private DatasetFactory dsFactory = new ChronoscopeDatasetFactory();

    private DatasetRequestMaker dsMaker = new DatasetRequestMaker();

    public void testTupleAccessors() {
        double[] domain = { 100.0, 200.0, 300.0 };
        double[] range = { 1.0, 2.0, 3.0 };
        DatasetRequest.Basic request = dsMaker.newRequest(domain, range);
        Dataset ds = dsFactory.create(request);
        for (int i = 0; i < ds.getNumSamples(); i++) {
            assertEquals(domain[i], ds.getFlyweightTuple(i).getDomain());
            assertEquals(range[i], ds.getFlyweightTuple(i).getRange0());
        }
    }

    public void testSinglePoint() {
        DatasetRequest request = dsMaker.newRequest(new double[] { 1000 }, new double[] { 10 });
        Dataset ds = dsFactory.create(request);
        assertEquals(1, ds.getNumSamples());
        assertEquals(new Interval(1000, 1000), ds.getDomainExtrema());
        assertEquals(new Interval(10, 10), ds.getRangeExtrema(0));
        assertEquals(0.0, ds.getMinDomainInterval());
    }

    public void testGeneral() {
        OODoubleArray domain = new OODoubleArray(new double[] { 1000, 2000, 3000, 4000 });
        OODoubleArray range = new OODoubleArray(new double[] { 10, 50, 40, 60 });
        DatasetRequest.Basic request = dsMaker.newRequest(domain.getArray(), range.getArray());
        Dataset ds = dsFactory.create(request);
        Dataset mutableDs = createMutableDataset(request);
        int numMipLevels = (int) MathUtil.log2(domain.size()) + 1;
        assertEqual(ds, mutableDs, numMipLevels);
    }

    private Dataset createMutableDataset(DatasetRequest.Basic request) {
        OODoubleArray domain = new OODoubleArray(request.getDomain());
        OODoubleArray range = new OODoubleArray(request.getRangeTupleSlice(0));
        request.setDomain(domain.removeLast().getArray());
        request.setRangeTupleSlice(0, range.removeLast().getArray());
        MutableDataset ds = dsFactory.createMutable(request);
        ds.mutate(Mutation.append(domain.getLast(), range.getLast()));
        return ds;
    }

    private static void assertEqual(Dataset expected, Dataset actual, int numMipLevels) {
        assertEquals(expected.getDomainExtrema(), actual.getDomainExtrema());
        assertEquals(expected.getRangeExtrema(0), actual.getRangeExtrema(0));
        assertEquals(expected.getMinDomainInterval(), actual.getMinDomainInterval());
        MipMapChain expectedChain = expected.getMipMapChain();
        MipMapChain actualChain = actual.getMipMapChain();
        for (int i = 0; i < numMipLevels; i++) {
            MipMap expectedMipMap = expectedChain.getMipMap(i);
            MipMap actualMipMap = actualChain.getMipMap(i);
            assertEquals(expectedMipMap.size(), actualMipMap.size());
            int numSamples = actualMipMap.size();
            Array1D expectedDomain = expectedMipMap.getDomain();
            Array1D actualDomain = actualMipMap.getDomain();
            for (int j = 0; j < numSamples; j++) {
                assertEquals(expectedDomain.get(j), actualDomain.get(j));
                assertEquals(expectedMipMap.getTuple(j).getRange0(), actualMipMap.getTuple(j).getRange0());
            }
        }
    }
}
