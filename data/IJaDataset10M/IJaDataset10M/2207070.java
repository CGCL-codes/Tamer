package simsyncgui.chart;

import gov.nist.mel.simsync.GroupMember;
import gov.nist.mel.simsync.SimulationGroup;
import java.awt.Color;
import java.awt.Graphics;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import javax.swing.JComponent;

/**
 * Component that displays a set of horizontal bars representing the current times
 * of the members of a group.
 * @author Guillaume Radde (guillaume.radde@nist.gov)
 */
public class SimulationGroupChart extends JComponent implements PropertyChangeListener {

    private SimulationGroup group;

    private final ArrayList<GroupMemberBar> children = new ArrayList<GroupMemberBar>();

    private int minTime = 0;

    private int maxTime = 0;

    private MemberListener listener = new MemberListener();

    public static final int SCALE_HEIGHT = 20;

    public static final int NAME_COLUMN_SIZE = 100;

    public static final int SIM_HEIGHT = 30;

    /**
     * The minimum number of pixels between each unit of the scale
     */
    public static final int SCALE_LEGEND_SEPARATION = 30;

    static {
        GroupMemberBar.NAME_COLUMN_SIZE = NAME_COLUMN_SIZE;
    }

    public void addMember(GroupMember member) {
        GroupMemberBar bar = new GroupMemberBar();
        children.add(bar);
        bar.setMember(member);
        bar.setMinTime(getMinTime());
        bar.setMaxTime(getMaxTime());
        member.addPropertyChangeListener(listener);
        add(bar);
    }

    private void removeMember(GroupMember member) {
        GroupMemberBar removedBar = null;
        for (GroupMemberBar bar : children) {
            if (bar.getMember().equals(member)) {
                bar.displayLeavingMember();
            }
        }
    }

    private void initialize() {
        for (GroupMember member : group.getMembers()) {
            addMember(member);
        }
        validate();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.WHITE);
        g.fillRect(g.getClipBounds().x, g.getClipBounds().y, g.getClipBounds().width, g.getClipBounds().height);
        paintScale(g);
        int memberCount = 0;
        for (GroupMemberBar bar : children) {
            bar.setBounds(0, (memberCount * SIM_HEIGHT) + SCALE_HEIGHT, getWidth(), SIM_HEIGHT);
            memberCount++;
        }
    }

    private void paintScale(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(NAME_COLUMN_SIZE, 0, getWidth() - NAME_COLUMN_SIZE, SCALE_HEIGHT);
        g.fillRect(NAME_COLUMN_SIZE, getHeight() - SCALE_HEIGHT, getWidth() - NAME_COLUMN_SIZE, SCALE_HEIGHT);
        int displayedRange = maxTime - minTime;
    }

    /**
     * @return the minTime
     */
    public int getMinTime() {
        return minTime;
    }

    /**
     * @param minTime the minTime to set
     */
    public void setMinTime(int minTime) {
        this.minTime = minTime;
        for (GroupMemberBar bar : children) {
            bar.setMinTime(minTime);
        }
        repaint();
    }

    /**
     * @return the maxTime
     */
    public int getMaxTime() {
        return maxTime;
    }

    /**
     * @param maxTime the maxTime to set
     */
    public void setMaxTime(int maxTime) {
        this.maxTime = maxTime;
        for (GroupMemberBar bar : children) {
            bar.setMaxTime(maxTime);
        }
        repaint();
    }

    public void setGroup(SimulationGroup group) {
        this.group = group;
        group.addPropertyChangeListener(this);
        initialize();
    }

    /**
     * A Listener that will update the max and min time displayed when the requested
     * time of a group member changes.
     */
    private class MemberListener implements PropertyChangeListener {

        public void propertyChange(PropertyChangeEvent evt) {
            String prop = evt.getPropertyName();
            if (GroupMember.LOCAL_TIME_PROP.equals(prop) || GroupMember.REQUESTED_TIME_PROP.equals(prop)) {
                int newTime = (Integer) evt.getNewValue();
                if (newTime > getMaxTime()) {
                    setMaxTime(newTime);
                    int newMinTime = Integer.MAX_VALUE;
                    for (GroupMemberBar bar : children) {
                        if (bar.getMember().getLocalTime() < newMinTime) {
                            newMinTime = bar.getMember().getLocalTime();
                        }
                    }
                    setMinTime(newMinTime);
                }
            }
        }
    }

    public void propertyChange(PropertyChangeEvent evt) {
        String prop = evt.getPropertyName();
        if (SimulationGroup.MEMBERS_PROP.equals(prop)) {
            if (evt.getOldValue() == null && evt.getNewValue() != null) {
                addMember((GroupMember) evt.getNewValue());
            } else if (evt.getOldValue() != null && evt.getNewValue() == null) {
                removeMember((GroupMember) evt.getOldValue());
            }
        }
    }
}
