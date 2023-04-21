package pl.xperios.rdk.client.renderers;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.button.ButtonGroup;
import com.extjs.gxt.ui.client.widget.custom.ThemeSelector;
import com.extjs.gxt.ui.client.widget.layout.AccordionLayout;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.google.gwt.user.client.ui.Widget;
import java.util.ArrayList;
import pl.xperios.rdk.client.commons.Messageable;
import pl.xperios.rdk.shared.XLog;
import pl.xperios.rdk.shared.beans.UiComponent;

/**
 *
 * @author Praca
 */
public class WebSectionRendererImpl implements SectionRenderer {

    public WebSectionRendererImpl() {
        XLog.trace("WebSectionRendererImpl created");
    }

    public ArrayList<Component> getComponentsForMenuBar(ArrayList<UiComponent> elements, final Messageable messageable) {
        ArrayList<Component> components = new ArrayList<Component>();
        for (final UiComponent element : elements) {
            if (element.getComponentType().getName().equals("Text")) {
                components.add(new MenuItem(element.getValue()));
            } else if (element.getComponentType().getName().equals("Panel")) {
                Menu menu = new Menu();
                for (Component subComponent : getComponentsForMenuBar(element.getChildreen(), messageable)) {
                    menu.add(subComponent);
                }
                MenuItem button = new MenuItem(element.getTitle());
                button.setSubMenu(menu);
                button.addSelectionListener(new SelectionListener<MenuEvent>() {

                    @Override
                    public void componentSelected(MenuEvent ce) {
                        messageable.setMessage(element.getValue());
                    }
                });
                components.add(button);
            } else if (element.getComponentType().getName().equals("Button")) {
                MenuItem button = new MenuItem(element.getTitle());
                button.addSelectionListener(new SelectionListener<MenuEvent>() {

                    @Override
                    public void componentSelected(MenuEvent ce) {
                        messageable.setMessage(element.getValue());
                    }
                });
                components.add(button);
            } else if (element.getComponentType().getName().equals("Menu")) {
                Menu menu = new Menu();
                for (Component subComponent : getComponentsForMenuBar(element.getChildreen(), messageable)) {
                    menu.add(subComponent);
                }
                MenuItem button = new MenuItem(element.getTitle());
                button.setSubMenu(menu);
                components.add(button);
            } else if (element.getComponentType().getName().equals("Text")) {
                components.add(new MenuItem(element.getValue()));
            } else if (element.getComponentType().getName().equals("Template Chooser")) {
                ThemeSelector selector = new ThemeSelector();
                components.add(selector);
            }
        }
        return components;
    }

    public ArrayList<Component> getComponentsForToolBar(ArrayList<UiComponent> elements, final Messageable messageable) {
        ArrayList<Component> components = new ArrayList<Component>();
        for (final UiComponent element : elements) {
            if (element.getComponentType().getName().equals("Text")) {
                MenuItem button = new MenuItem(element.getTitle());
                button.addSelectionListener(new SelectionListener<MenuEvent>() {

                    @Override
                    public void componentSelected(MenuEvent ce) {
                        messageable.setMessage(element.getValue());
                    }
                });
                components.add(button);
            } else if (element.getComponentType().getName().equals("Panel")) {
                XLog.trace("Panel: subelements: " + element.getChildreen().size());
                ButtonGroup group = new ButtonGroup(element.getChildreen().size());
                group.setHeading(element.getTitle());
                for (Component subElement : getComponentsForToolBar(element.getChildreen(), messageable)) {
                    group.add(subElement);
                }
                components.add(group);
            } else if (element.getComponentType().getName().equals("Button")) {
                Button button = new Button(element.getTitle());
                button.addSelectionListener(new SelectionListener<ButtonEvent>() {

                    @Override
                    public void componentSelected(ButtonEvent ce) {
                        messageable.setMessage(element.getValue());
                    }
                });
                components.add(button);
            } else if (element.getComponentType().getName().equals("Menu")) {
                Menu menu = new Menu();
                for (Component subComponent : getComponentsForMenuBar(element.getChildreen(), messageable)) {
                    menu.add(subComponent);
                }
                Button button = new Button(element.getTitle());
                button.setMenu(menu);
                button.addSelectionListener(new SelectionListener<ButtonEvent>() {

                    @Override
                    public void componentSelected(ButtonEvent ce) {
                        messageable.setMessage(element.getValue());
                    }
                });
                components.add(button);
            } else if (element.getComponentType().getName().equals("Text")) {
                components.add(new Button(element.getValue()));
            } else if (element.getComponentType().getName().equals("Template Chooser")) {
                ThemeSelector selector = new ThemeSelector();
                components.add(selector);
            }
        }
        return components;
    }

    public ArrayList<Widget> getComponentsForMenu(ArrayList<UiComponent> elements, final Messageable messageable) {
        ArrayList<Widget> components = new ArrayList<Widget>();
        for (final UiComponent element : elements) {
            if (element.getComponentType().getName().equals("Text")) {
                ContentPanel panel = new ContentPanel(new FlowLayout());
                panel.setHeading(element.getTitle());
                panel.setAutoHeight(true);
                panel.addText(element.getValue());
                components.add(panel);
            } else if (element.getComponentType().getName().equals("Panel")) {
                XLog.trace("Panel: subelements: " + element.getChildreen().size());
                ContentPanel panel = new ContentPanel(new RowLayout());
                panel.setHeading(element.getTitle());
                panel.setAutoHeight(true);
                for (Widget subElement : getComponentsForMenu(element.getChildreen(), messageable)) {
                    panel.add(subElement, new RowData(1, -1, new Margins(2)));
                }
                components.add(panel);
            } else if (element.getComponentType().getName().equals("Button")) {
                Button button = new Button(element.getTitle());
                button.addSelectionListener(new SelectionListener<ButtonEvent>() {

                    @Override
                    public void componentSelected(ButtonEvent ce) {
                        messageable.setMessage(element.getValue());
                    }
                });
                components.add(button);
            } else if (element.getComponentType().getName().equals("Menu")) {
                ContentPanel panel = new ContentPanel(new AccordionLayout());
                panel.setHeading(element.getTitle());
                panel.setHeight(300 + 20 * element.getChildreen().size());
                for (ContentPanel subElement : getComponentsForMenuAccordion(element.getChildreen(), messageable)) {
                    panel.add(subElement);
                }
                components.add(panel);
            } else if (element.getComponentType().getName().equals("Text")) {
                components.add(new Button(element.getValue()));
            } else if (element.getComponentType().getName().equals("Template Chooser")) {
                ContentPanel panel = new ContentPanel(new FitLayout());
                panel.setHeading(element.getTitle());
                ThemeSelector selector = new ThemeSelector();
                panel.add(selector);
                components.add(panel);
            }
        }
        return components;
    }

    public ArrayList<ContentPanel> getComponentsForMenuAccordion(ArrayList<UiComponent> elements, Messageable messageable) {
        ArrayList<ContentPanel> components = new ArrayList<ContentPanel>();
        for (final UiComponent element : elements) {
            if (element.getComponentType().getName().equals("Text")) {
                ContentPanel panel = new ContentPanel();
                panel.setHeading(element.getTitle());
                panel.addText(element.getValue());
                components.add(panel);
            } else if (element.getComponentType().getName().equals("Panel")) {
                XLog.trace("Panel: subelements: " + element.getChildreen().size());
                ContentPanel panel = new ContentPanel(new RowLayout());
                panel.setHeading(element.getTitle());
                panel.setAutoHeight(true);
                for (Widget subElement : getComponentsForMenu(element.getChildreen(), messageable)) {
                    panel.add(subElement, new RowData(1, -1, new Margins(2)));
                }
                components.add(panel);
            } else if (element.getComponentType().getName().equals("Button")) {
            } else if (element.getComponentType().getName().equals("Menu")) {
                ContentPanel panel = new ContentPanel(new AccordionLayout());
                panel.setHeading(element.getTitle());
                panel.setHeight(300);
                for (ContentPanel subElement : getComponentsForMenuAccordion(element.getChildreen(), messageable)) {
                    panel.add(subElement);
                }
                components.add(panel);
            } else if (element.getComponentType().getName().equals("Template Chooser")) {
                ContentPanel panel = new ContentPanel(new RowLayout());
                panel.setHeading(element.getTitle());
                panel.setHeight(20);
                ThemeSelector selector = new ThemeSelector();
                panel.add(selector, new RowData(1, -1));
                components.add(panel);
            }
        }
        return components;
    }
}
