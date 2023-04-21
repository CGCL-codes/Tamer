package net.mlw.vlh.adapter.jdbc.util.setter;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;

/** Consumes a String[] and sets multiple Integer(s) on the Statement. 
 * 
 * @author Matthew L. Wilson
 * @version $Revision: 1.2 $ $Date: 2005/12/19 10:58:01 $
 */
public class LongArraySetter extends AbstractArraySetter {

    /**
	 * @see net.mlw.vlh.adapter.jdbc.util.Setter#set(java.sql.PreparedStatement, int, java.lang.Object)
	 */
    public int set(PreparedStatement query, int index, Object value) throws SQLException, ParseException {
        if (isUseBindVarables()) {
            String[] values = (value instanceof String[]) ? (String[]) value : new String[] { (String) value };
            for (int i = 0, length = values.length; i < length; i++) {
                query.setLong(index++, Long.parseLong(values[i]));
            }
        }
        return index;
    }
}
