package android.view;

import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.MediumTest;
import android.view.View;
import android.view.MutateDrawable;

public class MutateDrawableTest extends ActivityInstrumentationTestCase2<MutateDrawable> {

    private View mFirstButton;

    private View mSecondButton;

    public MutateDrawableTest() {
        super("com.android.frameworks.coretests", MutateDrawable.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mFirstButton = getActivity().findViewById(com.android.frameworks.coretests.R.id.a);
        mSecondButton = getActivity().findViewById(com.android.frameworks.coretests.R.id.b);
    }

    @MediumTest
    public void testSetUpConditions() throws Exception {
        assertNotNull(mFirstButton);
        assertNotNull(mSecondButton);
        assertNotSame(mFirstButton.getBackground(), mSecondButton.getBackground());
    }

    @MediumTest
    public void testDrawableCanMutate() throws Exception {
        assertNotSame(mFirstButton.getBackground().getConstantState(), mSecondButton.getBackground().getConstantState());
    }
}
