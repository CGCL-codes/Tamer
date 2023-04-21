package net.sf.planofattack.swing.event;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import junitx.util.PrivateAccessor;
import net.sf.planofattack.Plan;
import net.sf.planofattack.Tool;
import net.sf.planofattack.drawable.Drawable;
import net.sf.planofattack.drawable.DrawableFactory;
import net.sf.planofattack.mvc.PlanController;
import net.sf.planofattack.mvc.PlanControllerImpl;
import net.sf.planofattack.mvc.PlanModel;
import org.easymock.EasyMock;
import test.util.EasyMockTestCase;
import test.util.TestMother;

public class PlanPanelInputAdapterTest extends EasyMockTestCase {

    private PlanPanelInputAdapter adapter;

    private PlanModel plan;

    private PlanController controller;

    private DrawableFactory drawableFactory;

    @Override
    public void setUp() {
        plan = new PlanModel(new Plan());
        controller = mock(PlanController.class);
        drawableFactory = mock(DrawableFactory.class);
        adapter = new PlanPanelInputAdapter(drawableFactory, controller);
    }

    private void handleKeyEvent(KeyEvent event) {
        adapter.keyPressed(event);
        adapter.keyReleased(event);
        adapter.keyTyped(event);
    }

    public void test_isLeftMouseButton() {
        assertTrue(isLeftMouseButton(adapter, TestMother.createLeftMouseButtonClicked(TestMother.createPlanPanel(new PlanControllerImpl()), 0, 0, 0)));
        assertTrue(isLeftMouseButton(adapter, TestMother.createLeftMouseButtonDragged(TestMother.createPlanPanel(new PlanControllerImpl()), 0, 0)));
        assertFalse(isLeftMouseButton(adapter, TestMother.createRightMouseButtonClicked(TestMother.createPlanPanel(new PlanControllerImpl()), 0, 0, 0)));
    }

    private boolean isLeftMouseButton(PlanPanelInputAdapter adapter, MouseEvent event) {
        try {
            return (Boolean) PrivateAccessor.invoke(adapter, "isLeftMouseButton", new Class[] { MouseEvent.class }, new Object[] { event });
        } catch (Throwable err) {
            throw new RuntimeException(err);
        }
    }

    public void test_mouseDragged_NullTool() {
        adapter.setTool(null);
        EasyMock.expect(controller.isDrawableSelected()).andReturn(false);
        replayMocks();
        adapter.mouseDragged(TestMother.createLeftMouseButtonDragged(TestMother.createPlanPanel(new PlanControllerImpl()), 5, 5));
    }

    public void test_mouseDragged_OVAL_DrawableNotSelected() {
        Drawable newDrawable = TestMother.createRectangle(5, 5, 1, 1, true);
        EasyMock.expect(drawableFactory.createDrawable(Tool.OVAL, 5, 5)).andReturn(newDrawable);
        EasyMock.expect(controller.isDrawableSelected()).andReturn(false);
        controller.addDrawable(newDrawable);
        replayMocks();
        adapter.setTool(Tool.OVAL);
        adapter.mouseDragged(TestMother.createLeftMouseButtonDragged(TestMother.createPlanPanel(new PlanControllerImpl()), 5, 5));
    }

    public void test_mouseDragged_OVAL_DrawableNotSelected_RightMouseButton() {
        adapter.setTool(Tool.OVAL);
        EasyMock.expect(controller.isDrawableSelected()).andReturn(false);
        replayMocks();
        adapter.mouseDragged(TestMother.createRightMouseButtonDragged(TestMother.createPlanPanel(new PlanControllerImpl()), 5, 5));
    }

    public void test_mouseDragged_SELECT_DrawableNotSelected() {
        adapter.setTool(Tool.SELECT);
        EasyMock.expect(controller.isDrawableSelected()).andReturn(false);
        replayMocks();
        adapter.mouseDragged(TestMother.createLeftMouseButtonDragged(TestMother.createPlanPanel(new PlanControllerImpl()), 5, 5));
    }

    public void test_mouseDragged_SELECT_DrawableSelected() {
        adapter.setTool(Tool.SELECT);
        EasyMock.expect(controller.isDrawableSelected()).andReturn(true);
        controller.updateDrawable(5, 5);
        replayMocks();
        adapter.mouseDragged(TestMother.createLeftMouseButtonDragged(TestMother.createPlanPanel(new PlanControllerImpl()), 5, 5));
    }

    public void test_mouseDragged_SELECT_DrawableSelected_RightMouseButton() {
        adapter.setTool(Tool.SELECT);
        EasyMock.expect(controller.isDrawableSelected()).andReturn(true);
        controller.unselectAllDrawables();
        replayMocks();
        adapter.mouseDragged(TestMother.createRightMouseButtonDragged(TestMother.createPlanPanel(new PlanControllerImpl()), 5, 5));
    }

    public void test_mouseReleased() {
        controller.stopResizing();
        replayMocks();
        adapter.mouseReleased(null);
    }

    public void test_mousePressed_RECTANGLE_drawableNotSelected() {
        adapter.setTool(Tool.RECTANGLE);
        controller.unselectAllDrawables();
        replayMocks();
        adapter.mousePressed(TestMother.createLeftMouseButtonClicked(TestMother.createPlanPanel(new PlanControllerImpl()), 10, 10, 1));
    }

    public void test_mousePressed_OVAL_drawableNotSelected() {
        adapter.setTool(Tool.OVAL);
        controller.unselectAllDrawables();
        replayMocks();
        adapter.mousePressed(TestMother.createLeftMouseButtonClicked(TestMother.createPlanPanel(new PlanControllerImpl()), 10, 10, 1));
    }

    public void test_mousePressed_SELECT_drawableNotSelected() {
        adapter.setTool(Tool.SELECT);
        EasyMock.expect(controller.isDrawableSelected()).andReturn(false);
        controller.clickOnAll(10, 10);
        replayMocks();
        adapter.mousePressed(TestMother.createLeftMouseButtonClicked(TestMother.createPlanPanel(new PlanControllerImpl()), 10, 10, 1));
    }

    public void test_mousePressed_SELECT_drawableAlreadySelected() {
        adapter.setTool(Tool.SELECT);
        EasyMock.expect(controller.isDrawableSelected()).andReturn(true);
        controller.clickOnSelected(10, 10);
        replayMocks();
        adapter.mousePressed(TestMother.createLeftMouseButtonClicked(TestMother.createPlanPanel(new PlanControllerImpl()), 10, 10, 1));
    }

    public void test_mousePressed_RECTANGLE_drawableAlreadySelected() {
        adapter.setTool(Tool.RECTANGLE);
        controller.unselectAllDrawables();
        replayMocks();
        adapter.mousePressed(TestMother.createLeftMouseButtonClicked(TestMother.createPlanPanel(new PlanControllerImpl()), 10, 10, 1));
    }

    public void test_mousePressed_OVAL_drawableAlreadySelected() {
        adapter.setTool(Tool.OVAL);
        controller.unselectAllDrawables();
        replayMocks();
        adapter.mousePressed(TestMother.createLeftMouseButtonClicked(TestMother.createPlanPanel(new PlanControllerImpl()), 10, 10, 1));
    }

    public void test_mouseClicked_RECTANGLE() {
        adapter.setTool(Tool.RECTANGLE);
        controller.unselectAllDrawables();
        replayMocks();
        adapter.mouseClicked(TestMother.createLeftMouseButtonClicked(TestMother.createPlanPanel(new PlanControllerImpl()), 5, 5, 1));
    }

    public void test_mouseClicked_OVAL() {
        adapter.setTool(Tool.OVAL);
        controller.unselectAllDrawables();
        replayMocks();
        adapter.mouseClicked(TestMother.createLeftMouseButtonClicked(TestMother.createPlanPanel(new PlanControllerImpl()), 5, 5, 1));
    }

    public void test_mouseClicked_SELECT() {
        adapter.setTool(Tool.SELECT);
        controller.unselectAllDrawables();
        controller.clickOnAll(5, 5);
        replayMocks();
        adapter.mouseClicked(TestMother.createLeftMouseButtonClicked(TestMother.createPlanPanel(new PlanControllerImpl()), 5, 5, 1));
    }

    public void test_mouseClicked_NoSelection() {
        adapter.setTool(Tool.SELECT);
        controller.unselectAllDrawables();
        controller.clickOnAll(100, 100);
        replayMocks();
        adapter.mouseClicked(TestMother.createLeftMouseButtonClicked(TestMother.createPlanPanel(new PlanControllerImpl()), 100, 100, 1));
    }

    public void test_mouseClicked_RightMouseButton() {
        adapter.setTool(Tool.SELECT);
        replayMocks();
        adapter.mouseClicked(TestMother.createRightMouseButtonClicked(TestMother.createPlanPanel(new PlanControllerImpl()), 100, 100, 1));
    }
}
