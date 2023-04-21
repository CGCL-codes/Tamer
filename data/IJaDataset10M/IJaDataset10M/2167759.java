package fr.albin.ui.table;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import fr.albin.data.MainConfiguration;
import fr.albin.data.model.Item;
import fr.albin.util.ReflectionUtils;

/**
 * The simplest book data model with already defined columns.
 * TODO export all config in xml file
 * @author avigier
 *
 */
public class SimpleItemTableModel extends BasicItemTableModel {

    public SimpleItemTableModel(List<Item> list) {
        super(list);
    }

    /**
	 * The default column of this item model is :
	 * id, isbn, title and author.
	 */
    public void createDefaultColumns() {
        this.columns = new ArrayList<TableUserColumn>();
        try {
            Class<?> itemClass = MainConfiguration.getInstance().getItemClass();
            TableUserColumn userCol = new TableUserColumn();
            userCol.setGetter(ReflectionUtils.getMethodByName(itemClass, "getId"));
            userCol.setName("Id");
            userCol.setType(Integer.class);
            this.columns.add(userCol);
            userCol = new TableUserColumn();
            userCol.setGetter(ReflectionUtils.getMethodByName(itemClass, "getIsbn"));
            userCol.setName("Isbn");
            userCol.setType(String.class);
            this.columns.add(userCol);
            userCol = new TableUserColumn();
            userCol.setGetter(ReflectionUtils.getMethodByName(itemClass, "getTitle"));
            userCol.setName("Title");
            userCol.setType(String.class);
            this.columns.add(userCol);
            userCol = new TableUserColumn();
            userCol.setGetter(ReflectionUtils.getMethodByName(itemClass, "getAuthor"));
            userCol.setName("Authors");
            userCol.setType(String.class);
            this.columns.add(userCol);
        } catch (Exception e) {
            LOGGER.error(e);
        }
    }

    private static final Logger LOGGER = Logger.getLogger(SimpleItemTableModel.class);
}
