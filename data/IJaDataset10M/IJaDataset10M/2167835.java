package com.iamnota.spellingpractice.Teacher.state.word;

import com.iamnota.spellingpractice.Teacher.WordController;
import com.iamnota.spellingpractice.Teacher.WordView;

public class Loading extends State {

    private static Loading me;

    protected Loading() {
        ;
    }

    public static synchronized Loading getInstance() {
        if (Loading.me == null) {
            Loading.me = new Loading();
        }
        return Loading.me;
    }

    public String getName() {
        return "Loading";
    }

    public State loadDoneGood(WordController wordController) {
        WordView wordView = wordController.getWordView();
        wordView.opened();
        return IdleNotModified.getInstance();
    }

    public State loadDoneBad(WordController wordController) {
        return Empty.getInstance();
    }
}
