package bg.plambis.dict;

import java.io.Serializable;

/**
 * Base class for every word in the dictionary 
 * @author pivanov
 */
public class Word implements Serializable {

    private static final long serialVersionUID = 1228511354723440744L;

    private int id = -1;

    private String word;

    private String translation;

    public Word(int id, String word, String translation) {
        this.id = id;
        this.word = word;
        this.translation = translation;
    }

    public Word(String word) {
        this.word = word;
    }

    public int getId() {
        return id;
    }

    public String getWord() {
        return word;
    }

    public String getTranslation() {
        return translation;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null && !(obj instanceof Word)) return false;
        if (this == obj) return true;
        Word otherWord = (Word) obj;
        return word.equalsIgnoreCase(otherWord.getWord());
    }

    @Override
    public int hashCode() {
        return word.hashCode();
    }

    @Override
    public String toString() {
        return "Id: " + id + "Word: " + word + "  Translation: " + translation;
    }
}
