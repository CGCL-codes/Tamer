package com.appspot.bambugame.server.data;

import javax.persistence.Id;

public class HangmanQuestionSuggestion {

    @Id
    public long ID;

    public String SENTENCE;

    public String HINT;

    public String EXTRAS;

    public long SUGGESTER_ID;

    public String SUGGESTER_NAME;

    public long PLURK_ID;

    public HangmanQuestionSuggestion() {
    }

    public HangmanQuestionSuggestion(String pSentence, String pHint, String pExtras, long pSuggesterID, String pSuggesterName, long pPlurkID) {
        SENTENCE = pSentence;
        HINT = pHint;
        EXTRAS = pExtras;
        SUGGESTER_ID = pSuggesterID;
        SUGGESTER_NAME = pSuggesterName;
        PLURK_ID = pPlurkID;
        ID = SENTENCE.hashCode();
    }
}
