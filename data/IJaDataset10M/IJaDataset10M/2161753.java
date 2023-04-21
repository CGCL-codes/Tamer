package org.makagiga.commons;

import static org.makagiga.commons.UI._;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.makagiga.commons.beans.AbstractBeanInfo;

/** A small color chooser. */
public class MSmallColorChooser extends MPanel {

    /**
	 * @since 2.0
	 */
    public static final String COLOR_PROPERTY = "color";

    private MColorButton colorButton;

    private MSmallButton clearButton;

    /**
	 * Constructs a small color chooser.
	 * @param text A text label
	 * @param colors A set of colors displayed in the panel
	 *
	 * @throws NullPointerException If @p colors is @c null
	 */
    public MSmallColorChooser(final String text, final Color... colors) {
        super(UI.HORIZONTAL);
        setOpaque(false);
        TK.checkNull(colors, "colors");
        addGap();
        if (text != null) {
            add(new MLabel(text));
            addGap();
        }
        clearButton = new MSmallButton(MActionInfo.SET_DEFAULT_VALUES) {

            @Override
            protected void onClick() {
                MSmallColorChooser.this.setColor(null);
            }
        };
        clearButton.setVisible(false);
        add(clearButton);
        for (Color i : colors) add(new Button(i));
        addGap();
        colorButton = new MColorButton();
        MLabel more = MLabel.createFor(colorButton, _("More:"));
        more.setStyle("font-size: smaller");
        add(more);
        addGap();
        colorButton.addValueListener(new ValueListener<Color>() {

            public void valueChanged(final ValueEvent<Color> e) {
                MSmallColorChooser.this.firePropertyChange(MSmallColorChooser.COLOR_PROPERTY, e.getOldValue(), e.getNewValue());
                MSmallColorChooser.this.fireStateChanged();
            }
        });
        add(colorButton);
        addChangeListener(new ChangeListener() {

            public void stateChanged(final ChangeEvent e) {
                onChange();
            }
        });
    }

    /**
	 * Constructs a small color chooser with default set of colors.
	 * @param text A text label
	 */
    public MSmallColorChooser(final String text) {
        this(text, createDefaultPalette());
    }

    /**
	 * Constructs a small color chooser with default set of colors.
	 */
    public MSmallColorChooser() {
        this(null);
        AbstractBeanInfo.init();
    }

    /**
	 * @since 2.0
	 */
    public void addChangeListener(final ChangeListener l) {
        listenerList.add(ChangeListener.class, l);
    }

    /**
	 * @since 2.0
	 */
    public ChangeListener[] getChangeListeners() {
        return listenerList.getListeners(ChangeListener.class);
    }

    /**
	 * @since 2.0
	 */
    public void removeChangeListener(final ChangeListener l) {
        listenerList.remove(ChangeListener.class, l);
    }

    /**
	 * @since 2.4
	 */
    public static Color[] createDefaultPalette() {
        return new Color[] { new Color(0xD9FFB3), new Color(0xFFFFB3), new Color(0xFFD9B3), new Color(0xFFB3B3), new Color(0xFFB3FF), new Color(0xB3D9FF), new Color(0xB3FFFF), Color.WHITE, Color.LIGHT_GRAY, Color.GRAY, Color.BLACK };
    }

    /**
	 * @since 2.4
	 */
    public static Color[] createWeb20Palette() {
        return new Color[] { new Color(0xCBFD06), new Color(0xCFEB41), new Color(0xB7D362), new Color(0x71B723), new Color(0xffff00), new Color(0xFF9934), new Color(0xE73300), new Color(0xFF1A8E), new Color(0xD2E6FF), new Color(0x6EB9E0), new Color(0x0593FF), new Color(0x000000), new Color(0xffffff) };
    }

    /**
	 * EXAMPLE:
	 * @code
	 * MSmallColorChooser smallColorChooser = new MSmallColorChooser();
	 * smallColorChooser.getClearButton().setVisible(true);
	 * @endcode
	 *
	 * @since 3.0
	 */
    public MSmallButton getClearButton() {
        return clearButton;
    }

    /**
	 * Returns the color.
	 */
    public Color getColor() {
        return colorButton.getColor();
    }

    /**
	 * Sets color to @p value.
	 */
    public void setColor(final Color value) {
        Object oldColor = getColor();
        colorButton.setColor(value);
        if (TK.isChange(oldColor, value)) firePropertyChange(COLOR_PROPERTY, oldColor, value);
    }

    /**
	 * Enables/disables this component and its subcomponents.
	 * @param value @c true - enable
	 */
    @Override
    public void setEnabled(final boolean value) {
        super.setEnabled(value);
        clearButton.setEnabled(value);
        colorButton.setEnabled(value);
    }

    /**
	 * @since 2.0
	 */
    protected void fireStateChanged() {
        TK.fireStateChanged(this, getChangeListeners());
    }

    /**
	 * Invoked when color is changed.
	 * By default this method does nothing.
	 *
	 * @since 2.0
	 */
    protected void onChange() {
    }

    private final class Button extends MSmallColorButton {

        public Button(final Color color) {
            super(color, new Dimension(17, 17), MColorIcon.Type.OVAL);
        }

        @Override
        protected void onClick() {
            MSmallColorChooser.this.setColor(super.getColor());
            MSmallColorChooser.this.fireStateChanged();
        }
    }
}
