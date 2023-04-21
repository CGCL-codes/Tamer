package yapgen.base;

import java.util.Collection;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import yapgen.base.type.StringValuable;
import yapgen.base.util.XmlSerializable;

/**
 *
 * @author riccardo
 */
public class WordValueSubstance extends BaseObject implements StringValuable, PathMap, XmlSerializable {

    protected WordValueMap attributes = new WordValueMap();

    public WordValueSubstance() {
        initAttributeTypes();
    }

    private void initAttributeTypes() {
        if (EntityManager.getAttributeTypes(this.getClass()) != null) {
            this.attributes.addAttributeTypes(EntityManager.getAttributeTypes(this.getClass()));
        }
    }

    public WordValueMap getAttributes() {
        return attributes;
    }

    @Override
    public int size() {
        return attributes.size();
    }

    @Override
    public Collection values() {
        return attributes.values();
    }

    @Override
    public Object get(LinkedList<Word> wordPath) {
        return attributes.get(wordPath);
    }

    @Override
    public WordValue get(Word word) {
        return attributes.get(word);
    }

    @Override
    public WordValue get(String path) {
        return attributes.get(path);
    }

    public void put(Word word, String stringValue) {
        attributes.put(word, stringValue);
    }

    public void put(String path, String stringValue) {
        attributes.put(path, stringValue);
    }

    public void putAll(WordValueSubstance substance) {
        this.attributes.putAll(substance.getAttributes());
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{").append(attributes).append("}::").append(this.getClass().getSimpleName());
        return builder.toString();
    }

    @Override
    public void valueFromString(String stringValue) {
        this.attributes.valueFromString(stringValue);
    }

    @Override
    public String valueToString() {
        return this.attributes.valueToString();
    }

    @Override
    public Object clone() {
        WordValueSubstance copy = null;
        try {
            copy = (WordValueSubstance) super.clone();
            copy.attributes = (WordValueMap) attributes.clone();
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(WordValueSubstance.class.getName()).log(Level.SEVERE, null, ex);
        }
        return copy;
    }

    @Override
    public String toXml() {
        StringBuilder builder = new StringBuilder();
        builder.append(attributes.toXml());
        return builder.toString();
    }
}
