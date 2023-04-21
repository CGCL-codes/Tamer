package com.wordpower.ui;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.List;
import com.wordpower.WordPowerMIDlet;
import com.wordpower.resource.ResourceManager;

public class MainForm extends List implements CommandListener, DataPresentation {

    public static final int ITEM_VOCABULARY_REVIEW = 0;

    public static final int ITEM_VOCABULARY_TEST = 1;

    public static final int ITEM_SPELLING_TEST = 2;

    private static MainForm instance;

    public static MainForm getInstance() {
        if (instance == null) instance = new MainForm();
        return instance;
    }

    public static void show() {
        Display.getDisplay(WordPowerMIDlet.getInstance()).setCurrent(getInstance());
        getInstance().update();
    }

    private Command exitCommand;

    private MainForm() {
        super(getString("mainMenu"), List.IMPLICIT);
        setCommandListener(this);
        append(getString("vocabReview"), getImage("vocabReview"));
        append(getString("vocabTest"), getImage("vocabTest"));
        append(getString("spellTest"), getImage("spellTest"));
        exitCommand = new Command(getString("exit"), Command.EXIT, 2);
        addCommand(exitCommand);
    }

    public void commandAction(Command c, Displayable d) {
        if (exitCommand == c) WordPowerMIDlet.getInstance().notifyDestroyed(); else {
            OptionForm.getInstance().reset();
            OptionForm.show();
        }
    }

    public DataPresentation getSelectedDisplayable() {
        return getDisplayable(getSelectedIndex());
    }

    public DataPresentation getDisplayable(int index) {
        switch(index) {
            case MainForm.ITEM_VOCABULARY_REVIEW:
                return VocabularyReviewForm.getInstance();
            case MainForm.ITEM_VOCABULARY_TEST:
                return VocabularyTestForm.getInstance();
            case MainForm.ITEM_SPELLING_TEST:
                return SpellingTestForm.getInstance();
            default:
                return null;
        }
    }

    private static String getString(String key) {
        return ResourceManager.getInstance().getString(key);
    }

    private static Image getImage(String key) {
        return ResourceManager.getImage(key);
    }

    public void reset() {
    }

    public void update() {
    }

    public Displayable getDisplayable() {
        return this;
    }
}
