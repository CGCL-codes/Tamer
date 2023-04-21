package ch.sahits.codegen.sql.generator;

import java.io.InputStream;
import java.sql.SQLException;

/**
 * This interface defines the method that are needed
 * for generating of a insert script
 * @author Andi Hotz
 */
public interface ISQLInsertScriptGenerator {

    /**
	 * Generate the insert script as a string
	 * @return insert statements
	 * @throws SQLException 
	 */
    public String getInsertScriptAsString() throws SQLException;

    /**
	 * Retrieve the insert script contents as an input stream to be read from
	 * @return Input stream
	 * @throws SQLException 
	 */
    public InputStream getInsertScriptAsInputStream() throws SQLException;
}
