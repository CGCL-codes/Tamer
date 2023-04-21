package org.viewaframework.binding.swing;

import java.util.Arrays;
import org.viewaframework.binding.collection.EventList;
import org.viewaframework.binding.core.BeanAdapter;
import org.viewaframework.binding.core.Property;
import org.viewaframework.binding.swing.table.ColumnInfo;
import org.viewaframework.binding.util.Author;
import org.viewaframework.binding.util.DataBuilder;

public class LabelBindingTest extends FrameAvailableTestBase {

    public void testLabelBinding() throws Exception {
        SwingBinding swb = getSwingBinding();
        BeanAdapter<Author> author = DataBuilder.createAuthorBeanAdapter();
        EventList<Author> list = new EventList<Author>(DataBuilder.createAuthorList());
        swb.createTableListBinding(getTable(), list, Arrays.asList(new ColumnInfo(0, "name"))).createTableSelectionBinding(getTable(), author).createLabelBinding(getLabel(), new Property<String>(String.class, "text"), author, new Property<String>(String.class, "name")).bind();
        getFrame().setVisible(true);
        assertEquals(getLabel().getText(), "Gothard");
    }
}
