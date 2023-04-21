package net.sf.statcvs.input;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import net.sf.statcvs.model.Author;
import net.sf.statcvs.model.Directory;
import net.sf.statcvs.model.Repository;
import net.sf.statcvs.model.SymbolicName;
import net.sf.statcvs.model.VersionedFile;
import net.sf.statcvs.output.ConfigurationOptions;
import net.sf.statcvs.util.FilePatternMatcher;
import net.sf.statcvs.util.FileUtils;

/**
 * <p>Helps building the {@link net.sf.statcvs.model.Repository} from a CVS
 * log. The <tt>Builder</tt> is fed by some CVS history data source, for
 * example a CVS log parser. The <tt>Repository</tt> can be retrieved
 * using the {@link #createCvsContent} method.</p>
 * 
 * <p>The class also takes care of the creation of <tt>Author</tt> and 
 * </tt>Directory</tt> objects and makes sure that there's only one of these
 * for each author name and path. It also provides LOC count services.</p>
 * 
 * @author Richard Cyganiak <richard@cyganiak.de>
 * @version $Id: Builder.java,v 1.40 2008/04/02 11:22:14 benoitx Exp $
 */
public class Builder implements CvsLogBuilder {

    private static Logger logger = Logger.getLogger(Builder.class.getName());

    private final RepositoryFileManager repositoryFileManager;

    private final FilePatternMatcher includePattern;

    private final FilePatternMatcher excludePattern;

    private final Pattern tagsPattern;

    private final Map authors = new HashMap();

    private final Map directories = new HashMap();

    private final Map symbolicNames = new HashMap();

    private final List fileBuilders = new ArrayList();

    private final Set atticFileNames = new HashSet();

    private FileBuilder currentFileBuilder = null;

    private Date startDate = null;

    private String projectName = null;

    private int countRejectedByExclude = 0;

    private int countAcceptedByExclude = 0;

    private int countRejectedByInclude = 0;

    private int countAcceptedByInclude = 0;

    private boolean flagOutOfSync = false;

    private boolean flagHasLocalCVSMetadata = false;

    private int countFoundLocalFiles = 0;

    private int countNotFoundLocalFiles = 0;

    /**
     * Creates a new <tt>Builder</tt>
     * @param repositoryFileManager the {@link RepositoryFileManager} that
     * 								can be used to retrieve LOC counts for
     * 								the files that this builder will create
     * @param includePattern a list of Ant-style wildcard patterns, seperated
     *                       by : or ;
     * @param excludePattern a list of Ant-style wildcard patterns, seperated
     *                       by : or ;
     * @param tagsPattern A regular expression; matching symbolic names are recorded
     */
    public Builder(final RepositoryFileManager repositoryFileManager, final FilePatternMatcher includePattern, final FilePatternMatcher excludePattern, final Pattern tagsPattern) {
        this.repositoryFileManager = repositoryFileManager;
        this.includePattern = includePattern;
        this.excludePattern = excludePattern;
        this.tagsPattern = tagsPattern;
        directories.put("", Directory.createRoot());
    }

    /**
     * Starts building the module.
     * 
     * @param moduleName name of the module
     */
    public void buildModule(final String moduleName) {
        this.projectName = moduleName;
    }

    /**
     * Starts building a new file. The files are not expected to be created
     * in any particular order.
     * @param filename the file's name with path, for example "path/file.txt"
     * @param isBinary <tt>true</tt> if it's a binary file
     * @param isInAttic <tt>true</tt> if the file is dead on the main branch
     * @param revBySymnames maps revision (string) by symbolic name (string)
     */
    public void buildFile(final String filename, final boolean isBinary, final boolean isInAttic, final Map revBySymnames) {
        if (currentFileBuilder != null) {
            fileBuilders.add(currentFileBuilder);
        }
        currentFileBuilder = new FileBuilder(this, filename, isBinary, revBySymnames);
        if (isInAttic) {
            atticFileNames.add(filename);
        }
    }

    /**
     * Adds a revision to the current file. The revisions must be added in
     * CVS logfile order, that is starting with the most recent one.
     * 
     * @param data the revision
     */
    public void buildRevision(final RevisionData data) {
        currentFileBuilder.addRevisionData(data);
        if (startDate == null || startDate.compareTo(data.getDate()) > 0) {
            startDate = data.getDate();
        }
    }

    /**
     * Returns a Repository object of all files.
     * 
     * @return Repository a Repository object
     */
    public Repository createCvsContent() {
        if (currentFileBuilder != null) {
            fileBuilders.add(currentFileBuilder);
            currentFileBuilder = null;
        }
        final Repository result = new Repository();
        final Iterator it = fileBuilders.iterator();
        while (it.hasNext()) {
            final FileBuilder fileBuilder = (FileBuilder) it.next();
            final VersionedFile file = fileBuilder.createFile(startDate);
            if (file == null) {
                continue;
            }
            if (fileBuilder.hasUnexpectedLocalRevision()) {
                this.flagOutOfSync = true;
            }
            if (fileBuilder.hasLocalCVSMetadata()) {
                this.flagHasLocalCVSMetadata = true;
            }
            if (fileBuilder.hasLocalFileNotFound()) {
                this.countNotFoundLocalFiles++;
                this.flagOutOfSync = true;
            } else if (file.getCurrentLinesOfCode() > 0) {
                this.countFoundLocalFiles++;
            }
            result.addFile(file);
            logger.finer("adding " + file.getFilenameWithPath() + " (" + file.getRevisions().size() + " revisions)");
        }
        final SortedSet revisions = result.getRevisions();
        final List commits = new CommitListBuilder(revisions).createCommitList();
        result.setCommits(commits);
        result.setSymbolicNames(getMatchingSymbolicNames());
        return result;
    }

    public String getProjectName() {
        return projectName;
    }

    /**
     * Returns the <tt>Set</tt> of filenames that are "in the attic".
     * @return a <tt>Set</tt> of <tt>String</tt>s
     */
    public Set getAtticFileNames() {
        return atticFileNames;
    }

    /**
     * @return <tt>true</tt> if there was an exclude pattern, and it rejected all files
     */
    public boolean allRejectedByExcludePattern() {
        return this.countRejectedByExclude > 0 && this.countAcceptedByExclude == 0;
    }

    /**
     * @return <tt>true</tt> if there was an include pattern, and it rejected all files
     */
    public boolean allRejectedByIncludePattern() {
        return this.countRejectedByInclude > 0 && this.countAcceptedByInclude == 0;
    }

    /**
     * Returns <tt>true</tt> if the local working copy is out of
     * sync with the log. The current implementation spots if
     * local files have been deleted and not yet committed, or
     * if the log file was generated before the latest commit.
     */
    public boolean isLogAndLocalFilesOutOfSync() {
        return this.flagHasLocalCVSMetadata && this.flagOutOfSync;
    }

    /**
     * Returns <tt>true</tt> if no local copy was found for
     * the majority of files in the log. This is a strong indication
     * that the log is not for the specified local working copy. 
     */
    public boolean isLocalFilesNotFound() {
        return this.countNotFoundLocalFiles > this.countFoundLocalFiles;
    }

    /**
     * Returns <tt>true</tt> if at least some local files have matching
     * entries in local CVS metada directories. If this is not the case,
     * then the local copy is probably just an export, not a checkout,
     * and we can't check if the log and working copy are in sync.
     */
    public boolean hasLocalCVSMetadata() {
        return this.flagHasLocalCVSMetadata;
    }

    /**
     * returns the <tt>Author</tt> of the given name or creates it
     * if it does not yet exist. Author names are handled as case-insensitive.
     * @param name the author's name
     * @return a corresponding <tt>Author</tt> object
     */
    public Author getAuthor(final String name) {
        if (this.authors.containsKey(name.toLowerCase())) {
            return (Author) this.authors.get(name.toLowerCase());
        }
        final Properties p = ConfigurationOptions.getConfigProperties();
        final Author newAuthor = new Author(name);
        this.authors.put(name.toLowerCase(), newAuthor);
        if (p != null) {
            newAuthor.setRealName(p.getProperty("user." + name.toLowerCase() + ".realName"));
            newAuthor.setHomePageUrl(p.getProperty("user." + name.toLowerCase() + ".url"));
            newAuthor.setImageUrl(p.getProperty("user." + name.toLowerCase() + ".image"));
            newAuthor.setEmail(p.getProperty("user." + name.toLowerCase() + ".email"));
        }
        return newAuthor;
    }

    /**
     * Returns the <tt>Directory</tt> of the given filename or creates it
     * if it does not yet exist.
     * @param filename the name and path of a file, for example "src/Main.java"
     * @return a corresponding <tt>Directory</tt> object
     */
    public Directory getDirectory(final String filename) {
        final int lastSlash = filename.lastIndexOf('/');
        if (lastSlash == -1) {
            return getDirectoryForPath("");
        }
        return getDirectoryForPath(filename.substring(0, lastSlash + 1));
    }

    /**
     * Returns the {@link SymbolicName} with the given name or creates it
     * if it does not yet exist.
     * 
     * @param name the symbolic name's name
     * @return the corresponding symbolic name object
     */
    public SymbolicName getSymbolicName(final String name) {
        SymbolicName sym = (SymbolicName) symbolicNames.get(name);
        if (sym != null) {
            return sym;
        } else {
            sym = new SymbolicName(name);
            symbolicNames.put(name, sym);
            return sym;
        }
    }

    public int getLOC(final String filename) throws NoLineCountException {
        if (repositoryFileManager == null) {
            throw new NoLineCountException("no RepositoryFileManager");
        }
        return repositoryFileManager.getLinesOfCode(filename);
    }

    /**
     * @see RepositoryFileManager#getRevision(String)
     */
    public String getRevision(final String filename) throws IOException {
        if (repositoryFileManager == null) {
            throw new IOException("no RepositoryFileManager");
        }
        return repositoryFileManager.getRevision(filename);
    }

    /**
     * Matches a filename against the include and exclude patterns. If no
     * include pattern was specified, all files will be included. If no
     * exclude pattern was specified, no files will be excluded.
     * @param filename a filename
     * @return <tt>true</tt> if the filename matches one of the include
     *         patterns and does not match any of the exclude patterns.
     *         If it matches an include and an exclude pattern, <tt>false</tt>
     *         will be returned.
     */
    public boolean matchesPatterns(final String filename) {
        if (excludePattern != null) {
            if (excludePattern.matches(filename)) {
                this.countRejectedByExclude++;
                return false;
            } else {
                this.countAcceptedByExclude++;
            }
        }
        if (includePattern != null) {
            if (includePattern.matches(filename)) {
                this.countAcceptedByInclude++;
            } else {
                this.countRejectedByInclude++;
                return false;
            }
        }
        return true;
    }

    /**
     * @param path for example "src/net/sf/statcvs/"
     * @return the <tt>Directory</tt> corresponding to <tt>statcvs</tt>
     */
    private Directory getDirectoryForPath(final String path) {
        if (directories.containsKey(path)) {
            return (Directory) directories.get(path);
        }
        final Directory parent = getDirectoryForPath(FileUtils.getParentDirectoryPath(path));
        final Directory newDirectory = parent.createSubdirectory(FileUtils.getDirectoryName(path));
        directories.put(path, newDirectory);
        return newDirectory;
    }

    private SortedSet getMatchingSymbolicNames() {
        final TreeSet result = new TreeSet();
        if (this.tagsPattern == null) {
            return result;
        }
        for (final Iterator it = this.symbolicNames.values().iterator(); it.hasNext(); ) {
            final SymbolicName sn = (SymbolicName) it.next();
            if (sn.getDate() != null && this.tagsPattern.matcher(sn.getName()).matches()) {
                result.add(sn);
            }
        }
        return result;
    }
}
