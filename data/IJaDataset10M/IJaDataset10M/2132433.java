package edu.asu.vogon.vocabulary.factories;

import java.util.HashMap;
import java.util.Map;
import edu.asu.vogon.model.Vocabulary;

public class DelegatingVocabularyFactory implements IFactory {

    private Map<String, IFactory> factories;

    public DelegatingVocabularyFactory() {
        factories = new HashMap<String, IFactory>();
        factories.put("obo", new OBOFactory());
    }

    public Vocabulary createVocabulary(String path, String name) {
        int indexLastDot = path.lastIndexOf(".");
        if (indexLastDot == -1) return null;
        String extension = path.substring(indexLastDot + 1).toLowerCase();
        IFactory factory = factories.get(extension);
        if (factory != null) {
            return factory.createVocabulary(path, name);
        }
        return null;
    }
}
