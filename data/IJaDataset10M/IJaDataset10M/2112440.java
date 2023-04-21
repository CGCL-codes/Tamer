package ch.ethz.mxquery.datamodel.xdm;

import ch.ethz.mxquery.datamodel.Identifier;
import ch.ethz.mxquery.datamodel.MXQueryDate;
import ch.ethz.mxquery.datamodel.XQName;
import ch.ethz.mxquery.datamodel.types.Type;
import ch.ethz.mxquery.exceptions.MXQueryException;

public final class DateAttrToken extends ElementToken {

    private final MXQueryDate value;

    public DateAttrToken(Identifier id, MXQueryDate value, XQName name, XDMScope scope) throws MXQueryException {
        super(Type.createAttributeType(Type.DATE), id, name, scope);
        this.value = value;
    }

    public DateAttrToken(DateAttrToken token) {
        super(token);
        this.value = token.getDate();
    }

    public MXQueryDate getDate() {
        return this.value;
    }

    public String getValueAsString() {
        return this.value.toString();
    }

    public NamedToken copy(XQName newName) throws MXQueryException {
        return new DateAttrToken(this.id, this.value, newName, dynamicScope);
    }

    public TokenInterface copy() {
        return new DateAttrToken(this);
    }
}
