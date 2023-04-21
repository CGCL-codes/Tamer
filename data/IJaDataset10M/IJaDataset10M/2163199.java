package com.antilia.letsplay.domain;

import java.util.Random;
import com.antilia.hibernate.command.DefaultCommander;
import com.antilia.letsplay.model.Word;

/**
 * @author Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 *
 */
public class DWords {

    private Integer size;

    public DWords() {
        size = DefaultCommander.count(DWord.class).intValue();
    }

    public Word getRandom() {
        int word = new Random().nextInt(size);
        DWord dWord = DefaultCommander.findById(DWord.class, new Long(word));
        Word w = new Word();
        w.setText(dWord.getText());
        w.setImage(new DataBaseImage(dWord.getImage().getBytes()));
        return w;
    }
}
