package ao.irc;

import ao.holdem.model.Round;
import ao.holdem.model.act.AbstractAction;
import ao.holdem.model.act.Action;
import ao.holdem.model.card.Card;
import ao.holdem.model.card.Hole;
import org.apache.log4j.Logger;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * PLAYER INFORMATION (in pdb.* files)
    -----------------------------------
    player             #play prflop    turn         bankroll    winnings
              timestamp    pos   flop       river          action     cards
    Marzon    766303976  8  1 Bc  bc    kc    kf      12653  300    0

     PDB format
     ==========
     column 1        player nickname
     column 2        timestamp of this hand (see HDB)
     column 3        number of player dealt cards
     column 4        position of player (starting at 1,
                            in order of cards received)
     column 5        betting action preflop (see below)
     column 6        betting action on flop (see below)
     column 7        betting action on turn (see below)
     column 8        betting action on river (see below)
     column 9        player's bankroll at start of hand
     column 10       total action of player during hand
     column 11       amount of pot won by player
     column 12+      pocket cards of player (if revealed at showdown)
 */
public class IrcAction {

    private static final Logger LOG = Logger.getLogger(IrcAction.class);

    private static final Pattern pat = Pattern.compile("(\\S+)\\s+" + "(\\d+)\\s+" + "(\\d+)\\s+" + "(\\d+)\\s+" + "(\\S+)\\s+" + "(\\S+)\\s+" + "(\\S+)\\s+" + "(\\S+)\\s+" + "(\\d+)\\s+" + "(\\d+)\\s+" + "(\\d+)\\s*" + "(.*)");

    public static IrcAction fromLine(String line) {
        try {
            return new IrcAction(line.trim());
        } catch (Error e) {
            LOG.error("can't load from line " + line, e);
            return null;
        }
    }

    private final String name;

    private final long timestamp;

    private final int numPlayers;

    private final int position;

    private Action[] preflop;

    private final Action[] onFlop;

    private final Action[] onTurn;

    private final Action[] onRiver;

    private final int startingBankroll;

    private final int totalAction;

    private final int potWon;

    private final Hole revealedHole;

    private final String asIs;

    private IrcAction(String line) {
        Matcher m = pat.matcher(line);
        if (!m.matches()) {
            throw new Error("IrcAction can't match: " + line);
        }
        name = m.group(1);
        timestamp = Long.parseLong(m.group(2));
        numPlayers = Integer.parseInt(m.group(3));
        position = Integer.parseInt(m.group(4));
        preflop = parseActions(m.group(5), false);
        onFlop = parseActions(m.group(6), hasFolded(preflop));
        onTurn = parseActions(m.group(7), hasFolded(preflop, onFlop));
        onRiver = parseActions(m.group(8), hasFolded(preflop, onFlop, onTurn));
        startingBankroll = Integer.parseInt(m.group(9));
        totalAction = Integer.parseInt(m.group(10));
        potWon = Integer.parseInt(m.group(11));
        revealedHole = parseHole(m.group(12));
        asIs = line;
    }

    private Hole parseHole(String holeString) {
        if (holeString == null || holeString.length() == 0) return null;
        String holes[] = holeString.split("\\s+");
        return Hole.valueOf(Card.valueOfCard(holes[0]), Card.valueOfCard(holes[1]));
    }

    private Action[] parseActions(String actionString, boolean hasFolded) {
        if (hasFolded) return new Action[0];
        Action[] actions = new Action[actionString.length()];
        int nextIndex = 0;
        char_loop: for (char actionChar : actionString.toCharArray()) {
            switch(actionChar) {
                case '-':
                    break;
                case 'B':
                    actions[nextIndex++] = Action.BIG_BLIND;
                    break;
                case 'A':
                    actions[nextIndex - 1] = actions[nextIndex - 1].toAllIn();
                    break;
                case 'f':
                    actions[nextIndex++] = Action.FOLD;
                    break char_loop;
                case 'Q':
                case 'K':
                    actions[nextIndex++] = Action.QUIT;
                    break char_loop;
                case 'k':
                    actions[nextIndex++] = Action.CHECK;
                    break;
                case 'c':
                    actions[nextIndex++] = Action.CALL;
                    break;
                case 'b':
                    actions[nextIndex++] = Action.BET;
                    break;
                case 'r':
                    actions[nextIndex++] = Action.RAISE;
                    break;
                default:
                    throw new Error("unrecognized action: '" + actionChar + "'");
            }
        }
        return Arrays.copyOf(actions, nextIndex);
    }

    private boolean hasFolded(Action[]... actions) {
        for (Action[] actionSet : actions) {
            for (Action act : actionSet) {
                if (act.abstraction() == AbstractAction.QUIT_FOLD) {
                    return true;
                }
            }
        }
        return false;
    }

    public String name() {
        return name;
    }

    public long timestamp() {
        return timestamp;
    }

    public int numPlayers() {
        return numPlayers;
    }

    public int position() {
        return position;
    }

    public Action[] preFlop() {
        return preflop;
    }

    public Action[] onFlop() {
        return onFlop;
    }

    public Action[] onTurn() {
        return onTurn;
    }

    public Action[] onRiver() {
        return onRiver;
    }

    public Action[] action(Round during) {
        switch(during) {
            case PREFLOP:
                return preFlop();
            case FLOP:
                return onFlop();
            case TURN:
                return onTurn();
            case RIVER:
                return onRiver();
        }
        return null;
    }

    public void shrinkBlind() {
        preflop[0] = preflop[0].asSmallBlind();
    }

    public void growBlind() {
        preflop[0] = preflop[0].asBigBlind();
    }

    public int startingBankroll() {
        return startingBankroll;
    }

    public int totalAction() {
        return totalAction;
    }

    public int potWon() {
        return potWon;
    }

    public Hole hole() {
        return revealedHole;
    }

    @Override
    public String toString() {
        return asIs;
    }
}
