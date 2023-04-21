package org.geonetwork.domain.filter110.comparisonop;

import org.geonetwork.domain.filter110.expression.Literal;
import org.geonetwork.domain.filter110.expression.PropertyName;

/**
 * 
 * @author heikki doeleman
 *
 */
public class PropertyIsLike extends ComparisonOps {

    private PropertyName propertyName;

    private Literal literal;

    private String wildCard;

    private String singleChar;

    private String escapeChar;

    public PropertyName getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(PropertyName propertyName) {
        this.propertyName = propertyName;
    }

    public Literal getLiteral() {
        return literal;
    }

    public void setLiteral(Literal literal) {
        this.literal = literal;
    }

    public String getWildCard() {
        return wildCard;
    }

    public void setWildCard(String wildCard) {
        this.wildCard = wildCard;
    }

    public String getSingleChar() {
        return singleChar;
    }

    public void setSingleChar(String singleChar) {
        this.singleChar = singleChar;
    }

    public String getEscapeChar() {
        return escapeChar;
    }

    public void setEscapeChar(String escapeChar) {
        this.escapeChar = escapeChar;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((escapeChar == null) ? 0 : escapeChar.hashCode());
        result = prime * result + ((literal == null) ? 0 : literal.hashCode());
        result = prime * result + ((propertyName == null) ? 0 : propertyName.hashCode());
        result = prime * result + ((singleChar == null) ? 0 : singleChar.hashCode());
        result = prime * result + ((wildCard == null) ? 0 : wildCard.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!super.equals(obj)) return false;
        if (getClass() != obj.getClass()) return false;
        PropertyIsLike other = (PropertyIsLike) obj;
        if (escapeChar == null) {
            if (other.escapeChar != null) return false;
        } else if (!escapeChar.equals(other.escapeChar)) return false;
        if (literal == null) {
            if (other.literal != null) return false;
        } else if (!literal.equals(other.literal)) return false;
        if (propertyName == null) {
            if (other.propertyName != null) return false;
        } else if (!propertyName.equals(other.propertyName)) return false;
        if (singleChar == null) {
            if (other.singleChar != null) return false;
        } else if (!singleChar.equals(other.singleChar)) return false;
        if (wildCard == null) {
            if (other.wildCard != null) return false;
        } else if (!wildCard.equals(other.wildCard)) return false;
        return true;
    }
}
