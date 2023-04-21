package org.axb.eclipseti.model;

/**
 * @author AXB
 *
*/
public class SetiRankInfo implements IParent {

    private int fRank;

    private int fRankTotalUsers;

    private int fNumSameRank;

    private double fTopRankPct;

    private SetiUserData fParent;

    /**
     * @param data
     */
    public SetiRankInfo(SetiUserData data) {
        fParent = data;
    }

    public Object getParent() {
        return fParent;
    }

    /**
	 * @return
	 */
    public int getNumSameRank() {
        return fNumSameRank;
    }

    /**
	 * @return
	 */
    public int getRank() {
        return fRank;
    }

    /**
	 * @return
	 */
    public int getRankTotalUsers() {
        return fRankTotalUsers;
    }

    /**
	 * @return
	 */
    public double getTopRankPct() {
        return fTopRankPct;
    }

    /**
	 * @param i
	 */
    public void setNumSameRank(int i) {
        fNumSameRank = i;
    }

    /**
	 * @param i
	 */
    public void setRank(int i) {
        fRank = i;
    }

    /**
	 * @param i
	 */
    public void setRankTotalUsers(int i) {
        fRankTotalUsers = i;
    }

    /**
	 * @param d
	 */
    public void setTopRankPct(double d) {
        fTopRankPct = d;
    }
}
