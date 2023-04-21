package org.mars_sim.msp.ui.swing.tool.mission.edit;

import org.mars_sim.msp.core.Coordinates;
import org.mars_sim.msp.core.Simulation;
import org.mars_sim.msp.core.person.Person;
import org.mars_sim.msp.core.person.ai.mission.*;
import org.mars_sim.msp.core.structure.Settlement;
import org.mars_sim.msp.core.vehicle.Rover;
import org.mars_sim.msp.ui.swing.MarsPanelBorder;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * The mission info panel for the edit mission dialog.
 */
public class InfoPanel extends JPanel {

    static final String ACTION_NONE = "None";

    static final String ACTION_CONTINUE = "End EVA and Continue to Next Site";

    static final String ACTION_HOME = "Return to Home Settlement and End Mission";

    static final String ACTION_NEAREST = "Go to Nearest Settlement and End Mission";

    Mission mission;

    Dialog parent;

    JTextField descriptionField;

    JComboBox actionDropDown;

    DefaultListModel memberListModel;

    JList memberList;

    JButton addMembersButton;

    JButton removeMembersButton;

    /**
	 * Constructor
	 * @param mission the mission to edit.
	 * @param parent the parent dialog.
	 */
    InfoPanel(Mission mission, Dialog parent) {
        this.mission = mission;
        this.parent = parent;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new MarsPanelBorder());
        JPanel descriptionPane = new JPanel(new FlowLayout(FlowLayout.LEFT));
        descriptionPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(descriptionPane);
        JLabel descriptionLabel = new JLabel("Description: ");
        descriptionPane.add(descriptionLabel);
        descriptionField = new JTextField(mission.getDescription(), 20);
        descriptionPane.add(descriptionField);
        JPanel actionPane = new JPanel(new FlowLayout(FlowLayout.LEFT));
        actionPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(actionPane);
        JLabel actionLabel = new JLabel("Action: ");
        actionPane.add(actionLabel);
        actionDropDown = new JComboBox(getActions(mission));
        actionDropDown.setEnabled(actionDropDown.getItemCount() > 1);
        actionPane.add(actionDropDown);
        JPanel membersPane = new JPanel(new BorderLayout());
        membersPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        membersPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(membersPane);
        JLabel membersLabel = new JLabel("Members: ");
        membersLabel.setVerticalAlignment(JLabel.TOP);
        membersPane.add(membersLabel, BorderLayout.WEST);
        JPanel memberListPane = new JPanel(new BorderLayout(0, 0));
        membersPane.add(memberListPane, BorderLayout.CENTER);
        JScrollPane memberScrollPane = new JScrollPane();
        memberScrollPane.setPreferredSize(new Dimension(100, 100));
        memberListPane.add(memberScrollPane, BorderLayout.CENTER);
        memberListModel = new DefaultListModel();
        Iterator<Person> i = mission.getPeople().iterator();
        while (i.hasNext()) memberListModel.addElement(i.next());
        memberList = new JList(memberListModel);
        memberList.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                removeMembersButton.setEnabled(memberList.getSelectedValues().length > 0);
            }
        });
        memberScrollPane.setViewportView(memberList);
        JPanel memberButtonPane = new JPanel(new FlowLayout(FlowLayout.CENTER));
        memberListPane.add(memberButtonPane, BorderLayout.SOUTH);
        addMembersButton = new JButton("Add Members");
        addMembersButton.setEnabled(canAddMembers());
        addMembersButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                addMembers();
            }
        });
        memberButtonPane.add(addMembersButton);
        removeMembersButton = new JButton("Remove Members");
        removeMembersButton.setEnabled(false);
        removeMembersButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                removeMembers();
            }
        });
        memberButtonPane.add(removeMembersButton);
    }

    /**
	 * Checks if members can be added to the mission.
	 * @return true if members can be added.
	 */
    private boolean canAddMembers() {
        boolean roomInMission = (memberListModel.size() < mission.getMissionCapacity());
        boolean availablePeople = (getAvailablePeople().size() > 0);
        return (roomInMission && availablePeople);
    }

    /**
	 * Open the add members dialog.
	 */
    private void addMembers() {
        new AddMembersDialog(parent, mission, memberListModel, getAvailablePeople());
        addMembersButton.setEnabled(canAddMembers());
    }

    /**
	 * Remove selected members from the list.
	 */
    private void removeMembers() {
        int[] selectedIndexes = memberList.getSelectedIndices();
        Object[] selectedPeople = new Object[selectedIndexes.length];
        for (int x = 0; x < selectedIndexes.length; x++) selectedPeople[x] = memberListModel.elementAt(selectedIndexes[x]);
        for (Object aSelectedPeople : selectedPeople) memberListModel.removeElement(aSelectedPeople);
        addMembersButton.setEnabled(canAddMembers());
    }

    /**
	 * Gets a vector of possible actions for the mission.
	 * @param mission the mission 
	 * @return vector of actions.
	 */
    private Vector<String> getActions(Mission mission) {
        Vector<String> actions = new Vector<String>();
        actions.add(ACTION_NONE);
        String phase = mission.getPhase();
        if (phase.equals(CollectResourcesMission.COLLECT_RESOURCES)) {
            CollectResourcesMission collectResourcesMission = (CollectResourcesMission) mission;
            if (collectResourcesMission.getNumCollectionSites() > collectResourcesMission.getNumCollectionSitesVisited()) actions.add(ACTION_CONTINUE);
        }
        if (mission instanceof TravelMission) {
            TravelMission travelMission = (TravelMission) mission;
            int nextNavpointIndex = travelMission.getNextNavpointIndex();
            if ((nextNavpointIndex > -1) && (nextNavpointIndex < (travelMission.getNumberOfNavpoints() - 1))) {
                if (!mission.getPhase().equals(VehicleMission.EMBARKING)) actions.add(ACTION_HOME);
            }
        }
        if (mission instanceof VehicleMission) {
            VehicleMission vehicleMission = (VehicleMission) mission;
            try {
                Settlement closestSettlement = vehicleMission.findClosestSettlement();
                if ((closestSettlement != null) && !closestSettlement.equals(vehicleMission.getAssociatedSettlement())) {
                    if (!mission.getPhase().equals(VehicleMission.EMBARKING)) actions.add(ACTION_NEAREST);
                }
            } catch (Exception e) {
            }
        }
        return actions;
    }

    /**
	 * Gets a collection of people available to be added to the mission.
	 * @return collection of available people.
	 */
    private Collection<Person> getAvailablePeople() {
        Collection<Person> result = new ConcurrentLinkedQueue<Person>();
        if (mission instanceof RoverMission) {
            Rover rover = ((RoverMission) mission).getRover();
            String phase = mission.getPhase();
            Collection<Person> peopleAtLocation = null;
            if (rover != null) {
                if (phase.equals(RoverMission.EMBARKING) || phase.equals(RoverMission.DISEMBARKING)) {
                    Settlement settlement = rover.getSettlement();
                    if (settlement != null) peopleAtLocation = settlement.getInhabitants();
                } else {
                    peopleAtLocation = rover.getCrew();
                }
            }
            Iterator<Person> i = peopleAtLocation.iterator();
            while (i.hasNext()) {
                Person person = i.next();
                if (!memberListModel.contains(person)) result.add(person);
            }
        }
        try {
            Coordinates missionLocation = mission.getCurrentMissionLocation();
            Iterator<Person> i = Simulation.instance().getUnitManager().getPeople().iterator();
            while (i.hasNext()) {
                Person person = i.next();
                if (person.getLocationSituation().equals(Person.OUTSIDE)) {
                    if (person.getCoordinates().equals(missionLocation)) {
                        if (!memberListModel.contains(person)) result.add(person);
                    }
                }
            }
        } catch (Exception e) {
        }
        return result;
    }
}
