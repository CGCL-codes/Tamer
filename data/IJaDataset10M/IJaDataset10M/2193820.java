package net.sourceforge.ipp;

import java.util.Vector;

public class PlayerVector extends Vector<Player> {

    private static final long serialVersionUID = 3022527965118817563L;

    private int nplayers = 0;

    private int button = -1;

    private int current = -1;

    public void set(Player x, int i) {
        setElementAt(x, i);
    }

    public boolean add(Player x) {
        addElement(x);
        nplayers += 1;
        return true;
    }

    public void remove(Player x) {
        removeElement(x);
        nplayers -= 1;
    }

    public Player nth(int i) {
        return elementAt(i);
    }

    public void incrButton() {
        setButton((button + 1) % nplayers);
    }

    private void setButton(int n) {
        button = findActivePlayer(n, 0);
    }

    public int findActivePlayer(int start, int steps) {
        if (steps >= numPlayersNotFoldedOrBusted()) {
            return -1;
        }
        int tmpCurrent = current;
        current = (start - 1) % nplayers;
        incrCurrentPlayer();
        for (int i = 0; i < steps; i++) {
            incrCurrentPlayer();
        }
        int tigersToe = current;
        current = tmpCurrent;
        return tigersToe;
    }

    public Player getPlayerOnButton() {
        if (button != -1) {
            return elementAt(button);
        } else {
            return null;
        }
    }

    public void setPlayerOnButton(Player p) {
        int n = indexOf(p);
        if (n != -1) {
            button = n;
        }
    }

    public void setCurrentFromButton() {
        current = button;
        Player p = nth(current);
        if (p.isFolded() || p.isBusted() || p.isTappedOut() || p.isAllIn()) {
            incrCurrentPlayerForBetting();
        }
    }

    public Player getCurrentPlayer() {
        if (current > -1) {
            return nth(current);
        } else {
            return null;
        }
    }

    public void setCurrentPlayer(Player p) {
        int n = indexOf(p);
        if (n != -1) {
            current = n;
        }
    }

    public void setNoCurrentPlayer() {
        current = -1;
    }

    public void incrCurrentPlayer() {
        if (nplayers <= 1) {
            return;
        }
        Player p;
        do {
            current = (current + 1) % nplayers;
            p = nth(current);
        } while (p.isFolded() || p.isBusted());
    }

    public void incrCurrentPlayerForBetting() {
        if (nplayers <= 1) {
            return;
        }
        Player p;
        int start = current;
        do {
            current = (current + 1) % nplayers;
            p = nth(current);
            if (start == current) {
                break;
            }
        } while (p.isFolded() || p.isBusted() || p.isTappedOut() || p.isAllIn());
    }

    public Player find(String name) {
        for (Player p : this) {
            if (name.equals(p.getName())) {
                return p;
            }
        }
        return null;
    }

    public void setLastMsg(String msg) {
        for (Player p : this) {
            p.setLastMsg(msg);
        }
    }

    public void setBeaten(boolean b) {
        for (Player p : this) {
            p.setBeaten(b);
        }
    }

    public void resetForBettingRound() {
        for (Player p : this) {
            p.resetForBettingRound();
        }
    }

    public void resetForHand() {
        for (Player p : this) {
            p.resetForHand();
        }
    }

    public boolean allPaidUp(int amt) {
        for (Player p : this) {
            if (!p.isBusted() && !p.isFolded() && !p.isTappedOut() && !p.isAllIn() && (!p.hasPlayed() || p.getCurrentBet() < amt)) {
                return false;
            }
        }
        return true;
    }

    public int numPlayersStillBetting() {
        int n = 0;
        for (Player p : this) {
            if (!p.isBusted() && !p.isFolded() && !p.isTappedOut() && !p.isAllIn()) {
                n++;
            }
        }
        return n;
    }

    public int numPlayersNotFoldedOrBusted() {
        int n = 0;
        for (Player p : this) {
            if (!(p.isFolded() || p.isBusted())) {
                n += 1;
            }
        }
        return n;
    }

    public void foldPlayer(Player p) throws OnePlayerLeftException {
        p.setFolded(true);
        if (numPlayersNotFoldedOrBusted() == 1) {
            throw new OnePlayerLeftException();
        }
    }

    public int numPlayersNotBusted() {
        int n = 0;
        for (Player p : this) {
            if (!p.isBusted()) {
                n++;
            }
        }
        return n;
    }

    public void broadcast(String msg) {
        for (Player p : this) {
            p.sendTo(msg);
        }
    }

    public void broadcastToOthers(Player exclude, String msg) {
        for (Player p : this) {
            if (p != exclude) {
                p.sendTo(msg);
            }
        }
    }
}
