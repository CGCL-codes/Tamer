package wtanaka.praya.dict;

import wtanaka.praya.obj.Message;
import wtanaka.praya.Protocol;

public class TextDefinition extends Message implements Definition {

    private String word, definition, dictionary;

    public TextDefinition(Protocol parent, String word, String dictionary, String definition) {
        super(parent);
        this.word = word;
        this.dictionary = dictionary;
        this.definition = definition;
    }

    public String getWord() {
        return word;
    }

    public String getDictionary() {
        return dictionary;
    }

    public String getDefinition() {
        return definition;
    }

    public String getContents() {
        return "[" + getWord() + "]" + " from: " + dictionary + "\n" + definition;
    }
}
