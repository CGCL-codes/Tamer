package aima.core.environment.vacuum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import aima.core.agent.Action;
import aima.core.agent.Percept;
import aima.core.agent.impl.AbstractAgent;
import aima.core.agent.impl.aprog.TableDrivenAgentProgram;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): Figure 2.3, page 36.<br>
 * <br>
 * Figure 2.3 Partial tabulation of a simple agent function for the
 * vacuum-cleaner world shown in Figure 2.2.
 * 
 * @author Ciaran O'Reilly
 * 
 */
public class TableDrivenVacuumAgent extends AbstractAgent {

    public TableDrivenVacuumAgent() {
        super(new TableDrivenAgentProgram(getPerceptSequenceActions()));
    }

    private static Map<List<Percept>, Action> getPerceptSequenceActions() {
        Map<List<Percept>, Action> perceptSequenceActions = new HashMap<List<Percept>, Action>();
        List<Percept> ps;
        ps = createPerceptSequence(new VacuumEnvPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Clean));
        perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_MOVE_RIGHT);
        ps = createPerceptSequence(new VacuumEnvPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Dirty));
        perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_SUCK);
        ps = createPerceptSequence(new VacuumEnvPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Clean));
        perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_MOVE_LEFT);
        ps = createPerceptSequence(new VacuumEnvPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Dirty));
        perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_SUCK);
        ps = createPerceptSequence(new VacuumEnvPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Clean), new VacuumEnvPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Clean));
        perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_MOVE_RIGHT);
        ps = createPerceptSequence(new VacuumEnvPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Clean), new VacuumEnvPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Dirty));
        perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_SUCK);
        ps = createPerceptSequence(new VacuumEnvPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Clean), new VacuumEnvPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Clean));
        perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_MOVE_LEFT);
        ps = createPerceptSequence(new VacuumEnvPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Clean), new VacuumEnvPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Dirty));
        perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_SUCK);
        ps = createPerceptSequence(new VacuumEnvPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Dirty), new VacuumEnvPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Clean));
        perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_MOVE_RIGHT);
        ps = createPerceptSequence(new VacuumEnvPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Dirty), new VacuumEnvPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Dirty));
        perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_SUCK);
        ps = createPerceptSequence(new VacuumEnvPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Dirty), new VacuumEnvPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Clean));
        perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_MOVE_LEFT);
        ps = createPerceptSequence(new VacuumEnvPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Dirty), new VacuumEnvPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Dirty));
        perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_SUCK);
        ps = createPerceptSequence(new VacuumEnvPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Clean), new VacuumEnvPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Clean));
        perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_MOVE_RIGHT);
        ps = createPerceptSequence(new VacuumEnvPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Clean), new VacuumEnvPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Dirty));
        perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_SUCK);
        ps = createPerceptSequence(new VacuumEnvPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Clean), new VacuumEnvPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Clean));
        perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_MOVE_LEFT);
        ps = createPerceptSequence(new VacuumEnvPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Clean), new VacuumEnvPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Dirty));
        perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_SUCK);
        ps = createPerceptSequence(new VacuumEnvPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Dirty), new VacuumEnvPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Clean));
        perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_MOVE_RIGHT);
        ps = createPerceptSequence(new VacuumEnvPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Dirty), new VacuumEnvPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Dirty));
        perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_SUCK);
        ps = createPerceptSequence(new VacuumEnvPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Dirty), new VacuumEnvPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Clean));
        perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_MOVE_LEFT);
        ps = createPerceptSequence(new VacuumEnvPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Dirty), new VacuumEnvPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Dirty));
        perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_SUCK);
        ps = createPerceptSequence(new VacuumEnvPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Clean), new VacuumEnvPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Clean), new VacuumEnvPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Clean));
        perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_MOVE_RIGHT);
        ps = createPerceptSequence(new VacuumEnvPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Clean), new VacuumEnvPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Clean), new VacuumEnvPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Dirty));
        perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_SUCK);
        ps = createPerceptSequence(new VacuumEnvPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Clean), new VacuumEnvPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Clean), new VacuumEnvPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Clean));
        perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_MOVE_LEFT);
        ps = createPerceptSequence(new VacuumEnvPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Clean), new VacuumEnvPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Clean), new VacuumEnvPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Dirty));
        perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_SUCK);
        ps = createPerceptSequence(new VacuumEnvPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Clean), new VacuumEnvPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Dirty), new VacuumEnvPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Clean));
        perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_MOVE_RIGHT);
        ps = createPerceptSequence(new VacuumEnvPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Clean), new VacuumEnvPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Dirty), new VacuumEnvPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Dirty));
        perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_SUCK);
        ps = createPerceptSequence(new VacuumEnvPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Clean), new VacuumEnvPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Dirty), new VacuumEnvPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Clean));
        perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_MOVE_LEFT);
        ps = createPerceptSequence(new VacuumEnvPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Clean), new VacuumEnvPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Dirty), new VacuumEnvPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Dirty));
        perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_SUCK);
        ps = createPerceptSequence(new VacuumEnvPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Clean), new VacuumEnvPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Clean), new VacuumEnvPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Clean));
        perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_MOVE_RIGHT);
        ps = createPerceptSequence(new VacuumEnvPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Clean), new VacuumEnvPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Clean), new VacuumEnvPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Dirty));
        perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_SUCK);
        ps = createPerceptSequence(new VacuumEnvPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Clean), new VacuumEnvPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Clean), new VacuumEnvPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Clean));
        perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_MOVE_LEFT);
        ps = createPerceptSequence(new VacuumEnvPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Clean), new VacuumEnvPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Clean), new VacuumEnvPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Dirty));
        perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_SUCK);
        ps = createPerceptSequence(new VacuumEnvPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Clean), new VacuumEnvPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Dirty), new VacuumEnvPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Clean));
        perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_MOVE_RIGHT);
        ps = createPerceptSequence(new VacuumEnvPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Clean), new VacuumEnvPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Dirty), new VacuumEnvPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Dirty));
        perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_SUCK);
        ps = createPerceptSequence(new VacuumEnvPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Clean), new VacuumEnvPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Dirty), new VacuumEnvPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Clean));
        perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_MOVE_LEFT);
        ps = createPerceptSequence(new VacuumEnvPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Clean), new VacuumEnvPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Dirty), new VacuumEnvPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Dirty));
        perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_SUCK);
        ps = createPerceptSequence(new VacuumEnvPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Dirty), new VacuumEnvPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Clean), new VacuumEnvPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Clean));
        perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_MOVE_RIGHT);
        ps = createPerceptSequence(new VacuumEnvPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Dirty), new VacuumEnvPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Clean), new VacuumEnvPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Dirty));
        perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_SUCK);
        ps = createPerceptSequence(new VacuumEnvPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Dirty), new VacuumEnvPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Clean), new VacuumEnvPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Clean));
        perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_MOVE_LEFT);
        ps = createPerceptSequence(new VacuumEnvPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Dirty), new VacuumEnvPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Clean), new VacuumEnvPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Dirty));
        perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_SUCK);
        ps = createPerceptSequence(new VacuumEnvPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Dirty), new VacuumEnvPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Dirty), new VacuumEnvPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Clean));
        perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_MOVE_RIGHT);
        ps = createPerceptSequence(new VacuumEnvPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Dirty), new VacuumEnvPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Dirty), new VacuumEnvPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Dirty));
        perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_SUCK);
        ps = createPerceptSequence(new VacuumEnvPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Dirty), new VacuumEnvPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Dirty), new VacuumEnvPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Clean));
        perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_MOVE_LEFT);
        ps = createPerceptSequence(new VacuumEnvPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Dirty), new VacuumEnvPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Dirty), new VacuumEnvPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Dirty));
        perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_SUCK);
        ps = createPerceptSequence(new VacuumEnvPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Dirty), new VacuumEnvPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Clean), new VacuumEnvPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Clean));
        perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_MOVE_RIGHT);
        ps = createPerceptSequence(new VacuumEnvPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Dirty), new VacuumEnvPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Clean), new VacuumEnvPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Dirty));
        perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_SUCK);
        ps = createPerceptSequence(new VacuumEnvPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Dirty), new VacuumEnvPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Clean), new VacuumEnvPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Clean));
        perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_MOVE_LEFT);
        ps = createPerceptSequence(new VacuumEnvPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Dirty), new VacuumEnvPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Clean), new VacuumEnvPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Dirty));
        perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_SUCK);
        ps = createPerceptSequence(new VacuumEnvPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Dirty), new VacuumEnvPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Dirty), new VacuumEnvPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Clean));
        perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_MOVE_RIGHT);
        ps = createPerceptSequence(new VacuumEnvPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Dirty), new VacuumEnvPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Dirty), new VacuumEnvPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Dirty));
        perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_SUCK);
        ps = createPerceptSequence(new VacuumEnvPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Dirty), new VacuumEnvPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Dirty), new VacuumEnvPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Clean));
        perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_MOVE_LEFT);
        ps = createPerceptSequence(new VacuumEnvPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Dirty), new VacuumEnvPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Dirty), new VacuumEnvPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Dirty));
        perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_SUCK);
        ps = createPerceptSequence(new VacuumEnvPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Clean), new VacuumEnvPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Clean), new VacuumEnvPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Clean));
        perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_MOVE_RIGHT);
        ps = createPerceptSequence(new VacuumEnvPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Clean), new VacuumEnvPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Clean), new VacuumEnvPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Dirty));
        perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_SUCK);
        ps = createPerceptSequence(new VacuumEnvPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Clean), new VacuumEnvPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Clean), new VacuumEnvPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Clean));
        perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_MOVE_LEFT);
        ps = createPerceptSequence(new VacuumEnvPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Clean), new VacuumEnvPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Clean), new VacuumEnvPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Dirty));
        perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_SUCK);
        ps = createPerceptSequence(new VacuumEnvPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Clean), new VacuumEnvPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Dirty), new VacuumEnvPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Clean));
        perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_MOVE_RIGHT);
        ps = createPerceptSequence(new VacuumEnvPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Clean), new VacuumEnvPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Dirty), new VacuumEnvPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Dirty));
        perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_SUCK);
        ps = createPerceptSequence(new VacuumEnvPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Clean), new VacuumEnvPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Dirty), new VacuumEnvPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Clean));
        perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_MOVE_LEFT);
        ps = createPerceptSequence(new VacuumEnvPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Clean), new VacuumEnvPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Dirty), new VacuumEnvPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Dirty));
        perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_SUCK);
        ps = createPerceptSequence(new VacuumEnvPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Clean), new VacuumEnvPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Clean), new VacuumEnvPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Clean));
        perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_MOVE_RIGHT);
        ps = createPerceptSequence(new VacuumEnvPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Clean), new VacuumEnvPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Clean), new VacuumEnvPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Dirty));
        perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_SUCK);
        ps = createPerceptSequence(new VacuumEnvPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Clean), new VacuumEnvPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Clean), new VacuumEnvPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Clean));
        perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_MOVE_LEFT);
        ps = createPerceptSequence(new VacuumEnvPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Clean), new VacuumEnvPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Clean), new VacuumEnvPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Dirty));
        perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_SUCK);
        ps = createPerceptSequence(new VacuumEnvPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Clean), new VacuumEnvPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Dirty), new VacuumEnvPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Clean));
        perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_MOVE_RIGHT);
        ps = createPerceptSequence(new VacuumEnvPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Clean), new VacuumEnvPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Dirty), new VacuumEnvPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Dirty));
        perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_SUCK);
        ps = createPerceptSequence(new VacuumEnvPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Clean), new VacuumEnvPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Dirty), new VacuumEnvPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Clean));
        perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_MOVE_LEFT);
        ps = createPerceptSequence(new VacuumEnvPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Clean), new VacuumEnvPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Dirty), new VacuumEnvPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Dirty));
        perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_SUCK);
        ps = createPerceptSequence(new VacuumEnvPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Dirty), new VacuumEnvPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Clean), new VacuumEnvPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Clean));
        perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_MOVE_RIGHT);
        ps = createPerceptSequence(new VacuumEnvPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Dirty), new VacuumEnvPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Clean), new VacuumEnvPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Dirty));
        perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_SUCK);
        ps = createPerceptSequence(new VacuumEnvPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Dirty), new VacuumEnvPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Clean), new VacuumEnvPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Clean));
        perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_MOVE_LEFT);
        ps = createPerceptSequence(new VacuumEnvPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Dirty), new VacuumEnvPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Clean), new VacuumEnvPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Dirty));
        perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_SUCK);
        ps = createPerceptSequence(new VacuumEnvPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Dirty), new VacuumEnvPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Dirty), new VacuumEnvPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Clean));
        perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_MOVE_RIGHT);
        ps = createPerceptSequence(new VacuumEnvPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Dirty), new VacuumEnvPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Dirty), new VacuumEnvPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Dirty));
        perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_SUCK);
        ps = createPerceptSequence(new VacuumEnvPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Dirty), new VacuumEnvPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Dirty), new VacuumEnvPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Clean));
        perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_MOVE_LEFT);
        ps = createPerceptSequence(new VacuumEnvPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Dirty), new VacuumEnvPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Dirty), new VacuumEnvPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Dirty));
        perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_SUCK);
        ps = createPerceptSequence(new VacuumEnvPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Dirty), new VacuumEnvPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Clean), new VacuumEnvPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Clean));
        perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_MOVE_RIGHT);
        ps = createPerceptSequence(new VacuumEnvPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Dirty), new VacuumEnvPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Clean), new VacuumEnvPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Dirty));
        perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_SUCK);
        ps = createPerceptSequence(new VacuumEnvPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Dirty), new VacuumEnvPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Clean), new VacuumEnvPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Clean));
        perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_MOVE_LEFT);
        ps = createPerceptSequence(new VacuumEnvPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Dirty), new VacuumEnvPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Clean), new VacuumEnvPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Dirty));
        perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_SUCK);
        ps = createPerceptSequence(new VacuumEnvPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Dirty), new VacuumEnvPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Dirty), new VacuumEnvPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Clean));
        perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_MOVE_RIGHT);
        ps = createPerceptSequence(new VacuumEnvPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Dirty), new VacuumEnvPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Dirty), new VacuumEnvPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Dirty));
        perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_SUCK);
        ps = createPerceptSequence(new VacuumEnvPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Dirty), new VacuumEnvPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Dirty), new VacuumEnvPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Clean));
        perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_MOVE_LEFT);
        ps = createPerceptSequence(new VacuumEnvPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Dirty), new VacuumEnvPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Dirty), new VacuumEnvPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Dirty));
        perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_SUCK);
        return perceptSequenceActions;
    }

    private static List<Percept> createPerceptSequence(Percept... percepts) {
        List<Percept> perceptSequence = new ArrayList<Percept>();
        for (Percept p : percepts) {
            perceptSequence.add(p);
        }
        return perceptSequence;
    }
}
