package er.extensions;

import com.webobjects.foundation.*;
import com.webobjects.eocontrol.*;
import com.webobjects.eoaccess.*;
import com.webobjects.appserver.*;
import java.util.*;
import org.apache.log4j.Category;

public class ERXPrimaryKeyListQualifier extends EOKeyValueQualifier {

    public static final Category cat = Category.getInstance(ERXPrimaryKeyListQualifier.class);

    public ERXPrimaryKeyListQualifier(NSArray eos) {
        super("id", EOQualifier.QualifierOperatorEqual, ERXEOToManyQualifier.primaryKeysForObjectsFromSameEntity(eos));
    }

    public ERXPrimaryKeyListQualifier(String key, NSArray eos) {
        super(key, EOQualifier.QualifierOperatorEqual, ERXEOToManyQualifier.primaryKeysForObjectsFromSameEntity(eos));
    }

    public ERXPrimaryKeyListQualifier(String key, String foreignKey, NSArray eos) {
        super(key, EOQualifier.QualifierOperatorEqual, ERXEOToManyQualifier.primaryKeysForObjectsFromSameEntity(foreignKey, eos));
    }

    public ERXPrimaryKeyListQualifier(String k, NSSelector s, Object v) {
        super(k, s, v);
    }

    public String sqlStringForSQLExpression(EOSQLExpression e) {
        StringBuffer sb = new StringBuffer();
        sb.append(e.sqlStringForAttributeNamed(key()));
        sb.append(" IN  ");
        sb.append(value());
        return sb.toString();
    }

    public String description() {
        return " <primaryKey> IN '" + value() + "'";
    }

    public String toString() {
        return description();
    }

    public Object clone() {
        return new ERXPrimaryKeyListQualifier(key(), selector(), value());
    }
}
