package statechum.analysis.learning;

import com.ericsson.otp.erlang.OtpErlangAtom;
import com.ericsson.otp.erlang.OtpErlangList;
import com.ericsson.otp.erlang.OtpErlangObject;
import com.ericsson.otp.erlang.OtpErlangString;
import com.ericsson.otp.erlang.OtpErlangTuple;
import java.util.*;
import java.awt.Frame;
import java.io.IOException;
import statechum.analysis.Erlang.ErlangLabel;
import statechum.analysis.Erlang.ErlangModule;
import statechum.analysis.Erlang.ErlangRunner;
import statechum.analysis.learning.ErlangOracleLearner.TraceOutcome.TRACEOUTCOME;
import statechum.analysis.learning.observers.ProgressDecorator.LearnerEvaluationConfiguration;
import statechum.Helper;
import statechum.Label;
import statechum.Pair;
import statechum.analysis.learning.rpnicore.LearnerGraph;
import statechum.model.testset.PTASequenceEngine;

/**
 * 
 * @author ramsay
 */
public class ErlangOracleLearner extends RPNIUniversalLearner {

    protected ErlangModule module;

    public ErlangModule getModule() {
        return module;
    }

    protected LearnerEvaluationConfiguration myEvalCnf;

    public ErlangOracleLearner(Frame parent, LearnerEvaluationConfiguration evalCnf) {
        super(parent, evalCnf);
        if (config.getErlangSourceFile() != null) {
            try {
                module = ErlangModule.loadModule(config, true);
            } catch (IOException e) {
                Helper.throwUnchecked("Failed to load trace file " + config.getErlangSourceFile(), e);
            }
        }
        myEvalCnf = evalCnf;
        configErlang();
    }

    public void configErlang() {
        ErlangRunner.getRunner().configurationToErlang(myEvalCnf.config);
        ErlangRunner.getRunner().call(new OtpErlangObject[] { new OtpErlangAtom("addPath"), new OtpErlangString(module.sourceFolder.getAbsolutePath()) }, "addPath");
    }

    public void finished() {
        ErlangRunner.getRunner().call(new OtpErlangObject[] { new OtpErlangAtom("delPath"), new OtpErlangString(module.sourceFolder.getAbsolutePath()) }, "delPath");
    }

    @Override
    public LearnerGraph learnMachine() {
        LearnerGraph result = super.learnMachine();
        finished();
        return result;
    }

    /**
	 * We often need to add all possible failed outputs for each input rejected
	 * by Erlang, this collection holds all those outputs which correspond to a
	 * specific input.
	 */
    protected Map<Label, Set<Label>> inputToPossibleOutputs = new TreeMap<Label, Set<Label>>();

    protected void updateInputToPossibleOutputs(Label label) {
        Label inputPortionOfLabel = stripOutput((ErlangLabel) label);
        Set<Label> outputsSeenForThisInput = inputToPossibleOutputs.get(inputPortionOfLabel);
        if (outputsSeenForThisInput == null) {
            outputsSeenForThisInput = new TreeSet<Label>();
            inputToPossibleOutputs.put(inputPortionOfLabel, outputsSeenForThisInput);
        }
        outputsSeenForThisInput.add(label);
        if (!module.behaviour.getAlphabet().contains(label)) module.behaviour.addToAlphabet((ErlangLabel) label);
    }

    @Override
    public Pair<Integer, String> CheckWithEndUser(@SuppressWarnings("unused") LearnerGraph model, final List<Label> question, @SuppressWarnings("unused") final int expectedForNoRestart, @SuppressWarnings("unused") final List<Boolean> consistentFacts, @SuppressWarnings("unused") final PairScore pairBeingMerged, @SuppressWarnings("unused") final Object[] moreOptions) {
        TraceOutcome outcome = askErlang(question);
        StringBuffer response = null;
        switch(outcome.outcome) {
            case TRACE_DIFFERENTOUTPUT:
                if (outcome.answerDetails.length > 0) {
                    response = new StringBuffer();
                    response.append("- [");
                    response.append(RPNILearner.questionToString(question));
                    response.append("] ");
                    response.append("+ [");
                    response.append(RPNILearner.questionToString(Arrays.asList(outcome.answerDetails)));
                    response.append("] ");
                    updateInputToPossibleOutputs(outcome.answerDetails[outcome.answerDetails.length - 1]);
                }
                break;
            case TRACE_OK:
                break;
            case TRACE_FAIL:
                response = new StringBuffer();
                int prefixLen = outcome.answerDetails.length - 1;
                List<Label> prefix = question.subList(0, prefixLen);
                Label failedLabel = question.get(prefixLen);
                response.append("- [");
                boolean first = true;
                ErlangLabel noop = stripOutput((ErlangLabel) failedLabel);
                for (Label lbl : inputToPossibleOutputs.get(noop)) {
                    if (!first) response.append(','); else first = false;
                    List<Label> failedSequence = new LinkedList<Label>(prefix);
                    failedSequence.add(lbl);
                    response.append(RPNILearner.questionToString(failedSequence));
                }
                response.append(" ]");
        }
        Pair<Integer, String> result = null;
        if (response != null) {
            result = new Pair<Integer, String>(AbstractOracle.USER_NEWTRACE, response.toString());
        } else {
            result = new Pair<Integer, String>(AbstractOracle.USER_ACCEPTED, null);
        }
        return result;
    }

    /**
	 * Erlang states are represented by traces used to enter those states. The
	 * reason for this is that we cannot re-enter states in an Erlang engine
	 * without re-running traces, hence the only realistic way is to associate
	 * states with paths.
	 */
    public static class ErlangState implements Comparable<ErlangState> {

        public final ErlangState previousState;

        public final Label inputToThisState;

        public final boolean accept;

        /** Inputs which have been rejected from this state. */
        public final Set<Label> rejects = new TreeSet<Label>();

        private final int ident;

        /** ErlangState id. */
        private static int ErlangStateId = 0;

        public static synchronized ErlangState newErlangState(ErlangState prev, Label label, boolean a) {
            return new ErlangState(ErlangStateId++, prev, label, a);
        }

        private ErlangState(int id, ErlangState prev, Label label, boolean a) {
            previousState = prev;
            ident = id;
            inputToThisState = label;
            accept = a;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ident;
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null) return false;
            if (!(obj instanceof ErlangState)) return false;
            ErlangState other = (ErlangState) obj;
            if (ident != other.ident) return false;
            return true;
        }

        @Override
        public int compareTo(ErlangState o) {
            return ident - o.ident;
        }
    }

    ErlangState initialState = null;

    /**
	 * The purpose of this class is to explore Erlang state machine without
	 * having to think about sequences which may lead somewhere or not lead
	 * anywhere at all.
	 */
    protected class ErlangMachine implements PTASequenceEngine.FSMAbstraction {

        @Override
        public Object getNextState(Object currentState, Label input) {
            assert input instanceof ErlangLabel;
            ErlangState state = (ErlangState) currentState;
            Label inputPortionOfLabel = stripOutput((ErlangLabel) input);
            if (state.rejects.contains(inputPortionOfLabel)) return null;
            if (state.rejects.contains(input)) return null;
            LinkedList<Label> newTrace = new LinkedList<Label>();
            newTrace.addFirst(input);
            ErlangState nextState = state;
            int traceLength = 1;
            while (nextState != initialState) {
                ++traceLength;
                newTrace.addFirst(nextState.inputToThisState);
                nextState = nextState.previousState;
            }
            TraceOutcome outcome = askErlang(newTrace);
            switch(outcome.outcome) {
                case TRACE_OK:
                    assert outcome.answerDetails.length == traceLength;
                    nextState = ErlangState.newErlangState(state, input, true);
                    break;
                case TRACE_DIFFERENTOUTPUT:
                    assert outcome.answerDetails.length == traceLength;
                    ErlangLabel nextLabel = outcome.answerDetails[traceLength - 1];
                    state.rejects.add(input);
                    if (!module.behaviour.getAlphabet().contains(nextLabel)) {
                        module.behaviour.getAlphabet().add(nextLabel);
                    }
                    nextState = null;
                    break;
                case TRACE_FAIL:
                    state.rejects.add(inputPortionOfLabel);
                    nextState = null;
                    break;
                default:
                    assert false;
            }
            return nextState;
        }

        @Override
        public Object getInitState() {
            if (initialState == null) {
                TraceOutcome outcome = askErlang(new LinkedList<Label>());
                assert outcome.outcome == TRACEOUTCOME.TRACE_OK;
                initialState = ErlangState.newErlangState(null, null, true);
            }
            return initialState;
        }

        @Override
        public boolean isAccept(Object currentState) {
            return ((ErlangState) currentState).accept;
        }

        /**
		 * This method should not be called.
		 * 
		 * @see statechum.model.testset.PTASequenceEngine.FSMAbstraction#setAccept(java.lang.Object,
		 *      boolean)
		 */
        @Override
        public void setAccept(@SuppressWarnings("unused") Object currentState, @SuppressWarnings("unused") boolean value) {
            throw new UnsupportedOperationException("this method should not be called");
        }

        @Override
        public boolean shouldBeReturned(Object elem) {
            if (elem == null) return false;
            return ((ErlangState) elem).accept;
        }
    }

    /** Starts the learning process by generating a number of traces. */
    public void GenerateInitialTraces() {
        topLevelListener.init(GenerateInitialTraces(5), 0, 0);
    }

    public PTASequenceEngine GenerateInitialTraces(int wavecount) {
        PTASequenceEngine engine = new PTASequenceEngine();
        engine.init(new ErlangMachine());
        PTASequenceEngine.SequenceSet seq = engine.new SequenceSet();
        seq.setIdentity();
        PTASequenceEngine.SequenceSet seqNext = null;
        for (int waveNo = 0; waveNo < wavecount; ++waveNo) {
            LinkedHashSet<Label> currentAlphabet = new LinkedHashSet<Label>(module.behaviour.getAlphabet());
            seqNext = seq.crossWithSet(currentAlphabet);
            if (currentAlphabet.size() < module.behaviour.getAlphabet().size()) {
                LinkedHashSet<Label> newAlphabet = new LinkedHashSet<Label>(module.behaviour.getAlphabet());
                seqNext = seq.crossWithSet(newAlphabet);
                assert newAlphabet.size() == module.behaviour.getAlphabet().size() : "alphabet was extended for the second time";
            }
            seq = seqNext;
        }
        initInputToPossibleOutputsMap();
        return engine;
    }

    /**
	 * Creates map associating input to all possible outputs
	 */
    public void initInputToPossibleOutputsMap() {
        for (Label label : module.behaviour.getAlphabet()) updateInputToPossibleOutputs(label);
    }

    @Override
    public LearnerGraph init(Collection<List<Label>> plus, Collection<List<Label>> minus) {
        initInputToPossibleOutputsMap();
        return super.init(plus, minus);
    }

    @Override
    public LearnerGraph init(PTASequenceEngine engine, int plusSize, int minusSize) {
        initInputToPossibleOutputsMap();
        return super.init(engine, plusSize, minusSize);
    }

    /** Records the result of running a trace past Erlang. */
    public static class TraceOutcome {

        public static enum TRACEOUTCOME {

            TRACE_OK, TRACE_FAIL, TRACE_DIFFERENTOUTPUT
        }

        ;

        public final ErlangLabel[] questionDetails;

        public final ErlangLabel[] answerDetails;

        public final TRACEOUTCOME outcome;

        public TraceOutcome(ErlangLabel[] trace, ErlangLabel[] answer, TRACEOUTCOME out) {
            questionDetails = trace;
            answerDetails = answer;
            outcome = out;
        }

        public String toTraceFileString() {
            String result = "";
            if (outcome == TRACEOUTCOME.TRACE_DIFFERENTOUTPUT) {
                result = "- [";
                ErlangLabel[] shortQuestion;
                shortQuestion = new ErlangLabel[answerDetails.length];
                for (int i = 0; i < answerDetails.length; i++) {
                    shortQuestion[i] = questionDetails[i];
                }
                result += RPNILearner.questionToString(Arrays.asList(shortQuestion));
                if (answerDetails.length > 0) {
                    result += "]\n+ [";
                    result += RPNILearner.questionToString(Arrays.asList(answerDetails));
                }
            } else {
                if (outcome == TRACEOUTCOME.TRACE_OK) {
                    result = "+ [";
                } else if (outcome == TRACEOUTCOME.TRACE_FAIL) {
                    result = "- [";
                }
                result += RPNILearner.questionToString(Arrays.asList(answerDetails));
            }
            result += "]";
            return result;
        }
    }

    public static ErlangLabel stripOutput(ErlangLabel label) {
        return new ErlangLabel(label.function, label.callName, label.input, null);
    }

    /** Determines the outcome of running a trace past Erlang. */
    public TraceOutcome askErlang(List<? extends Label> question) {
        ErlangLabel[] questionDetails = new ErlangLabel[question.size()];
        int i = 0;
        for (Label lbl : question) {
            if (!(lbl instanceof ErlangLabel)) throw new IllegalArgumentException("question element " + lbl + " is not of Erlang type");
            ErlangLabel erlLabel = (ErlangLabel) lbl;
            if (!module.getName().equals(erlLabel.function.getModuleName())) throw new IllegalArgumentException("current module is " + module.getName() + " but attempting to call " + erlLabel.function.getModuleName());
            if (!module.behaviour.getAlphabet().contains(lbl)) throw new IllegalArgumentException("label " + lbl + " does not belong to the alphabet \n" + module.behaviour.getAlphabet());
            questionDetails[i++] = (ErlangLabel) lbl;
        }
        return askErlang(questionDetails);
    }

    /** Determines the outcome of running a trace past Erlang. */
    public TraceOutcome askErlang(ErlangLabel[] questionDetails) {
        configErlang();
        boolean first = true;
        if (config.getErlangDisplayQuestions()) {
            System.out.print("Asking erlang about ");
            for (Object o : questionDetails) {
                if (first) first = false; else System.out.print(',');
                System.out.print(o.toString());
            }
            System.out.println();
        }
        OtpErlangTuple result = ErlangRunner.getRunner().call(new OtpErlangObject[] { new OtpErlangAtom("runTrace"), new OtpErlangAtom(module.getName()), new OtpErlangAtom(module.behaviour.getWrapperName()), new OtpErlangList(questionDetails), new OtpErlangList() }, "running trace");
        OtpErlangAtom outcome = (OtpErlangAtom) result.elementAt(1);
        TRACEOUTCOME outcomeEnum = null;
        if (outcome.atomValue().equals("ok")) {
            outcomeEnum = TRACEOUTCOME.TRACE_OK;
        } else if (outcome.atomValue().equals("failed_but")) {
            outcomeEnum = TRACEOUTCOME.TRACE_DIFFERENTOUTPUT;
        } else if (outcome.atomValue().equals("timeout")) {
            outcomeEnum = TRACEOUTCOME.TRACE_FAIL;
        } else if (outcome.atomValue().equals("failed")) {
            outcomeEnum = TRACEOUTCOME.TRACE_FAIL;
        } else {
            throw new IllegalArgumentException("unknown Erlang response " + outcome);
        }
        OtpErlangList trace = (OtpErlangList) result.elementAt(2);
        ErlangLabel[] answerDetails = new ErlangLabel[trace.arity()];
        for (int i = 0; i < trace.arity(); ++i) {
            OtpErlangTuple elemAti = (OtpErlangTuple) trace.elementAt(i);
            if (elemAti.arity() < 2 || elemAti.arity() > 3) throw new IllegalArgumentException("received tuple " + elemAti + " of invalid arity");
            if (elemAti.arity() == 3) {
                if (config.getUseErlangOutputs()) {
                    answerDetails[i] = new ErlangLabel(questionDetails[i].function, questionDetails[i].callName, questionDetails[i].input, elemAti.elementAt(2));
                } else {
                    answerDetails[i] = new ErlangLabel(questionDetails[i].function, questionDetails[i].callName, questionDetails[i].input);
                }
            } else {
                answerDetails[i] = questionDetails[i];
            }
        }
        if (config.getErlangDisplayQuestions()) {
            System.out.println("And the answer is " + outcomeEnum + " :");
            first = true;
            for (Object o : answerDetails) {
                if (first) first = false; else System.out.print(',');
                System.out.print(o.toString());
            }
            System.out.println();
        }
        return new TraceOutcome(questionDetails, answerDetails, outcomeEnum);
    }
}
