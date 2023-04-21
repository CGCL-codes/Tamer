package com.google.gdt.eclipse.designer.gxt.model.widgets;

import com.google.gdt.eclipse.designer.gxt.model.GxtModelTest;
import com.google.gdt.eclipse.designer.model.widgets.WidgetInfo;
import org.eclipse.wb.internal.core.model.generic.FlowContainer;
import org.eclipse.wb.internal.core.model.generic.FlowContainerFactory;

/**
 * Test for {@link VerticalPanelInfo}.
 * 
 * @author scheglov_ke
 */
public class VerticalPanelTest extends GxtModelTest {

    public void _test_exit() throws Exception {
        System.exit(0);
    }

    public void test_parse_virtualLayoutData() throws Exception {
        VerticalPanelInfo panel = parseJavaInfo("public class Test extends VerticalPanel {", "  public Test() {", "    {", "      Button button = new Button();", "      add(button);", "    }", "  }", "}");
        assertHierarchy("{this: com.extjs.gxt.ui.client.widget.VerticalPanel} {this} {/add(button)/}", "  {new: com.extjs.gxt.ui.client.widget.button.Button} {local-unique: button} {/new Button()/ /add(button)/}", "    {virtual-layout_data: com.extjs.gxt.ui.client.widget.layout.TableData} {virtual-layout-data} {}");
        assertFalse(panel.hasLayout());
        WidgetInfo button = panel.getWidgets().get(0);
        assertNotNull(button.getPropertyByTitle("LayoutData"));
    }

    public void test_parse_explicitLayoutData() throws Exception {
        VerticalPanelInfo panel = parseJavaInfo("public class Test extends VerticalPanel {", "  public Test() {", "    {", "      Button button = new Button();", "      add(button, new TableData());", "    }", "  }", "}");
        assertHierarchy("{this: com.extjs.gxt.ui.client.widget.VerticalPanel} {this} {/add(button, new TableData())/}", "  {new: com.extjs.gxt.ui.client.widget.button.Button} {local-unique: button} {/new Button()/ /add(button, new TableData())/}", "    {new: com.extjs.gxt.ui.client.widget.layout.TableData} {empty} {/add(button, new TableData())/}");
        WidgetInfo button = panel.getWidgets().get(0);
        assertNotNull(button.getPropertyByTitle("LayoutData"));
    }

    public void test_MOVE_out() throws Exception {
        LayoutContainerInfo container = parseJavaInfo("public class Test extends LayoutContainer {", "  public Test() {", "    {", "      VerticalPanel panel = new VerticalPanel();", "      {", "        Button button = new Button();", "        panel.add(button, new TableData());", "      }", "      add(panel);", "    }", "  }", "}");
        assertHierarchy("{this: com.extjs.gxt.ui.client.widget.LayoutContainer} {this} {/add(panel)/}", "  {implicit-layout: default} {implicit-layout} {}", "  {new: com.extjs.gxt.ui.client.widget.VerticalPanel} {local-unique: panel} {/new VerticalPanel()/ /panel.add(button, new TableData())/ /add(panel)/}", "    {new: com.extjs.gxt.ui.client.widget.button.Button} {local-unique: button} {/new Button()/ /panel.add(button, new TableData())/}", "      {new: com.extjs.gxt.ui.client.widget.layout.TableData} {empty} {/panel.add(button, new TableData())/}");
        VerticalPanelInfo panel = (VerticalPanelInfo) container.getWidgets().get(0);
        WidgetInfo button = panel.getWidgets().get(0);
        container.getLayout().command_MOVE(button, null);
        assertEditor("public class Test extends LayoutContainer {", "  public Test() {", "    {", "      VerticalPanel panel = new VerticalPanel();", "      add(panel);", "    }", "    {", "      Button button = new Button();", "      add(button);", "    }", "  }", "}");
        assertHierarchy("{this: com.extjs.gxt.ui.client.widget.LayoutContainer} {this} {/add(panel)/ /add(button)/}", "  {implicit-layout: default} {implicit-layout} {}", "  {new: com.extjs.gxt.ui.client.widget.VerticalPanel} {local-unique: panel} {/new VerticalPanel()/ /add(panel)/}", "  {new: com.extjs.gxt.ui.client.widget.button.Button} {local-unique: button} {/new Button()/ /add(button)/}");
    }

    public void test_command_CREATE() throws Exception {
        VerticalPanelInfo panel = parseJavaInfo("// filler filler filler", "public class Test extends VerticalPanel {", "  public Test() {", "  }", "}");
        panel.refresh();
        ComponentInfo newButton = createButton();
        FlowContainer flowContainer = new FlowContainerFactory(panel, true).get().get(0);
        flowContainer.command_CREATE(newButton, null);
        assertEditor("// filler filler filler", "public class Test extends VerticalPanel {", "  public Test() {", "    {", "      Button button = new Button();", "      add(button);", "    }", "  }", "}");
        assertHierarchy("{this: com.extjs.gxt.ui.client.widget.VerticalPanel} {this} {/add(button)/}", "  {new: com.extjs.gxt.ui.client.widget.button.Button empty} {local-unique: button} {/new Button()/ /add(button)/}", "    {virtual-layout_data: com.extjs.gxt.ui.client.widget.layout.TableData} {virtual-layout-data} {}");
    }
}
