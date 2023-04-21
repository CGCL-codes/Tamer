package net.sf.buildbox.devmodel.fs.api;

import java.io.File;
import java.util.Collection;
import java.util.Set;
import net.sf.buildbox.devmodel.Commit;
import net.sf.buildbox.devmodel.ModelView;
import net.sf.buildbox.devmodel.Version;
import net.sf.buildbox.devmodel.fs.LiveCommitLog;
import net.sf.buildbox.devmodel.fs.impl.LiveChangesXml;

public abstract class ParsedVersion extends Version {

    protected final ModelView devmodel;

    private final File basedir;

    public final LiveCommitLog liveCommitLog;

    private LiveChangesXml liveChangesXml;

    protected ParsedVersion(ModelView devmodel, File basedir) {
        this.devmodel = devmodel;
        this.basedir = basedir;
        liveCommitLog = new LiveCommitLog(new File(basedir.getParentFile(), "commits.xml"));
    }

    public final File getBasedir() {
        return basedir;
    }

    public Collection<Commit> getCommits() {
        return liveCommitLog.getCommits(devmodel, this);
    }

    /**
     * Returns tokens that match all specified labels
     * @param labels the set of labels required to match selected tokens; each label must match
     * @return matching tokens
     */
    public abstract Iterable<String> getTokens(Set<Label> labels);

    protected final LiveChangesXml liveChangesXml() {
        if (liveChangesXml == null) {
            liveChangesXml = new LiveChangesXml(locateArtifact("changes", "xml"));
        }
        return liveChangesXml;
    }

    /**
     * Returns all labels attached to given token
     * @param token the token 
     * @return attached labels
     */
    public abstract Set<Label> getLabels(String token);

    public abstract File locateArtifact(String classifier, String type);
}
