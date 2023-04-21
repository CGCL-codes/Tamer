package com.rapidminer.operator.io;

import com.rapidminer.operator.Operator;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.nio.CSVExampleSource;
import com.rapidminer.operator.nio.model.CSVResultSetConfiguration;
import com.rapidminer.operator.nio.model.DataResultSet;

/**
 * A class holding information about syntactical configuration for parsing
 * CSV files
 * 
 * @author Simon Fischer
 */
public class CSVbatchResultSetConfiguration extends CSVResultSetConfiguration {

    public CSVbatchResultSetConfiguration(CSVExampleSource c) throws OperatorException {
        super(c);
    }

    @Override
    public DataResultSet makeDataResultSet(Operator operator) throws OperatorException {
        return new CSVbatchResultSet(this, operator);
    }
}
