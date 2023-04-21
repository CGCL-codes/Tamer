package org.jmeld.ui;

import org.jmeld.ui.*;
import org.jmeld.ui.search.*;
import org.jmeld.ui.swing.*;
import org.jmeld.ui.util.*;
import org.jmeld.util.*;
import javax.swing.*;

public abstract class AbstractBarDialog extends JPanel {

    private JMeldPanel meldPanel;

    public AbstractBarDialog(JMeldPanel meldPanel) {
        this.meldPanel = meldPanel;
        init();
    }

    protected abstract void init();

    protected abstract void _activate();

    protected abstract void _deactivate();

    protected JMeldPanel getMeldPanel() {
        return meldPanel;
    }

    public final void activate() {
        _activate();
    }

    public final void deactivate() {
        _deactivate();
    }
}
