package net.sf.rmoffice.ui.renderer;

import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JSpinner.NumberEditor;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.TableCellRenderer;
import net.sf.rmoffice.core.items.MagicalFeature;
import net.sf.rmoffice.meta.ISkill;
import net.sf.rmoffice.meta.SkillCategory;
import net.sf.rmoffice.ui.UIConstants;
import com.jidesoft.swing.DefaultOverlayable;

/**
 * Editor for {@link SkillCategory}, {@link ISkill}s and {@link MagicalFeature#getBonus()}
 */
public class NumberSpinnerTableRenderer extends JLabel implements TableCellRenderer {

    private static final long serialVersionUID = 1L;

    private static final JLabel OVERLAY1 = new JLabel(new ImageIcon(NumberSpinnerTableRenderer.class.getResource("/images/icons/overlay_1.png")));

    private static final JLabel OVERLAY2 = new JLabel(new ImageIcon(NumberSpinnerTableRenderer.class.getResource("/images/icons/overlay_2.png")));

    private static final JLabel OVERLAY3 = new JLabel(new ImageIcon(NumberSpinnerTableRenderer.class.getResource("/images/icons/overlay_3.png")));

    private final OverlayableSpinner spinner;

    private final DefaultOverlayable overlayable;

    private final boolean alwaysShow;

    /**
	 * Creates a new cell editor instance where the spinner component is shown only
	 * if the underlying object need this. Example: an non-editable skill category does not
	 * need a jspinner.
	 */
    public NumberSpinnerTableRenderer() {
        this(false);
    }

    /**
	 * Creates new cell editor instance.
	 * 
	 * @param alwaysShow whether the spinner component should shown always or not
	 */
    public NumberSpinnerTableRenderer(boolean alwaysShow) {
        this.alwaysShow = alwaysShow;
        spinner = new OverlayableSpinner(new SpinnerNumberModel(0d, 0d, 100d, 0.5d));
        spinner.setBorder(null);
        overlayable = new DefaultOverlayable(spinner);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Object val = table.getValueAt(row, 0);
        if (val instanceof SkillCategory) {
            SkillCategory gr = (SkillCategory) val;
            if (!gr.getRankType().isGroupRankEditable() && !alwaysShow) {
                return getEmptyLabel(table, isSelected);
            }
        }
        if (value instanceof MagicalFeature) {
            MagicalFeature feature = (MagicalFeature) value;
            if (feature.getType().isBonusAvailable()) {
                value = feature.getBonus();
            } else {
                return getEmptyLabel(table, isSelected);
            }
        }
        if (isSelected) {
            spinner.setBackground(table.getSelectionBackground());
            ((NumberEditor) spinner.getEditor()).getTextField().setBackground(table.getSelectionBackground());
            setOpaque(true);
        } else {
            spinner.setBackground(table.getBackground());
            ((NumberEditor) spinner.getEditor()).getTextField().setBackground(UIConstants.COLOR_EDITABLE_BG);
            setOpaque(false);
        }
        if (value == null) {
            spinner.setValue(Double.valueOf(0));
        } else {
            spinner.setValue(value);
        }
        for (JComponent comp : overlayable.getOverlayComponents()) {
            overlayable.removeOverlayComponent(comp);
        }
        if (table.getModel() instanceof IOverlaySupportable) {
            IOverlaySupportable overlaySupport = (IOverlaySupportable) table.getModel();
            int actualRow = table.convertRowIndexToModel(row);
            int actualCol = table.convertColumnIndexToModel(column);
            switch(overlaySupport.getLevelUpSteps(actualRow, actualCol)) {
                case 1:
                    overlayable.addOverlayComponent(OVERLAY1, DefaultOverlayable.WEST);
                    break;
                case 2:
                    overlayable.addOverlayComponent(OVERLAY2, DefaultOverlayable.WEST);
                    break;
                case 3:
                    overlayable.addOverlayComponent(OVERLAY3, DefaultOverlayable.WEST);
                    break;
            }
        }
        return overlayable;
    }

    /**
	 * 
	 * @param table
	 * @param isSelected
	 * @return
	 */
    private Component getEmptyLabel(JTable table, boolean isSelected) {
        if (isSelected) {
            setBackground(table.getSelectionBackground());
            setOpaque(true);
        } else {
            setBackground(table.getBackground());
            setOpaque(false);
        }
        return this;
    }
}
