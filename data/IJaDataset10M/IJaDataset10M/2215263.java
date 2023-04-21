package com.nixsolutions.dictionary;

import com.nixsolutions.dictionary.model.DictionaryResourceLoader;
import com.nixsolutions.dictionary.model.DictionarySearchModel;
import com.nixsolutions.dictionary.model.DictionaryAudioPlayerImpl;
import com.nixsolutions.dictionary.view.*;
import javax.microedition.lcdui.*;
import java.util.Hashtable;
import java.util.Stack;

/**
 *  Class used as Controller for MVC pattern
 */
public class DictionaryController {

    private static final int ENGLISH_LNG_NUMBER = 0;

    private static DictionaryController instance;

    private Display display;

    private DictionaryMIDlet miDlet;

    private Alert alert;

    private Stack viewsStack;

    private DictionaryView lastScreen;

    private DictionarySplashScreen splashScreen = new DictionarySplashScreen();

    private DictionaryCategoryList categoryList;

    private DictionaryPhraseList phraseList;

    private DictionarySearchForm searchForm;

    private DictionaryPinyinForm pinyinForm;

    private DictionaryActionSelectList actionSelectList;

    /**
     * private constructor for Singleton pattern
     */
    private DictionaryController() {
    }

    /**
     *
     * @return instance od class
     */
    public static DictionaryController getInstance() {
        if (instance == null) {
            instance = new DictionaryController();
        }
        return instance;
    }

    /**
     * method initialize controller and start resources loader thread.
     *
     * @param miDlet instance of DictionaryMIDlet to get Display and close application
     */
    public void init(DictionaryMIDlet miDlet) {
        if (this.display == null) {
            this.miDlet = miDlet;
            this.display = Display.getDisplay(this.miDlet);
            this.viewsStack = new Stack();
            splashScreen.show(display, null);
            Thread th = new Thread(new DictionaryResourceLoader());
            th.start();
        }
    }

    /**
     * Method handle all events from application
     *
     * @param event event to be handled/
     */
    public void handleEvent(DictionaryEvent event) {
        switch(event.getType()) {
            case DictionaryEvent.EXIT_EVENT:
                miDlet.notifyDestroyed();
                break;
            case DictionaryEvent.CLOSE_SPLASH_EVENT:
                if (categoryList == null) {
                    categoryList = new DictionaryCategoryList();
                }
                categoryList.setLngNumber(ENGLISH_LNG_NUMBER);
                addDisplayable(categoryList.show(display, null));
                break;
            case DictionaryEvent.BACK_EVENT:
                showLastElement();
                break;
            case DictionaryEvent.LANGUAGE_SELECT_EVENT:
                if (categoryList == null) {
                    categoryList = new DictionaryCategoryList();
                }
                categoryList.setLngNumber(event.getAsInt("lngNum"));
                addDisplayable(categoryList.show(display, null));
                break;
            case DictionaryEvent.CATEGORY_SELECT_EVENT:
                if (phraseList == null) {
                    phraseList = new DictionaryPhraseList();
                }
                phraseList.setCategoryName(event.getAsString("ctgName"));
                Hashtable searchResult = event.getAsHashtable("res");
                if (searchResult == null) {
                    phraseList.setCategoryNumber(event.getAsInt("ctgNum"));
                    phraseList.setLngNunber(event.getAsInt("lngNum"));
                } else {
                    phraseList.setPhrases(searchResult);
                }
                addDisplayable(phraseList.show(display, null));
                break;
            case DictionaryEvent.SEARCH_SELECT_EVENT:
                if (searchForm == null) {
                    searchForm = new DictionarySearchForm();
                }
                searchForm.setLngNumber(event.getAsInt("lngNum"));
                addDisplayable(searchForm.show(display, null));
                break;
            case DictionaryEvent.SEARCH_EVENT:
                DictionarySearchModel.getInstance().search(event.getAsString("txt"), event.getAsInt("lngNum"));
                break;
            case DictionaryEvent.PLAY_SOUND_EVENT:
                DictionaryAudioPlayerImpl.getInstance().play(event.getAsString("sndCode"));
                break;
            case DictionaryEvent.SHOW_ERROR_EVENT:
                showError(event.getAsString("txt"));
                break;
            case DictionaryEvent.VIEW_PINYIN_EVENT:
                if (pinyinForm == null) {
                    pinyinForm = new DictionaryPinyinForm();
                }
                pinyinForm.setText(event.getAsString("sndCode"), event.getAsBoolean("isPinyin"));
                addDisplayable(pinyinForm.show(display, null));
                break;
            case DictionaryEvent.SHOW_OPTION_EVENT:
                if (actionSelectList == null) {
                    actionSelectList = new DictionaryActionSelectList();
                }
                actionSelectList.setPhraseCodeAndText(event.getAsString("sndCode"), event.getAsString("text"));
                addDisplayable(actionSelectList.show(display, null));
                break;
        }
    }

    /**
     * Save last shoved 'view' in Stack, for correctry working of "BACK" command
     *
     * @param element element to be saved
     */
    private void addDisplayable(Displayable element) {
        if (lastScreen != null) {
            viewsStack.push(lastScreen);
        }
        lastScreen = (DictionaryView) element;
        lastScreen.show(display, null);
    }

    /**
     * show privios 'view' on display
     */
    private void showLastElement() {
        lastScreen = (DictionaryView) viewsStack.pop();
        lastScreen.show(display, null);
    }

    /**
     * show Alert with Exception text to user
     *
     * @param text Message of alert
     */
    private void showError(String text) {
        String title = DictionaryPropetriesLoader.getProperty("ttl.err");
        if (alert == null) {
            alert = new Alert(title);
            alert.setTimeout(Alert.FOREVER);
            alert.setType(AlertType.ERROR);
        }
        alert.setString(text);
        if (lastScreen != null) {
            lastScreen.show(display, alert);
        } else {
            display.setCurrent(alert, new Form(title));
        }
    }
}
