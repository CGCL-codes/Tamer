package edu.asu.vogon.vocabulary.factories;

import edu.asu.vogon.model.Vocabulary;

public interface IFactory {

    public Vocabulary createVocabulary(String file, String name);
}
