package xbird.xquery.type.xs;

import xbird.xqj.XQJConstants;
import xbird.xquery.XQueryException;
import xbird.xquery.dm.value.xsi.QNameValue;
import xbird.xquery.meta.DynamicContext;
import xbird.xquery.meta.StaticContext;
import xbird.xquery.misc.QNameUtil;
import xbird.xquery.misc.QNameTable.QualifiedName;
import xbird.xquery.type.*;

/**
 * 
 * <DIV lang="en"></DIV>
 * <DIV lang="ja"></DIV>
 * 
 * @author Makoto YUI (yuin405+xbird@gmail.com)
 */
public final class QNameType extends AtomicType {

    private static final long serialVersionUID = 4809744166146766369L;

    public static final String SYMBOL = "xs:QName";

    public static final QNameType QNAME = new QNameType();

    public QNameType() {
        super(TypeTable.QNAME_TID, SYMBOL);
    }

    public Class getJavaObjectType() {
        return QualifiedName.class;
    }

    public QNameValue createInstance(String literal, AtomicType srcType, DynamicContext dynEnv) throws XQueryException {
        final StaticContext staticEnv = dynEnv.getStaticContext();
        final QualifiedName name;
        try {
            name = QNameUtil.parse(literal, staticEnv.getStaticalyKnownNamespaces(), staticEnv.getDefaultElementNamespace());
        } catch (XQueryException e) {
            e.setErrCode("err:FONS0004");
            throw e;
        }
        return new QNameValue(name);
    }

    @Override
    protected boolean isSuperTypeOf(final AtomicType expected) {
        return expected instanceof QNameType;
    }

    @Override
    public int getXQJBaseType() {
        return XQJConstants.XQBASETYPE_QNAME;
    }
}
