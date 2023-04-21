package calclipse.caldron.gui.skinning;

import java.awt.Color;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.TextUI;
import javax.swing.text.JTextComponent;

/**
 * A stylable wrapper around a text component.
 * This class adds support for the following properties:
 * <ul>
 * <li>
 * {@link
 * calclipse.caldron.gui.skinning.StylableProperties#CARET_COLOR}
 * </li>
 * <li>
 * {@link
 * calclipse.caldron.gui.skinning.StylableProperties#DISABLED_COLOR}
 * (maps to the disabledTextColor property)
 * </li>
 * <li>
 * {@link
 * calclipse.caldron.gui.skinning.StylableProperties#SELECTION_COLOR}
 * (maps to the selectedTextColor property)
 * </li>
 * <li>
 * {@link
 * calclipse.caldron.gui.skinning.StylableProperties#SELECTION_BACKGROUND}
 * (maps to the selectionColor property)
 * </li>
 * </ul>
 * @author T. Sommerland
 */
public class StylableTextComponent extends StylableComponent<JTextComponent> {

    public StylableTextComponent(final JTextComponent component, final String selector) {
        super(component, selector);
        defaultValues.put(StylableProperties.CARET_COLOR, component.getCaretColor());
        defaultValues.put(StylableProperties.DISABLED_COLOR, component.getDisabledTextColor());
        defaultValues.put(StylableProperties.SELECTION_COLOR, component.getSelectedTextColor());
        defaultValues.put(StylableProperties.SELECTION_BACKGROUND, component.getSelectionColor());
    }

    @Override
    protected void setUI(final ComponentUI ui) {
        component.setUI((TextUI) ui);
    }

    @Override
    public void setValue(final String property, final Object value) {
        if (StylableProperties.CARET_COLOR.equals(property)) {
            component.setCaretColor((Color) value);
        } else if (StylableProperties.DISABLED_COLOR.equals(property)) {
            component.setDisabledTextColor((Color) value);
        } else if (StylableProperties.SELECTION_COLOR.equals(property)) {
            component.setSelectedTextColor((Color) value);
        } else if (StylableProperties.SELECTION_BACKGROUND.equals(property)) {
            component.setSelectionColor((Color) value);
        } else {
            super.setValue(property, value);
        }
    }
}
