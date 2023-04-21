package android.view.cts;

import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetClass;
import dalvik.annotation.TestTargetNew;
import dalvik.annotation.TestTargets;
import android.os.Parcel;
import android.test.InstrumentationTestCase;
import android.view.View.BaseSavedState;

@TestTargetClass(BaseSavedState.class)
public class View_BaseSavedStateTest extends InstrumentationTestCase {

    @TestTargets({ @TestTargetNew(level = TestLevel.COMPLETE, method = "BaseSavedState", args = { android.os.Parcel.class }), @TestTargetNew(level = TestLevel.COMPLETE, method = "BaseSavedState", args = { android.os.Parcelable.class }) })
    public void testConstructors() {
        BaseSavedState state = new BaseSavedState(Parcel.obtain());
        new BaseSavedState(state);
    }
}
