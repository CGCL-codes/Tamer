package org.geometerplus.android.fbreader;

import org.geometerplus.zlibrary.text.model.ZLTextModel;
import org.geometerplus.zlibrary.text.view.ZLTextView;
import org.geometerplus.fbreader.fbreader.FBAction;
import org.geometerplus.fbreader.fbreader.FBReaderApp;

class ShowNavigationAction extends FBAction {

    private final FBReader myActivity;

    ShowNavigationAction(FBReader activity, FBReaderApp fbreader) {
        super(fbreader);
        myActivity = activity;
    }

    @Override
    public boolean isVisible() {
        final ZLTextView view = (ZLTextView) Reader.getCurrentView();
        final ZLTextModel textModel = view.getModel();
        return textModel != null && textModel.getParagraphsNumber() != 0;
    }

    public void run() {
        myActivity.navigate();
    }
}
