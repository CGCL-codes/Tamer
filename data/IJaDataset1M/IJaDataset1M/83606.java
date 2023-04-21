package alt.djudge.frontend.server.datatypes;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.cache.Cache;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import alt.djudge.frontend.server.models.CacheModel;
import alt.djudge.frontend.shared.dto.admin.ContestLanguageEntryDTO;
import alt.djudge.frontend.shared.dto.admin.AdminDTOEntry;

@PersistenceCapable(detachable = "true")
public class ContestLanguageEntry extends AbstractEntry {

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
    private Long contestId;

    @Persistent
    private Long languageId;

    public void setContestId(Long contestId) {
        this.contestId = contestId;
    }

    public Long getContestId() {
        return contestId;
    }

    public void setLanguageId(Long languageId) {
        this.languageId = languageId;
    }

    public Long getLanguageId() {
        return languageId;
    }

    @Override
    public void clearCacheCustom() {
        Cache cache = CacheModel.getStaticCache();
        String keyContest = CacheModel.getContestLanguagesKey(contestId.toString());
        cache.remove(keyContest);
    }

    @Override
    public void fromDTO(AdminDTOEntry dtoEntry) {
        setContestId(Long.parseLong(dtoEntry.getFieldValue(1)));
        setLanguageId(Long.parseLong(dtoEntry.getFieldValue(2)));
    }

    @Override
    public AdminDTOEntry ownAdminDTO() {
        String[] data = new String[] { getId().toString(), getContestId().toString(), getLanguageId().toString() };
        return new ContestLanguageEntryDTO(data);
    }
}
