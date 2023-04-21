package pt.utl.ist.lucene.treceval.handlers;

import org.apache.lucene.document.Field;
import java.util.Collection;
import java.util.Map;

/**
 * @author Jorge Machado
 * @date 22/Ago/2008
 * @see pt.utl.ist.lucene.treceval.handlers.topics
 */
public class IdMap {

    String id;

    Map<String, String> textFields;

    Map<String, String> storedFields;

    Collection<Field> preparedFields;

    Collection<TextField> isolatedTextFields;

    public static class TextField {

        public String name;

        public String value;

        public TextField(String name, String value) {
            this.name = name;
            this.value = value;
        }
    }

    public IdMap(String id, Map<String, String> textFields, Collection<Field> uniqueFields) {
        this.id = id;
        this.textFields = textFields;
        this.preparedFields = uniqueFields;
    }

    public IdMap(String id, Map<String, String> textFields) {
        this.id = id;
        this.textFields = textFields;
    }

    public IdMap(String id, Map<String, String> textFields, Map<String, String> storedFields, Collection<Field> preparedFields, Collection<TextField> isolatedTextFields) {
        this.id = id;
        this.textFields = textFields;
        this.storedFields = storedFields;
        this.preparedFields = preparedFields;
        this.isolatedTextFields = isolatedTextFields;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTextFields(Map<String, String> textFields) {
        this.textFields = textFields;
    }

    public Map<String, String> getTextFields() {
        return textFields;
    }

    public Collection<Field> getPreparedFields() {
        return preparedFields;
    }

    public void setPreparedFields(Collection<Field> preparedFields) {
        this.preparedFields = preparedFields;
    }

    public Map<String, String> getStoredFields() {
        return storedFields;
    }

    public void setStoredFields(Map<String, String> storedFields) {
        this.storedFields = storedFields;
    }

    public Collection<TextField> getIsolatedTextFields() {
        return isolatedTextFields;
    }

    public void setIsolatedTextFields(Collection<TextField> isolatedTextFields) {
        this.isolatedTextFields = isolatedTextFields;
    }
}
