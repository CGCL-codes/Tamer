package net.sf.buildbox.buildrobot.events;

import java.util.Set;

/**
 * Represents commits that were not matched to any registered module.
 * All below single VCS.
 */
public final class UnmatchedCommitEvent extends BuildEvent {

    static final long serialVersionUID = 20080921L;

    private final String revision;

    private final String vcsId;

    private final Set<String> paths;

    public UnmatchedCommitEvent(String revision, String vcsId, Set<String> paths) {
        this.revision = revision;
        this.vcsId = vcsId;
        this.paths = paths;
    }

    public String getRevision() {
        return revision;
    }

    public String getVcsId() {
        return vcsId;
    }

    public Set<String> getPaths() {
        return paths;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(getClass().getSimpleName());
        sb.append("{revision=");
        sb.append(revision);
        sb.append(",vcsId=");
        sb.append(vcsId);
        sb.append(",paths=");
        sb.append(paths);
        sb.append('}');
        return sb.toString();
    }
}
