package org.mitre.interactive.refimpl.gui.view;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlException;
import org.mitre.interactive.refimpl.gui.InteractiveRefImplApp;
import org.mitre.interactive.x02.ItemBaseType;
import org.mitre.interactive.x02.QuestionTestActionType;
import org.mitre.interactive.x02.QuestionnaireDocument.Questionnaire;
import org.mitre.interactive.refimpl.gui.common.InteractiveDocumentException;
import org.mitre.interactive.refimpl.gui.common.InteractiveDocumentManager;
import org.mitre.interactive.refimpl.gui.common.InteractiveDocumentUtilities;
import org.mitre.interactive.refimpl.gui.common.IInteractiveGUIEventListener;
import org.mitre.interactive.refimpl.gui.common.InteractiveGUIEvent;
import org.mitre.interactive.refimpl.gui.components.InteractiveTreePane;

/**
 * InteractiveDocumentView serves as the main panel and routes events to
 * the appropriate views.
 * 
 * @author  mcasipe
 */
public class InteractiveDocumentView extends javax.swing.JPanel implements IInteractiveGUIEventListener {

    private static Logger logger = Logger.getLogger(InteractiveDocumentView.class.getPackage().getName());

    private InteractiveTreePane treePane = null;

    private boolean isShowNavigation = true;

    /**
     * InteractiveDocumentView constructor.
     * 
     */
    public InteractiveDocumentView() {
        initComponents();
    }

    public void isShowNavigation(boolean show) {
        isShowNavigation = show;
    }

    /**
     * This method saves all results to the specified file.
     * 
     * @param file
     * @throws org.mitre.interactive.refimpl.gui.common.InteractiveDocumentException
     * @throws java.io.IOException
     */
    public void save(File file) throws InteractiveDocumentException, IOException {
        logger.info(InteractiveDocumentUtilities.printDocument(InteractiveDocumentManager.getInteractiveDocument()));
        InteractiveDocumentUtilities.saveDocument(InteractiveDocumentManager.getInteractiveDocument(), file);
    }

    /**
     * This method updates all views in the view with the interactive document
     * in the given file.
     * 
     * @param f
     * @throws org.apache.xmlbeans.XmlException
     * @throws java.io.IOException
     * @throws org.mitre.interactive.refimpl.gui.common.InteractiveDocumentException
     */
    public void update(File f) throws XmlException, IOException, InteractiveDocumentException {
        leftPanel.removeAll();
        rightPanel.removeAll();
        boolean success = InteractiveDocumentManager.updateInstance(f);
        if (!success) {
            if (InteractiveRefImplApp.getTargetQuestionnaireId() != null) JOptionPane.showMessageDialog(this, "Invalid Questionnaire id: " + InteractiveRefImplApp.getTargetQuestionnaireId()); else JOptionPane.showMessageDialog(this, "Error while loading file: " + f.getName());
            return;
        }
        treePane = new InteractiveTreePane();
        treePane.buildTree();
        treePane.addInteractiveEventListener(this);
        leftPanel.add(treePane);
        InteractiveView interactiveView = new InteractiveView();
        interactiveView.buildView();
        interactiveView.addInteractiveEventListener(this);
        rightPanel.add(interactiveView, "1");
        mainSplitPane.setDividerLocation(200);
        treePane.setVisible(isShowNavigation);
        leftPanel.setVisible(isShowNavigation);
        logger.info("Show Navigation? " + isShowNavigation);
    }

    /**
     * This method sets up the questionnaire view when a start questionnaire
     * event is passed.
     * 
     * @param event
     */
    public void startQuestionnaire(InteractiveGUIEvent event) {
        Object source = event.getSource();
        if (source instanceof InteractiveTreePane) rightPanel.remove(rightPanel.getComponents()[0]); else rightPanel.remove((JPanel) source);
        String id = event.getUserObjectId();
        Questionnaire qn = InteractiveDocumentManager.getQuestionnaireById(id);
        if (qn == null) {
            logger.debug("InteractiveDocumentView, startQuestionnaire(): Invalid questionnaire id - " + qn.getId());
            return;
        }
        QuestionnaireView qnView = new QuestionnaireView();
        qnView.buildView(source, qn);
        qnView.addInteractiveEventListener(this);
        rightPanel.add(qnView, "1");
        rightPanel.revalidate();
        treePane.select(qn);
    }

    public void startQuestionTestAction(InteractiveGUIEvent event) {
        Object source = event.getSource();
        if (source instanceof InteractiveTreePane) rightPanel.remove(rightPanel.getComponents()[0]); else rightPanel.remove((JPanel) source);
        String id = event.getUserObjectId();
        ItemBaseType ibt = InteractiveDocumentManager.getTestActionById(id);
        if (ibt instanceof QuestionTestActionType) {
            QuestionTestActionType qta = (QuestionTestActionType) ibt;
            QuestionTestActionView taView = new QuestionTestActionView();
            taView.buildView(source, qta);
            taView.addInteractiveEventListener(this);
            rightPanel.add(taView, "1");
            rightPanel.revalidate();
            treePane.select(qta);
        } else {
            logger.debug("InteractiveDocumentView, startQuestionnaire(): Invalid question test action - " + id);
        }
    }

    public void startInteractive(InteractiveGUIEvent event) {
        Object source = event.getSource();
        if (source instanceof InteractiveTreePane) rightPanel.remove(rightPanel.getComponents()[0]); else rightPanel.remove((JPanel) source);
        InteractiveView interView = new InteractiveView();
        interView.buildView();
        interView.addInteractiveEventListener(this);
        rightPanel.add(interView, "1");
        rightPanel.revalidate();
        treePane.selectRoot();
    }

    public void goToPreviousPage(InteractiveGUIEvent event) {
        logger.info("Go to Previous TreeNode sibling...");
        Object obj = event.getSource();
        if (obj == null || !(obj instanceof IView)) {
            logger.debug("goToPreviousPage(): " + obj.toString());
            return;
        }
        if (isNormalPath((IView) obj)) {
            logger.info("goToPreviousPage(): Normal Path");
            treePane.goToPreviousTreeNode();
        } else {
            if (obj instanceof InteractiveTreePane) rightPanel.remove(rightPanel.getComponents()[0]); else rightPanel.remove((JPanel) obj);
            Object source = ((IView) obj).getSource();
            ((IView) source).rebuild();
            logger.info("goToPreviousPage(): NOT Normal Path: " + source.getClass().getName());
            rightPanel.add((JPanel) source, "1");
            rightPanel.revalidate();
        }
    }

    public void goToNextPage(InteractiveGUIEvent event) {
        logger.info("Go to Next TreeNode sibling...");
        Object obj = event.getSource();
        logger.debug("goToNextPage()");
        if (obj == null || !(obj instanceof IView)) {
            logger.debug("goToNextPage(): " + obj.toString());
            return;
        }
        if (isNormalPath((IView) obj)) {
            logger.info("goToNextPage(): Normal Path");
            treePane.goToNextTreeNode();
        } else {
            logger.info("goToNextPage(): NOT Normal Path");
            if (obj instanceof InteractiveTreePane) rightPanel.remove(rightPanel.getComponents()[0]); else rightPanel.remove((JPanel) obj);
            IView current = (IView) obj;
            while (current != null) {
                Object source = current.getSource();
                if (source instanceof QuestionnaireView) {
                    logger.info("Source is quesionnaire view!!!");
                    boolean hasnext = ((QuestionnaireView) source).goToNextQTA();
                    if (hasnext) {
                        logger.info("Quesionnaire View has next.");
                        return;
                    }
                    current = (IView) source;
                    logger.info("Questionnaire View has no next.");
                } else if (source instanceof QuestionTestActionView) current = (IView) source; else break;
            }
            if (current == null) current = getTheRootQuestionTestActionView((IView) obj);
            ((IView) current).rebuild();
            logger.info("goToNextPage(): " + current.getClass().getName());
            rightPanel.add((JPanel) current, "1");
            rightPanel.revalidate();
        }
    }

    /**
     * This returns the view that started to deviate from the interactive tree.
     * 
     * @param view
     * @return
     */
    private IView getTheRootQuestionTestActionView(IView view) {
        List<IView> viewArray = new ArrayList<IView>();
        IView current = view;
        IView before = null;
        while (current != null) {
            Object obj = current.getSource();
            if (obj instanceof IView) {
                before = (IView) obj;
                viewArray.add(0, before);
                current = before;
            } else break;
        }
        for (IView v : viewArray) {
            if (v instanceof QuestionTestActionView) return v;
        }
        return null;
    }

    private boolean isNormalPath(IView view) {
        boolean isNormal = true;
        IView current = view;
        IView before = null;
        while (current != null) {
            Object obj = current.getSource();
            if (obj instanceof IView) {
                before = (IView) obj;
                if (current instanceof QuestionnaireView && before instanceof QuestionTestActionView) {
                    isNormal = false;
                    break;
                }
                current = before;
            } else break;
        }
        return isNormal;
    }

    private void initComponents() {
        mainSplitPane = new javax.swing.JSplitPane();
        leftPanel = new javax.swing.JPanel();
        rightPanel = new javax.swing.JPanel();
        setMinimumSize(new java.awt.Dimension(700, 500));
        setName("Form");
        mainSplitPane.setDividerLocation(375);
        mainSplitPane.setName("mainSplitPane");
        mainSplitPane.setPreferredSize(new java.awt.Dimension(500, 500));
        leftPanel.setName("leftPanel");
        leftPanel.setPreferredSize(new java.awt.Dimension(500, 500));
        leftPanel.setLayout(new javax.swing.BoxLayout(leftPanel, javax.swing.BoxLayout.LINE_AXIS));
        mainSplitPane.setLeftComponent(leftPanel);
        rightPanel.setName("rightPanel");
        rightPanel.setLayout(new java.awt.CardLayout());
        mainSplitPane.setRightComponent(rightPanel);
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(mainSplitPane, javax.swing.GroupLayout.DEFAULT_SIZE, 700, Short.MAX_VALUE));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(mainSplitPane, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE));
    }

    private javax.swing.JPanel leftPanel;

    private javax.swing.JSplitPane mainSplitPane;

    private javax.swing.JPanel rightPanel;
}
