package net.laubenberger.bogatyr.view.swing.pane;

import java.awt.Component;
import java.io.IOException;
import java.net.URL;
import javax.swing.JEditorPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.laubenberger.bogatyr.helper.HelperLog;

/**
 * This is an extended JEditorPane.
 *
 * @author Stefan Laubenberger
 * @version 0.9.2 (20100611)
 * @since 0.7.0
 */
public class PaneEditor extends JEditorPane {

    private static final long serialVersionUID = -3298005917085461997L;

    private static final Logger log = LoggerFactory.getLogger(PaneEditor.class);

    public PaneEditor() {
        super();
        if (log.isTraceEnabled()) log.trace(HelperLog.constructor());
    }

    public PaneEditor(final String type, final String text) {
        super(type, text);
        if (log.isTraceEnabled()) log.trace(HelperLog.constructor(type, text));
    }

    public PaneEditor(final String url) throws IOException {
        super(url);
        if (log.isTraceEnabled()) log.trace(HelperLog.constructor(url));
    }

    public PaneEditor(final URL initialPage) throws IOException {
        super(initialPage);
        if (log.isTraceEnabled()) log.trace(HelperLog.constructor(initialPage));
    }

    @Override
    public void setEnabled(final boolean isEnabled) {
        super.setEnabled(isEnabled);
        for (final Component component : getComponents()) {
            component.setEnabled(isEnabled);
        }
    }
}
