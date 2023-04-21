package net.openchrom.chromatogram.alignment.converter.retentionindices;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import net.openchrom.chromatogram.alignment.converter.exceptions.FileIsNotWriteableException;
import net.openchrom.chromatogram.alignment.model.core.IRetentionIndices;

public interface IRetentionIndicesExportConverter {

    /**
	 * This method returns the file of the written retention indices.
	 * 
	 * @param file
	 * @param retentionIndices
	 * @return File
	 * @throws FileNotFoundException
	 * @throws FileIsNotWriteableException
	 * @throws IOException
	 */
    File convert(File file, IRetentionIndices retentionIndices) throws FileNotFoundException, FileIsNotWriteableException, IOException;

    /**
	 * This class checks the file attributes and throws an exception if
	 * something is wrong.
	 * 
	 * @param file
	 * @throws FileNotFoundException
	 * @throws FileIsNotWriteableException
	 * @throws IOException
	 */
    void validate(File file) throws FileNotFoundException, FileIsNotWriteableException, IOException;
}
