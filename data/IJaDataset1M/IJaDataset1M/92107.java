package teach.multiagent07.net;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import org.matsim.basic.v01.BasicLinkImpl;
import org.matsim.utils.vis.netvis.DrawableAgentI;
import teach.multiagent07.simulation.CAMobSim;
import teach.multiagent07.simulation.Vehicle;
import teach.multiagent07.util.Event;

public class CALink extends BasicLinkImpl implements CANetStateWritableI {

    class DepatureTimeComparator implements Comparator<Vehicle> {

        public int compare(Vehicle veh1, Vehicle veh2) {
            if (veh1.getDepartureTime() > veh2.getDepartureTime()) return 1; else if (veh1.getDepartureTime() < veh2.getDepartureTime()) return -1;
            return 0;
        }
    }

    private PriorityQueue<Vehicle> parkingList = new PriorityQueue<Vehicle>(30, new DepatureTimeComparator());

    private List<Vehicle> waitingList = new ArrayList<Vehicle>();

    public CALink(String id) {
        super(id);
    }

    private final double CELLSIZE = 200;

    private double length;

    private int nCells;

    private List<Vehicle> cells;

    public static void main(String[] args) {
        CALink link = new CALink("1");
        link.setLength(375);
        link.build();
        link.randomFill(0.5);
        for (int step = 0; step < 30; step++) {
            link.move(step);
            link.tty();
        }
    }

    public void addVeh(Vehicle veh) {
        if (veh != null) {
            Event event = new Event(CAMobSim.getCurrentTime(), Event.ENTER_LINK, this, veh.getId());
            CAMobSim.getEventManager().addEvent(event);
        }
        cells.set(0, veh);
    }

    public void removeFirstVeh() {
        Vehicle outgoingVeh = cells.get(0);
        cells.set(nCells - 1, null);
        if (outgoingVeh != null) {
            Event event = new Event(CAMobSim.getCurrentTime(), Event.LEAVE_LINK, this, outgoingVeh.getId());
            CAMobSim.getEventManager().addEvent(event);
        }
    }

    public Vehicle getFirstVeh() {
        return cells.get(nCells - 1);
    }

    public boolean hasSpace() {
        return cells.get(0) == null;
    }

    public void doBoundary() {
        if (getFirstVeh() != null && hasSpace()) {
            Vehicle veh = getFirstVeh();
            removeFirstVeh();
            addVeh(veh);
        }
    }

    @Override
    public void setLength(double l) {
        length = l;
    }

    public void randomFill(double d) {
        for (int i = 0; i < nCells; i++) {
            if (Math.random() <= d) cells.set(i, new Vehicle());
        }
    }

    public void build() {
        nCells = (int) (length / CELLSIZE);
        cells = new ArrayList();
        for (int i = 0; i < nCells; i++) cells.add(null);
    }

    public void tty() {
        for (int i = 0; i < nCells; i++) {
            if (cells.get(i) != null) System.out.print("X"); else System.out.print(".");
        }
        System.out.println("");
    }

    private void park2Wait(int now) {
        while (parkingList.peek() != null) {
            Vehicle veh = parkingList.peek();
            if (veh.getDepartureTime() <= now) {
                parkingList.poll();
                waitingList.add(veh);
                veh.leaveActivity();
            } else break;
        }
    }

    private void wait2Cell() {
        int middle = nCells / 2;
        Vehicle cellTest = cells.get(middle);
        if (!waitingList.isEmpty() && cellTest == null) {
            Vehicle veh = waitingList.get(0);
            waitingList.remove(0);
            cells.set(middle, veh);
        }
    }

    private void removeDestinationReached() {
        int middle = nCells / 2;
        Vehicle veh = cells.get(middle);
        if (veh != null) {
            if (veh.getDestinationLink() == this) {
                veh.reachActivity();
                cells.set(middle, null);
            }
        }
    }

    public void move(int step) {
        park2Wait(step);
        wait2Cell();
        removeDestinationReached();
        for (int i = 0; i < nCells - 1; i++) {
            if (cells.get(i) != null && cells.get(i + 1) == null) {
                Vehicle veh = cells.get(i);
                cells.set(i + 1, veh);
                cells.set(i, null);
                i++;
            }
        }
    }

    public void addParking(Vehicle vehicle) {
        parkingList.add(vehicle);
    }

    public List<DrawableAgentI> getDisplayAgents() {
        List<DrawableAgentI> vehs = new ArrayList<DrawableAgentI>();
        for (int i = 0; i < nCells; i++) {
            if (cells.get(i) != null) {
                CANetStateWriter.AgentOnLink veh = new CANetStateWriter.AgentOnLink(i * CELLSIZE);
                vehs.add(veh);
            }
        }
        return vehs;
    }

    public double getDisplayValue() {
        double quota = 0;
        for (int i = 0; i < nCells; i++) if (cells.get(i) != null) quota += 1;
        return quota / nCells;
    }
}
