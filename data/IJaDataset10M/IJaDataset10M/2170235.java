package uk.icat3.search.parameter.util;

import uk.icat3.exceptions.NoParametersException;
import java.util.HashMap;
import java.util.Map;
import uk.icat3.entity.Parameter;
import uk.icat3.exceptions.NoElementTypeException;
import uk.icat3.util.ElementType;
import uk.icat3.util.Queries;

/**
 * This class define all the parameter and condition extracted from a parameter
 * search structure.
 * 
 * @author cruzcruz
 */
public class ExtractedJPQL {

    /** Condition of the parameter search */
    protected StringBuffer condition;

    /** List of datafile parameter */
    protected Map<String, Parameter> datafileParameter;

    /** List of dataset parameter */
    protected Map<String, Parameter> datasetParameter;

    /** List of sample parameter */
    protected Map<String, Parameter> sampleParameter;

    /** List of JPQL parameters */
    protected Map<String, Object> jpqlParameter;

    /**
     * Constructor
     */
    public ExtractedJPQL() {
        condition = new StringBuffer();
        datafileParameter = new HashMap<String, Parameter>();
        datasetParameter = new HashMap<String, Parameter>();
        sampleParameter = new HashMap<String, Parameter>();
        jpqlParameter = new HashMap<String, Object>();
    }

    /**
     * Return JPQL statement relative to the JPQL parameter declaration
     *
     * @return JPQL parameters declaration
     * @throws NoParametersException
     */
    public String getParametersJPQL(ElementType type) throws NoElementTypeException, NoParametersException {
        if (type == ElementType.INVESTIGATION) return getInvestigationParametersJPQL(); else if (type == ElementType.DATAFILE) return getDatafileParameterJPQL(); else if (type == ElementType.DATASET) return getDatasetParameterJPQL(); else if (type == ElementType.SAMPLE) return getSampleParameterJPQL();
        throw new NoElementTypeException(type);
    }

    /**
     * Return JPQL string statment reference to the SampleParameter
     * 
     * @return JPQL string statement
     * @throws NoParametersException
     */
    private String getSampleParameterJPQL() throws NoParametersException {
        String ret = "";
        for (Map.Entry<String, Parameter> e : datafileParameter.entrySet()) ret += ", IN(" + Queries.DATAFILE_NAME + ".datafileParameterCollection) " + e.getKey();
        for (Map.Entry<String, Parameter> e : datasetParameter.entrySet()) ret += ", IN(" + Queries.DATASET_NAME + ".datasetParameterCollection) " + e.getKey();
        for (Map.Entry<String, Parameter> e : sampleParameter.entrySet()) ret += ", IN(" + Queries.PARAM_NAME_JPQL + ".sampleParameterCollection) " + e.getKey();
        if (ret.isEmpty()) throw new NoParametersException();
        String parameter = "";
        if (!datafileParameter.isEmpty()) parameter += ", IN(" + Queries.PARAM_NAME_JPQL + ".investigationId.datasetCollection) " + Queries.DATASET_NAME + ", IN(" + Queries.DATASET_NAME + ".datafileCollection) " + Queries.DATAFILE_NAME;
        if (datafileParameter.isEmpty() && !datasetParameter.isEmpty()) parameter += ", IN(" + Queries.PARAM_NAME_JPQL + ".investigationId.datasetCollection) " + Queries.DATASET_NAME;
        if (parameter.isEmpty()) return ret.substring(2);
        return parameter.substring(2) + ret;
    }

    /**
     * Return JPQL string statment reference to the DatasetParameter
     *
     * @return JPQL string statement
     * @throws NoParametersException
     */
    private String getDatasetParameterJPQL() throws NoParametersException {
        String ret = "";
        for (Map.Entry<String, Parameter> e : datafileParameter.entrySet()) ret += ", IN(" + Queries.DATAFILE_NAME + ".datafileParameterCollection) " + e.getKey();
        for (Map.Entry<String, Parameter> e : datasetParameter.entrySet()) ret += ", IN(" + Queries.PARAM_NAME_JPQL + ".datasetParameterCollection) " + e.getKey();
        for (Map.Entry<String, Parameter> e : sampleParameter.entrySet()) ret += ", IN(" + Queries.SAMPLE_NAME + ".sampleParameterCollection) " + e.getKey();
        if (ret.isEmpty()) throw new NoParametersException();
        String parameter = "";
        if (!datafileParameter.isEmpty()) parameter += ", IN(" + Queries.PARAM_NAME_JPQL + ".datafileCollection) " + Queries.DATAFILE_NAME;
        if (!sampleParameter.isEmpty()) parameter += ", IN(" + Queries.PARAM_NAME_JPQL + ".investigation.sampleCollection) " + Queries.SAMPLE_NAME;
        if (parameter.isEmpty()) return ret.substring(2);
        return parameter.substring(2) + ret;
    }

    /**
     * Return JPQL string statment reference to the DatafileParameter
     *
     * @return JPQL string statement
     * @throws NoParametersException
     */
    private String getDatafileParameterJPQL() throws NoParametersException {
        String ret = "";
        for (Map.Entry<String, Parameter> e : datafileParameter.entrySet()) ret += ", IN(" + Queries.PARAM_NAME_JPQL + ".datafileParameterCollection) " + e.getKey();
        for (Map.Entry<String, Parameter> e : datasetParameter.entrySet()) ret += ", IN(" + Queries.PARAM_NAME_JPQL + ".dataset.datasetParameterCollection) " + e.getKey();
        for (Map.Entry<String, Parameter> e : sampleParameter.entrySet()) ret += ", IN(" + Queries.SAMPLE_NAME + ".sampleParameterCollection) " + e.getKey();
        if (ret.isEmpty()) throw new NoParametersException();
        String parameter = "";
        if (!sampleParameter.isEmpty()) parameter += ", IN(" + Queries.PARAM_NAME_JPQL + ".dataset.investigation.sampleCollection) " + Queries.SAMPLE_NAME;
        if (parameter.isEmpty()) return ret.substring(2);
        return parameter.substring(2) + ret;
    }

    /**
     * Return JPQL string statment relative to a list of parameters for
     * investigation. The investigation includes all three types of parameter
     * (datafile, dataset and sample)
     * 
     * @return
     * @throws NoParametersException
     */
    private String getInvestigationParametersJPQL() throws NoParametersException {
        String ret = "";
        for (Map.Entry<String, Parameter> e : datafileParameter.entrySet()) ret += ", IN(" + Queries.DATAFILE_NAME + ".datafileParameterCollection) " + e.getKey();
        for (Map.Entry<String, Parameter> e : datasetParameter.entrySet()) ret += ", IN(" + Queries.DATASET_NAME + ".datasetParameterCollection) " + e.getKey();
        for (Map.Entry<String, Parameter> e : sampleParameter.entrySet()) ret += ", IN(" + Queries.SAMPLE_NAME + ".sampleParameterCollection) " + e.getKey();
        if (ret.isEmpty()) throw new NoParametersException();
        String parameter = "";
        if (!datafileParameter.isEmpty()) parameter += ", IN(" + Queries.PARAM_NAME_JPQL + ".datasetCollection) " + Queries.DATASET_NAME + ", IN(" + Queries.DATASET_NAME + ".datafileCollection) " + Queries.DATAFILE_NAME;
        if (datafileParameter.isEmpty() && !datasetParameter.isEmpty()) parameter += ", IN(" + Queries.PARAM_NAME_JPQL + ".datasetCollection) " + Queries.DATASET_NAME;
        if (!sampleParameter.isEmpty()) parameter += ", IN(" + Queries.PARAM_NAME_JPQL + ".sampleCollection) " + Queries.SAMPLE_NAME;
        return parameter.substring(2) + ret;
    }

    /**
     * Check if there are any condition
     * @return
     */
    public boolean isEmpty() {
        if (this.condition.length() == 0) return true;
        return false;
    }

    public String getCondition() {
        return condition.toString();
    }

    public Map<String, Parameter> getDatafileParameter() {
        return datafileParameter;
    }

    public Map<String, Object> getAllJPQLParameter() {
        HashMap<String, Object> ret = new HashMap<String, Object>();
        ret.putAll(datafileParameter);
        ret.putAll(datasetParameter);
        ret.putAll(sampleParameter);
        ret.putAll(jpqlParameter);
        return ret;
    }

    public Map<String, Parameter> getDatasetParameter() {
        return datasetParameter;
    }

    public Map<String, Parameter> getSampleParameter() {
        return sampleParameter;
    }
}
