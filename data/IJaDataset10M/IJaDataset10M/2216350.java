package net.sf.jzeno.tests;

import java.util.ArrayList;
import java.util.List;
import net.sf.jzeno.echo.ConstructionList;
import net.sf.jzeno.echo.EchoSupport;
import net.sf.jzeno.echo.databinding.BoundTableModel;
import net.sf.jzeno.echo.databinding.DynaButton;
import net.sf.jzeno.echo.databinding.DynaTable;
import net.sf.jzeno.tests.viewer.IntegerViewer;
import net.sf.jzeno.tests.viewer.StringViewer;
import net.sf.jzeno.util.GuidGenerator;
import nextapp.echo.table.TableModel;
import nextapp.echoservlet.EchoTestSupport;
import nextapp.echoservlet.ui.ButtonUI;
import org.apache.log4j.Logger;
import echopoint.SortableTable;
import echopoint.table.SortablePagedTableModel;

/**
 * @author ddhondt
 * 
 * Testcase for the DynaTable.
 */
public class DynaTableTest extends AbstractTest2 {

    protected static Logger log = Logger.getLogger(DynaTableTest.class);

    public void testDynaTableModel() throws Exception {
        MockModelBean bean = new MockModelBean();
        MockModelBean2 b1 = new MockModelBean2();
        MockModelBean2 b2 = new MockModelBean2();
        MockModelBean2 b3 = new MockModelBean2();
        bean.getBeans().add(b1);
        bean.getBeans().add(b2);
        bean.getBeans().add(b3);
        ConstructionList cl = null;
        cl = new ConstructionList();
        cl.add("string", "Label for string", "", null);
        cl.add("integer", "Label for integer", "", null);
        DynaTable table = new DynaTable(MockModelBean.class, "beans", "", cl);
        table.setBean(bean);
        table.rebind();
        SortableTable st = (SortableTable) EchoSupport.findComponentByType(table, SortableTable.class);
        SortablePagedTableModel sptm = (SortablePagedTableModel) st.getModel();
        BoundTableModel model = (BoundTableModel) sptm.getModel();
        assertTrue(model.getColumnCount() == 2);
        assertTrue(model.getRowCount() == 3);
    }

    public void testDynaTableModelUnbound() throws Exception {
        MockModelBean bean = new MockModelBean();
        MockModelBean2 b1 = new MockModelBean2();
        MockModelBean2 b2 = new MockModelBean2();
        MockModelBean2 b3 = new MockModelBean2();
        bean.getBeans().add(b1);
        bean.getBeans().add(b2);
        bean.getBeans().add(b3);
        ConstructionList cl = null;
        cl = new ConstructionList();
        cl.add("string", "Label for string", "", null);
        cl.add("integer", "Label for integer", "", null);
        DynaTable table = new DynaTable(cl);
        table.setList(new ArrayList(bean.getBeans()));
        SortableTable st = (SortableTable) EchoSupport.findComponentByType(table, SortableTable.class);
        SortablePagedTableModel sptm = (SortablePagedTableModel) st.getModel();
        BoundTableModel model = (BoundTableModel) sptm.getModel();
        assertTrue(model.getColumnCount() == 2);
        assertTrue(model.getRowCount() == 3);
    }

    public void testDynaTableModelCellRendering() throws Exception {
        String stringValue = "junit-string-value" + GuidGenerator.getInstance().generate();
        int intValue = 367;
        MockModelBean bean = new MockModelBean();
        MockModelBean2 b1 = new MockModelBean2();
        b1.setString(stringValue);
        b1.setInteger(intValue);
        MockModelBean2 b2 = new MockModelBean2();
        b2.setString(stringValue);
        b2.setInteger(intValue);
        MockModelBean2 b3 = new MockModelBean2();
        b3.setString(stringValue);
        b3.setInteger(intValue);
        bean.getBeans().add(b1);
        bean.getBeans().add(b2);
        bean.getBeans().add(b3);
        ConstructionList cl = null;
        cl = new ConstructionList();
        cl.add("string", "Label for string", "", null);
        cl.add("integer", "Label for integer", "", null);
        DynaTable table = new DynaTable(MockModelBean.class, "beans", "", cl);
        table.setBean(bean);
        table.rebind();
        SortableTable st = (SortableTable) EchoSupport.findComponentByType(table, SortableTable.class);
        SortablePagedTableModel sptm = (SortablePagedTableModel) st.getModel();
        BoundTableModel model = (BoundTableModel) sptm.getModel();
        assertTrue(model.getColumnCount() == 2);
        assertTrue(model.getRowCount() == 3);
        StringViewer sv = null;
        sv = (StringViewer) model.getValueAt(0, 0);
        assertEquals(stringValue, sv.getText());
        sv = (StringViewer) model.getValueAt(0, 1);
        assertEquals(stringValue, sv.getText());
        IntegerViewer iv = null;
        iv = (IntegerViewer) model.getValueAt(1, 0);
        assertEquals(Integer.toString(intValue), iv.getText());
        iv = (IntegerViewer) model.getValueAt(1, 1);
        assertEquals(Integer.toString(intValue), iv.getText());
    }

    public void testDynaButtonInRow() throws Exception {
        String stringValue = "junit-string-value" + GuidGenerator.getInstance().generate();
        int intValue = 367;
        MockModelBean bean = new MockModelBean();
        MockModelBean2 b1 = new MockModelBean2();
        b1.setString(stringValue);
        b1.setInteger(intValue);
        MockModelBean2 b2 = new MockModelBean2();
        b2.setString(stringValue);
        b2.setInteger(intValue);
        MockModelBean2 b3 = new MockModelBean2();
        b3.setString(stringValue);
        b3.setInteger(intValue);
        bean.getBeans().add(b1);
        bean.getBeans().add(b2);
        bean.getBeans().add(b3);
        ConstructionList cl = null;
        cl = new ConstructionList();
        cl.add("string", "Label for string", "", null);
        cl.add("integer", "Label for integer", "", null);
        cl.add("", "Button", "actionCommand=event", DynaButton.class);
        DynaTable table = new DynaTable(cl);
        List list = new ArrayList(bean.getBeans());
        table.setList(list);
        MockEventSink mes = new MockEventSink();
        mes.add(table);
        SortableTable st = (SortableTable) EchoSupport.findComponentByType(table, SortableTable.class);
        SortablePagedTableModel sptm = (SortablePagedTableModel) st.getModel();
        BoundTableModel model = (BoundTableModel) sptm.getModel();
        assertTrue(model.getColumnCount() == 3);
        assertTrue(model.getRowCount() == 3);
        DynaButton button = null;
        ButtonUI buttonUI = null;
        mes.clear();
        button = (DynaButton) model.getValueAt(2, 0);
        buttonUI = (ButtonUI) EchoTestSupport.createComponentPeer(button);
        buttonUI.clientAction("press");
        assertTrue(mes.isEventReceived());
        assertTrue(list.get(0) == mes.getBoundValue());
        mes.clear();
        button = (DynaButton) model.getValueAt(2, 1);
        buttonUI = (ButtonUI) EchoTestSupport.createComponentPeer(button);
        buttonUI.clientAction("press");
        assertTrue(mes.isEventReceived());
        assertTrue(list.get(1) == mes.getBoundValue());
        mes.clear();
        button = (DynaButton) model.getValueAt(2, 2);
        buttonUI = (ButtonUI) EchoTestSupport.createComponentPeer(button);
        buttonUI.clientAction("press");
        assertTrue(mes.isEventReceived());
        assertTrue(list.get(2) == mes.getBoundValue());
    }

    /**
     * Check that setting the List property and/or model property maintains the currently set model..
     */
    public void testWellBehavedModelProperty() throws Exception {
        String stringValue = "junit-string-value" + GuidGenerator.getInstance().generate();
        int intValue = 367;
        MockModelBean bean = new MockModelBean();
        MockModelBean2 b1 = new MockModelBean2();
        b1.setString(stringValue);
        b1.setInteger(intValue);
        MockModelBean2 b2 = new MockModelBean2();
        b2.setString(stringValue);
        b2.setInteger(intValue);
        MockModelBean2 b3 = new MockModelBean2();
        b3.setString(stringValue);
        b3.setInteger(intValue);
        bean.getBeans().add(b1);
        bean.getBeans().add(b2);
        bean.getBeans().add(b3);
        ConstructionList cl = null;
        cl = new ConstructionList();
        cl.add("string", "Label for string", "", null);
        cl.add("integer", "Label for integer", "", null);
        DynaTable table = new DynaTable(cl);
        TableModel model = table.getModel();
        table.setList(new ArrayList(bean.getBeans()));
        assertTrue(model == table.getModel());
        table.rebind();
        assertTrue(table.getModel().getColumnCount() == 2);
        assertTrue(table.getModel().getRowCount() == 3);
        StringViewer sv = null;
        sv = (StringViewer) model.getValueAt(0, 0);
        assertEquals(stringValue, sv.getText());
        sv = (StringViewer) model.getValueAt(0, 1);
        assertEquals(stringValue, sv.getText());
        IntegerViewer iv = null;
        iv = (IntegerViewer) model.getValueAt(1, 0);
        assertEquals(Integer.toString(intValue), iv.getText());
        iv = (IntegerViewer) model.getValueAt(1, 1);
        assertEquals(Integer.toString(intValue), iv.getText());
    }

    public void testModelDecoration() throws Exception {
        String stringValue = "junit-string-value" + GuidGenerator.getInstance().generate();
        int intValue = 367;
        MockModelBean bean = new MockModelBean();
        MockModelBean2 b1 = new MockModelBean2();
        b1.setString(stringValue);
        b1.setInteger(intValue);
        MockModelBean2 b2 = new MockModelBean2();
        b2.setString(stringValue);
        b2.setInteger(intValue);
        MockModelBean2 b3 = new MockModelBean2();
        b3.setString(stringValue);
        b3.setInteger(intValue);
        bean.getBeans().add(b1);
        bean.getBeans().add(b2);
        bean.getBeans().add(b3);
        ConstructionList cl = null;
        cl = new ConstructionList();
        cl.add("string", "Label for string", "", null);
        cl.add("integer", "Label for integer", "", null);
        DynaTable table = new DynaTable(cl);
        MockTableModelDecorator decorator = new MockTableModelDecorator(table);
        table.setModel(decorator);
        table.setList(new ArrayList(bean.getBeans()));
        assertTrue(decorator == table.getModel());
        table.rebind();
        assertTrue(table.getModel().getColumnCount() == 2);
        assertTrue(table.getModel().getRowCount() == 3);
        assertEquals("", decorator.getValueAt(0, 0));
        assertEquals("", decorator.getValueAt(0, 1));
        IntegerViewer iv = null;
        iv = (IntegerViewer) decorator.getValueAt(1, 0);
        assertEquals(Integer.toString(intValue), iv.getText());
        iv = (IntegerViewer) decorator.getValueAt(1, 1);
        assertEquals(Integer.toString(intValue), iv.getText());
    }

    public void testPropertyComponent() throws Exception {
        PropertyComponentTestSupport support = null;
        support = new PropertyComponentTestSupport(getComponentClass(), createBean(), getBeanProperty(), createNewValueForProperty());
        assertTrue(support.testBeanProperty());
        support = new PropertyComponentTestSupport(getComponentClass(), createBean(), getBeanProperty(), createNewValueForProperty());
        assertTrue(support.testDecoratorClassNameProperty());
        support = new PropertyComponentTestSupport(getComponentClass(), createBean(), getBeanProperty(), createNewValueForProperty());
        assertTrue(support.testPropertyProperty());
        support = new PropertyComponentTestSupport(getComponentClass(), createBean(), getBeanProperty(), createNewValueForProperty());
        assertTrue(support.testRequiredProperty());
        support = new PropertyComponentTestSupport(getComponentClass(), createBean(), getBeanProperty(), createNewValueForProperty());
        assertTrue(support.testValidProperty());
    }

    protected Class getComponentClass() {
        return DynaTable.class;
    }

    protected Object createBean() {
        MockModelBean ret = new MockModelBean();
        ret.getBeans().add(new MockModelBean2());
        ret.getBeans().add(new MockModelBean2());
        return ret;
    }

    protected String getBeanProperty() {
        return "beans";
    }

    protected Object createNewValueForProperty() {
        MockModelBean ret = new MockModelBean();
        ret.getBeans().add(new MockModelBean2());
        ret.getBeans().add(new MockModelBean2());
        ret.getBeans().add(new MockModelBean2());
        return ret;
    }
}
