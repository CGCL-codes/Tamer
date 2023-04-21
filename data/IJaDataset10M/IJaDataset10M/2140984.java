package de.tvstaufia.kneiphelfer.gui;

import java.awt.BorderLayout;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import de.tvstaufia.kneiphelfer.Ereignis;

public class EreignissePanel extends JPanel implements ListSelectionListener {

    /**
	 * 
	 */
    private static final long serialVersionUID = -8557267909831913523L;

    private JList ereignisseList;

    private JSplitPane rightLeftSplitPane, upDownSplitPane;

    private JScrollPane ereignisseListScrollPane;

    private EreignisseStammDaten ereignisFrame;

    private EreignisseStammDaten ereignisStammDatenPanel;

    private JPanel ereignisWarenFrame;

    private Ereignis selectedEreignis = null;

    private EreignisseListModel ereignisseListModel;

    private boolean alreadyInEreignisseListSelectionChanged = false;

    JTabbedPane tabbedPane;

    public EreignissePanel() {
        super();
        this.setLayout(new BorderLayout());
        this.rightLeftSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        this.add(this.rightLeftSplitPane, BorderLayout.CENTER);
        this.ereignisseListModel = new EreignisseListModel();
        this.ereignisseList = new JList(this.ereignisseListModel);
        this.ereignisseList.setName("Ereignisse-Liste");
        this.ereignisseList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.ereignisseList.addListSelectionListener(this);
        this.ereignisseListScrollPane = new JScrollPane(this.ereignisseList);
        this.rightLeftSplitPane.setLeftComponent(this.ereignisseListScrollPane);
        ereignisFrame = new EreignisseStammDaten(this);
        ereignisFrame.setLayout(new BorderLayout());
        this.upDownSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        ereignisStammDatenPanel = new EreignisseStammDaten(this);
        this.tabbedPane = new JTabbedPane();
        this.upDownSplitPane.setTopComponent(this.ereignisStammDatenPanel);
        this.upDownSplitPane.setBottomComponent(this.tabbedPane);
        this.ereignisFrame.add(this.upDownSplitPane, BorderLayout.CENTER);
        this.rightLeftSplitPane.setRightComponent(this.ereignisFrame);
        ereignisWarenFrame = new JPanel();
        this.tabbedPane.add("Waren", this.ereignisWarenFrame);
    }

    public Ereignis getSelectedEreignis() {
        return selectedEreignis;
    }

    public void setSelectedEreignis(Ereignis selectedEreignis) {
        this.selectedEreignis = selectedEreignis;
        this.tabbedPane.remove(this.ereignisWarenFrame);
        if (this.selectedEreignis != null) {
            this.ereignisWarenFrame = new WarenPanel(this.selectedEreignis);
        } else {
            this.ereignisWarenFrame = new JPanel();
        }
        this.tabbedPane.add("Waren", this.ereignisWarenFrame);
    }

    public JList getEreignisseList() {
        return ereignisseList;
    }

    public EreignisseListModel getEreignisseListModel() {
        return ereignisseListModel;
    }

    public void setEreignisseListModel(EreignisseListModel ereignisseListModel) {
        this.ereignisseListModel = ereignisseListModel;
    }

    public boolean checkForUnsavedEreignis() {
        return this.ereignisStammDatenPanel.checkForUnsavedEreignis() == true;
    }

    public void saveEreignis() {
        this.ereignisStammDatenPanel.saveEreignis();
    }

    public void readFromSelectedEreignis() {
        this.ereignisStammDatenPanel.readFromSelectedEreignis();
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (this.alreadyInEreignisseListSelectionChanged == false) {
            this.alreadyInEreignisseListSelectionChanged = true;
            int saveFirst = JOptionPane.NO_OPTION;
            if (this.checkForUnsavedEreignis()) {
                saveFirst = JOptionPane.showConfirmDialog(this, "Ungespeicherte Änderungen am Ereignisse, erst speichern?", "Ungesicherte Änderungen speichern?", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
            }
            if (saveFirst == JOptionPane.YES_OPTION) {
                this.saveEreignis();
            } else if (saveFirst == JOptionPane.CANCEL_OPTION) {
                this.ereignisseList.setSelectedValue(this.getSelectedEreignis(), false);
                this.alreadyInEreignisseListSelectionChanged = false;
                return;
            }
            this.setSelectedEreignis((Ereignis) this.ereignisseList.getSelectedValue());
            this.readFromSelectedEreignis();
            this.alreadyInEreignisseListSelectionChanged = false;
        }
    }
}
