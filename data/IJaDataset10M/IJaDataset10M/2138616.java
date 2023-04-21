package uk.ac.ebi.pride.chart.model.implementation;

import java.util.Map;

/**
 * <p>
 * Contains the basic information for iterating over it with the sliding window
 * developed in PeakListManager class
 * </p>
 *
 * @author Antonio Fabregat
 * Date: 15-sep-2010
 * Time: 15:54:42
 */
public class PeakListBasicInfo {

    /**
     * Contains an association of what is the related spectrum to each binary array id
     * Map<binary_array_id, spectrum_id>
     */
    private Map<Integer, Integer> binaryArrayIdsSpectrum;

    /**
     * Contains the byte order is used when reading or writing multibyte values
     * stored as mzData element .../chartData/endian.  Only possible values are defined by the
     * static String members of this class 'BIG_ENDIAN_LABEL' (or "big") and 'LITTLE_ENDIAN_LABEL' (or "little").
     */
    private String dataEndian;

    /**
     * Contains the precision of the binary array (mzData element .../chartData/precision) that indicates
     * if the array contains encoded double values or encoded float values.
     * Only possible values for this parameter are defined byt he static String members of
     * this class 'FLOAT_PRECISION' (or "32") and 'DOUBLE_PRECISION' (or "64").
     */
    private String dataPrecision;

    /**
     * <p> Creates an instance of this PeakListBasicInfo object using an existing database connection object</p>
     *
     * @param list association of what is the related spectrum to each binary array id Map<binary_array_id, spectrum_id>
     * @param dataEndian the byte order is used when reading or writing multibyte values
     *                   stored as mzData element .../chartData/endian.  Only possible values are defined by the
     *                   static String members of this class 'BIG_ENDIAN_LABEL' (or "big") and 'LITTLE_ENDIAN_LABEL' (or "little").
     * @param dataPrecision the precision of the binary array (mzData element .../chartData/precision) that indicates
     *                      if the array contains encoded double values or encoded float values.
     *                      Only possible values for this parameter are defined byt he static String members of
     *                      this class 'FLOAT_PRECISION' (or "32") and 'DOUBLE_PRECISION' (or "64").
     */
    public PeakListBasicInfo(Map<Integer, Integer> list, String dataEndian, String dataPrecision) {
        this.binaryArrayIdsSpectrum = list;
        this.dataEndian = dataEndian;
        this.dataPrecision = dataPrecision;
    }

    /**
     * Returns the associations between binary array IDs and the spectrum IDs
     *
     * @return the associations between binary array IDs and the spectrum IDs
     */
    public Map<Integer, Integer> getList() {
        return binaryArrayIdsSpectrum;
    }

    /**
     * Returs the chartData endian value
     *
     * @return the chartData endian value
     */
    public String getDataEndian() {
        return dataEndian;
    }

    /**
     * Returns the chartData precision value
     *
     * @return the chartData precision value
     */
    public String getDataPrecision() {
        return dataPrecision;
    }
}
