package eis.examples.carriage;

import java.util.LinkedList;
import eis.exceptions.ActException;
import eis.exceptions.AgentException;
import eis.exceptions.NoEnvironmentException;
import eis.exceptions.PerceiveException;
import eis.exceptions.RelationException;
import eis.iilang.*;

abstract class Agent extends Thread {

    protected EnvironmentInterface ei = null;

    protected String id = null;

    public Agent(EnvironmentInterface ei, String id) {
        this.ei = ei;
        this.id = id;
        this.setPriority(MIN_PRIORITY);
    }

    protected void say(String msg) {
        System.out.println(id + " says: " + msg);
    }
}

class PushingAgent extends Agent {

    public PushingAgent(EnvironmentInterface ei, String id) {
        super(ei, id);
    }

    public void run() {
        try {
            ei.associateEntity(id, ei.getFreeEntities().getFirst());
            while (true) {
                LinkedList<Percept> percepts = null;
                percepts = ei.getAllPercepts(id);
                say("I believe the carriage is at " + percepts);
                ei.performAction(id, new Action("push"));
                try {
                    Thread.sleep(950);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (PerceiveException e) {
            e.printStackTrace();
        } catch (RelationException e) {
            e.printStackTrace();
        } catch (ActException e) {
            e.printStackTrace();
        } catch (NoEnvironmentException e) {
            e.printStackTrace();
        }
    }
}

class AlternatingAgent extends Agent {

    public AlternatingAgent(EnvironmentInterface ei, String id) {
        super(ei, id);
    }

    public void run() {
        try {
            ei.associateEntity(id, ei.getFreeEntities().getFirst());
            while (true) {
                LinkedList<Percept> percepts = null;
                percepts = ei.getAllPercepts(id);
                say("I believe the carriage is at " + percepts);
                ei.performAction(id, new Action("push"));
                percepts = ei.getAllPercepts(id);
                say("I believe the carriage is at " + percepts);
                ei.performAction(id, new Action("wait"));
                try {
                    Thread.sleep(950);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (PerceiveException e) {
            e.printStackTrace();
        } catch (RelationException e) {
            e.printStackTrace();
        } catch (ActException e) {
            e.printStackTrace();
        } catch (NoEnvironmentException e) {
            e.printStackTrace();
        }
    }
}

public class Main {

    public static void main(String[] args) {
        try {
            EnvironmentInterface ei = new EnvironmentInterface();
            Thread.sleep(1000);
            Agent ag1 = new PushingAgent(ei, "ag1");
            Agent ag2 = new AlternatingAgent(ei, "ag2");
            ei.registerAgent("ag1");
            ei.registerAgent("ag2");
            ag1.start();
            ag2.start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (AgentException e) {
            e.printStackTrace();
        }
    }
}
