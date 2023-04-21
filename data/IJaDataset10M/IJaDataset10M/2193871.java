package com.angel.io.processors.commands.imports;

import com.angel.io.exceptions.InvalidRowDataException;
import com.angel.io.parameters.ParametersService;
import com.angel.io.type.rows.ImportRowFile;
import com.angel.io.type.rows.impl.HeaderRowFile;

/**
 * @author William
 *
 */
public interface ImportRowProcessorCommand {

    public void checkRowData(ImportRowFile rowFile, HeaderRowFile header) throws InvalidRowDataException;

    public <T> T processRow(ImportRowFile rowFile, HeaderRowFile headerRowFile, ParametersService parametersServices);

    public String[] getHeaderColumnsNames();
}
