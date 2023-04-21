package dr.evolution.io;

import dr.evolution.alignment.Alignment;
import dr.evolution.sequence.SequenceList;
import java.io.IOException;

/**
 * Interface for importers that do sequences
 *
 * @version $Id: SequenceImporter.java,v 1.3 2005/05/24 20:25:56 rambaut Exp $
 *
 * @author Andrew Rambaut
 * @author Alexei Drummond
 */
public interface SequenceImporter {

    /**
	 * importAlignment. 
	 */
    Alignment importAlignment() throws IOException, Importer.ImportException;

    /**
	 * importSequences. 
	 */
    SequenceList importSequences() throws IOException, Importer.ImportException;
}
