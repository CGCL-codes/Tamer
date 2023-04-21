package com.iamnota.spellingpractice.Student.state;

import com.iamnota.spellingpractice.Student.Controller;
import com.iamnota.spellingpractice.Student.SpellingView;
import com.iamnota.spellingpractice.Student.WordAudio;

public class Loading extends LoggedIn {

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

    public State loadStoppedSuccessfully(Controller controller) {
        SpellingView spellingView = controller.getSpellingView();
        spellingView.loadStopped();
        spellingView.listenStarted();
        WordAudio wordAudio = controller.getWordAudio();
        wordAudio.playStartWord();
        return Listening.getInstance();
    }

    public State loadStoppedUnsuccessfully(Controller controller) {
        SpellingView spellingView = controller.getSpellingView();
        spellingView.showNoWordsStarted();
        spellingView.displayNoWords();
        return ShowingNoWords.getInstance();
    }
}
