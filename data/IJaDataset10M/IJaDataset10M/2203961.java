package com.rapidminer.operator.io;

import java.io.File;
import java.sql.ResultSet;
import java.util.Iterator;
import java.util.List;
import org.kobjects.jdbc.TableManager;
import org.kobjects.jdbc.util.AbstractResultSet;
import com.rapidminer.example.Attribute;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.parameter.ParameterType;
import com.rapidminer.parameter.ParameterTypeFile;
import com.rapidminer.parameter.UndefinedParameterError;

/**
 * This class can read arff, comma separated values (csv), dbase and bibtex
 * files. It uses Stefan Haustein's kdb tools.
 * 
 * @author Simon Fischer, Ingo Mierswa
 *          Exp $
 */
public abstract class KDBExampleSource extends ResultSetExampleSource {

    /** The parameter name for &quot;The file containing the data&quot; */
    public static final String PARAMETER_DATA_FILE = "data_file";

    public abstract String getFormat();

    public abstract String getExtension();

    public KDBExampleSource(OperatorDescription description) {
        super(description);
    }

    /** Does nothing. */
    @Override
    public void tearDown() {
    }

    @Override
    public ResultSet getResultSet() throws UndefinedParameterError {
        File dataFile = getParameterAsFile(PARAMETER_DATA_FILE);
        String dataFileAbsolutePath = dataFile.getAbsolutePath();
        return TableManager.getResultSet(getFormat() + ":" + dataFileAbsolutePath, TableManager.READ);
    }

    @Override
    public void setNominalValues(List attributeList, ResultSet resultSet, Attribute label) throws OperatorException {
        if (resultSet instanceof AbstractResultSet) {
            AbstractResultSet ars = (AbstractResultSet) resultSet;
            Iterator i = attributeList.iterator();
            int j = 0;
            while (i.hasNext()) {
                j++;
                Attribute attribute = (Attribute) i.next();
                Object[] values = ars.getColumnSet().getColumn(j).getValues();
                if (attribute.isNominal()) {
                    if (values == null) {
                        logWarning("Information about class values is null!");
                    } else {
                        for (int k = 0; k < values.length; k++) {
                            attribute.getMapping().mapString(values[k].toString());
                        }
                    }
                }
            }
        } else {
            logWarning("Result set does not provide information about class values!");
        }
    }

    @Override
    public List<ParameterType> getParameterTypes() {
        List<ParameterType> types = super.getParameterTypes();
        types.add(new ParameterTypeFile(PARAMETER_DATA_FILE, "The file containing the data", getExtension(), false));
        return types;
    }
}
