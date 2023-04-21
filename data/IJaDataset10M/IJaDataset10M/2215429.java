package org.vardb.search.dao;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.vardb.resources.CResourceType;

@Entity
@Table(name = "vw_suggestions")
public class CSuggestion {

    protected String m_keyword;

    protected String m_lowercase;

    protected CResourceType m_type;

    protected String m_identifier;

    @Id
    public String getKeyword() {
        return m_keyword;
    }

    public void setKeyword(String keyword) {
        m_keyword = keyword;
    }

    public String getLowercase() {
        return m_lowercase;
    }

    public void setLowercase(String lowercase) {
        m_lowercase = lowercase;
    }

    public CResourceType getType() {
        return m_type;
    }

    public void setType(CResourceType type) {
        m_type = type;
    }

    public String getIdentifier() {
        return m_identifier;
    }

    public void setIdentifier(String identifier) {
        m_identifier = identifier;
    }
}
