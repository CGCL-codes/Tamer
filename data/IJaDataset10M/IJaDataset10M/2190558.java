package net.sf.rcpforms.widgetwrapper.wrapper;

import net.sf.rcpforms.widgets2.IRCPAbstractText;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;

public abstract class AbstractSimpleTextControl extends RCPControl implements IRCPAbstractText {

    private ModifyListener m_modifyListener;

    public AbstractSimpleTextControl(final String labelText, final int style, final Object uid) {
        super(labelText, style, uid);
        m_modifyListener = null;
    }

    public AbstractSimpleTextControl(final String labelText, final int style) {
        super(labelText, style);
        m_modifyListener = null;
    }

    /**
     * @see net.sf.rcpforms.widgetwrapper.wrapper.RCPControl#defineSelfListeners()
     */
    @Override
    protected void defineSelfListeners() {
        super.defineSelfListeners();
        m_modifyListener = new ModifyListener() {

            private String oldText = null;

            public void modifyText(final ModifyEvent e) {
                final String current = getText_noEv();
                firePropertyChange(PROP_TEXT, oldText, current);
                oldText = current;
            }
        };
        swtAttachModifyListener(m_modifyListener);
    }

    protected void swtAttachModifyListener(final ModifyListener modifyListener) {
    }

    protected void swtRemoveModifyListener(final ModifyListener modifyListener) {
    }

    @Override
    public void dispose() {
        swtRemoveModifyListener(m_modifyListener);
        super.dispose();
    }

    public void setText(final String text) {
        if (!hasSpawned()) {
            addSpawnHook4Property(PROP_TEXT, text);
            return;
        }
        final String oldText = getText_noEv();
        setText_noEv(text);
        firePropertyChange(PROP_TEXT, oldText, text);
    }

    protected abstract void setText_noEv(final String text);

    public String getText() {
        if (!hasSpawned()) {
            return (String) getCashedHookPropertyValue(PROP_TEXT, null);
        }
        return getText_noEv();
    }

    protected abstract String getText_noEv();
}
