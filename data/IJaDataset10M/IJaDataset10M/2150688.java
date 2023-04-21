package com.inet.jortho;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.text.JTextComponent;

public class AddWordAction extends AbstractAction {

    private String word;

    private JTextComponent jText;

    /**
     * Create a action to add a word to the current user dictionary.
     * 
     * @param jText
     *            component that need refresh after adding to remove the red zigzag line
     * @param word
     *            the word that can be added
     */
    public AddWordAction(JTextComponent jText, String word) {
        this(jText, word, Utils.getResource("addToDictionary"));
    }

    /**
     * Create a action to add a word to the current user dictionary.
     * 
     * @param jText
     *            component that need refresh after adding to remove the red zigzag line
     * @param word
     *            the word that can be added
     * @param label
     *            the display text of the action
     */
    public AddWordAction(JTextComponent jText, String word, String label) {
        super(label);
        this.word = word;
        this.jText = jText;
    }

    /**
     * Add the word to the current user directory.
     */
    public void actionPerformed(ActionEvent arg0) {
        UserDictionaryProvider provider = SpellChecker.getUserDictionaryProvider();
        if (provider != null) {
            provider.addWord(word);
        }
        Dictionary dictionary = SpellChecker.getCurrentDictionary();
        dictionary.add(word);
        dictionary.trimToSize();
        AutoSpellChecker.refresh(jText);
    }
}
