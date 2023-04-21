package com.google.gdt.eclipse.designer.gxt.model.layout;

import com.google.gdt.eclipse.designer.gxt.model.GxtModelTest;
import com.google.gdt.eclipse.designer.gxt.model.widgets.FieldInfo;
import com.google.gdt.eclipse.designer.gxt.model.widgets.LayoutContainerInfo;
import com.google.gdt.eclipse.designer.model.widgets.WidgetInfo;
import org.eclipse.wb.internal.core.model.generic.FlowContainer;
import org.eclipse.wb.internal.core.model.generic.FlowContainerFactory;

/**
 * Test for {@link FormLayoutInfo}.
 * 
 * @author scheglov_ke
 */
public class FormLayoutTest extends GxtModelTest {

    public void _test_exit() throws Exception {
        System.exit(0);
    }

    public void test_setLayout() throws Exception {
        LayoutContainerInfo container = parseJavaInfo("// filler filler filler", "public class Test extends LayoutContainer {", "  public Test() {", "  }", "}");
        container.refresh();
        FormLayoutInfo layout = createJavaInfo("com.extjs.gxt.ui.client.widget.layout.FormLayout");
        container.setLayout(layout);
        assertEditor("// filler filler filler", "public class Test extends LayoutContainer {", "  public Test() {", "    setLayout(new FormLayout());", "  }", "}");
        assertHierarchy("{this: com.extjs.gxt.ui.client.widget.LayoutContainer} {this} {/setLayout(new FormLayout())/}", "  {new: com.extjs.gxt.ui.client.widget.layout.FormLayout} {empty} {/setLayout(new FormLayout())/}");
        assertSame(layout, container.getLayout());
    }

    public void test_parse() throws Exception {
        LayoutContainerInfo container = parseJavaInfo("public class Test extends LayoutContainer {", "  public Test() {", "    setLayout(new FormLayout());", "    {", "      LabelField labelField = new LabelField();", "      add(labelField);", "    }", "  }", "}");
        assertHierarchy("{this: com.extjs.gxt.ui.client.widget.LayoutContainer} {this} {/setLayout(new FormLayout())/ /add(labelField)/}", "  {new: com.extjs.gxt.ui.client.widget.layout.FormLayout} {empty} {/setLayout(new FormLayout())/}", "  {new: com.extjs.gxt.ui.client.widget.form.LabelField} {local-unique: labelField} {/new LabelField()/ /add(labelField)/}", "    {virtual-layout_data: com.extjs.gxt.ui.client.widget.layout.FormData} {virtual-layout-data} {}");
        container.refresh();
        WidgetInfo field = container.getWidgets().get(0);
        FormDataInfo formData = FormLayoutInfo.getFormData(field);
        assertNotNull(formData);
    }

    /**
   * Test for flow container support.
   */
    public void test_flowContainer() throws Exception {
        LayoutContainerInfo container = parseJavaInfo("public class Test extends LayoutContainer {", "  public Test() {", "    setLayout(new FormLayout());", "  }", "}");
        container.refresh();
        FormLayoutInfo layout = (FormLayoutInfo) container.getLayout();
        FlowContainer flowContainer = new FlowContainerFactory(layout, false).get().get(0);
        FieldInfo newField = createJavaInfo("com.extjs.gxt.ui.client.widget.form.LabelField", "empty");
        assertTrue(flowContainer.validateComponent(newField));
        flowContainer.command_CREATE(newField, null);
        assertEditor("public class Test extends LayoutContainer {", "  public Test() {", "    setLayout(new FormLayout());", "    {", "      LabelField labelField = new LabelField();", "      add(labelField, new FormData('100%'));", "    }", "  }", "}");
    }
}
