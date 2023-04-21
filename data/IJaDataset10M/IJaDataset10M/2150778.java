package uk.org.ogsadai.client.toolkit.document;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import uk.org.ogsadai.client.toolkit.exception.ClientServerCompatibilityException;
import uk.org.ogsadai.client.toolkit.exception.DataValueParseException;
import uk.org.ogsadai.client.toolkit.exception.DataValueUnknownTypeException;
import uk.org.ogsadai.common.Base64;
import uk.org.ogsadai.data.BinaryData;
import uk.org.ogsadai.data.BooleanData;
import uk.org.ogsadai.data.CharData;
import uk.org.ogsadai.data.DataValue;
import uk.org.ogsadai.data.DateData;
import uk.org.ogsadai.data.DoubleData;
import uk.org.ogsadai.data.FloatData;
import uk.org.ogsadai.data.IntegerData;
import uk.org.ogsadai.data.ListBegin;
import uk.org.ogsadai.data.ListEnd;
import uk.org.ogsadai.data.LongData;
import uk.org.ogsadai.data.StringData;
import uk.org.ogsadai.resource.property.DatePropertyConvertor;

/**
 * Mapper for document-based request values. Maps from
 * <code>String</code> values to
 * <code>uk.org.ogsadai.data.DataValue</code> values. Type is
 * specified by XML request element name (one of <code>binary, string,
 * charArray, long, int, float, double, date, boolean, listBegin,
 * listEnd</code>.
 *
 * @author The OGSA-DAI Project Team.
 */
public class DataValueFactory {

    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh, 2007-2008.";

    /** 
     * Map from XML element name (<code>String</code> to
     * <code>DataConverter</code> inner-class objects. 
     */
    private final Map mDataMapper = new HashMap();

    /**
     * Constructor - populates mapper from XML element names
     * to data convertors.
     */
    public DataValueFactory() {
        mDataMapper.put("binary", new DataConverter() {

            public DataValue convert(String value) {
                return new BinaryData(Base64.decode(value));
            }
        });
        mDataMapper.put("string", new DataConverter() {

            public DataValue convert(String value) {
                return new StringData(value);
            }
        });
        mDataMapper.put("charArray", new DataConverter() {

            public DataValue convert(String value) {
                return new CharData(value.toCharArray());
            }
        });
        mDataMapper.put("long", new DataConverter() {

            public DataValue convert(String value) {
                return new LongData(Long.parseLong(value));
            }
        });
        mDataMapper.put("int", new DataConverter() {

            public DataValue convert(String value) {
                return new IntegerData(Integer.parseInt(value));
            }
        });
        mDataMapper.put("float", new DataConverter() {

            public DataValue convert(String value) {
                return new FloatData(Float.parseFloat(value));
            }
        });
        mDataMapper.put("double", new DataConverter() {

            public DataValue convert(String value) {
                return new DoubleData(Double.parseDouble(value));
            }
        });
        mDataMapper.put("date", new DataConverter() {

            public DataValue convert(String value) throws ParseException {
                return new DateData(DatePropertyConvertor.parseXSDDate(value));
            }
        });
        mDataMapper.put("boolean", new DataConverter() {

            public DataValue convert(String value) {
                return new BooleanData(Boolean.valueOf(value).booleanValue());
            }
        });
        mDataMapper.put("listBegin", new DataConverter() {

            public DataValue convert(String value) {
                return ListBegin.VALUE;
            }
        });
        mDataMapper.put("listEnd", new DataConverter() {

            public DataValue convert(String value) {
                return ListEnd.VALUE;
            }
        });
    }

    /**
     * Converts a data value from <code>String</code> to
     * <code>uk.org.ogsadai.data.DataValue</code> values.
     * 
     * @param type
     *     Type of data value. One of <code>binary, string, charArray,
     *     long, int, float, double, date, boolean, listBegin,
     *     listEnd</code>. 
     * @param value
     *     Value.
     * @return <code>DataValue</code>.
     * @throws ClientServerCompatibilityException
     *     If the server has provided unexpected data values or types.
     */
    public DataValue getDataValue(String type, String value) throws ClientServerCompatibilityException {
        DataConverter converter = (DataConverter) mDataMapper.get(type);
        if (converter != null) {
            try {
                return converter.convert(value);
            } catch (NumberFormatException nfe) {
                throw new DataValueParseException(type, value, nfe);
            } catch (ParseException pe) {
                throw new DataValueParseException(type, value, pe);
            }
        } else {
            throw new DataValueUnknownTypeException(type);
        }
    }

    /**
     * Converts data into a DataValue object.
     *
     * @author The OGSA-DAI Project Team.
     */
    private interface DataConverter {

        /**
         * Converts the given data string into a DataValue object.
         * 
         * @param value
         *     Data value.
         * @return the data value as an object
         * @throws NumberFormatException
         *     If there is an error parsing a numerical data value.
         * @throws ParseException
         *     If there is an error parsing a non-numerical data value.
         */
        public DataValue convert(String value) throws NumberFormatException, ParseException;
    }
}
