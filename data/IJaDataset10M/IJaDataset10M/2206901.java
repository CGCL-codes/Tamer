package android.widget.listview.focus;

import android.widget.listview.ListHorizontalFocusWithinItemWins;
import android.test.ActivityInstrumentationTestCase;
import android.test.suitebuilder.annotation.MediumTest;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

public class ListHorizontalFocusWithinItemWinsTest extends ActivityInstrumentationTestCase<ListHorizontalFocusWithinItemWins> {

    private ListView mListView;

    private Button mTopLeftButton;

    private Button mTopRightButton;

    private Button mBottomMiddleButton;

    public ListHorizontalFocusWithinItemWinsTest() {
        super("com.android.frameworks.coretests", ListHorizontalFocusWithinItemWins.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mListView = getActivity().getListView();
        mTopLeftButton = getActivity().getTopLeftButton();
        mTopRightButton = getActivity().getTopRightButton();
        mBottomMiddleButton = getActivity().getBottomMiddleButton();
    }

    @MediumTest
    public void testPreconditions() {
        assertEquals("list position", 0, mListView.getSelectedItemPosition());
        assertTrue("mTopLeftButton.isFocused()", mTopLeftButton.isFocused());
        assertEquals("global focus search to right from top left is bottom middle", mBottomMiddleButton, FocusFinder.getInstance().findNextFocus(mListView, mTopLeftButton, View.FOCUS_RIGHT));
        assertEquals("global focus search to left from top right is bottom middle", mBottomMiddleButton, FocusFinder.getInstance().findNextFocus(mListView, mTopRightButton, View.FOCUS_LEFT));
    }

    @MediumTest
    public void testOptionWithinItemTrumpsGlobal() {
        sendKeys(KeyEvent.KEYCODE_DPAD_RIGHT);
        assertEquals("list position", 0, mListView.getSelectedItemPosition());
        assertTrue("mTopRightButton.isFocused()", mTopRightButton.isFocused());
        sendKeys(KeyEvent.KEYCODE_DPAD_LEFT);
        assertEquals("list position", 0, mListView.getSelectedItemPosition());
        assertTrue("mTopLeftButton.isFocused()", mTopLeftButton.isFocused());
    }
}
