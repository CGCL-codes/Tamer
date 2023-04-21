package net.sf.osadm.linedata.csv;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.sourceforge.argval.collection.CollectionUtil;

/**
 * Implementation of the interface {@link CsvConfig}.
 * 
 * @author T. Verhagen
 */
public class CsvConfigImpl implements CsvConfig {

    /** Logging instance of this class. */
    protected static transient Logger logger = LoggerFactory.getLogger(CsvConfigImpl.class);

    /** The csv separator character. */
    private String separator = null;

    private String nullValue = null;

    private List<String> headerList = null;

    private Boolean isHeaderActive = null;

    /**
	 * Base constructor which initializes with the default values.
	 *
	 * <p>
	 * The following default values are used:
	 * <table border="1" cellpadding="3" cellspacing="0">
 	 *   <tr bgcolor="#CCCCFF" CLASS="TableHeadingColor">
 	 *     <th>The configuration item</th><th>The default value</th>
 	 *   </tr>
 	 *   <tr align="center">
 	 *     <td>separator</td><td>comma</td>
 	 *   </tr>
 	 *   <tr align="center">
 	 *     <td>nullValue</td><td>empty string</td>
 	 *   </tr>
 	 *   <tr align="center">
 	 *     <td>isHeaderActive</td><td>false</td>
 	 *   </tr>
 	 * </table>
	 * </p>
	 * 
	 */
    public CsvConfigImpl() {
        super();
        separator = ",";
        nullValue = "";
        isHeaderActive = false;
    }

    /**
	 *  
	 * @param  separator  The separator character.
	 * @param  headerList  The headers.
	 * @param  isHeaderActive  The header is active value, for when header are 
	 *                         expected or not.
	 */
    public CsvConfigImpl(String separator, List<String> headerList, Boolean isHeaderActive) {
        this();
        this.separator = separator;
        this.headerList = headerList;
        this.isHeaderActive = isHeaderActive;
    }

    /**
	 * Simple constructor, when only the separator and isHeaderActive are known.
	 * 
	 * @param  separator  The separator character.
	 * @param  isHeaderActive  The header is active value, for when header are 
	 *                         expected or not.
	 */
    public CsvConfigImpl(String separator, Boolean isHeaderActive) {
        this(separator, null, isHeaderActive);
    }

    /** {@inheritDoc} */
    public String getNullValue() {
        return nullValue;
    }

    /** {@inheritDoc} */
    public String getSeparator() {
        return separator;
    }

    /** {@inheritDoc} */
    public Boolean isHeaderActive() {
        return isHeaderActive;
    }

    /** {@inheritDoc} */
    public List<String> getHeaderList() {
        return headerList;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "isHeaderActive '" + isHeaderActive + "'  headers  " + CollectionUtil.toString(headerList) + "  seperator '" + separator + "'  nullValue '" + nullValue + "'";
    }
}
