package de.fhdarmstadt.fbi.dtree.xml;

import java.util.Map;
import de.fhdarmstadt.fbi.dtree.model.Alphabet;
import de.fhdarmstadt.fbi.dtree.model.DTree;

public final class ParseResult {

    private DTree tree;

    private Map data;

    private Alphabet alphabet;

    public ParseResult(final Alphabet alphabet, final DTree tree, final Map data) {
        if (alphabet == null) {
            throw new NullPointerException();
        }
        this.alphabet = alphabet;
        this.tree = tree;
        this.data = data;
    }

    public final Map getData() {
        return data;
    }

    public final DTree getTree() {
        return tree;
    }

    public final Alphabet getAlphabet() {
        return alphabet;
    }
}
