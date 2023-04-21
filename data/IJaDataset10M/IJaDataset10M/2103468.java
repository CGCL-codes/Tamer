package net.androidseminar.eventhandling.test;

import net.androidseminar.eventhandling.LoginActivity;
import net.androidseminar.eventhandling.R;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.widget.Button;

public class LoginActivityTest extends ActivityInstrumentationTestCase2<LoginActivity> {

    private LoginActivity testCandidate;

    public LoginActivityTest() {
        super(LoginActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        testCandidate = getActivity();
    }

    @UiThreadTest
    public void testEvents() {
        getInstrumentation().callActivityOnCreate(testCandidate, null);
        Button b = (Button) testCandidate.findViewById(R.id.button_login);
        assertTrue(b.hasOnClickListeners());
    }
}
