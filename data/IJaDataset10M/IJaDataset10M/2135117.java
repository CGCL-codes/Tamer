package org.argouml.ui;

/**
 * This exists purely for the backwards compatibility of modules. New code
 * should use {@link org.argouml.application.api.AbstractArgoJPanel}.
 * 
 * @deprecated by Bob Tarling in 0.25.5. Use
 *             {@link org.argouml.application.api.AbstractArgoJPanel}.
 *             <p>
 *             Because this is a widely used class it is recommended to retain
 *             the deprecated class for at least two major releases (ie until
 *             after 0.28 is released using the current numbering scheme).
 */
@Deprecated
public abstract class AbstractArgoJPanel extends org.argouml.application.api.AbstractArgoJPanel {

    /**
     * The constructor.
     *
     */
    @Deprecated
    public AbstractArgoJPanel() {
        super();
    }

    /**
     * The constructor.
     *
     * @param title The name as a localized string.
     */
    @Deprecated
    public AbstractArgoJPanel(String title) {
        super(title);
    }

    /**
     * The constructor.
     *
     * @param title The name (a localized string).
     * @param t if true, remove tab from parent JTabbedPane
     */
    @Deprecated
    public AbstractArgoJPanel(String title, boolean t) {
        super(title, t);
    }
}
