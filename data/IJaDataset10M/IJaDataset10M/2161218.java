package sqcon.sql;

import sqcon.util.StringParsingException;
import sqcon.util.StringTokenParser;
import java.util.NoSuchElementException;

public class Attribute extends Aliasable implements SQLComparable {

    private Table table;

    private String attributeName;

    /**
	 * Constructor, if alias is not specified, then the alias is it self
	 * @param attributeName
	 */
    public Attribute(String attributeName) {
        super(attributeName);
        this.attributeName = attributeName;
        this.table = null;
    }

    public Attribute(String attributeName, String alias) {
        super(alias);
        this.attributeName = attributeName;
        this.table = null;
    }

    public Attribute(Table table, String attributeName, String alias) {
        super(alias);
        this.table = table;
        this.attributeName = attributeName;
    }

    public Attribute(Table table, String attributeName) {
        super(attributeName);
        this.table = table;
        this.attributeName = attributeName;
    }

    public Table getTable() {
        return table;
    }

    public String getAttributeName() {
        return attributeName;
    }

    @Override
    public String printString() {
        return this.toString();
    }

    @Override
    public boolean isQuery() {
        return false;
    }

    public String toString() {
        String attrLong = attributeName;
        if (!this.getAlias().equals(attributeName)) {
            attrLong = attrLong + " AS " + this.getAlias();
        }
        if (table == null) {
            return attrLong;
        } else {
            return table.getAlias() + "." + attrLong;
        }
    }

    public static Attribute generateAttribute(String attrStr) throws ConstructSqlException {
        StringTokenParser stp = new StringTokenParser(attrStr);
        try {
            String attrName = stp.nextToken();
            String tableName = null;
            if (attrName.indexOf('.') >= 0) {
                StringTokenParser attrStp = new StringTokenParser(attrName, ".");
                tableName = attrStp.nextToken();
                attrName = attrStp.nextToken();
                if (attrStp.hasMoreTokens()) {
                    throw new ConstructSqlException("Error parsing Attribute");
                }
            }
            String alias = stp.nextToken();
            if (alias.equalsIgnoreCase("AS")) {
                alias = stp.nextToken();
            }
            if (stp.hasMoreTokens()) {
                throw new ConstructSqlException("Error parsing Attribute");
            }
            return new Attribute(new Table(tableName), attrName, alias);
        } catch (StringParsingException e) {
            throw new ConstructSqlException("Error parsing Attribute");
        } catch (NoSuchElementException e) {
            throw new ConstructSqlException("Error parsing Attribute");
        }
    }
}
