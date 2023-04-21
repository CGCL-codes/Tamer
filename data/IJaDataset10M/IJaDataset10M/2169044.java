package com.fourspaces.scratch.test.guice;

import com.fourspaces.scratch.mapping.annotation.Path;
import com.fourspaces.scratch.mapping.annotation.PathParam;
import com.fourspaces.scratch.result.Result;
import com.fourspaces.scratch.result.Text;
import com.google.inject.Inject;

public class GuiceInjectedController {

    private TestInterface tester;

    private TestInterface tester2;

    private String state = "foo";

    @Path("/inject")
    public Result one() {
        return new Text(tester.test());
    }

    @Path("/inject2")
    public Result two() {
        return new Text(tester2.test());
    }

    @Path("^/state/(.*)/$")
    public Result setstate(@PathParam(1) String state) {
        this.state = state;
        return new Text(state);
    }

    @Path("/state/")
    public Result state() {
        return new Text(state);
    }

    @Inject
    public void tester(TestInterface test) {
        this.tester = test;
    }

    @Inject
    public void tester2(@Two TestInterface test) {
        this.tester2 = test;
    }
}
