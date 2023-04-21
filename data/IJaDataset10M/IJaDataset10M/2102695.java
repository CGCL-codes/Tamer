package java.awt.event;

import java.awt.Button;
import java.awt.Container;
import junit.framework.TestCase;

public class HierarchyEventTest extends TestCase {

    public final void testHierarchyEventComponentintComponentContainer() {
        Button button = new Button();
        Container container = new Container();
        HierarchyEvent event = new HierarchyEvent(button, HierarchyEvent.ANCESTOR_RESIZED, button, container);
        assertEquals(event.getSource(), button);
        assertEquals(event.getID(), HierarchyEvent.ANCESTOR_RESIZED);
        assertEquals(event.getComponent(), button);
        assertEquals(event.getChangeFlags(), 0);
        assertEquals(event.getChanged(), button);
        assertEquals(event.getChangedParent(), container);
    }

    public final void testHierarchyEventComponentintComponentContainerlong() {
        Button button = new Button();
        Container container = new Container();
        HierarchyEvent event = new HierarchyEvent(button, HierarchyEvent.HIERARCHY_CHANGED, button, container, HierarchyEvent.PARENT_CHANGED | HierarchyEvent.DISPLAYABILITY_CHANGED);
        assertEquals(event.getSource(), button);
        assertEquals(event.getID(), HierarchyEvent.HIERARCHY_CHANGED);
        assertEquals(event.getComponent(), button);
        assertEquals(event.getChangeFlags(), HierarchyEvent.PARENT_CHANGED | HierarchyEvent.DISPLAYABILITY_CHANGED);
        assertEquals(event.getChanged(), button);
        assertEquals(event.getChangedParent(), container);
    }
}
