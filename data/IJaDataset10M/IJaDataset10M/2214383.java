package ch.intertec.storybook.view.navigation;

import java.beans.PropertyChangeEvent;
import javax.swing.JTabbedPane;
import net.infonode.docking.View;
import net.miginfocom.swing.MigLayout;
import ch.intertec.storybook.controller.DocumentController;
import ch.intertec.storybook.toolkit.I18N;
import ch.intertec.storybook.view.AbstractPanel;
import ch.intertec.storybook.view.MainFrame;

/**
 * @author martin
 * 
 */
@SuppressWarnings("serial")
public class NavigationPanel extends AbstractPanel {

    private JTabbedPane tabbedPane;

    public NavigationPanel(MainFrame mainFrame) {
        super(mainFrame);
    }

    @Override
    public void modelPropertyChange(PropertyChangeEvent evt) {
        Object newValue = evt.getNewValue();
        String propName = evt.getPropertyName();
        if (DocumentController.CommonProps.REFRESH.check(propName)) {
            View newView = (View) newValue;
            View view = (View) getParent().getParent();
            if (view == newView) {
                refresh();
            }
            return;
        }
        if (propName.startsWith("Edit") || propName.startsWith("Init")) {
            return;
        }
        if (propName.contains("Scene") || propName.contains("Chapter") || propName.contains("Strand")) {
            refresh();
            return;
        }
    }

    @Override
    public void refresh() {
        int index = tabbedPane.getSelectedIndex();
        super.refresh();
        tabbedPane.setSelectedIndex(index);
    }

    @Override
    public void init() {
    }

    @Override
    public void initUi() {
        setLayout(new MigLayout("wrap,fill,ins 0"));
        tabbedPane = new JTabbedPane();
        tabbedPane.addTab(I18N.getMsg("msg.menu.navigate.goto.chapter"), new FindChapterPanel(mainFrame));
        tabbedPane.addTab(I18N.getMsg("msg.menu.navigate.goto.date"), new FindDatePanel(mainFrame));
        add(tabbedPane, "grow");
    }
}
