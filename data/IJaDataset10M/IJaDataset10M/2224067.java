package br.ufmg.catustec.arangi.view;

import java.util.ArrayList;
import java.util.List;
import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlCommandLink;
import javax.faces.component.html.HtmlGraphicImage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.apache.commons.lang.StringUtils;
import org.richfaces.component.UIToolBar;
import org.richfaces.component.html.HtmlDropDownMenu;
import org.richfaces.component.html.HtmlMenuGroup;
import org.richfaces.component.html.HtmlMenuItem;
import org.richfaces.component.html.HtmlToolBar;
import org.richfaces.component.html.HtmlToolBarGroup;
import br.ufmg.catustec.arangi.commons.ArangiConstants;
import br.ufmg.catustec.arangi.dto.MenuItem;

/**
 * Dynamic menu with security using richfaces.
 *
 * @author Wagner Salazar
 *
 */
public class BasicToolBar {

    private UIToolBar toolBar;

    /**
	 *
	 */
    protected void addExitButton() {
        HtmlToolBar htmlToolBar = (HtmlToolBar) getToolBar();
        HtmlToolBarGroup group = new HtmlToolBarGroup();
        group.setLocation("right");
        HtmlGraphicImage image = new HtmlGraphicImage();
        image.setValue("/img/exit.gif");
        image.setStyleClass("image");
        image.setId("arangi_exit_image");
        HtmlCommandLink link = new HtmlCommandLink();
        String expression = "#{facesViewEventHandler.exit}";
        MethodExpression method = FacesBeanHelper.createMethodExpression(expression, String.class, new Class[] {});
        link.setActionExpression(method);
        link.setImmediate(true);
        expression = "#{msgs.exitMessage}";
        ValueExpression exp = FacesBeanHelper.createValueExpression(expression, String.class);
        link.setValueExpression("title", exp);
        link.setId("arangi_exit_command");
        link.getChildren().add(image);
        group.getChildren().add(link);
        htmlToolBar.getChildren().add(group);
    }

    /**
	 * Performed after adding the items to the menu
	 */
    protected void afterAddElements() {
    }

    /**
	 * Performed before adding the items to the menu
	 */
    protected void beforeAddElements() {
    }

    /**
	 * Construct the first level of the menu
	 */
    private void configureMenu() {
        MenuItem menu = FacesBeanHelper.getBeanByExpression("#{applicationMenu}", MenuItem.class);
        HtmlToolBar htmlToolBar = (HtmlToolBar) getToolBar();
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        String expression = "";
        ValueExpression exp = null;
        List<UIComponent> listItens = new ArrayList<UIComponent>();
        int cont = 0;
        for (MenuItem item : menu.getChildren()) {
            cont++;
            List<UIComponent> list = configureMenuItem(item.getChildren(), cont + "");
            if (!list.isEmpty()) {
                HtmlDropDownMenu dropDownMenu = new HtmlDropDownMenu();
                expression = "#{" + item.getAliasResourceBundle() + "['" + item.getValue() + "']}";
                exp = FacesBeanHelper.createValueExpression(expression, String.class);
                dropDownMenu.setValueExpression("value", exp);
                dropDownMenu.setId("arangi_menu_" + cont);
                dropDownMenu.getChildren().addAll(list);
                listItens.add(dropDownMenu);
            }
        }
        beforeAddElements();
        htmlToolBar.getChildren().addAll(listItens);
        afterAddElements();
        String buttonExit = externalContext.getInitParameter(ArangiConstants.ContextParam.BUTTON_EXIT);
        if (buttonExit != null && buttonExit.equalsIgnoreCase(ArangiConstants.TRUE)) {
            addExitButton();
        }
    }

    /**
	 * Construct the other levels of the menu
	 */
    private List<UIComponent> configureMenuItem(List<MenuItem> children, String prefix) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        String arangiSecurity = externalContext.getInitParameter(ArangiConstants.ContextParam.SECURITY);
        boolean isSecurity = false;
        if (arangiSecurity != null && arangiSecurity.equalsIgnoreCase(ArangiConstants.TRUE)) {
            isSecurity = true;
        }
        String expression = "";
        ValueExpression exp = null;
        List<UIComponent> listItens = new ArrayList<UIComponent>();
        int cont = 0;
        for (MenuItem item : children) {
            cont++;
            String prefix2 = prefix + "_" + cont;
            if (!item.getChildren().isEmpty()) {
                HtmlMenuGroup menuGroup = new HtmlMenuGroup();
                expression = "#{" + item.getAliasResourceBundle() + "['" + item.getValue() + "']}";
                exp = FacesBeanHelper.createValueExpression(expression, String.class);
                menuGroup.setValueExpression("value", exp);
                menuGroup.setId("arangi_menu_group_" + prefix2);
                List<UIComponent> list = configureMenuItem(item.getChildren(), prefix2);
                menuGroup.getChildren().addAll(list);
                if (!list.isEmpty()) {
                    listItens.add(menuGroup);
                }
            } else {
                HtmlMenuItem menuItem = new HtmlMenuItem();
                String realLink = item.getLink();
                if (realLink.startsWith("/")) {
                    realLink = realLink.substring(1);
                }
                expression = "#{" + item.getAliasResourceBundle() + "['" + item.getValue() + "']}";
                exp = FacesBeanHelper.createValueExpression(expression, String.class);
                menuItem.setValueExpression("value", exp);
                menuItem.setId("arangi_menu_item_" + prefix2);
                menuItem.setOnclick("javascript:closeConversation();window.location='" + realLink + "'; return false;");
                menuItem.setSubmitMode("ajax");
                boolean insert = isAllowed(item);
                if (!isSecurity || insert) {
                    listItens.add(menuItem);
                }
            }
        }
        return listItens;
    }

    /**
	 * Verify if the item is allowed for the current user
	 * @param item
	 * @return
	 */
    protected boolean isAllowed(MenuItem item) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        String link = item.getLink();
        if (link.startsWith("/")) {
            link = link.substring(1);
        }
        String prefixSearch = externalContext.getInitParameter(ArangiConstants.ContextParam.PREFIX_SEARCH);
        if (StringUtils.isEmpty(prefixSearch)) {
            prefixSearch = ArangiConstants.PREFIX_SEARCH;
        }
        int endIndex = (link.indexOf(".") > 0) ? link.indexOf(".") : link.length();
        String role = link.substring(0, endIndex);
        if (externalContext.isUserInRole(role) || externalContext.isUserInRole(role.replaceAll(prefixSearch, StringUtils.EMPTY))) {
            return true;
        }
        return false;
    }

    public UIToolBar getToolBar() {
        return toolBar;
    }

    public void setToolBar(UIToolBar toolBar) {
        this.toolBar = toolBar;
        if (toolBar.getChildCount() == 0) {
            configureMenu();
        }
    }
}
