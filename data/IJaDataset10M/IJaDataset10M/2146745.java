package de.jmda.mview.typeshape;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

public class ElementPanelListModel<T extends Element> extends DefaultListModel implements ListCellRenderer {

    private static final long serialVersionUID = 1L;

    private ElementPanel<T>[] elementPanels;

    private DisplayOptionsModelUtil displayOptionsModelUtil;

    public ElementPanelListModel(Collection<T> elements, DisplayOptionsModelUtil displayOptionsModelUtil) {
        int elementsSize = elements.size();
        elementPanels = new ElementPanel[elementsSize];
        this.displayOptionsModelUtil = displayOptionsModelUtil;
        if (elementsSize == 0) {
            return;
        }
        Element element = elements.iterator().next();
        if (element instanceof VariableElement) {
            if (displayOptionsModelUtil.displayFields() == false) {
                return;
            }
        }
        if (element instanceof ExecutableElement) {
            if (displayOptionsModelUtil.displayMethods() == false) {
                return;
            }
        }
        List<T> elementList = new ArrayList<T>(elements);
        Collections.sort(elementList, new Comparator<T>() {

            @Override
            public int compare(T o1, T o2) {
                return o1.getSimpleName().toString().compareTo(o2.getSimpleName().toString());
            }
        });
        int i = 0;
        for (T elementListItem : elementList) {
            elementPanels[i] = new ElementPanel<T>(elementListItem, displayOptionsModelUtil);
            i++;
        }
    }

    @Override
    public int getSize() {
        return elementPanels.length;
    }

    @Override
    public ElementPanel<T> getElementAt(int index) {
        return elementPanels[index];
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        if (value == null) {
            return new JPanel();
        }
        ElementPanel<?> result;
        if (false == (value instanceof ElementPanel<?>)) {
            throw new IllegalArgumentException("unsupported type " + value.getClass() + ", expected " + ElementPanel.class);
        }
        result = (ElementPanel<?>) value;
        if (isSelected) {
            result.setBackground(new Color(200, 200, 0));
            result.setForeground(list.getSelectionForeground());
        } else {
            result.setBackground(list.getBackground());
            result.setForeground(list.getForeground());
        }
        return result;
    }
}
