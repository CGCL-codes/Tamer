package org.vikamine.swing.subgroup.editors.zoomtable;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import org.vikamine.app.Resources;
import org.vikamine.swing.subgroup.AllSubgroupPluginController;

/**
 * @author Tobias Vogele
 */
public class SortAttributesAction extends AbstractAction {

    /**
     * 
     */
    private static final long serialVersionUID = -6360926901071430427L;

    static class InstancesSortOrderDialogAction extends AbstractAction {

        private static final long serialVersionUID = 6802624257147834015L;

        public InstancesSortOrderDialogAction() {
            super(Resources.I18N.getString("vikamine.zoomtable.actions.instancesSortOrderDialog"));
        }

        public void actionPerformed(ActionEvent e) {
            new SortOrderDialogBuilder().showDialog();
        }
    }

    abstract class SetSwitchAction extends AbstractAction {

        /** generated. */
        private static final long serialVersionUID = 39669124701222578L;

        SetSwitchAction(String nameKeyPart) {
            super(Resources.I18N.getString("vikamine.zoomtable.actions.sort." + nameKeyPart));
        }

        abstract boolean isSet();
    }

    public SortAttributesAction() {
        putValue(SMALL_ICON, new ImageIcon(getClass().getResource("/resources/images/sort.gif")));
        putValue(SHORT_DESCRIPTION, Resources.I18N.getString("vikamine.zoomtable.actions.sortAttributes"));
    }

    public void actionPerformed(ActionEvent e) {
        JPopupMenu menu = createPopupMenu();
        AbstractButton button = (AbstractButton) e.getSource();
        menu.show(button, button.getX(), button.getY() + button.getHeight());
    }

    private JPopupMenu createPopupMenu() {
        JPopupMenu menu = new JPopupMenu();
        menu.add(createMenuItem(new SetSwitchAction("alphabetical") {

            private static final long serialVersionUID = -3824865618355433287L;

            @Override
            boolean isSet() {
                return getModel().getSorting() == CommonZoomTreesModel.SORT_ALPHABETICAL;
            }

            public void actionPerformed(ActionEvent e) {
                getModel().setSorting(CommonZoomTreesModel.SORT_ALPHABETICAL);
                getModel().resort();
            }
        }));
        menu.add(createMenuItem(new SetSwitchAction("best.alphabetical") {

            private static final long serialVersionUID = -945385186416611351L;

            @Override
            boolean isSet() {
                return getModel().getSorting() == CommonZoomTreesModel.SORT_BEST_ATTRIBUTES_ALPHABETICAL;
            }

            public void actionPerformed(ActionEvent e) {
                getModel().setSorting(CommonZoomTreesModel.SORT_BEST_ATTRIBUTES_ALPHABETICAL);
                getModel().resort();
            }
        }));
        JRadioButtonMenuItem it = new JRadioButtonMenuItem(new InstancesSortOrderDialogAction());
        it.setSelected(getModel().getSorting() == CommonZoomTreesModel.SORT_MANUAL);
        menu.add(it);
        return menu;
    }

    private JMenuItem createMenuItem(SetSwitchAction action) {
        JRadioButtonMenuItem item = new JRadioButtonMenuItem(action);
        item.setSelected(action.isSet());
        return item;
    }

    public CommonZoomTreesModel getModel() {
        return AllSubgroupPluginController.getInstance().getZoomController().getActiveZoomTreeModel();
    }
}
