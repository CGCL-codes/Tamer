package uk.ac.lkl.migen.system.expresser.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import uk.ac.lkl.common.util.config.MiGenConfiguration;
import uk.ac.lkl.migen.system.expresser.model.ExpresserModel;
import uk.ac.lkl.migen.system.expresser.model.ModelColor;
import uk.ac.lkl.migen.system.expresser.ui.icon.IconManager;
import uk.ac.lkl.migen.system.expresser.ui.icon.IconVariableFactory;

/**
 * Creates a popup menu with icons for allocating colours 
 * followed by the icon for the palette
 * 
 * @author Ken Kahn
 *
 */
public class ColorChooserPopupMenu extends JPopupMenu {

    public static ImageIcon colorPaletteIcon = IconManager.getIcon("color-selection", 32);

    private static ImageIcon colorPaletteIconSmall = IconManager.getIcon("color-selection", 23);

    public ColorChooserPopupMenu(final ColorChosen handler, ExpresserModel expresserModel) {
        super();
        if (!MiGenConfiguration.isRequireOnlyTotalTileCount()) {
            List<ModelColor> colors = expresserModel.getPalette().getColors();
            for (final ModelColor color : colors) {
                if (!color.isNegative()) {
                    ImageIcon icon = IconVariableFactory.createColorAllocationIcon(color.getRGB(), color.isNegative());
                    JMenuItem menuItem = new JMenuItem(icon);
                    ActionListener selectColor = new ActionListener() {

                        public void actionPerformed(ActionEvent e) {
                            handler.colorChosen(color);
                        }
                    };
                    menuItem.addActionListener(selectColor);
                    add(menuItem);
                }
            }
        }
        List<ModelColor> colorsWithoutRules = expresserModel.getColorsWithoutRules();
        if (!colorsWithoutRules.isEmpty()) {
            ImageIcon manyColorsAllocationIcon = IconVariableFactory.createManyColorsAllocationIcon(colorsWithoutRules);
            JMenuItem paletteMenuItem = new JMenuItem(manyColorsAllocationIcon == null ? colorPaletteIconSmall : manyColorsAllocationIcon);
            ActionListener noColor = new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    handler.colorChosen(null);
                }
            };
            paletteMenuItem.addActionListener(noColor);
            add(paletteMenuItem);
        }
    }

    public static JButton createColorChooser(final ColorChosen handler, final ExpresserModel expresserModel) {
        final JButton colorChooser = new JButton(colorPaletteIcon);
        ActionListener createPopupMenu = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (!MiGenConfiguration.isRequireOnlyTotalTileCount()) {
                    JPopupMenu popup = new ColorChooserPopupMenu(handler, expresserModel);
                    popup.show(colorChooser, 0, 0);
                }
            }
        };
        colorChooser.addActionListener(createPopupMenu);
        return colorChooser;
    }
}
