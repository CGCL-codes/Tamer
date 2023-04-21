package org.mybatis.generator.config;

import static org.mybatis.generator.internal.util.StringUtility.stringContainsSpace;
import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;
import static org.mybatis.generator.internal.util.messages.Messages.getString;
import java.util.List;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.XmlElement;

/**
 * @author Jeff Butler
 * 
 */
public class IgnoredColumn {

    private String columnName;

    private boolean isColumnNameDelimited;

    private String configuredDelimitedColumnName;

    /**
     * 
     */
    public IgnoredColumn(String columnName) {
        super();
        this.columnName = columnName;
        isColumnNameDelimited = stringContainsSpace(columnName);
    }

    public String getColumnName() {
        return columnName;
    }

    public boolean isColumnNameDelimited() {
        return isColumnNameDelimited;
    }

    public void setColumnNameDelimited(boolean isColumnNameDelimited) {
        this.isColumnNameDelimited = isColumnNameDelimited;
        configuredDelimitedColumnName = isColumnNameDelimited ? "true" : "false";
    }

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof IgnoredColumn)) {
            return false;
        }
        return columnName.equals(((IgnoredColumn) obj).getColumnName());
    }

    public int hashCode() {
        return columnName.hashCode();
    }

    public XmlElement toXmlElement() {
        XmlElement xmlElement = new XmlElement("ignoreColumn");
        xmlElement.addAttribute(new Attribute("column", columnName));
        if (stringHasValue(configuredDelimitedColumnName)) {
            xmlElement.addAttribute(new Attribute("delimitedColumnName", configuredDelimitedColumnName));
        }
        return xmlElement;
    }

    public void validate(List<String> errors, String tableName) {
        if (!stringHasValue(columnName)) {
            errors.add(getString("ValidationError.21", tableName));
        }
    }
}
