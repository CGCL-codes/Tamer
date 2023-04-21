package ch.intertec.storybook.view.manage;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import net.infonode.docking.View;
import net.miginfocom.swing.MigLayout;
import org.hibernate.Session;
import ch.intertec.storybook.SbConstants;
import ch.intertec.storybook.SbConstants.InternalKey;
import ch.intertec.storybook.SbConstants.ViewName;
import ch.intertec.storybook.controller.DocumentController;
import ch.intertec.storybook.model.DocumentModel;
import ch.intertec.storybook.model.hbn.dao.ChapterDAOImpl;
import ch.intertec.storybook.model.hbn.entity.Chapter;
import ch.intertec.storybook.model.hbn.entity.Internal;
import ch.intertec.storybook.model.hbn.entity.Part;
import ch.intertec.storybook.model.hbn.entity.Scene;
import ch.intertec.storybook.toolkit.EntityUtil;
import ch.intertec.storybook.toolkit.I18N;
import ch.intertec.storybook.toolkit.ViewUtil;
import ch.intertec.storybook.toolkit.swing.SwingUtil;
import ch.intertec.storybook.view.AbstractPanel;
import ch.intertec.storybook.view.MainFrame;
import ch.intertec.storybook.view.options.ManageOptionsDialog;

/**
 * @author martin
 * 
 */
@SuppressWarnings("serial")
public class ManagePanel extends AbstractPanel {

    private JScrollPane scroller;

    private JPanel panel;

    private int cols;

    public ManagePanel(MainFrame mainFrame) {
        super(mainFrame);
    }

    @Override
    public void modelPropertyChange(PropertyChangeEvent evt) {
        String propName = evt.getPropertyName();
        Object newValue = evt.getNewValue();
        if (DocumentController.CommonProps.REFRESH.check(propName)) {
            View newView = (View) evt.getNewValue();
            View view = (View) getParent().getParent();
            if (view == newView) {
                init();
                refresh();
            }
            return;
        }
        if (DocumentController.CommonProps.SHOW_OPTIONS.check(propName)) {
            View view = (View) evt.getNewValue();
            if (!view.getName().equals(ViewName.MANAGE.toString())) {
                return;
            }
            ManageOptionsDialog dlg = new ManageOptionsDialog(mainFrame);
            SwingUtil.showModalDialog(dlg, this);
            return;
        }
        if (DocumentController.ManageViewProps.SHOW_ENTITY.check(propName)) {
            if (newValue instanceof Scene) {
                Scene scene = (Scene) newValue;
                ViewUtil.scrollToScene(this, panel, scene);
                return;
            }
            if (newValue instanceof Chapter) {
                Chapter chapter = (Chapter) newValue;
                ViewUtil.scrollToChapter(this, panel, chapter);
                return;
            }
        }
        if (DocumentController.ManageViewProps.COLUMNS.check(propName)) {
            init();
            refresh();
            return;
        }
        if (DocumentController.PartProps.CHANGE.check(propName)) {
            refresh();
            SwingUtil.scrollToTop(scroller);
            return;
        }
        dispatchToChapterPanels(this, evt);
    }

    @Override
    public void init() {
        try {
            Internal internal = EntityUtil.restoreInternal(mainFrame, InternalKey.MANAGE_COLUMNS, SbConstants.DEFAULT_MANAGE_COLUMNS);
            cols = internal.getIntegerValue();
        } catch (Exception e) {
            e.printStackTrace();
            cols = SbConstants.DEFAULT_MANAGE_COLUMNS;
        }
    }

    @Override
    public void initUi() {
        setLayout(new MigLayout("flowy,fill,ins 0"));
        panel = new JPanel();
        panel.setBackground(SwingUtil.getBackgroundColor());
        scroller = new JScrollPane(panel);
        scroller.getVerticalScrollBar().setUnitIncrement(20);
        SwingUtil.setMaxPreferredSize(scroller);
        refresh();
    }

    @Override
    public void refresh() {
        Part currentPart = mainFrame.getCurrentPart();
        DocumentModel model = mainFrame.getDocumentModel();
        Session session = model.beginTransaction();
        ChapterDAOImpl dao = new ChapterDAOImpl(session);
        List<Chapter> chapters = dao.findAll(currentPart);
        model.commit();
        removeAll();
        JScrollPane scrollerUnassigend = new JScrollPane(new ChapterPanel(mainFrame));
        scrollerUnassigend.setMinimumSize(new Dimension(200, 180));
        add(scrollerUnassigend, "growx");
        add(scroller, "grow");
        MigLayout layout = new MigLayout("wrap " + cols, "", "[top]");
        panel.setLayout(layout);
        panel.removeAll();
        for (Chapter chapter : chapters) {
            panel.add(new ChapterPanel(mainFrame, chapter), "grow");
        }
        if (panel.getComponentCount() == 0) {
            panel.add(new JLabel(I18N.getMsg("msg.warning.no.chapters")));
        }
        revalidate();
        repaint();
    }

    private static void dispatchToChapterPanels(Container cont, PropertyChangeEvent evt) {
        List<Component> ret = new ArrayList<Component>();
        SwingUtil.findComponentsByClass(cont, ChapterPanel.class, ret);
        for (Component comp : ret) {
            ChapterPanel panel = (ChapterPanel) comp;
            panel.modelPropertyChange(evt);
        }
    }
}
