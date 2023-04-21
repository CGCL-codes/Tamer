package com.hy.mydesktop.client.mvc.form.textfield;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import com.extjs.gxt.desktop.client.Desktop;
import com.extjs.gxt.desktop.client.Shortcut;
import com.extjs.gxt.desktop.client.StartMenu;
import com.extjs.gxt.desktop.client.TaskBar;
import com.extjs.gxt.samples.resources.client.TestData;
import com.extjs.gxt.samples.resources.client.model.Stock;
import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.data.BaseTreeModel;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.ModelIconProvider;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Controller;
import com.extjs.gxt.ui.client.mvc.View;
import com.extjs.gxt.ui.client.store.GroupingStore;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.util.IconHelper;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.button.ToolButton;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridGroupRenderer;
import com.extjs.gxt.ui.client.widget.grid.GroupColumnData;
import com.extjs.gxt.ui.client.widget.grid.GroupingView;
import com.extjs.gxt.ui.client.widget.layout.AccordionLayout;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuBar;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.extjs.gxt.ui.client.widget.treepanel.TreePanel;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.hy.mydesktop.client.component.event.AppEvents;
import com.hy.mydesktop.client.component.factory.GxtComponentFactory;
import com.hy.mydesktop.client.component.factory.GxtDesktopComponentFactory;
import com.hy.mydesktop.client.component.meta.GxtComponentMetaModel;
import com.hy.mydesktop.client.component.meta.GxtComponentMetaNodeModel;
import com.hy.mydesktop.client.component.mvc.event.EventStructureMetaModel;
import com.hy.mydesktop.client.component.type.ComponentTypeEnum;
import com.hy.mydesktop.client.mvc.base.BaseController;
import com.hy.mydesktop.client.mvc.desktop.DesktopView;

public class TextFieldView extends View {

    private Window window;

    private Desktop desktop = null;

    private TextField<String> textField = null;

    public TextField<String> getTextField() {
        return textField;
    }

    public void setTextField(TextField<String> textField) {
        this.textField = textField;
    }

    public TextFieldView(Controller controller) {
        super(controller);
    }

    @Override
    protected void handleEvent(AppEvent event) {
        if (desktop == null) {
            desktop = Registry.get("desktop");
        }
        Map<String, Object> map = event.getData();
        if (AppEvents.DESKTOP_WINDOWAPPLICATION_INIT == event.getType()) {
            String viewDataCode = ((BaseController) this.getController()).getViewData();
            GxtComponentMetaNodeModel gxtComponentMetaNodeModel = null;
            if (viewDataCode != null) {
                System.out.println("viewDataCode代码是：" + viewDataCode + "执行getGxtComponentMetaNodeModel ()");
                if (viewDataCode.equals("menuBar_viewData_init_01")) {
                }
            }
            gxtComponentMetaNodeModel = ((BaseController) (this.getController())).getGxtComponentMetaNodeModel();
            GxtComponentMetaModel gxtComponentMetaModel = new GxtComponentMetaModel();
            List<GxtComponentMetaNodeModel> list = new LinkedList<GxtComponentMetaNodeModel>();
            list.add(gxtComponentMetaNodeModel);
            gxtComponentMetaModel.setList(list);
            textField = GxtComponentFactory.createGxtComponent(gxtComponentMetaModel);
            BaseController parentController = (BaseController) (((BaseController) this.getController()).getParent());
            Map<BaseController, Component> controllerandcomponent = (Map<BaseController, Component>) map.get("controllerandcomponent");
            LayoutContainer layoutContainer = (LayoutContainer) controllerandcomponent.get(parentController);
            if (layoutContainer != null) {
                layoutContainer.add(textField);
            }
            System.err.println("textField 的parent" + layoutContainer);
        }
        ;
        if (AppEvents.WINDOW_ININT == event.getType()) {
            Shortcut s111 = new Shortcut();
            s111.setText("Grid Wi11111111111111ndow");
            s111.setId("grid-win-sho111111111111111rtcut");
            desktop = (Desktop) Registry.get(DesktopView.DESKTOP);
            if (desktop == null) {
                desktop = new Desktop();
            }
            System.out.println(desktop);
            desktop.addShortcut(s111);
        }
        ;
        if (AppEvents.DESKTOP_SHORTCUT_ONCLICK_INIT == event.getType()) {
            this.createDynamicDesktop2(event);
        }
    }

    private GxtComponentMetaModel getDesktopMetaModel() {
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("component_model_type", ComponentTypeEnum.DESKTOP_MODEL);
        GxtComponentMetaNodeModel node1 = new GxtComponentMetaNodeModel(map1, 1, 6);
        Map<String, Object> map2 = new HashMap<String, Object>();
        map2.put("component_model_type", ComponentTypeEnum.SHORTCUT_MODEL);
        map2.put("appEventType", AppEvents.DESKTOP_SHORTCUT_ONCLICK_INIT);
        GxtComponentMetaNodeModel node2 = new GxtComponentMetaNodeModel(map2, 2, 3);
        Map<String, Object> map3 = new HashMap<String, Object>();
        map3.put("component_model_type", ComponentTypeEnum.SHORTCUT_MODEL);
        GxtComponentMetaNodeModel node3 = new GxtComponentMetaNodeModel(map3, 4, 5);
        List<GxtComponentMetaNodeModel> list = new ArrayList<GxtComponentMetaNodeModel>();
        list.add(node1);
        list.add(node2);
        list.add(node3);
        GxtComponentMetaModel gxtComponentMetaModel = new GxtComponentMetaModel();
        gxtComponentMetaModel.setList(list);
        return gxtComponentMetaModel;
    }

    public void createDynamicWindow(AppEvent event) {
        ;
        List<GxtComponentMetaNodeModel> list = this.getgComponentMetaNodeModelList();
        GxtComponentMetaModel gxtComponentMetaModel = new GxtComponentMetaModel(list);
        Component component = GxtComponentFactory.createGxtComponent(gxtComponentMetaModel);
    }

    public void createDynamicDesktop2(AppEvent event) {
        GxtDesktopComponentFactory.createGxtComponent(desktop, (GxtComponentMetaModel) event.getData());
    }

    public void createDynamicDesktop(AppEvent event) {
        SelectionListener<MenuEvent> menuListener = new SelectionListener<MenuEvent>() {

            @Override
            public void componentSelected(MenuEvent me) {
                itemSelected(me);
            }
        };
        SelectionListener<ComponentEvent> shortcutListener = new SelectionListener<ComponentEvent>() {

            @Override
            public void componentSelected(ComponentEvent ce) {
                itemSelected(ce);
            }
        };
        Window gridWindow = createGridWindow();
        Shortcut s1 = new Shortcut();
        s1.setText("Grid Window");
        s1.setId("grid-win-shortcut");
        s1.setTitle("tiltle");
        s1.setData("window", gridWindow);
        s1.addSelectionListener(shortcutListener);
        desktop.addShortcut(s1);
        Window accordionWindow = createAccordionWindow();
        Shortcut s2 = new Shortcut();
        s2.setText("Accordion Window");
        s2.setId("acc-win-shortcut");
        s2.setData("window", accordionWindow);
        s2.addSelectionListener(shortcutListener);
        desktop.addShortcut(s2);
        Shortcut shortcut = new Shortcut();
        shortcut.setText("MyAccordionWindow");
        shortcut.setData("window", this.getDynamiCaiGouPlan());
        shortcut.setIconStyle("user");
        shortcut.addSelectionListener(shortcutListener);
        desktop.addShortcut(shortcut);
        TaskBar taskBar = desktop.getTaskBar();
        StartMenu startMenu = taskBar.getStartMenu();
        startMenu.setIconStyle("user");
        startMenu.setHeading("The StartMenu");
        MenuItem menuItem = new MenuItem();
        menuItem.setText("accordinWindow");
        menuItem.setTitle("title");
        menuItem.setIcon(IconHelper.createStyle("accordion"));
        menuItem.setData("window", this.createGridWindow());
        menuItem.addSelectionListener(menuListener);
        startMenu.add(menuItem);
    }

    /**
	 * 
	 * <ul>
	 * <li>方法含义：用来响应桌面的快捷方式的单击事件：模拟视窗系统中，窗口的所以显示效果</li>
	 * <li>方法作者：花宏宇</li>
	 * <li>编写日期：2010-9-8；时间：下午下午03:32:05</li>
	 * </ul>
	 * <ul>
	 * <b>修订编号：</b>
	 * <li>修订日期：</li>
	 * <li>修订作者：</li>
	 * <li>修订原因：</li>
	 * <li>修订内容：</li>
	 * </ul>
	 * @param ce
	 */
    private void itemSelected(ComponentEvent ce) {
        Window w;
        if (ce instanceof MenuEvent) {
            MenuEvent me = (MenuEvent) ce;
            w = me.getItem().getData("window");
        } else {
            w = ce.getComponent().getData("window");
        }
        if (!desktop.getWindows().contains(w)) {
            desktop.addWindow(w);
        }
        if (w != null && !w.isVisible()) {
            w.show();
        } else {
            w.toFront();
        }
    }

    private Window createAccordionWindow() {
        final Window w = new Window();
        w.setMinimizable(true);
        w.setMaximizable(true);
        w.setIcon(IconHelper.createStyle("accordion"));
        w.setHeading("Accordion Window");
        w.setWidth(200);
        w.setHeight(350);
        ToolBar toolBar = new ToolBar();
        Button item = new Button();
        item.setIcon(IconHelper.createStyle("icon-connect"));
        toolBar.add(item);
        toolBar.add(new SeparatorToolItem());
        w.setTopComponent(toolBar);
        item = new Button();
        item.setIcon(IconHelper.createStyle("icon-user-add"));
        toolBar.add(item);
        item = new Button();
        item.setIcon(IconHelper.createStyle("icon-user-delete"));
        toolBar.add(item);
        w.setLayout(new AccordionLayout());
        ContentPanel cp = new ContentPanel();
        cp.setAnimCollapse(false);
        cp.setHeading("Online Users");
        cp.setScrollMode(Scroll.AUTO);
        cp.getHeader().addTool(new ToolButton("x-tool-refresh"));
        w.add(cp);
        TreeStore<ModelData> store = new TreeStore<ModelData>();
        TreePanel<ModelData> tree = new TreePanel<ModelData>(store);
        tree.setIconProvider(new ModelIconProvider<ModelData>() {

            public AbstractImagePrototype getIcon(ModelData model) {
                if (model.get("icon") != null) {
                    return IconHelper.createStyle((String) model.get("icon"));
                } else {
                    return null;
                }
            }
        });
        tree.setDisplayProperty("name");
        ModelData m = newItem("Family", null);
        store.add(m, false);
        tree.setExpanded(m, true);
        store.add(m, newItem("Darrell", "user"), false);
        store.add(m, newItem("Maro", "user-girl"), false);
        store.add(m, newItem("Lia", "user-kid"), false);
        store.add(m, newItem("Alec", "user-kid"), false);
        store.add(m, newItem("Andrew", "user-kid"), false);
        m = newItem("Friends", null);
        store.add(m, false);
        tree.setExpanded(m, true);
        store.add(m, newItem("Bob", "user"), false);
        store.add(m, newItem("Mary", "user-girl"), false);
        store.add(m, newItem("Sally", "user-girl"), false);
        store.add(m, newItem("Jack", "user"), false);
        cp.add(tree);
        cp = new ContentPanel();
        cp.setAnimCollapse(false);
        cp.setHeading("Settings");
        cp.setBodyStyleName("pad-text");
        cp.addText(TestData.DUMMY_TEXT_SHORT);
        w.add(cp);
        cp = new ContentPanel();
        cp.setAnimCollapse(false);
        cp.setHeading("Stuff");
        cp.setBodyStyleName("pad-text");
        cp.addText(TestData.DUMMY_TEXT_SHORT);
        w.add(cp);
        cp = new ContentPanel();
        cp.setAnimCollapse(false);
        cp.setHeading("More Stuff");
        cp.setBodyStyleName("pad-text");
        cp.addText(TestData.DUMMY_TEXT_SHORT);
        w.add(cp);
        return w;
    }

    private Window createGridWindow() {
        Window w = new Window();
        w.setIcon(IconHelper.createStyle("icon-grid"));
        w.setMinimizable(true);
        w.setMaximizable(true);
        w.setHeading("Grid Window");
        w.setSize(500, 400);
        w.setLayout(new FitLayout());
        GroupingStore<Stock> store = new GroupingStore<Stock>();
        store.add(TestData.getCompanies());
        store.groupBy("industry");
        ColumnConfig company = new ColumnConfig("name", "Company", 60);
        ColumnConfig price = new ColumnConfig("open", "Price", 20);
        price.setNumberFormat(NumberFormat.getCurrencyFormat());
        ColumnConfig change = new ColumnConfig("change", "Change", 20);
        ColumnConfig industry = new ColumnConfig("industry", "Industry", 20);
        ColumnConfig last = new ColumnConfig("date", "Last Updated", 20);
        last.setDateTimeFormat(DateTimeFormat.getFormat("MM/dd/y"));
        List<ColumnConfig> config = new ArrayList<ColumnConfig>();
        config.add(company);
        config.add(price);
        config.add(change);
        config.add(industry);
        config.add(last);
        final ColumnModel cm = new ColumnModel(config);
        GroupingView view = new GroupingView();
        view.setForceFit(true);
        view.setGroupRenderer(new GridGroupRenderer() {

            public String render(GroupColumnData data) {
                String f = cm.getColumnById(data.field).getHeader();
                String l = data.models.size() == 1 ? "Item" : "Items";
                return f + ": " + data.group + " (" + data.models.size() + " " + l + ")";
            }
        });
        Grid<Stock> grid = new Grid<Stock>(store, cm);
        grid.setView(view);
        grid.setBorders(true);
        w.add(grid);
        return w;
    }

    public Component getDynamiCaiGouPlan() {
        List<GxtComponentMetaNodeModel> list = this.getgComponentMetaNodeModelList();
        GxtComponentMetaModel gxtComponentMetaModel = new GxtComponentMetaModel(list);
        return GxtComponentFactory.createGxtComponent(gxtComponentMetaModel);
    }

    public List<GxtComponentMetaNodeModel> getgComponentMetaNodeModelList() {
        Map<String, Object> window = new HashMap<String, Object>();
        window.put("component_model_type", ComponentTypeEnum.WINDOW_MODEL);
        window.put("title", "采购材料入库计划");
        window.put("width", 1000);
        window.put("hight", 600);
        window.put("resizable", false);
        window.put("maxmizable", true);
        window.put("minmizable", true);
        window.put("layout", ComponentTypeEnum.FITLAYOUT_MODEL);
        GxtComponentMetaNodeModel node2 = new GxtComponentMetaNodeModel(window, 1, 44);
        Map<String, Object> panel = new HashMap<String, Object>();
        panel.put("component_model_type", ComponentTypeEnum.CONTENTPANEL_MODEL);
        panel.put("headervisible", false);
        panel.put("width", 1000);
        panel.put("hight", 600);
        GxtComponentMetaNodeModel node1 = new GxtComponentMetaNodeModel(panel, 2, 43);
        List<BaseTreeModel> listModel = new ArrayList<BaseTreeModel>();
        BaseTreeModel file = new BaseTreeModel();
        file.set("name", "文件");
        BaseTreeModel file1 = new BaseTreeModel();
        file1.set("name", "新建");
        file.add(file1);
        BaseTreeModel file2 = new BaseTreeModel();
        file2.set("name", "保存");
        file.add(file2);
        BaseTreeModel file3 = new BaseTreeModel();
        file3.set("name", "打印");
        file.add(file3);
        BaseTreeModel file4 = new BaseTreeModel();
        file4.set("name", "预览");
        file.add(file4);
        BaseTreeModel file5 = new BaseTreeModel();
        file5.set("name", "退出");
        file.add(file5);
        BaseTreeModel edit = new BaseTreeModel();
        edit.set("name", "编辑");
        BaseTreeModel edit1 = new BaseTreeModel();
        edit1.set("name", "引入");
        edit.add(edit1);
        BaseTreeModel edit2 = new BaseTreeModel();
        edit2.set("name", "查找");
        edit.add(edit2);
        BaseTreeModel view = new BaseTreeModel();
        view.set("name", "查看");
        BaseTreeModel view1 = new BaseTreeModel();
        view1.set("name", "基础资料查看");
        view.add(view1);
        BaseTreeModel view2 = new BaseTreeModel();
        view2.set("name", "编码查看");
        view.add(view2);
        BaseTreeModel view3 = new BaseTreeModel();
        view3.set("name", "采购价格查询");
        view.add(view3);
        BaseTreeModel view4 = new BaseTreeModel();
        view4.set("name", "历史价格查询");
        view.add(view4);
        BaseTreeModel view5 = new BaseTreeModel();
        view5.set("name", "库存状态查询");
        view.add(view5);
        BaseTreeModel view51 = new BaseTreeModel();
        view51.set("name", "在库库存查询");
        view5.add(view51);
        BaseTreeModel view52 = new BaseTreeModel();
        view52.set("name", "在途库存查询");
        view5.add(view52);
        BaseTreeModel option = new BaseTreeModel();
        option.set("name", "选项");
        BaseTreeModel option1 = new BaseTreeModel();
        option1.set("name", "获取批次信息");
        option.add(option1);
        BaseTreeModel option2 = new BaseTreeModel();
        option2.set("name", "物料配套查询录入");
        option.add(option2);
        listModel.add(file);
        listModel.add(edit);
        listModel.add(view);
        listModel.add(option);
        Map<String, Object> menubar = new HashMap<String, Object>();
        menubar.put("component_model_type", ComponentTypeEnum.MENUBAR_MODEL);
        menubar.put("menubaritem", listModel);
        GxtComponentMetaNodeModel node3 = new GxtComponentMetaNodeModel(menubar, 3, 4);
        List<String> btn = new ArrayList<String>();
        btn.add("新建");
        btn.add("保存");
        btn.add("打开");
        btn.add("引入");
        btn.add("导出");
        Map<String, Object> toolbar = new HashMap<String, Object>();
        toolbar.put("component_model_type", ComponentTypeEnum.TOOLBAR_MODEL);
        toolbar.put("add", btn);
        GxtComponentMetaNodeModel node4 = new GxtComponentMetaNodeModel(toolbar, 5, 6);
        Map<String, Object> btnSubmit = new HashMap<String, Object>();
        btnSubmit.put("component_model_type", ComponentTypeEnum.BUTTON_MODEL);
        btnSubmit.put("text", "确定");
        GxtComponentMetaNodeModel btnSubmit1 = new GxtComponentMetaNodeModel(btnSubmit);
        Map<String, Object> btnquxiao = new HashMap<String, Object>();
        btnquxiao.put("component_model_type", ComponentTypeEnum.BUTTON_MODEL);
        btnquxiao.put("text", "取消");
        GxtComponentMetaNodeModel btn1 = new GxtComponentMetaNodeModel(btnquxiao);
        List<GxtComponentMetaNodeModel> list1 = new ArrayList<GxtComponentMetaNodeModel>();
        list1.add(btnSubmit1);
        list1.add(btn1);
        Map<String, Object> MaxPanel = new HashMap<String, Object>();
        MaxPanel.put("component_model_type", ComponentTypeEnum.FORMPANEL_MODEL);
        MaxPanel.put("autowidth", true);
        MaxPanel.put("autoheight", true);
        MaxPanel.put("frame", true);
        MaxPanel.put("headervisible", false);
        MaxPanel.put("addbutton", list1);
        MaxPanel.put("buttonalign", "CENTER");
        GxtComponentMetaNodeModel node5 = new GxtComponentMetaNodeModel(MaxPanel, 7, 42);
        Map<String, Object> topPanel = new HashMap<String, Object>();
        topPanel.put("component_model_type", ComponentTypeEnum.CONTENTPANEL_MODEL);
        topPanel.put("autowidth", true);
        topPanel.put("hight", 80);
        topPanel.put("headervisible", false);
        topPanel.put("layout", ComponentTypeEnum.COLUMNLAYOUT_MODEL);
        GxtComponentMetaNodeModel node6 = new GxtComponentMetaNodeModel(topPanel, 8, 33);
        Map<String, Object> leftPanel = new HashMap<String, Object>();
        leftPanel.put("component_model_type", ComponentTypeEnum.CONTENTPANEL_MODEL);
        leftPanel.put("width", 300);
        leftPanel.put("hight", 80);
        leftPanel.put("headervisible", false);
        leftPanel.put("layout", ComponentTypeEnum.FORMLAYOUT_MODEL);
        GxtComponentMetaNodeModel node7 = new GxtComponentMetaNodeModel(leftPanel, 9, 16);
        Map<String, Object> id = new HashMap<String, Object>();
        id.put("component_model_type", ComponentTypeEnum.TEXTFIELD_MODEL);
        id.put("fieldlabel", "编号");
        id.put("allowblank", false);
        GxtComponentMetaNodeModel node10 = new GxtComponentMetaNodeModel(id, 10, 11);
        Map<String, Object> type = new HashMap<String, Object>();
        type.put("component_model_type", ComponentTypeEnum.COMBOBOX_MODEL);
        type.put("fieldlabel", "入库类别");
        type.put("allowblank", false);
        GxtComponentMetaNodeModel node11 = new GxtComponentMetaNodeModel(type, 12, 13);
        Map<String, Object> date = new HashMap<String, Object>();
        date.put("component_model_type", ComponentTypeEnum.DATEFIELD_MODEL);
        date.put("fieldlabel", "填单日期");
        date.put("allowblank", false);
        GxtComponentMetaNodeModel node12 = new GxtComponentMetaNodeModel(date, 14, 15);
        Map<String, Object> centerPanel = new HashMap<String, Object>();
        centerPanel.put("component_model_type", ComponentTypeEnum.CONTENTPANEL_MODEL);
        centerPanel.put("width", 300);
        centerPanel.put("hight", 80);
        centerPanel.put("headervisible", false);
        centerPanel.put("layout", ComponentTypeEnum.FORMLAYOUT_MODEL);
        GxtComponentMetaNodeModel node8 = new GxtComponentMetaNodeModel(centerPanel, 17, 24);
        Map<String, Object> ghs = new HashMap<String, Object>();
        ghs.put("component_model_type", ComponentTypeEnum.TEXTFIELD_MODEL);
        ghs.put("fieldlabel", "供货商");
        ghs.put("allowblank", false);
        GxtComponentMetaNodeModel node13 = new GxtComponentMetaNodeModel(ghs, 18, 19);
        Map<String, Object> zj = new HashMap<String, Object>();
        zj.put("component_model_type", ComponentTypeEnum.COMBOBOX_MODEL);
        zj.put("fieldlabel", "质检方式");
        zj.put("allowblank", false);
        GxtComponentMetaNodeModel node14 = new GxtComponentMetaNodeModel(zj, 20, 21);
        Map<String, Object> fzr = new HashMap<String, Object>();
        fzr.put("component_model_type", ComponentTypeEnum.TEXTFIELD_MODEL);
        fzr.put("fieldlabel", "负责人");
        fzr.put("allowblank", false);
        GxtComponentMetaNodeModel node15 = new GxtComponentMetaNodeModel(fzr, 22, 23);
        Map<String, Object> rightPanel = new HashMap<String, Object>();
        rightPanel.put("component_model_type", ComponentTypeEnum.CONTENTPANEL_MODEL);
        rightPanel.put("width", 300);
        rightPanel.put("hight", 80);
        rightPanel.put("headervisible", false);
        rightPanel.put("layout", ComponentTypeEnum.FORMLAYOUT_MODEL);
        GxtComponentMetaNodeModel node9 = new GxtComponentMetaNodeModel(rightPanel, 25, 32);
        Map<String, Object> ck = new HashMap<String, Object>();
        ck.put("component_model_type", ComponentTypeEnum.COMBOBOX_MODEL);
        ck.put("fieldlabel", "接收仓库");
        ck.put("allowblank", false);
        GxtComponentMetaNodeModel node16 = new GxtComponentMetaNodeModel(ck, 26, 27);
        Map<String, Object> isZj = new HashMap<String, Object>();
        isZj.put("component_model_type", ComponentTypeEnum.CHECKBOX_MODEL);
        isZj.put("fieldlabel", "是否质检");
        isZj.put("boxlabel", "是否质检");
        isZj.put("value", true);
        GxtComponentMetaNodeModel node17 = new GxtComponentMetaNodeModel(isZj, 28, 29);
        Map<String, Object> zd = new HashMap<String, Object>();
        zd.put("component_model_type", ComponentTypeEnum.TEXTFIELD_MODEL);
        zd.put("fieldlabel", "制单");
        zd.put("allowblank", false);
        GxtComponentMetaNodeModel node18 = new GxtComponentMetaNodeModel(zd, 30, 31);
        Map<String, Object> middlePanel = new HashMap<String, Object>();
        middlePanel.put("component_model_type", ComponentTypeEnum.CONTENTPANEL_MODEL);
        middlePanel.put("autowidth", true);
        middlePanel.put("hight", 80);
        middlePanel.put("headervisible", false);
        middlePanel.put("layout", ComponentTypeEnum.FORMLAYOUT_MODEL);
        GxtComponentMetaNodeModel node19 = new GxtComponentMetaNodeModel(middlePanel, 34, 37);
        Map<String, Object> bz = new HashMap<String, Object>();
        bz.put("component_model_type", ComponentTypeEnum.TEXTAREA_MODEL);
        bz.put("label", "备注");
        GxtComponentMetaNodeModel node20 = new GxtComponentMetaNodeModel(bz, 35, 36);
        Map<String, Object> bottomPanel = new HashMap<String, Object>();
        bottomPanel.put("component_model_type", ComponentTypeEnum.CONTENTPANEL_MODEL);
        bottomPanel.put("autowidth", true);
        bottomPanel.put("hight", 290);
        bottomPanel.put("headervisible", false);
        GxtComponentMetaNodeModel node21 = new GxtComponentMetaNodeModel(bottomPanel, 38, 41);
        Map<String, Object> grid = new HashMap<String, Object>();
        grid.put("component_model_type", ComponentTypeEnum.EDITABLEGRID_MODEL);
        grid.put("number", 10);
        grid.put("hight", 280);
        grid.put("width", 950);
        GxtComponentMetaNodeModel node22 = new GxtComponentMetaNodeModel(grid, 39, 40);
        List<GxtComponentMetaNodeModel> list = new ArrayList<GxtComponentMetaNodeModel>();
        list.add(node2);
        list.add(node1);
        list.add(node3);
        list.add(node4);
        list.add(node5);
        list.add(node6);
        list.add(node7);
        list.add(node8);
        list.add(node9);
        list.add(node10);
        list.add(node11);
        list.add(node12);
        list.add(node13);
        list.add(node14);
        list.add(node15);
        list.add(node16);
        list.add(node17);
        list.add(node18);
        list.add(node19);
        list.add(node20);
        list.add(node21);
        list.add(node22);
        return list;
    }

    private ModelData newItem(String text, String iconStyle) {
        ModelData m = new BaseModelData();
        m.set("name", text);
        m.set("icon", iconStyle);
        return m;
    }

    /**
		 * 模拟从后台传过来的关于Window控件的元模型信息
		 */
    private GxtComponentMetaNodeModel getGxtComponentMetaNodeModel2() {
        Map<String, Object> map2 = new HashMap<String, Object>();
        map2.put("component_model_type", ComponentTypeEnum.TEXTFIELD_MODEL);
        Map<String, Object> codes2 = new HashMap<String, Object>();
        Map<String, Object> subMap = new HashMap<String, Object>();
        EventStructureMetaModel eventStructureMetaModel = new EventStructureMetaModel();
        eventStructureMetaModel.setControllerId("textFieldController01");
        eventStructureMetaModel.setResponseModeId("00_01_02");
        subMap.put("eventstructuremetamodel", eventStructureMetaModel);
        codes2.put("0301", subMap);
        map2.put("registereventtypes", codes2);
        GxtComponentMetaNodeModel node2 = new GxtComponentMetaNodeModel(map2);
        return node2;
    }
}
