package gov.nist.javax.sip.header.ims;

import java.text.ParseException;
import javax.sip.header.ExtensionHeader;

/**
 * 
 * @author aayush.bhatnagar
 * Rancore Technologies Pvt Ltd, Mumbai India.
 *
 */
public class PUserDatabase extends gov.nist.javax.sip.header.ParametersHeader implements PUserDatabaseHeader, SIPHeaderNamesIms, ExtensionHeader {

    private String databaseName;

    /**
	 * 
	 * @param databaseName
	 */
    public PUserDatabase(String databaseName) {
        super(NAME);
        this.databaseName = databaseName;
    }

    /**
	 * Default constructor.
	 */
    public PUserDatabase() {
        super(P_USER_DATABASE);
    }

    public String getDatabaseName() {
        return this.databaseName;
    }

    public void setDatabaseName(String databaseName) {
        if ((databaseName == null) || (databaseName.equals(" "))) throw new NullPointerException("Database name is null"); else if (!databaseName.contains("aaa://")) this.databaseName = new StringBuffer().append("aaa://").append(databaseName).toString(); else this.databaseName = databaseName;
    }

    protected String encodeBody() {
        StringBuffer retval = new StringBuffer();
        retval.append("<");
        if (getDatabaseName() != null) retval.append(getDatabaseName());
        if (!parameters.isEmpty()) retval.append(SEMICOLON + this.parameters.encode());
        retval.append(">");
        return retval.toString();
    }

    public boolean equals(Object other) {
        return (other instanceof PUserDatabaseHeader) && super.equals(other);
    }

    public Object clone() {
        PUserDatabase retval = (PUserDatabase) super.clone();
        return retval;
    }

    public void setValue(String value) throws ParseException {
        throw new ParseException(value, 0);
    }
}
