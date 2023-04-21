package com.dukesoftware.ongakumusou.gui.tab.edit.body;

import java.util.Enumeration;
import javax.swing.JTextField;
import com.dukesoftware.ongakumusou.data.element.Element;

/**
 * 
 * <p></p>
 *
 * <h5>update history</h5> 
 * <p>2007/07/25 This file was created.</p>
 *
 * @author 
 * @since 2007/07/25
 * @version last update 2007/07/25
 */
public class SearchResultSetterForElement {

    private final JTextField textField;

    private final ListTemplate template;

    private Enumeration<?> memEnmeration;

    public SearchResultSetterForElement(ListTemplate template, JTextField textField) {
        this.textField = textField;
        this.template = template;
    }

    public void search() {
        final String text = textField.getText();
        if (memEnmeration == null) {
            memEnmeration = template.elementListModel.elements();
        }
        for (Enumeration<?> en = memEnmeration; en.hasMoreElements(); ) {
            Element elem = (Element) en.nextElement();
            if (elem.title().contains(text)) {
                memEnmeration = en;
                template.setSelectedValueInElementList(elem);
                break;
            }
        }
        if (!memEnmeration.hasMoreElements()) {
            memEnmeration = null;
        }
    }
}
