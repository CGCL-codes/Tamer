package net.sf.elbe.core.model.filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.sf.elbe.core.model.filter.parser.LdapFilterToken;

public class LdapFilterItemComponent extends LdapFilterComponent {

    private LdapFilterToken filtertypeToken;

    private LdapFilterToken valueToken;

    public LdapFilterItemComponent(LdapFilter parent) {
        super(parent);
        this.filtertypeToken = null;
        this.valueToken = null;
    }

    public boolean setStartToken(LdapFilterToken attributeToken) {
        if (attributeToken != null && attributeToken.getType() == LdapFilterToken.ATTRIBUTE) {
            super.setStartToken(attributeToken);
            return true;
        } else {
            return false;
        }
    }

    public boolean setAttributeToken(LdapFilterToken attributeToken) {
        return this.setStartToken(attributeToken);
    }

    public LdapFilterToken getAttributeToken() {
        return this.getStartToken();
    }

    public boolean setFiltertypeToken(LdapFilterToken filtertypeToken) {
        if (this.filtertypeToken == null && filtertypeToken != null && (filtertypeToken.getType() == LdapFilterToken.EQUAL || filtertypeToken.getType() == LdapFilterToken.GREATER || filtertypeToken.getType() == LdapFilterToken.LESS || filtertypeToken.getType() == LdapFilterToken.APROX || filtertypeToken.getType() == LdapFilterToken.PRESENT)) {
            this.filtertypeToken = filtertypeToken;
            return true;
        } else {
            return false;
        }
    }

    public LdapFilterToken getFilterToken() {
        return this.filtertypeToken;
    }

    public boolean setValueToken(LdapFilterToken valueToken) {
        if (this.valueToken == null && valueToken != null && valueToken.getType() == LdapFilterToken.VALUE) {
            this.valueToken = valueToken;
            return true;
        } else {
            return false;
        }
    }

    public LdapFilterToken getValueToken() {
        return this.valueToken;
    }

    public boolean isValid() {
        return startToken != null && filtertypeToken != null && (valueToken != null || filtertypeToken.getType() == LdapFilterToken.PRESENT);
    }

    public LdapFilterToken[] getTokens() {
        List tokenList = new ArrayList();
        if (this.startToken != null) {
            tokenList.add(this.startToken);
        }
        if (this.filtertypeToken != null) {
            tokenList.add(this.filtertypeToken);
        }
        if (this.valueToken != null) {
            tokenList.add(this.valueToken);
        }
        LdapFilterToken[] tokens = (LdapFilterToken[]) tokenList.toArray(new LdapFilterToken[tokenList.size()]);
        Arrays.sort(tokens);
        return tokens;
    }

    public String toString() {
        return (startToken != null ? startToken.getValue() : "") + (filtertypeToken != null ? filtertypeToken.getValue() : "") + (valueToken != null ? valueToken.getValue() : "");
    }

    public boolean addFilter(LdapFilter filter) {
        return false;
    }

    public LdapFilter[] getInvalidFilters() {
        if (this.isValid()) {
            return new LdapFilter[0];
        } else {
            return new LdapFilter[] { parent };
        }
    }

    public LdapFilter getFilter(int offset) {
        if (this.startToken != null && this.startToken.getOffset() <= offset && offset < this.startToken.getOffset() + this.startToken.getLength()) {
            return this.parent;
        } else if (this.filtertypeToken != null && this.filtertypeToken.getOffset() <= offset && offset < this.filtertypeToken.getOffset() + this.filtertypeToken.getLength()) {
            return this.parent;
        } else if (this.valueToken != null && this.valueToken.getOffset() <= offset && offset < this.valueToken.getOffset() + this.valueToken.getLength()) {
            return this.parent;
        } else {
            return null;
        }
    }

    public String getInvalidCause() {
        if (this.startToken == null) {
            return "Missing attribute name";
        } else if (this.filtertypeToken == null) {
            return "Missing filter type, select one of '=', '>=', '<=','~='";
        } else if (this.valueToken == null) {
            return "Missing value";
        } else {
            return null;
        }
    }
}
