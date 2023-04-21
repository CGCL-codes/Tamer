package com.iver.cit.gvsig.geoprocess.core.fmap;

import com.iver.cit.gvsig.exceptions.visitors.StartVisitorException;
import com.iver.cit.gvsig.exceptions.visitors.StopVisitorException;
import com.iver.cit.gvsig.exceptions.visitors.VisitorException;
import com.iver.cit.gvsig.fmap.core.IRow;
import com.iver.cit.gvsig.fmap.edition.DefaultRowEdited;
import com.iver.cit.gvsig.fmap.edition.IRowEdited;
import com.iver.cit.gvsig.fmap.edition.IWriter;

/**
 * It saves in a persistent data store Features, by using a IWriter.
 * It does preprocess and postprocess in start() and stop() methods.
 * @author azabala
 *
 */
public class FeaturePersisterProcessor2 implements FeatureProcessor {

    IWriter writer;

    int numFeatures;

    public FeaturePersisterProcessor2(IWriter writer) {
        this.writer = writer;
    }

    public void processFeature(IRow feature) throws VisitorException {
        DefaultRowEdited editedFeature = new DefaultRowEdited(feature, IRowEdited.STATUS_ADDED, numFeatures++);
        writer.process(editedFeature);
    }

    public void start() throws StartVisitorException {
        this.writer.preProcess();
    }

    public void finish() throws StopVisitorException {
        writer.postProcess();
    }
}
