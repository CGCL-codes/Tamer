package glaceo.data.dao.contest;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import glaceo.data.dao.IGEntity;
import glaceo.data.dao.standings.GFreeTicket;
import glaceo.data.helper.GContestRoundType;
import glaceo.gui.client.model.helper.GContestRoundFormat;

/**
 * Represents a round in a contest. A round can be either be composed of a single KO style
 * or one or more round robin {@link GContestElement} objects.
 *
 * @version $Id$
 * @author jjanke
 */
@Entity
@Table(name = "m_contest_round")
public class GContestRound implements Iterable<GContestElement>, IGEntity, Comparable<GContestRound> {

    @Id
    @Column(name = "id")
    private String d_strId;

    @Column(name = "num")
    private int d_nNum;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private GContestRoundType d_type = GContestRoundType.NOT_DEFINED;

    @Enumerated(EnumType.STRING)
    @Column(name = "format")
    private GContestRoundFormat d_format;

    /**
   * Indicates that the round has been started. This flag is especially of relevance in
   * multi-round contests and rounds with the format
   * {@link GContestRoundFormat#ROUND_ROBIN}. Such rounds may span over multiple movedays.
   * Together with the {@link #d_fPlayed} flag, this field can be used to determine
   * whether a round is currently in progress or not.
   */
    @Column(name = "started")
    private boolean d_fStarted;

    /**
   * Indicates if the round has already been played or not. This flag is of no relevance
   * for all contests that consist of a single contest round. It is suited for cup and
   * international contests where more than one round is necessary and it is required to
   * create new matches and goal accounts in the middle of the season.
   */
    @Column(name = "played")
    private boolean d_fPlayed;

    /**
   * Flag that can be optionally used to indicate that all statistical analysis actions
   * have been carried out for the round. Useful especially for rounds having multiple
   * contest elements and where actions spanning over multiple elements need to be carried
   * out (e.g. determining the best 2 second ranked teams etc.).
   */
    @Column(name = "analysed")
    private boolean d_fAnalysed;

    /**
   * Indicates whether this round is the last round of the parent contest.
   */
    @Column(name = "last")
    private boolean d_fLastRound = false;

    /**
   * Indicates the number of KO matches that must be won following this round and that are
   * played using the same goal account and contest element type.
   */
    @Column(name = "left_ko_matches_same_ga")
    private int d_nLeftKOMatchesSameGA = 0;

    /**
   * Indicates the number of round robin matches that must be won following this round and
   * that are played using the same goal account and contest element type.
   */
    @Column(name = "left_rr_matches_same_ga")
    private int d_nLeftRRMatchesSameGA = 0;

    /**
   * The highest possible rank a club that does not qualify for the next round (if this
   * round is part of a multi-round contest) can reach. E.g. if it's the round of the last
   * 16, a losing team can at best be classified 9th at the end of the competition.
   */
    @Column(name = "best_loser_rank")
    private int d_nBestLoserRank = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contest_id")
    private GContest d_contest;

    @OneToMany(mappedBy = "d_contestRound", cascade = { CascadeType.ALL })
    @OrderBy("d_strId")
    private List<GContestElement> d_listContestElements;

    @OneToMany(mappedBy = "d_contestRound", cascade = { CascadeType.ALL })
    @OrderBy("d_nId")
    private List<GFreeTicket> d_listFreeTickets;

    /** Default parameterless constructor. */
    protected GContestRound() {
    }

    /**
   * Creates a new contest round. This constructor is intended for contests where the
   * structure is built dynamically at the beginning of each season.
   *
   * @param parent the contest to which this round will be attached
   * @param strId the ID of the round
   * @param nNum the number of the round (very important - it determines the order in the
   *          contest)
   * @param type the contest round type
   * @param format the format of the attached contest elements
   */
    public GContestRound(GContest parent, String strId, int nNum, GContestRoundType type, GContestRoundFormat format) {
        d_contest = parent;
        d_strId = strId;
        d_nNum = nNum;
        d_fPlayed = false;
        d_type = type;
        d_format = format;
        d_contest.addRound(this);
    }

    /**
   * Adds a contest element to this round.
   *
   * @param element the contest element to be added
   */
    public void addContestElement(GContestElement element) {
        if (d_listContestElements == null) d_listContestElements = new ArrayList<GContestElement>();
        d_listContestElements.add(element);
        element.setContestRound(this);
    }

    /**
   * Adds a new free ticket to this contest round.
   *
   * @param freeTicket the free ticket to be added
   */
    public void addFreeTicket(GFreeTicket freeTicket) {
        if (d_listFreeTickets == null) d_listFreeTickets = new ArrayList<GFreeTicket>();
        d_listFreeTickets.add(freeTicket);
    }

    /**
   * @return Returns the minimum number of movedays that are required to execute the
   *         entire contest round.
   */
    public int getMinNumMovedays() {
        assert d_listContestElements != null : "A contest round must have at least one contest element.";
        int nMax = 0;
        for (GContestElement element : d_listContestElements) {
            int nNumMovedays = element.getMinNumMovedays();
            if (nNumMovedays > nMax) nMax = nNumMovedays;
        }
        return nMax;
    }

    /**
   * @return Indicates whether this contest round may be better organised if more than the
   *         minimum number of movedays is allocated to it.
   */
    public boolean prefersAdditionalMovedays() {
        assert d_listContestElements != null : "A contest round must have at least one contest element.";
        for (GContestElement element : d_listContestElements) {
            if (element.prefersAdditionalMovedays()) return true;
        }
        return false;
    }

    public int getRoundNum() {
        return d_nNum;
    }

    /**
   * Returns the number of KO matches that must be won and that follow this round and that
   * use the same type of contest element and the same goal account. This is not
   * necessarily the total number of KO matches as e.g. in a best of three series, only 2
   * out of possibly 3 matches need to be won.
   */
    public int getRemainingKOMatchesSameGA() {
        return d_nLeftKOMatchesSameGA;
    }

    /**
   * Returns the number of round robin matches that follow this round and use the same
   * goals account.
   */
    public int getRemainingRRMatchesSameGA() {
        return d_nLeftRRMatchesSameGA;
    }

    /**
   * Returns the sum of the two methods {@link #getRemainingKOMatchesSameGA()} and
   * {@link #getRemainingRRMatchesSameGA()}.
   */
    public int getRemainingMatchesSameGA() {
        return d_nLeftKOMatchesSameGA + d_nLeftRRMatchesSameGA;
    }

    /**
   * @see #getRemainingKOMatchesSameGA()
   */
    public void setRemainingKOMatchesSameGA(int nNumMatches) {
        d_nLeftKOMatchesSameGA = nNumMatches;
    }

    /**
   * @see #getRemainingRRMatchesSameGA()
   */
    public void setRemainingRRMatchesSameGA(int nNumMatches) {
        d_nLeftRRMatchesSameGA = nNumMatches;
    }

    public GContestRoundType getRoundType() {
        return d_type;
    }

    public void setRoundType(GContestRoundType type) {
        d_type = type;
    }

    /**
   * Returns the highest possible rank, a team not qualifying for the next round can
   * obtain at the end of the competition. Returns 0 if this is the only round of a
   * contest.
   */
    public int getBestPossibleLoserRank() {
        return d_nBestLoserRank;
    }

    /**
   * Sets the highest possible rank, a team not qualifying for the next round can obtain
   * at the end of the competition.
   *
   * @param nRank the best possible loser rank, set to 0 if this is the only contest round
   */
    public void setBestPossibleLoserRank(int nRank) {
        d_nBestLoserRank = nRank;
    }

    public GContest getContest() {
        return d_contest;
    }

    public List<GContestElement> getContestElements() {
        return d_listContestElements;
    }

    public List<GFreeTicket> getFreeTickets() {
        return d_listFreeTickets;
    }

    public GContestRoundFormat getFormat() {
        return d_format;
    }

    /**
   * Returns <code>true</code> if this is the first round of a contest.
   */
    public boolean isFirstRound() {
        return d_nNum == 1;
    }

    /**
   * Returns <code>true</code> if this is the last round of a contest.
   */
    public boolean isLastRound() {
        return d_fLastRound;
    }

    /**
   * Sets whether this round is the last of a specific contest or not.
   */
    public void setLastRound(boolean flag) {
        d_fLastRound = flag;
    }

    /**
   * Returns whether the round is currently in progress. I.e. it has been started but not
   * all matchdays have been played so far.
   */
    public final boolean isInProgress() {
        return d_fStarted && !d_fPlayed;
    }

    /**
   * Sets this round as being started or not.
   */
    public final void setStarted(boolean flag) {
        d_fStarted = flag;
    }

    public boolean isPlayed() {
        return d_fPlayed;
    }

    public void setPlayed(boolean flag) {
        d_fPlayed = flag;
    }

    public boolean isAnalysed() {
        return d_fAnalysed;
    }

    public void setAnalysed(boolean flag) {
        d_fAnalysed = flag;
    }

    public void reset() {
        d_fStarted = false;
        d_fPlayed = false;
        d_fAnalysed = false;
    }

    public Iterator<GContestElement> iterator() {
        return d_listContestElements.iterator();
    }

    public String getId() {
        return d_strId;
    }

    public Class<? extends IGEntity> getGEntityClass() {
        return GContestRound.class;
    }

    public boolean instanceOf(Class<? extends IGEntity> classOther) {
        return classOther.isAssignableFrom(getGEntityClass());
    }

    @Override
    public boolean equals(Object obj) {
        try {
            return d_strId.equals(((GContestRound) obj).getId());
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return d_strId.hashCode();
    }

    @Override
    public String toString() {
        return d_strId;
    }

    /**
   * Sorts rounds by contest and round number.
   */
    public int compareTo(GContestRound other) {
        int nTmp = this.getContest().compareTo(other.getContest());
        if (nTmp != 0) return nTmp;
        return this.getRoundNum() - other.getRoundNum();
    }
}
