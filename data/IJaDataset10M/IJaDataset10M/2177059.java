package org.timepedia.chronoscope.client.data.mock;

import com.google.inject.Inject;
import org.timepedia.chronoscope.client.Dataset;
import org.timepedia.chronoscope.client.data.DatasetFactory;
import org.timepedia.chronoscope.client.data.DatasetRequest;
import org.timepedia.chronoscope.client.util.TimeUnit;
import org.timepedia.chronoscope.client.util.date.ChronoDate;

/**
 * Factory for creating in-memory mock datasets for testing.
 *
 * @author chad takahashi
 */
public class MockDatasetFactory {

    private DatasetFactory dsFactory;

    public MockDatasetFactory(DatasetFactory datasetFactory) {
        dsFactory = datasetFactory;
    }

    @Inject
    public void setDsFactory(DatasetFactory dsFactory) {
        this.dsFactory = dsFactory;
    }

    /**
   * Creates a basic sine wave composed of 1000 points spaced 1 day apart
   * starting at year 1970.
   */
    public Dataset getBasicDataset() {
        int numSamples = 1000;
        ChronoDate d = ChronoDate.get(1970, 0, 1);
        double[] domainValues = new double[numSamples];
        double[] rangeValues = new double[numSamples];
        for (int i = 0; i < numSamples; i++) {
            double tmp = 5.0 * (double) i / (double) numSamples;
            double ry = Math.sin(Math.PI * tmp) / Math.exp(tmp / 5.0);
            domainValues[i] = d.getTime();
            rangeValues[i] = ry;
            d.add(TimeUnit.DAY, 1);
        }
        DatasetRequest.Basic request = new DatasetRequest.Basic();
        request.setIdentifier("mock");
        request.setAxisId("none");
        request.setRangeLabel("Mock");
        request.setDomain(domainValues);
        request.addRangeTupleSlice(rangeValues);
        return dsFactory.create(request);
    }
}
