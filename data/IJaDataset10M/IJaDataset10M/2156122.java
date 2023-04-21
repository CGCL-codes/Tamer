package DataProcessing;

import DataProcessing.Exceptions.ComparisonFailedException;
import DataProcessing.Exceptions.OutputReadException;
import DataProcessing.Exceptions.OutputTestReadException;
import java.io.BufferedReader;

/**
 *
 * @author partizanka
 */
public abstract class OutputDataProcessor extends DataProcessor {

    /**
     *
     * @param outputReader
     * @param testOutputReader
     * @throws OutputTestReadException
     * @throws OutputReadException
     * @throws ComparisonFailedException
     */
    public abstract void process(BufferedReader outputReader, BufferedReader testOutputReader) throws OutputTestReadException, OutputReadException, ComparisonFailedException;
}
