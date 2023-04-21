package net.javacrumbs.fluentapi2;

public class Mock {

    public RequestExpectations whenConnecting() {
        return new MockInternal();
    }

    public void verify() {
    }
}
