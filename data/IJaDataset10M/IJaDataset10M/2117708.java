package com.hy.mydesktop.shared.business;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import com.extjs.gxt.ui.client.data.BaseModel;
import com.extjs.gxt.ui.client.data.ModelData;
import com.hy.mydesktop.client.component.meta.GridColumnConfigModel;
import com.hy.mydesktop.client.component.mvc.event.EventStructureMetaModel;
import com.hy.mydesktop.client.component.type.ComponentControllerEnum;
import com.hy.mydesktop.client.component.type.ComponentTypeEnum;
import com.hy.mydesktop.client.mvc.core.builder.GridBuilderTemplate;
import com.hy.mydesktop.client.mvc.core.builder.WindowApplicationBuildTemplate;
import com.hy.mydesktop.shared.persistence.domain.gxt.GxtAppEventType;
import com.hy.mydesktop.shared.persistence.domain.gxt.GxtComponentControllerMetaModel;
import com.hy.mydesktop.shared.persistence.domain.gxt.GxtComponentMetaModel;
import com.hy.mydesktop.shared.persistence.domain.gxt.GxtComponentMetaNodeModel;
import com.hy.mydesktop.shared.persistence.domain.gxt.ItemOfAssociateToWindow;
import com.hy.mydesktop.shared.persistence.domain.gxt.constant.ComponentMetaDataConstants;
import com.hy.mydesktop.shared.rpc.meta.GxtToSeamServiceParameter;

/**
 * 
 * <ul>
 * <li>开发作者：李冰</li>
 * <li>设计日期：2010-11-25；时间：上午11:52:46</li>
 * <li>类型名称：LocationTypeServices</li>
 * <li>设计目的：位置类型</li>
 * </ul>
 * <ul>
 * <b>修订编号：</b>
 * <li>修订日期：</li>
 * <li>修订作者：</li>
 * <li>修订原因：</li>
 * <li>修订内容：</li>
 * </ul>
 */
public class LocationTypeServices {

    private GxtComponentMetaModel createDynamicDesktopController3() {
        Map<String, Object> desktopController = new HashMap<String, Object>();
        desktopController.put("component_controller_model_type_index", ComponentControllerEnum.DESKTOP_CONTROLLOR_MODEL.ordinal());
        List<String> codes1 = new ArrayList<String>();
        codes1.add("0003");
        codes1.add("0002");
        codes1.add("0005");
        desktopController.put("registereventtypes", codes1);
        desktopController.put("id", "desktopController");
        GxtComponentMetaNodeModel node = new GxtComponentMetaNodeModel(desktopController, 1, 4);
        Map<String, Object> shortcutController = new HashMap<String, Object>();
        shortcutController.put("component_controller_model_type_index", ComponentControllerEnum.SHORTCUT_CONTROLLER_MODEL.ordinal());
        List<String> codes2 = new ArrayList<String>();
        codes2.add("0003");
        codes2.add("0002");
        codes2.add("0005");
        shortcutController.put("registereventtypes", codes2);
        shortcutController.put("id", "shortcutController01");
        shortcutController.put("viewdata", "shortcut_viewData_init_01");
        shortcutController.put("gxtcomponentmetanodemodel", this.getShortcutGxtComponentMetaNodeModel());
        GxtComponentMetaNodeModel node2 = new GxtComponentMetaNodeModel(shortcutController, 2, 3);
        List<GxtComponentMetaNodeModel> list = new ArrayList<GxtComponentMetaNodeModel>();
        list.add(node);
        list.add(node2);
        GxtComponentMetaModel gxtComponentMetaModel = new GxtComponentMetaModel(list);
        return gxtComponentMetaModel;
    }

    private GxtComponentMetaNodeModel getShortcutGxtComponentMetaNodeModel() {
        Map<String, Object> map2 = new HashMap<String, Object>();
        map2.put("component_model_type_index", ComponentTypeEnum.SHORTCUT_MODEL.ordinal());
        Map<String, Object> RegisterEventTypeDatas = new HashMap<String, Object>();
        Map<String, Object> oneRegisterEventTypeData = new HashMap<String, Object>();
        EventStructureMetaModel eventStructureMetaModel = new EventStructureMetaModel();
        eventStructureMetaModel.setControllerId("shortcutController01");
        eventStructureMetaModel.setRootControllerId("desktopController");
        eventStructureMetaModel.setResponseModeId("00_01_02");
        oneRegisterEventTypeData.put("eventstructuremetamodel", eventStructureMetaModel);
        Map<String, Object> oneDispacherEventTypeData = new HashMap<String, Object>();
        EventStructureMetaModel subEventStructureMetaModel = new EventStructureMetaModel();
        subEventStructureMetaModel.setControllerId("1_1_windowController_LocationType");
        subEventStructureMetaModel.setRootControllerId("1_1_windowController_LocationType");
        oneDispacherEventTypeData.put("eventstructuremetamodel", subEventStructureMetaModel);
        oneDispacherEventTypeData.put("count", 7);
        oneRegisterEventTypeData.put("dispachereventtype", "0007");
        RegisterEventTypeDatas.put("0003", oneRegisterEventTypeData);
        map2.put("registereventtypes", RegisterEventTypeDatas);
        Map<String, Object> dispacherEventTypeDatas = new HashMap<String, Object>();
        dispacherEventTypeDatas.put("0007", oneDispacherEventTypeData);
        map2.put("dispachereventtypes", dispacherEventTypeDatas);
        map2.put("text", "Rpc   -----LoginView----AppEvents.DESKTOP_INIT_FOR_LOGGED_IN_USER");
        GxtComponentMetaNodeModel node2 = new GxtComponentMetaNodeModel(map2);
        return node2;
    }

    public List<GxtComponentMetaModel> validateLogin0(BaseModel baseModel) {
        System.err.println("Rpc get Username + Password" + baseModel.get("username") + "  " + baseModel.get("password"));
        List<GxtComponentMetaModel> list = new ArrayList<GxtComponentMetaModel>();
        GxtComponentMetaModel gxtComponentMetaModel = new GxtComponentMetaModel(this.getList2(baseModel));
        list.add(this.createDynamicDesktopController3());
        return list;
    }

    public List<GxtComponentMetaNodeModel> getList2(BaseModel baseModel) {
        Map<String, Object> windowController = new HashMap<String, Object>();
        windowController.put("component_controller_model_type_index", ComponentControllerEnum.WINDOW_CONTROLLER_MODEL.ordinal());
        List<String> codes1 = new ArrayList<String>();
        codes1.add("0101");
        codes1.add("0301");
        codes1.add("0003");
        codes1.add("0201");
        codes1.add("0401");
        codes1.add("0007");
        codes1.add("0008");
        windowController.put("id", "windowController");
        windowController.put("registereventtypes", codes1);
        windowController.put("viewdata", "window_viewData_init_01");
        GxtComponentMetaNodeModel node = new GxtComponentMetaNodeModel(windowController, 1, 14);
        Map<String, Object> contentPanelController = new HashMap<String, Object>();
        contentPanelController.put("component_controller_model_type_index", ComponentControllerEnum.CONTENTPANEL_CONTROLLER_MODEL.ordinal());
        List<String> codes2 = new ArrayList<String>();
        codes2.add("0201");
        codes2.add("0301");
        codes2.add("0401");
        codes2.add("0101");
        codes2.add("0007");
        codes2.add("0008");
        contentPanelController.put("registereventtypes", codes2);
        contentPanelController.put("id", "contentPanelController");
        contentPanelController.put("viewdata", "contentPanel_viewData_init_01");
        GxtComponentMetaNodeModel node2 = new GxtComponentMetaNodeModel(contentPanelController, 2, 13);
        Map<String, Object> menuBarController = new HashMap<String, Object>();
        menuBarController.put("component_controller_model_type_index", ComponentControllerEnum.MENUBAR_CONTROLLER_MODEL.ordinal());
        List<String> codes3 = new ArrayList<String>();
        codes3.add("0201");
        codes3.add("0301");
        codes3.add("0401");
        codes3.add("0101");
        codes3.add("0007");
        codes3.add("0008");
        menuBarController.put("registereventtypes", codes3);
        menuBarController.put("id", "menuBarController");
        menuBarController.put("viewdata", "menuBar_viewData_init_01");
        GxtComponentMetaNodeModel node3 = new GxtComponentMetaNodeModel(menuBarController, 3, 4);
        Map<String, Object> toolBarController = new HashMap<String, Object>();
        toolBarController.put("component_controller_model_type_index", ComponentControllerEnum.TOOLBAR_CONTROLLER_MODEL.ordinal());
        List<String> codes4 = new ArrayList<String>();
        codes4.add("0201");
        codes4.add("0301");
        codes4.add("0401");
        codes4.add("0101");
        codes4.add("0007");
        codes4.add("0008");
        toolBarController.put("registereventtypes", codes4);
        toolBarController.put("id", "toolBarController");
        toolBarController.put("viewdata", "toolBar_viewData_init_01");
        GxtComponentMetaNodeModel node4 = new GxtComponentMetaNodeModel(toolBarController, 5, 6);
        Map<String, Object> sumFormPanelController = new HashMap<String, Object>();
        sumFormPanelController.put("component_controller_model_type_index", ComponentControllerEnum.FORMPANEL_CONTROLLER_MODEL.ordinal());
        List<String> codes45 = new ArrayList<String>();
        codes45.add("0201");
        codes45.add("0301");
        codes45.add("0401");
        codes45.add("0101");
        codes45.add("0007");
        codes45.add("0008");
        sumFormPanelController.put("registereventtypes", codes45);
        sumFormPanelController.put("id", "sumFormPanelController");
        sumFormPanelController.put("controllerdata", "sumFormPanel_controllerdata_init_01");
        sumFormPanelController.put("viewdata", "sumFormPanel_viewData_init_01");
        GxtComponentMetaNodeModel node45 = new GxtComponentMetaNodeModel(sumFormPanelController, 7, 12);
        Map<String, Object> formPanelController = new HashMap<String, Object>();
        formPanelController.put("component_controller_model_type_index", ComponentControllerEnum.FORMSET_MODULE_CONTROLLER_MODEL.ordinal());
        List<String> codes5 = new ArrayList<String>();
        codes5.add("0201");
        codes5.add("0301");
        codes5.add("0401");
        codes5.add("0101");
        codes5.add("0007");
        codes5.add("0008");
        formPanelController.put("registereventtypes", codes5);
        formPanelController.put("id", "formPanelController");
        formPanelController.put("controllerdata", "formPanel_controllerdata_init_01");
        formPanelController.put("viewdata", "formPanel_viewData_init_01");
        GxtComponentMetaNodeModel node5 = new GxtComponentMetaNodeModel(formPanelController, 8, 9);
        Map<String, Object> treePanelModulesController = new HashMap<String, Object>();
        treePanelModulesController.put("component_controller_model_type_index", ComponentControllerEnum.TREEPANELMODULES_CONTROLLER_MODEL.ordinal());
        List<String> codes6 = new ArrayList<String>();
        codes6.add("0401");
        codes6.add("0301");
        codes6.add("0201");
        codes6.add("0101");
        codes6.add("0007");
        codes6.add("0008");
        treePanelModulesController.put("registereventtypes", codes6);
        treePanelModulesController.put("id", "treePanelModulesController");
        treePanelModulesController.put("viewdata", "treePanelModules_viewData_init_01");
        GxtComponentMetaNodeModel node6 = new GxtComponentMetaNodeModel(treePanelModulesController, 10, 11);
        List<GxtComponentMetaNodeModel> list = new ArrayList<GxtComponentMetaNodeModel>();
        list.add(node);
        list.add(node2);
        list.add(node3);
        list.add(node4);
        list.add(node45);
        list.add(node5);
        list.add(node6);
        return list;
    }

    public GxtComponentControllerMetaModel createDynamicWindowController() {
        String controllerMetaModelId = "locationTypeServices";
        Map<String, Object> gridMap2 = new HashMap<String, Object>();
        String formId = "locationTypeService";
        gridMap2 = createGridMap2(controllerMetaModelId, formId, gridMap2);
        GxtComponentControllerMetaModel gxtComponentControllerMetaModel = WindowApplicationBuildTemplate.createSimpleWindowApplication_model1(controllerMetaModelId, "位置类型设置", formId, gridMap2);
        return gxtComponentControllerMetaModel;
    }

    private Map<String, Object> createGridMap2(String controllerMetaModelId, String formId, Map<String, Object> gridMap2) {
        List<GridColumnConfigModel> columnConfigs = new LinkedList<GridColumnConfigModel>();
        columnConfigs.add(new GridColumnConfigModel("NAME", "名称", 250));
        columnConfigs.add(new GridColumnConfigModel("DESCRIPTION", "描述", 300));
        gridMap2.put(ComponentMetaDataConstants.GRID_COLUMNCONFIG_MODELS, columnConfigs);
        List<ItemOfAssociateToWindow> childrenItems = new LinkedList<ItemOfAssociateToWindow>();
        childrenItems.add(GridBuilderTemplate.createItemForEditorPagingGrid(controllerMetaModelId, formId));
        gridMap2.put(ComponentMetaDataConstants.CHILDREN_ITEMS, childrenItems);
        return gridMap2;
    }

    public GxtComponentMetaModel createDynamicWindowController2() {
        Map<String, Object> windowController = new HashMap<String, Object>();
        windowController.put("component_controller_model_type_index", ComponentControllerEnum.WINDOW_CONTROLLER_MODEL.ordinal());
        List<String> codes1 = new ArrayList<String>();
        codes1.add("0301");
        codes1.add("0401");
        codes1.add("0007");
        codes1.add("0008");
        windowController.put("id", "1_1_windowController_LocationType");
        windowController.put("registereventtypes", codes1);
        windowController.put("viewdata", "window_viewData_init_01");
        windowController.put("gxtcomponentmetanodemodel", this.getWindowGxtComponentMetaNodeModel());
        GxtComponentMetaNodeModel node = new GxtComponentMetaNodeModel(windowController, 1, 14);
        Map<String, Object> contentPanelController = new HashMap<String, Object>();
        contentPanelController.put("component_controller_model_type_index", ComponentControllerEnum.CONTENTPANEL_CONTROLLER_MODEL.ordinal());
        List<String> codes2 = new ArrayList<String>();
        codes2.add("0301");
        codes2.add("0401");
        codes2.add("0007");
        codes2.add("0008");
        contentPanelController.put("registereventtypes", codes2);
        contentPanelController.put("id", "2_1_contentPanelController_LocationType");
        contentPanelController.put("viewdata", "contentPanel_viewData_init_01");
        contentPanelController.put("gxtcomponentmetanodemodel", this.getContentPanelGxtComponentMetaNodeModel());
        GxtComponentMetaNodeModel node2 = new GxtComponentMetaNodeModel(contentPanelController, 2, 13);
        Map<String, Object> menuBarController = new HashMap<String, Object>();
        menuBarController.put("component_controller_model_type_index", ComponentControllerEnum.MENUBAR_CONTROLLER_MODEL.ordinal());
        List<String> codes3 = new ArrayList<String>();
        codes3.add("0301");
        codes3.add("0401");
        codes3.add("0007");
        codes3.add("0008");
        menuBarController.put("registereventtypes", codes3);
        menuBarController.put("id", "3_1_menuBarController_LocationType");
        menuBarController.put("viewdata", "menuBar_viewData_init_01");
        GxtComponentMetaNodeModel node3 = new GxtComponentMetaNodeModel(menuBarController, 3, 4);
        Map<String, Object> toolBarController = new HashMap<String, Object>();
        toolBarController.put("component_controller_model_type_index", ComponentControllerEnum.TOOLBAR_CONTROLLER_MODEL.ordinal());
        List<String> codes4 = new ArrayList<String>();
        codes4.add("0301");
        codes4.add("0401");
        codes4.add("0007");
        codes4.add("0008");
        toolBarController.put("registereventtypes", codes4);
        toolBarController.put("id", "3_1_toolBarController_LocationType");
        toolBarController.put("viewdata", "toolBar_viewData_init_01");
        toolBarController.put("gxtcomponentmetanodemodel", this.getToolBarGxtComponentMetaNodeModel2());
        GxtComponentMetaNodeModel node4 = new GxtComponentMetaNodeModel(toolBarController, 5, 6);
        Map<String, Object> sumFormPanelController = new HashMap<String, Object>();
        sumFormPanelController.put("component_controller_model_type_index", ComponentControllerEnum.FORMPANEL_CONTROLLER_MODEL.ordinal());
        List<String> codes45 = new ArrayList<String>();
        codes45.add("0301");
        codes45.add("0401");
        codes45.add("0007");
        codes45.add("0008");
        sumFormPanelController.put("registereventtypes", codes45);
        sumFormPanelController.put("id", "3_1_sumFormPanelController_LocationType");
        sumFormPanelController.put("controllerdata", "sumFormPanel_controllerdata_init_01");
        sumFormPanelController.put("viewdata", "sumFormPanel_viewData_init_01");
        sumFormPanelController.put("gxtcomponentmetanodemodel", this.getSumFormPanelGxtComponentMetaNodeModel());
        GxtComponentMetaNodeModel node45 = new GxtComponentMetaNodeModel(sumFormPanelController, 7, 12);
        Map<String, Object> gridController = new HashMap<String, Object>();
        gridController.put("component_controller_model_type_index", ComponentControllerEnum.GRIDSETMODULES_CONTROLLER_MODEL.ordinal());
        List<String> codes6 = new ArrayList<String>();
        codes6.add("0401");
        codes6.add("0301");
        codes6.add("0007");
        codes6.add("0008");
        gridController.put("registereventtypes", codes6);
        gridController.put("id", "4_1_gridController_LocationType");
        gridController.put("viewdata", "textField_viewData_init_01");
        gridController.put("gxtcomponentmetanodemodel", this.getGridSetModuleGxtComponentMetaNodeModel());
        GxtComponentMetaNodeModel node6 = new GxtComponentMetaNodeModel(gridController, 8, 11);
        Map<String, Object> dataLoadController = new HashMap<String, Object>();
        dataLoadController.put("component_controller_model_type_index", ComponentControllerEnum.GRID_DATALOAD_CONTROLLER_MODEL.ordinal());
        List<String> codes12222 = new ArrayList<String>();
        codes12222.add("0301");
        codes12222.add("0201");
        codes12222.add("0401");
        dataLoadController.put("id", "5_1_dataLoadController_LocationType");
        dataLoadController.put("registereventtypes", codes12222);
        GxtComponentMetaNodeModel node62 = new GxtComponentMetaNodeModel(dataLoadController, 9, 10);
        List<GxtComponentMetaNodeModel> list = new ArrayList<GxtComponentMetaNodeModel>();
        list.add(node);
        list.add(node2);
        list.add(node3);
        list.add(node4);
        list.add(node45);
        list.add(node6);
        list.add(node62);
        GxtComponentMetaModel gxtComponentMetaModel = new GxtComponentMetaModel(list);
        return gxtComponentMetaModel;
    }

    /**
		 * 模拟从后台传过来的关于Window控件的元模型信息
		 */
    private GxtComponentMetaNodeModel getWindowGxtComponentMetaNodeModel() {
        Map<String, Object> map2 = new HashMap<String, Object>();
        map2.put("component_model_type_index", ComponentTypeEnum.WINDOW_MODEL.ordinal());
        map2.put("title", "位置类型设置");
        map2.put("width", 800);
        map2.put("hight", 550);
        map2.put("id", "01_01_windowGxtComponent_LocationType");
        map2.put("resizable", false);
        map2.put("maxmizable", false);
        map2.put("minmizable", true);
        map2.put("layout", ComponentTypeEnum.FITLAYOUT_MODEL.ordinal());
        GxtComponentMetaModel gxtComponentMetaModel = new GxtComponentMetaModel();
        GxtComponentMetaNodeModel gxtComponentMetaNodeModel = new GxtComponentMetaNodeModel(map2);
        return gxtComponentMetaNodeModel;
    }

    private GxtComponentMetaNodeModel getContentPanelGxtComponentMetaNodeModel() {
        Map<String, Object> map2 = new HashMap<String, Object>();
        map2.put("component_model_type_index", ComponentTypeEnum.CONTENTPANEL_MODEL.ordinal());
        map2.put("headervisible", false);
        map2.put("id", "02_01_contentPanelGxtComponent_LocationType");
        map2.put("title", "Rpc===dataload----AppEvents.DESKTOP_INIT_FOR_LOGGED_IN_USER");
        GxtComponentMetaNodeModel node2 = new GxtComponentMetaNodeModel(map2);
        return node2;
    }

    private GxtComponentMetaNodeModel getToolBarGxtComponentMetaNodeModel2() {
        Map<String, Object> button01 = new HashMap<String, Object>();
        button01.put("component_model_type_index", ComponentTypeEnum.BUTTON_MODEL.ordinal());
        button01.put("text", "新建");
        Map<String, Object> codes2 = new HashMap<String, Object>();
        Map<String, Object> oneRegisterEventTypeData = new HashMap<String, Object>();
        EventStructureMetaModel eventStructureMetaModel = new EventStructureMetaModel();
        eventStructureMetaModel.setControllerId("windowController2");
        eventStructureMetaModel.setRootControllerId("windowController2");
        oneRegisterEventTypeData.put("eventstructuremetamodel", eventStructureMetaModel);
        codes2.put("0010", oneRegisterEventTypeData);
        button01.put("registereventtypes", codes2);
        BaseModel gxtToSeamServiceParameter = new BaseModel();
        gxtToSeamServiceParameter.set("serviceComponentName", "com.test.server.business.LocationTypeServices");
        gxtToSeamServiceParameter.set("serviceMethodName", "loadSubApplication");
        BaseModel[] methodArguments2 = new BaseModel[1];
        methodArguments2[0] = new BaseModel();
        gxtToSeamServiceParameter.set("methodArguments", methodArguments2);
        button01.put("gxttoseamserviceparametermodel", gxtToSeamServiceParameter);
        GxtComponentMetaNodeModel node11 = new GxtComponentMetaNodeModel(button01);
        Map<String, Object> button02 = new HashMap<String, Object>();
        button02.put("component_model_type_index", ComponentTypeEnum.BUTTON_MODEL.ordinal());
        button02.put("text", "保存");
        GxtComponentMetaNodeModel node22 = new GxtComponentMetaNodeModel(button02);
        Map<String, Object> button03 = new HashMap<String, Object>();
        button03.put("component_model_type_index", ComponentTypeEnum.BUTTON_MODEL.ordinal());
        button03.put("text", "清空");
        GxtComponentMetaNodeModel node33 = new GxtComponentMetaNodeModel(button03);
        Map<String, Object> button04 = new HashMap<String, Object>();
        button04.put("component_model_type_index", ComponentTypeEnum.BUTTON_MODEL.ordinal());
        button04.put("text", "导入");
        GxtComponentMetaNodeModel node44 = new GxtComponentMetaNodeModel(button04);
        Map<String, Object> button05 = new HashMap<String, Object>();
        button05.put("component_model_type_index", ComponentTypeEnum.BUTTON_MODEL.ordinal());
        button05.put("text", "导出");
        GxtComponentMetaNodeModel node55 = new GxtComponentMetaNodeModel(button05);
        List<GxtComponentMetaNodeModel> list = new ArrayList<GxtComponentMetaNodeModel>();
        list.add(node11);
        list.add(node22);
        list.add(node33);
        Map<String, Object> toolbar = new HashMap<String, Object>();
        toolbar.put("component_model_type_index", ComponentTypeEnum.TOOLBAR_MODEL.ordinal());
        toolbar.put("id", "03_01_toolbarGxtComponent_LocationType");
        toolbar.put("children", list);
        ToolBarHandler.configureToolBarGxtComponentMetaNodeModel(toolbar);
        GxtComponentMetaNodeModel node4 = new GxtComponentMetaNodeModel(toolbar, 5, 6);
        return node4;
    }

    private GxtComponentMetaNodeModel getSumFormPanelGxtComponentMetaNodeModel() {
        Map<String, Object> btnSubmit = new HashMap<String, Object>();
        btnSubmit.put("component_model_type_index", ComponentTypeEnum.BUTTON_MODEL.ordinal());
        btnSubmit.put("text", "sumFormPanel确定000000");
        btnSubmit.put("id", "03_01_sumFormPanelGxtComponent_LocationType");
        Map<String, Object> codes2 = new HashMap<String, Object>();
        Map<String, Object> oneRegisterEventTypeData = new HashMap<String, Object>();
        EventStructureMetaModel eventStructureMetaModel = new EventStructureMetaModel();
        eventStructureMetaModel.setRootControllerId("windowController2");
        oneRegisterEventTypeData.put("eventstructuremetamodel", eventStructureMetaModel);
        oneRegisterEventTypeData.put("dispachereventtype", "0007");
        codes2.put("0302", oneRegisterEventTypeData);
        btnSubmit.put("registereventtypes", codes2);
        GxtComponentMetaNodeModel btnSubmit1 = new GxtComponentMetaNodeModel(btnSubmit);
        Map<String, Object> btnquxiao = new HashMap<String, Object>();
        btnquxiao.put("component_model_type_index", ComponentTypeEnum.BUTTON_MODEL.ordinal());
        btnquxiao.put("text", "取消");
        GxtComponentMetaNodeModel btn1 = new GxtComponentMetaNodeModel(btnquxiao);
        List<GxtComponentMetaNodeModel> list1 = new ArrayList<GxtComponentMetaNodeModel>();
        list1.add(btnSubmit1);
        list1.add(btn1);
        Map<String, Object> MaxPanel = new HashMap<String, Object>();
        MaxPanel.put("component_model_type_index", ComponentTypeEnum.FORMPANEL_MODEL.ordinal());
        MaxPanel.put("autowidth", true);
        MaxPanel.put("autoheight", true);
        MaxPanel.put("frame", true);
        MaxPanel.put("headervisible", false);
        MaxPanel.put("addsubmitbutton", true);
        MaxPanel.put("addresetbutton", true);
        MaxPanel.put("buttonalign", "CENTER");
        MaxPanel.put("layoutindex", ComponentTypeEnum.FORMLAYOUT_MODEL.ordinal());
        FormPanelHandler.configureFormPanel(MaxPanel);
        GxtComponentMetaNodeModel node5 = new GxtComponentMetaNodeModel(MaxPanel);
        return node5;
    }

    private GxtComponentMetaNodeModel getGridSetModuleGxtComponentMetaNodeModel() {
        Map<String, Object> map2 = new HashMap<String, Object>();
        map2.put("component_model_type_index", ComponentTypeEnum.GRIDSETMODULES_MODEL.ordinal());
        map2.put("autowidth", true);
        map2.put("autoheight", true);
        map2.put("frame", true);
        map2.put("headervisible", false);
        Map<String, Object> oneDispacherEventTypeData = new HashMap<String, Object>();
        EventStructureMetaModel subEventStructureMetaModel = new EventStructureMetaModel();
        subEventStructureMetaModel.setControllerId("5_1_dataLoadController_LocationType");
        subEventStructureMetaModel.setRootControllerId("1_1_windowController_LocationType");
        oneDispacherEventTypeData.put("eventstructuremetamodel", subEventStructureMetaModel);
        Map<String, Object> dispacherEventTypeDatas = new HashMap<String, Object>();
        dispacherEventTypeDatas.put("0007", oneDispacherEventTypeData);
        map2.put("dispachereventtypes", dispacherEventTypeDatas);
        Map<String, Object> map3 = new HashMap<String, Object>();
        map3.put("component_model_type_index", ComponentTypeEnum.GRIDMODULE_MODEL.ordinal());
        map3.put("version", 1);
        map3.put("hight", 380);
        map3.put("headervisible", false);
        map3.put("rownumber", true);
        map3.put("id", "locationType");
        List<ModelData> columnConfigs = new ArrayList<ModelData>();
        columnConfigs.add(new GridColumnConfigModel("NAME", "名称", 200));
        columnConfigs.add(new GridColumnConfigModel("DESCRIPTION", "描述", 300));
        map3.put("columnconfigload", columnConfigs);
        HandlerData.data(map3, "locationTypeService", "getAllLocationType", "addLocationType", "modifyLocationType", "loadEditorableGrid");
        GxtComponentMetaNodeModel node3 = new GxtComponentMetaNodeModel(map3);
        List<GxtComponentMetaNodeModel> list = new ArrayList<GxtComponentMetaNodeModel>();
        list.add(node3);
        map2.put("gridlist", list);
        GxtComponentMetaNodeModel node2 = new GxtComponentMetaNodeModel(map2);
        return node2;
    }
}
