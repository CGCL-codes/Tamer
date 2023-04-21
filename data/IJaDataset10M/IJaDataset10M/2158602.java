package com.iamnota.spellingpractice.Teacher.state.word;

import com.iamnota.spellingpractice.Teacher.WordController;
import com.iamnota.spellingpractice.Teacher.WordAudio;
import com.iamnota.spellingpractice.Teacher.WordView;

public class IdleModified extends Idle {

    private static IdleModified me;

    protected IdleModified() {
        ;
    }

    public static synchronized IdleModified getInstance() {
        if (IdleModified.me == null) {
            IdleModified.me = new IdleModified();
        }
        return IdleModified.me;
    }

    public String getName() {
        return "IdleModified";
    }

    public State closeView(WordController wordController) {
        WordView wordView = wordController.getWordView();
        if (wordView.isAskSaveChanges()) {
            wordView.updateModel();
            return SavingCloseView.getInstance();
        } else {
            return super.unload(wordController).closeView(wordController);
        }
    }

    public State newWord(WordController wordController) {
        WordView wordView = wordController.getWordView();
        if (wordView.isAskSaveChanges()) {
            wordView.updateModel();
            return SavingNewWord.getInstance();
        } else {
            return super.unload(wordController).newWord(wordController);
        }
    }

    public State load(WordController wordController, String id) {
        WordView wordView = wordController.getWordView();
        if (wordView.isAskSaveChanges()) {
            wordView.updateModel();
            return SavingLoad.getInstance(id);
        } else {
            return super.load(wordController, id);
        }
    }

    public State unload(WordController wordController) {
        WordView wordView = wordController.getWordView();
        if (wordView.isAskSaveChanges()) {
            wordView.updateModel();
            return SavingUnload.getInstance();
        } else {
            return super.unload(wordController);
        }
    }

    public State playStartWord(WordController wordController) {
        WordAudio wordAudio = wordController.getWordAudio();
        wordAudio.playStartWord();
        return PlayingWordModified.getInstance();
    }
}
