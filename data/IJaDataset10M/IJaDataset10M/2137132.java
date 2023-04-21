package raptor.chat;

/**
 * This code was adapted from some code johnthegreat for Raptor.
 */
public class Partnership implements Comparable<Partnership> {

    private Bugger bugger1;

    private Bugger bugger2;

    public int compareTo(Partnership partnership) {
        int teamRating1 = getTeamRating();
        int teamRating2 = partnership.getTeamRating();
        return teamRating1 < teamRating2 ? 1 : teamRating1 == teamRating2 ? 0 : -1;
    }

    public Bugger getBugger1() {
        return bugger1;
    }

    public Bugger getBugger2() {
        return bugger2;
    }

    public int getTeamRating() {
        return bugger1.getRatingAsInt() + bugger2.getRatingAsInt();
    }

    public void setBugger1(Bugger bugger) {
        bugger1 = bugger;
    }

    public void setBugger2(Bugger bugger) {
        bugger2 = bugger;
    }

    @Override
    public String toString() {
        return bugger1 + " " + bugger2;
    }
}
