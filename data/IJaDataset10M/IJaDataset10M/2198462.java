package testOpen.testGps.testGopens.testControler;

import open.gps.gopens.Gopens;
import open.gps.gopens.render.view.RenderView;
import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.test.InstrumentationTestCase;
import android.test.TouchUtils;

public class TestRenderControler extends InstrumentationTestCase {

    private RenderView view;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Instrumentation instrumentation = getInstrumentation();
        Instrumentation.ActivityMonitor monitor = instrumentation.addMonitor(Gopens.class.getName(), null, false);
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClass(instrumentation.getTargetContext(), Gopens.class);
        instrumentation.startActivitySync(intent);
        Activity currentActivity = getInstrumentation().waitForMonitorWithTimeout(monitor, 5);
        this.view = ((Gopens) currentActivity).getView();
    }

    public void testRenderClickControllerOnClick() {
        TouchUtils.clickView(this, this.view);
        assertEquals(true, this.view.getmZoomController().isVisible());
    }
}
