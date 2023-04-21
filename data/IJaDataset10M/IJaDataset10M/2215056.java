package adqlParser;

import saadadb.meta.AttributeHandler;
import adqlParser.parser.ParseException;
import adqlParser.query.ADQLColumn;

public class ADQLColumnAndMeta extends ADQLColumn {

    protected AttributeHandler meta;

    public ADQLColumnAndMeta(String columnName, String prefix) throws ParseException {
        super(columnName, prefix);
        meta = null;
    }

    public ADQLColumnAndMeta(String columnName) throws ParseException {
        super(columnName);
        meta = null;
    }

    /**
	 * @return the meta
	 */
    public AttributeHandler getMeta() {
        return meta;
    }

    /**
	 * @param meta the meta to set
	 */
    public void setMeta(AttributeHandler meta) {
        this.meta = meta;
    }
}
