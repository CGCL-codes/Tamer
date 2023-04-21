package org.geotools.caching;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import org.opengis.filter.Filter;
import org.geotools.caching.grid.GridFeatureCache;
import org.geotools.caching.spatialindex.store.DiskStorage;
import org.geotools.caching.spatialindex.store.MemoryStorage;
import org.geotools.caching.util.Generator;
import org.geotools.data.FeatureSource;
import org.geotools.data.FeatureStore;
import org.geotools.data.memory.MemoryDataStore;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.filter.spatial.BBOXImpl;

public class BenchMark {

    FeatureSource control;

    AbstractFeatureCache[] sample;

    FeatureCollection dataset;

    int numdata = 5000;

    List<Filter> filterset;

    int numfilters = 150;

    double[] windows = new double[] { .05, .025, .001 };

    double[] windows_real = new double[] {};

    void createUnitsquareDataSet() {
        Generator gen = new Generator(1, 1);
        dataset = new DefaultFeatureCollection("Test", Generator.type);
        for (int i = 0; i < numdata; i++) {
            dataset.add(gen.createFeature(i));
        }
    }

    void createUnitsquareFilterSet() {
        filterset = new ArrayList<Filter>(numfilters);
        Coordinate p = new Coordinate(0.5, 0.5);
        for (int i = 0; i < numfilters; i += windows.length) {
            for (int j = 0; j < windows.length; j++) {
                filterset.add(Generator.createBboxFilter(p, windows[j], windows[j]));
                p = Generator.pickRandomPoint(p, windows[j], windows[j]);
            }
        }
    }

    void createFilterSet(Envelope e) {
        filterset = new ArrayList<Filter>(numfilters);
        Coordinate p = new Coordinate(e.centre());
        double width = e.getMaxX() - e.getMinX();
        double height = e.getMaxY() - e.getMinY();
        for (int i = 0; i < numfilters; i += windows.length) {
            for (int j = 0; j < windows.length; j++) {
                BBOXImpl f = (BBOXImpl) Generator.createBboxFilter(p, windows[j], windows[j]);
                f.setPropertyName("road");
                f.setSRS("");
                filterset.add(f);
                p = Generator.pickRandomPoint(p, windows[j], windows[j]);
            }
        }
    }

    void initLocalControl() throws IOException {
        MemoryDataStore ds = new MemoryDataStore();
        ds.createSchema(dataset.getSchema());
        ds.addFeatures(dataset);
        control = (FeatureStore) ds.getFeatureSource(dataset.getSchema().getTypeName());
    }

    QueryStatistics[] runQueries(FeatureSource fs) {
        QueryStatistics[] stats = new QueryStatistics[filterset.size()];
        Iterator<Filter> iter = filterset.iterator();
        int i = 0;
        while (iter.hasNext()) {
            Filter f = iter.next();
            stats[i] = new QueryStatistics();
            double progress = (100d * i) / filterset.size();
            if ((10 * (int) (progress / 10)) == progress) {
                System.out.print((int) progress + "..");
            }
            try {
                long startTime = System.currentTimeMillis();
                FeatureCollection resultSet = fs.getFeatures(f);
                FeatureIterator fiter = resultSet.features();
                while (fiter.hasNext()) {
                    fiter.next();
                }
                resultSet.close(fiter);
                long endTime = System.currentTimeMillis();
                stats[i].setNumberOfFeatures(resultSet.size());
                stats[i].setExecutionTime(endTime - startTime);
            } catch (IOException e) {
                e.printStackTrace();
            }
            i++;
        }
        System.out.println("done.");
        return stats;
    }

    void printResults(QueryStatistics[] control_stats, QueryStatistics[] ds_stats, PrintStream out) {
        long meanDifference = 0;
        long meanOverhead = 0;
        int overheadCount = 0;
        long meanLeverage = 0;
        int leverageCount = 0;
        boolean conform = true;
        for (int i = 0; i < filterset.size(); i++) {
            if (i > 1) {
                long diff = ds_stats[i].getExecutionTime() - control_stats[i].getExecutionTime();
                meanDifference += diff;
                if (diff > 0) {
                    overheadCount++;
                    meanOverhead += diff;
                } else {
                    leverageCount++;
                    meanLeverage += diff;
                }
            }
            if (ds_stats[i].getNumberOfFeatures() != control_stats[i].getNumberOfFeatures()) {
                conform = false;
                System.err.println("Query " + i + " : Got " + ds_stats[i].getNumberOfFeatures() + " features, expected " + control_stats[i].getNumberOfFeatures());
            }
        }
        if (conform) {
            out.println("Query results seem to be ok.");
        } else {
            out.println("Sample did not yield same results as control.");
        }
        meanDifference = ((filterset.size() - 2) > 0) ? (meanDifference / (filterset.size() - 2)) : 0;
        meanOverhead = (overheadCount > 0) ? (meanOverhead / overheadCount) : 0;
        meanLeverage = (leverageCount > 0) ? (meanLeverage / leverageCount) : 0;
        out.println("Mean execution time difference = " + meanDifference + " ms.");
        out.println("Mean overhead = " + meanOverhead + " ms. (" + ((100 * overheadCount) / (overheadCount + leverageCount)) + " %)");
        out.println("Mean leverage = " + meanLeverage + " ms. (" + ((100 * leverageCount) / (overheadCount + leverageCount)) + " %)");
    }

    public void localSetup() throws IOException {
        System.out.print("DataSet : ");
        createUnitsquareDataSet();
        System.out.println("OK");
        System.out.print("Control (init) : ");
        initLocalControl();
        System.out.println("OK");
        System.out.print("FilterSet : ");
        createUnitsquareFilterSet();
        System.out.println("OK");
    }

    public static void main(String[] args) {
        BenchMark thisClass = new BenchMark();
        try {
            thisClass.localSetup();
            System.out.print("Sample (init) : ");
            thisClass.sample = new AbstractFeatureCache[4];
            thisClass.sample[0] = new GridFeatureCache(thisClass.control, 500, 1000, new MemoryStorage(100));
            thisClass.sample[1] = new GridFeatureCache(thisClass.control, 500, 2500, new MemoryStorage(100));
            DiskStorage storage = new DiskStorage(File.createTempFile("cache", ".tmp"), 1000);
            thisClass.sample[2] = new GridFeatureCache(thisClass.control, 500, 1000, storage);
            storage = new DiskStorage(File.createTempFile("cache", ".tmp"), 1000);
            thisClass.sample[3] = new GridFeatureCache(thisClass.control, 500, 2500, storage);
            System.out.println("OK");
            System.out.print("Control (run) : ");
            QueryStatistics[] control_stats = thisClass.runQueries(thisClass.control);
            QueryStatistics[][] sample_stats = new QueryStatistics[thisClass.sample.length][];
            for (int i = 0; i < thisClass.sample.length; i++) {
                System.out.print("Sample " + i + " (run) : ");
                sample_stats[i] = thisClass.runQueries(thisClass.sample[i]);
                System.out.println(thisClass.sample[i]);
                thisClass.printResults(control_stats, sample_stats[i], System.out);
                System.out.println(thisClass.sample[i].sourceAccessStats());
            }
            for (int i = thisClass.sample.length - 1; i >= 0; i--) {
                System.out.print("Sample " + i + " (run inverse order) : ");
                sample_stats[i] = thisClass.runQueries(thisClass.sample[i]);
                System.out.println(thisClass.sample[i]);
                thisClass.printResults(control_stats, sample_stats[i], System.out);
                System.out.println(thisClass.sample[i].sourceAccessStats());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (FeatureCacheException e) {
            e.printStackTrace();
        }
    }

    class QueryStatistics {

        private int numberOfFeatures;

        private long executionTime;

        /**
         * @return  the executionTime
         * @uml.property  name="executionTime"
         */
        public long getExecutionTime() {
            return executionTime;
        }

        /**
         * @param executionTime  the executionTime to set
         * @uml.property  name="executionTime"
         */
        public void setExecutionTime(long executionTime) {
            this.executionTime = executionTime;
        }

        /**
         * @return  the numberOfFeatures
         * @uml.property  name="numberOfFeatures"
         */
        public int getNumberOfFeatures() {
            return numberOfFeatures;
        }

        /**
         * @param numberOfFeatures  the numberOfFeatures to set
         * @uml.property  name="numberOfFeatures"
         */
        public void setNumberOfFeatures(int numberOfFeatures) {
            this.numberOfFeatures = numberOfFeatures;
        }
    }
}
