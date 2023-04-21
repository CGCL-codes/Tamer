package d2.worldmodel;

import d2.core.D2;
import d2.core.D2Module;
import java.lang.reflect.Constructor;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import org.jdom.Element;
import d2.execution.adaptation.NothingPlanAdaptation;
import d2.execution.adaptation.parameters.ParameterAdaptation;
import d2.execution.planexecution.RealTimePlanExecution;
import d2.execution.planner.NothingPlanner;
import d2.execution.planner.Planner;
import d2.plans.Plan;
import gatech.mmpm.Action;
import gatech.mmpm.ConfigurationException;
import gatech.mmpm.GameState;
import gatech.mmpm.Trace;
import gatech.mmpm.sensor.Sensor;
import gatech.mmpm.util.XMLWriter;
import java.util.HashMap;

/**
 * Define the action and opponent model and the condition matcher of the domain. It has a "two-steps
 * construction", with the constructor receiving no args, and a second method,
 * config that receives the Properties with all the parameters that could be
 * needed by the model.
 */
public class WorldModel extends D2Module {

    private OpponentsModel m_om = new EmptyOpponentsModel();

    private ActionsModel m_am = new EmptyActionsModel();

    private ConditionMatcher m_cm = new ConditionMatcher();

    HashMap<String, Double> m_entityWeights = null;

    int DEBUG = 0;

    public WorldModel() {
    }

    public HashMap<String, Double> getEntityWeights() {
        return m_entityWeights;
    }

    public void setEntityWeights(HashMap<String, Double> entityWeights) {
        m_entityWeights = entityWeights;
    }

    public void config(Properties configuration) throws ConfigurationException {
        String actionModel = configuration.getProperty("action_model");
        String opponentModel = configuration.getProperty("opponent_model");
        String conditionMatcher = configuration.getProperty("condition_matcher");
        if (actionModel != null) {
            try {
                Class c = Class.forName(actionModel);
                Constructor cons = c.getConstructor();
                setActionsModel((ActionsModel) cons.newInstance());
            } catch (Exception e) {
                e.printStackTrace();
                throw new ConfigurationException("'" + actionModel + "' is not a valid actions model.");
            }
        } else {
            setActionsModel(new EmptyActionsModel());
        }
        if (opponentModel != null) {
            try {
                Class c = Class.forName(opponentModel);
                Constructor cons = c.getConstructor();
                setOpponentsModel((OpponentsModel) cons.newInstance());
            } catch (Exception e) {
                e.printStackTrace();
                throw new ConfigurationException("'" + opponentModel + "' is not a valid opponents model.");
            }
        } else {
            setOpponentsModel(new EmptyOpponentsModel());
        }
        if (conditionMatcher != null) {
            try {
                Class c = Class.forName(conditionMatcher);
                Constructor cons = c.getConstructor();
                setConditionMatcher((ConditionMatcher) cons.newInstance());
            } catch (Exception e) {
                e.printStackTrace();
                throw new ConfigurationException("'" + conditionMatcher + "' is not a valid actions model.");
            }
        } else {
            setConditionMatcher(new ConditionMatcher());
        }
    }

    public void setOpponentsModel(OpponentsModel newOM) {
        m_om = newOM;
    }

    public void setActionsModel(ActionsModel newAM) {
        m_am = newAM;
    }

    public void setConditionMatcher(ConditionMatcher newCM) {
        m_cm = newCM;
    }

    public ActionsModel getActionsModel() {
        return m_am;
    }

    public OpponentsModel getOpponentsModel() {
        return m_om;
    }

    public ConditionMatcher getConditionMatcher() {
        return m_cm;
    }

    public void learnFromTraces(List<String> trace_file_names) {
        int count = 0;
        for (count = 0; count < trace_file_names.size(); count++) {
            String filename = trace_file_names.get(count);
            Trace trace = gatech.mmpm.tracer.TraceParser.parse(filename, d2.core.Config.getDomain());
            if (trace != null) {
                trace.setTraceID(count);
                if (m_am != null) {
                    m_am.learn(trace);
                }
                if (m_om != null) {
                    m_om.learn(trace);
                }
            } else {
                System.err.println("Failed to load trace in " + filename);
            }
        }
    }

    public void evaluateWithTrace(Trace dt) {
        m_am.evaluateWithTrace(dt);
        m_om.evaluateWithTrace(dt);
    }

    public GameState simulate(int cycle, GameState gs, int cycles, int granularity, String player, Sensor winGamegoal) {
        Set<String> playerNames = gs.getAllPlayers();
        for (int i = cycle; i < cycle + cycles; i += granularity) {
            double val = (Float) winGamegoal.evaluate(i, gs, player);
            if (val == 0.0 || val == 1.0) {
                return gs;
            } else {
                List<Action> newActions = m_om.predictActions(i, gs, playerNames);
                gs = m_am.simulate(gs, newActions, granularity);
            }
        }
        return gs;
    }

    public GameState simulate(int cycle, GameState a_gs, Plan plan, String playerID, int cycles, int granularity, Sensor winGamegoal) {
        GameState working_gs = (GameState) a_gs.clone();
        Planner pe = new NothingPlanner(plan, null, playerID, new RealTimePlanExecution(playerID, new ParameterAdaptation(), new NothingPlanAdaptation(), null), null);
        List<Action> newActions = new LinkedList<Action>();
        Set<String> opponentNames = null;
        boolean planFinished = false;
        int planFinishedMargin = 64;
        if (DEBUG >= 1) {
            System.out.println("WorldModel.simulate, game state:");
            System.out.println(a_gs);
            System.out.println("WorldModel.simulate, plan::");
            Planner.printPlanNice(plan);
        }
        for (int i = cycle; i < cycle + cycles && !planFinished; i += granularity) {
            double val = (winGamegoal != null ? (Float) winGamegoal.evaluate(i, working_gs, playerID) : 0.5);
            if (val == 0.0 || val == 1.0) {
                return working_gs;
            } else {
                opponentNames = working_gs.getAllPlayers();
                opponentNames.remove(playerID);
                if (DEBUG >= 1) {
                    System.out.println("simulate: starting cycle " + i);
                }
                newActions.clear();
                if (pe.execute(newActions, i, working_gs, playerID)) {
                    planFinishedMargin -= granularity;
                    if (planFinishedMargin < 0) {
                        planFinished = true;
                    }
                }
                if (DEBUG >= 1) {
                    System.out.println("simulate: predicting opponent actions...");
                }
                List<Action> opponentsActions = m_om.predictActions(i, working_gs, opponentNames);
                if (opponentsActions != null) {
                    newActions.addAll(opponentsActions);
                }
                if (DEBUG >= 1) {
                    System.out.println("simulate: actions: " + newActions);
                }
                working_gs = m_am.simulate(working_gs, newActions, granularity);
                if (DEBUG >= 1) {
                    System.out.println("simulate: cycle " + i + " finished.");
                }
            }
        }
        if (DEBUG >= 1) {
            System.out.println("WorldModel.simulate(end), game state:");
            System.out.println(working_gs);
            System.out.println("WorldModel.simulate(end), plan::");
            Planner.printPlanNice(plan);
        }
        return working_gs;
    }

    /**
     * This method does exactly the same as 'simulate' but returns the sequence
     * of game states in a list so that it can be inspected.
     *
     * @param cycle
     * @param a_gs
     * @param plan
     * @param playerID
     * @param cycles
     * @param granularity
     * @param winGamegoal
     * @return
     */
    public List<GameState> logSimulation(int cycle, GameState a_gs, Plan plan, String playerID, int cycles, int granularity, Sensor winGamegoal) {
        GameState working_gs = (GameState) a_gs.clone();
        Planner pe = new NothingPlanner(plan, null, playerID, new RealTimePlanExecution(playerID, new ParameterAdaptation(), new NothingPlanAdaptation(), null), null);
        List<Action> newActions = new LinkedList<Action>();
        Set<String> opponentNames = null;
        boolean planFinished = false;
        List<GameState> log = new LinkedList<GameState>();
        int planFinishedMargin = 64;
        log.add((GameState) working_gs.clone());
        for (int i = cycle; i < cycle + cycles && !planFinished; i += granularity) {
            double val = (winGamegoal != null ? (Float) winGamegoal.evaluate(i, working_gs, playerID) : 0.5);
            if (val == 0.0 || val == 1.0) {
                return log;
            } else {
                opponentNames = working_gs.getAllPlayers();
                opponentNames.remove(playerID);
                newActions.clear();
                if (pe.execute(newActions, i, working_gs, playerID)) {
                    planFinishedMargin -= granularity;
                    if (planFinishedMargin < 0) {
                        planFinished = true;
                    }
                }
                List<Action> opponentsActions = m_om.predictActions(i, working_gs, opponentNames);
                if (opponentsActions != null) {
                    newActions.addAll(opponentsActions);
                }
                working_gs = m_am.simulate(working_gs, newActions, granularity);
                log.add((GameState) working_gs.clone());
            }
        }
        return log;
    }

    public static WorldModel loadfromXML(Element e) {
        if (e != null) {
            WorldModel wm = new WorldModel();
            java.util.Properties config = new java.util.Properties();
            config.setProperty("action_model", e.getChildText("action-model"));
            config.setProperty("opponent_model", e.getChildText("opponent-model"));
            config.setProperty("condition_matcher", e.getChildText("condition-matcher"));
            try {
                wm.config(config);
            } catch (ConfigurationException e1) {
                e1.printStackTrace();
            }
            return wm;
        }
        return null;
    }

    public void savetoXML(XMLWriter w) {
        w.tag("world-model");
        w.tag("action-model", m_am.getClass().getName());
        w.tag("opponent-model", m_om.getClass().getName());
        w.tag("condition-matcher", m_cm.getClass().getName());
        w.tag("/world-model");
    }

    public String toString() {
        return "Opponents Model:\n" + m_om + "\nActions Model:\n" + m_am + "\nCondition Matcher:\n" + m_cm;
    }
}
