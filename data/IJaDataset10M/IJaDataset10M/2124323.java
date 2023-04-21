package freestyleLearningGroup.freestyleLearning.learningUnitViewManagers.glossary.elementsContentsPanel;

import java.awt.CardLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JFrame;
import freestyleLearning.learningUnitViewAPI.FSLLearningUnitViewManager;
import freestyleLearning.learningUnitViewAPI.elementsContentsPanel.FSLAbstractLearningUnitViewElementContentPanel;
import freestyleLearning.learningUnitViewAPI.events.learningUnitEvent.FSLLearningUnitEventGenerator;
import freestyleLearning.learningUnitViewAPI.events.learningUnitViewEvent.FSLLearningUnitViewAdapter;
import freestyleLearning.learningUnitViewAPI.events.learningUnitViewEvent.FSLLearningUnitViewEvent;
import freestyleLearningGroup.freestyleLearning.learningUnitViewManagers.glossary.FLGGlossaryManager;
import freestyleLearningGroup.freestyleLearning.learningUnitViewManagers.glossary.data.xmlBindingSubclasses.FLGGlossaryElement;
import freestyleLearningGroup.freestyleLearning.learningUnitViewManagers.glossary.dialog.MemoryStartDialog;
import freestyleLearningGroup.freestyleLearning.learningUnitViewManagers.glossary.event.FLGMemoryEvent;
import freestyleLearningGroup.independent.gui.FLGOptionPane;
import freestyleLearningGroup.independent.util.FLGInternationalization;

public class FLGMemoryContentPanel extends FSLAbstractLearningUnitViewElementContentPanel {

    private MemoryStatisticPanel m_memoryStatisticPanel;

    private MemoryStandardPanel m_memoryStandardPanel;

    private FLGInternationalization m_internationalization;

    private List m_currentSession = new ArrayList();

    private int m_currentIndex;

    private long m_thinkTime;

    private long m_lastStartTime;

    public void init(FSLLearningUnitViewManager learningUnitViewManager, FSLLearningUnitEventGenerator learningUnitEventGenerator, boolean editMode) {
        setLayout(new CardLayout());
        m_internationalization = new FLGInternationalization("freestyleLearningGroup.freestyleLearning.learningUnitViewManagers.glossary.elementsContentsPanel.internationalization", getClass().getClassLoader());
        m_memoryStandardPanel = new MemoryStandardPanel(this);
        m_memoryStatisticPanel = new MemoryStatisticPanel(this);
        super.init(learningUnitViewManager, learningUnitEventGenerator, editMode);
        learningUnitViewManager.addLearningUnitViewListener(new FLGMemoryContentPanel_Adapter());
    }

    public void saveUserChanges() {
        ((FLGGlossaryManager) learningUnitViewManager).setModifiedByMemory(false);
    }

    protected void setActiveLearningUnitViewElementPanel(boolean active) {
        super.setActiveLearningUnitViewElementPanel(active);
    }

    public boolean isModifiedByUserInput() {
        return ((FLGGlossaryManager) learningUnitViewManager).isModifiedByMemory();
    }

    protected void buildIndependentUI() {
        add(m_memoryStandardPanel, "standardPanel");
        add(m_memoryStatisticPanel, "statisticPanel");
        ((CardLayout) getLayout()).show(this, "startPanel");
    }

    protected void buildDependentUI(boolean reloadIfAlreadyLoaded) {
    }

    protected JComponent[] getEditToolBarComponents() {
        return new JComponent[0];
    }

    public long getThinkTime() {
        return m_thinkTime;
    }

    public List getCurrentSession() {
        return m_currentSession;
    }

    protected void start() {
        if (learningUnitViewElementsManager.getAllLearningUnitViewElementIds().length == 0) {
            FLGOptionPane.showMessageDialog(m_internationalization.getString("FLGMemoryContentPanel.noElementsMsg"), m_internationalization.getString("FLGMemoryContentPanel.sessionNotPossibleMsg"), FLGOptionPane.INFORMATION_MESSAGE);
            return;
        }
        MemoryStartDialog dialog = MemoryStartDialog.showStartDialog(new JFrame());
        if (dialog.isCanceled()) {
            return;
        }
        ((FLGGlossaryManager) learningUnitViewManager).setModifiedByMemory(true);
        if (dialog.getStrategy() == MemoryStartDialog.SINCE_STRATEGY) createListByDate(dialog.getNotRepeatedSince()); else createListByCategory(dialog.getCategory());
        Collections.shuffle(m_currentSession);
        if (m_currentSession.size() == 0) {
            FLGOptionPane.showMessageDialog(m_internationalization.getString("FLGMemoryContentPanel.selectionContainsNoElementsMsg"), m_internationalization.getString("FLGMemoryContentPanel.sessionNotPossibleMsg"), FLGOptionPane.INFORMATION_MESSAGE);
            return;
        }
        m_currentIndex = -1;
        if (dialog.isContentFirst()) m_memoryStandardPanel.setShowMode(MemoryStandardPanel.CONTENT_FIRST); else m_memoryStandardPanel.setShowMode(MemoryStandardPanel.TITLE_FIRST);
        next();
    }

    protected void uncover() {
        m_thinkTime += System.currentTimeMillis() - m_lastStartTime;
        m_memoryStandardPanel.uncover();
        learningUnitViewManager.fireLearningUnitViewEvent(new FLGMemoryEvent(FLGMemoryEvent.MEMORY_UNCOVERED, ((FLGGlossaryElement) m_currentSession.get(m_currentIndex)).getId()));
    }

    protected void moveUp() {
        FLGGlossaryElement currentElement = (FLGGlossaryElement) m_currentSession.get(m_currentIndex);
        currentElement.setTries(currentElement.getTries() + 1);
        currentElement.setLastTry(new Date());
        currentElement.setCategory(currentElement.getCategory() + 1);
        next();
    }

    protected void moveNot() {
        FLGGlossaryElement currentElement = (FLGGlossaryElement) m_currentSession.get(m_currentIndex);
        currentElement.setTries(currentElement.getTries() + 1);
        currentElement.setLastTry(new Date());
        next();
    }

    protected void moveDown() {
        FLGGlossaryElement currentElement = (FLGGlossaryElement) m_currentSession.get(m_currentIndex);
        currentElement.setTries(currentElement.getTries() + 1);
        currentElement.setLastTry(new Date());
        currentElement.setCategory(currentElement.getCategory() - 1);
        next();
    }

    protected void showStatistics() {
        m_memoryStatisticPanel.computeStatistic();
        ((CardLayout) getLayout()).show(this, "statisticPanel");
    }

    protected void next() {
        if (m_currentIndex++ == m_currentSession.size() - 1) {
            showStatistics();
            learningUnitViewManager.fireLearningUnitViewEvent(new FLGMemoryEvent(FLGMemoryEvent.MEMORY_SESSION_FINISHED, null));
            return;
        }
        m_lastStartTime = System.currentTimeMillis();
        try {
            m_memoryStandardPanel.setGlossaryElement((FLGGlossaryElement) m_currentSession.get(m_currentIndex));
            learningUnitViewManager.fireLearningUnitViewEvent(new FLGMemoryEvent(FLGMemoryEvent.MEMORY_COVERED, ((FLGGlossaryElement) m_currentSession.get(m_currentIndex)).getId()));
        } catch (Exception e) {
            System.out.println("element cannot be loaded");
            e.printStackTrace();
            next();
        }
    }

    public FSLLearningUnitViewManager getLearningUnitViewManager() {
        return learningUnitViewManager;
    }

    protected void stop() {
        ((CardLayout) getLayout()).show(this, "standardPanel");
        m_memoryStandardPanel.coverAll();
        learningUnitViewManager.fireLearningUnitViewEvent(new FLGMemoryEvent(FLGMemoryEvent.MEMORY_STOPPED));
    }

    private void createListByCategory(int category) {
        String elementIds[] = learningUnitViewElementsManager.getAllLearningUnitViewElementIds();
        m_currentSession.clear();
        for (int i = 0; i < elementIds.length; i++) {
            FLGGlossaryElement element = (FLGGlossaryElement) learningUnitViewElementsManager.getLearningUnitViewElement(elementIds[i], false);
            if (!element.getFolder() && element.getCategory() == category && element.getMemoryUsable()) m_currentSession.add(element);
        }
    }

    private void createListByDate(Date date) {
        String elementIds[] = learningUnitViewElementsManager.getAllLearningUnitViewElementIds();
        m_currentSession.clear();
        for (int i = 0; i < elementIds.length; i++) {
            FLGGlossaryElement element = (FLGGlossaryElement) learningUnitViewElementsManager.getLearningUnitViewElement(elementIds[i], false);
            if (!element.getFolder() && element.getLastTryAsDate() != null && element.getLastTryAsDate().before(date) && element.getMemoryUsable()) m_currentSession.add(element);
        }
    }

    protected class FLGMemoryContentPanel_Adapter extends FSLLearningUnitViewAdapter {

        public void learningUnitViewSpecificEventOccurred(FSLLearningUnitViewEvent event) {
            FLGMemoryEvent mEvent = (FLGMemoryEvent) event;
            switch(mEvent.getEventSpecificType()) {
                case FLGMemoryEvent.MEMORY_START:
                    start();
                    break;
                case FLGMemoryEvent.MEMORY_STOP:
                    stop();
                    break;
                case FLGMemoryEvent.MEMORY_UNCOVER:
                    uncover();
                    break;
                case FLGMemoryEvent.MEMORY_MOVE_UP:
                    moveUp();
                    break;
                case FLGMemoryEvent.MEMORY_MOVE_DOWN:
                    moveDown();
                    break;
                case FLGMemoryEvent.MEMORY_MOVE_NOT:
                    moveNot();
                    break;
                case FLGMemoryEvent.MEMORY_NEXT:
                    next();
                    break;
            }
        }
    }
}
