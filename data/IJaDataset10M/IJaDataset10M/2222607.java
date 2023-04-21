package org.hibernate.search.bridge;

import org.apache.lucene.document.Document;

/**
 * Bridge to use a StringBridge as a FieldBridge.
 *
 * @author Emmanuel Bernard (C) 2011 Red Hat Inc.
 * @author Sanne Grinovero (C) 2011 Red Hat Inc.
 */
public class String2FieldBridgeAdaptor implements FieldBridge, StringBridge {

    private final StringBridge stringBridge;

    public String2FieldBridgeAdaptor(StringBridge stringBridge) {
        this.stringBridge = stringBridge;
    }

    public void set(String name, Object value, Document document, LuceneOptions luceneOptions) {
        String indexedString = stringBridge.objectToString(value);
        if (indexedString == null && luceneOptions.indexNullAs() != null) {
            indexedString = luceneOptions.indexNullAs();
        }
        luceneOptions.addFieldToDocument(name, indexedString, document);
    }

    public String objectToString(Object object) {
        return stringBridge.objectToString(object);
    }
}
