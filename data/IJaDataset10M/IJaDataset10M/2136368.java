package ui2swt;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import nanoxml.XMLElement;

public class ElementParser4 implements ElementParser {

    public ElementParser4() {
    }

    public void parse(XMLElement iRootElement, UIDefinition iUIDefinition) {
        Enumeration childEnum = iRootElement.enumerateChildren();
        while (childEnum.hasMoreElements()) {
            XMLElement childElement = (XMLElement) (childEnum.nextElement());
            String childName = childElement.getName();
            if ("widget".equals(childName)) {
                Widget rootWidget = this.parseRootWidget(childElement, iUIDefinition);
                iUIDefinition.setRootWidget(rootWidget);
            }
        }
    }

    private Widget parseRootWidget(XMLElement iElement, UIDefinition iUIDefinition) {
        Widget rootWidget = new Widget(iElement.getStringAttribute("class"));
        ElementParserHelper.parseAttributes(iElement, rootWidget);
        Enumeration childEnum = iElement.enumerateChildren();
        while (childEnum.hasMoreElements()) {
            XMLElement childElement = (XMLElement) (childEnum.nextElement());
            String childName = childElement.getName();
            if ("property".equals(childName)) {
                ElementParserHelper.parseProperty(childElement, rootWidget);
            } else if ("attribute".equals(childName)) {
                ElementParserHelper.parseProperty(childElement, rootWidget);
            } else if ("widget".equals(childName)) {
                String className = childElement.getStringAttribute("class");
                if ("QMenuBar".equals(className)) {
                    Widget menuBar = this.parseMenuBar(childElement);
                    iUIDefinition.setMenuBar(menuBar);
                } else if ("QToolBar".equals(className)) {
                    Widget toolBar = this.parseToolBar(childElement);
                    iUIDefinition.addToolBar(toolBar);
                } else if ("QStatusBar".equals(className)) {
                } else {
                    this.parseWidget(childElement, className, rootWidget);
                }
            } else if ("layout".equals(childName)) {
                String className = childElement.getStringAttribute("class");
                this.parseLayout(childElement, className, rootWidget);
            } else if ("item".equals(childName)) {
                this.parseWidgetItem(childElement, rootWidget);
            } else if ("action".equals(childName)) {
                Action action = this.parseAction(childElement);
                iUIDefinition.addAction(action);
            } else {
                System.err.println("WARNING: Unknown element: " + childName);
            }
        }
        return rootWidget;
    }

    private Widget parseWidget(XMLElement iElement, String iClassName, Widget iParent) {
        Widget widget = new Widget(iClassName, iParent);
        ElementParserHelper.parseAttributes(iElement, widget);
        Enumeration childEnum = iElement.enumerateChildren();
        while (childEnum.hasMoreElements()) {
            XMLElement childElement = (XMLElement) (childEnum.nextElement());
            String childName = childElement.getName();
            if ("property".equals(childName)) {
                ElementParserHelper.parseProperty(childElement, widget);
            } else if ("attribute".equals(childName)) {
                ElementParserHelper.parseProperty(childElement, widget);
            } else if ("widget".equals(childName)) {
                String className = childElement.getStringAttribute("class");
                if ("Line".equals(className)) {
                    this.parseWidget(childElement, Widget.QT_LINE, widget);
                } else {
                    this.parseWidget(childElement, className, widget);
                }
            } else if ("layout".equals(childName)) {
                String className = childElement.getStringAttribute("class");
                this.parseLayout(childElement, className, widget);
            } else if ("item".equals(childElement.getName())) {
                this.parseWidgetItem(childElement, widget);
            } else {
                System.err.println("WARNING: Unknown element: " + childName);
            }
        }
        return widget;
    }

    private void parseWidgetItem(XMLElement iElement, Widget iParent) {
        Enumeration childEnum = iElement.enumerateChildren();
        while (childEnum.hasMoreElements()) {
            XMLElement childElement = (XMLElement) (childEnum.nextElement());
            String childName = childElement.getName();
            if ("widget".equals(childName)) {
                String className = childElement.getStringAttribute("class");
                Widget widget = null;
                if ("Line".equals(className)) {
                    widget = this.parseWidget(childElement, Widget.QT_LINE, iParent);
                } else {
                    widget = this.parseWidget(childElement, className, iParent);
                }
                ElementParserHelper.parseAttributes(iElement, widget);
            } else if ("layout".equals(childName)) {
                String className = childElement.getStringAttribute("class");
                Widget layout = this.parseWidget(childElement, className, iParent);
                ElementParserHelper.parseAttributes(iElement, layout);
            } else if ("spacer".equals(childName)) {
                Widget widget = this.parseWidget(childElement, Widget.QT_SPACER, iParent);
                ElementParserHelper.parseAttributes(iElement, widget);
            } else {
                System.err.println("WARNING: Unknown element: " + childName);
            }
        }
    }

    private Layout parseLayout(XMLElement iElement, String iClassName, Widget iParent) {
        Layout layout = new Layout(iClassName, iParent);
        ElementParserHelper.parseAttributes(iElement, layout);
        Enumeration childEnum = iElement.enumerateChildren();
        while (childEnum.hasMoreElements()) {
            XMLElement childElement = (XMLElement) (childEnum.nextElement());
            String childName = childElement.getName();
            if ("property".equals(childName)) {
                ElementParserHelper.parseProperty(childElement, layout);
            } else if ("attribute".equals(childName)) {
                ElementParserHelper.parseProperty(childElement, layout);
            } else if ("item".equals(childElement.getName())) {
                this.parseLayoutItem(childElement, iParent);
            } else {
                System.err.println("WARNING: Unknown element: " + childName);
            }
        }
        return layout;
    }

    private void parseLayoutItem(XMLElement iElement, Widget iParent) {
        Enumeration childEnum = iElement.enumerateChildren();
        while (childEnum.hasMoreElements()) {
            XMLElement childElement = (XMLElement) (childEnum.nextElement());
            String childName = childElement.getName();
            if ("widget".equals(childName)) {
                String className = childElement.getStringAttribute("class");
                Widget widget = null;
                if ("Line".equals(className)) {
                    widget = this.parseWidget(childElement, Widget.QT_LINE, iParent);
                } else {
                    widget = this.parseWidget(childElement, className, iParent);
                }
                ElementParserHelper.parseAttributes(iElement, widget);
            } else if ("layout".equals(childName)) {
                String className = childElement.getStringAttribute("class");
                Widget layoutWidget = new Widget(Widget.QT_LAYOUT_WIDGET, iParent);
                ElementParserHelper.parseAttributes(iElement, layoutWidget);
                this.parseLayout(childElement, className, layoutWidget);
            } else if ("spacer".equals(childName)) {
                Widget widget = this.parseWidget(childElement, Widget.QT_SPACER, iParent);
                ElementParserHelper.parseAttributes(iElement, widget);
            } else {
                System.err.println("WARNING: Unknown element: " + childName);
            }
        }
    }

    private Widget parseMenuBar(XMLElement iElement) {
        return parseMenu(iElement, Widget.QT_MENU_BAR, null);
    }

    private Widget parseMenu(XMLElement iElement, String iClassName, Widget iParent) {
        Widget menu = new Widget(iClassName, iParent);
        Map childMap = new HashMap();
        ElementParserHelper.parseAttributes(iElement, menu);
        Enumeration childEnum = iElement.enumerateChildren();
        while (childEnum.hasMoreElements()) {
            XMLElement childElement = (XMLElement) (childEnum.nextElement());
            String childName = childElement.getName();
            if ("property".equals(childName)) {
                ElementParserHelper.parseProperty(childElement, menu);
            } else if ("widget".equals(childName)) {
                String className = childElement.getStringAttribute("class");
                String name = childElement.getStringAttribute("name");
                if ("QMenu".equals(className)) {
                    Widget child = this.parseMenu(childElement, Widget.QT_MENU, null);
                    childMap.put(name, child);
                } else {
                    System.err.println("WARNING: Unknown menu child class: " + className);
                }
            } else if ("addaction".equals(childName)) {
                String name = childElement.getStringAttribute("name");
                if ("separator".equals(name)) {
                    new Widget(Widget.QT_MENU_SEPARATOR, menu);
                } else {
                    Widget child = (Widget) (childMap.get(name));
                    if (child != null) {
                        menu.addChild(child);
                    } else {
                        child = new Widget(Widget.QT_MENU_ACTION, menu);
                        child.setProperty("name", name);
                    }
                }
            } else {
                System.err.println("WARNING: Unknown menubar element: " + childName);
            }
        }
        return menu;
    }

    private Widget parseToolBar(XMLElement iElement) {
        Widget toolBar = new Widget(Widget.QT_TOOL_BAR);
        ElementParserHelper.parseAttributes(iElement, toolBar);
        Enumeration childEnum = iElement.enumerateChildren();
        while (childEnum.hasMoreElements()) {
            XMLElement childElement = (XMLElement) (childEnum.nextElement());
            String childName = childElement.getName();
            if ("property".equals(childName)) {
                ElementParserHelper.parseProperty(childElement, toolBar);
            } else if ("addaction".equals(childName)) {
                Widget child;
                String name = childElement.getStringAttribute("name");
                if ("separator".equals(name)) {
                    child = new Widget(Widget.QT_TOOL_BAR_SEPARATOR, toolBar);
                } else {
                    child = new Widget(Widget.QT_TOOL_BAR_ACTION, toolBar);
                }
                ElementParserHelper.parseAttributes(childElement, child);
            }
        }
        return toolBar;
    }

    private Action parseAction(XMLElement iElement) {
        Action action = new Action();
        ElementParserHelper.parseAttributes(iElement, action);
        Enumeration childEnum = iElement.enumerateChildren();
        while (childEnum.hasMoreElements()) {
            XMLElement childElement = (XMLElement) (childEnum.nextElement());
            String childName = childElement.getName();
            if ("property".equals(childName)) {
                ElementParserHelper.parseProperty(childElement, action);
            }
        }
        return action;
    }
}
