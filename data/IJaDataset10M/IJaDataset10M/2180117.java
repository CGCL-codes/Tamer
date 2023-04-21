package automenta.netention.gwtdepr.ui.detail;

import java.util.LinkedList;
import java.util.List;
import automenta.netention.gwtdepr.NetworkService;
import automenta.netention.gwtdepr.NetworkServiceAsync;
import automenta.netention.gwtdepr.data.DetailData;
import automenta.netention.gwtdepr.ui.detail.property.IntPropertyPanel;
import automenta.netention.gwtdepr.ui.detail.property.NodePropertyPanel;
import automenta.netention.gwtdepr.ui.detail.property.PropertyPanel;
import automenta.netention.gwtdepr.ui.detail.property.RealPropertyPanel;
import automenta.netention.gwtdepr.ui.detail.property.StringPropertyPanel;
import automenta.netention.server.value.Property;
import automenta.netention.server.value.PropertyValue;
import automenta.netention.server.value.integer.IntegerEquals;
import automenta.netention.server.value.integer.IntegerIs;
import automenta.netention.server.value.node.NodeEquals;
import automenta.netention.server.value.node.NodeIs;
import automenta.netention.server.value.node.NodeNotEquals;
import automenta.netention.server.value.real.RealEquals;
import automenta.netention.server.value.real.RealIs;
import automenta.netention.server.value.string.StringEquals;
import automenta.netention.server.value.string.StringIs;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

public abstract class NodeEditPanel extends DockPanel {

    final NetworkServiceAsync netService = GWT.create(NetworkService.class);

    private NodePropertiesPanel propertyList;

    private NodeEditMenu menu;

    private DetailData nodeData;

    private List<PropertyPanel> propertyPanels = new LinkedList();

    public static class PatternInfoPanel extends DockPanel {

        public PatternInfoPanel(String patternID) {
            super();
            add(new Label("Information about " + patternID), CENTER);
            add(new Button("Remove Pattern"), SOUTH);
        }
    }

    public static class NodePropertiesPanel extends FlowPanel {

        public NodePropertiesPanel() {
            super();
            addStyleName("NodePropertiesPanel");
        }
    }

    public NodeEditPanel(DetailData pd) {
        super();
        setStyleName("NodeEditPanel");
        DockPanel topPanel = new DockPanel();
        topPanel.addStyleName("NodeEditTopPanel");
        add(topPanel, NORTH);
        menu = new NodeEditMenu() {

            @Override
            protected void refreshProperties() {
                NodeEditPanel.this.refresh();
            }
        };
        topPanel.add(menu, CENTER);
        topPanel.setCellWidth(menu, "100%");
        Button saveButton = new Button("Save");
        saveButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                onSaveNode();
            }
        });
        saveButton.addStyleName("MenuSaveButton");
        topPanel.add(saveButton, EAST);
        topPanel.setCellWidth(saveButton, "0%");
        Button deleteButton = new Button("Delete");
        deleteButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                onDeleteNode();
            }
        });
        deleteButton.addStyleName("MenuDeleteButton");
        topPanel.add(deleteButton, EAST);
        topPanel.setCellWidth(deleteButton, "0%");
        propertyList = new NodePropertiesPanel();
        add(propertyList, CENTER);
        setCellVerticalAlignment(propertyList, ALIGN_TOP);
        setCellHeight(propertyList, "100%");
        setNode(pd);
    }

    protected abstract void onDeleteNode();

    protected abstract void onSaveNode();

    protected void refresh() {
        setNode(getNode());
    }

    public void setNode(DetailData pd) {
        propertyList.clear();
        this.nodeData = pd;
        menu.updateMenu(pd);
        propertyPanels.clear();
        synchronized (pd.getProperties()) {
            for (PropertyValue v : pd.getProperties()) {
                Widget pp = newPropertyPanel(v);
                if (pp instanceof PropertyPanel) propertyPanels.add((PropertyPanel) pp);
                propertyList.add(pp);
            }
        }
    }

    public DetailData getNode() {
        return nodeData;
    }

    protected Widget newPropertyPanel(final PropertyValue v) {
        String prop = v.getProperty();
        Widget w = null;
        if (v instanceof RealIs) {
            w = new RealPropertyPanel(prop, (RealIs) v);
        } else if (v instanceof RealEquals) {
            w = new RealPropertyPanel(prop, (RealEquals) v);
        } else if (v instanceof StringIs) {
            w = new StringPropertyPanel(prop, (StringIs) v);
        } else if (v instanceof StringEquals) {
            w = new StringPropertyPanel(prop, (StringEquals) v);
        } else if (v instanceof IntegerIs) {
            w = new IntPropertyPanel(prop, (IntegerIs) v);
        } else if (v instanceof IntegerEquals) {
            w = new IntPropertyPanel(prop, (IntegerEquals) v);
        } else if (v instanceof NodeIs) {
            w = new NodePropertyPanel(prop, (NodeIs) v);
        } else if (v instanceof NodeNotEquals) {
            w = new NodePropertyPanel(prop, (NodeNotEquals) v);
        } else if (v instanceof NodeEquals) {
            w = new NodePropertyPanel(prop, (NodeEquals) v);
        } else if (v instanceof PropertyValue) {
            w = new Button(v.toString());
        }
        if (w instanceof PropertyPanel) {
            final PropertyPanel pp = (PropertyPanel) w;
            pp.setNode(getNode());
            pp.getPropertyLabel().addClickHandler(new ClickHandler() {

                @Override
                public void onClick(ClickEvent event) {
                    ;
                    final PopupPanel menuPopup = new PopupPanel();
                    menuPopup.setAutoHideEnabled(true);
                    menuPopup.showRelativeTo(pp.getPropertyLabel());
                    MenuBar propertyMenu = new MenuBar(true);
                    propertyMenu.addItem("Remove", new Command() {

                        @Override
                        public void execute() {
                            removeProperty(v);
                            menuPopup.hide();
                        }
                    });
                    menuPopup.add(propertyMenu);
                }
            });
        }
        return w;
    }

    public static PropertyValue newDefaultValue(Property prop) {
        return prop.newDefaultValue();
    }

    protected void removeProperty(PropertyValue v) {
        synchronized (getNode().getProperties()) {
            getNode().getProperties().remove(v);
        }
        refresh();
    }

    /** applies all editable widget content to the NodeData (ex: prior to saving) */
    public void widgetsToValue() {
        for (PropertyPanel pp : propertyPanels) pp.widgetToValue();
    }
}
