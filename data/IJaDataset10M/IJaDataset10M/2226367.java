package glaceo.data;

import glaceo.error.GNotImplementedException;
import java.util.List;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

/**
 * Base class for all classes implementing a particular competition format (e.g. a KO or a
 * round robin round).
 *
 * @version $Id$
 * @author jjanke
 */
@Entity
@Table(name = "m_contest_element")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
public abstract class GContestElement {

    @Id
    @Column(name = "id")
    private String d_strId;

    @ManyToOne
    @JoinColumn(name = "contest_round_id")
    private GContestRound d_contestRound;

    /**
   * The label may e.g. be the letter or number designating a particular round robin pool
   * or the name (like 'quarter final', 'semi final' etc.) of a KO round. Please be aware
   * that the contained text may just be a reference to a resource defined somewhere else
   * and containing the actual localized label.
   */
    @Column(name = "label")
    private String d_strLabel;

    @OneToMany(mappedBy = "d_contestElement")
    @MapKey(name = "d_nNum")
    @OrderBy("d_nNum")
    private Map<Integer, GMatchday> d_mapMatchdays;

    /** Default parameterless constructor. */
    protected GContestElement() {
    }

    /**
   * @return Returns the number of matchdays which must be played to complete the entire
   *         contest element.
   */
    public abstract int getNumMatchdays();

    /**
   * @return Returns the minimum number of movedays that are required to play all
   *         matchdays of this contest element.
   */
    public abstract int getMinNumMovedays();

    /**
   * Attaches the given matchdays to the also given movedays in such a manner that the
   * days are equally distributed and respect the requirements of the specific contest
   * element (e.g. for a KO element, all matchdays of one round must be attached to the
   * same moveday).
   *
   * @param listMovedays list that contains all the movedays to which the matchdays should
   *          be attached
   * @param listMatchdays list of all the matchdays that should be attached to the
   *          different movedays
   */
    public abstract void attachMatchdaysToMovedays(List<GMoveday> listMovedays, List<GMatchday> listMatchdays);

    /**
   * Returns the amount of matches to be played by one club during this contest element.
   * For contest elements where the number of matches may vary, this method is not
   * implemented and throws a {@link GNotImplementedException}.
   *
   * @return the number of matches to be played by one club
   * @throws GNotImplementedException if the number of matches to be played by one club
   *           cannot be exactly determined
   */
    public int getNumMatchesPerClub() {
        throw new GNotImplementedException("The number of matches for one club cannot be determined exactly.");
    }

    /**
   * @return Indicates whether this contest element may be better organised if more than
   *         the strict minimum number of movedays is allocated to it. By default
   *         <code>false</code> is returned.
   */
    public boolean prefersAdditionalMovedays() {
        return false;
    }

    public final GContest getContest() {
        return getContestRound().getContest();
    }

    public final GContestRound getContestRound() {
        return d_contestRound;
    }

    public final String getLabel() {
        return d_strLabel;
    }

    /**
   * @return Returns a map of all matchdays where the key is the matchday number.
   */
    public final Map<Integer, GMatchday> getMatchdays() {
        return d_mapMatchdays;
    }

    /**
   * Returns the matchday with the given number.
   *
   * @param nNum the number of the matchday to be fetched
   * @return the requested matchday
   */
    public final GMatchday getMatchday(int nNum) {
        return d_mapMatchdays.get(Integer.valueOf(nNum));
    }

    public final String getId() {
        return d_strId;
    }

    @Override
    public boolean equals(Object obj) {
        try {
            return d_strId.equals(((GContestElement) obj).d_strId);
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
}
