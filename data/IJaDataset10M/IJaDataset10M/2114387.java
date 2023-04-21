package alt.djudge.frontend.server.datatypes.scores;

import java.io.Serializable;
import javax.cache.Cache;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import alt.djudge.frontend.server.models.CacheModel;

@PersistenceCapable(detachable = "true")
@Inheritance(customStrategy = "complete-table")
public class ContestScoreEntry extends AbstractScoreEntry implements ContestScoreInterface, Serializable {

    private static final long serialVersionUID = 1L;

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    protected Long id;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Persistent
    protected Long contestId;

    public ContestScoreEntry() {
    }

    public ContestScoreEntry(Long contestId) {
        this.contestId = contestId;
    }

    public void setContestId(Long contestId) {
        this.contestId = contestId;
    }

    public Long getContestId() {
        return contestId;
    }

    @Override
    public void clearCacheCustom() {
        Cache cache = CacheModel.getStaticCache();
        String keyContest = CacheModel.getContestScoreEntryKey(contestId);
        cache.remove(keyContest);
    }
}
