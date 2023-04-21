package be.lassi.ui.sheet;

import static be.lassi.util.Util.newArrayList;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import be.lassi.base.Listener;
import be.lassi.context.ShowContext;
import be.lassi.context.ShowContextListener;
import be.lassi.domain.Groups;

/**
 * Sheet window tabs that can be used to select channel groups. The CTRL and
 * SHIFT keys can be used to select multiple tabs. If no group is selected, then
 * the first tab is selected.
 */
public class SheetGroupTabs {

    private static final String TIP = "Select channel group, use CTRL or SHIFT key to select multiple groups";

    private JTabbedPane tabbedPane;

    /**
     * The tabbePane child panels that sit between the tabbePane
     * and the sheet table panel.
     */
    private final List<JPanel> childPanels = newArrayList();

    /**
     * The main component that is always shown in each of the
     * child panels.
     */
    private final JComponent component;

    private final ShowContext context;

    private boolean shiftDown;

    private boolean controlDown;

    private final GroupsListener groupsListener = new GroupsListener();

    public SheetGroupTabs(final ShowContext context, final JComponent component) {
        this.context = context;
        this.component = component;
    }

    public JComponent build() {
        buildTabbedPane();
        addChildPanelsIfRequired();
        addTabsIfRequired();
        initChangeListener();
        insertMouseListener();
        initShowContextListener();
        return tabbedPane;
    }

    private void initChangeListener() {
        tabbedPane.addChangeListener(new TabbedPaneChangeListener());
    }

    private void initShowContextListener() {
        ShowContextListener listener = new MyShowContextListener();
        context.addShowContextListener(listener);
        listener.postShowChange();
    }

    private void insertMouseListener() {
        MouseListener[] orignalListeners = tabbedPane.getMouseListeners();
        removeListeners(orignalListeners);
        tabbedPane.addMouseListener(new SelectionListener());
        addListeners(orignalListeners);
    }

    private void removeListeners(final MouseListener[] listeners) {
        for (MouseListener listener : listeners) {
            tabbedPane.removeMouseListener(listener);
        }
    }

    private void addListeners(final MouseListener[] listeners) {
        for (MouseListener listener : listeners) {
            tabbedPane.addMouseListener(listener);
        }
    }

    private void buildTabbedPane() {
        tabbedPane = new JTabbedPane(SwingConstants.BOTTOM, JTabbedPane.WRAP_TAB_LAYOUT);
        tabbedPane.setToolTipText(TIP);
        tabbedPane.setBorder(null);
        tabbedPane.putClientProperty("Quaqua.TabbedPane.contentBorderPainted", Boolean.FALSE);
        tabbedPane.putClientProperty("Quaqua.TabbedPane.shortenTabs", Boolean.FALSE);
        tabbedPane.putClientProperty("Quaqua.Component.visualMargin", new Insets(0, -3, 0, -3));
    }

    private class SelectionListener extends MouseAdapter {

        @Override
        public void mousePressed(final MouseEvent e) {
            shiftDown = e.isShiftDown();
            controlDown = e.isControlDown();
        }
    }

    private Groups getGroups() {
        return context.getShow().getGroups();
    }

    private void addChildPanelsIfRequired() {
        while (childPanels.size() < getGroups().size() + 1) {
            JPanel panel = new JPanel(new BorderLayout());
            panel.putClientProperty("Quaqua.TabbedPaneChild.contentInsets", new Insets(0, 0, 0, 0));
            childPanels.add(panel);
        }
    }

    private void addTabsIfRequired() {
        while (tabbedPane.getTabCount() < getGroups().size() + 1) {
            JPanel childPanel = childPanels.get(tabbedPane.getTabCount());
            tabbedPane.addTab("", childPanel);
            tabbedPane.setToolTipTextAt(tabbedPane.getTabCount() - 1, TIP);
        }
    }

    private void removeExcessTabs() {
        while (tabbedPane.getTabCount() > getGroups().size() + 1) {
            tabbedPane.removeTabAt(tabbedPane.getTabCount() - 1);
        }
    }

    /**
     * Adapts the tabs to changing group definitions.
     */
    private class GroupsListener implements Listener {

        public void changed() {
            removeExcessTabs();
            addChildPanelsIfRequired();
            addTabsIfRequired();
            tabbedPane.setTitleAt(0, "ALL");
            Color color = (!getGroups().isEnabled()) ? Color.RED : tabbedPane.getForeground();
            tabbedPane.setForegroundAt(0, color);
            for (int i = 0; i < getGroups().size(); i++) {
                tabbedPane.setTitleAt(i + 1, getGroups().get(i).getName());
                color = getGroups().get(i).isEnabled() ? Color.RED : tabbedPane.getForeground();
                tabbedPane.setForegroundAt(i + 1, color);
            }
        }
    }

    private class MyShowContextListener implements ShowContextListener {

        public void postShowChange() {
            getGroups().getListeners().add(groupsListener);
            groupsListener.changed();
        }

        public void preShowChange() {
            getGroups().getListeners().remove(groupsListener);
        }
    }

    private class TabbedPaneChangeListener implements ChangeListener {

        private int previousSelectedIndex = -1;

        private TabbedPaneChangeListener() {
            JPanel child = childPanels.get(0);
            child.add(component, BorderLayout.CENTER);
        }

        public void stateChanged(final ChangeEvent e) {
            int index = tabbedPane.getSelectedIndex();
            JPanel child = childPanels.get(index);
            child.add(component, BorderLayout.CENTER);
            if (index == 0) {
                getGroups().setAllEnabled(false);
            } else if (index > 0) {
                if (shiftDown) {
                    selectRange(index);
                } else if (controlDown) {
                    selectExra(index);
                } else {
                    selectSingle(index);
                }
            }
            previousSelectedIndex = index;
        }

        private void selectSingle(final int index) {
            int groupIndex = index - 1;
            for (int i = 0; i < getGroups().size(); i++) {
                boolean enabled = groupIndex == i;
                if (getGroups().isEnabled(i) != enabled) {
                    getGroups().setEnabled(i, groupIndex == i);
                }
            }
        }

        private void selectExra(final int index) {
            int groupIndex = index - 1;
            boolean enabled = getGroups().isEnabled(groupIndex);
            getGroups().setEnabled(groupIndex, !enabled);
        }

        private void selectRange(final int index) {
            if (previousSelectedIndex == -1) {
                getGroups().setEnabled(index + 1, true);
            } else {
                if (previousSelectedIndex < index) {
                    for (int i = previousSelectedIndex + 1; i <= index; i++) {
                        if (i > 0) {
                            getGroups().setEnabled(i - 1, true);
                        }
                    }
                } else {
                    for (int i = previousSelectedIndex; i >= index; i--) {
                        if (i > 0) {
                            getGroups().setEnabled(i - 1, true);
                        }
                    }
                }
            }
        }
    }
}
