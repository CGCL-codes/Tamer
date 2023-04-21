package fr.kirin.logger.filter;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComponent;
import javax.swing.JToggleButton;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import fr.kirin.logger.i18n.I18N;
import fr.kirin.logger.model.Level;
import fr.kirin.logger.model.DefaultLoggerTreeModel.InfoBean;
import fr.kirin.logger.model.gui.IconFactory;
import fr.kirin.logger.model.gui.TreeDisplayer;

/**
 * 
 *Factory for filter creation. 
 * @author kirin
 *
 */
public class FilterFactory {

    private FilterFactory() {
    }

    /**
	 * Create a filter based on the node level.
	 * @param level the level to filter, i.e. the node with level won't be displayed
	 * @return the tree filter.
	 */
    public static TreeFilter createLevelFiler(final Level level) {
        return new TreeFilter() {

            public boolean accept(TreeNode node) {
                InfoBean bean = (InfoBean) ((DefaultMutableTreeNode) node).getUserObject();
                if (bean.getLevel() == level) return false;
                return true;
            }
        };
    }

    /**factory method for leve filter GUI creation.
	 * Create a filterGUI with a level Tree filter.
	 * this method create a FilterGUi with an icon.
	 * 
	 * @param level the level to filter.
	 * @param iconPath the icon for the button
	 * @param toolTip tooltip for the button.
	 * @return a new FilterGUI.
	 */
    public static FilterGUI createLevelFilterGUI(final Level level, final String iconPath, final String toolTip) {
        return new AbstractFilterGUI(createLevelFiler(level)) {

            @Override
            public JComponent getGUI() {
                final JToggleButton filter = new JToggleButton(IconFactory.loadIcon(iconPath));
                Dimension buttonsSize = new Dimension(TreeDisplayer.MAX_WIDTH, TreeDisplayer.MAX_HEIGHT);
                filter.setPreferredSize(buttonsSize);
                filter.setMaximumSize(buttonsSize);
                filter.setToolTipText(I18N.getMessage(toolTip));
                filter.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        if (filter.isSelected()) {
                            model.addFilter(getFilter());
                        } else {
                            model.removeFilter(getFilter());
                        }
                    }
                });
                return filter;
            }
        };
    }
}
