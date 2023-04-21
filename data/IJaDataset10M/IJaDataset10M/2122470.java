package org.gwt.mosaic.xul.client.ui;

import org.gwt.beansbinding.core.client.Binding;
import org.gwt.beansbinding.core.client.PropertyStateEvent;
import org.gwt.beansbinding.core.client.PropertyStateListener;
import org.gwt.mosaic.actions.client.Action;
import org.gwt.mosaic.actions.client.ActionBindings;
import org.gwt.mosaic.actions.client.ActionMap;
import org.gwt.mosaic.actions.client.CheckBoxMenuItemBindings;
import org.gwt.mosaic.actions.client.CommandAction;
import org.gwt.mosaic.actions.client.MenuItemBindings;
import org.gwt.mosaic.actions.client.RadioButtonMenuItemBindings;
import org.gwt.mosaic.application.client.Application;
import org.gwt.mosaic.ui.client.util.ButtonHelper;
import org.gwt.mosaic.ui.client.util.ButtonHelper.ButtonLabelType;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.Widget;

/**
 * An element, much like a button, that is placed on a menubar. When the user
 * clicks the menu element, the child menupopup of the menu will be displayed.
 * This element is also used to create submenus.
 * 
 * @author georgopoulos.georgios(at)gmail.com
 * 
 */
public class Menu extends Container {

    private static final long serialVersionUID = -77555816999625746L;

    private transient MenuBar menuBar = null;

    /**
   * The label that will appear on the element. If this is left out, no text
   * appears.
   */
    public static final String LABEL = "label";

    public String getLabel() {
        return getString(LABEL);
    }

    public void setLabel(String label) {
        putString(LABEL, label);
    }

    public Menu() {
        super();
    }

    @Override
    public Element add(Element element, int index) {
        if (element instanceof Menupopup) {
            return super.add(element, index);
        }
        return null;
    }

    @Override
    protected Widget createUI() {
        if (menuBar == null) {
            menuBar = new MenuBar(true);
            menuBar.setAnimationEnabled(true);
        }
        syncUI(menuBar);
        return menuBar;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void syncUI(Widget ui) {
        super.syncUI(ui);
        menuBar.clearItems();
        final ActionMap actionMap = Application.getInstance().getContext().getActionMap();
        for (int i = 0, n = getElementCount(); i < n; i++) {
            Menupopup menu = (Menupopup) getElement(i);
            for (int j = 0, m = menu.getElementCount(); j < m; j++) {
                final Element element = menu.getElement(j);
                if (element instanceof Menu) {
                    final Menu subMenu = (Menu) element;
                    final MenuItem menuItem = menuBar.addItem(ButtonHelper.createButtonLabel(CommandAction.ACTION_IMAGES.noimage(), subMenu.getLabel(), ButtonLabelType.TEXT_ON_RIGHT), true, (MenuBar) subMenu.getUI());
                } else if (element instanceof Menuseparator) {
                    final Menuseparator menuseparator = (Menuseparator) element;
                    menuseparator.setMenuItemSeparator(menuBar.addSeparator());
                } else if (element instanceof Menuitem) {
                    final Menuitem menuitem = (Menuitem) element;
                    final MenuItem menuItem = menuBar.addItem(menuitem.getLabel(), new Command() {

                        public void execute() {
                        }
                    });
                    menuitem.setMenuItem(menuItem);
                    if (menuitem.attributeMap.containsKey(Menuitem.LABEL)) {
                        menuItem.setHTML(ButtonHelper.createButtonLabel(CommandAction.ACTION_IMAGES.noimage(), menuitem.getLabel(), ButtonLabelType.TEXT_ON_RIGHT));
                    }
                    ActionBindings<MenuItem> menuItemBindings = menuitem.getMenuItemBindings();
                    if (menuItemBindings != null) {
                        menuItemBindings.unbind();
                        menuitem.setMenuItemBindings(null);
                    }
                    if (menuitem.getAttributeMap().containsKey(Menuitem.COMMAND)) {
                        final Action action = actionMap.get(menuitem.getCommand());
                        if (action != null) {
                            if ("checkbox".equals(menuitem.getType())) {
                                menuItemBindings = new CheckBoxMenuItemBindings(action, menuItem);
                            } else if ("radio".equals(menuitem.getType())) {
                                String name = menuitem.attributeMap.containsKey(Menuitem.NAME) ? menuitem.getName() : DOM.createUniqueId();
                                menuItemBindings = new RadioButtonMenuItemBindings(name, action, menuItem);
                            } else {
                                menuItemBindings = new MenuItemBindings(action, menuItem);
                            }
                            menuitem.setMenuItemBindings(menuItemBindings);
                            Binding nameBinding = menuItemBindings.getBinding(Action.NAME);
                            if (nameBinding != null) {
                                nameBinding.setSourceNullValue(menuitem.getLabel());
                                nameBinding.getTargetProperty().addPropertyStateListener(nameBinding.getTargetObject(), new PropertyStateListener() {

                                    public void propertyStateChanged(PropertyStateEvent pse) {
                                        menuitem.setLabel((String) pse.getNewValue());
                                    }
                                });
                            }
                            Binding shortDescrBinding = menuItemBindings.getBinding(Action.SHORT_DESCRIPTION);
                            if (shortDescrBinding != null) {
                                shortDescrBinding.setSourceNullValue(getTooltiptext());
                                shortDescrBinding.getTargetProperty().addPropertyStateListener(shortDescrBinding.getTargetObject(), new PropertyStateListener() {

                                    public void propertyStateChanged(PropertyStateEvent pse) {
                                        menuitem.setTooltiptext((String) pse.getNewValue());
                                    }
                                });
                            }
                            Binding smallIconBinding = menuItemBindings.getBinding(Action.SMALL_ICON);
                            if (smallIconBinding != null) {
                                smallIconBinding.getTargetProperty().addPropertyStateListener(smallIconBinding.getTargetObject(), new PropertyStateListener() {

                                    public void propertyStateChanged(PropertyStateEvent pse) {
                                    }
                                });
                            }
                            Binding enabledBinding = menuItemBindings.getBinding("enabled");
                            if (enabledBinding != null) {
                                enabledBinding.getTargetProperty().addPropertyStateListener(enabledBinding.getTargetObject(), new PropertyStateListener() {

                                    public void propertyStateChanged(PropertyStateEvent pse) {
                                    }
                                });
                            }
                            Binding selectedBinding = menuItemBindings.getBinding("selected");
                            if (selectedBinding != null) {
                                if (selectedBinding != null) {
                                    selectedBinding.getTargetProperty().addPropertyStateListener(selectedBinding.getTargetObject(), new PropertyStateListener() {

                                        public void propertyStateChanged(PropertyStateEvent pse) {
                                            menuitem.setChecked((Boolean) pse.getNewValue());
                                        }
                                    });
                                }
                            }
                            Binding visibleBinding = menuItemBindings.getBinding("visible");
                            if (visibleBinding != null) {
                                visibleBinding.getTargetProperty().addPropertyStateListener(visibleBinding.getTargetObject(), new PropertyStateListener() {

                                    public void propertyStateChanged(PropertyStateEvent pse) {
                                        menuitem.setHidden(!((Boolean) pse.getNewValue()));
                                    }
                                });
                            }
                            menuItemBindings.bind();
                        }
                    }
                }
            }
        }
    }
}
