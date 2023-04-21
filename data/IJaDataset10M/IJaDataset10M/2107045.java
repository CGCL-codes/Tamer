package net.sourceforge.binml.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import net.sourceforge.binml.tree.DataTree;

/**
 * 
 * @author Fredrik Hederstierna
 * @version $Id: $
 * 
 * Created: 2006
 * 
 * Copyright 2006 Purple Scout AB, Malmoe, Sweden
 * 
 * Class that describes a factory design pattern for creating a DataTree
 */
public class DataTreeFactory {

    /**
	 * Factory function that creates a data tree.
	 * 
	 * @param dataParser
	 *            Data parser that creates tree
	 * @param binaryFilename
	 *            Filename of binary file
	 * @return The created data tree
	 */
    public static DataTree createDataTree(DataParser dataParser, String binaryFilename, boolean littleEndian) throws BinaryParseException {
        DataTree dataTree = null;
        if (dataParser != null && binaryFilename != null) {
            File binFile = new File(binaryFilename);
            DataSource dataSource = null;
            try {
                dataSource = (DataSource) new BinaryDataSource(binFile);
            } catch (FileNotFoundException fnfex) {
                throw new BinaryParseException("File: " + binaryFilename + " not found");
            }
            try {
                dataTree = dataParser.createDataTree(dataSource, littleEndian);
            } catch (OutOfMemoryError uome) {
                throw new BinaryParseException("Out of Memory, the reason for this may be (but can have other reasons also): wrong endian for this file, error in the descriptor of the fileformat or damage on the file.");
            } catch (Exception ex) {
                throw new BinaryParseException("Error while parsing the binary file: " + ex.getMessage());
            }
            try {
                dataSource.close();
            } catch (IOException e) {
                throw new BinaryParseException("IO operation on " + binaryFilename + " failed: " + e.getMessage());
            }
        } else {
            BinaryParseException newexception = null;
            if (dataParser == null) {
                newexception = new BinaryParseException("ERROR dataParser was null");
            }
            if (binaryFilename == null) {
                newexception = new BinaryParseException("ERROR binary filename was null");
            }
            throw newexception;
        }
        return dataTree;
    }
}
