package statechum.analysis.learning.observers;

import java.util.Collection;
import java.util.List;
import java.util.Stack;
import statechum.JUConstants;
import statechum.Label;
import statechum.Pair;
import statechum.analysis.learning.Learner;
import statechum.analysis.learning.PairScore;
import statechum.analysis.learning.StatePair;
import statechum.analysis.learning.rpnicore.*;
import statechum.model.testset.PTASequenceEngine;

public class ClusteringTrackerDecorator extends LearnerDecorator {

    int counter = 0;

    public ClusteringTrackerDecorator(Learner learner) {
        super(learner);
    }

    @Override
    public Stack<PairScore> ChooseStatePairs(LearnerGraph graph) {
        statechum.analysis.learning.util.ScoreMatrixOutput.writeMatrix(graph, "stage" + Integer.toString(counter));
        counter++;
        return decoratedLearner.ChooseStatePairs(graph);
    }

    @Override
    public LearnerGraph learnMachine(Collection<List<Label>> sPlus, Collection<List<Label>> sMinus) {
        init(sPlus, sMinus);
        return learnMachine();
    }

    @Override
    public LearnerGraph learnMachine() {
        LearnerGraph result = decoratedLearner.learnMachine();
        return result;
    }

    @Override
    public String getResult() {
        return decoratedLearner.getResult();
    }

    @Override
    public Pair<Integer, String> CheckWithEndUser(LearnerGraph graph, List<Label> question, int responseForNoRestart, List<Boolean> acceptedElements, PairScore pairBeingMerged, Object[] options) {
        return decoratedLearner.CheckWithEndUser(graph, question, responseForNoRestart, acceptedElements, pairBeingMerged, options);
    }

    @Override
    public List<List<Label>> ComputeQuestions(PairScore pair, LearnerGraph original, LearnerGraph temp) {
        return decoratedLearner.ComputeQuestions(pair, original, temp);
    }

    @Override
    public List<List<Label>> RecomputeQuestions(PairScore pair, LearnerGraph original, LearnerGraph temp) {
        return decoratedLearner.RecomputeQuestions(pair, original, temp);
    }

    @Override
    public LearnerGraph MergeAndDeterminize(LearnerGraph original, StatePair pair) {
        return decoratedLearner.MergeAndDeterminize(original, pair);
    }

    @Override
    public void Restart(RestartLearningEnum mode) {
        decoratedLearner.Restart(mode);
    }

    @Override
    public LearnerGraph init(Collection<List<Label>> plus, Collection<List<Label>> minus) {
        return decoratedLearner.init(plus, minus);
    }

    @Override
    public LearnerGraph init(PTASequenceEngine en, int plus, int minus) {
        return decoratedLearner.init(en, plus, minus);
    }

    @Override
    public void AugmentPTA(LearnerGraph pta, RestartLearningEnum ptaKind, List<Label> sequence, boolean accepted, JUConstants newColour) {
        decoratedLearner.AugmentPTA(pta, ptaKind, sequence, accepted, newColour);
    }

    @Override
    public boolean AddConstraints(LearnerGraph graph, LearnerGraph outcome, StringBuffer counterExampleHolder) {
        return decoratedLearner.AddConstraints(graph, outcome, counterExampleHolder);
    }
}
