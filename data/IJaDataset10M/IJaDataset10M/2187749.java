package com.bluebrim.text.impl.client;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;
import com.bluebrim.gui.client.*;
import com.bluebrim.swing.client.*;
import com.bluebrim.text.impl.shared.*;
import com.bluebrim.text.shared.*;

public abstract class CoAbstractTagUI extends CoUserInterface implements CoAttributeListenerIF {

    protected CoAbstractTextEditor m_editor;

    private boolean m_isApplying;

    private boolean m_doClearLocalAttributes;

    private boolean m_isUpdating;

    private String m_lastTag;

    private CoList m_tagList;

    public CoAbstractTagUI() {
        this(null);
    }

    public CoAbstractTagUI(CoAbstractTextEditor editor) {
        super();
        buildForComponent();
        setEditor(editor);
    }

    public void attributesChanged(AttributeSet as) {
        update(as);
        getPanel().repaint();
    }

    public void attributesChanged(CoAttributeEvent e) {
        if (e.didEditableChange()) {
            setAllEnabled(m_editor.isEditable());
            return;
        }
        if (m_isApplying || !didAttributesChange(e)) return;
        attributesChanged(getAttributes(e));
    }

    protected void doAfterCreateUserInterface() {
        m_tagList.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent ev) {
                if (ev.getValueIsAdjusting()) return;
                if (m_isUpdating) return;
                m_isApplying = true;
                String tag = (String) m_tagList.getSelectedValue();
                if (tag != null) {
                    setTag(tag, m_doClearLocalAttributes);
                } else {
                    m_tagList.setSelectedValue(m_lastTag, true);
                }
                m_isApplying = false;
            }
        });
    }

    public void open() {
        Window d = CoWindowList.findWindowFor(this);
        if (d != null) {
            ((CoDialog) d).setLocationRelativeTo(m_editor);
        } else {
            Container w = m_editor.getTopLevelAncestor();
            if (w instanceof Frame) {
                d = new CoDialog((Frame) w, getTitle(), false, this);
            } else if (w instanceof Dialog) {
                d = new CoDialog((Dialog) w, getTitle(), false, this);
            } else {
                return;
            }
        }
        d.show();
    }

    protected void setAllEnabled(boolean b) {
        m_tagList.setEnabled(b);
    }

    public void setContext(CoTextEditorContextIF context) {
        DefaultListModel m = new DefaultListModel();
        m.addElement(com.bluebrim.text.shared.CoStyledDocument.DEFAULT_TAG_NAME);
        if (context != null) {
            Iterator i = getTags(context).iterator();
            while (i.hasNext()) {
                m.addElement(i.next());
            }
        }
        m_isUpdating = true;
        m_tagList.clearSelection();
        m_tagList.setModel(m);
        m_isUpdating = false;
        if (m_editor != null) attributesChanged(getSelectedAttributes());
    }

    public void setEditor(CoAbstractTextEditor editor) {
        if (m_editor == editor) return;
        if (m_editor != null) {
            m_editor.removeAttributeListener(this);
        }
        m_editor = editor;
        if (m_editor != null) {
            m_editor.addAttributeListener(this);
        }
        setAllEnabled(m_editor != null);
    }

    protected void createWidgets(CoPanel p, CoUserInterfaceBuilder b) {
        super.createWidgets(p, b);
        m_tagList = new CoList() {

            protected void processMouseEvent(MouseEvent ev) {
                m_doClearLocalAttributes = (ev.getModifiers() & KeyEvent.ALT_MASK) != 0;
                super.processMouseEvent(ev);
            }
        };
        b.prepareList(m_tagList);
        CoListBox lb = b.createListBox(m_tagList);
        p.add(lb);
        m_tagList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        m_tagList.setVisibleRowCount(10);
        m_tagList.setRequestFocusEnabled(false);
    }

    protected abstract boolean didAttributesChange(CoAttributeEvent e);

    protected abstract AttributeSet getAttributes(CoAttributeEvent e);

    protected abstract AttributeSet getSelectedAttributes();

    protected abstract String getTag(AttributeSet as);

    protected abstract List getTags(CoTextEditorContextIF context);

    protected abstract String getTitle();

    protected abstract void setTag(String tag, boolean doClearLocalAttributes);

    private void update(AttributeSet as) {
        m_isUpdating = true;
        String s = getTag(as);
        if (s == null) {
            s = com.bluebrim.text.shared.CoStyledDocument.DEFAULT_TAG_NAME;
        }
        if (s == CoStyleConstants.AS_IS_STRING_VALUE) {
            m_tagList.clearSelection();
            m_lastTag = null;
        } else {
            m_tagList.setSelectedValue(s, true);
            m_lastTag = s;
        }
        m_isUpdating = false;
    }
}
