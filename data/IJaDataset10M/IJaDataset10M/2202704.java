package fr.cnes.sitools.solr.model;

import java.io.Serializable;
import java.util.List;

/**
 * Schema configuration class 
 * @author jp.boignard (AKKA Technologies)
 * 
 */
public final class SchemaConfigDTO implements Serializable {

    /**
   * serialVersionUID
   */
    private static final long serialVersionUID = 4777889116414262816L;

    /**
   * Document name
   */
    private String document;

    /**
   * unique Key field
   */
    private String uniqueKey;

    /**
   * defaultSearchField field
   */
    private String defaultSearchField;

    /**
   * Field list
   */
    private List<SchemaFieldDTO> fields;

    /**
   * SchemaConfigDTO constructor
   */
    public SchemaConfigDTO() {
        super();
    }

    /**
   * Gets the uniqueKey value
   * 
   * @return the uniqueKey
   */
    public String getUniqueKey() {
        return uniqueKey;
    }

    /**
   * Sets the value of uniqueKey
   * 
   * @param uniqueKey
   *          the uniqueKey to set
   */
    public void setUniqueKey(String uniqueKey) {
        this.uniqueKey = uniqueKey;
    }

    /**
   * Gets the defaultSearchField value
   * 
   * @return the defaultSearchField
   */
    public String getDefaultSearchField() {
        return defaultSearchField;
    }

    /**
   * Sets the value of defaultSearchField
   * 
   * @param defaultSearchField
   *          the defaultSearchField to set
   */
    public void setDefaultSearchField(String defaultSearchField) {
        this.defaultSearchField = defaultSearchField;
    }

    /**
   * Gets the fields value
   * 
   * @return the fields
   */
    public List<SchemaFieldDTO> getFields() {
        return fields;
    }

    /**
   * Sets the value of fields
   * 
   * @param fields
   *          the fields to set
   */
    public void setFields(List<SchemaFieldDTO> fields) {
        this.fields = fields;
    }

    /**
   * Gets the document value
   * 
   * @return the document
   */
    public String getDocument() {
        return document;
    }

    /**
   * Sets the value of document
   * 
   * @param document
   *          the document to set
   */
    public void setDocument(String document) {
        this.document = document;
    }
}
