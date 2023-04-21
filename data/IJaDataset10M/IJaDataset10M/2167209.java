package wickettree.table;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import wickettree.TableTree;

/**
 * Copy of
 * {@link org.apache.wicket.extensions.markup.html.repeater.data.table.NoRecordsToolbar}
 */
public class NoRecordsToolbar extends AbstractToolbar {

    private static final long serialVersionUID = 1L;

    private static final IModel<String> DEFAULT_MESSAGE_MODEL = new ResourceModel("datatable.no-records-found", "No Records Found");

    public NoRecordsToolbar(final TableTree<?> table) {
        this(table, DEFAULT_MESSAGE_MODEL);
    }

    public NoRecordsToolbar(final TableTree<?> table, IModel<String> messageModel) {
        super(table);
        WebMarkupContainer td = new WebMarkupContainer("td");
        add(td);
        td.add(new AttributeModifier("colspan", new Model<String>(String.valueOf(table.getColumns().size()))));
        td.add(new Label("msg", messageModel));
    }

    @Override
    public boolean isVisible() {
        return getTree().getRowCount() == 0;
    }
}
