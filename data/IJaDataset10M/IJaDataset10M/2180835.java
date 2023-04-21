package org.jumpmind.pos.flow.activity;

import org.jumpmind.pos.flow.annotation.FlowActivity;

public class TestActivity {

    boolean actionMethod1Called = false;

    boolean actionMethod2Called = false;

    @FlowActivity(id = "testMeActivity")
    public void actionMethod1() {
        actionMethod1Called = true;
    }

    @FlowActivity(id = "testYouActivity")
    public void actionMethod2() {
        actionMethod2Called = true;
    }

    public boolean isBothActionMethodsCalled() {
        return actionMethod1Called && actionMethod2Called;
    }
}
