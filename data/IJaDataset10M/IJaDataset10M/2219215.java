package com.trsvax.gwthello.client.gwtui.widgets;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.i18n.client.Dictionary;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.MenuBar;

public class MenuWidget extends Composite {

    final MenuBar widget;

    final Element element;

    final Dictionary dictionary;

    public MenuWidget(Element element, Dictionary dictionary) {
        this.element = element;
        this.dictionary = dictionary;
        widget = new MenuBar();
        init();
        element.setInnerHTML("");
        initWidget(widget);
    }

    void init() {
        Command cmd = new Command() {

            public void execute() {
            }
        };
        NodeList<Element> nl = element.getElementsByTagName("dt");
        for (int i = 0; i < nl.getLength(); i++) {
            MenuBar sub = new MenuBar(true);
            String head = nl.getItem(i).getInnerText();
            Element dd = nl.getItem(i).getNextSiblingElement();
            while (true) {
                if (dd == null) break;
                String tag = dd.getTagName();
                if (dd.getTagName().equals("DD")) {
                    String text = dd.getInnerText();
                    sub.addItem(text, cmd);
                }
                if (dd.getTagName().equals("DT")) {
                    break;
                }
                dd = dd.getNextSiblingElement();
            }
            widget.addItem(head, sub);
        }
    }
}
