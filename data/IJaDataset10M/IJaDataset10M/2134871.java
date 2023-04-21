package clustermines.core;

import java.util.ArrayList;
import java.util.List;

public class SweeperHistory implements SweeperListener {

    enum ActionType {

        REVEAL("r"), FLAG("f");

        String string;

        ActionType(String string) {
            this.string = string;
        }

        public String toString() {
            return string;
        }
    }

    ;

    private List<HistoryItem> history;

    private int[][] field;

    private Sweeper sweeper;

    private int nextStep;

    private boolean isReplaying;

    private boolean safeBorder;

    public SweeperHistory(Sweeper sweeper) {
        this.sweeper = sweeper;
        safeBorder = sweeper.isSafeBorder();
        history = new ArrayList<HistoryItem>(sweeper.getHeight() * sweeper.getWidth() * 11 / 10);
        nextStep = 0;
        field = null;
        isReplaying = false;
    }

    public void setField(Sweeper sweeper) {
        field = new int[sweeper.getWidth()][sweeper.getHeight()];
        for (int x = 0; x < sweeper.getWidth(); x++) {
            for (int y = 0; y < sweeper.getHeight(); y++) {
                field[x][y] = sweeper.getSquare(x, y).getMines();
            }
        }
    }

    public void reveal(Square square) {
        if (!isReplaying) {
            if (field == null) {
                setField(sweeper);
            }
            history.add(HistoryItem.newRevealItem(square));
        } else {
            HistoryItem next = history.get(nextStep);
            if (next.action == ActionType.REVEAL && square.x == next.x && square.y == next.y) {
                nextStep++;
                if (nextStep == history.size()) {
                    stopReplay();
                }
            } else {
                stopReplay();
                history.add(HistoryItem.newRevealItem(square));
            }
        }
    }

    public void flag(Square square) {
        if (!isReplaying) {
            history.add(HistoryItem.newFlagItem(square));
        } else {
            HistoryItem next = history.get(nextStep);
            if (next.action == ActionType.FLAG && square.x == next.x && square.y == next.y) {
                nextStep++;
                if (nextStep == history.size()) {
                    stopReplay();
                }
            } else {
                stopReplay();
                history.add(HistoryItem.newFlagItem(square));
            }
        }
    }

    public Sweeper replay() {
        sweeper = Sweeper.newSweeperFromFieldMatrix(field);
        sweeper.setSafeBorder(safeBorder);
        sweeper.addSweeperListener(this);
        isReplaying = true;
        nextStep = 0;
        return sweeper;
    }

    public void stopReplay() {
        for (int i = history.size() - 1; i >= nextStep; i--) {
            history.remove(i);
        }
        isReplaying = false;
    }

    public void step() {
        if (isReplaying) {
            Square square = sweeper.getSquare(history.get(nextStep).x, history.get(nextStep).y);
            switch(history.get(nextStep).action) {
                case FLAG:
                    sweeper.flag(square);
                    break;
                case REVEAL:
                    sweeper.reveal(square);
                    break;
            }
        }
    }

    public void skipToEnd() {
        while (isReplaying && nextStep < history.size() - 1) {
            step();
        }
    }

    public void fastForward(int steps) {
        if (steps > history.size()) {
            steps = history.size();
        }
        for (int i = 0; i < steps && isReplaying; i++) {
            step();
        }
    }

    public boolean isReplaying() {
        return isReplaying;
    }

    public boolean canReplay() {
        return field != null;
    }

    public String writeReplayPanelLabel() {
        String buf = "Last step: ";
        if (nextStep > 0) {
            buf += history.get(nextStep - 1);
        }
        buf += "\nNext step: ";
        if (nextStep < history.size()) {
            buf += history.get(nextStep);
        }
        return buf;
    }

    public String toString() {
        String buf = "nextStep = " + nextStep + "\n";
        int i = 0;
        for (HistoryItem item : history) {
            if (i == nextStep) {
                buf += "-> " + item + "\n";
            } else {
                buf += i + ": " + item + "\n";
            }
            i++;
        }
        return buf;
    }

    public int getNextStepId() {
        return nextStep;
    }

    public int getTotallSteps() {
        return history.size();
    }
}

class HistoryItem {

    SweeperHistory.ActionType action;

    int x, y;

    HistoryItem(SweeperHistory.ActionType action, int x, int y) {
        super();
        this.action = action;
        this.x = x;
        this.y = y;
    }

    static HistoryItem newRevealItem(Square square) {
        return new HistoryItem(SweeperHistory.ActionType.REVEAL, square.x, square.y);
    }

    static HistoryItem newFlagItem(Square square) {
        return new HistoryItem(SweeperHistory.ActionType.FLAG, square.x, square.y);
    }

    public String toString() {
        return action + " " + x + " " + y;
    }
}
