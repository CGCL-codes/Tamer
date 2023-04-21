package es.eucm.eadventure.editor.gui.auxiliar;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import es.eucm.eadventure.editor.control.Controller;
import es.eucm.eadventure.editor.control.controllers.ChapterToolManager;
import es.eucm.eadventure.editor.control.controllers.general.ChapterListDataControl;

public class ToolSystemDebugger extends JDialog {

    private ChapterListDataControl dataControl;

    private List<JList> chapterLists;

    private JPanel mainPanel;

    public ToolSystemDebugger(ChapterListDataControl dataControl) {
        super(Controller.getInstance().peekWindow(), "", Dialog.ModalityType.MODELESS);
        this.dataControl = dataControl;
        update();
        setVisible(true);
    }

    public void update() {
        if (mainPanel != null) this.remove(mainPanel);
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(1, dataControl.getChaptersCount()));
        String[] chapterTitles = dataControl.getChapterTitles();
        chapterLists = new ArrayList<JList>();
        for (int i = 0; i < dataControl.getChapterToolManagers().size(); i++) {
            ChapterToolManager ctManager = dataControl.getChapterToolManagers().get(i);
            JPanel listPanel = new JPanel();
            listPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), chapterTitles[i]));
            String[] listValues = new String[ctManager.getGlobalToolManager().getRedoList().size() + ctManager.getGlobalToolManager().getUndoList().size() + 1];
            for (int j = 0; j < ctManager.getGlobalToolManager().getRedoList().size(); j++) {
                listValues[j] = ctManager.getGlobalToolManager().getRedoList().get(j).getToolName();
            }
            listValues[ctManager.getGlobalToolManager().getRedoList().size()] = "[[CURRENT]]]";
            for (int j = 0; j < ctManager.getGlobalToolManager().getUndoList().size(); j++) {
                listValues[ctManager.getGlobalToolManager().getRedoList().size() + ctManager.getGlobalToolManager().getUndoList().size() - j] = ctManager.getGlobalToolManager().getUndoList().get(j).getToolName();
            }
            JList newList = new JList(listValues);
            listPanel.setLayout(new BorderLayout());
            listPanel.add(newList, BorderLayout.CENTER);
            mainPanel.add(listPanel);
        }
        this.add(mainPanel);
        pack();
        this.repaint();
    }
}
