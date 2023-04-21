package esa.herschel.randres.tm.utils;

import java.util.HashMap;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

public class MyLabelProvider extends LabelProvider {

    private HashMap<String, String> m_items;

    public MyLabelProvider(HashMap<String, String> items) {
        m_items = items;
    }

    @Override
    public String getText(Object element) {
        return m_items.get(element);
    }
}
