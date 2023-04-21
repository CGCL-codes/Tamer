package net.sf.buildbox.releasator.legacy;

import net.sf.buildbox.args.annotation.Param;
import net.sf.buildbox.args.annotation.Option;
import net.sf.buildbox.args.annotation.Global;
import net.sf.buildbox.releasator.model.PomChange;
import java.util.List;
import java.util.ArrayList;

public abstract class AbstractPrepareCommand extends JReleasator {

    protected final ScmData scm;

    protected final String releaseVersion;

    protected final String codename;

    protected String author;

    protected boolean dryOnly;

    protected List<String> changeItems = new ArrayList<String>();

    protected List<PomChange> pomChanges = new ArrayList<PomChange>();

    public AbstractPrepareCommand(ScmData scm, String releaseVersion, String... codename) {
        this.scm = scm;
        this.releaseVersion = releaseVersion;
        this.codename = concat(codename);
    }

    protected static String concat(String... strings) {
        final StringBuilder sb = new StringBuilder();
        for (String string : strings) {
            if (sb.length() != 0) {
                sb.append(' ');
            }
            sb.append(string);
        }
        final String rv = sb.toString();
        return rv.trim().equals("") ? null : rv;
    }

    @Global(false)
    @Option(longName = "--dry", description = "only pretend the build, no real commits/uploads are performed")
    public void setDryOnly() {
        this.dryOnly = true;
    }

    @Global(false)
    @Option(longName = "--author", description = "email of person to be recorded in changes.xml as the author of this release; please use lowercase")
    public void setAuthor(@Param("email") String author) {
        this.author = author;
    }

    @Global(false)
    @Option(longName = "--changes-item-simple", description = "adds change item as part of the release")
    public void addChangeItemSimple(@Param("text") String text) {
        changeItems.add(text);
    }

    @Global(false)
    @Option(longName = "--pom-change", description = "adds change item as part of the release")
    public void addPomChange(@Param("what") String location, @Param("value") String value) {
        pomChanges.add(new PomChange(location, value));
    }

    public void copyOptionsFrom(AbstractPrepareCommand other) {
        super.copyOptionsFrom(other);
        if (other.dryOnly) {
            setDryOnly();
        }
        setAuthor(other.author);
        changeItems.addAll(other.changeItems);
        pomChanges.addAll(other.pomChanges);
    }
}
