package name.emu.webapp.kos.wicket.pages.account;

import java.util.ArrayList;
import java.util.List;
import name.emu.webapp.kos.domain.Dummy;
import name.emu.webapp.kos.service.data.AccountSortSpec;
import name.emu.webapp.kos.service.data.AccountWithoutPassword;
import name.emu.webapp.kos.wicket.dataprovider.AccountsDataProvider;
import name.emu.webapp.kos.wicket.pages.ListPage;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

@AuthorizeInstantiation("USER")
public class ListSecretAccountPage extends ListPage {

    public ListSecretAccountPage() {
        List<IColumn<AccountWithoutPassword>> columns = new ArrayList<IColumn<AccountWithoutPassword>>();
        columns.add(new PropertyColumn<AccountWithoutPassword>(new Model<String>("System"), AccountSortSpec.SortCriteria.SYSTEM.name(), "system"));
        columns.add(new PropertyColumn<AccountWithoutPassword>(new Model<String>("User"), AccountSortSpec.SortCriteria.USER_NAME.name(), "userName"));
        columns.add(new AbstractColumn<AccountWithoutPassword>(new Model<String>("Details")) {

            public void populateItem(Item<ICellPopulator<AccountWithoutPassword>> cellItem, String componentId, IModel<AccountWithoutPassword> rowModel) {
                cellItem.add(new ActionPanel(componentId, rowModel));
            }
        });
        DataTable<AccountWithoutPassword> dataTable;
        dataTable = new DefaultDataTable<AccountWithoutPassword>("secretAccounts", columns, new AccountsDataProvider(), 25);
        add(dataTable);
    }

    private static class ActionPanel extends Panel {

        /**
         * @param id
         *            component id
         * @param model
         *            model for contact
         */
        public ActionPanel(String id, IModel<AccountWithoutPassword> model) {
            super(id, model);
            AccountWithoutPassword account = model.getObject();
            BookmarkablePageLink<Dummy> link;
            PageParameters parameters = new PageParameters();
            parameters.add(EditSecretAccountPage.ACCOUNT_PARAM, account.getSecretAccountId());
            link = new BookmarkablePageLink<Dummy>("link", EditSecretAccountPage.class, parameters);
            link.add(new Label("linkText", "view/edit"));
            add(link);
        }
    }
}
