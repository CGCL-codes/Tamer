package ao.simple.alexo.state;

import ao.simple.alexo.AlexoAction;
import ao.simple.alexo.card.AlexoCardSequence;
import ao.simple.alexo.card.AlexoHand;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 */
public class AlexoState {

    private static final int ANTE = 1;

    private static final int SMALL = 2;

    private static final int BIG = 4;

    private final AlexoRound round;

    private final int firstCommit;

    private final int lastCommit;

    private final int remainingRoundBets;

    private final boolean firstToActNext;

    private final boolean endOfHand;

    private final AlexoAction prevActInRound;

    public AlexoState() {
        round = AlexoRound.PREFLOP;
        firstCommit = ANTE;
        lastCommit = ANTE;
        remainingRoundBets = 2;
        firstToActNext = true;
        endOfHand = false;
        prevActInRound = null;
    }

    private AlexoState(AlexoRound nextRound, int nextFirstCommit, int nextLastCommit, int nextRemainingRoundBets, boolean nextFirstNextToAct, AlexoAction nextPrevActionInRound) {
        round = nextRound;
        firstCommit = nextFirstCommit;
        lastCommit = nextLastCommit;
        remainingRoundBets = nextRemainingRoundBets;
        firstToActNext = nextFirstNextToAct;
        endOfHand = (nextRound == null);
        prevActInRound = nextPrevActionInRound;
    }

    public AlexoState advance(AlexoAction act) {
        if (!validActions().contains(act)) {
            throw new Error("Invalid action.");
        }
        if (act == AlexoAction.FOLD) {
            return new AlexoState(null, firstCommit, lastCommit, -1, !firstToActNext, act);
        }
        if (act == AlexoAction.CHECK_CALL) {
            if (firstCommit != lastCommit) {
                int newCommit = Math.max(firstCommit, lastCommit);
                return new AlexoState(round.next(), newCommit, newCommit, 2, true, null);
            } else {
                if (prevActInRound == AlexoAction.CHECK_CALL) {
                    return new AlexoState(round.next(), firstCommit, lastCommit, 2, true, null);
                } else {
                    return new AlexoState(round, firstCommit, lastCommit, remainingRoundBets, !firstToActNext, act);
                }
            }
        } else {
            return new AlexoState(round, firstCommit + (firstToActNext ? betSize() : 0), lastCommit + (!firstToActNext ? betSize() : 0), remainingRoundBets - 1, !firstToActNext, act);
        }
    }

    private int betSize() {
        return round == AlexoRound.PREFLOP ? SMALL : BIG;
    }

    public List<AlexoAction> validActions() {
        if (endOfHand) return Collections.emptyList();
        List<AlexoAction> validActions = new ArrayList<AlexoAction>();
        validActions.add(AlexoAction.FOLD);
        validActions.add(AlexoAction.CHECK_CALL);
        if (remainingRoundBets > 0) {
            validActions.add(AlexoAction.BET_RAISE);
        }
        return validActions;
    }

    public int deltas(AlexoCardSequence cards) {
        assert endOfHand;
        boolean firstToActWins;
        if (prevActInRound == AlexoAction.FOLD) {
            firstToActWins = firstToActNext;
        } else {
            int firstVal = AlexoHand.valueOf(cards.firstHole(), cards.community());
            int lastVal = AlexoHand.valueOf(cards.lastHole(), cards.community());
            assert firstVal != lastVal;
            firstToActWins = (firstVal > lastVal);
        }
        return firstToActWins ? lastCommit : -firstCommit;
    }

    public AlexoOutcome outcome() {
        assert endOfHand;
        return (prevActInRound == AlexoAction.FOLD) ? firstToActNext ? AlexoOutcome.FIRST_TO_ACT_WINS : AlexoOutcome.LAST_TO_ACT_WINS : AlexoOutcome.SHOWDOWN;
    }

    public boolean endOfHand() {
        return endOfHand;
    }

    public boolean firstToActIsNext() {
        return firstToActNext;
    }

    public int firstCommit() {
        return firstCommit;
    }

    public int lastCommit() {
        return lastCommit;
    }

    public boolean atStartOfRound() {
        return prevActInRound == null;
    }

    public AlexoRound round() {
        return round;
    }
}
