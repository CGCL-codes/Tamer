package sample.spellcheck.editor;

import sample.spellcheck.stub.SimplifiedSpellCheckCallbackHandler;
import sample.spellcheck.stub.SimplifiedSpellCheckStub.DoSpellingSuggestionsResponse;

public class SimplifiedSpellCheckCallbackHandlerImpl extends SimplifiedSpellCheckCallbackHandler {

    private Observer observer = null;

    private String phrase = null;

    public SimplifiedSpellCheckCallbackHandlerImpl(Observer observer, String phrase) {
        this.observer = observer;
        this.phrase = phrase;
    }

    public void receiveResultdoSpellingSuggestions(DoSpellingSuggestionsResponse param1) {
        String suggestion = param1.get_return();
        if (suggestion == null) {
            observer.update(phrase);
            observer.updateError("No suggestions found for " + phrase);
        } else {
            observer.update(suggestion);
        }
    }

    public void receiveErrordoSpellingSuggestions(Exception e) {
        e.printStackTrace();
        observer.updateError(e.getMessage());
    }
}
