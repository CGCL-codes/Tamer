package org.devyant.magicbeans.ui.awt.layout;

import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import org.devyant.magicbeans.MagicComponent;
import org.devyant.magicbeans.exceptions.MagicException;
import org.devyant.magicbeans.layout.AbstractMagicLayout;

/**
 * AbstractGridBagMagicLayout is a <b>cool</b> class.
 * 
 * @author ftavares
 * @version $Revision: 1.1 $ $Date: 2006/04/21 15:27:21 $ ($Author: ftavares $)
 * @param <T> The most basic type of this toolkit's components
 * @since Jun 24, 2005 10:16:51 PM
 */
public abstract class AbstractGridBagMagicLayout<T extends Component> extends AbstractMagicLayout<T> {

    /**
     * The serialVersionUID <code>long</code>.
     */
    private static final long serialVersionUID = -8635208882682826756L;

    /**
     * The layout <code>GridBagLayout</code>.
     */
    private final GridBagLayout layout = new GridBagLayout();

    /**
     * The DEFAULT_ANCHOR <code>int</code>.
     */
    public static final int DEFAULT_ANCHOR = GridBagConstraints.LINE_END;

    /**
     * The <code>GridBagConstraints</code>.
     */
    private final GridBagConstraints gridBagConstraints = new GridBagConstraints();

    /**
     * The top value for the default defaultInsets.
     */
    private static final int INSETS_TOP = 5;

    /**
     * The left value for the default defaultInsets.
     */
    private static final int INSETS_LEFT = 5;

    /**
     * The bottom value for the default defaultInsets.
     */
    private static final int INSETS_BOTTOM = 5;

    /**
     * The right value for the default defaultInsets.
     */
    private static final int INSETS_RIGHT = 5;

    /**
     * The defaultInsets for the components.
     */
    public static final Insets defaultInsets = new Insets(INSETS_TOP, INSETS_LEFT, INSETS_BOTTOM, INSETS_RIGHT);

    /**
     * The current line being filled at the <code>GridBagLayout</code>.
     */
    private int line = 0;

    /**
     * The STANDARD_WEIGHTX <code>double</code>.
     */
    public static final double STANDARD_WEIGHTX = 1.0;

    /**
     * The NO_WEIGHTX <code>double</code>.
     */
    public static final double NO_WEIGHTX = 0.0;

    /**
     * @see org.devyant.magicbeans.MagicLayout#addLabeledComponent(java.lang.Object, java.lang.Object, org.devyant.magicbeans.MagicComponent)
     */
    public void addLabeledComponent(final T container, final T label, final MagicComponent<? extends T> component) throws MagicException {
        addComponent(container, label, 0, NO_WEIGHTX);
        addComponent(container, component.render(), 1, STANDARD_WEIGHTX);
        nextRow();
    }

    /**
     * @see org.devyant.magicbeans.MagicLayout#addUnlabeledComponent(java.lang.Object, org.devyant.magicbeans.MagicComponent)
     */
    public void addUnlabeledComponent(T container, MagicComponent<? extends T> component) throws MagicException {
        addComponent(container, component.render(), 0, STANDARD_WEIGHTX, DEFAULT_ANCHOR, GridBagConstraints.BOTH, 2, 1, defaultInsets);
        nextRow();
    }

    /**
     * @see org.devyant.magicbeans.MagicLayout#addControledComponent(java.lang.Object, java.lang.Object, boolean, T[])
     */
    public void addControledComponent(T container, T component, boolean expand, T... controllers) {
        final int fill;
        if (expand) {
            fill = GridBagConstraints.BOTH;
        } else {
            fill = GridBagConstraints.HORIZONTAL;
        }
        addComponent(container, component, 0, STANDARD_WEIGHTX, DEFAULT_ANCHOR, fill, 1, controllers.length, defaultInsets);
        for (int i = 0; i < controllers.length; i++) {
            addComponent(container, controllers[i], 1, NO_WEIGHTX, DEFAULT_ANCHOR, GridBagConstraints.NONE, 1, 1, defaultInsets);
            nextRow();
        }
    }

    /**
     * @see org.devyant.magicbeans.MagicLayout#addButton(Object, java.lang.Object)
     */
    public void addButton(final T container, final T button) {
        addComponent(container, button, 0, NO_WEIGHTX, GridBagConstraints.LINE_END, GridBagConstraints.NONE, GridBagConstraints.REMAINDER, 1, new Insets(INSETS_TOP + 10, INSETS_LEFT, INSETS_BOTTOM, INSETS_RIGHT));
        nextRow();
    }

    /**
     * @see org.devyant.magicbeans.MagicLayout#addStatus(Object, Object)
     */
    public void addStatus(final T container, final T status) {
        addComponent(container, status, 0, STANDARD_WEIGHTX, GridBagConstraints.CENTER, GridBagConstraints.BOTH, GridBagConstraints.REMAINDER, 1, new Insets(0, INSETS_LEFT, 0, INSETS_RIGHT));
        nextRow();
    }

    /**
     * @see org.devyant.magicbeans.MagicLayout#addSimpleComponent(java.lang.Object, java.lang.Object)
     */
    public void addSimpleComponent(T container, T component) {
        addComponent(container, component, 0, STANDARD_WEIGHTX, GridBagConstraints.CENTER, GridBagConstraints.BOTH, GridBagConstraints.REMAINDER, 1, defaultInsets);
        nextRow();
    }

    /**
     * Add a component to the panel.
     * @param container The container to add to
     * @param component The component to add
     * @param x The x coordinate
     * @param weightx The <code>GridBagConstraints</code>'s weightx value
     */
    public final void addComponent(final T container, final T component, int x, double weightx) {
        addComponent(container, component, x, weightx, GridBagConstraints.LINE_START, GridBagConstraints.BOTH);
    }

    /**
     * @param container The container to add to
     * @param component The component to add
     * @param x The x coordinate
     * @param weightx The <code>GridBagConstraints</code>'s weightx value
     * @param anchor The <code>GridBagConstraints</code>'s anchor to use
     * @param fill The <code>GridBagConstraints</code>'s fill to use
     */
    public final void addComponent(final T container, final T component, int x, double weightx, int anchor, int fill) {
        addComponent(container, component, x, weightx, anchor, fill, 1, 1, defaultInsets);
    }

    /**
     * @param container The container to add to
     * @param component The component to add
     * @param x The x coordinate
     * @param weightx The <code>GridBagConstraints</code>'s weightx value
     * @param anchor The <code>GridBagConstraints</code>'s anchor to use
     * @param fill The <code>GridBagConstraints</code>'s fill to use
     * @param gridwidth The <code>GridBagConstraints</code>'s gridwidth to use
     * @param gridheight The <code>GridBagConstraints</code>'s gridheight to use
     * @param insets The <code>GridBagConstraints</code>'s defaultInsets to use
     */
    public final void addComponent(final T container, final T component, int x, double weightx, int anchor, int fill, int gridwidth, int gridheight, Insets insets) {
        ((Container) container).setLayout(this.layout);
        this.gridBagConstraints.anchor = anchor;
        this.gridBagConstraints.fill = fill;
        this.gridBagConstraints.gridwidth = gridwidth;
        this.gridBagConstraints.gridheight = gridheight;
        this.gridBagConstraints.gridx = x;
        this.gridBagConstraints.gridy = this.line;
        this.gridBagConstraints.insets = insets;
        this.gridBagConstraints.weightx = weightx;
        ((Container) container).add(component, this.gridBagConstraints);
    }

    /**
     * Break the current line and move the pointer to the next one.
     */
    public final void nextRow() {
        this.line++;
    }
}
