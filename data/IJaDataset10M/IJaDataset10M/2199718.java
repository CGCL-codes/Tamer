package org.openconcerto.erp.generationDoc;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DocumentGeneratorManager {

    private static DocumentGeneratorManager instance = new DocumentGeneratorManager();

    private Map<String, DocumentGenerator> generators = new HashMap<String, DocumentGenerator>();

    private DocumentGenerator defautGenerator;

    public static DocumentGeneratorManager getInstance() {
        return instance;
    }

    public void add(String templateId, DocumentGenerator generator) {
        this.generators.put(templateId, generator);
    }

    public void remove(DocumentGenerator generator) {
        final Set<String> keys = generators.keySet();
        for (String key : keys) {
            if (generators.get(key).equals(generator)) {
                generators.remove(key);
            }
        }
    }

    public void setDefaultGenerator(DocumentGenerator generator) {
        this.defautGenerator = generator;
    }

    /**
     * Returns the document generator a specific templateId
     * */
    public DocumentGenerator getGenerator(String templateId) {
        DocumentGenerator generator = this.generators.get(templateId);
        if (generator == null) {
            generator = defautGenerator;
        }
        return generator;
    }

    public void dump() {
        System.out.println(this.getClass().getCanonicalName());
        System.out.println("Default generator:" + this.defautGenerator);
        Set<String> ids = generators.keySet();
        for (String templateId : ids) {
            System.out.println("'" + templateId + "' : " + generators.get(templateId));
        }
    }
}
