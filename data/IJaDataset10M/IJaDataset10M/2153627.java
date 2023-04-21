package net.sf.wicketdemo.common.component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.sf.wicketdemo.tech.BeanUtils;
import net.sf.wicketdemo.tech.ModelUtils;
import org.apache.commons.lang.Validate;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PropertyListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.util.ListModel;

/**
 * 
 * @author Bram Bogaert
 */
public class DisplayMapPanel extends Panel {

    private static final long serialVersionUID = 1L;

    private final IModel<String> title = new Model<String>();

    private final IModel<String> titleKeyColumn = Model.of("Key");

    private final IModel<String> titleValueColumn = Model.of("Value");

    private final IModel<List<Entry<?, ?>>> entryList = new ListModel<Entry<?, ?>>();

    private final IModel<Boolean> sortMapEntries = Model.of(Boolean.TRUE);

    public DisplayMapPanel(final String id) {
        super(id);
        initEntryListModel();
        addElements();
    }

    private void initEntryListModel() {
        entryList.setObject(new ArrayList<Entry<?, ?>>());
    }

    private void addElements() {
        addTitle();
        addContent();
    }

    private void addTitle() {
        add(new LabelInvisibleWhenNull("title", title));
        add(new Label("titleKeyColumn", titleKeyColumn));
        add(new Label("titleValueColumn", titleValueColumn));
    }

    private void addContent() {
        final PropertyListView<Entry<?, ?>> lv = new PropertyListView<Entry<?, ?>>("entryList", entryList) {

            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(final ListItem<Entry<?, ?>> item) {
                final Entry<?, ?> itemModelObject = item.getModelObject();
                String id = null;
                Component component = null;
                id = "key";
                component = getKeyComponent(id, itemModelObject);
                addComponent(item, id, component);
                id = "value";
                component = getValueComponent(id, itemModelObject);
                addComponent(item, id, component);
            }

            private void addComponent(final ListItem<Entry<?, ?>> item, final String id, final Component component) {
                Validate.isTrue(id.equals(component.getId()));
                item.add(component);
            }
        };
        add(lv);
    }

    protected Component getKeyComponent(final String id, final Entry<?, ?> itemModelObject) {
        return new Label(id);
    }

    protected Component getValueComponent(final String id, final Entry<?, ?> itemModelObject) {
        return new Label(id);
    }

    public void setMap(final Map<?, ?> map) {
        if (map == null) {
            initEntryListModel();
        } else {
            final boolean sortEntryList = Boolean.TRUE.equals(sortMapEntries.getObject());
            final List<Entry<?, ?>> entryListObject = ModelUtils.toGenericEntryList(ModelUtils.getEntryList(map, sortEntryList));
            entryList.setObject(entryListObject);
        }
    }

    public void setMapFromObject(final Object object) {
        Map<String, Object> map = null;
        if (object != null) {
            map = BeanUtils.describe(object);
        }
        setMap(map);
    }

    /**
	 * 
	 * @param entries a list of entries.
	 * Note: consider making sure these entries are serializable.
	 */
    public void setEntries(final List<Entry<?, ?>> entries) {
        entryList.setObject(entries);
    }

    public void setSortMapEntries(final boolean sortMapEntries) {
        this.sortMapEntries.setObject(Boolean.valueOf(sortMapEntries));
        if (sortMapEntries) {
            final List<Entry<Object, Object>> typedEntries = ModelUtils.toTypedEntryList(entryList.getObject());
            final List<Entry<Object, Object>> sortedEntries = ModelUtils.getEntryList(typedEntries, sortMapEntries);
            final List<Entry<?, ?>> entryListObject = ModelUtils.toGenericEntryList(sortedEntries);
            entryList.setObject(entryListObject);
        }
    }

    public void setTitle(final String title) {
        this.title.setObject(title);
    }

    public String getTitle() {
        return title.getObject();
    }

    public void setTitleKeyColumn(final String titleKeyColumn) {
        this.titleKeyColumn.setObject(titleKeyColumn);
    }

    public String getTitleKeyColumn() {
        return titleKeyColumn.getObject();
    }

    public void setTitleValueColumn(final String titleValueColumn) {
        this.titleValueColumn.setObject(titleValueColumn);
    }

    public String getTitleValueColumn() {
        return titleValueColumn.getObject();
    }
}
