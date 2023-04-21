package com.bluebrim.text.impl.client;

import java.awt.Component;
import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.text.AttributeSet;
import com.bluebrim.menus.client.CoMenuBuilder;
import com.bluebrim.menus.client.CoMenuItem;
import com.bluebrim.menus.client.CoPopupMenu;
import com.bluebrim.menus.client.CoSubMenu;
import com.bluebrim.text.shared.CoTextEditorContextIF;

public class CoStyledTextPopupMenu extends CoPopupMenu {

    protected Object getSpellCheckProperties() {
        throw new UnsupportedOperationException("Until Wintertree spell checker is replaced by a GPL spell checker");
    }

    protected void setAllEnabled(boolean b) {
        setAllEnabled(b, this);
    }

    protected static void setAllEnabled(boolean b, JMenu m) {
        Component[] c = m.getMenuComponents();
        for (int i = 0; i < c.length; i++) {
            if (c[i] instanceof JMenu) {
                setAllEnabled(b, (JMenu) c[i]);
            } else if (c[i] instanceof JMenuItem) {
                c[i].setEnabled(b);
            }
        }
    }

    protected static void setAllEnabled(boolean b, JPopupMenu m) {
        Component[] c = m.getComponents();
        for (int i = 0; i < c.length; i++) {
            if (c[i] instanceof JMenu) {
                setAllEnabled(b, (JMenu) c[i]);
            } else if (c[i] instanceof JMenuItem) {
                c[i].setEnabled(b);
            }
        }
    }

    public void setCoCharacterStyleUI(com.bluebrim.text.impl.client.CoCharacterStyleActionUI ui) {
        m_menu.setCoCharacterStyleUI(ui);
    }

    public void setContext(CoTextEditorContextIF context) {
        m_menu.setContext(context);
    }

    public void setCoParagraphStyleUI(com.bluebrim.text.impl.client.CoParagraphStyleActionUI ui) {
        m_menu.setCoParagraphStyleUI(ui);
    }

    public void setEditor(CoAbstractTextEditor editor) {
        m_menu.setEditor(editor);
    }

    public void setTextMeasurementPrefsUI(CoTextMeasurementPrefsUI ui) {
        m_menu.setTextMeasurementPrefsUI(ui);
    }

    public void updatePopupMenu(AttributeSet paraAttr, AttributeSet charAttr, int startSelection, int endSelection) {
        m_menu.update(paraAttr, charAttr, startSelection, endSelection);
    }

    private CoStyledTextMenuImplementation m_menu;

    public CoStyledTextPopupMenu(Action[] actions, CoMenuBuilder builder, CoAbstractTextEditor editor, com.bluebrim.text.impl.client.CoCharacterStyleActionUI charStyle, com.bluebrim.text.impl.client.CoParagraphStyleActionUI paraStyle, CoCharacterTagUI charTag, CoParagraphTagUI paraTag, CoTextMeasurementPrefsUI measurementPrefs) {
        super();
        m_menu = new CoStyledTextMenuImplementation(builder) {

            protected void add(CoSubMenu menu) {
                CoStyledTextPopupMenu.this.add(menu);
            }

            protected void add(CoMenuItem menuItem) {
                CoStyledTextPopupMenu.this.add(menuItem);
            }

            protected void addSeparator() {
                CoStyledTextPopupMenu.this.addSeparator();
            }

            protected void setAllEnabled(boolean b) {
                CoStyledTextPopupMenu.this.setAllEnabled(b);
            }
        };
        m_menu.create(actions, editor, charStyle, paraStyle, charTag, paraTag, measurementPrefs);
    }

    public void setParagraphTagUI(CoParagraphTagUI ui) {
        m_menu.setParagraphTagUI(ui);
    }
}
