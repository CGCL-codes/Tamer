package consciouscode.seedling;

import junit.framework.Assert;

class MockBranchChangeListener implements BranchChangeListener {

    public enum EventType {

        INSTALL, UNINSTALL
    }

    private BranchChangeEvent myEvent;

    private EventType myEventType;

    public void childInstalled(BranchChangeEvent event) {
        assert myEvent == null;
        myEvent = event;
        myEventType = EventType.INSTALL;
        Assert.assertNotNull(event.getNode());
    }

    public void childUninstalled(BranchChangeEvent event) {
        assert myEvent == null;
        myEvent = event;
        myEventType = EventType.UNINSTALL;
        Assert.assertNotNull(event.getNode());
    }

    public void assertInstalled(BranchNode branch, String nodeName) {
        assertInstalled(branch, nodeName, null);
    }

    public void assertInstalled(BranchNode branch, String nodeName, Object node) {
        Assert.assertNotNull("expected event but none was heard", myEvent);
        Assert.assertSame(EventType.INSTALL, myEventType);
        Assert.assertSame(branch, myEvent.getSource());
        Assert.assertSame(branch, myEvent.getBranch());
        Assert.assertEquals(nodeName, myEvent.getNodeName());
        if (node != null) {
            Assert.assertSame(node, myEvent.getNode());
        }
    }

    public void assertUninstalled(BranchNode branch, String nodeName, Object node) {
        Assert.assertNotNull("expected uninstall event but none was heard", myEvent);
        Assert.assertSame(EventType.UNINSTALL, myEventType);
        Assert.assertSame(branch, myEvent.getSource());
        Assert.assertSame(branch, myEvent.getBranch());
        Assert.assertEquals(nodeName, myEvent.getNodeName());
        Assert.assertSame(node, myEvent.getNode());
    }

    public BranchChangeEvent getEvent() {
        return myEvent;
    }

    public void clear() {
        myEvent = null;
        myEventType = null;
    }
}
