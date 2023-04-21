package blueprint4j.db;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.sql.*;
import java.io.*;
import blueprint4j.utils.*;
import javax.swing.ImageIcon;

public class FieldImageResource extends FieldString {

    /**
	 * public Field( Blob p_name, Blob p_description, boolean p_key_field
	 *
	 * Name: Name of field -> equivalent to table.field_name
	 * Description: Description of field
	 * key_field: Is this field a key field in the table (index field)
	 */
    public FieldImageResource(String p_name, int p_field_type, String p_description, Entity entity) {
        super(p_name, p_field_type, p_description, 250, entity);
    }

    public Field newInstance(Entity entity) {
        return new FieldImageResource(getName(), getFieldType(), getDescription(), entity);
    }
}
