package org.mars_sim.msp.ui.swing.tool.mission.edit;

import org.mars_sim.msp.core.person.Person;
import org.mars_sim.msp.core.person.ai.mission.Mission;
import org.mars_sim.msp.ui.swing.MarsPanelBorder;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.Iterator;

/**
 * A dialog window for adding members to the mission for the mission tool.
 */
class AddMembersDialog extends JDialog {

    Mission mission;

    DefaultListModel memberListModel;

    DefaultListModel availableListModel;

    JList availableList;

    JButton addButton;

    /**
	 * Constructor
	 * @param owner the owner dialog.
	 * @param mission the mission to add to.
	 * @param memberListModel the member list model in the edit mission dialog.
	 * @param availablePeople the available people to add.
	 */
    public AddMembersDialog(Dialog owner, Mission mission, DefaultListModel memberListModel, Collection<Person> availablePeople) {
        super(owner, "Add Members", true);
        this.mission = mission;
        this.memberListModel = memberListModel;
        setLayout(new BorderLayout(5, 5));
        ((JComponent) getContentPane()).setBorder(new MarsPanelBorder());
        JLabel headerLabel = new JLabel("Select available people to add to the mission.");
        add(headerLabel, BorderLayout.NORTH);
        JPanel availablePeoplePane = new JPanel(new BorderLayout(0, 0));
        add(availablePeoplePane, BorderLayout.CENTER);
        JScrollPane availableScrollPane = new JScrollPane();
        availableScrollPane.setPreferredSize(new Dimension(100, 100));
        availablePeoplePane.add(availableScrollPane, BorderLayout.CENTER);
        availableListModel = new DefaultListModel();
        Iterator<Person> i = availablePeople.iterator();
        while (i.hasNext()) availableListModel.addElement(i.next());
        availableList = new JList(availableListModel);
        availableList.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                addButton.setEnabled(availableList.getSelectedValues().length > 0);
            }
        });
        availableScrollPane.setViewportView(availableList);
        JPanel buttonPane = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        add(buttonPane, BorderLayout.SOUTH);
        addButton = new JButton("Add");
        addButton.setEnabled(availableList.getSelectedValues().length > 0);
        addButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                addPeople();
                dispose();
            }
        });
        buttonPane.add(addButton);
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        buttonPane.add(cancelButton);
        pack();
        setLocationRelativeTo(owner);
        setResizable(false);
        setVisible(true);
    }

    /**
	 * Add people to edit mission dialog.
	 */
    private void addPeople() {
        int[] selectedIndexes = availableList.getSelectedIndices();
        for (int selectedIndexe : selectedIndexes) {
            if (memberListModel.getSize() < mission.getMissionCapacity()) memberListModel.addElement(availableListModel.elementAt(selectedIndexe));
        }
    }
}
