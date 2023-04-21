package org.pojosoft.ria.gwt.client.ui;

import com.google.gwt.user.client.ui.Widget;
import org.pojosoft.ria.gwt.client.DataProvider;
import org.pojosoft.ria.gwt.client.Renderer;
import org.pojosoft.ria.gwt.client.RendererContext;
import org.pojosoft.ria.gwt.client.service.UserMessageUtils;
import org.pojosoft.ria.gwt.client.ui.support.BreadCrumb;
import org.pojosoft.ria.gwt.client.ui.support.WidgetFactory;
import java.util.Map;

/**
 * A default {@link Renderer renderer} implementation for rendering a header of a module
 *
 * @author POJO Software
 */
public class DefaultModuleHeaderRenderer implements Renderer {

    DataProvider dataProvider;

    Map params;

    ModuleHeaderRendererContext rendererContext;

    public void setDataProvider(DataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }

    public void setParams(Map params) {
        this.params = params;
    }

    public Object getParam(String id) {
        return params != null ? params.get(id) : null;
    }

    public Widget render(RendererContext context) {
        this.rendererContext = (ModuleHeaderRendererContext) context;
        ModuleComposite composite = rendererContext.getModuleComposite();
        if (composite.getModelObjectId() == null) {
            composite.getBreadCrumb().startCrumb(new BreadCrumb.Crumb(UserMessageUtils.getMessage("header.AddEntity", composite.getModelObjectName()), composite.getWrappedWidget()));
        } else {
            if (composite.getMode().equals(ModuleComposite.MODE_VIEW)) {
                composite.getBreadCrumb().addCrumb(new BreadCrumb.Crumb(UserMessageUtils.getMessage("header.AddEntity", composite.getModelObjectName()), composite.getWrappedWidget()));
            } else {
                composite.getBreadCrumb().addCrumb(new BreadCrumb.Crumb(UserMessageUtils.getMessage("header.UpdateEntity", composite.getModelObjectName(), composite.getModelObjectId()), composite.getWrappedWidget()));
            }
        }
        if (composite.isShownInPopup()) return WidgetFactory.createNavigationWidget(getAddAndSearchModuleName(), composite.getBreadCrumb(), false); else return WidgetFactory.createNavigationWidget(getAddAndSearchModuleName(), composite.getBreadCrumb(), true);
    }

    String getAddAndSearchModuleName() {
        String moduleName = null;
        if (params != null) {
            moduleName = (String) params.get("moduleNameUsedForAddAndSearch");
        }
        if (moduleName == null) {
            moduleName = rendererContext.getModuleComposite().getModelObjectName();
        }
        return moduleName;
    }
}
