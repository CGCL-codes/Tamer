package sisi;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.zkoss.zk.ui.Component;
import org.zkoss.zkplus.databind.TypeConverter;

/**
 * @author Robert Pichelhofer
 * 
 * Databindingconverter Direction: Output only
 * 
 * Source: Date, Timestamp Destination; Label, Listcell
 * 
 * Dateformat is retrieved from the user locals = Browsersetting
 * 
 * Examples: <listcell label="@{person.birthdate,
 * converter:'org.zkforge.converters.DateUser'}"/> <label
 * value="@{person.birthdate, converter:'org.zkforge.converters.DateUser'}"/>
 * 
 * 
 * TODO: check: it is possible to store the usersettings (interface docs says:
 * should not hold any states since is shared by the whole application)
 */
public class DateUser implements TypeConverter {

    public Object coerceToBean(Object arg0, Component arg1) {
        return null;
    }

    public Object coerceToUi(Object arg0, Component arg1) {
        if (arg0 == null) {
            return null;
        }
        if (arg0 instanceof String) {
            return (String) arg0;
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat(getDefaultFormat());
            Date date = (Date) arg0;
            return sdf.format(date);
        }
    }

    /**
	 * Returns the default format, which is used when outputing the date
	 * <p>
	 * The default format ({@link #getFormat}) depends on JVM's setting and
	 * the current user's locale. That is,
	 * <code>DateFormat.getDateInstance(DateFormat,DEFAULT, Locales.getCurrent).</code>
	 * 
	 * <p>
	 * You might override this method to provide your own default format.
	 */
    protected String getDefaultFormat() {
        return "dd/MM/yyyy";
    }
}
