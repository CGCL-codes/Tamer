package unbbayes.gui.umpst;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;
import java.util.TreeSet;
import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import unbbayes.model.umpst.project.UMPSTProject;

public class TrackingPanel extends IUMPSTPanel {

    private static final long serialVersionUID = 1L;

    private JButton buttonCopy, buttonDelete;

    private JButton buttonSave;

    JList list, listAux;

    DefaultListModel listModel = new DefaultListModel();

    DefaultListModel listModelAux = new DefaultListModel();

    public TrackingPanel(UmpstModule janelaPai, UMPSTProject umpstProject) {
        super(janelaPai);
        this.setUmpstProject(umpstProject);
        this.setLayout(new GridLayout(1, 0));
        this.add(getTrackingPanel());
    }

    public Box getTrackingPanel() {
        Box box = Box.createHorizontalBox();
        Set<String> keys = getUmpstProject().getMapGoal().keySet();
        TreeSet<String> sortedKeys = new TreeSet<String>(keys);
        for (String key : sortedKeys) {
            listModel.addElement(getUmpstProject().getMapGoal().get(key).getGoalName());
        }
        list = new JList(listModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        list.setLayoutOrientation(JList.VERTICAL_WRAP);
        list.setVisibleRowCount(-1);
        JScrollPane listScroller = new JScrollPane(list);
        listScroller.setMinimumSize(new Dimension(300, 200));
        box.add(listScroller);
        buttonCopy = new JButton("copy >>");
        box.add(buttonCopy);
        buttonCopy.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                listModelAux.addElement(list.getSelectedValue());
                listModel.removeElement(list.getSelectedValue());
            }
        });
        buttonDelete = new JButton("<< delete");
        box.add(buttonDelete);
        buttonDelete.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                listModel.addElement(listAux.getSelectedValue());
                listModelAux.removeElement(listAux.getSelectedValue());
            }
        });
        buttonSave = new JButton("Save");
        box.add(buttonSave);
        buttonSave.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                UmpstModule pai = getFatherPanel();
                changePanel(pai.getMenuPanel());
            }
        });
        listAux = new JList(listModelAux);
        listAux.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        listAux.setLayoutOrientation(JList.VERTICAL_WRAP);
        listAux.setVisibleRowCount(-1);
        JScrollPane listScrollerAux = new JScrollPane(listAux);
        listScrollerAux.setMinimumSize(new Dimension(300, 200));
        box.add(listScrollerAux);
        return box;
    }
}
