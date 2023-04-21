package net.sf.planofattack.infonode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.sf.planofattack.swing.event.ActionRegistry;
import org.easymock.EasyMock;
import test.util.EasyMockTestCase;
import test.util.TestMother;

public class ClosingPlanHandlerTest extends EasyMockTestCase {

    private ClosingPlanHandler handler;

    private ViewManager viewManager;

    private ActionRegistry actionRegistry;

    public void setUp() {
        viewManager = mock(ViewManager.class);
        actionRegistry = mock(ActionRegistry.class);
        handler = new ClosingPlanHandler();
        handler.setViewManager(viewManager);
        handler.setActionRegistry(actionRegistry);
    }

    public void test_windowClosed_LastWindowClosed() {
        EasyMock.expect(viewManager.getAllPlanViews()).andReturn(new ArrayList<PlanView>());
        actionRegistry.disable(ActionRegistry.SAVE_PLAN);
        actionRegistry.disable(ActionRegistry.SELECT_ALL);
        actionRegistry.disable(ActionRegistry.DELETE);
        actionRegistry.disable(ActionRegistry.CUT);
        actionRegistry.disable(ActionRegistry.COPY);
        actionRegistry.disable(ActionRegistry.PASTE);
        actionRegistry.disable(ActionRegistry.REDO);
        actionRegistry.disable(ActionRegistry.UNDO);
        actionRegistry.disable(ActionRegistry.CHANGE_MAP);
        replayMocks();
        handler.windowClosed(null);
    }

    public void test_windowClosed_NotLastWindowClosed() {
        List<PlanView> views = Arrays.asList(TestMother.createPlanView());
        EasyMock.expect(viewManager.getAllPlanViews()).andReturn(views);
        replayMocks();
        handler.windowClosed(null);
    }
}
