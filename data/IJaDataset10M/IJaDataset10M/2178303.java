package edu.ucdavis.genomics.metabolomics.binbase.bci.io;

import java.util.Map;
import edu.ucdavis.genomics.metabolomics.exception.ConfigurationException;
import edu.ucdavis.genomics.metabolomics.util.io.dest.Destination;
import edu.ucdavis.genomics.metabolomics.util.io.dest.DestinationFactory;

public class SopDestinationFactory extends DestinationFactory {

    @Override
    public Destination createDestination(Object identifier, Map<?, ?> propertys) throws ConfigurationException {
        return new SopDestination(identifier.toString());
    }
}
