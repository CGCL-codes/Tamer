package org.ai.statespacesearch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * A basic minimax and alpha-beta pruning implementation.
 * Does not assume zero-sum game.
 * Employs iterative deepening depth-first search.
 * TODO: Specify properties further.
 *
 * @author melvin
 *
 * @param <T>
 * @param <E>
 */
public class StateSpaceSearchBasicImplementation<T, E extends StateSpaceSearchGameState> implements StateSearchSpace<T, E> {

    private GameStateTransformer<T, E> gameStateTransformer;

    private ActionGenerator<T, E> actionGenerator;

    private GameStateEvaluation<E> evaluator;

    private TimeLimit timeLimit;

    private int team;

    @Override
    public List<ScoreAction<T>> getStateSearchSpaceMove(final TimeLimit aTimeLimit, final int maxDepth, final GameStateTransformer<T, E> aGameStateTransformer, final ActionGenerator<T, E> aActionGenerator, final GameStateEvaluation<E> aEvaluator, final int aTeam, final E initialialGameState) {
        gameStateTransformer = aGameStateTransformer;
        actionGenerator = aActionGenerator;
        evaluator = aEvaluator;
        timeLimit = aTimeLimit;
        team = aTeam;
        if (maxDepth < 1) {
            throw new IllegalArgumentException("Max depth must be at least 1; but the value " + maxDepth + " was received");
        }
        final double alpha = -2.0, beta = 2.0;
        List<ScoreAction<T>> previousResults = new ArrayList<ScoreAction<T>>();
        for (int i = 1; i <= maxDepth; i++) {
            if (timeLimit.isTimeLimitExceeded(System.currentTimeMillis())) {
                break;
            }
            final List<ScoreAction<T>> newResults = iterateDeepeningDepthFirstSearchStart(i, 0, alpha, beta, initialialGameState, previousResults);
            if (newResults != null) {
                previousResults = newResults;
            }
        }
        if (previousResults.isEmpty()) {
            previousResults = new ArrayList<ScoreAction<T>>();
            for (T action : actionGenerator.getActionsForGameState(initialialGameState)) {
                previousResults.add(new ScoreAction<T>(0.0, action));
            }
            return previousResults;
        }
        return previousResults;
    }

    private final List<ScoreAction<T>> iterateDeepeningDepthFirstSearchStart(final int maxDepth, final int depth, double alpha, double beta, final E gameState, List<ScoreAction<T>> previousResults) {
        if (previousResults == null || previousResults.isEmpty()) {
            previousResults = new ArrayList<ScoreAction<T>>();
            for (T action : actionGenerator.getActionsForGameState(gameState)) {
                previousResults.add(new ScoreAction<T>(0.0, action));
            }
            if (previousResults.isEmpty()) {
                return previousResults;
            }
        }
        List<ScoreAction<T>> newResults = new ArrayList<ScoreAction<T>>();
        for (ScoreAction<T> previousScoreAction : previousResults) {
            final E nextGameState = gameStateTransformer.getTransformedGameState(previousScoreAction.getAction(), gameState);
            final Double newScore = iterateDeepeningDepthFirstSearch(maxDepth, 1, alpha, beta, nextGameState);
            if (newScore == null) {
                return null;
            }
            newResults.add(new ScoreAction<T>(newScore, previousScoreAction.getAction()));
        }
        Collections.sort(newResults, new Comparator<ScoreAction<T>>() {

            public int compare(ScoreAction<T> o1, ScoreAction<T> o2) {
                return Double.compare(o2.getScore(), o1.getScore());
            }
        });
        return newResults;
    }

    private final Double iterateDeepeningDepthFirstSearch(final int maxDepth, final int depth, double alpha, double beta, final E gameState) {
        if (depth == maxDepth) {
            return evaluator.getGameStateEvaluation(gameState, team, depth);
        }
        Double bestScore = null;
        final List<T> actions = actionGenerator.getActionsForGameState(gameState);
        if (actions.size() == 0) {
            return evaluator.getGameStateEvaluation(gameState, team, depth);
        }
        final boolean isOurselves = team == gameState.getCurrentTeam();
        for (T action : actions) {
            if (timeLimit.isTimeLimitExceeded(System.currentTimeMillis())) {
                return null;
            }
            final E nextGameState = gameStateTransformer.getTransformedGameState(action, gameState);
            Double scoreAction = iterateDeepeningDepthFirstSearch(maxDepth, depth + 1, alpha, beta, nextGameState);
            if (scoreAction == null) {
                return null;
            }
            if (bestScore == null) {
                bestScore = scoreAction;
            } else if (bestScore < scoreAction && isOurselves) {
                bestScore = scoreAction;
            } else if (bestScore > scoreAction && !isOurselves) {
                bestScore = scoreAction;
            }
            if (isOurselves) {
                alpha = Math.max(alpha, scoreAction);
            } else {
                beta = Math.min(beta, scoreAction);
            }
            if (alpha >= beta) {
                break;
            }
        }
        return bestScore;
    }
}
