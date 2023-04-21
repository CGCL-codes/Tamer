package org.ensembl.driver;

import java.util.List;
import org.ensembl.datamodel.Location;
import org.ensembl.datamodel.RepeatFeature;

/**
 * Provides access to RepeatFeatures in the datasource.
 */
public interface RepeatFeatureAdaptor extends FeatureAdaptor {

    RepeatFeature fetch(long internalID) throws AdaptorException;

    /**
     * Warning: some repeats are stored unstranded (strand=0) which can produce unexpected results.
     * If location.strand = +1 or -1 then repeats where repeat.location.strand=0 will be 
     * omitted from the result. The 
     * solution is to set location.strand=0 and then filter out the those on the strand
     * you are not interested in.
     */
    List fetch(Location location) throws AdaptorException;

    long store(RepeatFeature repeat) throws AdaptorException;

    void delete(long internalID) throws AdaptorException;

    void delete(RepeatFeature repeat) throws AdaptorException;

    /** 
     * Name of the default RepeatFeatureAdaptor available from a driver. 
     */
    static final String TYPE = "repeat_feature";
}
