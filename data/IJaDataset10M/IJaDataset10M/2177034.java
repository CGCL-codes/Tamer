package com.googlecode.brui.uicomponents.table;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import com.googlecode.brui.beans.JellyBean;
import com.googlecode.brui.components.table.BeanPropertyTableModel;

public class BeanPropertyTableModelTest {

    private JellyBean jellyBean;

    private BeanPropertyTableModel model;

    @Before
    public void setup() {
        jellyBean = new JellyBean();
        model = new BeanPropertyTableModel(jellyBean);
    }

    @Test
    public void propertyNamesInFirstColumn() {
        assertEquals("colour", model.getValueAt(0, 0));
        assertEquals("size", model.getValueAt(1, 0));
        assertEquals("type", model.getValueAt(2, 0));
    }

    @Test
    public void canGetPropertyValue() {
        jellyBean.setColour("red");
        assertEquals("red", model.getValueAt(0, 1));
    }

    @Test
    public void canGetNullPropertyValue() {
        assertNull(model.getValueAt(0, 1));
    }

    @Test
    public void canGetIntegerPropertyValue() {
        assertEquals(0, model.getValueAt(1, 1));
    }

    @Test
    public void tableModelByDefaultHasNullBean() {
        assertNull(new BeanPropertyTableModel().getBean());
    }

    @Test
    public void modelHasTwoColumns() {
        assertEquals(2, model.getColumnCount());
    }

    @Test
    public void emptyModelHasTwoColumns() {
        model = new BeanPropertyTableModel();
        assertEquals(2, model.getColumnCount());
    }

    @Test
    public void nameColumnIsNotEditable() {
        assertFalse(model.isCellEditable(0, 0));
        assertFalse(model.isCellEditable(1, 0));
        assertFalse(model.isCellEditable(2, 0));
    }

    @Test
    public void valueColumnIsEditable() {
        assertTrue(model.isCellEditable(0, 1));
        assertTrue(model.isCellEditable(1, 1));
        assertTrue(model.isCellEditable(2, 1));
    }

    @Test
    public void rowCountMatchesBeanPropertyCount() {
        assertEquals(3, model.getRowCount());
    }

    @Test
    public void canSetPropertyValue() {
        model.setValueAt("orange", 0, 1);
        assertEquals("orange", jellyBean.getColour());
    }

    @Test
    public void canSetIntPropertyValue() {
        model.setValueAt("2", 1, 1);
        assertEquals(2, jellyBean.getSize());
    }

    @Test
    public void canGetPropertyNameColumnTitle() {
        assertEquals("Property", model.getColumnName(0));
    }

    @Test
    public void canGetPropertyValueColumnTitle() {
        assertEquals("Value", model.getColumnName(1));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void cannotGetNonExistantColumnTitle() {
        model.getColumnName(2);
    }

    @Test
    public void canGetModelRowCount() {
        assertEquals(3, model.getRowCount());
    }

    @Test
    public void canGetModelRowCountFromEmptyModel() {
        assertEquals(0, new BeanPropertyTableModel().getRowCount());
    }
}
