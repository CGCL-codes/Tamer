package addressbook;

import java.util.*;
import javax.swing.*;

public abstract class TabbedArrayOfComponents extends JTabbedPane {

    TabbedArrayOfComponents(int tabPlacement) {
        super(tabPlacement);
    }

    abstract JComponent produce();

    void addElement(String name) {
        if (indexOfTab(name) > 0) {
            setSelectedIndex(indexOfTab(name));
            return;
        }
        addTab(name, produce());
    }

    void modifyElement(String name, String newname) {
        int i = indexOfTab(name);
        if (i >= 0) setTitleAt(i, newname);
    }

    void modifyElement(String newname) {
        int i = getSelectedIndex();
        if (i >= 0) setTitleAt(i, newname);
    }

    void deleteElement() {
        int i = getSelectedIndex();
        if (i >= 0) removeTabAt(i);
    }

    void deleteElement(String name) {
        int i = indexOfTab(name);
        if (i >= 0) removeTabAt(i);
    }

    JComponent getElement(String name) {
        int i = indexOfTab(name);
        if (i < 0) return null;
        return (JComponent) getComponentAt(i);
    }
}
