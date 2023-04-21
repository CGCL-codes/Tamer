package org.posper.gui.panels.customer;

import javax.swing.ListCellRenderer;
import net.adrianromero.data.gui.ListCellRendererBasic;
import net.adrianromero.data.loader.ComparatorCreator;
import net.adrianromero.data.loader.StaticSentenceHibernateQuery;
import net.adrianromero.data.loader.TableDefinitionHibernate;
import net.adrianromero.data.loader.Vectorer;
import net.adrianromero.data.user.ListProvider;
import net.adrianromero.data.user.ListProviderHibernate;
import net.adrianromero.format.Formats;
import net.adrianromero.tpv.forms.AppLocal;
import net.adrianromero.tpv.panels.JPanelTable;
import org.posper.gui.AppView;
import org.posper.hibernate.CustomerGroup;

/**
 * This panel allows the editing of groups with which customers may be associated. 
 * 
 * @author Aaron Luchko <aaron.luchko@oxn.ca>
 */
public class CustomerGroupsPanel extends JPanelTable {

    private CustomerGroupsEditor jeditor;

    private TableDefinitionHibernate<CustomerGroup> tgroups;

    public CustomerGroupsPanel(AppView oApp) {
        super(oApp);
        tgroups = new TableDefinitionHibernate<CustomerGroup>(CustomerGroup.class, new StaticSentenceHibernateQuery<CustomerGroup>("from CustomerGroup order by name"), new String[] { "Name" }, new Formats[] { Formats.STRING }, new String[] { "Name" }, new Formats[] { Formats.STRING });
        jeditor = new CustomerGroupsEditor(m_Dirty);
    }

    public String getTitle() {
        return AppLocal.getInstance().getIntString("Menu.CustomerGroups");
    }

    @Override
    public CustomerGroupsEditor getEditor() {
        return jeditor;
    }

    @Override
    public ListProvider<CustomerGroup> getListProvider() {
        return new ListProviderHibernate<CustomerGroup>(tgroups);
    }

    @Override
    public Vectorer getVectorer() {
        return tgroups.getVectorerBasic();
    }

    @Override
    public ComparatorCreator getComparatorCreator() {
        return tgroups.getComparatorCreator();
    }

    @Override
    public ListCellRenderer getListCellRenderer() {
        return new ListCellRendererBasic(tgroups.getRenderStringBasic());
    }

    public void refreshView() {
    }
}
