package com.hy.mydesktop.client.component.factory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.data.BaseModel;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.EventType;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.event.WidgetListener;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.widget.BoxComponent;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.Container;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Layout;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.hy.mydesktop.client.component.factory.ButtonFactory;
import com.hy.mydesktop.client.component.type.ComponentControllerEnum;
import com.hy.mydesktop.client.component.type.ComponentTypeEnum;
import com.hy.mydesktop.shared.persistence.domain.gxt.GxtComponentMetaModel;
import com.hy.mydesktop.shared.persistence.domain.gxt.GxtComponentMetaNodeModel;
import com.hy.mydesktop.shared.persistence.domain.gxt.constant.GxtMetaModelTypeEnum;
import com.hy.mydesktop.shared.rpc.util.ComponentControllerEnumConverter;
import com.hy.mydesktop.shared.rpc.util.ComponentTypeEnumConverter;
import com.hy.mydesktop.shared.util.log.MyLoggerUtil;

/**
 * 
 * <ul>
 * <li>开发作者：花宏宇</li>
 * <li>设计日期：2010-8-27；时间：上午11:52:08</li>
 * <li>类型名称：GxtComponentFactory</li>
 * <li>设计目的：</li>
 * </ul>
 * <ul>
 * <b>修订编号：</b>
 * <li>修订日期：</li>
 * <li>修订作者：</li>
 * <li>修订原因：</li>
 * <li>修订内容：</li>
 * </ul>
 */
public class GxtComponentFactory {

    public static final String className = "com.hy.mydesktop.client.component.factory.GxtComponentFactory";

    /**
	 * 
	 * <ul>
	 * <li>方法含义：GxtComponentMetaModel信息，创建控件。<br>
	 * 注；GxtComponentMetaModel必须是树形结构(只有一个根节点)
	 * </li>
	 * <li>方法作者：花宏宇</li>
	 * <li>编写日期：2010-8-28；时间：下午上午10:56:34</li>
	 * </ul>
	 * <ul>
	 * <b>修订编号：</b>
	 * <li>修订日期：</li>
	 * <li>修订作者：</li>
	 * <li>修订原因：</li>
	 * <li>修订内容：</li>
	 * </ul>
	 * @param <COMPONENT>
	 * @param gxtComponentMetaModel
	 * @return
	 */
    public static <COMPONENT extends Component> COMPONENT createGxtComponent(GxtComponentMetaModel gxtComponentMetaModel) {
        GxtMetaModelTypeEnum gxtMetaModelTypeEnum = gxtComponentMetaModel.getGxtMetaModelTypeEnum();
        switch(gxtMetaModelTypeEnum) {
            case BASE_GXT_META_MODEL_TYPE:
                return createBaseGxtComponent(gxtComponentMetaModel.getRoot());
            case TREE_GXT_META_MODEL_TYPE:
                return createTreeGxtComponent(gxtComponentMetaModel);
            default:
                return null;
        }
    }

    public static <COMPONENT extends Component> COMPONENT createOneGxtComponent(GxtComponentMetaNodeModel gxtComponentMetaNodeModel) {
        return createBaseGxtComponent(gxtComponentMetaNodeModel);
    }

    /**
	 * 根据GxtComponentMetaModel(继承BaseModel)，创建单一控件
	 * @param <COMPONENT>
	 * @param gxtComponentMetaNodeModel
	 * @return
	 */
    private static <COMPONENT extends Component> COMPONENT createBaseGxtComponent(GxtComponentMetaNodeModel gxtComponentMetaNodeModel) {
        COMPONENT component = null;
        if (gxtComponentMetaNodeModel == null) {
            MyLoggerUtil.printError(className, "某个控制器Controller对应的gxtComponentMetaNodeModel为空!");
        }
        ComponentTypeEnum componentTypeEnum = gxtComponentMetaNodeModel.getComponentTypeEnum();
        if (componentTypeEnum == null) {
            ComponentTypeEnumConverter.updateComponentTypeEnum(gxtComponentMetaNodeModel);
            componentTypeEnum = gxtComponentMetaNodeModel.getComponentTypeEnum();
            if (componentTypeEnum == null) {
                MyLoggerUtil.printError(className, "没有查询到componentTypeEnum类型");
            } else {
                MyLoggerUtil.printDebug(className, " gxtComponentMetaNodeModel.getComponentTypeEnumIndex() is " + gxtComponentMetaNodeModel.getComponentTypeEnumIndex() + "(" + componentTypeEnum.name() + "; gxtComponentMetaNodeModel.getComponentTypeEnum() is null ;)");
            }
        }
        switch(componentTypeEnum) {
            case TREEPANELMODULES_MODEL:
                {
                    component = (COMPONENT) TreePanelModulesFactory.createTreePanelModule(gxtComponentMetaNodeModel);
                    break;
                }
            case GRIDSETMODULES_MODEL:
                {
                    component = (COMPONENT) GridSetModulesFactory.createGridModule(gxtComponentMetaNodeModel);
                    break;
                }
            case FORM_MODULE_MODEL:
                {
                    component = (COMPONENT) FormModuleFactory.createFormModule(gxtComponentMetaNodeModel);
                    break;
                }
            case BUTTON_MODEL:
                {
                    component = (COMPONENT) ButtonFactory.createButton(gxtComponentMetaNodeModel);
                    break;
                }
            case GRID_MODEL:
                {
                    MyLoggerUtil.printError(className, "GRID_MODEL不能有GridFactory创建");
                    break;
                }
            case EDITABLEGRID_MODEL:
                {
                    component = (COMPONENT) EditableGridFactory.createEditorGrid(gxtComponentMetaNodeModel);
                    break;
                }
            case WINDOW_MODEL:
                {
                    component = (COMPONENT) WindowFactory.createWindow(gxtComponentMetaNodeModel);
                    break;
                }
            case TEXTFIELD_MODEL:
                {
                    component = (COMPONENT) TextFieldFactory.createTextField(gxtComponentMetaNodeModel);
                    break;
                }
            case CONTENTPANEL_MODEL:
                {
                    component = (COMPONENT) ContentPanelFactory.createContentPanel(gxtComponentMetaNodeModel);
                    break;
                }
            case TABITEM_MODEL:
                {
                    component = (COMPONENT) TabItemFactory.createTabItem(gxtComponentMetaNodeModel);
                    break;
                }
            case TABPANEL_MODEL:
                {
                    component = (COMPONENT) TabPanelFactory.createTabPanel(gxtComponentMetaNodeModel);
                    break;
                }
            case MENUBAR_MODEL:
                {
                    component = (COMPONENT) MenuBarFactory.createMenuBar(gxtComponentMetaNodeModel);
                    break;
                }
            case TOOLBAR_MODEL:
                {
                    component = (COMPONENT) ToolBarFactory.createToolBar(gxtComponentMetaNodeModel);
                    break;
                }
            case FIELDSET_MODEL:
                {
                    component = (COMPONENT) FieldSetFactory.createFieldSet(gxtComponentMetaNodeModel);
                    break;
                }
            case DATEFIELD_MODEL:
                {
                    component = (COMPONENT) DateFieldFactory.createDateField(gxtComponentMetaNodeModel);
                    break;
                }
            case DATEPICKER_MODEL:
                {
                    component = (COMPONENT) DatePickerFactory.createDatePicker(gxtComponentMetaNodeModel);
                    break;
                }
            case COMBOBOX_MODEL:
                {
                    component = (COMPONENT) ComboBoxFactory.createComboBox(gxtComponentMetaNodeModel);
                    break;
                }
            case SIMPLECOMBOBOX_MODEL:
                {
                    component = (COMPONENT) SimpleComboBoxFactory.createSimpleComboBox(gxtComponentMetaNodeModel);
                    break;
                }
            case RADIOGROUP_MODEL:
                {
                    component = (COMPONENT) RadioGroupFactory.createRadioGroup(gxtComponentMetaNodeModel);
                    break;
                }
            case RADIO_MODEL:
                {
                    component = (COMPONENT) RadioFactory.createRadio(gxtComponentMetaNodeModel);
                    break;
                }
            case CHECKBOXGROUP_MODEL:
                {
                    component = (COMPONENT) CheckBoxGroupFactory.createCheckBoxGroup(gxtComponentMetaNodeModel);
                    break;
                }
            case CHECKBOX_MODEL:
                {
                    component = (COMPONENT) CheckBoxFactory.createCheckBox(gxtComponentMetaNodeModel);
                    break;
                }
            case MENUBARITEM_MODEL:
                {
                    component = (COMPONENT) MenuItemBarFactory.createMenuBarItem(gxtComponentMetaNodeModel);
                    break;
                }
            case FORMPANEL_MODEL:
                {
                    component = (COMPONENT) FormpanelFactory.createFormPanel(gxtComponentMetaNodeModel);
                    break;
                }
            case TEXTAREA_MODEL:
                {
                    component = (COMPONENT) TextAreaFactory.createTextArea(gxtComponentMetaNodeModel);
                    break;
                }
            case CHART_MODEL:
                {
                    break;
                }
            default:
                {
                    component = null;
                    break;
                }
        }
        MyLoggerUtil.printComponentDebugForInit(component.getClass());
        return component;
    }

    /**
	 * 
	 * <ul>
	 * <li>方法含义：创建具有树形结构的控件（GxtComponentMetaModel为树形结构）</li>
	 * <li>方法作者：花宏宇</li>
	 * <li>编写日期：2010-8-28；时间：下午上午10:53:22</li>
	 * </ul>
	 * <ul>
	 * <b>修订编号：</b>
	 * <li>修订日期：</li>
	 * <li>修订作者：</li>
	 * <li>修订原因：</li>
	 * <li>修订内容：</li>
	 * </ul>
	 * @param <COMPONENT>
	 * @param gxtComponentMetaModelList
	 * @return
	 */
    private static <COMPONENT extends Component> COMPONENT createTreeGxtComponent(GxtComponentMetaModel gxtComponentMetaModel) {
        if (gxtComponentMetaModel.getGxtMetaModelTypeEnum() != GxtMetaModelTypeEnum.TREE_GXT_META_MODEL_TYPE) {
            return null;
        }
        GxtComponentMetaNodeModel root = gxtComponentMetaModel.getRoot();
        Map<GxtComponentMetaNodeModel, COMPONENT> map = new HashMap<GxtComponentMetaNodeModel, COMPONENT>();
        GxtComponentMetaNodeModel tempNode = null;
        tempNode = root;
        COMPONENT tempComponent = null;
        while (tempNode != null) {
            tempComponent = createBaseGxtComponent(tempNode);
            map.put(tempNode, tempComponent);
            if (gxtComponentMetaModel.getParent(tempNode) != null) {
                GxtComponentMetaNodeModel parent = gxtComponentMetaModel.getParent(tempNode);
                LayoutContainer layoutContainer = (LayoutContainer) map.get(parent);
                layoutContainer.add(tempComponent);
            }
            tempNode = gxtComponentMetaModel.getNextNode(tempNode);
        }
        return map.get(root);
    }

    public static Component ItemGxtComponent(GxtComponentMetaNodeModel subGxtComponentMetaNodeModel) {
        return createBaseGxtComponent(subGxtComponentMetaNodeModel);
    }
}
