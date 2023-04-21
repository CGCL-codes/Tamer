package com.google.gdt.eclipse.designer.uibinder.model.widgets;

import com.google.gdt.eclipse.designer.uibinder.model.UiBinderModelTest;
import org.eclipse.wb.draw2d.geometry.Rectangle;
import org.eclipse.wb.internal.core.model.util.ScriptUtils;
import static org.fest.assertions.Assertions.assertThat;

/**
 * Test for {@link TreeInfo} widget.
 * 
 * @author scheglov_ke
 */
public class TreeTest extends UiBinderModelTest {

    public void _test_exit() throws Exception {
        System.exit(0);
    }

    /**
   * Even empty <code>Tree</code> should have two items.
   */
    public void test_empty() throws Exception {
        parse("// filler filler filler filler filler", "<ui:UiBinder>", "  <g:FlowPanel>", "    <g:Tree wbp:name='tree'/>", "  </g:FlowPanel>", "</ui:UiBinder>");
        refresh();
        TreeInfo tree = getObjectByName("tree");
        {
            Rectangle bounds = tree.getBounds();
            assertThat(bounds.x).isEqualTo(0);
            assertThat(bounds.y).isEqualTo(0);
            assertThat(bounds.width).isEqualTo(450);
            assertThat(bounds.height).isGreaterThan(50);
        }
        assertEquals(2, ScriptUtils.evaluate("getItemCount()", tree.getObject()));
    }

    /**
   * Even empty <code>Tree</code> should have two items.
   */
    public void test_empty_this() throws Exception {
        TreeInfo tree = parse("// filler filler filler filler filler", "<ui:UiBinder>", "  <g:Tree/>", "</ui:UiBinder>");
        refresh();
        assertEquals(new Rectangle(0, 0, 450, 300), tree.getBounds());
        assertEquals(2, ScriptUtils.evaluate("getItemCount()", tree.getObject()));
    }

    public void test_CREATE_this() throws Exception {
        ComplexPanelInfo flowPanel = parse("// filler filler filler filler filler", "<ui:UiBinder>", "  <g:FlowPanel/>", "</ui:UiBinder>");
        refresh();
        TreeInfo tree = createObject("com.google.gwt.user.client.ui.Tree");
        flowContainer_CREATE(flowPanel, tree, null);
        assertXML("// filler filler filler filler filler", "<ui:UiBinder>", "  <g:FlowPanel>", "    <g:Tree/>", "  </g:FlowPanel>", "</ui:UiBinder>");
    }

    public void test_CREATE_item() throws Exception {
        TreeInfo tree = parse("// filler filler filler filler filler", "<ui:UiBinder>", "  <g:Tree/>", "</ui:UiBinder>");
        refresh();
        TreeItemInfo newItem = createObject("com.google.gwt.user.client.ui.TreeItem");
        flowContainer_CREATE(tree, newItem, null);
        assertXML("// filler filler filler filler filler", "// filler filler filler filler filler", "<ui:UiBinder>", "  <g:Tree>", "    <g:TreeItem text='New TreeItem'/>", "  </g:Tree>", "</ui:UiBinder>");
    }

    public void test_CREATE_widget() throws Exception {
        TreeInfo tree = parse("// filler filler filler filler filler", "<ui:UiBinder>", "  <g:Tree/>", "</ui:UiBinder>");
        refresh();
        WidgetInfo newButton = createObject("com.google.gwt.user.client.ui.Button");
        flowContainer_CREATE(tree, newButton, null);
        assertXML("// filler filler filler filler filler", "// filler filler filler filler filler", "<ui:UiBinder>", "  <g:Tree>", "    <g:Button>New Button</g:Button>", "  </g:Tree>", "</ui:UiBinder>");
    }

    public void test_MOVE_reorder() throws Exception {
        TreeInfo tree = parse("// filler filler filler filler filler", "// filler filler filler filler filler", "<ui:UiBinder>", "  <g:Tree>", "    <g:TreeItem wbp:name='item_1'/>", "    <g:TreeItem wbp:name='item_2'/>", "    <g:TreeItem wbp:name='item_3'/>", "  </g:Tree>", "</ui:UiBinder>");
        refresh();
        TreeItemInfo item_1 = getObjectByName("item_1");
        TreeItemInfo item_3 = getObjectByName("item_3");
        flowContainer_MOVE(tree, item_3, item_1);
        assertXML("// filler filler filler filler filler", "// filler filler filler filler filler", "<ui:UiBinder>", "  <g:Tree>", "    <g:TreeItem wbp:name='item_3'/>", "    <g:TreeItem wbp:name='item_1'/>", "    <g:TreeItem wbp:name='item_2'/>", "  </g:Tree>", "</ui:UiBinder>");
    }

    /**
   * Test for copy/paste {@link TreeInfo} with its children.
   */
    public void test_clipboard() throws Exception {
        final ComplexPanelInfo flowPanel = parse("// filler filler filler filler filler", "// filler filler filler filler filler", "<ui:UiBinder>", "  <g:FlowPanel>", "    <g:Tree wbp:name='tree'>", "      <g:TreeItem text='a'>", "        <g:TreeItem text='aa'/>", "        <g:TreeItem text='ab'/>", "      </g:TreeItem>", "      <g:TreeItem text='b'/>", "      <g:TreeItem text='c'/>", "    </g:Tree>", "  </g:FlowPanel>", "</ui:UiBinder>");
        refresh();
        {
            TreeInfo tree = getObjectByName("tree");
            doCopyPaste(tree, new PasteProcedure<WidgetInfo>() {

                public void run(WidgetInfo copy) throws Exception {
                    flowContainer_CREATE(flowPanel, copy, null);
                }
            });
        }
        assertXML("// filler filler filler filler filler", "// filler filler filler filler filler", "<ui:UiBinder>", "  <g:FlowPanel>", "    <g:Tree wbp:name='tree'>", "      <g:TreeItem text='a'>", "        <g:TreeItem text='aa'/>", "        <g:TreeItem text='ab'/>", "      </g:TreeItem>", "      <g:TreeItem text='b'/>", "      <g:TreeItem text='c'/>", "    </g:Tree>", "    <g:Tree>", "      <g:TreeItem text='a' state='true'>", "        <g:TreeItem text='aa'/>", "        <g:TreeItem text='ab'/>", "      </g:TreeItem>", "      <g:TreeItem text='b'/>", "      <g:TreeItem text='c'/>", "    </g:Tree>", "  </g:FlowPanel>", "</ui:UiBinder>");
    }
}
