package bioevent.core;

import java.io.IOException;
import java.sql.SQLException;

/**
 * common interface for all attribute calculators
 */
public interface ICoreferenceAttributeCalculator {

    /**
	 * @throws SQLException 
	 * 
	 * artifactToProcess can be Document, Sentence or Token Return value is
	 * string or double
	 * 
	 * @throws IOException 
	 */
    public int addAttributeValues(CorefExample exampleToProcess) throws IOException, SQLException;
}
