package gwtm.client.ui.props.tm;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import gwtm.client.MainEntryPoint;
import gwtm.client.services.schema.SchemaServiceInvoker;
import gwtm.client.services.tm.Callback;
import gwtm.client.services.tm.virtual.AssociationVirtual;
import gwtm.client.services.tm.virtual.TopicVirtual;
import gwtm.client.services.tm.virtual.VTopicVirtuals;
import gwtm.client.ui.lists.DlgListTopics;
import gwtm.client.ui.lists.ListTopics;
import gwtm.client.ui.trees.explorer.ModelExpAssociation;
import gwtm.client.ui.trees.explorer.TabItemExplorer;
import net.mygwt.ui.client.Events;
import net.mygwt.ui.client.Style;
import net.mygwt.ui.client.event.BaseEvent;
import net.mygwt.ui.client.event.Listener;
import net.mygwt.ui.client.event.SelectionListener;
import net.mygwt.ui.client.event.ShellListenerAdapter;
import net.mygwt.ui.client.widget.Button;
import net.mygwt.ui.client.widget.Item;
import net.mygwt.ui.client.widget.List;
import net.mygwt.ui.client.widget.menu.Menu;
import net.mygwt.ui.client.widget.menu.MenuItem;
import net.mygwt.ui.client.widget.tree.TreeItem;

/**
 *
 * @author Yorgos
 */
public class PanAerAssociationType extends VerticalPanel {

    private PanPropsAssociation m_panPropsAssociation;

    private AssociationVirtual m_a;

    private ListTopics m_listTopics;

    private Button m_btnSet, m_btnExplore, m_btnRemove;

    public PanAerAssociationType(PanPropsAssociation panProps) {
        m_panPropsAssociation = panProps;
        setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        Panel header = createHeader();
        add(header);
        List list = createEmptyList();
        add(list);
        Panel buttons = createPanelButtons();
        add(buttons);
    }

    protected Panel createHeader() {
        DockPanel pan = new DockPanel();
        pan.setWidth("100%");
        pan.setStyleName("small-title");
        Item itemIcon = new Item("small-title");
        itemIcon.setSize("20px", "100%");
        itemIcon.setIconStyle("icon-associationTypes");
        itemIcon.setEnabled(false);
        pan.add(itemIcon, DockPanel.WEST);
        pan.setCellWidth(itemIcon, "20px");
        Label lab = new Label("Association Type");
        lab.setStyleName("small-title");
        lab.setHeight("100%");
        pan.add(lab, DockPanel.CENTER);
        pan.setCellWidth(lab, "100%");
        pan.setCellVerticalAlignment(lab, HasVerticalAlignment.ALIGN_BOTTOM);
        return pan;
    }

    protected List createEmptyList() {
        m_listTopics = new ListTopics("icon-associationType");
        m_listTopics.setWidth("100%");
        m_listTopics.addListener(Events.DoubleClick, new Listener() {

            public void handleEvent(BaseEvent be) {
                onListDoubleClick();
            }
        });
        Menu contextMenu = new Menu();
        contextMenu.add(getMenuItemSet());
        contextMenu.add(getMenuItemExplore());
        contextMenu.add(getMenuItemRemove());
        m_listTopics.setContextMenu(contextMenu);
        return m_listTopics;
    }

    protected Panel createPanelButtons() {
        HorizontalPanel horiz = new HorizontalPanel();
        m_btnSet = new Button("Set", new SelectionListener() {

            public void widgetSelected(BaseEvent be) {
                onSet();
            }
        });
        horiz.add(m_btnSet);
        m_btnExplore = new Button("Explore", new SelectionListener() {

            public void widgetSelected(BaseEvent be) {
                onExplore();
            }
        });
        horiz.add(m_btnExplore);
        m_btnRemove = new Button("Remove", new SelectionListener() {

            public void widgetSelected(BaseEvent be) {
                onRemove();
            }
        });
        horiz.add(m_btnRemove);
        return horiz;
    }

    public void setAssociation(AssociationVirtual a) {
        m_a = a;
        reset();
    }

    protected MenuItem getMenuItemSet() {
        MenuItem item = new MenuItem(Style.PUSH);
        item.setText("Set");
        item.addSelectionListener(new SelectionListener() {

            public void widgetSelected(BaseEvent be) {
                onSet();
            }
        });
        return item;
    }

    protected MenuItem getMenuItemExplore() {
        MenuItem item = new MenuItem(Style.PUSH);
        item.setText("Explore");
        item.addSelectionListener(new SelectionListener() {

            public void widgetSelected(BaseEvent be) {
                onExplore();
            }
        });
        return item;
    }

    protected MenuItem getMenuItemRemove() {
        MenuItem item = new MenuItem(Style.PUSH);
        item.setText("Remove");
        item.addSelectionListener(new SelectionListener() {

            public void widgetSelected(BaseEvent be) {
                onRemove();
            }
        });
        return item;
    }

    public void reset() {
        if (m_a != null) {
            m_a.getType(new Callback.T() {

                public void retTopic(TopicVirtual t) {
                    setAssociationType(t);
                }
            });
        }
    }

    private void setAssociationType(TopicVirtual t) {
        if (t != null) {
            VTopicVirtuals v = new VTopicVirtuals(t);
            m_listTopics.setTopicVirtuals(v);
            m_btnExplore.setEnabled(true);
            m_btnRemove.setEnabled(true);
        } else {
            VTopicVirtuals v = new VTopicVirtuals();
            m_listTopics.setTopicVirtuals(v);
            m_btnExplore.setEnabled(false);
            m_btnRemove.setEnabled(false);
        }
    }

    public void onListDoubleClick() {
        onExplore();
    }

    public void onExplore() {
        if (m_listTopics.getItemCount() == 0) return;
        TopicVirtual t = m_listTopics.getListItemTopic(0).getTopicVirtual();
        TabItemExplorer expl = MainEntryPoint.getInstance().getTabFolderCenter().showTabExplorer();
        expl.showTopicOnRoot(t);
    }

    public void onSet() {
        final DlgListTopics d = new DlgListTopics("Association Types (from schema)", "icon-associationType");
        ShellListenerAdapter listener = new ShellListenerAdapter() {

            public void shellClosed(BaseEvent be) {
                DlgListTopics _d = (DlgListTopics) be.widget;
                final TopicVirtual t = _d.getSelTopic();
                if (t == null) return;
                m_a.setType(t, new Callback.OK() {

                    public void retOK() {
                        setAssociationType(t);
                        MainEntryPoint.getInstance().setDerty(true);
                        showChangedNames();
                    }
                });
            }
        };
        d.addShellListener(listener);
        d.open();
        Callback.Ts ret = new Callback.Ts() {

            public void retTopics(VTopicVirtuals v) {
                d.getListTopics().setTopicVirtuals(v);
                if (m_listTopics.getItemCount() > 0) {
                    TopicVirtual t = m_listTopics.getListItemTopic(0).getTopicVirtual();
                    d.getListTopics().selectTopicServer(t);
                }
            }
        };
        SchemaServiceInvoker.getAssociationTypes(ret);
    }

    public void onRemove() {
        if (m_listTopics.getItemCount() == 0) return;
        TopicVirtual t = m_listTopics.getListItemTopic(0).getTopicVirtual();
        if (!Window.confirm("Remove Type: " + t.getDisplayName())) return;
        m_a.setType(null, new Callback.OK() {

            public void retOK() {
                m_listTopics.setTopicVirtuals(new VTopicVirtuals());
                MainEntryPoint.getInstance().setDerty(true);
                showChangedNames();
            }
        });
    }

    private void showChangedNames() {
        m_panPropsAssociation.displayAssociationName();
        TabItemExplorer expl = MainEntryPoint.getInstance().getTabFolderCenter().getTabExplorer();
        TreeItem item = expl.getTree().getSelectedItem();
        if (item == null) return;
        Object dat = item.getData();
        if (dat instanceof ModelExpAssociation) {
            ModelExpAssociation mod = (ModelExpAssociation) dat;
            mod.refreshFromServer(item);
        }
    }
}
