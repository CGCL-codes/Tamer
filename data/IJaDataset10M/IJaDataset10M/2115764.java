package vh.data.club;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import vh.data.contest.VHContest;
import vh.data.contest.VHContestType;
import vh.data.season.VHSeason;

/**
 * Each object of this class represents the result a particular club has achieved in a
 * given season and contest. The class is used to keep track of a club's successes.
 * 
 * @version $Id: VHClubContestHistory.java 70 2006-12-28 15:34:30Z janjanke $
 * @author jjanke
 */
@Entity
@Table(name = "club_contest_history")
public class VHClubContestHistory {

    /**
   * Unique id.
   */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int d_nId = 0;

    /**
   * Season for which the club's result is recorded.
   */
    @ManyToOne
    @JoinColumn(name = "season_id")
    private VHSeason d_season;

    /**
   * The club for which a result is archived.
   */
    @ManyToOne
    @JoinColumn(name = "club_id")
    private VHClub d_club;

    /**
   * The contest the recorded result is for.
   */
    @ManyToOne
    @JoinColumn(name = "contest_id")
    private VHContest d_contest;

    /**
   * The type of contest.
   */
    @Enumerated(EnumType.STRING)
    @Column(name = "contest_type")
    private VHContestType d_contestType;

    /**
   * The level of the contents. E.g. 1 for the first national league, 2 for the second
   * division etc.
   */
    @Column(name = "contest_level")
    private int d_nContestLevel;

    /**
   * The final rank of the club in the given contest.
   */
    @Column(name = "rank")
    private int d_nRank;

    /**
   * Indicates whether the club has been promoted so that it can play in a higher ranked
   * competition in the following season.
   */
    @Column(name = "promoted")
    private boolean d_fPromoted;

    /**
   * Indicates whether the club has been relegated so that it must play in a lower ranked
   * competition in the following season.
   */
    @Column(name = "relegated")
    private boolean d_fRelegated;

    /**
   * The number of points the club achieved during the recorded contest for the European
   * Club Ranking (ECR).
   */
    @Column(name = "points_ecr")
    private BigDecimal d_decPointsECR;

    /**
   * Default no argument constructor.
   */
    protected VHClubContestHistory() {
    }

    /**
   * Returns the club.
   */
    public VHClub getClub() {
        return d_club;
    }

    /**
   * Returns the contest.
   */
    public VHContest getContest() {
        return d_contest;
    }

    /**
   * Returns the contest's type (league, cup, European cup etc.).
   */
    public VHContestType getContestType() {
        return d_contestType;
    }

    /**
   * Returns the level of the contents. If the contents for which a result is recorded
   * corresponds to the first national league, 1 is returned. For a third division league,
   * 3 would be returned etc.
   */
    public int getContestLevel() {
        return d_nContestLevel;
    }

    /**
   * Returns the number of ECR points awarded.
   */
    public BigDecimal getPointsECR() {
        return d_decPointsECR;
    }

    /**
   * Indicates whether the referenced club was promoted.
   */
    public boolean isPromoted() {
        return d_fPromoted;
    }

    /**
   * Indicates whether the referenced club was relegated.
   */
    public boolean isRelegated() {
        return d_fRelegated;
    }

    /**
   * Returns the unique object ID.
   */
    public int getId() {
        return d_nId;
    }

    /**
   * Returns the final rank of the referenced club in the referenced contest.
   */
    public int getRank() {
        return d_nRank;
    }

    /**
   * Returns the season for which a result is recorded.
   */
    public VHSeason getSeason() {
        return d_season;
    }

    @Override
    public boolean equals(Object obj) {
        try {
            return d_nId == ((VHClubContestHistory) obj).d_nId;
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return d_nId;
    }
}
