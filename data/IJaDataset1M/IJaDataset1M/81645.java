package org.geometerplus.fbreader.fbreader;

class ClearFindResultsAction extends FBAction {

    ClearFindResultsAction(FBReaderApp fbreader) {
        super(fbreader);
    }

    public void run() {
        Reader.getTextView().clearFindResults();
    }
}
