package pl.lims.client.tabs;

import java.util.List;
import pl.lims.client.services.SetupManager;
import pl.lims.client.services.SetupManagerAsync;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.DataList;
import com.extjs.gxt.ui.client.widget.DataListItem;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;

public class ConfigurationTab extends HorizontalPanel {

    private final SetupManagerAsync setupManager = GWT.create(SetupManager.class);

    @SuppressWarnings("deprecation")
    final DataList statusList = new DataList();

    final DataList categoryList = new DataList();

    public ConfigurationTab() {
    }

    @Override
    protected void onLoad() {
        setSpacing(50);
        add(loadStatusconfigPanel());
        add(loadCategoryConfigPanel());
    }

    private LayoutContainer loadCategoryConfigPanel() {
        LayoutContainer categoryConfig = new LayoutContainer();
        categoryConfig.add(new Label("Categories"));
        final TextBox status = new TextBox();
        final Button addButton = new Button("Add category");
        addButton.addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                setupManager.addCategory(status.getText(), new AsyncCallback<String>() {

                    public void onFailure(Throwable caught) {
                        MessageBox.alert("Error!", caught.getMessage(), null);
                    }

                    public void onSuccess(String result) {
                        Info.display("Category adding result.", result);
                        loadCategoryList();
                    }
                });
            }
        });
        loadCategoryList();
        HorizontalPanel hp = new HorizontalPanel();
        hp.add(status);
        hp.add(addButton);
        hp.setWidth(233);
        categoryConfig.add(hp);
        categoryList.setTitle("Existing categories");
        Menu contextMenu = new Menu();
        MenuItem remove = new MenuItem();
        remove.setText("Remove selected");
        remove.setIconStyle("icon-delete");
        remove.addSelectionListener(new SelectionListener<MenuEvent>() {

            @Override
            public void componentSelected(MenuEvent ce) {
                DataListItem item = categoryList.getSelectedItem();
                if (item == null) Info.display("Error", "Nothing is selected!"); else {
                    String name = item.getText();
                    Info.display("Category delete", "Deleting category " + name);
                    setupManager.removeCategory(name, new AsyncCallback<String>() {

                        public void onFailure(Throwable caught) {
                            MessageBox.alert("Error", "Error while deleting category.", null);
                        }

                        ;

                        public void onSuccess(String result) {
                            Info.display("Category deleting", result);
                            loadCategoryList();
                        }
                    });
                }
            }
        });
        contextMenu.add(remove);
        categoryList.setContextMenu(contextMenu);
        categoryList.setWidth(233);
        categoryConfig.add(categoryList);
        return categoryConfig;
    }

    private LayoutContainer loadStatusconfigPanel() {
        LayoutContainer statusConfig = new LayoutContainer();
        statusConfig.add(new Label("Incident Statuses"));
        final TextBox status = new TextBox();
        final Button addButton = new Button("Add status");
        addButton.addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                setupManager.addStatus(status.getText(), new AsyncCallback<String>() {

                    public void onFailure(Throwable caught) {
                        MessageBox.alert("Error!", caught.getMessage(), null);
                    }

                    public void onSuccess(String result) {
                        Info.display("Status adding result.", result);
                        loadStatusList();
                    }
                });
            }
        });
        loadStatusList();
        HorizontalPanel hp = new HorizontalPanel();
        hp.add(status);
        hp.add(addButton);
        hp.setWidth(220);
        statusConfig.add(hp);
        statusList.setTitle("Existing statuses");
        Menu contextMenu = new Menu();
        MenuItem remove = new MenuItem();
        remove.setText("Remove selected");
        remove.setIconStyle("icon-delete");
        remove.addSelectionListener(new SelectionListener<MenuEvent>() {

            @Override
            public void componentSelected(MenuEvent ce) {
                DataListItem item = statusList.getSelectedItem();
                if (item == null) Info.display("Error", "Nothing is selected!"); else {
                    String name = item.getText();
                    Info.display("Status delete", "Deleting status " + name);
                    setupManager.removeStatus(name, new AsyncCallback<String>() {

                        public void onFailure(Throwable caught) {
                            MessageBox.alert("Error", "Error while deleting status.", null);
                        }

                        ;

                        public void onSuccess(String result) {
                            Info.display("Status deleting", result);
                            loadStatusList();
                        }

                        ;
                    });
                }
            }
        });
        contextMenu.add(remove);
        statusList.setContextMenu(contextMenu);
        statusList.setWidth(220);
        statusConfig.add(statusList);
        return statusConfig;
    }

    private void loadStatusList() {
        setupManager.getStatusNames(new AsyncCallback<List<String>>() {

            public void onFailure(Throwable caught) {
                MessageBox.alert("Error!", caught.getMessage(), null);
            }

            public void onSuccess(List<String> result) {
                statusList.removeAll();
                for (String name : result) {
                    DataListItem item = new DataListItem();
                    item.setText(name);
                    statusList.add(item);
                }
            }
        });
    }

    private void loadCategoryList() {
        setupManager.getCategories(new AsyncCallback<List<String>>() {

            public void onFailure(Throwable caught) {
                MessageBox.alert("Error!", caught.getMessage(), null);
            }

            @SuppressWarnings("deprecation")
            public void onSuccess(java.util.List<String> result) {
                categoryList.removeAll();
                for (String name : result) {
                    DataListItem item = new DataListItem();
                    item.setText(name);
                    categoryList.add(item);
                }
            }
        });
    }

    ;
}
