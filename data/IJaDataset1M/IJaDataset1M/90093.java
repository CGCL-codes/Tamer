package com.gerodp.controller;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import com.gerodp.model.ModelSingleton;
import com.gerodp.view.ScreenEditArticle;

/**
 * Handles when the user press the add new word button
 * @author gero
 *
 */
public class AddWordListener implements SelectionListener {

    private ScreenEditArticle screen;

    public AddWordListener(ScreenEditArticle screen) {
        this.screen = screen;
    }

    public void widgetDefaultSelected(SelectionEvent arg0) {
    }

    public void widgetSelected(SelectionEvent arg0) {
        String word = ((ScreenEditArticle) screen).getWordText().getText().trim().toLowerCase().replaceAll("[ ]+", " ");
        String description = ((ScreenEditArticle) screen).getDescriptionText().getText().toLowerCase().replaceAll("[ ]+", " ");
        ModelSingleton.getModel().addWord(word, description);
    }
}
