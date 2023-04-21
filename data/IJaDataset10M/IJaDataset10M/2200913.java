package org.unitmetrics.java.ui.views;

public class PackageDependenciesViewTest extends ViewTestCase {

    AbstractDependencyGraphView view;

    protected void setUp() throws Exception {
        super.setUp();
        view = (AbstractDependencyGraphView) getPage().showView("org.unitmetrics.java.ui.packageDependenciesView");
    }

    protected void tearDown() throws Exception {
        getPage().hideView(view);
        super.tearDown();
    }

    public void testShowHideView() {
    }
}
