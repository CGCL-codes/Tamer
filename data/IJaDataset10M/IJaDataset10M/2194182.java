package net.laubenberger.bogatyr.view.swing;

import java.awt.Color;
import javax.swing.JSeparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.laubenberger.bogatyr.helper.HelperLog;

/**
 * This is an extended JSeparator.
 *
 * @author Stefan Laubenberger
 * @version 0.9.2 (20100611)
 * @since 0.2.0
 */
public class Separator extends JSeparator {

    private static final long serialVersionUID = 544751396135811303L;

    private static final Logger log = LoggerFactory.getLogger(Separator.class);

    public Separator() {
        super();
        if (log.isTraceEnabled()) log.trace(HelperLog.constructor());
    }

    public Separator(final int orientation) {
        super(orientation);
        if (log.isTraceEnabled()) log.trace(HelperLog.constructor(orientation));
    }

    public Separator(final Color color) {
        this();
        if (log.isTraceEnabled()) log.trace(HelperLog.constructor(color));
        setAlignmentY(TOP_ALIGNMENT);
        setAlignmentX(LEFT_ALIGNMENT);
        setBackground(color);
        setForeground(color);
    }

    public Separator(final int orientation, final Color color) {
        this(orientation);
        if (log.isTraceEnabled()) log.trace(HelperLog.constructor(orientation, color));
        setAlignmentY(TOP_ALIGNMENT);
        setAlignmentX(LEFT_ALIGNMENT);
        setBackground(color);
        setForeground(color);
    }
}
