package com.iamnota.spellingpractice.Teacher.appengine;

import com.iamnota.spellingpractice.Teacher.*;

public class WordAudioImpl implements WordAudio {

    private WordController wordController;

    private WordModel wordModel;

    public WordAudioImpl(WordController wordController, WordModel wordModel) {
        this.wordController = wordController;
        this.wordModel = wordModel;
    }

    @Override
    public void playStartWord() {
    }

    @Override
    public void playStopWord() {
    }

    @Override
    public void recordStartWord() {
    }

    @Override
    public void recordStopWord() {
    }
}
