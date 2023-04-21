package calclipse.caldron.gui.skinning;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.border.Border;
import javax.swing.plaf.ComponentUI;
import calclipse.core.gui.skin.Stylable;
import calclipse.core.gui.skin.style.Background;
import calclipse.core.gui.skin.style.BackgroundStyle;
import calclipse.core.gui.skin.style.UIStyle;

/**
 * A {@link calclipse.core.gui.skin.Stylable}
 * wrapper around a <code>JComponent</code>.
 * The stylable properties supported by this class are
 * {@link calclipse.caldron.gui.skinning.StylableProperties#BACKGROUND},
 * {@link calclipse.caldron.gui.skinning.StylableProperties#UI},
 * {@link calclipse.caldron.gui.skinning.StylableProperties#COLOR},
 * {@link calclipse.caldron.gui.skinning.StylableProperties#CURSOR},
 * {@link calclipse.caldron.gui.skinning.StylableProperties#FONT} and
 * {@link calclipse.caldron.gui.skinning.StylableProperties#BORDER}.
 * This class does not support custom background painting,
 * so changing the background style only affects the background color.
 * This class is abstract and subclasses must override
 * {@link #setUI(ComponentUI)}.
 * 
 * @author T. Sommerland
 */
public abstract class StylableComponent<T extends JComponent> implements Stylable {

    /**
     * The default values for the stylable properties supported by this class.
     * Subclasses may put additional values in here.
     */
    protected final Map<String, Object> defaultValues = new HashMap<String, Object>();

    /**
     * The component that is to be styled.
     */
    protected final T component;

    private final String selector;

    public StylableComponent(final T component, final String selector) {
        this.component = component;
        this.selector = selector;
        defaultValues.put(StylableProperties.BACKGROUND, new Background(component.getBackground()));
        defaultValues.put(StylableProperties.UI, null);
        defaultValues.put(StylableProperties.COLOR, component.getForeground());
        defaultValues.put(StylableProperties.CURSOR, component.getCursor());
        defaultValues.put(StylableProperties.FONT, component.getFont());
        defaultValues.put(StylableProperties.BORDER, component.getBorder());
    }

    /**
     * Sets the UI delegate of {@link #component}.
     */
    protected abstract void setUI(final ComponentUI ui);

    @Override
    public Object getDefaultValue(final String property) {
        return defaultValues.get(property);
    }

    @Override
    public Collection<String> getProperties() {
        return Collections.unmodifiableCollection(defaultValues.keySet());
    }

    @Override
    public String getSelector() {
        return selector;
    }

    @Override
    public void setValue(final String property, final Object value) {
        if (StylableProperties.BACKGROUND.equals(property)) {
            final BackgroundStyle bgStyle = (BackgroundStyle) value;
            component.setBackground(bgStyle.getColor());
        } else if (StylableProperties.UI.equals(property)) {
            final UIStyle uiStyle = (UIStyle) value;
            if (uiStyle == null) {
                component.updateUI();
            } else {
                setUI(uiStyle.getUI(component));
            }
        } else if (StylableProperties.COLOR.equals(property)) {
            component.setForeground((Color) value);
        } else if (StylableProperties.CURSOR.equals(property)) {
            component.setCursor((Cursor) value);
        } else if (StylableProperties.FONT.equals(property)) {
            component.setFont((Font) value);
        } else if (StylableProperties.BORDER.equals(property)) {
            component.setBorder((Border) value);
        } else {
            throw new IllegalArgumentException(property);
        }
    }
}
