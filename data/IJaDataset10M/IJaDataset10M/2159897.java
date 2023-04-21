package com.google.code.appengine.awt;

import com.google.code.appengine.awt.Container;
import com.google.code.appengine.awt.Dimension;
import com.google.code.appengine.awt.FlowLayout;
import junit.framework.TestCase;

public class FlowLayoutRTest extends TestCase {

    Container emptyContainer;

    Dimension defSize;

    FlowLayout layout;

    public static void main(String[] args) {
        junit.textui.TestRunner.run(FlowLayoutRTest.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        emptyContainer = new Container();
        defSize = new Dimension(10, 10);
        layout = new FlowLayout();
    }

    public final void testMinimumLayoutSize() {
        assertEquals(defSize, layout.minimumLayoutSize(emptyContainer));
    }

    public final void testPreferredLayoutSize() {
        assertEquals(defSize, layout.preferredLayoutSize(emptyContainer));
    }

    public final void testLayoutContainer() {
        layout.layoutContainer(emptyContainer);
    }

    public void testSetAlignment() {
        layout.setAlignment(-1);
    }

    public void testRemoveLayoutComponent() {
        layout.removeLayoutComponent(null);
    }
}
