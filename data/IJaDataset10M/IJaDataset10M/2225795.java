package org.hibernate.search.test.bridge;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.hibernate.search.bridge.FieldBridge;
import org.hibernate.search.bridge.LuceneOptions;

/**
 * @author Emmanuel Bernard
 */
public class TruncateFieldBridge implements FieldBridge {

    public Object get(String name, Document document) {
        Field field = document.getField(name);
        return field.stringValue();
    }

    public void set(String name, Object value, Document document, LuceneOptions luceneOptions) {
        String stringValue = (String) value;
        if (stringValue != null) {
            String indexedString = stringValue.substring(0, stringValue.length() / 2);
            luceneOptions.addFieldToDocument(name, indexedString, document);
        }
    }
}
