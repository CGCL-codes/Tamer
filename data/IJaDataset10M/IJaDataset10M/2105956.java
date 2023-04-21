package xbird.xquery.type.xs;

import xbird.util.xml.XMLUtils;
import xbird.xqj.XQJConstants;

/**
 * 
 * <DIV lang="en"></DIV>
 * <DIV lang="ja"></DIV>
 * 
 * @author Makoto YUI (yuin405+xbird@gmail.com)
 */
public class NCNameType extends NameType {

    private static final long serialVersionUID = -7280608993971169321L;

    public static final String SYMBOL = "xs:NCName";

    public static final NCNameType NCNAME = new NCNameType();

    public NCNameType() {
        super(SYMBOL);
    }

    protected NCNameType(String type) {
        super(type);
    }

    public boolean isValid(String literal) {
        return XMLUtils.isNCName(literal);
    }

    @Override
    public int getXQJBaseType() {
        return XQJConstants.XQBASETYPE_NCNAME;
    }
}
