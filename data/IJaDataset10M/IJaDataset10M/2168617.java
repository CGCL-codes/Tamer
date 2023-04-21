package fi.passiba.groups.ui.pages;

import fi.passiba.groups.ui.pages.search.SearchPanelPage;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;

public class BasePage extends ProtectedPage {

    List<MenuItem> menu = createMenu();

    public BasePage() {
        add(new SearchPanelPage("searchPanel"));
        add(new UserPanel("userPanel"));
        ListView lv2 = new ListView("menus", menu) {

            protected void populateItem(ListItem item) {
                MenuItem menuitem = (MenuItem) item.getModelObject();
                BookmarkablePageLink link = new BookmarkablePageLink("link", menuitem.destination);
                link.add(new Label("caption", menuitem.caption));
                item.add(link);
            }
        };
        add(lv2);
        add(new LogOutPanel("logoutPanel", Main.class));
        add(new FeedbackPanel("feedback"));
    }

    /**
     * Construct.
     * 
     * @param model
     */
    public BasePage(IModel model) {
        super(model);
        add(new SearchPanelPage("searchPanel"));
        add(new UserPanel("userPanel"));
        add(new LogOutPanel("logoutPanel", Main.class));
        ListView lv2 = new ListView("menus", menu) {

            protected void populateItem(ListItem item) {
                MenuItem menuitem = (MenuItem) item.getModelObject();
                BookmarkablePageLink link = new BookmarkablePageLink("link", menuitem.destination);
                link.add(new Label("caption", menuitem.caption));
                item.add(link);
            }
        };
        add(lv2);
        add(new FeedbackPanel("feedback"));
    }

    public class MenuItem implements Serializable {

        private String caption;

        private Class destination;
    }

    private List<MenuItem> createMenu() {
        List<MenuItem> menu = new ArrayList<MenuItem>();
        MenuItem item = new MenuItem();
        item.caption = getLocalizer().getString("nav_front_page", this);
        item.destination = Main.class;
        menu.add(item);
        item = new MenuItem();
        item.caption = getLocalizer().getString("navigation_groups_page", this);
        item.destination = Main.class;
        menu.add(item);
        item = new MenuItem();
        item.caption = getLocalizer().getString("navigation_readverses_page", this);
        item.destination = Main.class;
        menu.add(item);
        item = new MenuItem();
        item.caption = getLocalizer().getString("navigation_users_page", this);
        item.destination = Main.class;
        menu.add(item);
        return menu;
    }
}
