package org.nsu.learn.gui.client.window.workspace;

import java.util.ArrayList;
import java.util.List;
import org.nsu.learn.gui.client.TestEgeService;
import org.nsu.learn.gui.client.dialog.edit.TestTaskEditDialog;
import org.nsu.learn.gui.client.dialog.view.TestTaskViewDialog;
import org.nsu.learn.gui.client.window.model.TestTaskModel;
import com.extjs.gxt.ui.client.data.BaseModel;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.event.WindowEvent;
import com.extjs.gxt.ui.client.event.WindowListener;
import com.extjs.gxt.ui.client.store.GroupingStore;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridGroupRenderer;
import com.extjs.gxt.ui.client.widget.grid.GroupColumnData;
import com.extjs.gxt.ui.client.widget.grid.GroupingView;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author makarov
 * @version 1.0.14.02.2011
 *
 */
public class TasksEgeTestWorkspace extends AbstractListWorkspace {

    private Grid<TestTaskModel> grid;

    public TasksEgeTestWorkspace() {
        super();
        cp.setHeading(MESSAGES.workTypes());
        GroupingStore<TestTaskModel> store = new GroupingStore<TestTaskModel>();
        store.groupBy("taskName");
        grid = new Grid<TestTaskModel>(store, cm);
        grid.setStyleAttribute("borderTop", "none");
        grid.setBorders(false);
        grid.setStripeRows(true);
        grid.setColumnLines(true);
        grid.setColumnReordering(true);
        grid.setHeight(500);
        GroupingView view = new GroupingView();
        view.setShowGroupedColumn(false);
        view.setForceFit(true);
        view.setGroupRenderer(new GridGroupRenderer() {

            public String render(GroupColumnData data) {
                String f = cm.getColumnById(data.field).getHeader();
                String l = data.models.size() == 1 ? "Задача" : "Задачи";
                return f + ": " + data.group + " (" + data.models.size() + " " + l + ")";
            }
        });
        grid.setView(view);
        cp.add(grid);
        refreshData();
    }

    @Override
    protected ToolBar getToolBarElements() {
        ToolBar toolBar = new ToolBar();
        Button viewButton = new Button(MESSAGES.viewButton());
        viewButton.addSelectionListener(new SelectionListener<ButtonEvent>() {

            public void componentSelected(ButtonEvent ce) {
                TestTaskModel testTaskModel = grid.getSelectionModel().getSelectedItem();
                if (testTaskModel != null) {
                    TestTaskViewDialog testTaskViewDialog = new TestTaskViewDialog(testTaskModel);
                    testTaskViewDialog.show();
                }
            }
        });
        toolBar.add(viewButton);
        Button editButton = new Button(MESSAGES.editButton());
        editButton.addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                TestTaskModel testTaskModel = grid.getSelectionModel().getSelectedItem();
                if (testTaskModel != null) {
                    TestTaskEditDialog testTaskEditDialog = new TestTaskEditDialog(testTaskModel);
                    testTaskEditDialog.addWindowListener(new WindowListener() {

                        @Override
                        public void windowHide(WindowEvent we) {
                            super.windowHide(we);
                            if (we.getButtonClicked() != null && we.getButtonClicked().getText().equals(MESSAGES.saveButton())) {
                                refreshData();
                            }
                        }
                    });
                    testTaskEditDialog.show();
                }
            }
        });
        toolBar.add(editButton);
        Button createButton = new Button(MESSAGES.createButton());
        createButton.addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                TestTaskEditDialog testTaskEditDialog = new TestTaskEditDialog(null);
                testTaskEditDialog.addWindowListener(new WindowListener() {

                    @Override
                    public void windowHide(WindowEvent we) {
                        super.windowHide(we);
                        refreshData();
                    }
                });
                testTaskEditDialog.show();
            }
        });
        toolBar.add(createButton);
        toolBar.add(new Label("Группировать по: "));
        ComboBox<GroupModel> groupComboBox = new ComboBox<TasksEgeTestWorkspace.GroupModel>();
        ListStore<GroupModel> store = new ListStore<TasksEgeTestWorkspace.GroupModel>();
        GroupModel model = new GroupModel("taskName", "Имя задачи");
        store.add(new GroupModel("themeBlock", "Тематический блок"));
        store.add(model);
        store.add(new GroupModel("testLevel", "Уровень задачи"));
        groupComboBox.setValue(model);
        groupComboBox.setStore(store);
        groupComboBox.setTriggerAction(TriggerAction.ALL);
        groupComboBox.setDisplayField("displayValue");
        groupComboBox.setValueField("groupName");
        groupComboBox.addSelectionChangedListener(new SelectionChangedListener<TasksEgeTestWorkspace.GroupModel>() {

            @Override
            public void selectionChanged(SelectionChangedEvent<GroupModel> se) {
                GroupingStore<TestTaskModel> store = (GroupingStore<TestTaskModel>) grid.getStore();
                store.groupBy(se.getSelectedItem().getGroupName());
                grid.getView().refresh(true);
            }
        });
        toolBar.add(groupComboBox);
        return toolBar;
    }

    @Override
    protected List<ColumnConfig> getColumnConfigs() {
        List<ColumnConfig> configs = new ArrayList<ColumnConfig>();
        ColumnConfig column = new ColumnConfig();
        column.setId("id");
        column.setHeader("ID");
        column.setWidth(100);
        column.setRowHeader(true);
        configs.add(column);
        column = new ColumnConfig();
        column.setId("text");
        column.setHeader("Текст задачи");
        column.setWidth(150);
        column.setRowHeader(true);
        configs.add(column);
        column = new ColumnConfig();
        column.setId("themeBlock");
        column.setHeader("Блок");
        column.setWidth(100);
        column.setRowHeader(true);
        configs.add(column);
        column = new ColumnConfig();
        column.setId("answer");
        column.setHeader("Ответ");
        column.setWidth(50);
        column.setRowHeader(true);
        configs.add(column);
        column = new ColumnConfig();
        column.setId("taskName");
        column.setHeader("Имя задачи");
        column.setWidth(100);
        column.setRowHeader(true);
        configs.add(column);
        column = new ColumnConfig();
        column.setId("testLevel");
        column.setHeader("Сложность задачи");
        column.setWidth(100);
        column.setRowHeader(true);
        configs.add(column);
        column = new ColumnConfig();
        column.setId("point");
        column.setHeader("Баллы");
        column.setWidth(50);
        column.setRowHeader(true);
        configs.add(column);
        column = new ColumnConfig();
        column.setId("maxPoint");
        column.setHeader("Максимальный балл");
        column.setWidth(70);
        column.setRowHeader(true);
        configs.add(column);
        column = new ColumnConfig();
        column.setId("time");
        column.setHeader("Время(сек)");
        column.setWidth(70);
        column.setRowHeader(true);
        configs.add(column);
        return configs;
    }

    @Override
    protected void refreshData() {
        grid.mask();
        TestEgeService.App.getInstance().getAllTestTaskModels(new AsyncCallback<List<TestTaskModel>>() {

            @Override
            public void onFailure(Throwable caught) {
            }

            @Override
            public void onSuccess(List<TestTaskModel> result) {
                grid.getStore().removeAll();
                grid.getStore().add(result);
                grid.getView().refresh(true);
                grid.repaint();
                grid.unmask();
            }
        });
    }

    private class GroupModel extends BaseModel {

        public GroupModel() {
        }

        public GroupModel(String groupName, String displayValue) {
            set("groupName", groupName);
            set("displayValue", displayValue);
        }

        public String getGroupName() {
            return (String) get("groupName");
        }

        public String getDisplayName() {
            return (String) get("displayValue");
        }
    }
}
