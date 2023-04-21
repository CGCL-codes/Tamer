package org.jackysoft.web.ui.renderer;

import org.jackysoft.web.ui.Event;
import org.jackysoft.web.ui.EventHandler;
import org.jackysoft.web.ui.HtmlElement;
import org.jackysoft.web.ui.form.HtmlCheckBox;

/**
 * 这是一个简单的属性渲染器，把对象属性简单的当作一个字符串来显示
 * */
public class CheckBoxRenerder implements SimpleCellRenderer {

    private Object _vo;

    public CheckBoxRenerder() {
    }

    @Override
    public HtmlElement getDisplay() {
        HtmlElement checkbox = new HtmlCheckBox();
        checkbox.addEventListener(Event.CLICK, new EventHandler("checkboxrenerder_click(this)"));
        checkbox.setValue(_vo.toString());
        return checkbox;
    }

    @Override
    public void setValue(Object value) {
        _vo = value;
    }
}
