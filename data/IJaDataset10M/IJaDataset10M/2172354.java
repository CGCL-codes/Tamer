package com.hy.mydesktop.client.component.factory;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.Layout;
import com.extjs.gxt.ui.client.widget.Window;
import com.hy.mydesktop.client.component.meta.AccordionPanelModelEnum;
import com.hy.mydesktop.client.component.meta.WindowModelEnum;
import com.hy.mydesktop.client.component.type.ComponentTypeEnum;
import com.hy.mydesktop.shared.persistence.domain.gxt.GxtComponentMetaNodeModel;
import com.hy.mydesktop.shared.rpc.util.ComponentTypeEnumConverter;

/**
 * 
 * <ul>
 * <li>开发作者：汤莉</li>
 * <li>设计日期：2010-8-29；时间：下午03:55:18</li>
 * <li>类型名称：WindowFactory</li>
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
public class WindowFactory2012_2_7 {

    /**
	 * 
	 * <ul>
	 * <li>方法含义：创建Window控件</li>
	 * <li>方法作者：汤莉</li>
	 * <li>编写日期：2010-8-29；时间：下午下午03:05:09</li>
	 * </ul>
	 * <ul>
	 * <b>修订编号：</b>
	 * <li>修订日期：</li>
	 * <li>修订作者：</li>
	 * <li>修订原因：</li>
	 * <li>修订内容：</li>
	 * </ul>
	 * @param gxtComponentMetaNodeModel
	 * @return
	 */
    public static Window createWindow(GxtComponentMetaNodeModel gxtComponentMetaNodeModel) {
        return configureWindow(gxtComponentMetaNodeModel);
    }

    /**
	 * 
	 * <ul>
	 * <li>方法含义：对窗口的属性进行配置</li>
	 * <li>方法作者：汤莉</li>
	 * <li>编写日期：2010-8-29；时间：下午下午03:56:36</li>
	 * </ul>
	 * <ul>
	 * <b>修订编号：</b>
	 * <li>修订日期：</li>
	 * <li>修订作者：</li>
	 * <li>修订原因：</li>
	 * <li>修订内容：</li>
	 * </ul>
	 * @param gxtComponentMetaNodeModel
	 * @return
	 */
    private static Window configureWindow(GxtComponentMetaNodeModel gxtComponentMetaNodeModel) {
        Window window = createDefaultWindow();
        window = AbstractComponetFactory.configureAbstractComponet(window, gxtComponentMetaNodeModel);
        if (gxtComponentMetaNodeModel.get(WindowModelEnum.INITIALWIDTH.name().toLowerCase()) != null) {
            window.setInitialWidth((Integer) gxtComponentMetaNodeModel.get(WindowModelEnum.INITIALWIDTH.name().toLowerCase()));
        }
        if (gxtComponentMetaNodeModel.get(WindowModelEnum.MINWIDTH.name().toLowerCase()) != null) {
            window.setMinWidth((Integer) gxtComponentMetaNodeModel.get(WindowModelEnum.MINWIDTH.name().toLowerCase()));
        }
        if (gxtComponentMetaNodeModel.get(WindowModelEnum.HIGHT.name().toLowerCase()) != null) {
            window.setHeight((Integer) gxtComponentMetaNodeModel.get(WindowModelEnum.HIGHT.name().toLowerCase()));
        }
        if (gxtComponentMetaNodeModel.get(WindowModelEnum.MINHIGHT.name().toLowerCase()) != null) {
            window.setMinHeight((Integer) gxtComponentMetaNodeModel.get(WindowModelEnum.MINHIGHT.name().toLowerCase()));
        }
        if (gxtComponentMetaNodeModel.get(WindowModelEnum.TITLE.name().toLowerCase()) != null) {
            window.setHeading((String) gxtComponentMetaNodeModel.get(WindowModelEnum.TITLE.name().toLowerCase()));
        }
        if (gxtComponentMetaNodeModel.get(WindowModelEnum.HEADING.name().toLowerCase()) != null) {
            window.setHeading((String) gxtComponentMetaNodeModel.get(WindowModelEnum.TITLE.name().toLowerCase()));
        }
        if (gxtComponentMetaNodeModel.get(WindowModelEnum.MODAL.name().toLowerCase()) != null) {
            window.setModal((Boolean) gxtComponentMetaNodeModel.get(WindowModelEnum.MODAL.name().toLowerCase()));
        }
        if (gxtComponentMetaNodeModel.get(WindowModelEnum.RESIZABLE.name().toLowerCase()) != null) {
            window.setResizable((Boolean) gxtComponentMetaNodeModel.get(WindowModelEnum.RESIZABLE.name().toLowerCase()));
        }
        if (gxtComponentMetaNodeModel.get(WindowModelEnum.MAXMIZABLE.name().toLowerCase()) != null) {
            window.setMaximizable((Boolean) gxtComponentMetaNodeModel.get(WindowModelEnum.MAXMIZABLE.name().toLowerCase()));
        }
        if (gxtComponentMetaNodeModel.get(WindowModelEnum.MINMIZABLE.name().toLowerCase()) != null) {
            window.setMinimizable((Boolean) gxtComponentMetaNodeModel.get(WindowModelEnum.MINMIZABLE.name().toLowerCase()));
        }
        if (gxtComponentMetaNodeModel.get(WindowModelEnum.CLOSABLE.name().toLowerCase()) != null) {
            window.setClosable((Boolean) gxtComponentMetaNodeModel.get(WindowModelEnum.CLOSABLE.name().toLowerCase()));
        }
        if (gxtComponentMetaNodeModel.get(WindowModelEnum.SCROLLMODE.name().toLowerCase()) != null) {
            window.setScrollMode((Scroll) gxtComponentMetaNodeModel.get(WindowModelEnum.SCROLLMODE.name().toLowerCase()));
        }
        if (gxtComponentMetaNodeModel.get(WindowModelEnum.LAYOUT.name().toLowerCase()) != null) {
            Object object = gxtComponentMetaNodeModel.get(WindowModelEnum.LAYOUT.name().toLowerCase());
            ComponentTypeEnum componentTypeEnum = null;
            if (object instanceof Integer) {
                Integer componentTypeEnumIndex = (Integer) gxtComponentMetaNodeModel.get(WindowModelEnum.LAYOUT.name().toLowerCase());
                componentTypeEnum = ComponentTypeEnumConverter.getComponentTypeEnum(componentTypeEnumIndex);
            } else {
                componentTypeEnum = (ComponentTypeEnum) object;
            }
            Layout layout = null;
            switch(componentTypeEnum) {
                case FITLAYOUT_MODEL:
                    layout = FitLayoutFactory.createFitLayout(gxtComponentMetaNodeModel);
                    window.setLayout(layout);
                    break;
                case ACCORDIONLAYOUT_MODEL:
                    layout = AccordionLayoutFactory.createAccordionLayout(gxtComponentMetaNodeModel);
                    window.setLayout(layout);
                    break;
                case ROWLAYOUT_MODEL:
                    layout = RowLayoutFactory.createRowLayout(gxtComponentMetaNodeModel);
                    window.setLayout(layout);
                    break;
                case VBOXLAYOUT_MODEL:
                    layout = VboxLayoutFactory.createVboxLayout(gxtComponentMetaNodeModel);
                    window.setLayout(layout);
                    break;
                case COLUMNLAYOUT_MODEL:
                    layout = ColumnLayoutFactory.createColumnLayout(gxtComponentMetaNodeModel);
                    window.setLayout(layout);
                    break;
                case FLOWLAYOUT_MODEL:
                    layout = FlowLayoutFactory.createFlowLayout(gxtComponentMetaNodeModel);
                    window.setLayout(layout);
                    break;
                case TABLELAYOUT_MODEL:
                    layout = TableLayoutFactory.createTableLayout(gxtComponentMetaNodeModel);
                    window.setLayout(layout);
                    break;
                case FORMLAYOUT_MODEL:
                    layout = FormLayoutFactory.createFormLayout(gxtComponentMetaNodeModel);
                    window.setLayout(layout);
                    break;
                default:
                    break;
            }
        }
        if (gxtComponentMetaNodeModel.get(WindowModelEnum.TOPCOMPONENT.name().toLowerCase()) != null) {
            window.setTopComponent((Component) gxtComponentMetaNodeModel.get(WindowModelEnum.TOPCOMPONENT.name().toLowerCase()));
        }
        return window;
    }

    /**
	 * 
	 * <ul>
	 * <li>方法含义：创建默认的窗口</li>
	 * <li>方法作者：汤莉</li>
	 * <li>编写日期：2010-8-29；时间：下午下午03:56:49</li>
	 * </ul>
	 * <ul>
	 * <b>修订编号：</b>
	 * <li>修订日期：</li>
	 * <li>修订作者：</li>
	 * <li>修订原因：</li>
	 * <li>修订内容：</li>
	 * </ul>
	 * @return
	 */
    private static Window createDefaultWindow() {
        Window window = new Window();
        window.setSize(400, 300);
        window.setHeading("window");
        return window;
    }
}
