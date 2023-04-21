package com.google.gdt.eclipse.designer.uibinder.model.widgets;

import com.google.gdt.eclipse.designer.uibinder.model.UiBinderModelTest;
import org.eclipse.wb.draw2d.geometry.Rectangle;
import org.eclipse.wb.internal.core.model.property.Property;
import org.eclipse.wb.internal.core.model.util.ObjectsLabelProvider;
import org.eclipse.wb.internal.core.model.util.PropertyUtils;
import static org.fest.assertions.Assertions.assertThat;
import java.util.List;

/**
 * Test for {@link DockPanelInfo} widget.
 * 
 * @author scheglov_ke
 */
public class DockPanelTest extends UiBinderModelTest {

    public void _test_exit() throws Exception {
        System.exit(0);
    }

    /**
   * Test for {@link DockPanelInfo#hasCenterWidget()}
   */
    public void test_hasCenterWidget_has() throws Exception {
        DockPanelInfo panel = parse("// filler filler filler filler filler", "<ui:UiBinder>", "  <g:DockPanel>", "    <g:Dock direction='CENTER'>", "      <g:Button wbp:name='button'/>", "    </g:Dock>", "  </g:DockPanel>", "</ui:UiBinder>");
        refresh();
        assertEquals(true, panel.hasCenterWidget());
    }

    /**
   * Test for {@link DockPanelInfo#hasCenterWidget()}
   */
    public void test_hasCenterWidget_no() throws Exception {
        DockPanelInfo panel = parse("// filler filler filler filler filler", "<ui:UiBinder>", "  <g:DockPanel>", "    <g:Dock direction='WEST'>", "      <g:Button/>", "    </g:Dock>", "  </g:DockPanel>", "</ui:UiBinder>");
        refresh();
        assertEquals(false, panel.hasCenterWidget());
    }

    /**
   * Even empty <code>DockPanel</code> should have some reasonable size.
   */
    public void test_empty() throws Exception {
        parse("// filler filler filler filler filler", "<ui:UiBinder>", "  <g:FlowPanel>", "    <g:DockPanel wbp:name='panel'/>", "  </g:FlowPanel>", "</ui:UiBinder>");
        refresh();
        DockPanelInfo panel = getObjectByName("panel");
        {
            Rectangle bounds = panel.getBounds();
            assertThat(bounds.width).isGreaterThan(130);
            assertThat(bounds.height).isGreaterThan(20);
        }
    }

    /**
   * Direction should be reflected in child {@link WidgetInfo} title.
   */
    public void test_directionInTitle() throws Exception {
        parse("// filler filler filler filler filler", "<ui:UiBinder>", "  <g:DockPanel>", "    <g:Dock direction='WEST'>", "      <g:Button wbp:name='button_1'/>", "    </g:Dock>", "    <g:Dock direction='NORTH'>", "      <g:Button wbp:name='button_2'/>", "    </g:Dock>", "  </g:DockPanel>", "</ui:UiBinder>");
        refresh();
        WidgetInfo button_1 = getObjectByName("button_1");
        WidgetInfo button_2 = getObjectByName("button_2");
        {
            String title = ObjectsLabelProvider.INSTANCE.getText(button_1);
            assertThat(title).startsWith("WEST - ");
        }
        {
            String title = ObjectsLabelProvider.INSTANCE.getText(button_2);
            assertThat(title).startsWith("NORTH - ");
        }
    }

    /**
   * Test for contributed "Direction" property.
   */
    public void test_directionProperty() throws Exception {
        parse("// filler filler filler filler filler", "<ui:UiBinder>", "  <g:DockPanel>", "    <g:Dock direction='WEST'>", "      <g:Button wbp:name='button'/>", "    </g:Dock>", "  </g:DockPanel>", "</ui:UiBinder>");
        refresh();
        WidgetInfo button = getObjectByName("button");
        Property directionProperty;
        {
            directionProperty = button.getPropertyByTitle("Direction");
            assertNotNull(directionProperty);
            assertSame(directionProperty, button.getPropertyByTitle("Direction"));
            assertEquals("Direction", directionProperty.getTitle());
            assertTrue(directionProperty.getCategory().isSystem());
            assertTrue(directionProperty.isModified());
        }
        assertEquals("WEST", getPropertyText(directionProperty));
        {
            addComboPropertyItems(directionProperty);
            List<String> items = getComboPropertyItems();
            assertThat(items).containsExactly("NORTH", "SOUTH", "WEST", "EAST", "CENTER", "LINE_START", "LINE_END");
        }
        directionProperty.setValue("NORTH");
        assertXML("// filler filler filler filler filler", "<ui:UiBinder>", "  <g:DockPanel>", "    <g:Dock direction='NORTH'>", "      <g:Button wbp:name='button'/>", "    </g:Dock>", "  </g:DockPanel>", "</ui:UiBinder>");
    }

    /**
   * Test for contributed "Cell" property.
   */
    public void test_CellProperty() throws Exception {
        parse("// filler filler filler filler filler", "<ui:UiBinder>", "  <g:DockPanel>", "    <g:Dock direction='WEST'>", "      <g:Button wbp:name='button'/>", "    </g:Dock>", "  </g:DockPanel>", "</ui:UiBinder>");
        refresh();
        WidgetInfo button = getObjectByName("button");
        Property cellProperty;
        {
            cellProperty = button.getPropertyByTitle("Cell");
            assertNotNull(cellProperty);
            assertSame(cellProperty, button.getPropertyByTitle("Cell"));
            assertEquals("Cell", cellProperty.getTitle());
            assertTrue(cellProperty.getCategory().isSystem());
            assertFalse(cellProperty.isModified());
            assertEquals("(cell properties)", getPropertyText(cellProperty));
        }
        {
            Property widthProperty = PropertyUtils.getByPath(button, "Cell/width");
            assertFalse(widthProperty.isModified());
            assertEquals(Property.UNKNOWN_VALUE, widthProperty.getValue());
            widthProperty.setValue("5cm");
            assertXML("// filler filler filler filler filler", "<ui:UiBinder>", "  <g:DockPanel>", "    <g:Dock direction='WEST' width='5cm'>", "      <g:Button wbp:name='button'/>", "    </g:Dock>", "  </g:DockPanel>", "</ui:UiBinder>");
            assertTrue(widthProperty.isModified());
            assertEquals("5cm", widthProperty.getValue());
            widthProperty.setValue("10cm");
            assertXML("// filler filler filler filler filler", "<ui:UiBinder>", "  <g:DockPanel>", "    <g:Dock direction='WEST' width='10cm'>", "      <g:Button wbp:name='button'/>", "    </g:Dock>", "  </g:DockPanel>", "</ui:UiBinder>");
            assertTrue(widthProperty.isModified());
            assertEquals("10cm", widthProperty.getValue());
            widthProperty.setValue(Property.UNKNOWN_VALUE);
            assertXML("// filler filler filler filler filler", "<ui:UiBinder>", "  <g:DockPanel>", "    <g:Dock direction='WEST'>", "      <g:Button wbp:name='button'/>", "    </g:Dock>", "  </g:DockPanel>", "</ui:UiBinder>");
            assertFalse(widthProperty.isModified());
            assertEquals(Property.UNKNOWN_VALUE, widthProperty.getValue());
        }
        {
            Property heightProperty = PropertyUtils.getByPath(button, "Cell/height");
            assertFalse(heightProperty.isModified());
            assertEquals(Property.UNKNOWN_VALUE, heightProperty.getValue());
            heightProperty.setValue("5cm");
            assertXML("// filler filler filler filler filler", "<ui:UiBinder>", "  <g:DockPanel>", "    <g:Dock direction='WEST' height='5cm'>", "      <g:Button wbp:name='button'/>", "    </g:Dock>", "  </g:DockPanel>", "</ui:UiBinder>");
            assertTrue(heightProperty.isModified());
            assertEquals("5cm", heightProperty.getValue());
            heightProperty.setValue(Property.UNKNOWN_VALUE);
            assertXML("// filler filler filler filler filler", "<ui:UiBinder>", "  <g:DockPanel>", "    <g:Dock direction='WEST'>", "      <g:Button wbp:name='button'/>", "    </g:Dock>", "  </g:DockPanel>", "</ui:UiBinder>");
            assertFalse(heightProperty.isModified());
            assertEquals(Property.UNKNOWN_VALUE, heightProperty.getValue());
        }
        {
            Property alignmentProperty = PropertyUtils.getByPath(button, "Cell/horizontalAlignment");
            assertFalse(alignmentProperty.isModified());
            assertEquals("ALIGN_LEFT", getPropertyText(alignmentProperty));
            {
                addComboPropertyItems(alignmentProperty);
                List<String> items = getComboPropertyItems();
                assertThat(items).containsExactly("ALIGN_LEFT", "ALIGN_CENTER", "ALIGN_RIGHT");
            }
            setComboPropertyValue(alignmentProperty, 2);
            assertXML("// filler filler filler filler filler", "<ui:UiBinder>", "  <g:DockPanel>", "    <g:Dock direction='WEST' horizontalAlignment='ALIGN_RIGHT'>", "      <g:Button wbp:name='button'/>", "    </g:Dock>", "  </g:DockPanel>", "</ui:UiBinder>");
            assertTrue(alignmentProperty.isModified());
            assertEquals("ALIGN_RIGHT", getPropertyText(alignmentProperty));
            alignmentProperty.setValue(Property.UNKNOWN_VALUE);
            assertXML("// filler filler filler filler filler", "<ui:UiBinder>", "  <g:DockPanel>", "    <g:Dock direction='WEST'>", "      <g:Button wbp:name='button'/>", "    </g:Dock>", "  </g:DockPanel>", "</ui:UiBinder>");
            assertFalse(alignmentProperty.isModified());
            assertEquals("ALIGN_LEFT", getPropertyText(alignmentProperty));
        }
        {
            Property alignmentProperty = PropertyUtils.getByPath(button, "Cell/verticalAlignment");
            assertFalse(alignmentProperty.isModified());
            assertEquals("ALIGN_TOP", getPropertyText(alignmentProperty));
            {
                addComboPropertyItems(alignmentProperty);
                List<String> items = getComboPropertyItems();
                assertThat(items).containsExactly("ALIGN_TOP", "ALIGN_MIDDLE", "ALIGN_BOTTOM");
            }
            setComboPropertyValue(alignmentProperty, 2);
            assertXML("// filler filler filler filler filler", "<ui:UiBinder>", "  <g:DockPanel>", "    <g:Dock direction='WEST' verticalAlignment='ALIGN_BOTTOM'>", "      <g:Button wbp:name='button'/>", "    </g:Dock>", "  </g:DockPanel>", "</ui:UiBinder>");
            assertTrue(alignmentProperty.isModified());
            assertEquals("ALIGN_BOTTOM", getPropertyText(alignmentProperty));
            alignmentProperty.setValue(Property.UNKNOWN_VALUE);
            assertXML("// filler filler filler filler filler", "<ui:UiBinder>", "  <g:DockPanel>", "    <g:Dock direction='WEST'>", "      <g:Button wbp:name='button'/>", "    </g:Dock>", "  </g:DockPanel>", "</ui:UiBinder>");
            assertFalse(alignmentProperty.isModified());
            assertEquals("ALIGN_TOP", getPropertyText(alignmentProperty));
        }
    }

    public void test_flowContainer_CREATE() throws Exception {
        DockPanelInfo panel = parse("// filler filler filler filler filler", "<ui:UiBinder>", "  <g:DockPanel>", "    <g:Dock direction='EAST'>", "      <g:Button wbp:name='existingButton'/>", "    </g:Dock>", "  </g:DockPanel>", "</ui:UiBinder>");
        refresh();
        WidgetInfo existingButton = getObjectByName("existingButton");
        WidgetInfo newButton = createButton();
        flowContainer_CREATE(panel, newButton, existingButton);
        assertXML("// filler filler filler filler filler", "<ui:UiBinder>", "  <g:DockPanel>", "    <g:Dock direction='WEST'>", "      <g:Button/>", "    </g:Dock>", "    <g:Dock direction='EAST'>", "      <g:Button wbp:name='existingButton'/>", "    </g:Dock>", "  </g:DockPanel>", "</ui:UiBinder>");
        assertHierarchy("// filler filler filler filler filler", "// filler filler filler filler filler", "<g:DockPanel>", "  <g:Button>", "  <g:Button wbp:name='existingButton'>");
    }

    public void test_flowContainer_MOVE_reorder() throws Exception {
        DockPanelInfo panel = parse("// filler filler filler filler filler", "<ui:UiBinder>", "  <g:DockPanel>", "    <g:Dock direction='WEST'>", "      <g:Button wbp:name='button_1'/>", "    </g:Dock>", "    <g:Dock direction='EAST' >", "      <g:Button wbp:name='button_2'/>", "    </g:Dock>", "  </g:DockPanel>", "</ui:UiBinder>");
        refresh();
        WidgetInfo button_1 = getObjectByName("button_1");
        WidgetInfo button_2 = getObjectByName("button_2");
        flowContainer_MOVE(panel, button_2, button_1);
        assertXML("// filler filler filler filler filler", "<ui:UiBinder>", "  <g:DockPanel>", "    <g:Dock direction='EAST' >", "      <g:Button wbp:name='button_2'/>", "    </g:Dock>", "    <g:Dock direction='WEST'>", "      <g:Button wbp:name='button_1'/>", "    </g:Dock>", "  </g:DockPanel>", "</ui:UiBinder>");
        assertHierarchy("// filler filler filler filler filler", "// filler filler filler filler filler", "<g:DockPanel>", "  <g:Button wbp:name='button_2'>", "  <g:Button wbp:name='button_1'>");
    }

    public void test_flowContainer_MOVE_reparent() throws Exception {
        parse("// filler filler filler filler filler", "<ui:UiBinder>", "  <g:FlowPanel>", "    <g:Button wbp:name='button'/>", "    <g:DockPanel wbp:name='panel'/>", "  </g:FlowPanel>", "</ui:UiBinder>");
        refresh();
        DockPanelInfo panel = getObjectByName("panel");
        WidgetInfo button = getObjectByName("button");
        flowContainer_MOVE(panel, button, null);
        assertXML("// filler filler filler filler filler", "// filler filler filler filler filler", "<ui:UiBinder>", "  <g:FlowPanel>", "    <g:DockPanel wbp:name='panel'>", "      <g:Dock direction='WEST'>", "        <g:Button wbp:name='button'/>", "      </g:Dock>", "    </g:DockPanel>", "  </g:FlowPanel>", "</ui:UiBinder>");
        assertHierarchy("// filler filler filler filler filler", "// filler filler filler filler filler", "<g:FlowPanel>", "  <g:DockPanel wbp:name='panel'>", "    <g:Button wbp:name='button'>");
    }

    public void test_clipboard() throws Exception {
        final ComplexPanelInfo flowPanel = parse("// filler filler filler filler filler", "// filler filler filler filler filler", "<ui:UiBinder>", "  <g:FlowPanel>", "    <g:DockPanel wbp:name='panel'>", "      <g:Dock direction='EAST'>", "        <g:Button/>", "      </g:Dock>", "    </g:DockPanel>", "  </g:FlowPanel>", "</ui:UiBinder>");
        refresh();
        {
            DockPanelInfo panel = getObjectByName("panel");
            doCopyPaste(panel, new PasteProcedure<WidgetInfo>() {

                public void run(WidgetInfo copy) throws Exception {
                    flowContainer_CREATE(flowPanel, copy, null);
                }
            });
        }
        assertXML("// filler filler filler filler filler", "// filler filler filler filler filler", "<ui:UiBinder>", "  <g:FlowPanel>", "    <g:DockPanel wbp:name='panel'>", "      <g:Dock direction='EAST'>", "        <g:Button/>", "      </g:Dock>", "    </g:DockPanel>", "    <g:DockPanel>", "      <g:Dock direction='EAST'>", "        <g:Button/>", "      </g:Dock>", "    </g:DockPanel>", "  </g:FlowPanel>", "</ui:UiBinder>");
    }
}
