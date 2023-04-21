package edu.opexcavator.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author Jesica N. Fera
 *
 */
public class DocumentImpl implements Document {

    private final String text;

    private Collection<Sentence> sentences;

    private boolean isTokenized;

    private Collection<Token> tokens;

    public DocumentImpl() {
        text = "";
    }

    /**
	 * @param text
	 */
    public DocumentImpl(String text) {
        this.text = text;
    }

    public void setSentences(Collection<Sentence> sentences) {
        this.sentences = sentences;
        Collection<Token> tokens = new ArrayList<Token>();
        for (Sentence sentence : sentences) {
            tokens.addAll(sentence.getComponents());
        }
        this.setTokens(tokens);
    }

    public void setTokens(Collection<Token> tokens) {
        this.tokens = tokens;
    }

    /**
	 * Only allow field modification by using set method explicitly.
	 * @return
	 */
    public Collection<Sentence> getSentences() {
        return Collections.unmodifiableCollection(sentences);
    }

    /**
	 * @return
	 */
    public boolean isTokenized() {
        return isTokenized;
    }

    public boolean isSplitted() {
        return (sentences != null && !sentences.isEmpty());
    }

    /**
	 * @return
	 */
    public Collection<Token> getTokens() {
        List<Token> tokens = new ArrayList<Token>();
        if (sentences != null) {
            for (Sentence sentence : sentences) {
                tokens.addAll(sentence.getComponents());
            }
        }
        if (tokens.isEmpty()) {
            return Collections.unmodifiableCollection(this.tokens);
        }
        return tokens;
    }

    /**
	 * @return
	 */
    public String getText() {
        return this.text;
    }

    public void setTokenized(boolean tokenized) {
        this.isTokenized = true;
    }

    /**
	 * @return
	 */
    public boolean hasPOSTags() {
        if (!sentences.isEmpty()) {
            List<Token> firstSentenceTokens = new ArrayList<Token>(sentences.iterator().next().getComponents());
            if (!firstSentenceTokens.isEmpty()) {
                if (firstSentenceTokens.get(0).isPOSTagged()) {
                    return true;
                }
            }
        }
        return false;
    }
}
