package com.bluebrim.text.impl.client;

import com.bluebrim.gui.client.*;
import com.bluebrim.swing.client.*;
import com.bluebrim.text.shared.*;

/**
 */
public abstract class CoAbstractTextStyleUI extends CoDomainUserInterface implements CoNumericComponentBuilder {

    protected CoTypographyContextIF m_context;

    public CoAbstractTextStyleUI() {
        super();
    }

    public final CoTypographyContextIF getContext() {
        return m_context;
    }

    protected abstract void postContextChange(CoTypographyContextIF context);

    public void setContext(CoTypographyContextIF context) {
        m_context = context;
        postContextChange(m_context);
    }

    protected CoPanel createDefaultPanel() {
        CoPanel p = getUIBuilder().createPanel(new CoBorderLayout(), false, null, "PANEL");
        getUIBuilder().setTransparentWidgets(true);
        return p;
    }
}
