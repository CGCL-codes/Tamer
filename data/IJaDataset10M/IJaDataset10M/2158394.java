package com.softaspects.jsf.renderer.flexMenu;

import com.softaspects.jsf.support.components.ComponentTypes;
import com.softaspects.jsf.support.util.StringUtils;
import com.softaspects.jsf.component.base.BaseComponentConsts;
import com.softaspects.jsf.support.FacesRenderingUtil;
import com.softaspects.jsf.component.flexMenu.*;
import com.softaspects.jsf.renderer.base.RenderingUtils;
import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;
import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

/**
 * FlexMenu Component renderer
 */
public class FlexMenuRenderer extends BaseFlexMenuRenderer {

    protected String getComponentType() {
        return ComponentTypes.FLEX_MENU_COMPONENT;
    }

    public boolean getRendersChildren() {
        return true;
    }

    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent) throws IOException {
        super.encodeBegin(facesContext, uiComponent);
        FacesRenderingUtil.renderCommonScripts(facesContext);
        try {
            FacesRenderingUtil.renderScriptInclude(facesContext, ((FlexMenu) uiComponent).getComponentTypeName());
        } catch (Exception e) {
        }
        Writer out = facesContext.getResponseWriter();
        FlexMenu flexMenu = (FlexMenu) uiComponent;
        setConfigurations(facesContext, flexMenu);
        out.write(RenderingUtils.submitHiddenButtonRender(uiComponent.getId()));
        out.write(RenderingUtils.getActionDataField(uiComponent.getId()));
        out.write("<div");
        out.write(RenderingUtils.getProperty(BaseComponentConsts.ID_PROPERTY, FlexMenuRendererUtil.getDivId(flexMenu)));
        out.write(">");
        out.write(getConfigInitString(flexMenu));
    }

    private String getUlClass() {
        return "fm_menu_main";
    }

    public void encodeChildren(FacesContext facesContext, UIComponent uiComponent) throws IOException {
        if (facesContext == null || uiComponent == null) {
            throw new NullPointerException();
        }
        FlexMenu menu = (FlexMenu) uiComponent;
        Writer out = facesContext.getResponseWriter();
        out.write("<ul");
        out.write(RenderingUtils.getProperty(BaseComponentConsts.ID_PROPERTY, menu.getId()));
        out.write(RenderingUtils.getProperty(BaseComponentConsts.CLASS_PROPERTY, getUlClass()));
        out.write(RenderingUtils.getProperty(BaseComponentConsts.STYLE_PROPERTY, "display:none;"));
        out.write(">");
        out.write(getInitString(menu));
        StringBuffer result = new StringBuffer();
        result.append(RenderingUtils.createJScriptBeginTag());
        if (menu.getServerSideAction() != null) result.append(menu.getVarName()).append(".serverSideAction=").append(menu.getServerSideAction()).append(";\n");
        if (menu.getPopUp() != null) result.append(menu.getVarName()).append(".popUp=").append(menu.getPopUp()).append(";\n");
        result.append(RenderingUtils.createJScriptEndTag());
        out.write(result.toString());
        int itemsCount = menu.getSubItemsCount();
        int counter = 0;
        Iterator kids = uiComponent.getChildren().iterator();
        while (kids.hasNext()) {
            UIComponent kid = (UIComponent) kids.next();
            if (kid instanceof FlexMenuBaseItem) {
                if (counter == 0) {
                    kid.getAttributes().put("FIRST", Boolean.TRUE);
                }
                if (counter == itemsCount - 1) {
                    kid.getAttributes().put("POSITION", Boolean.TRUE);
                }
                kid.encodeBegin(facesContext);
                if (kid.getRendersChildren()) {
                    kid.encodeChildren(facesContext);
                }
                kid.encodeEnd(facesContext);
                counter++;
            }
        }
        StringBuffer initString = new StringBuffer();
        initString.append(RenderingUtils.createJScriptBeginTag());
        initString.append(menu.getVarName());
        initString.append(".");
        initString.append("init();\n");
        initString.append(RenderingUtils.createJScriptEndTag());
        out.write(initString.toString());
        out.write("</ul>");
    }

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent) throws IOException {
        super.encodeEnd(facesContext, uiComponent);
        Writer out = facesContext.getResponseWriter();
        out.write("</div>\n");
        out.write("<div style=\"visibility: hidden; clear: both; height: 0; line-height: 0;\">&nbsp;</div>");
    }

    private String getConfigInitString(FlexMenu menu) {
        List params = new ArrayList();
        String contructorName = "FlexMenuConfiguration";
        String divId = FlexMenuRendererUtil.getDivId(menu);
        String varName = FlexMenuRendererUtil.getConfigVarName(menu);
        FlexMenuConfiguration flexMenuConfiguration = menu.getMenuConfiguration();
        params.add(StringUtils.getSurrounded(divId));
        StringBuffer initString = new StringBuffer();
        initString.append(StringUtils.getInitString(varName, contructorName, params));
        initString.append("\n");
        initString.append(RenderingUtils.createJScriptBeginTag());
        initString.append(varName);
        initString.append(".");
        initString.append("initializeCSSRules();\n");
        initString.append(RenderingUtils.createJScriptEndTag());
        return initString.toString();
    }

    private String getInitString(FlexMenu menu) {
        List params = new ArrayList();
        String contructorName = "FlexMenuComponent";
        String id = menu.getId();
        String varName = menu.getVarName();
        String orientation = FlexMenuRendererUtil.getOrientation(menu);
        String configVarName = FlexMenuRendererUtil.getConfigVarName(menu);
        params.add(StringUtils.getSurrounded(id));
        params.add(StringUtils.getSurrounded(varName));
        params.add(orientation);
        params.add(configVarName);
        return StringUtils.getInitString(menu.getVarName(), contructorName, params);
    }

    public void setConfigurations(FacesContext facesContext, FlexMenu flexMenu) {
        Iterator kids = flexMenu.getChildren().iterator();
        while (kids.hasNext()) {
            UIComponent kid = (UIComponent) kids.next();
            if (kid instanceof FlexMenuConfiguration) {
                flexMenu.setMenuConfiguration((FlexMenuConfiguration) kid);
            }
            if (kid instanceof FlexSubmenuConfiguration) {
                flexMenu.setSubmenuConfiguration((FlexSubmenuConfiguration) kid);
            }
        }
    }
}
