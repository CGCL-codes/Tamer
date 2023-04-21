package uk.icat3.exceptions;

import uk.icat3.search.parameter.ParameterComparisonCondition;
import uk.icat3.exceptions.ParameterSearchException;

/**
 *
 * @author cruzcruz
 */
public class NoParameterTypeException extends ParameterSearchException {

    private static final String msg = "No parameter type (datasetParameter, datafileParameter or sampleParameter) was defined";

    public NoParameterTypeException(String msg) {
        super(NoParameterTypeException.msg + ": " + msg);
    }

    public NoParameterTypeException(ParameterComparisonCondition comp) {
        super(NoParameterTypeException.msg + ": Error in comparator '" + comp.getParameterSearch().getParam().getParameterPK().getName() + " (" + comp.getParameterSearch().getParam().getParameterPK().getUnits() + ")'");
    }
}
