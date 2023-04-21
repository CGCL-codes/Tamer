package com.wordpower.ui;

import java.io.IOException;
import java.util.TimerTask;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.List;
import com.wordpower.WordPowerMIDlet;
import com.wordpower.expr.FilterExpressionParser;
import com.wordpower.expr.ParseException;
import com.wordpower.model.Library;
import com.wordpower.model.Timing;
import com.wordpower.model.WordEntry;
import com.wordpower.model.presentation.WordEntryPresentation;
import com.wordpower.resource.ResourceManager;
import com.wordpower.util.CollectionUtil;

public class VocabularyReviewForm extends List implements CommandListener, DataPresentation {

    private static VocabularyReviewForm instance;

    public static VocabularyReviewForm getInstance() {
        if (instance == null) instance = new VocabularyReviewForm();
        return instance;
    }

    public static void show() {
        getInstance().update();
        Display.getDisplay(WordPowerMIDlet.getInstance()).setCurrent(getInstance());
    }

    private WordEntryPresentation[] entries;

    private Command showMeaningCommand;

    private Command setMarkCommand;

    private Command pauseCommand;

    private Command resumeCommand;

    private Command mainMenuCommand;

    private long justNow;

    private int time;

    private boolean paused;

    private boolean running;

    private VocabularyReviewForm() {
        super(getString("vocabularyReview"), List.IMPLICIT);
        setCommandListener(this);
        setMarkCommand = new Command(getString("setMark"), Command.ITEM, 1);
        showMeaningCommand = new Command(getString("showMeaning"), Command.ITEM, 2);
        pauseCommand = new Command(getString("pause"), Command.ITEM, 3);
        resumeCommand = new Command(getString("resume"), Command.ITEM, 3);
        mainMenuCommand = new Command(getString("mainMenu"), Command.ITEM, 3);
        setSelectCommand(setMarkCommand);
        addCommand(showMeaningCommand);
        addCommand(pauseCommand);
        addCommand(resumeCommand);
        addCommand(mainMenuCommand);
    }

    private static String getString(String key) {
        return ResourceManager.getInstance().getString(key);
    }

    private static Image getImage(String key) {
        return ResourceManager.getImage(key);
    }

    public void commandAction(Command c, Displayable d) {
        if (setMarkCommand == c) {
            WordEntryPresentation item = entries[getSelectedIndex()];
            item.setMark((item.getMark() + 1) % 3);
            item.setShow(WordEntryPresentation.SHOW_WORD);
            set(getSelectedIndex(), item.toString(), item.getMarkImage());
        } else if (showMeaningCommand == c) {
            WordEntryPresentation item = entries[getSelectedIndex()];
            item.setShow(WordEntryPresentation.SHOW_WORD == item.getShow() ? WordEntryPresentation.SHOW_BOTH : WordEntryPresentation.SHOW_WORD);
            item.setMark(WordEntryPresentation.MARK_NO == item.getMark() ? WordEntryPresentation.MARK_QUESTION : WordEntryPresentation.MARK_NO);
            set(getSelectedIndex(), item.toString(), item.getMarkImage());
        } else if (pauseCommand == c) {
            paused = true;
        } else if (resumeCommand == c) {
            paused = false;
        } else if (mainMenuCommand == c) {
            running = false;
            MainForm.show();
        }
    }

    public void reset() {
        entries = null;
        deleteAll();
        justNow = 0;
        time = 0;
        paused = false;
        running = false;
    }

    public void update() {
        if (entries == null) {
            Library[] libraries = LibraryForm.getInstance().getSelectedLibraries();
            for (int i = 0; i < libraries.length; i++) {
                try {
                    libraries[i].load();
                } catch (IOException ex) {
                    ex.printStackTrace();
                    Alerts.show(ex.toString(), AlertType.ERROR);
                }
            }
            String expr = OptionForm.getInstance().getFilterExpression();
            try {
                WordEntry[] temp = FilterExpressionParser.evalutate(libraries, expr);
                if (OptionForm.getInstance().shuffles()) CollectionUtil.shuffle(temp, 3); else CollectionUtil.sort(temp);
                entries = new WordEntryPresentation[temp.length];
                for (int i = 0; i < entries.length; i++) entries[i] = new WordEntryPresentation(temp[i]);
                for (int i = 0; i < entries.length; i++) append(entries[i].toString(), getImage("question"));
                time = OptionForm.getInstance().getSelectedTiming().getMillis();
                running = true;
                if (Timing.COUNT_UP_TIMING.equals(OptionForm.getInstance().getSelectedTiming())) time = 0;
                if (!Timing.NO_TIMING.equals(OptionForm.getInstance().getSelectedTiming())) {
                    Timers.scheduleAtFixedRate(new TimerTask() {

                        public void run() {
                            if (!running) cancel();
                            if (justNow == 0) justNow = System.currentTimeMillis();
                            if (!paused) {
                                int elapsed = (int) (System.currentTimeMillis() - justNow);
                                time += (Timing.COUNT_UP_TIMING.equals(OptionForm.getInstance().getSelectedTiming()) ? elapsed : -elapsed);
                                if (time < 0) {
                                    time = 0;
                                    cancel();
                                }
                                setTitle(format(time));
                            }
                            justNow = System.currentTimeMillis();
                        }
                    }, 0, 100);
                } else {
                    setTitle("--:--:--");
                }
            } catch (ParseException ex) {
                ex.printStackTrace();
                Alerts.show(getString("invalidFilterExpr") + expr);
                return;
            }
        }
    }

    public Displayable getDisplayable() {
        return this;
    }

    private String format(int time) {
        int millis = time % 1000;
        time /= 1000;
        int hour = time / 3600;
        int minute = (time - hour * 3600) / 60;
        int second = time - hour * 3600 - minute * 60;
        String hourStr = Integer.toString(hour);
        String minuteStr = Integer.toString(minute);
        String secondStr = Integer.toString(second);
        String millisStr = Integer.toString(millis);
        if (hourStr.length() == 1) hourStr = "0" + hourStr;
        if (minuteStr.length() == 1) minuteStr = "0" + minuteStr;
        if (secondStr.length() == 1) secondStr = "0" + secondStr;
        if (millisStr.length() < 3) {
            int diff = 3 - millisStr.length();
            String diffStr = "";
            for (int i = 0; i < diff; i++) diffStr += "0";
            millisStr = diffStr + millisStr;
        }
        return hourStr + ":" + minuteStr + ":" + secondStr + "." + millisStr;
    }
}
