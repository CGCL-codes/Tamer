package ua.org.nuos.sdms.clientgui.client.components.tabs;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import ua.org.nuos.sdms.clientgui.client.components.factory.SeparatorFactory;
import ua.org.nuos.sdms.clientgui.client.listeners.TabSelectionChangeListener;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: dio
 * Date: 18.03.12
 * Time: 18:49
 * To change this template use File | Settings | File Templates.
 */
public class CustomTabSet extends HorizontalLayout {

    private final String NAV_WIDTH = "200px";

    private final String TAB_WIDTH = "203px";

    protected List<ICustomTab> tabs;

    protected VerticalLayout content;

    protected VerticalLayout navPanel;

    protected int selectedIndex;

    protected HorizontalLayout tabButtonLayout;

    public CustomTabSet() {
        tabs = new LinkedList<ICustomTab>();
        init();
    }

    protected void init() {
        setSizeFull();
        setSpacing(true);
        navPanel = new VerticalLayout();
        navPanel.setSpacing(true);
        navPanel.setWidth(NAV_WIDTH);
        tabButtonLayout = new HorizontalLayout();
        tabButtonLayout.setHeight("100%");
        tabButtonLayout.setWidth(TAB_WIDTH);
        tabButtonLayout.addComponent(navPanel);
        tabButtonLayout.addComponent(SeparatorFactory.getVertical("width: 1px; height: 100%; background-color: #999999;"));
        tabButtonLayout.setComponentAlignment(navPanel, Alignment.TOP_LEFT);
        tabButtonLayout.addStyleName("padding-right");
        content = new VerticalLayout();
        content.setSizeFull();
        addComponent(tabButtonLayout);
        addComponent(content);
        setExpandRatio(content, 1.f);
    }

    public void addTab(CustomTab tab) {
        tab.setIndex(tabs.size());
        if (tab.isDataViewer()) {
            ((DataTab) tab).setListener(selectionChangeListener());
        }
        if (tabs.isEmpty()) {
            tab.select();
            selectedIndex = 0;
        }
        navPanel.addComponent(tab.getButton());
        tabs.add(tab);
    }

    public void addSeparator(Component separator) {
        if (!tabs.isEmpty()) {
            navPanel.addComponent(SeparatorFactory.getHorizontal());
        }
        navPanel.addComponent(separator);
    }

    protected TabSelectionChangeListener selectionChangeListener() {
        return new TabSelectionChangeListener() {

            @Override
            public void changeSelection(CustomTab tab) {
                if (tab.isSelected()) {
                    return;
                }
                for (ICustomTab t : tabs) {
                    t.deselect();
                }
                if (tab.isDataViewer) {
                    refreshContent(((DataTab) tab).getDataContainer());
                    selectedIndex = tab.getIndex();
                }
            }
        };
    }

    private void refreshContent(Component dataContainer) {
        content.removeAllComponents();
        content.addComponent(dataContainer);
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }
}
