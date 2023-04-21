package rddl.solver.mdp.rtdp;

import graph.Graph;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;
import dd.discrete.ADDDNode;
import dd.discrete.ADDINode;
import dd.discrete.DD;
import dd.discrete.ADD;
import dd.discrete.ADDNode;
import rddl.*;
import rddl.RDDL.*;
import rddl.policy.Policy;
import rddl.policy.SPerseusSPUDDPolicy;
import rddl.solver.DDUtils;
import rddl.solver.TimeOutException;
import rddl.solver.DDUtils.doubleOutPut;
import rddl.solver.DDUtils.doubleOutPut2;
import rddl.solver.mdp.Action;
import rddl.translate.RDDL2Format;
import util.CString;
import util.Pair;

public class sBRTDP_Avg extends Policy {

    public static int SOLVER_TIME_LIMIT_PER_TURN = 2;

    public static final boolean SHOW_STATE = true;

    public static final boolean SHOW_ACTIONS = true;

    public static final boolean SHOW_ACTION_TAKEN = true;

    public static final boolean DISPLAY_SPUDD_ADDS_GRAPHVIZ = false;

    public static final boolean DISPLAY_SPUDD_ADDS_TEXT = false;

    public RDDL2Format _translation = null;

    public ADD _context;

    public ArrayList<Integer> _allMDPADDs;

    public ArrayList<CString> _alStateVars;

    public ArrayList<CString> _alPrimeStateVars;

    public ArrayList<CString> _alActionNames;

    public int _nContUpperUpdates;

    public HashMap<CString, Action> _hmActionName2Action;

    public int _nRemainingHorizon = -1;

    public Random _rand = new Random();

    public sBRTDP_Avg() {
    }

    public sBRTDP_Avg(String instance_name) {
        super(instance_name);
    }

    public ArrayList<PVAR_INST_DEF> getActions(State s) throws EvalException {
        if (s == null) {
            System.err.println("ERROR: NO STATE/OBS: MDP must have state.");
            System.exit(1);
        }
        TreeSet<CString> true_vars = CString.Convert2CString(SPerseusSPUDDPolicy.getTrueFluents(s, "states"));
        if (SHOW_STATE) {
            System.out.println("\n==============================================");
            System.out.println("\nTrue state variables:");
            for (CString prop_var : true_vars) System.out.println(" - " + prop_var);
        }
        Map<String, ArrayList<PVAR_INST_DEF>> action_map = ActionGenerator.getLegalBoolActionMap(s);
        ArrayList add_state_assign = DDUtils.ConvertTrueVars2DDAssign(_context, true_vars, _alStateVars);
        _csBestActionInitState = null;
        doRTDP(SOLVER_TIME_LIMIT_PER_TURN, add_state_assign);
        String action_taken = (_csBestActionInitState == null ? null : _csBestActionInitState._string);
        if (action_taken == null) {
            ArrayList<String> actions = new ArrayList<String>(action_map.keySet());
            action_taken = actions.get(_rand.nextInt(actions.size()));
            if (SHOW_ACTION_TAKEN) System.out.println("\n--> [Random] action taken: " + action_taken);
        } else if (SHOW_ACTION_TAKEN) System.out.println("\n--> [RTDP] best action taken: " + action_taken);
        System.out.println("Number of Vupper Updates:" + _nContUpperUpdates);
        --_nRemainingHorizon;
        return action_map.get(action_taken);
    }

    public void roundInit(double time_left, int horizon, int round_number, int total_rounds) {
        System.out.println("\n*********************************************************");
        System.out.println(">>> ROUND INIT " + round_number + "/" + total_rounds + "; time remaining = " + time_left + ", horizon = " + horizon);
        System.out.println("*********************************************************");
        _nRemainingHorizon = horizon;
        _nContUpperUpdates = 0;
        if (_translation == null) {
            try {
                _translation = new RDDL2Format(_rddl, _sInstanceName, RDDL2Format.SPUDD_CURR, "");
            } catch (Exception e) {
                System.err.println("Could not construct MDP for: " + _sInstanceName + "\n" + e);
                e.printStackTrace(System.err);
                System.exit(1);
            }
            _allMDPADDs = new ArrayList<Integer>();
            _context = _translation._context;
            _alStateVars = new ArrayList<CString>();
            _alPrimeStateVars = new ArrayList<CString>();
            for (String s : _translation._alStateVars) {
                _alStateVars.add(new CString(s));
                _alPrimeStateVars.add(new CString(s + "'"));
            }
            _alActionNames = new ArrayList<CString>();
            for (String a : _translation._hmActionMap.keySet()) _alActionNames.add(new CString(a));
            _hmActionName2Action = new HashMap<CString, Action>();
            for (String a : _translation._hmActionMap.keySet()) {
                HashMap<CString, Integer> cpts = new HashMap<CString, Integer>();
                int reward = _context.getConstantNode(0d);
                ArrayList<Integer> reward_summands = _translation._act2rewardDD.get(a);
                for (int summand : reward_summands) reward = _context.applyInt(reward, summand, ADD.ARITH_SUM);
                _allMDPADDs.add(reward);
                for (String s : _translation._alStateVars) {
                    int dd = _translation._var2transDD.get(new Pair(a, s));
                    int dd_true = _context.getVarNode(s + "'", 0d, 1d);
                    dd_true = _context.applyInt(dd_true, dd, ADD.ARITH_PROD);
                    int dd_false = _context.getVarNode(s + "'", 1d, 0d);
                    int one_minus_dd = _context.applyInt(_context.getConstantNode(1d), dd, ADD.ARITH_MINUS);
                    dd_false = _context.applyInt(dd_false, one_minus_dd, ADD.ARITH_PROD);
                    int cpt = _context.applyInt(dd_true, dd_false, ADD.ARITH_SUM);
                    cpts.put(new CString(s + "'"), cpt);
                    _allMDPADDs.add(cpt);
                }
                CString action_name = new CString(a);
                Action action = new Action(_context, action_name, cpts, reward);
                _hmActionName2Action.put(action_name, action);
            }
            if (DISPLAY_SPUDD_ADDS_TEXT) {
                System.out.println("State variables: " + _alStateVars);
                System.out.println("Action names: " + _alActionNames);
                for (CString a : _alActionNames) {
                    Action action = _hmActionName2Action.get(a);
                    System.out.println("Content of action '" + a + "'\n" + action);
                }
            }
            final int MAX_DISPLAY = 10;
            if (DISPLAY_SPUDD_ADDS_GRAPHVIZ) {
                int displayed = 0;
                for (CString a : _alActionNames) {
                    Action action = _hmActionName2Action.get(a);
                    for (CString var : _alStateVars) {
                        _context.getGraph(action._hmStateVar2CPT.get(var)).launchViewer();
                        if (++displayed >= MAX_DISPLAY) break;
                    }
                    _context.getGraph(action._reward).launchViewer();
                    if (++displayed >= MAX_DISPLAY) break;
                }
            }
            resetSolver();
        }
        _nRemainingHorizon = _nHorizon;
    }

    public void roundEnd(double reward) {
        System.out.println("\n*********************************************************");
        System.out.println(">>> ROUND END, reward = " + reward);
        System.out.println("*********************************************************");
    }

    public void sessionEnd(double total_reward) {
        System.out.println("\n*********************************************************");
        System.out.println(">>> SESSION END, total reward = " + total_reward);
        System.out.println("*********************************************************");
    }

    public static final int VERBOSE_LEVEL = 0;

    public static final boolean ALWAYS_FLUSH = false;

    public static final double FLUSH_PERCENT_MINIMUM = 0.3d;

    public static final DecimalFormat _df = new DecimalFormat("#.###");

    public long _lStartTime;

    public int _nTimeLimitSecs;

    public static Runtime RUNTIME = Runtime.getRuntime();

    public INSTANCE _rddlInstance = null;

    public int _VUpper, _VLower, _VGap;

    public int _nDDType;

    public int _nTrials;

    public double _dRewardRange;

    public double _dMinRewardRange;

    public double _dDiscount;

    public ArrayList _InitialState;

    public double gapInitial;

    public CString _csBestActionInitState = null;

    public double _Tau;

    public Boolean _firstTime;

    public int _nHorizon;

    public HashMap<Integer, Pair<Pair<Double, Integer>, Pair<Double, Integer>>> _hmNodeWeight;

    public HashMap<Integer, Pair<Double, Double>> _hmNodeWeight2;

    public HashMap<Integer, Integer> _hmPrimeVarID2VarID;

    private double maxUpperUpdated, maxLowerUpdated;

    public void resetSolver() {
        _nTrials = -1;
        _rddlInstance = _rddl._tmInstanceNodes.get(this._sInstanceName);
        if (_rddlInstance == null) {
            System.err.println("ERROR: Could not fine RDDL instance '" + _rddlInstance + "'");
            System.exit(1);
        }
        _dDiscount = _rddlInstance._dDiscount;
        if (_dDiscount == 1d) _dDiscount = 0.99d;
        _nHorizon = _rddlInstance._nHorizon;
        _Tau = 40;
        _hmPrimeVarID2VarID = new HashMap<Integer, Integer>();
        for (Map.Entry<String, String> me : _translation._hmPrimeRemap.entrySet()) {
            String var = me.getKey();
            String var_prime = me.getValue();
            Integer var_id = (Integer) _context._hmVarName2ID.get(var);
            Integer var_prime_id = (Integer) _context._hmVarName2ID.get(var_prime);
            if (var_id == null || var_prime_id == null) {
                System.err.println("ERROR: Could not get var IDs " + var_id + "/" + var_prime_id + " for " + var + "/" + var_prime);
                System.exit(1);
            }
            _hmPrimeVarID2VarID.put(var_prime_id, var_id);
        }
        _dRewardRange = -Double.MAX_VALUE;
        _dMinRewardRange = Double.MAX_VALUE;
        for (Action a : _hmActionName2Action.values()) {
            double MinValue = _context.getMinValue(a._reward);
            _dRewardRange = Math.max(_dRewardRange, _context.getMaxValue(a._reward) - MinValue);
            _dMinRewardRange = Math.min(_dMinRewardRange, MinValue);
        }
        double value_range = (_dDiscount < 1d) ? _dRewardRange / (1d - _dDiscount) : _nHorizon * _dRewardRange;
        double min_value_range = (_dDiscount < 1d) ? _dMinRewardRange / (1d - _dDiscount) : _nHorizon * _dMinRewardRange;
        _VUpper = _context.getConstantNode(value_range);
        _VLower = _context.getConstantNode(min_value_range);
        _VGap = _context.getConstantNode(value_range - min_value_range);
        _hmNodeWeight = new HashMap<Integer, Pair<Pair<Double, Integer>, Pair<Double, Integer>>>();
    }

    private ArrayList remapWithPrimes(ArrayList nextState) {
        ArrayList state = new ArrayList();
        for (int j = 0; j < nextState.size(); j++) state.add(null);
        for (int i = 0; i < nextState.size(); i++) {
            Object val = nextState.get(i);
            if (val != null) {
                int id = (Integer) _context._alOrder.get(i);
                int idprime = (Integer) _context._hmGVarToLevel.get(_context._hmVarName2ID.get(_translation._hmPrimeRemap.get(_context._hmID2VarName.get(id))));
                state.set(idprime, val);
            }
        }
        return state;
    }

    private ArrayList samplingVGap(Integer beginF, HashMap<Integer, Integer> iD2ADD, ArrayList state) {
        ArrayList assign = new ArrayList();
        for (int i = 0; i <= _context._alOrder.size(); i++) assign.add(null);
        Integer F = beginF;
        for (CString s : _alStateVars) {
            double ran = _random.nextDouble();
            Integer index = (Integer) _context._hmVarName2ID.get(s._string);
            Integer level = (Integer) _context._hmGVarToLevel.get(index);
            ADDNode Node = _context.getNode(F);
            if (Node instanceof ADDINode) {
                ADDINode intNodeKey = (ADDINode) Node;
                Integer Fvar = intNodeKey._nTestVarID;
                if (Fvar.compareTo(index) == 0) {
                    String new_str = (String) _translation._hmPrimeRemap.get(s._string);
                    Integer new_id = null;
                    if (new_str == null) new_id = intNodeKey._nTestVarID; else new_id = (Integer) _context._hmVarName2ID.get(new_str);
                    Integer cpt_a_xiprime = iD2ADD.get(new_id);
                    if (cpt_a_xiprime == null) {
                        System.out.println("Prime var not found");
                        System.exit(1);
                    }
                    int level_prime = (Integer) _context._hmGVarToLevel.get(new_id);
                    state.set(level_prime, true);
                    double probTrue = _context.evaluate(cpt_a_xiprime, state);
                    state.set(level_prime, null);
                    double probFalse = 1 - probTrue;
                    Pair<Pair<Double, Integer>, Pair<Double, Integer>> pair = _hmNodeWeight.get(F);
                    double wH = pair._o1._o1 * probTrue;
                    double wL = pair._o2._o1 * probFalse;
                    Boolean condition = ran > (wL / (wH + wL));
                    assign.set(level, condition);
                    if (condition) F = intNodeKey._nHigh; else F = intNodeKey._nLow;
                } else assign.set(level, ran > 0.5);
            } else assign.set(level, ran > 0.5);
        }
        return assign;
    }

    private void updateVLower(ArrayList state) {
        double max = Double.NEGATIVE_INFINITY;
        for (CString actionName : _alActionNames) {
            double Qt = getQValue(_VLower, state, actionName);
            max = Math.max(max, Qt);
        }
        _VLower = DDUtils.UpdateValue(_context, _VLower, state, max);
        maxLowerUpdated = max;
    }

    public void doRTDP(int time_limit_secs, ArrayList init_state) {
        System.out.println("RTDP: Time limit: " + _nTimeLimitSecs + " seconds, discount: " + _dDiscount + ", horizon: " + _nRemainingHorizon + "/" + _nHorizon);
        _nTimeLimitSecs = time_limit_secs;
        _lStartTime = System.currentTimeMillis();
        _InitialState = init_state;
        try {
            _nTrials = 0;
            while (true) {
                doRTDPTrial(_nRemainingHorizon, init_state);
                _nTrials++;
            }
        } catch (TimeOutException e) {
            System.out.println("RTDP: TIME LIMIT EXCEEDED after " + _nTrials + " trials.");
        } catch (Exception e) {
            System.err.println("RTDP: ERROR at " + _nTrials + " trials.");
            e.printStackTrace(System.err);
            System.exit(1);
        } finally {
            System.out.println("RTDP: Vfun at trial " + _nTrials + ": " + _context.countExactNodes(_VUpper) + " nodes, best action: " + _csBestActionInitState);
        }
    }

    public void doRTDPTrial(int trial_depth, ArrayList init_state) throws TimeOutException {
        _firstTime = true;
        CString best_action = null;
        ArrayList cur_state = init_state;
        ArrayList<ArrayList> visited_states = new ArrayList<ArrayList>(_nRemainingHorizon);
        for (int steps_to_go = _nRemainingHorizon; steps_to_go > 0 && cur_state != null; steps_to_go--) {
            flushCaches();
            checkTimeLimit();
            visited_states.add(cur_state);
            QUpdateResult resU = getBestQValue(cur_state, _VUpper);
            QUpdateResult resL = getBestQValue(cur_state, _VLower);
            if (best_action == null) {
                _csBestActionInitState = resU._csBestAction;
                best_action = _csBestActionInitState;
            }
            _VUpper = DDUtils.UpdateValue(_context, _VUpper, cur_state, resU._dBestQValue);
            _nContUpperUpdates++;
            _VLower = DDUtils.UpdateValue(_context, _VLower, cur_state, resL._dBestQValue);
            Iterator<CString> it = _alStateVars.iterator();
            doubleOutPut2 weight = new doubleOutPut2(0d, 0);
            _VGap = DDUtils.insertValueInDD2(_VGap, cur_state, resU._dBestQValue - resL._dBestQValue, it, _translation._hmPrimeRemap, _context, _hmActionName2Action.get(resU._csBestAction)._hmVarID2CPT, _hmNodeWeight, weight);
            cur_state = sampleNextState(cur_state, resU._csBestAction, weight._dWeight);
        }
        for (int depth = visited_states.size() - 1; depth >= 0; depth--) {
            flushCaches();
            checkTimeLimit();
            cur_state = visited_states.get(depth);
            QUpdateResult resU = getBestQValue(cur_state, _VUpper);
            QUpdateResult resL = getBestQValue(cur_state, _VLower);
            _VUpper = DDUtils.UpdateValue(_context, _VUpper, cur_state, resU._dBestQValue);
            _nContUpperUpdates++;
            _VLower = DDUtils.UpdateValue(_context, _VLower, cur_state, resL._dBestQValue);
            if (depth == 0) _csBestActionInitState = resL._csBestAction;
        }
    }

    public static class QUpdateResult {

        public CString _csBestAction;

        public double _dBestQValue;

        public QUpdateResult(CString best_action, double best_qvalue) {
            _csBestAction = best_action;
            _dBestQValue = best_qvalue;
        }
    }

    public QUpdateResult getBestQValue(ArrayList cur_state, int _Value) {
        int prime_vfun = _context.remapGIDsInt(_Value, _translation._hmPrimeRemap);
        QUpdateResult result = new QUpdateResult(null, -Double.MAX_VALUE);
        for (Map.Entry<CString, Action> me : _hmActionName2Action.entrySet()) {
            Action a = me.getValue();
            double qvalue = getQValue(prime_vfun, cur_state, a._csActionName);
            if (result._csBestAction == null || qvalue > result._dBestQValue) {
                result._csBestAction = a._csActionName;
                result._dBestQValue = qvalue;
            }
        }
        return result;
    }

    public double getQValue(int prime_vfun, ArrayList cur_state, CString action) {
        Action a = _hmActionName2Action.get(action);
        Set vfun_gids = _context.getGIDs(prime_vfun);
        if (VERBOSE_LEVEL >= 1) {
            System.out.println("Regressing action: " + a._csActionName + "\nGIDs: " + vfun_gids);
            System.out.println("Before sum out: " + _context.printNode(prime_vfun));
        }
        for (Map.Entry<Integer, Integer> me : a._hmVarID2CPT.entrySet()) {
            int head_var_gid = me.getKey();
            int cpt_dd = me.getValue();
            if (!vfun_gids.contains(head_var_gid)) {
                if (VERBOSE_LEVEL >= 1) {
                    System.out.println("Skipping " + _context._hmID2VarName.get(head_var_gid) + " / " + head_var_gid);
                    System.out.println("... not in " + vfun_gids);
                }
                continue;
            }
            int level_prime = (Integer) _context._hmGVarToLevel.get(head_var_gid);
            cur_state.set(level_prime, true);
            double prob_true = _context.evaluate(cpt_dd, cur_state);
            if (Double.isNaN(prob_true)) {
                System.err.println("ERROR in RTDP.sampleNextState: Expected single value when evaluating: " + cur_state);
                System.exit(1);
            }
            cur_state.set(level_prime, null);
            int restricted_cpt_dd = _context.getVarNode(head_var_gid, 1d - prob_true, prob_true);
            if (VERBOSE_LEVEL >= 2) System.out.println("  - Summing out: " + _context._hmID2VarName.get(head_var_gid));
            prime_vfun = _context.applyInt(prime_vfun, restricted_cpt_dd, DD.ARITH_PROD);
            prime_vfun = _context.opOut(prime_vfun, head_var_gid, DD.ARITH_SUM);
        }
        if (VERBOSE_LEVEL >= 1) System.out.println("After sum out: " + _context.printNode(prime_vfun));
        double exp_future_val = _context.evaluate(prime_vfun, (ArrayList) null);
        double reward = _context.evaluate(a._reward, cur_state);
        return reward + _dDiscount * exp_future_val;
    }

    public Double setProbWeightVGap(Integer F, ArrayList state, HashMap<Integer, Integer> iD2ADD) {
        ADDNode Node = _context.getNode(F);
        if (Node instanceof ADDDNode) {
            return _context.evaluate(F, (ArrayList) null);
        }
        if (!_hmNodeWeight2.containsKey(F)) {
            ADDINode intNodeKey = (ADDINode) Node;
            Double highWeight = setProbWeightVGap(intNodeKey._nHigh, state, iD2ADD);
            Double lowWeight = setProbWeightVGap(intNodeKey._nLow, state, iD2ADD);
            String old_str = (String) _context._hmID2VarName.get(intNodeKey._nTestVarID);
            String new_str = (String) _translation._hmPrimeRemap.get(old_str);
            Integer new_id = null;
            if (new_str == null) new_id = intNodeKey._nTestVarID; else new_id = (Integer) _context._hmVarName2ID.get(new_str);
            Integer cpt_a_xiprime = iD2ADD.get(new_id);
            if (cpt_a_xiprime == null) {
                System.out.println("Prime var not found");
                System.exit(1);
            }
            int level_prime = (Integer) _context._hmGVarToLevel.get(new_id);
            state.set(level_prime, true);
            double probTrue = _context.evaluate(cpt_a_xiprime, state);
            state.set(level_prime, null);
            double probFalse = 1 - probTrue;
            double weightH = probTrue * (highWeight);
            double weightL = probFalse * (lowWeight);
            _hmNodeWeight2.put(F, new Pair<Double, Double>(weightH, weightL));
            return weightH + weightL;
        } else {
            Pair<Double, Double> pair = _hmNodeWeight2.get(F);
            return pair._o1 + pair._o2;
        }
    }

    public ArrayList sampleNextState(ArrayList current_state, CString action, Double B) {
        Action a = _hmActionName2Action.get(action);
        ADDNode Node = _context.getNode(_VGap);
        if (Node instanceof ADDINode) {
            if (_firstTime) {
                gapInitial = _context.evaluate((Integer) _VGap, _InitialState);
                _firstTime = false;
            }
            if (B < gapInitial / _Tau) {
                return null;
            }
        }
        return samplingVGap(_VGap, a._hmVarID2CPT, current_state);
    }

    public void flushCaches() {
        if (!ALWAYS_FLUSH && ((double) RUNTIME.freeMemory() / (double) RUNTIME.totalMemory()) > FLUSH_PERCENT_MINIMUM) {
            return;
        }
        _context.clearSpecialNodes();
        for (Integer dd : _allMDPADDs) _context.addSpecialNode(dd);
        _context.addSpecialNode(_VUpper);
        _context.addSpecialNode(_VLower);
        _context.addSpecialNode(_VGap);
        _context.flushCaches(false);
    }

    public void checkTimeLimit() throws TimeOutException {
        double elapsed_time = (System.currentTimeMillis() - _lStartTime) / 1000d;
        if (elapsed_time > (double) _nTimeLimitSecs) throw new TimeOutException("Elapsed time " + elapsed_time + " (s) exceeded time limit of " + _nTimeLimitSecs + " (s)");
    }

    public static String MemDisplay() {
        long total = RUNTIME.totalMemory();
        long free = RUNTIME.freeMemory();
        return total - free + ":" + total;
    }

    public void setLimitTime(Integer time) {
        SOLVER_TIME_LIMIT_PER_TURN = time;
    }

    public int getNumberUpdate() {
        return _nContUpperUpdates;
    }
}
