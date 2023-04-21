package xbird.xquery.type.xs;

import xbird.xqj.XQJConstants;

/**
 * 
 * <DIV lang="en"></DIV>
 * <DIV lang="ja"></DIV>
 * 
 * @author Makoto YUI (yuin405+xbird@gmail.com)
 */
public final class ENTITYType extends NCNameType {

    private static final long serialVersionUID = 2770545133796869556L;

    public static final String SYMBOL = "xs:ENTITY";

    public static final ENTITYType ENTITY = new ENTITYType();

    public ENTITYType() {
        super(SYMBOL);
    }

    @Override
    public int getXQJBaseType() {
        return XQJConstants.XQBASETYPE_ENTITY;
    }
}
