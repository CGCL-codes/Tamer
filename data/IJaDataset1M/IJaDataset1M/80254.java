package org.geogurus.data.operations;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geogurus.data.OperationAdapter;
import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.FeatureStore;
import org.geotools.data.Transaction;
import org.geotools.feature.FeatureCollection;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * Writes each feature to the FeatureStore
 * 
 * @author jesse
 */
public class ToFeatureStoreOp extends OperationAdapter<SimpleFeature, FeatureStore<SimpleFeatureType, SimpleFeature>> {

    @Override
    public void start(FeatureStore<SimpleFeatureType, SimpleFeature> context) {
        Transaction transaction = new DefaultTransaction(getClass().getSimpleName());
        context.setTransaction(transaction);
    }

    public boolean operate(SimpleFeature operatee, FeatureStore<SimpleFeatureType, SimpleFeature> context) {
        FeatureCollection<SimpleFeatureType, SimpleFeature> collectionOfOne = DataUtilities.collection(operatee);
        try {
            context.addFeatures(collectionOfOne);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public void end(FeatureStore<SimpleFeatureType, SimpleFeature> context, boolean finished) {
        try {
            context.getTransaction().commit();
        } catch (IOException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "error committing transaction", e);
        }
    }
}
