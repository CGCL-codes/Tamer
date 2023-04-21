package net.sourceforge.cruisecontrol.sourcecontrols;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import net.sourceforge.cruisecontrol.CruiseControlException;
import net.sourceforge.cruisecontrol.Modification;
import net.sourceforge.cruisecontrol.util.ValidationHelper;

/**
 * Scans a directory tree on a local drive rather than in a repository.
 *
 * @author <a href="mailto:alden@thoughtworks.com">Alden Almagro</a>
 */
public class FileSystem extends FakeUserSourceControl {

    private List modifications;

    private File folder;

    /**
     * Set the root folder of the directories that we are going to scan
     */
    public void setFolder(String s) {
        folder = new File(s);
    }

    public void validate() throws CruiseControlException {
        ValidationHelper.assertIsSet(folder, "folder", this.getClass());
        ValidationHelper.assertTrue(folder.exists(), "folder " + folder.getAbsolutePath() + " must exist for FileSystem");
    }

    /**
     * For this case, we don't care about the quietperiod, only that
     * one user is modifying the build.
     *
     * @param lastBuild date of last build
     * @param now IGNORED
     */
    public List getModifications(Date lastBuild, Date now) {
        modifications = new ArrayList();
        visit(folder, lastBuild.getTime());
        if (!modifications.isEmpty()) {
            getSourceControlProperties().modificationFound();
        }
        return modifications;
    }

    /**
     * Add a Modification to the list of modifications. A lot of default
     * behavior is assigned here because we don't have a repository to query the
     * modification.  All modifications will be set to type "change" and
     * userName "User".
     */
    private void addRevision(File revision) {
        Modification mod = new Modification("filesystem");
        mod.userName = getUserName();
        Modification.ModifiedFile modfile = mod.createModifiedFile(revision.getName(), revision.getParent());
        modfile.action = "change";
        mod.modifiedTime = new Date(revision.lastModified());
        mod.comment = "";
        modifications.add(mod);
    }

    /**
     * Recursively visit all files below the specified one.  Check for newer
     * timestamps
     */
    private void visit(File file, long lastBuild) {
        if ((!file.isDirectory()) && (file.lastModified() > lastBuild)) {
            addRevision(file);
        }
        if (file.isDirectory()) {
            String[] children = file.list();
            for (int i = 0; i < children.length; i++) {
                visit(new File(file, children[i]), lastBuild);
            }
        }
    }
}
