package DSENS;

import java.util.ArrayList;

/**
 *
 * @author teeteto
 */
public class WirelessSensorNode extends Node {

    private int X;

    private int Y;

    private int range;

    private double battery;

    private ArrayList<Event> eventQueue;

    private int eventIndex;

    Log l;

    int msgR;

    int msgT;

    int distBS;

    int link2BS;

    private boolean timer[];

    private ArrayList<Integer> distance;

    private ArrayList<Integer> distBS_tmp;

    private ArrayList<Integer> link_tmp;

    public int getX() {
        return X;
    }

    public void setX(int X) {
        this.X = X;
    }

    public int getY() {
        return Y;
    }

    public void setY(int Y) {
        this.Y = Y;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public double getBattery() {
        return battery;
    }

    public void setBattery(double battery) {
        this.battery = battery;
    }

    public int getDistBS() {
        return distBS;
    }

    public void setDistBS(int distBS) {
        this.distBS = distBS;
    }

    public int getLink2BS() {
        return link2BS;
    }

    public void setLink2BS(int link2BS) {
        this.link2BS = link2BS;
    }

    public WirelessSensorNode(ArrayList<Neighbour> neighborood) {
        super(neighborood);
        this.eventQueue = new ArrayList(1);
        this.eventIndex = 0;
        this.distBS_tmp = new ArrayList<Integer>(1);
        this.link_tmp = new ArrayList<Integer>(1);
        this.distance = new ArrayList<Integer>(1);
        this.msgR = 0;
        this.msgT = 0;
        this.timer = new boolean[2];
        timer[0] = timer[1] = false;
    }

    public WirelessSensorNode(NodeState state) {
        super(state);
        this.eventQueue = new ArrayList(1);
        this.eventIndex = 0;
    }

    public WirelessSensorNode(int X, int Y, int range, double battery) {
        this.X = X;
        this.Y = Y;
        this.range = range;
        this.battery = battery;
        this.eventQueue = new ArrayList(1);
        this.eventIndex = 0;
        this.l = new Log();
        this.link2BS = 100000;
        this.distBS = 100000;
        this.msgR = 0;
        this.msgT = 0;
        this.distance = new ArrayList<Integer>(1);
        this.distBS_tmp = new ArrayList<Integer>(1);
        this.link_tmp = new ArrayList<Integer>(1);
        this.timer = new boolean[2];
        timer[0] = timer[1] = false;
    }

    private NodeState powerManagement(double i) throws LogException {
        this.battery = this.battery - i;
        if (this.battery <= 0) {
            this.setState(NodeState.OFF);
            this.l.printlnLog("Battery OUT...shutdown node");
            return NodeState.OFF;
        } else return this.getState();
    }

    @Override
    public void printNode() {
        super.printNode();
    }

    @Override
    public void receiveEvent(Event e) {
        try {
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void manageEvent(Event e) {
        Event ev;
        int int_index = 0;
        ArrayList<Neighbour> nb;
        try {
            if (e.geteventType().equals("start")) {
                this.setDistBS(0);
                this.msgR++;
                for (int i = 0; i < this.neighborood.size(); i++) {
                    ev = new Event(this.ID, this.neighborood.get(i).getId(), "invite", 1, new Message(this.getDistBS() + 1));
                    GlobalInfo.TIMELINE.addEvent(1, ev);
                    this.msgT++;
                }
                l.printEvent(e);
                l.printNode(this);
                nb = new ArrayList<Neighbour>(1);
                nb.add(new Neighbour(0, 0));
                this.setNeighborood(nb);
                this.setState(NodeState.OFF);
            } else if (this.ID != 0 && e.geteventType().equals("invite") && super.getState() == NodeState.ON) {
                if (this.timer[0] == true) return;
                this.msgR++;
                this.setState(NodeState.WORKING);
                this.link_tmp.add(e.getgenNodeID());
                this.distBS_tmp.add(e.getMessage().getTimetolive());
                int temp = this.link_tmp.size() - 1;
                for (int j = 0; j < this.neighborood.size(); j++) if (this.neighborood.get(j).getId() == this.link_tmp.get(temp)) int_index = j;
                this.distance.add(this.neighborood.get(int_index).getLinkSize());
                int timer = this.neighborood.size() * 3;
                ev = new Event(this.ID, this.ID, "timer1", timer, new Message());
                GlobalInfo.TIMELINE.addEvent(timer, ev);
            } else if (this.ID != 0 && e.geteventType().equals("invite") && super.getState() == NodeState.WORKING) {
                if (this.timer[0] == true) return;
                this.msgR++;
                this.link_tmp.add(e.getgenNodeID());
                this.distBS_tmp.add(e.getMessage().getTimetolive());
                for (int j = 0; j < this.neighborood.size(); j++) if (this.neighborood.get(j).getId() == e.getgenNodeID()) int_index = j;
                this.distance.add(this.neighborood.get(int_index).getLinkSize());
            } else if (e.geteventType().equals("timer1")) {
                l.printEvent(e);
                this.timer[0] = true;
                this.setState(NodeState.ON);
                int bestNeighbour = 10000;
                for (int i = 0; i < this.link_tmp.size(); i++) {
                    if (this.distance.get(i) < bestNeighbour) {
                        this.link2BS = this.link_tmp.get(i);
                        bestNeighbour = this.distance.get(i);
                        this.setDistBS(this.distBS_tmp.get(i));
                    }
                }
                for (int j = 0; j < this.neighborood.size(); j++) if (this.neighborood.get(j).getId() == this.link2BS) bestNeighbour = j;
                for (int i = 0; i < this.neighborood.size(); i++) {
                    ev = new Event(this.ID, this.neighborood.get(i).getId(), "invite", 1, new Message(this.getDistBS() + 1));
                    GlobalInfo.TIMELINE.addEvent(1, ev);
                    this.msgT++;
                }
                nb = new ArrayList<Neighbour>(1);
                nb.add(new Neighbour(this.neighborood.get(bestNeighbour).getLinkSize(), this.link2BS));
                this.setNeighborood(nb);
                this.setState(NodeState.ON);
            } else if (e.geteventType().equals("CH")) {
                this.msgR++;
                for (int i = 0; i < this.neighborood.size(); i++) {
                    ev = new Event(this.ID, this.neighborood.get(i).getId(), "chrequest", 1, new Message(this.ID));
                    GlobalInfo.TIMELINE.addEvent(1, ev);
                    this.msgT++;
                }
                this.setState(NodeState.OFF);
            } else if (e.geteventType().equals("chrequest") && super.getState() == NodeState.ON) {
                if (this.timer[1] == true) return;
                this.msgR++;
                this.setState(NodeState.WORKING);
                this.link_tmp.add(e.getgenNodeID());
                int temp = this.link_tmp.size() - 1;
                for (int j = 0; j < this.neighborood.size(); j++) if (this.neighborood.get(j).getId() == this.link_tmp.get(temp)) int_index = j;
                this.distance.add(this.neighborood.get(int_index).getLinkSize());
                this.distBS = 1;
                int timer = this.neighborood.size() * 3;
                ev = new Event(this.ID, this.ID, "timer2", timer, new Message());
                GlobalInfo.TIMELINE.addEvent(timer, ev);
            } else if (e.geteventType().equals("chrequest") && super.getState() == NodeState.WORKING) {
                if (this.timer[1] == true) return;
                this.msgR++;
                this.link_tmp.add(e.getgenNodeID());
                for (int j = 0; j < this.neighborood.size(); j++) if (this.neighborood.get(j).getId() == e.getgenNodeID()) int_index = j;
                this.distance.add(this.neighborood.get(int_index).getLinkSize());
            } else if (e.geteventType().equals("timer2")) {
                l.printEvent(e);
                this.timer[1] = true;
                this.setState(NodeState.ON);
                int bestNeighbour = 10000;
                for (int i = 0; i < this.link_tmp.size(); i++) {
                    if (this.distance.get(i) < bestNeighbour) {
                        this.link2BS = this.link_tmp.get(i);
                        bestNeighbour = this.distance.get(i);
                    }
                }
                if (bestNeighbour == 10000) {
                    nb = new ArrayList<Neighbour>(1);
                    nb.add(new Neighbour(0, this.ID));
                    this.setNeighborood(nb);
                } else {
                    for (int j = 0; j < this.neighborood.size(); j++) if (this.neighborood.get(j).getId() == this.link2BS) bestNeighbour = j;
                    nb = new ArrayList<Neighbour>(1);
                    nb.add(new Neighbour(this.neighborood.get(bestNeighbour).getLinkSize(), this.link2BS));
                    this.setNeighborood(nb);
                }
            } else if (e.geteventType().equals("CH")) {
                this.distBS = 0;
                this.msgR++;
                for (int i = 0; i < this.neighborood.size(); i++) {
                    ev = new Event(this.ID, this.neighborood.get(i).getId(), "chrequest", 1, new Message(this.ID));
                    GlobalInfo.TIMELINE.addEvent(1, ev);
                    this.msgT++;
                }
                this.setState(NodeState.OFF);
            } else if (e.geteventType().equals("Check")) {
                if (this.distBS > 1) {
                    nb = new ArrayList<Neighbour>(1);
                    nb.add(new Neighbour(0, this.ID));
                    this.setNeighborood(nb);
                    this.setState(NodeState.OFF);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
