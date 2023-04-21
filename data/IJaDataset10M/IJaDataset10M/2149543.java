package vh.data.standings;

import vh.data.contest.VHContest;
import vh.data.contest.VHContestPhase;
import vh.data.season.VHSeason;

/**
 * DAO for {@link VHStandingsArchive}.
 * 
 * @version $Id: IVHStandingsArchiveDao.java 63 2006-12-10 18:41:08Z janjanke $
 * @author jjanke
 */
public interface IVHStandingsArchiveDao {

    /**
   * Inserts a newly created standings archive into the database.
   * 
   * @param archive the standings archive to be inserted
   */
    public void insertStandingsArchive(VHStandingsArchive archive);

    /**
   * Retrieves a particular standings archive.
   * 
   * @param season the season the standings archive should be retrieved for (pass
   *        <code>null</code> to get eternal standings)
   * @param contest the contest the standings archive is attached to
   * @param contestPhase the phase of the contest the standings archive is attached to
   * @return the corresponding {@link VHStandingsArchive} object
   */
    public VHStandingsArchive findBySeasonContestPhase(VHSeason season, VHContest contest, VHContestPhase contestPhase);
}
