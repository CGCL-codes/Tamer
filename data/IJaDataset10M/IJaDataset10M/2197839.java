package org.apache.ibatis.abator.config;

import org.apache.ibatis.abator.api.dom.xml.Attribute;
import org.apache.ibatis.abator.api.dom.xml.XmlElement;
import org.apache.ibatis.abator.internal.db.DatabaseDialects;

/**
 * This class specifies that a key is auto-generated, either as an identity
 * column (post insert), or as some other query like a sequences (pre insert).
 * 
 * @author Jeff Butler
 */
public class GeneratedKey {

    private String column;

    private String configuredSqlStatement;

    private String runtimeSqlStatement;

    private boolean isIdentity;

    /**
     * 
     */
    public GeneratedKey(String column, String configuredSqlStatement, boolean isIdentity) {
        super();
        this.column = column;
        this.isIdentity = isIdentity;
        this.configuredSqlStatement = configuredSqlStatement;
        DatabaseDialects dialect = DatabaseDialects.getDatabaseDialect(configuredSqlStatement);
        if (dialect == null) {
            this.runtimeSqlStatement = configuredSqlStatement;
        } else {
            this.runtimeSqlStatement = dialect.getIdentityRetrievalStatement();
        }
    }

    public String getColumn() {
        return column;
    }

    public boolean isIdentity() {
        return isIdentity;
    }

    public String getRuntimeSqlStatement() {
        return runtimeSqlStatement;
    }

    public XmlElement toXmlElement() {
        XmlElement xmlElement = new XmlElement("generatedKey");
        xmlElement.addAttribute(new Attribute("column", column));
        xmlElement.addAttribute(new Attribute("sqlStatement", configuredSqlStatement));
        xmlElement.addAttribute(new Attribute("identity", isIdentity ? "true" : "false"));
        return xmlElement;
    }
}
