package org.crucy.persistence.ui;

import java.util.LinkedHashSet;
import org.crucy.persistence.tablemodel.propeditmanager.EditablePropertiesManager;
import org.crucy.persistence.tablemodel.propeditmanager.UneditablePropertiesManager;
import org.crucy.persistence.tablemodel.propertybuilder.AutomaticPropertyBuilder;
import org.crucy.persistence.ui.JAdvancedBeanTable.Actions;

/**
 * Creates some basic instanciation of <tt>JAdvancedBeanTable</tt>.
 * @author Thibault
 *
 */
public final class JAdvancedBeanTableFactory {

    /**
	 * Hidden because, utility class.
	 */
    private JAdvancedBeanTableFactory() {
    }

    /**
	 * Creates a default Table that is uneditable and with no actions.
	 * @param <T> Type of bean contained by table
	 * @param clazz Class contained by table
	 * @return Created JAdvancedBeanTable.
	 */
    public static <T> JAdvancedBeanTable<T> createDefaultUneditableTable(final Class<T> clazz) {
        JAdvBeanTableBuilder<T> builder = new JAdvBeanTableBuilder<T>(clazz);
        builder.setEditManager(new UneditablePropertiesManager<T>());
        builder.setActions(new LinkedHashSet<Actions>());
        builder.setPropertyBuidler(new AutomaticPropertyBuilder<T>(builder.getEditManager()));
        return builder.build();
    }

    /**
	 * Creates a default Table that is editable and with all actions.
	 * @param <T> Type of bean contained by table
	 * @param clazz Class contained by table
	 * @return Created JAdvancedBeanTable.
	 */
    public static <T> JAdvancedBeanTable<T> createDefaultEditableTable(final Class<T> clazz) {
        JAdvBeanTableBuilder<T> builder = new JAdvBeanTableBuilder<T>(clazz);
        builder.setEditManager(new EditablePropertiesManager<T>());
        builder.setActions(null);
        builder.setPropertyBuidler(new AutomaticPropertyBuilder<T>(builder.getEditManager()));
        return builder.build();
    }
}
