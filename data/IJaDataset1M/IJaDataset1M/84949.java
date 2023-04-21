package com.antilia.letsplay.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import com.antilia.letsplay.model.mock.NamedImage;

/**
 * @author Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 *
 */
public class Words {

    private static List<Word> words = new ArrayList<Word>();

    public static final String[] WORDS = { "ardilla", "caballo", "cabeza", "cocodrilo", "dinosaurio", "jirafa", "gormiti", "gorra", "guitarra", "paloma", "pelota", "zapato" };

    private static final Words instance = new Words();

    private Words() {
        for (String w : WORDS) {
            Word word = new Word(w);
            word.setImage(new NamedImage(w));
            words.add(word);
        }
    }

    public Word getRandom() {
        int word = new Random().nextInt(words.size());
        return words.get(word);
    }

    public Iterable<Word> getAll() {
        return words;
    }

    public static Words getInstance() {
        return instance;
    }
}
