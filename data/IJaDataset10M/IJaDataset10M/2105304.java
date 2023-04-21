package com.botarena.client.contest.remote;

import java.util.List;
import com.botarena.shared.ContestInfo;
import com.botarena.shared.ContestState;
import com.botarena.shared.RankInfo;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("contestAccess")
public interface ContestAccess extends RemoteService {

    /**
	 * Get contest basics stats
	 * @param name
	 * @return
	 */
    public ContestState getContestState(String name);

    /**
	 * Get info about all contests
	 * @return
	 */
    public List<ContestInfo> getAllContests();

    /**
	 * Initialize ranking
	 * @param key
	 * @param size
	 * @return
	 */
    public List<RankInfo> getRanking(String key, int size);

    /**
	 * Get ranking parts by key
	 * @param botKeys
	 * @return
	 */
    public List<RankInfo> getRankPart(List<String> botKeys);

    /**
	 * Get top X bots in the given contest
	 * @param key
	 * @param size
	 * @return
	 */
    public List<RankInfo> getRankTop(String key, int size);
}
