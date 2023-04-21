package com.google.gdt.eclipse.designer.core.model.widgets;

import static com.google.gdt.eclipse.designer.model.widgets.panels.StackLayoutPanelInfo.isVisible;
import com.google.gdt.eclipse.designer.core.model.widgets.generic.GwtGefTest;
import com.google.gdt.eclipse.designer.model.widgets.WidgetInfo;
import com.google.gdt.eclipse.designer.model.widgets.panels.StackLayoutPanelInfo;
import org.eclipse.wb.draw2d.geometry.Point;
import org.eclipse.wb.draw2d.geometry.Rectangle;

/**
 * Test for {@link StackLayoutPanelInfo} in GEF.
 * 
 * @author scheglov_ke
 */
public class StackLayoutPanelGefTest extends GwtGefTest {

    public void _test_exit() throws Exception {
        System.exit(0);
    }

    public void test_CREATE_canvas_empty() throws Exception {
        StackLayoutPanelInfo panel = openJavaInfo("public class Test extends StackLayoutPanel {", "  public Test() {", "    super(Unit.EM);", "  }", "}");
        loadButton();
        canvas.create();
        canvas.moveTo(panel, 0.5, 5).click();
        assertEditor("public class Test extends StackLayoutPanel {", "  public Test() {", "    super(Unit.EM);", "    {", "      Button button = new Button();", "      add(button, new HTML('New Widget'), 2.0);", "    }", "  }", "}");
    }

    public void test_CREATE_canvas_before() throws Exception {
        StackLayoutPanelInfo panel = openJavaInfo("public class Test extends StackLayoutPanel {", "  public Test() {", "    super(Unit.EM);", "    {", "      Button button = new Button();", "      add(button, new HTML('A'), 2.0);", "    }", "  }", "}");
        loadButton();
        canvas.create();
        canvas.moveTo(panel, 0.5, 5).click();
        assertEditor("public class Test extends StackLayoutPanel {", "  public Test() {", "    super(Unit.EM);", "    {", "      Button button = new Button();", "      add(button, new HTML('New Widget'), 2.0);", "    }", "    {", "      Button button = new Button();", "      add(button, new HTML('A'), 2.0);", "    }", "  }", "}");
    }

    public void test_CREATE_canvas_after() throws Exception {
        StackLayoutPanelInfo panel = openJavaInfo("public class Test extends StackLayoutPanel {", "  public Test() {", "    super(Unit.EM);", "    {", "      Button button = new Button();", "      add(button, new HTML('A'), 2.0);", "    }", "  }", "}");
        loadButton();
        canvas.create();
        canvas.moveTo(panel, 0.5, 50).click();
        assertEditor("public class Test extends StackLayoutPanel {", "  public Test() {", "    super(Unit.EM);", "    {", "      Button button = new Button();", "      add(button, new HTML('A'), 2.0);", "    }", "    {", "      Button button = new Button();", "      add(button, new HTML('New Widget'), 2.0);", "    }", "  }", "}");
    }

    public void test_CREATE_tree() throws Exception {
        StackLayoutPanelInfo panel = openJavaInfo("public class Test extends StackLayoutPanel {", "  public Test() {", "    super(Unit.EM);", "  }", "}");
        loadButton();
        tree.moveOn(panel).click();
        assertEditor("public class Test extends StackLayoutPanel {", "  public Test() {", "    super(Unit.EM);", "    {", "      Button button = new Button();", "      add(button, new HTML('New Widget'), 2.0);", "    }", "  }", "}");
    }

    public void test_PASTE() throws Exception {
        StackLayoutPanelInfo panel = openJavaInfo("public class Test extends StackLayoutPanel {", "  public Test() {", "    super(Unit.EM);", "    {", "      Button myButton = new Button();", "      add(myButton, new HTML('header'), 1.5);", "    }", "  }", "}");
        {
            WidgetInfo button = getJavaInfoByName("myButton");
            canvas.select(button);
            getCopyAction().run();
        }
        getPasteAction().run();
        canvas.moveTo(panel, 0.5, 5).click();
        assertEditor("public class Test extends StackLayoutPanel {", "  public Test() {", "    super(Unit.EM);", "    {", "      Button button = new Button();", "      add(button, new HTML('New Widget'), 2.0);", "    }", "    {", "      Button myButton = new Button();", "      add(myButton, new HTML('header'), 1.5);", "    }", "  }", "}");
        assertHierarchy("{this: com.google.gwt.user.client.ui.StackLayoutPanel} {this} {/add(myButton, new HTML('header'), 1.5)/ /add(button, new HTML('New Widget'), 2.0)/}", "  {new: com.google.gwt.user.client.ui.Button} {local-unique: button} {/new Button()/ /add(button, new HTML('New Widget'), 2.0)/}", "    {new: com.google.gwt.user.client.ui.HTML} {empty} {/add(button, new HTML('New Widget'), 2.0)/}", "  {new: com.google.gwt.user.client.ui.Button} {local-unique: myButton} {/new Button()/ /add(myButton, new HTML('header'), 1.5)/}", "    {new: com.google.gwt.user.client.ui.HTML} {empty} {/add(myButton, new HTML('header'), 1.5)/}");
    }

    public void test_MOVE_widget() throws Exception {
        StackLayoutPanelInfo panel = openJavaInfo("public class Test extends StackLayoutPanel {", "  public Test() {", "    super(Unit.EM);", "    {", "      Button button_1 = new Button();", "      add(button_1, new HTML('A'), 2.0);", "    }", "    {", "      Button button_2 = new Button();", "      add(button_2, new HTML('B'), 2.0);", "    }", "  }", "}");
        WidgetInfo button_2 = getJavaInfoByName("button_2");
        tree.select(button_2);
        canvas.beginDrag(button_2).dragTo(panel, 0.5, 5).endDrag();
        assertEditor("public class Test extends StackLayoutPanel {", "  public Test() {", "    super(Unit.EM);", "    {", "      Button button_2 = new Button();", "      add(button_2, new HTML('B'), 2.0);", "    }", "    {", "      Button button_1 = new Button();", "      add(button_1, new HTML('A'), 2.0);", "    }", "  }", "}");
    }

    public void test_MOVE_header() throws Exception {
        StackLayoutPanelInfo panel = openJavaInfo("public class Test extends StackLayoutPanel {", "  public Test() {", "    super(Unit.EM);", "    {", "      Button button_1 = new Button();", "      add(button_1, new HTML('A'), 2.0);", "    }", "    {", "      Button button_2 = new Button();", "      add(button_2, new HTML('B'), 2.0);", "    }", "  }", "}");
        {
            Rectangle bounds = panel.getWidgetHandles().get(1).getBounds();
            Point handleCenter = bounds.getCenter();
            canvas.moveTo(panel, handleCenter.x, handleCenter.y);
            canvas.beginDrag().dragTo(panel, 5, 0).endDrag();
        }
        assertEditor("public class Test extends StackLayoutPanel {", "  public Test() {", "    super(Unit.EM);", "    {", "      Button button_2 = new Button();", "      add(button_2, new HTML('B'), 2.0);", "    }", "    {", "      Button button_1 = new Button();", "      add(button_1, new HTML('A'), 2.0);", "    }", "  }", "}");
    }

    public void test_ADD() throws Exception {
        openJavaInfo("public class Test implements EntryPoint {", "  public void onModuleLoad() {", "    RootLayoutPanel rootPanel = RootLayoutPanel.get();", "    {", "      StackLayoutPanel panel = new StackLayoutPanel(Unit.CM);", "      rootPanel.add(panel);", "      rootPanel.setWidgetLeftRight(panel, 10, Unit.PX, 10, Unit.PX);", "      rootPanel.setWidgetTopHeight(panel, 10, Unit.PX, 200, Unit.PX);", "    }", "    {", "      Button button = new Button('button');", "      rootPanel.add(button);", "      rootPanel.setWidgetLeftWidth(button, 10, Unit.PX, 150, Unit.PX);", "      rootPanel.setWidgetTopHeight(button, 250, Unit.PX, 50, Unit.PX);", "    }", "  }", "}");
        StackLayoutPanelInfo panel = getJavaInfoByName("panel");
        WidgetInfo button = getJavaInfoByName("button");
        canvas.beginDrag(button).dragTo(panel, 0.5, 5).endDrag();
        assertEditor("public class Test implements EntryPoint {", "  public void onModuleLoad() {", "    RootLayoutPanel rootPanel = RootLayoutPanel.get();", "    {", "      StackLayoutPanel panel = new StackLayoutPanel(Unit.CM);", "      {", "        Button button = new Button('button');", "        panel.add(button, new HTML('New Widget'), 2.0);", "      }", "      rootPanel.add(panel);", "      rootPanel.setWidgetLeftRight(panel, 10, Unit.PX, 10, Unit.PX);", "      rootPanel.setWidgetTopHeight(panel, 10, Unit.PX, 200, Unit.PX);", "    }", "  }", "}");
    }

    public void test_canvas_doubleClickHandle() throws Exception {
        StackLayoutPanelInfo panel = openJavaInfo("public class Test extends StackLayoutPanel {", "  public Test() {", "    super(Unit.EM);", "    {", "      Button button_1 = new Button();", "      add(button_1, new HTML('A'), 2.0);", "    }", "    {", "      Button button_2 = new Button();", "      add(button_2, new HTML('B'), 2.0);", "    }", "  }", "}");
        WidgetInfo button_1 = getJavaInfoByName("button_1");
        WidgetInfo button_2 = getJavaInfoByName("button_2");
        assertTrue(isVisible(button_1));
        assertFalse(isVisible(button_2));
        {
            Rectangle bounds = panel.getWidgetHandles().get(1).getBounds();
            Point handleCenter = bounds.getCenter();
            canvas.moveTo(panel, handleCenter.x, handleCenter.y).doubleClick();
        }
        assertFalse(isVisible(button_1));
        assertTrue(isVisible(button_2));
    }

    public void test_canvas_directEditHandle() throws Exception {
        StackLayoutPanelInfo panel = openJavaInfo("public class Test extends StackLayoutPanel {", "  public Test() {", "    super(Unit.EM);", "    {", "      Button button = new Button();", "      add(button, new HTML('AAA'), 2.0);", "    }", "  }", "}");
        {
            Rectangle bounds = panel.getWidgetHandles().get(0).getBounds();
            Point handleCenter = bounds.getCenter();
            canvas.moveTo(panel, handleCenter.x, handleCenter.y).click();
        }
        canvas.performDirectEdit("123");
        assertEditor("public class Test extends StackLayoutPanel {", "  public Test() {", "    super(Unit.EM);", "    {", "      Button button = new Button();", "      add(button, new HTML('123'), 2.0);", "    }", "  }", "}");
    }
}
