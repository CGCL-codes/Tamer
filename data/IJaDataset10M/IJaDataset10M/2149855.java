package com.cell.gfx.gui;

import java.util.Vector;
import com.cell.CObject;
import com.cell.gfx.IGraphics;

public class FormManager extends CObject {

    public static int FormBackAlphaColor = 0x80000000;

    protected Control DragedControl = null;

    public boolean Enable = true;

    protected Cursor CurCursor = new Cursor();

    protected Vector<Form> m_Forms = new Vector<Form>();

    protected FormManagerListener Listener;

    /**top level form , if is exist, drop render for next all*/
    private int TopFullFormIndex = -1;

    public FormManager(FormManagerListener listener) {
        Listener = listener;
    }

    /**
	 * @return if clip mouse event
	 */
    public boolean updateForms() {
        boolean flag = false;
        synchronized (m_Forms) {
            try {
                CurCursor.update(this);
            } catch (Exception err) {
                err.printStackTrace();
            }
            TopFullFormIndex = -1;
            boolean keyEnable = Form.KeyEnable;
            if (!Enable) Form.KeyEnable = false;
            if (m_Forms.size() > 0) {
                Form form = ((Form) m_Forms.lastElement());
                try {
                    boolean catchPoint = true;
                    if (!form.includePoint(Form.getPointerX(), Form.getPointerY())) {
                        catchPoint = false;
                        if (Form.isPointerDown() && form.isEnabled() && !(form instanceof Menu) && form.IsLayoutDialog == false && m_Forms.size() > 1) {
                            Form dst = null;
                            Form src = form;
                            for (int i = m_Forms.size() - 2; i >= 0; i--) {
                                dst = m_Forms.elementAt(i);
                                if (dst.isEnabled() && dst.includePoint(Form.getPointerX(), Form.getPointerY())) {
                                    m_Forms.removeElement(dst);
                                    m_Forms.addElement(dst);
                                    src.notifyPause();
                                    dst.notifyResume();
                                    form = dst;
                                    catchPoint = true;
                                    break;
                                }
                            }
                        }
                    }
                    form.IsDrawItemFocus = true;
                    form.notifyLogic();
                    if (form.IsLayoutDialog && form.IsVisible) {
                        flag = true;
                        Form.processedKey();
                    } else if (catchPoint && form.IsProcessKey) {
                        flag = true;
                        Form.processedKey();
                    }
                } catch (Exception err) {
                    err.printStackTrace();
                    form.IsClosed = true;
                    try {
                        form.notifyDestroy();
                        m_Forms.removeElement(form);
                    } catch (Exception err2) {
                        err2.printStackTrace();
                    }
                    Form.KeyEnable = keyEnable;
                    flag = true;
                }
            }
            for (int i = m_Forms.size() - 2; i >= 0; i--) {
                Form form = (Form) m_Forms.elementAt(i);
                try {
                    form.notifyLogic();
                } catch (Exception err) {
                    err.printStackTrace();
                    form.IsClosed = true;
                    try {
                        form.notifyDestroy();
                        m_Forms.removeElement(form);
                    } catch (Exception err2) {
                        err2.printStackTrace();
                    }
                    Form.KeyEnable = keyEnable;
                    flag = true;
                    break;
                } finally {
                    if (form.IsLayoutDialog && form.IsVisible) {
                        flag = true;
                        Form.processedKey();
                    } else if (form.IsProcessKey) {
                        if (form.includePoint(Form.getPointerX(), Form.getPointerY())) {
                            flag = true;
                            Form.processedKey();
                        }
                    }
                }
            }
            if (m_Forms.size() > 0) {
                Form top = getTopForm();
                if (top != null) {
                    if (!top.isVisible()) {
                        try {
                            m_Forms.removeElement(top);
                            Listener.formRemoved(top, this);
                            top.notifyDestroy();
                        } catch (Exception err2) {
                            err2.printStackTrace();
                        }
                        top.OwnerManager = null;
                        if (!(top instanceof Menu)) {
                            Form next = getTopForm();
                            if (next != null) {
                                next.notifyResume();
                                next = getTopHiddenForm();
                                if (next != null) {
                                    next.open();
                                    Listener.formShown(next, this);
                                }
                            }
                        }
                    } else if (top.isAlwaysVisible()) {
                        Form next = getTopHideableForm();
                        if (next != null && Form.isKeyDownState(next.KeyClose)) {
                            next.close(false);
                        }
                    }
                }
            }
            int count = m_Forms.size();
            for (int i = count - 1; i >= 0; i--) {
                Form form = m_Forms.elementAt(i);
                if (form.IsClosed && !form.isVisible()) {
                    try {
                        m_Forms.removeElementAt(i);
                        Listener.formRemoved(form, this);
                        form.notifyDestroy();
                    } catch (Exception err2) {
                        err2.printStackTrace();
                    }
                    form.OwnerManager = null;
                    if (!(form instanceof Menu)) {
                        Form next = getTopForm();
                        if (next != null) {
                            next.notifyResume();
                            if (next.isAlwaysVisible()) {
                                next = getTopHiddenForm();
                                if (next != null) {
                                    next.open();
                                    Listener.formShown(next, this);
                                }
                            }
                        }
                    }
                }
            }
            Form.KeyEnable = keyEnable;
        }
        return flag;
    }

    public int setTopFullForm(Form form) {
        TopFullFormIndex = m_Forms.indexOf(form);
        return TopFullFormIndex;
    }

    public void renderForms(IGraphics g) {
        synchronized (m_Forms) {
            int count = m_Forms.size() - 1;
            for (int i = 0; i <= count; i++) {
                if (i < TopFullFormIndex) {
                    continue;
                }
                Form form = m_Forms.elementAt(i);
                if (form.isFormTransition() || form.isVisible()) {
                    form.notifyRender(g);
                }
            }
            Form.clearClip(g);
            CurCursor.render(g);
        }
    }

    public int size() {
        return m_Forms.size();
    }

    public Form getForm(int index) {
        if (index < m_Forms.size()) {
            return ((Form) m_Forms.elementAt(index));
        }
        return null;
    }

    public Form getTopHiddenForm() {
        Form form = null;
        for (int i = m_Forms.size() - 1; i >= 0; i--) {
            form = m_Forms.elementAt(i);
            if (!form.IsClosed && !form.isVisible()) {
                return form;
            }
        }
        return null;
    }

    public Form getTopForm() {
        if (!m_Forms.isEmpty()) {
            return (Form) m_Forms.lastElement();
        }
        return null;
    }

    public Form getTopHideableForm() {
        Form form = null;
        for (int i = m_Forms.size() - 1; i >= 0; i--) {
            form = m_Forms.elementAt(i);
            if (form.isAlwaysVisible()) {
                continue;
            } else {
                return form;
            }
        }
        return null;
    }

    public Form getRootForm() {
        if (!m_Forms.isEmpty()) {
            return (Form) m_Forms.firstElement();
        }
        return null;
    }

    public Form getRootHideableForm() {
        Form form = null;
        for (int i = 0; i < m_Forms.size(); i++) {
            form = m_Forms.elementAt(i);
            if (form.isAlwaysVisible()) {
                continue;
            } else {
                return form;
            }
        }
        return null;
    }

    public int pushForm(Form form) {
        if (!m_Forms.contains(form)) {
            if (!m_Forms.isEmpty()) {
                Form prew = m_Forms.lastElement();
                if (!form.IsLayoutDialog && !(form instanceof Menu) && prew.IsLayoutAutoHide && prew.isVisible()) {
                    prew.hide();
                    Listener.formHidden(prew, this);
                    prew.notifyPause();
                }
            }
            form.OwnerManager = this;
            m_Forms.addElement(form);
            Listener.formAdded(form, this);
        } else if (form != null && m_Forms.lastElement() != form) {
            m_Forms.remove(form);
            m_Forms.addElement(form);
            form.show();
            Listener.formShown(form, this);
            form.notifyResume();
        }
        {
        }
        return m_Forms.size() - 1;
    }

    public void removeForm(Form form) {
        if (m_Forms.contains(form)) {
            if (form.OwnerManager == this) {
                form.OwnerManager = null;
            }
            m_Forms.removeElement(form);
            Listener.formRemoved(form, this);
        }
    }

    public void clearForm() {
        Form[] forms = new Form[m_Forms.size()];
        m_Forms.copyInto(forms);
        for (int i = 0; i < forms.length; i++) {
            Form form = forms[i];
            if (!form.isAlwaysVisible()) {
                form.notifyDestroy();
                if (form.isVisible()) {
                    form.close(false);
                    Listener.formHidden(form, this);
                }
                {
                    m_Forms.removeElement(form);
                    Listener.formRemoved(form, this);
                    form.OwnerManager = null;
                }
            }
        }
    }

    public void clearAllForm() {
        for (int i = 0; i < m_Forms.size(); i++) {
            Form form = m_Forms.elementAt(i);
            if (form.isVisible()) {
                form.close(false);
                Listener.formHidden(form, this);
            }
            form.notifyDestroy();
            Listener.formRemoved(form, this);
            form.OwnerManager = null;
        }
        m_Forms.clear();
    }

    public void destory() {
        clearAllForm();
    }

    public int indexOf(Form form) {
        return m_Forms.indexOf(form);
    }

    public void setCursor(Cursor cur) {
        CurCursor = cur;
    }

    public Cursor getCursor() {
        return CurCursor;
    }
}
