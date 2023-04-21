package net.sourceforge.nattable.extension.typeconfig.style;

import net.sourceforge.nattable.model.DefaultNatTableModel;
import net.sourceforge.nattable.typeconfig.style.StyleConfigRegistry;
import org.eclipse.swt.widgets.Display;

public interface IStyleMarkupHandler {

    /**
	 * If setting grid level styles in the style markup definition and you want to set custom alternate
	 * row colors, then make wrap those in the @link{AlternateRowColoringStyleConfiguration} class.
	 * @param assembler
	 * @param tableModel
	 */
    public void loadGridAttributes(DefaultNatTableModel tableModel, StyleConfigRegistry styleConfigRegistry, Display currentDisplay);

    /**
	 * The implementation of this method needs to handle the creation of default style ie: are at a global level, and the styles which belong
	 * to a specific display type converter.  In addition, the implementor also needs to load the display type converters from the configuration
	 * @param assembler
	 * @param registry
	 * @param currentDisplay
	 */
    public void assembleConfiguration(StyleConfigRegistry styleConfigRegistry, Display currentDisplay);
}
