package ch.intertec.storybook.toolkit;

import java.awt.Component;
import java.awt.Container;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import org.hibernate.Session;
import ch.intertec.storybook.action.ScrollToEntityAction;
import ch.intertec.storybook.model.DocumentModel;
import ch.intertec.storybook.model.hbn.dao.ChapterDAOImpl;
import ch.intertec.storybook.model.hbn.entity.Chapter;
import ch.intertec.storybook.model.hbn.entity.Part;
import ch.intertec.storybook.model.hbn.entity.Scene;
import ch.intertec.storybook.model.hbn.entity.Strand;
import ch.intertec.storybook.toolkit.swing.SwingUtil;
import ch.intertec.storybook.view.AbstractPanel;
import ch.intertec.storybook.view.AbstractScenePanel;
import ch.intertec.storybook.view.MainFrame;
import ch.intertec.storybook.view.book.BookPanel;
import ch.intertec.storybook.view.book.BookScenePanel;
import ch.intertec.storybook.view.chrono.ChronoPanel;
import ch.intertec.storybook.view.chrono.ChronoScenePanel;
import ch.intertec.storybook.view.chrono.StrandDateLabel;
import ch.intertec.storybook.view.manage.ChapterPanel;
import ch.intertec.storybook.view.manage.ManagePanel;
import ch.intertec.storybook.view.manage.dnd.ScenePanel;

/**
 * Provides tools around the views.
 * 
 * @author martin
 * 
 */
public class ViewUtil {

    public static boolean scrollToStrandDate(AbstractPanel container, JPanel panel, Strand strand, Date date) {
        if (strand == null || date == null) {
            return false;
        }
        return doScrolling(container, panel, strand, date);
    }

    public static boolean scrollToChapter(AbstractPanel container, JPanel panel, Chapter chapter) {
        if (container instanceof ManagePanel) {
            if (chapter == null) {
                return false;
            }
            boolean partChanged = changeCurrentPart(container, panel, chapter.getPart());
            ScrollToEntityAction action = new ScrollToEntityAction(container, panel, chapter);
            int delay = 1;
            if (partChanged) {
                delay = 200;
            }
            Timer timer = new Timer(delay, action);
            timer.setRepeats(false);
            timer.start();
            return action.isFound();
        }
        MainFrame mainFrame = container.getMainFrame();
        DocumentModel model = mainFrame.getDocumentModel();
        Session session = model.beginTransaction();
        ChapterDAOImpl dao = new ChapterDAOImpl(session);
        Scene scene = dao.findFirstScene(chapter);
        model.commit();
        return scrollToScene(container, panel, scene);
    }

    public static boolean scrollToScene(AbstractPanel container, JPanel panel, Scene scene) {
        if (scene == null) {
            return false;
        }
        boolean partChanged = changeCurrentPart(container, panel, scene);
        ScrollToEntityAction action = new ScrollToEntityAction(container, panel, scene);
        int delay = 1;
        if (partChanged) {
            delay = 200;
        }
        Timer timer = new Timer(delay, action);
        timer.setRepeats(false);
        timer.start();
        return action.isFound();
    }

    private static boolean changeCurrentPart(AbstractPanel container, JPanel panel, Scene scene) {
        if (scene == null) {
            return false;
        }
        boolean changed = false;
        if (scene.getChapter() != null) {
            MainFrame mainFrame = container.getMainFrame();
            Part part = scene.getChapter().getPart();
            if (mainFrame.getCurrentPart().getId() != part.getId()) {
                mainFrame.getSbActionManager().getActionHandler().handleChangePart(part);
                changed = true;
            }
        }
        return changed;
    }

    private static boolean changeCurrentPart(AbstractPanel container, JPanel panel, Part part) {
        if (part == null) {
            return false;
        }
        boolean changed = false;
        MainFrame mainFrame = container.getMainFrame();
        if (mainFrame.getCurrentPart().getId() != part.getId()) {
            mainFrame.getSbActionManager().getActionHandler().handleChangePart(part);
            changed = true;
        }
        return changed;
    }

    public static boolean doScrolling(AbstractPanel container, JPanel panel, Scene scene) {
        boolean found = false;
        List<AbstractScenePanel> panels = findScenePanels(container);
        for (AbstractScenePanel scenePanel : panels) {
            Scene sc = scenePanel.getScene();
            if (sc == null) {
                continue;
            }
            if (scene.getId() == sc.getId()) {
                Rectangle rect = scenePanel.getBounds();
                if (scenePanel instanceof ChronoScenePanel) {
                    rect = SwingUtilities.convertRectangle(scenePanel.getParent(), rect, panel);
                }
                SwingUtil.expandRectangle(rect);
                panel.scrollRectToVisible(rect);
                SwingUtil.flashComponent(scenePanel);
                found = true;
                break;
            }
        }
        return found;
    }

    public static boolean doScrolling(AbstractPanel container, JPanel panel, Chapter chapter) {
        boolean found = false;
        List<ChapterPanel> panels = findChapterPanels(container);
        for (ChapterPanel scenePanel : panels) {
            Chapter ch = scenePanel.getChapter();
            if (ch == null) {
                continue;
            }
            if (chapter.getId() == ch.getId()) {
                Rectangle rect = scenePanel.getBounds();
                SwingUtil.expandRectangle(rect);
                panel.scrollRectToVisible(rect);
                SwingUtil.flashComponent(scenePanel);
                found = true;
                break;
            }
        }
        return found;
    }

    public static boolean doScrolling(AbstractPanel container, JPanel panel, Strand strand, Date date) {
        boolean found = false;
        List<StrandDateLabel> panels = findStrandDateLabels(container);
        for (StrandDateLabel sdPanel : panels) {
            Strand s = sdPanel.getStrand();
            Date d = sdPanel.getDate();
            if (s == null || d == null) {
                continue;
            }
            if (strand.getId() == s.getId() && date.compareTo(d) == 0) {
                JComponent comp = null;
                if (container instanceof ChronoPanel) {
                    comp = (JComponent) sdPanel.getParent();
                } else if (container instanceof BookPanel) {
                    comp = (JComponent) sdPanel.getParent().getParent();
                }
                Rectangle rect = comp.getBounds();
                SwingUtil.expandRectangle(rect);
                panel.scrollRectToVisible(rect);
                SwingUtil.flashComponent(comp);
                found = true;
                break;
            }
        }
        return found;
    }

    public static List<AbstractScenePanel> findScenePanels(Container cont) {
        if (cont instanceof ChronoPanel) {
            return findChronoScenePanels(cont);
        }
        if (cont instanceof BookPanel) {
            return findBookScenePanels(cont);
        }
        if (cont instanceof ManagePanel) {
            return findManageScenePanels(cont);
        }
        return new ArrayList<AbstractScenePanel>();
    }

    private static List<AbstractScenePanel> findBookScenePanels(Container cont) {
        List<Component> components = new ArrayList<Component>();
        components = SwingUtil.findComponentsByClass(cont, BookScenePanel.class, components);
        List<AbstractScenePanel> panels = new ArrayList<AbstractScenePanel>();
        for (Component comp : components) {
            panels.add((AbstractScenePanel) comp);
        }
        return panels;
    }

    private static List<AbstractScenePanel> findChronoScenePanels(Container cont) {
        List<Component> components = new ArrayList<Component>();
        components = SwingUtil.findComponentsByClass(cont, ChronoScenePanel.class, components);
        List<AbstractScenePanel> panels = new ArrayList<AbstractScenePanel>();
        for (Component comp : components) {
            panels.add((AbstractScenePanel) comp);
        }
        return panels;
    }

    private static List<AbstractScenePanel> findManageScenePanels(Container cont) {
        List<Component> components = new ArrayList<Component>();
        components = SwingUtil.findComponentsByClass(cont, ScenePanel.class, components);
        List<AbstractScenePanel> panels = new ArrayList<AbstractScenePanel>();
        for (Component comp : components) {
            panels.add((AbstractScenePanel) comp);
        }
        return panels;
    }

    private static List<ChapterPanel> findChapterPanels(Container cont) {
        List<Component> components = new ArrayList<Component>();
        components = SwingUtil.findComponentsByClass(cont, ChapterPanel.class, components);
        List<ChapterPanel> panels = new ArrayList<ChapterPanel>();
        for (Component comp : components) {
            panels.add((ChapterPanel) comp);
        }
        return panels;
    }

    private static List<StrandDateLabel> findStrandDateLabels(Container cont) {
        List<Component> components = new ArrayList<Component>();
        components = SwingUtil.findComponentsByClass(cont, StrandDateLabel.class, components);
        List<StrandDateLabel> labels = new ArrayList<StrandDateLabel>();
        for (Component comp : components) {
            labels.add((StrandDateLabel) comp);
        }
        return labels;
    }
}
