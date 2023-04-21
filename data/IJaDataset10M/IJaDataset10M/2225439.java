package com.csft.poker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class TwoCardWinTieLose {

    private int c1;

    private int c2;

    private long win;

    private long tie;

    private long lose;

    public void setC1(int c1) {
        this.c1 = c1;
    }

    public int getC1() {
        return c1;
    }

    public void setC2(int c2) {
        this.c2 = c2;
    }

    public int getC2() {
        return c2;
    }

    public void setWin(long win) {
        this.win = win;
    }

    public long getWin() {
        return win;
    }

    public void setTie(long tie) {
        this.tie = tie;
    }

    public long getTie() {
        return tie;
    }

    public void setLose(long lose) {
        this.lose = lose;
    }

    public long getLose() {
        return lose;
    }

    /**
	 * Generate a TwoCardWinTieLose from its string representation
	 * 
	 * @param s
	 * @return
	 */
    public static TwoCardWinTieLose fromString(String s) {
        String[] ss = s.split("\t");
        TwoCardWinTieLose t = new TwoCardWinTieLose();
        int n = 0;
        t.c1 = new Integer(ss[n++]);
        t.c2 = new Integer(ss[n++]);
        t.win = new Long(ss[n++]);
        t.tie = new Long(ss[n++]);
        t.lose = new Long(ss[n++]);
        return t;
    }

    @Override
    public String toString() {
        return c1 + "\t" + c2 + "\t" + win + "\t" + tie + "\t" + lose;
    }

    /**
	 * Using Monte Carlo Method to calculate the win, tie, and lose ratio for
	 * particular hole cards and store the results in TwoCardWinTieLose.txt
	 * 
	 */
    public static void monteCarlo(int nRound) {
        List<TwoCardWinTieLose> tcwtls = new ArrayList<TwoCardWinTieLose>();
        String[] ss = FileHelper.readFileLineByLine("TwoCardWinTieLose.txt");
        for (String s : ss) {
            TwoCardWinTieLose tcwtl = TwoCardWinTieLose.fromString(s);
            tcwtls.add(tcwtl);
        }
        Dealer dealer = new Dealer();
        for (int round = 0; round < nRound; round++) {
            Card c1 = dealer.deal();
            Card c2 = dealer.deal();
            SevenCardHand sch = new SevenCardHand(c1, c2, dealer.deal(), dealer.deal(), dealer.deal(), dealer.deal(), dealer.deal());
            if (c1.getIndex() > c2.getIndex()) {
                Card temp = c1;
                c1 = c2;
                c2 = temp;
            }
            TwoCardWinTieLose nt = null;
            for (TwoCardWinTieLose tcwtl : tcwtls) {
                if (tcwtl.c1 == c1.getIndex() && tcwtl.c2 == c2.getIndex()) {
                    nt = tcwtl;
                    break;
                }
            }
            if (nt == null) {
                nt = new TwoCardWinTieLose();
                nt.c1 = c1.getIndex();
                nt.c2 = c2.getIndex();
                nt.win = 0;
                nt.tie = 0;
                nt.lose = 0;
                tcwtls.add(nt);
            }
            nt.win += sch.getWin();
            nt.tie += sch.getTie();
            nt.lose += sch.getLose();
        }
        TwoCardWinTieLose[] tcwtla = tcwtls.toArray(new TwoCardWinTieLose[0]);
        Arrays.sort(tcwtla, new Comparator<TwoCardWinTieLose>() {

            @Override
            public int compare(TwoCardWinTieLose o1, TwoCardWinTieLose o2) {
                if (o1.c1 > o2.c1) {
                    return 1;
                } else if (o1.c1 < o2.c1) {
                    return -1;
                } else {
                    if (o1.c2 > o2.c2) {
                        return 1;
                    } else if (o1.c2 < o2.c2) {
                        return -1;
                    } else {
                        return 0;
                    }
                }
            }
        });
        ss = new String[tcwtla.length];
        int n = 0;
        for (TwoCardWinTieLose tcwtl : tcwtla) {
            ss[n++] = tcwtl.toString();
        }
        FileHelper.writeLineByLine("TwoCardWinTieLose.txt", ss);
    }

    /**
	 * The cached Win tie lose table for the get method
	 */
    private static List<TwoCardWinTieLose> twoCardWinTieLoseList = new ArrayList<TwoCardWinTieLose>();

    /**
	 * Retrieve win,tie, lose ratio of a particular hole cards
	 */
    public static double[] get(Card card1, Card card2) {
        double[] ret = new double[3];
        if (twoCardWinTieLoseList.isEmpty()) {
            String[] ss = FileHelper.readFileLineByLine("TwoCardWinTieLose.txt");
            for (String s : ss) {
                TwoCardWinTieLose tcwtl = TwoCardWinTieLose.fromString(s);
                twoCardWinTieLoseList.add(tcwtl);
            }
        }
        int c1 = card1.getIndex();
        int c2 = card2.getIndex();
        if (c1 > c2) {
            int tmp = c1;
            c1 = c2;
            c2 = tmp;
        } else if (c1 == c2) {
            throw new IllegalArgumentException("Two cards should not be the same." + card1.toString() + " " + card2.toString());
        }
        for (TwoCardWinTieLose t : twoCardWinTieLoseList) {
            if (t.c1 == c1 && t.c2 == c2) {
                long total = t.getWin() + t.getTie() + t.getLose();
                ret[0] = t.getWin() * 1.0 / total;
                ret[1] = t.getTie() * 1.0 / total;
                ret[2] = t.getLose() * 1.0 / total;
            }
        }
        return ret;
    }
}
