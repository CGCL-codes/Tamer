package it.timehero.agent;

import it.timehero.util.AgentInputController;
import it.timehero.world.WorldEngine;
import jason.RevisionFailedException;
import jason.architecture.AgArch;
import jason.asSemantics.ActionExec;
import jason.asSemantics.Agent;
import jason.asSyntax.Literal;
import jason.bb.DefaultBeliefBase;
import jason.runtime.Settings;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Esempio di classe che interagisce con Jason
 */
public class SimpleJasonAgent extends AgArch implements Runnable {

    private boolean playermove = false;

    private static Logger logger = Logger.getLogger(SimpleJasonAgent.class.getName());

    private AgentInputController inputController;

    private int cyclenr;

    private String ID;

    private WorldEngine we;

    private String playerDirection;

    public SimpleJasonAgent(WorldEngine we, String ID) {
        inputController = new AgentInputController(we);
        this.we = we;
        this.ID = ID;
        try {
            Agent ag = new Agent();
            setTS(ag.initAg(this, new DefaultBeliefBase(), "data/agent/example/demo.asl", new Settings()));
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Init error", ex);
        }
    }

    public void run() {
        logger.info("Starting Agent " + getAgName());
        try {
            while (isRunning()) {
                getTS().reasoningCycle();
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Run error", e);
        }
    }

    public String getAgName() {
        return "Bob";
    }

    @Override
    public List<Literal> perceive() {
        cyclenr++;
        double xpos = 0, ypos = 0;
        String str = new String("user_position(" + xpos + "," + ypos + ")[source(self)]");
        try {
            getTS().getAg().delBel(Literal.parseLiteral(str));
        } catch (RevisionFailedException e) {
            e.printStackTrace();
        }
        xpos = we.getPlayerActor().getEntity().getX();
        ypos = we.getPlayerActor().getEntity().getY();
        List<Literal> l = new ArrayList<Literal>();
        l.add(Literal.parseLiteral("user_position(" + xpos + "," + ypos + ")"));
        getTS().getLogger().info("user_position(" + xpos + "," + ypos + ")");
        return l;
    }

    @Override
    public void act(ActionExec action, List<ActionExec> feedback) {
        getTS().getLogger().info("Agent " + getAgName() + " is doing: " + action.getActionTerm());
        if (action.getActionTerm().getFunctor().equals(new String("stand"))) {
        } else if (action.getActionTerm().getFunctor().equals(new String("move"))) {
            String str = new String("own_position(" + action.getActionTerm().getTerm(0) + "," + action.getActionTerm().getTerm(1) + ")[source(self)]");
            try {
                getTS().getAg().delBel(Literal.parseLiteral(str));
            } catch (RevisionFailedException e) {
                getTS().getLogger().info("Agent " + getAgName() + " not able to delete " + str);
                e.printStackTrace();
            }
            String direction = new String(action.getActionTerm().getTerm(2).toString());
            int delta = -1;
            inputController.getInput(direction, delta, ID);
        }
        action.setResult(true);
        feedback.add(action);
    }

    public void setPlayerMove(String direction) {
        playermove = true;
        playerDirection = direction;
    }

    @Override
    public boolean canSleep() {
        return true;
    }

    @Override
    public boolean isRunning() {
        return true;
    }

    @Override
    public void sleep() {
    }

    @Override
    public void sendMsg(jason.asSemantics.Message m) throws Exception {
    }

    @Override
    public void broadcast(jason.asSemantics.Message m) throws Exception {
    }

    @Override
    public void checkMail() {
    }
}
