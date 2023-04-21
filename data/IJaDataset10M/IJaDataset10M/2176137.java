package jokeboxjunior.gui.panels.other;

import jokeboxjunior.gui.panels.AbstractPanelController;
import cb_commonobjects.util.StringFuncs;
import jokeboxjunior.gui.windows.CustomEventType;

/**
 *
 * @author B1
 */
public class KeywordSearchPanelController extends AbstractPanelController {

    public static final String PANEL_NAME = "SearchPanel";

    protected KeywordSearchPanel myPanel;

    protected boolean isSearching = false;

    public KeywordSearchPanelController() {
        myPanel = (KeywordSearchPanel) this;
    }

    @Override
    public String getPanelName() {
        return PANEL_NAME;
    }

    @Override
    public void handleCustomEvent(CustomEventType thisType, Object thisData) {
        switch(thisType) {
            case PERFORM_KEYWORD_SEARCH:
                myPanel.jPerformSearch.setText(java.util.ResourceBundle.getBundle("jokeboxjunior/gui/res/other/KeywordSearchPanel").getString("CANCEL"));
                myPanel.jKeyword.setText((String) thisData);
                isSearching = true;
                break;
            case KEYWORD_SEARCH_COMPLETE:
            case CANCEL_SEARCH:
                myPanel.jPerformSearch.setText(java.util.ResourceBundle.getBundle("jokeboxjunior/gui/res/other/KeywordSearchPanel").getString("SEARCH"));
                isSearching = false;
                break;
        }
    }

    protected void performKeywordSearch(String thisKeyword) {
        if (!StringFuncs.isEmpty(thisKeyword)) {
            myParentWindow.fireCustomEvent(CustomEventType.PERFORM_KEYWORD_SEARCH, thisKeyword);
        }
    }

    protected void cancelKeywordSearch() {
        myParentWindow.fireCustomEvent(CustomEventType.CANCEL_SEARCH, null);
    }
}
