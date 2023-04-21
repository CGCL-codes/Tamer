package ogv.gui.managers;

import java.awt.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import ogv.OGV;
import ogv.gui.views.View;
import ogv.util.SwingUtils;

public class Manager4 extends AbstractWindowManager implements ChangeListener {

    private final JTabbedPane tabbedPane = new JTabbedPane();

    @Override
    public void updateAll() {
        super.updateAll();
        for (int i = 0; i < tabbedPane.getTabCount(); ++i) {
            Component comp = tabbedPane.getComponentAt(i);
            if (comp instanceof View) {
                View view = (View) comp;
                tabbedPane.setTitleAt(i, (OGV.getViews().isFreezed(view) ? "+" : "") + view.getTitle());
            }
        }
    }

    @Override
    public void updateTitle(View view) {
        Component comp = (Component) view;
        int i = tabbedPane.indexOfComponent(comp);
        if (i >= 0) tabbedPane.setTitleAt(i, (OGV.getViews().isFreezed(view) ? "+" : "") + view.getTitle());
    }

    @Override
    public void init() {
        initMainFrame();
        frame.add(tabbedPane);
        tabbedPane.addChangeListener(this);
    }

    @Override
    public void setCurrentPanel(View view) {
        menu.updateLocalMenu(view);
        Component comp = (Component) view;
        int i = tabbedPane.indexOfComponent(comp);
        if (i < 0) {
            tabbedPane.addTab(view.getTitle(), SwingUtils.getIcon(view.getIconName(), SwingUtils.BUTTON_ICON_SIZE), comp);
            i = tabbedPane.getComponentCount() - 1;
        } else tabbedPane.setTitleAt(i, view.getTitle());
        tabbedPane.setSelectedIndex(i);
        frame.setTitle(getTitle(null));
        super.setCurrentPanel(view);
    }

    @Override
    public void restoreLastPanel() {
        View view1 = dropView();
        View view2 = getCurrentView();
        tabbedPane.remove((Component) view1);
        setCurrentPanel(view2);
    }

    @Override
    public void closeAll() {
        tabbedPane.removeAll();
        super.closeAll();
    }

    @Override
    public void removeAll() {
        tabbedPane.removeAll();
        super.removeAll();
    }

    @Override
    public void updateUI() {
        super.updateUI();
        SwingUtilities.updateComponentTreeUI(frame);
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        Component comp = tabbedPane.getSelectedComponent();
        if (comp instanceof View) {
            View view = (View) comp;
            menu.updateLocalMenu(view);
            selectView(view);
        }
    }
}
