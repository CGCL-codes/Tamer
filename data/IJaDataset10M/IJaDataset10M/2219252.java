package bioevent.datapreparation.attributecalculator;

import java.io.IOException;
import bioevent.core.Artifact;

/**
 * Interface for artifact attribute calculators
 */
public interface IArtifactAttributeCalculator {

    /**
	 * 
	 * artifactToProcess can be Document, Sentence or Token Return value is
	 * string or double
	 * 
	 * @throws IOException
	 * @throws Exception 
	 */
    public int addAttributeValues(Artifact artifactToProcess) throws IOException, Exception;
}
