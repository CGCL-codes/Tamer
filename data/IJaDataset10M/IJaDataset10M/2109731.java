package com.vividsolutions.jump.workbench.ui.plugin.analysis;

import java.util.*;
import com.vividsolutions.jump.task.*;
import com.vividsolutions.jts.geom.*;
import com.vividsolutions.jump.feature.*;

/**
 * Exceutes a spatial query with a given mask FeatureCollection, source FeatureCollection,
 * and predicate.
 * Ensures result does not contain duplicates.
 *
 * @author Martin Davis
 * @version 1.0
 */
public class SpatialQueryExecuter {

    private FeatureCollection maskFC;

    private FeatureCollection sourceFC;

    private FeatureCollection queryFC;

    private boolean complementResult = false;

    private boolean allowDuplicatesInResult = false;

    private boolean isExceptionThrown = false;

    private Geometry geoms[] = new Geometry[2];

    private Set resultSet = new HashSet();

    public SpatialQueryExecuter(FeatureCollection maskFC, FeatureCollection sourceFC) {
        this.maskFC = maskFC;
        this.sourceFC = sourceFC;
    }

    /**
   * Sets whether duplicate features are allowed in the result set.
   *
   * @param isRemoveDuplicates true if duplicates are allowed
   */
    public void setAllowDuplicates(boolean isAllowDuplicates) {
        this.allowDuplicatesInResult = isAllowDuplicates;
    }

    /**
   * Sets whether the result set should be complemented
   *
   * @param complementResult true if the result should be complemented
   */
    public void setComplementResult(boolean complementResult) {
        this.complementResult = complementResult;
    }

    /**
   * Gets the featurec collection to query.
   * This may be indexed if this would improve performance.
   *
   * @param func
   * @return
   */
    private void createQueryFeatureCollection(GeometryPredicate pred) {
        boolean buildIndex = false;
        if (maskFC.size() > 10) buildIndex = true;
        if (sourceFC.size() > 100) buildIndex = true;
        if (pred instanceof GeometryPredicate.DisjointPredicate) buildIndex = false;
        if (buildIndex) {
            queryFC = new IndexedFeatureCollection(sourceFC);
        } else {
            queryFC = sourceFC;
        }
    }

    private Iterator query(GeometryPredicate pred, double[] params, Geometry gMask) {
        Envelope queryEnv = gMask.getEnvelopeInternal();
        if (pred instanceof GeometryPredicate.WithinDistancePredicate) {
            queryEnv.expandBy(params[0]);
        }
        boolean useQuery = true;
        if (pred instanceof GeometryPredicate.DisjointPredicate) useQuery = false;
        Iterator queryIt = null;
        if (useQuery) {
            Collection queryResult = queryFC.query(queryEnv);
            queryIt = queryResult.iterator();
        } else {
            queryIt = queryFC.iterator();
        }
        return queryIt;
    }

    public boolean isExceptionThrown() {
        return isExceptionThrown;
    }

    public FeatureCollection getResultFC() {
        return new FeatureDataset(sourceFC.getFeatureSchema());
    }

    private boolean isInResult(Feature f) {
        return resultSet.contains(f);
    }

    /**
   * Computes geomSrc.func(geomMask)
   *
   * @param monitor
   * @param func
   * @param params
   * @param resultFC
   */
    public void execute(TaskMonitor monitor, GeometryPredicate func, double[] params, FeatureCollection resultFC) {
        createQueryFeatureCollection(func);
        int total = maskFC.size();
        int count = 0;
        for (Iterator iMask = maskFC.iterator(); iMask.hasNext(); ) {
            monitor.report(count++, total, "features");
            if (monitor.isCancelRequested()) return;
            Feature fMask = (Feature) iMask.next();
            Geometry gMask = fMask.getGeometry();
            Iterator queryIt = query(func, params, gMask);
            for (; queryIt.hasNext(); ) {
                Feature fSrc = (Feature) queryIt.next();
                if (isInResult(fSrc)) continue;
                Geometry gSrc = fSrc.getGeometry();
                geoms[0] = gSrc;
                geoms[1] = gMask;
                boolean isInResult = isTrue(func, gSrc, gMask, params);
                if (isInResult) {
                    if (allowDuplicatesInResult) {
                        addToResult(fSrc, resultFC);
                    } else {
                        resultSet.add(fSrc);
                    }
                }
            }
        }
        if (!allowDuplicatesInResult) {
            if (complementResult) {
                loadComplement(resultFC);
            } else {
                loadResult(resultFC);
            }
        }
    }

    private void loadComplement(FeatureCollection resultFC) {
        for (Iterator i = sourceFC.iterator(); i.hasNext(); ) {
            Feature f = (Feature) i.next();
            if (!resultSet.contains(f)) {
                addToResult(f, resultFC);
            }
        }
    }

    private void loadResult(FeatureCollection resultFC) {
        for (Iterator i = resultSet.iterator(); i.hasNext(); ) {
            Feature f = (Feature) i.next();
            addToResult(f, resultFC);
        }
    }

    private void addToResult(Feature f, FeatureCollection resultFC) {
        Feature fResult = f.clone(true);
        resultFC.add(fResult);
    }

    private boolean isTrue(GeometryPredicate func, Geometry geom0, Geometry geom1, double[] params) {
        try {
            return func.isTrue(geom0, geom1, params);
        } catch (RuntimeException ex) {
            isExceptionThrown = true;
        }
        return false;
    }
}
