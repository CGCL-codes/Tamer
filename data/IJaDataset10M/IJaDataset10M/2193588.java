package zenbad.web.main.users;

import java.util.List;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.model.PropertyModel;
import zenbad.web.base.AbstractListView;
import zenbad.web.objects.UserObject;

public class UsersListView extends AbstractListView {

    private static final long serialVersionUID = 3274923513950421267L;

    public UsersListView(final String id, final List<UserObject> list) {
        super(id, list);
    }

    @Override
    protected void addFields(final ListItem listItem) {
        final UserObject user = (UserObject) listItem.getDefaultModelObject();
        listItem.add(new Label("username", new PropertyModel<String>(user, "username")));
        listItem.add(new Label("firstName", new PropertyModel<String>(user, "firstName")));
        listItem.add(new Label("lastName", new PropertyModel<String>(user, "lastName")));
    }
}
