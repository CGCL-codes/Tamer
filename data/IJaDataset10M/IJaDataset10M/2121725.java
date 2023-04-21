package edu.opexcavator.nlp;

import java.util.List;

/**
 * Interface for semantic search of word senses
 * @author Jesica N. Fera
 *
 */
public interface ISemanticSearch {

    public List<String> getSenseWords(String senseCode, char pos);

    public List<String> getWordSenses(String word, char pos);
}
