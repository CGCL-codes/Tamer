package br.mps.eti.siljac.toolkit.wrapper.type;

import br.mps.eti.siljac.exception.SiljacException;
import br.mps.eti.siljac.util.Constants;

public class String implements Type {

    private Integer x;

    private Integer y;

    private java.lang.String text;

    public java.lang.String getText() {
        return text;
    }

    public void setText(java.lang.String text) throws SiljacException {
        if (text == null) {
            throw new SiljacException(Constants.KEY.ERROR.TEXT_MANDATORY);
        }
        this.text = text;
    }

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) throws SiljacException {
        if (x == null) {
            throw new SiljacException(Constants.KEY.ERROR.X_MANDATORY);
        }
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) throws SiljacException {
        if (y == null) {
            throw new SiljacException(Constants.KEY.ERROR.Y_MANDATORY);
        }
        this.y = y;
    }

    public java.lang.String[] getAttributes() {
        return Constants.PROPERTY.ATTRIBUTES.STRING;
    }

    public Class[] getTypes() {
        return Constants.PROPERTY.TYPES.STRING;
    }

    public java.lang.String getKeyAttribute() {
        return Constants.PROPERTY.TEXT;
    }

    public java.lang.String getName() {
        return Constants.PROPERTY.TYPE;
    }

    public void validate() throws SiljacException {
        setX(this.x);
        setY(this.y);
        setText(this.text);
    }
}
