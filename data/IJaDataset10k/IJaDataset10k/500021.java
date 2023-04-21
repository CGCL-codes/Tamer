package net.sf.vcaperture.web;

import net.sf.vcaperture.web.models.RepositoryListModel;
import net.sf.vcaperture.web.urls.ViewDirectoryLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

public class HomePage extends ApertureBasePage {

    private static final long serialVersionUID = 1L;

    public HomePage() {
        constructComponentHierarchy();
    }

    protected void constructComponentHierarchy() {
        add(new ListView("repos", new RepositoryListModel()) {

            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(ListItem item) {
                item.add(new Label("name", new PropertyModel(item.getModel(), "name")));
                item.add(new ViewDirectoryLink("view", new PropertyModel(item.getModel(), "name"), new Model("")));
            }
        });
    }
}
