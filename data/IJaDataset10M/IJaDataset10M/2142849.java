package com.limegroup.gnutella;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.limewire.collection.Comparators;
import org.limewire.collection.Function;
import org.limewire.collection.IntSet;
import org.limewire.collection.MultiCollection;
import org.limewire.collection.MultiIterator;
import org.limewire.collection.StringTrie;
import org.limewire.concurrent.ExecutorsHelper;
import org.limewire.inspection.Inspectable;
import org.limewire.inspection.InspectableContainer;
import org.limewire.inspection.InspectableForSize;
import org.limewire.inspection.InspectablePrimitive;
import org.limewire.inspection.InspectionPoint;
import org.limewire.setting.StringArraySetting;
import org.limewire.statistic.StatsUtils;
import org.limewire.util.ByteOrder;
import org.limewire.util.FileUtils;
import org.limewire.util.I18NConvert;
import org.limewire.util.RPNParser;
import org.limewire.util.StringUtils;
import com.google.inject.Inject;
import com.limegroup.gnutella.FileManagerEvent.Type;
import com.limegroup.gnutella.auth.ContentResponseData;
import com.limegroup.gnutella.auth.ContentResponseObserver;
import com.limegroup.gnutella.downloader.VerifyingFile;
import com.limegroup.gnutella.library.LibraryData;
import com.limegroup.gnutella.library.SharingUtils;
import com.limegroup.gnutella.licenses.LicenseType;
import com.limegroup.gnutella.messages.QueryRequest;
import com.limegroup.gnutella.routing.HashFunction;
import com.limegroup.gnutella.routing.QueryRouteTable;
import com.limegroup.gnutella.settings.MessageSettings;
import com.limegroup.gnutella.settings.SearchSettings;
import com.limegroup.gnutella.settings.SharingSettings;
import com.limegroup.gnutella.xml.LimeXMLDocument;

public abstract class FileManagerImpl implements FileManager {

    private static final Log LOG = LogFactory.getLog(FileManagerImpl.class);

    private static final ExecutorService LOADER = ExecutorsHelper.newProcessingQueue("FileManagerLoader");

    /**
     * An index value that describes store files which have a file descriptor but are not
     * shared. 
     */
    private static final int STORE_FILEDESC_INDEX = Integer.MAX_VALUE;

    /** List of event listeners for FileManagerEvents. */
    private volatile CopyOnWriteArrayList<FileEventListener> eventListeners = new CopyOnWriteArrayList<FileEventListener>();

    /**
     * All of the data for FileManager.
     */
    private final LibraryData _data = new LibraryData();

    /** 
     * The list of complete and incomplete files.  An entry is null if it
     *  is no longer shared.
     * INVARIANT: for all i, _files[i]==null, or _files[i].index==i and either
     *  _files[i]._path is in a shared directory with a shareable extension or
     *  _files[i]._path is the incomplete directory if _files[i] is an IncompleteFileDesc.
     */
    private List<FileDesc> _files;

    /**
     * The total size of all complete files, in bytes.
     * INVARIANT: _filesSize=sum of all size of the elements of _files,
     *   except IncompleteFileDescs, whose size may change at any time.
     */
    @InspectablePrimitive("total size of shared files")
    private long _filesSize;

    /**
     * The number of complete files.
     * INVARIANT: _numFiles==number of elements of _files that are not null
     *  and not IncompleteFileDescs.
     */
    @InspectablePrimitive("number of shared files")
    private int _numFiles;

    /** 
     * The total number of files that are pending sharing.
     *  (ie: awaiting hashing or being added)
     */
    @InspectablePrimitive("number of pending files")
    private int _numPendingFiles;

    /**
     * The total number of incomplete files.
     * INVARIANT: _numFiles + _numIncompleteFiles == the number of
     *  elements of _files that are not null.
     */
    @InspectablePrimitive("number of incomplete files")
    private int _numIncompleteFiles;

    /**
     * The number of files that are forcibly shared over the network.
     * INVARIANT: _numFiles >= _numForcedFiles.
     */
    @InspectablePrimitive("number force-shared files")
    private int _numForcedFiles;

    /**
     * An index that maps a <tt>File</tt> on disk to the 
     *  <tt>FileDesc</tt> holding it.
     *
     * INVARIANT: For all keys k in _fileToFileDescMap, 
     *  _files[_fileToFileDescMap.get(k).getIndex()].getFile().equals(k)
     *
     * Keys must be canonical <tt>File</tt> instances.
     */
    private Map<File, FileDesc> _fileToFileDescMap;

    /**
     * A trie mapping keywords in complete filenames to the indices in _files.
     * Keywords are the tokens when the filename is tokenized with the
     * characters from DELIMITERS as delimiters.
     * 
     * IncompleteFile keywords are NOT stored.
     * 
     * INVARIANT: For all keys k in _keywordTrie, for all i in the IntSet
     * _keywordTrie.get(k), _files[i]._path.substring(k)!=-1. Likewise for all
     * i, for all k in _files[i]._path where _files[i] is not an
     * IncompleteFileDesc, _keywordTrie.get(k) contains i.
     */
    @InspectableForSize("size of keyword trie")
    private StringTrie<IntSet> _keywordTrie;

    /**
     * A trie mapping keywords in complete filenames to the indices in _files.
     * Contains ONLY incomplete keywords.
     */
    @InspectableForSize("size of incomplete keyword trie")
    private StringTrie<IntSet> _incompleteKeywordTrie;

    /**
     * A map of appropriately case-normalized URN strings to the
     * indices in _files.  Used to make query-by-hash faster.
     * 
     * INVARIANT: for all keys k in _urnMap, for all i in _urnMap.get(k),
     * _files[i].containsUrn(k).  Likewise for all i, for all k in
     * _files[i].getUrns(), _urnMap.get(k) contains i.
     */
    private Map<URN, IntSet> _urnMap;

    /**
     * The set of file extensions to share, sorted by StringComparator. 
     * INVARIANT: all extensions are lower case.
     */
    private static Set<String> _extensions;

    /**
	 * A Set of shared directories that are completely shared.  Files in these
	 * directories are shared by default and will be shared unless the File is
	 * listed in SharingSettings.FILES_NOT_TO_SHARE.
	 */
    @InspectableForSize("number completely shared directories")
    private Set<File> _completelySharedDirectories;

    /**
     * The IntSet for incomplete shared files.
     * 
     * INVARIANT: for all i in _incompletesShared,
     *       _files[i]._path == the incomplete directory.
     *       _files[i] instanceof IncompleteFileDesc
     *  Likewise, for all i s.t.
     *    _files[i] != null and _files[i] instanceof IncompleteFileDesc,
     *       _incompletesShared.contains(i)
     * 
     * This structure is not strictly needed for correctness, but it allows
     * others to retrieve all the incomplete shared files, which is
     * relatively useful.                                                                                                       
     */
    @InspectableForSize("number incompletely shared files")
    private IntSet _incompletesShared;

    /**
     * A Set of URNs that we're currently requesting validation for.
     * This is NOT cleared on new revisions, because it'll always be
     * valid.
     */
    private Set<URN> _requestingValidation = Collections.synchronizedSet(new HashSet<URN>());

    /**
     * Files that are shared only for this LW session.
     * INVARIANT: no file can be in this and _data.SPECIAL_FILES_TO_SHARE
     * at the same time
     */
    @InspectableForSize("number of transiently shared files")
    private Set<File> _transientSharedFiles = new HashSet<File>();

    /**
     * An index that maps a LWS <tt>File</tt> on disk to the 
     *  <tt>FileDesc</tt> holding it.
     *
     * INVARIANT: For all keys k in _fileToFileDescMap, 
     *  _files[_fileToFileDescMap.get(k).getIndex()].getFile().equals(k)
     *
     * Keys must be canonical <tt>File</tt> instances.
     */
    private Map<File, FileDesc> _storeToFileDescMap;

    /**
     *  The directory for downloading LWS songs to and any subdirectories
     *  that may recursively exist
     */
    @InspectableForSize("number of directories for the store")
    private Set<File> _storeDirectories;

    /**
     * Individual files that are not in a shared folder.
     */
    @InspectableForSize("number of individually shared files")
    private Collection<File> _individualSharedFiles;

    /**
     * The revision of the library.  Every time 'loadSettings' is called, the revision
     * is incremented.
     */
    @InspectablePrimitive("filemanager revision")
    protected volatile int _revision = 0;

    /**
     * The revision that finished loading all pending files.
     */
    @InspectablePrimitive("revision that finished loading")
    private volatile int _pendingFinished = -1;

    /**
     * The revision that finished updating shared directories.
     */
    private volatile int _updatingFinished = -1;

    /**
     * If true, indicates that the FileManager is currently updating.
     */
    @InspectablePrimitive("filemanager currently updating")
    private volatile boolean _isUpdating = false;

    /**
     * The last revision that finished both pending & updating.
     */
    private volatile int _loadingFinished = -1;

    /**
     * Whether the FileManager has been shutdown.
     */
    protected volatile boolean shutdown;

    /**
     *  Different types of files to be added to the filemanager
     */
    public enum AddType {

        ADD_SHARE(Type.ADD_FILE, Type.ADD_FAILED_FILE), ADD_STORE(Type.ADD_STORE_FILE, Type.ADD_STORE_FAILED_FILE);

        private final Type success;

        private final Type failure;

        AddType(Type success, Type failure) {
            this.success = success;
            this.failure = failure;
        }

        public Type getSuccessType() {
            return success;
        }

        public Type getFailureType() {
            return failure;
        }
    }

    ;

    /**
     * The filter object to use to discern shareable files.
     */
    private final FileFilter SHAREABLE_FILE_FILTER = new FileFilter() {

        public boolean accept(File f) {
            return isFileShareable(f);
        }
    };

    /**
     * The filter object to use to determine directories.
     */
    private static final FileFilter DIRECTORY_FILTER = new FileFilter() {

        public boolean accept(File f) {
            return f.isDirectory();
        }
    };

    /** 
     * An empty callback so we don't have to do != null checks everywhere.
     */
    private static final FileEventListener EMPTY_CALLBACK = new FileEventListener() {

        public void handleFileEvent(FileManagerEvent evt) {
        }
    };

    /**
     * The QueryRouteTable kept by this.  The QueryRouteTable will be 
     * lazily rebuilt when necessary.
     */
    protected static QueryRouteTable _queryRouteTable;

    /**
     * Boolean for checking if the QRT needs to be rebuilt.
     */
    protected static volatile boolean _needRebuild = true;

    private static final boolean isDelimiter(char c) {
        switch(c) {
            case ' ':
            case '-':
            case '.':
            case '_':
            case '+':
            case '/':
            case '*':
            case '(':
            case ')':
            case '\\':
            case ',':
                return true;
            default:
                return false;
        }
    }

    private final QRPUpdater qrpUpdater = new QRPUpdater();

    /** Contains the definition of a rare file */
    private final RareFileDefinition rareDefinition;

    protected final FileManagerController fileManagerController;

    /**
	 * Creates a new <tt>FileManager</tt> instance.
	 */
    @Inject
    public FileManagerImpl(FileManagerController fileManagerController) {
        this.fileManagerController = fileManagerController;
        rareDefinition = new RareFileDefinition();
        resetVariables();
    }

    /**
     * Method that resets all of the variables for this class, maintaining
     * all invariants.  This is necessary, for example, when the shared
     * files are reloaded.
     */
    private void resetVariables() {
        _filesSize = 0;
        _numFiles = 0;
        _numIncompleteFiles = 0;
        _numPendingFiles = 0;
        _numForcedFiles = 0;
        _files = new ArrayList<FileDesc>();
        _keywordTrie = new StringTrie<IntSet>(true);
        _incompleteKeywordTrie = new StringTrie<IntSet>(true);
        _urnMap = new HashMap<URN, IntSet>();
        _extensions = new HashSet<String>();
        _completelySharedDirectories = new HashSet<File>();
        _incompletesShared = new IntSet();
        _fileToFileDescMap = new HashMap<File, FileDesc>();
        _individualSharedFiles = Collections.synchronizedCollection(new MultiCollection<File>(_transientSharedFiles, _data.SPECIAL_FILES_TO_SHARE));
        _storeToFileDescMap = new HashMap<File, FileDesc>();
        _storeDirectories = new HashSet<File>();
    }

    public void start() {
        _data.clean();
        cleanIndividualFiles();
        loadSettings();
    }

    public void startAndWait(long timeout) throws InterruptedException, TimeoutException {
        final CountDownLatch startedLatch = new CountDownLatch(1);
        FileEventListener listener = new FileEventListener() {

            public void handleFileEvent(FileManagerEvent evt) {
                if (evt.getType() == Type.FILEMANAGER_LOADED) {
                    startedLatch.countDown();
                }
            }
        };
        try {
            addFileEventListener(listener);
            start();
            if (!startedLatch.await(timeout, TimeUnit.MILLISECONDS)) {
                throw new TimeoutException("Initialization of FileManager did not complete within " + timeout + " ms");
            }
        } finally {
            removeFileEventListener(listener);
        }
    }

    public void stop() {
        save();
        shutdown = true;
    }

    protected void save() {
        _data.save();
        fileManagerController.save();
    }

    public int getSize() {
        return ByteOrder.long2int(_filesSize);
    }

    public int getNumFiles() {
        return _numFiles - _numForcedFiles;
    }

    public int getNumStoreFiles() {
        return _storeToFileDescMap.size();
    }

    public int getNumIncompleteFiles() {
        return _numIncompleteFiles;
    }

    public int getNumPendingFiles() {
        return _numPendingFiles;
    }

    public int getNumForcedFiles() {
        return _numForcedFiles;
    }

    public synchronized FileDesc get(int i) {
        return _files.get(i);
    }

    public synchronized boolean isValidIndex(int i) {
        return (i >= 0 && i < _files.size());
    }

    public synchronized URN getURNForFile(File f) {
        FileDesc fd = getFileDescForFile(f);
        if (fd != null) return fd.getSHA1Urn();
        return null;
    }

    public synchronized FileDesc getFileDescForFile(File f) {
        try {
            f = FileUtils.getCanonicalFile(f);
        } catch (IOException ioe) {
            return null;
        }
        if (_fileToFileDescMap.containsKey(f)) return _fileToFileDescMap.get(f); else return _storeToFileDescMap.get(f);
    }

    public synchronized boolean isUrnShared(final URN urn) {
        FileDesc fd = getFileDescForUrn(urn);
        return fd != null && !(fd instanceof IncompleteFileDesc);
    }

    public synchronized FileDesc getFileDescForUrn(final URN urn) {
        if (!urn.isSHA1()) throw new IllegalArgumentException();
        IntSet indices = _urnMap.get(urn);
        if (indices == null) return null;
        IntSet.IntSetIterator iter = indices.iterator();
        FileDesc ret = null;
        while (iter.hasNext() && (ret == null || ret instanceof IncompleteFileDesc)) {
            int index = iter.next();
            ret = _files.get(index);
        }
        return ret;
    }

    public synchronized FileDesc[] getIncompleteFileDescriptors() {
        if (_incompletesShared == null) return null;
        FileDesc[] ret = new FileDesc[_incompletesShared.size()];
        IntSet.IntSetIterator iter = _incompletesShared.iterator();
        for (int i = 0; iter.hasNext(); i++) {
            FileDesc fd = _files.get(iter.next());
            assert fd != null : "Directory has null entry";
            ret[i] = fd;
        }
        return ret;
    }

    public synchronized FileDesc[] getAllSharedFileDescriptors() {
        FileDesc[] fds = new FileDesc[_fileToFileDescMap.size()];
        fds = _fileToFileDescMap.values().toArray(fds);
        return fds;
    }

    /**
     * Returns a list of all shared file descriptors in the given directory,
     * in any order.
     * 
     * Returns null if directory is not shared, or a zero-length array if it is
     * shared but contains no files.  This method is not recursive; files in 
     * any of the directory's children are not returned.
     * 
     * This operation is <b>not</b> efficient, and should not be done often.
     */
    public synchronized List<FileDesc> getSharedFilesInDirectory(File directory) {
        if (directory == null) throw new NullPointerException("null directory");
        try {
            directory = FileUtils.getCanonicalFile(directory);
        } catch (IOException e) {
            return Collections.emptyList();
        }
        List<FileDesc> shared = new ArrayList<FileDesc>();
        for (FileDesc fd : _fileToFileDescMap.values()) {
            if (directory.equals(fd.getFile().getParentFile())) shared.add(fd);
        }
        return shared;
    }

    public void loadSettings() {
        final int currentRevision = ++_revision;
        if (LOG.isDebugEnabled()) LOG.debug("Starting new library revision: " + currentRevision);
        LOADER.execute(new Runnable() {

            public void run() {
                loadStarted(currentRevision);
                loadSettingsInternal(currentRevision);
            }
        });
    }

    public void loadSettingsAndWait(long timeout) throws InterruptedException, TimeoutException {
        final CountDownLatch loadedLatch = new CountDownLatch(1);
        FileEventListener listener = new FileEventListener() {

            public void handleFileEvent(FileManagerEvent evt) {
                if (evt.getType() == Type.FILEMANAGER_LOADED) {
                    loadedLatch.countDown();
                }
            }
        };
        try {
            addFileEventListener(listener);
            loadSettings();
            if (!loadedLatch.await(timeout, TimeUnit.MILLISECONDS)) {
                throw new TimeoutException("Loading of FileManager settings did not complete within " + timeout + " ms");
            }
        } finally {
            removeFileEventListener(listener);
        }
    }

    public void loadWithNewDirectories(Set<? extends File> shared, Set<File> blackListSet) {
        SharingSettings.DIRECTORIES_TO_SHARE.setValue(shared);
        synchronized (_data.DIRECTORIES_NOT_TO_SHARE) {
            _data.DIRECTORIES_NOT_TO_SHARE.clear();
            _data.DIRECTORIES_NOT_TO_SHARE.addAll(canonicalize(blackListSet));
            _storeDirectories.clear();
            _storeDirectories.add(SharingSettings.getSaveLWSDirectory());
        }
        loadSettings();
    }

    /**
     * Kicks off necessary stuff for a load being started.
     */
    protected void loadStarted(int revision) {
        fileManagerController.loadStarted();
    }

    /**
     * Notification that something finished loading.
     */
    private void tryToFinish() {
        int revision;
        synchronized (this) {
            if (_pendingFinished != _updatingFinished || _pendingFinished != _revision || _loadingFinished >= _revision) return;
            _loadingFinished = _revision;
            revision = _loadingFinished;
        }
        loadFinished(revision);
    }

    /**
     * Kicks off necessary stuff for loading being done.
     */
    protected void loadFinished(int revision) {
        if (LOG.isDebugEnabled()) LOG.debug("Finished loading revision: " + revision);
        trim();
        fileManagerController.loadFinished();
        save();
        fileManagerController.loadFinishedPostSave();
        dispatchFileEvent(new FileManagerEvent(this, Type.FILEMANAGER_LOADED));
    }

    public boolean isLoadFinished() {
        return _loadingFinished == _revision;
    }

    public boolean isUpdating() {
        return _isUpdating;
    }

    /** 
     * Loads all shared files, putting them in a queue for being added.
     *
     * If the current revision ever changed from the expected revision, this returns
     * immediately.
     */
    protected void loadSettingsInternal(int revision) {
        if (LOG.isDebugEnabled()) LOG.debug("Loading Library Revision: " + revision);
        final File[] directories;
        synchronized (this) {
            resetVariables();
            String[] extensions = StringArraySetting.decode(SharingSettings.EXTENSIONS_TO_SHARE.getValue().toLowerCase());
            for (String ext : extensions) {
                _extensions.add(ext);
            }
            if (SharingSettings.EXTENSIONS_LIST_CUSTOM.getValue().length() > 0) {
                String[] array = StringArraySetting.decode(SharingSettings.EXTENSIONS_LIST_CUSTOM.getValue());
                for (String ext : array) {
                    _extensions.add(ext);
                }
            }
            if (SharingSettings.DISABLE_SENSITIVE.getValue()) {
                for (String ext : SharingSettings.getDefaultDisabledExtensions()) {
                    _extensions.remove(ext);
                }
            }
            if (SharingSettings.EXTENSIONS_LIST_UNSHARED.getValue().length() > 0) {
                String[] array = StringArraySetting.decode(SharingSettings.EXTENSIONS_LIST_UNSHARED.getValue());
                for (String ext : array) {
                    _extensions.remove(ext);
                }
            }
            directories = SharingSettings.DIRECTORIES_TO_SHARE.getValueAsArray();
            Arrays.sort(directories, new Comparator<File>() {

                public int compare(File a, File b) {
                    return a.toString().length() - b.toString().length();
                }
            });
        }
        fileManagerController.fileManagerLoading();
        dispatchFileEvent(new FileManagerEvent(this, Type.FILEMANAGER_LOADING));
        updateSharedDirectories(SharingUtils.PROGRAM_SHARE, null, revision);
        updateSharedDirectories(SharingUtils.PREFERENCE_SHARE, null, revision);
        _isUpdating = true;
        for (int i = 0; i < directories.length && _revision == revision; i++) updateSharedDirectories(directories[i], null, revision);
        Collection<File> specialFiles = _individualSharedFiles;
        ArrayList<File> list;
        synchronized (specialFiles) {
            list = new ArrayList<File>(specialFiles);
        }
        for (File file : list) {
            if (_revision != revision) break;
            addFileIfSharedOrStore(file, EMPTY_DOCUMENTS, true, _revision, null, AddType.ADD_SHARE);
        }
        _isUpdating = false;
        trim();
        if (LOG.isDebugEnabled()) LOG.debug("Finished queueing shared files for revision: " + revision);
        synchronized (this) {
            _updatingFinished = revision;
            if (_numPendingFiles == 0) _pendingFinished = revision;
        }
        tryToFinish();
    }

    private void updateSharedDirectories(File directory, File parent, int revision) {
        updateSharedDirectories(directory, directory, parent, revision, 1);
    }

    /**
     * Recursively adds this directory and all subdirectories to the shared
     * directories as well as queueing their files for sharing.  Does nothing
     * if <tt>directory</tt> doesn't exist, isn't a directory, or has already
     * been added.  This method is thread-safe.  It acquires locks on a
     * per-directory basis.  If the current revision ever changes from the
     * expected revision, this returns immediately.
     * 
     * @requires directory is part of DIRECTORIES_TO_SHARE or one of its
     *           children, and parent is directory's shared parent or null if
     *           directory's parent is not shared.
     * @modifies this
     */
    private void updateSharedDirectories(File rootShare, File directory, File parent, int revision, int depth) {
        try {
            directory = FileUtils.getCanonicalFile(directory);
        } catch (IOException e) {
            return;
        }
        if (!directory.exists()) return;
        if (!isFolderShareable(directory, true)) return;
        if (SharingUtils.isSensitiveDirectory(directory)) {
            if (_data.SENSITIVE_DIRECTORIES_NOT_TO_SHARE.contains(directory)) {
                return;
            }
            if (!_data.SENSITIVE_DIRECTORIES_VALIDATED.contains(directory)) {
                if (!fileManagerController.warnAboutSharingSensitiveDirectory(directory)) return;
            }
        }
        if (_revision != revision) return;
        boolean isForcedShare = SharingUtils.isForcedShareDirectory(directory);
        synchronized (this) {
            if (_completelySharedDirectories.contains(directory)) return;
            if (!_storeDirectories.contains(directory)) _completelySharedDirectories.add(directory);
            if (!isForcedShare) {
                dispatchFileEvent(new FileManagerEvent(this, Type.ADD_FOLDER, rootShare, depth, directory, parent));
            }
        }
        File[] file_list = directory.listFiles(SHAREABLE_FILE_FILTER);
        if (file_list == null) return;
        for (int i = 0; i < file_list.length && _revision == revision; i++) addFileIfSharedOrStore(file_list[i], EMPTY_DOCUMENTS, true, _revision, null, AddType.ADD_SHARE);
        if (_revision != revision) return;
        if (isForcedShare) return;
        File[] dir_list = directory.listFiles(DIRECTORY_FILTER);
        if (dir_list != null) {
            for (int i = 0; i < dir_list.length && _revision == revision; i++) updateSharedDirectories(rootShare, dir_list[i], directory, revision, depth + 1);
        }
    }

    public void removeFolderIfShared(File folder) {
        _isUpdating = true;
        removeFolderIfShared(folder, null);
        _isUpdating = false;
    }

    /**
	 * Removes a given directory from being completed shared.
	 * If 'parent' is null, this will remove it from the root-level of
	 * shared folders if it existed there.  (If it is non-null & it was
	 * a root-level shared folder, the folder remains shared.)
	 *
	 * The first time this is called, parent must be non-null in order to ensure
	 * it works correctly.  Otherwise, we'll end up adding tons of stuff
	 * to the DIRECTORIES_NOT_TO_SHARE.
	 */
    protected void removeFolderIfShared(File folder, File parent) {
        if (!folder.isDirectory() && folder.exists()) throw new IllegalArgumentException("Expected a directory, but given: " + folder);
        try {
            folder = FileUtils.getCanonicalFile(folder);
        } catch (IOException ignored) {
        }
        boolean contained;
        synchronized (this) {
            contained = _completelySharedDirectories.contains(folder);
        }
        if (contained) {
            if (parent != null && SharingSettings.DIRECTORIES_TO_SHARE.contains(folder)) {
                return;
            } else if (parent == null) {
                boolean explicitlyShared = SharingSettings.DIRECTORIES_TO_SHARE.remove(folder);
                if (!explicitlyShared || isFolderShared(folder.getParentFile())) _data.DIRECTORIES_NOT_TO_SHARE.add(folder);
            }
            synchronized (this) {
                _completelySharedDirectories.remove(folder);
            }
            File[] subs = folder.listFiles();
            if (subs != null) {
                for (int i = 0; i < subs.length; i++) {
                    File f = subs[i];
                    if (f.isDirectory()) removeFolderIfShared(f, folder); else if (f.isFile() && !_individualSharedFiles.contains(f)) {
                        if (removeFileIfShared(f) == null) fileManagerController.clearPendingShare(f);
                        if (isStoreFile(f)) _data.SPECIAL_STORE_FILES.remove(f);
                    }
                }
            }
            dispatchFileEvent(new FileManagerEvent(this, Type.REMOVE_FOLDER, folder));
        }
    }

    public void addSharedFolders(Set<File> folders, Set<File> blackListedSet) {
        if (folders.isEmpty()) {
            throw new IllegalArgumentException("Only blacklisting without sharing, not allowed");
        }
        _data.DIRECTORIES_NOT_TO_SHARE.addAll(canonicalize(blackListedSet));
        for (File folder : folders) {
            addSharedFolder(folder);
        }
    }

    /**
	 * Returns set of canonicalized files or the same set if there
	 * was an IOException for one of the files while canconicalizing. 
	 */
    private static Set<File> canonicalize(Set<File> files) {
        Set<File> canonical = new HashSet<File>(files.size());
        try {
            for (File excluded : files) {
                canonical.add(FileUtils.getCanonicalFile(excluded));
            }
        } catch (IOException ie) {
            canonical = files;
        }
        return canonical;
    }

    public Set<File> getFolderNotToShare() {
        synchronized (_data.DIRECTORIES_NOT_TO_SHARE) {
            return new HashSet<File>(_data.DIRECTORIES_NOT_TO_SHARE);
        }
    }

    public boolean addSharedFolder(File folder) {
        if (!folder.isDirectory()) throw new IllegalArgumentException("Expected a directory, but given: " + folder);
        try {
            folder = FileUtils.getCanonicalFile(folder);
        } catch (IOException ignored) {
        }
        if (!isFolderShareable(folder, false)) return false;
        _data.DIRECTORIES_NOT_TO_SHARE.remove(folder);
        if (!isFolderShared(folder.getParentFile())) SharingSettings.DIRECTORIES_TO_SHARE.add(folder);
        _isUpdating = true;
        updateSharedDirectories(folder, null, _revision);
        _isUpdating = false;
        return true;
    }

    public void addFileAlways(File file) {
        addFileAlways(file, EMPTY_DOCUMENTS, null);
    }

    public void addFileAlways(File file, FileEventListener callback) {
        addFileAlways(file, EMPTY_DOCUMENTS, callback);
    }

    public void addFileAlways(File file, List<? extends LimeXMLDocument> list) {
        addFileAlways(file, list, null);
    }

    public void addFileAlways(File file, List<? extends LimeXMLDocument> list, FileEventListener callback) {
        _data.FILES_NOT_TO_SHARE.remove(file);
        if (!isFileShareable(file)) _data.SPECIAL_FILES_TO_SHARE.add(file);
        addFileIfSharedOrStore(file, list, true, _revision, callback, AddType.ADD_SHARE);
    }

    public void addFileForSession(File file) {
        addFileForSession(file, null);
    }

    public void addFileForSession(File file, FileEventListener callback) {
        _data.FILES_NOT_TO_SHARE.remove(file);
        if (!isFileShareable(file)) _transientSharedFiles.add(file);
        addFileIfSharedOrStore(file, EMPTY_DOCUMENTS, true, _revision, callback, AddType.ADD_SHARE);
    }

    public void addFileIfShared(File file) {
        addFileIfSharedOrStore(file, EMPTY_DOCUMENTS, true, _revision, null, AddType.ADD_SHARE);
    }

    public void addFileIfShared(File file, FileEventListener callback) {
        addFileIfSharedOrStore(file, EMPTY_DOCUMENTS, true, _revision, callback, AddType.ADD_SHARE);
    }

    public void addFileIfShared(File file, List<? extends LimeXMLDocument> list) {
        addFileIfSharedOrStore(file, list, true, _revision, null, AddType.ADD_SHARE);
    }

    public void addFileIfShared(File file, List<? extends LimeXMLDocument> list, FileEventListener callback) {
        addFileIfSharedOrStore(file, list, true, _revision, callback, AddType.ADD_SHARE);
    }

    /**
     * Adds a file that is either associated with the store or is shared. Tries to create 
     * a FileDescriptor for the file and ew. Files are handled differently depending on their AddType. 
     * 
     * 
     * @param file - the file to be added
     * @param metadata - any LimeXMLDocs associated with this file
     * @param notify - if true signals the front-end via 
     *        ActivityCallback.handleFileManagerEvent() about the Event
     * @param revision - current  version of LimeXMLDocs being used
     * @param callback - the listener to notify about the event
     * @param addType - type of add that for this file
     */
    protected void addFileIfSharedOrStore(File file, List<? extends LimeXMLDocument> metadata, boolean notify, int revision, FileEventListener callback, AddType addFileType) {
        if (LOG.isDebugEnabled()) LOG.debug("Attempting to load store or shared file: " + file);
        if (callback == null) callback = EMPTY_CALLBACK;
        try {
            file = FileUtils.getCanonicalFile(file);
        } catch (IOException e) {
            callback.handleFileEvent(new FileManagerEvent(this, addFileType.getFailureType(), file));
            return;
        }
        if (!isFileShareable(file) && !isFileLocatedStoreDirectory(file)) {
            _individualSharedFiles.remove(file);
            callback.handleFileEvent(new FileManagerEvent(FileManagerImpl.this, addFileType.getFailureType(), file));
            return;
        }
        if (isStoreFile(file)) {
            return;
        }
        if (isFileShared(file)) {
            callback.handleFileEvent(new FileManagerEvent(FileManagerImpl.this, Type.ALREADY_SHARED_FILE, file));
            return;
        }
        synchronized (this) {
            if (revision != _revision) {
                callback.handleFileEvent(new FileManagerEvent(this, addFileType.getFailureType(), file));
                return;
            }
            _numPendingFiles++;
            _pendingFinished = -1;
        }
        fileManagerController.calculateAndCacheUrns(file, getNewUrnCallback(file, metadata, notify, revision, callback, addFileType));
    }

    /**
     * Constructs a new UrnCallback that will possibly load the file with the given URNs.
     */
    protected UrnCallback getNewUrnCallback(final File file, final List<? extends LimeXMLDocument> metadata, final boolean notify, final int revision, final FileEventListener callback, final AddType addFileType) {
        return new UrnCallback() {

            public void urnsCalculated(File f, Set<? extends URN> urns) {
                FileDesc fd = null;
                synchronized (FileManagerImpl.this) {
                    if (revision != _revision) {
                        LOG.warn("Revisions changed, dropping share.");
                        callback.handleFileEvent(new FileManagerEvent(FileManagerImpl.this, addFileType.getFailureType(), file));
                        return;
                    }
                    _numPendingFiles--;
                    if (!urns.isEmpty()) {
                        int fileIndex = _files.size();
                        fd = createFileDesc(file, urns, fileIndex);
                    }
                }
                if (fd == null) {
                    callback.handleFileEvent(new FileManagerEvent(FileManagerImpl.this, addFileType.getFailureType(), file));
                    return;
                }
                loadFile(fd, file, metadata, urns);
                if (isStoreXML(fd.getXMLDocument())) {
                    addStoreFile(fd, file, urns, addFileType, notify, callback);
                } else if (addFileType == AddType.ADD_SHARE) {
                    addSharedFile(file, fd, urns, addFileType, notify, callback);
                }
                boolean finished = false;
                synchronized (this) {
                    if (_numPendingFiles == 0) {
                        _pendingFinished = revision;
                        finished = true;
                    }
                }
                if (finished) {
                    tryToFinish();
                }
            }

            public boolean isOwner(Object o) {
                return o == fileManagerController;
            }
        };
    }

    /**
     * Loads a single shared file.
     */
    protected void loadFile(FileDesc fd, File file, List<? extends LimeXMLDocument> metadata, Set<? extends URN> urns) {
    }

    /**
     * Creates a file descriptor for the given file and places the fd into the set
     * of LWS file descriptors
     */
    private synchronized void addStoreFile(FileDesc fd, File file, Set<? extends URN> urns, AddType addFileType, final boolean notify, final FileEventListener callback) {
        if (LOG.isDebugEnabled()) LOG.debug("Sharing file: " + file);
        if (addFileType == AddType.ADD_SHARE) _data.SPECIAL_STORE_FILES.add(file);
        FileDesc fileDesc = createFileDesc(file, urns, STORE_FILEDESC_INDEX);
        if (fd.getXMLDocument() != null) fileDesc.addLimeXMLDocument(fd.getXMLDocument());
        _storeToFileDescMap.put(file, fileDesc);
        FileManagerEvent evt = new FileManagerEvent(FileManagerImpl.this, Type.ADD_STORE_FILE, fileDesc);
        if (notify) dispatchFileEvent(evt);
        callback.handleFileEvent(evt);
    }

    /**
     * Handles the actual sharing of a file by placing the file descriptor into the set of shared files
     */
    private synchronized void addSharedFile(File file, FileDesc fileDesc, Set<? extends URN> urns, AddType addFileType, final boolean notify, final FileEventListener callback) {
        if (LOG.isDebugEnabled()) LOG.debug("Sharing file: " + file);
        if (fileDesc.getIndex() != _files.size()) {
            LimeXMLDocument doc = fileDesc.getXMLDocument();
            fileDesc = createFileDesc(file, urns, _files.size());
            if (doc != null) fileDesc.addLimeXMLDocument(doc);
        }
        long fileLength = file.length();
        _filesSize += fileLength;
        _files.add(fileDesc);
        _fileToFileDescMap.put(file, fileDesc);
        _numFiles++;
        File parent = file.getParentFile();
        assert parent != null : "Null parent to \"" + file + "\"";
        if (SharingUtils.isForcedShareDirectory(parent)) _numForcedFiles++;
        loadKeywords(_keywordTrie, fileDesc);
        if (!SharingUtils.isForcedShare(file)) {
            fileManagerController.fileAdded(file, fileDesc.getSHA1Urn());
        }
        this.updateUrnIndex(fileDesc);
        _needRebuild = true;
        FileManagerEvent evt = new FileManagerEvent(FileManagerImpl.this, addFileType.getSuccessType(), fileDesc);
        if (notify) dispatchFileEvent(evt);
        callback.handleFileEvent(evt);
    }

    /**
     * @param trie to update
     * @param fd to load keywords from
     */
    private void loadKeywords(StringTrie<IntSet> trie, FileDesc fd) {
        String[] keywords = extractKeywords(fd);
        for (int i = 0; i < keywords.length; i++) {
            String keyword = keywords[i];
            IntSet indices = trie.get(keyword);
            if (indices == null) {
                indices = new IntSet();
                trie.add(keyword, indices);
            }
            indices.add(fd.getIndex());
        }
    }

    /**
     * Creates a file descriptor for a given file and a set of urns
     * @param file - file to create descriptor for
     * @param urns - urns to use
     * @param index - index to use
     * @return
     */
    private FileDesc createFileDesc(File file, Set<? extends URN> urns, int index) {
        FileDesc fileDesc = new FileDesc(file, urns, index);
        ContentResponseData r = fileManagerController.getResponseDataFor(fileDesc.getSHA1Urn());
        if (r != null && !r.isOK()) return null; else return fileDesc;
    }

    public synchronized FileDesc stopSharingFile(File file) {
        try {
            file = FileUtils.getCanonicalFile(file);
        } catch (IOException e) {
            return null;
        }
        boolean removed = _individualSharedFiles.remove(file);
        FileDesc fd = removeFileIfShared(file);
        if (fd == null) {
            fileManagerController.clearPendingShare(file);
        } else {
            file = fd.getFile();
            if (!removed) _data.FILES_NOT_TO_SHARE.add(file);
        }
        return fd;
    }

    public synchronized FileDesc removeFileIfShared(File f) {
        return removeFileIfShared(f, true);
    }

    /**
     * The actual implementation of removeFileIfShared(File)
     */
    protected synchronized FileDesc removeFileIfShared(File f, boolean notify) {
        try {
            f = FileUtils.getCanonicalFile(f);
        } catch (IOException e) {
            return null;
        }
        FileDesc fd = _fileToFileDescMap.get(f);
        if (fd == null) return null;
        int i = fd.getIndex();
        assert _files.get(i).getFile().equals(f) : "invariant broken!";
        _files.set(i, null);
        _fileToFileDescMap.remove(f);
        _needRebuild = true;
        if (fd instanceof IncompleteFileDesc) {
            removeUrnIndex(fd, false);
            removeKeywords(_incompleteKeywordTrie, fd);
            _numIncompleteFiles--;
            boolean removed = _incompletesShared.remove(i);
            assert removed : "File " + i + " not found in " + _incompletesShared;
            if (notify) {
                FileManagerEvent evt = new FileManagerEvent(this, Type.REMOVE_FILE, fd);
                dispatchFileEvent(evt);
            }
            return fd;
        }
        _numFiles--;
        _filesSize -= fd.getFileSize();
        File parent = f.getParentFile();
        if (SharingUtils.isForcedShareDirectory(parent)) {
            notify = false;
            _numForcedFiles--;
        }
        removeKeywords(_keywordTrie, fd);
        removeUrnIndex(fd, true);
        if (notify) {
            FileManagerEvent evt = new FileManagerEvent(this, Type.REMOVE_FILE, fd);
            dispatchFileEvent(evt);
        }
        return fd;
    }

    private void removeKeywords(StringTrie<IntSet> trie, FileDesc fd) {
        String[] keywords = extractKeywords(fd);
        for (int j = 0; j < keywords.length; j++) {
            String keyword = keywords[j];
            IntSet indices = trie.get(keyword);
            if (indices != null) {
                indices.remove(fd.getIndex());
                if (indices.size() == 0) trie.remove(keyword);
            }
        }
    }

    protected synchronized FileDesc removeStoreFile(File f, boolean notify) {
        try {
            f = FileUtils.getCanonicalFile(f);
        } catch (IOException e) {
            return null;
        }
        FileDesc fd = _storeToFileDescMap.get(f);
        if (fd == null) return null;
        _data.SPECIAL_STORE_FILES.remove(f);
        _storeToFileDescMap.remove(f);
        if (notify) {
            FileManagerEvent evt = new FileManagerEvent(this, Type.REMOVE_STORE_FILE, fd);
            dispatchFileEvent(evt);
        }
        return fd;
    }

    public synchronized void addIncompleteFile(File incompleteFile, Set<? extends URN> urns, String name, long size, VerifyingFile vf) {
        try {
            incompleteFile = FileUtils.getCanonicalFile(incompleteFile);
        } catch (IOException ioe) {
            return;
        }
        for (URN urn : urns) {
            if (!urn.isSHA1()) continue;
            IntSet shared = _urnMap.get(urn);
            if (shared == null) continue;
            for (IntSet.IntSetIterator isIter = shared.iterator(); isIter.hasNext(); ) {
                int i = isIter.next();
                FileDesc desc = _files.get(i);
                if (desc == null) continue;
                String incPath = incompleteFile.getAbsolutePath();
                String path = desc.getFile().getAbsolutePath();
                if (incPath.equals(path)) return;
            }
        }
        int fileIndex = _files.size();
        _incompletesShared.add(fileIndex);
        IncompleteFileDesc ifd = new IncompleteFileDesc(incompleteFile, urns, fileIndex, name, size, vf);
        _files.add(ifd);
        _fileToFileDescMap.put(incompleteFile, ifd);
        fileURNSUpdated(ifd);
        _numIncompleteFiles++;
        _needRebuild = true;
        dispatchFileEvent(new FileManagerEvent(this, Type.ADD_FILE, ifd));
    }

    public abstract void fileChanged(File f);

    public void validate(final FileDesc fd) {
        if (_requestingValidation.add(fd.getSHA1Urn())) {
            fileManagerController.requestValidation(fd.getSHA1Urn(), new ContentResponseObserver() {

                public void handleResponse(URN urn, ContentResponseData r) {
                    _requestingValidation.remove(fd.getSHA1Urn());
                    if (r != null && !r.isOK()) removeFileIfShared(fd.getFile());
                }
            });
        }
    }

    public synchronized void fileURNSUpdated(FileDesc fd) {
        updateUrnIndex(fd);
        if (fd instanceof IncompleteFileDesc) {
            IncompleteFileDesc ifd = (IncompleteFileDesc) fd;
            if (SharingSettings.ALLOW_PARTIAL_SHARING.getValue() && SharingSettings.LOAD_PARTIAL_KEYWORDS.getValue() && ifd.hasUrnsAndPartialData()) {
                loadKeywords(_incompleteKeywordTrie, fd);
                _needRebuild = true;
            }
        }
    }

    /**
     * @modifies this
     * @effects enters the given FileDesc into the _urnMap under all its 
     * reported URNs
     */
    private synchronized void updateUrnIndex(FileDesc fileDesc) {
        for (URN urn : fileDesc.getUrns()) {
            if (!urn.isSHA1()) continue;
            IntSet indices = _urnMap.get(urn);
            if (indices == null) {
                indices = new IntSet();
                _urnMap.put(urn, indices);
            }
            indices.add(fileDesc.getIndex());
        }
    }

    /**
     * Utility method to perform standardized keyword extraction for the given
     * <tt>FileDesc</tt>.  This handles extracting keywords according to 
     * locale-specific rules.
     * 
     * @param fd the <tt>FileDesc</tt> containing a file system path with 
     *  keywords to extact
     * @return an array of keyword strings for the given file
     */
    private static String[] extractKeywords(FileDesc fd) {
        return StringUtils.split(I18NConvert.instance().getNorm(fd.getPath()), DELIMITERS);
    }

    /** 
     * Removes any URN index information for desc
     * @param purgeState true if any state should also be removed (creation time, altlocs) 
     */
    private synchronized void removeUrnIndex(FileDesc fileDesc, boolean purgeState) {
        for (URN urn : fileDesc.getUrns()) {
            if (!urn.isSHA1()) continue;
            IntSet indices = _urnMap.get(urn);
            if (indices == null) {
                assert fileDesc instanceof IncompleteFileDesc;
                return;
            }
            indices.remove(fileDesc.getIndex());
            if (indices.size() == 0 && purgeState) {
                fileManagerController.lastUrnRemoved(urn);
                _urnMap.remove(urn);
            }
        }
    }

    public void renameFileIfSharedOrStore(File oldName, File newName) {
        renameFileIfSharedOrStore(oldName, newName, null);
    }

    public synchronized void renameFileIfSharedOrStore(final File oldName, final File newName, final FileEventListener callback) {
        FileDesc toRemove = getFileDescForFile(oldName);
        if (toRemove == null) {
            FileManagerEvent evt = new FileManagerEvent(this, Type.ADD_FAILED_FILE, oldName);
            dispatchFileEvent(evt);
            if (callback != null) callback.handleFileEvent(evt);
            return;
        }
        if (LOG.isDebugEnabled()) LOG.debug("Attempting to rename: " + oldName + " to: " + newName);
        List<LimeXMLDocument> xmlDocs = new LinkedList<LimeXMLDocument>(toRemove.getLimeXMLDocuments());
        if (toRemove.getIndex() != STORE_FILEDESC_INDEX) {
            final FileDesc removed = removeFileIfShared(oldName, false);
            assert removed == toRemove : "invariant broken.";
            if (_data.SPECIAL_FILES_TO_SHARE.remove(oldName) && !isFileInCompletelySharedDirectory(newName)) _data.SPECIAL_FILES_TO_SHARE.add(newName);
            fileManagerController.addUrns(newName, removed.getUrns());
            addFileIfSharedOrStore(newName, xmlDocs, false, _revision, new FileEventListener() {

                public void handleFileEvent(FileManagerEvent evt) {
                    if (LOG.isDebugEnabled()) LOG.debug("Add of newFile returned callback: " + evt);
                    FileManagerEvent newEvt = null;
                    if (evt.isAddEvent()) {
                        FileDesc fd = evt.getFileDescs()[0];
                        newEvt = new FileManagerEvent(FileManagerImpl.this, Type.RENAME_FILE, removed, fd);
                    } else {
                        newEvt = new FileManagerEvent(FileManagerImpl.this, Type.REMOVE_FILE, removed);
                    }
                    dispatchFileEvent(newEvt);
                    if (callback != null) callback.handleFileEvent(newEvt);
                }
            }, AddType.ADD_SHARE);
        } else {
            final FileDesc removed = removeStoreFile(oldName, false);
            assert removed == toRemove : "invariant broken.";
            if (_data.SPECIAL_STORE_FILES.remove(oldName)) _data.SPECIAL_STORE_FILES.add(newName);
            fileManagerController.addUrns(newName, removed.getUrns());
            addFileIfSharedOrStore(newName, xmlDocs, false, _revision, new FileEventListener() {

                public void handleFileEvent(FileManagerEvent evt) {
                    FileManagerEvent newEvt = null;
                    if (evt.isAddStoreEvent()) {
                        FileDesc fd = evt.getFileDescs()[0];
                        newEvt = new FileManagerEvent(FileManagerImpl.this, Type.RENAME_FILE, removed, fd);
                    } else {
                        newEvt = new FileManagerEvent(FileManagerImpl.this, Type.REMOVE_STORE_FILE, removed);
                    }
                    dispatchFileEvent(newEvt);
                    if (callback != null) callback.handleFileEvent(newEvt);
                }
            }, AddType.ADD_STORE);
        }
    }

    /** Ensures that this's index takes the minimum amount of space.  Only
     *  affects performance, not correctness; hence no modifies clause. */
    private synchronized void trim() {
        _keywordTrie.trim(new Function<IntSet, IntSet>() {

            public IntSet apply(IntSet intSet) {
                intSet.trim();
                return intSet;
            }
        });
    }

    public void validateSensitiveFile(File dir) {
        _data.SENSITIVE_DIRECTORIES_VALIDATED.add(dir);
        _data.SENSITIVE_DIRECTORIES_NOT_TO_SHARE.remove(dir);
    }

    public void invalidateSensitiveFile(File dir) {
        _data.SENSITIVE_DIRECTORIES_VALIDATED.remove(dir);
        _data.SENSITIVE_DIRECTORIES_NOT_TO_SHARE.add(dir);
        SharingSettings.DIRECTORIES_TO_SHARE.remove(dir);
    }

    public boolean hasIndividualFiles() {
        return !_data.SPECIAL_FILES_TO_SHARE.isEmpty();
    }

    public boolean hasIndividualStoreFiles() {
        return !_data.SPECIAL_STORE_FILES.isEmpty();
    }

    public boolean hasApplicationSharedFiles() {
        File[] files = SharingUtils.APPLICATION_SPECIAL_SHARE.listFiles();
        if (files == null) return false;
        for (File f : files) {
            if (isFileShared(f)) return true;
        }
        return false;
    }

    public File[] getIndividualFiles() {
        Set<File> candidates = _data.SPECIAL_FILES_TO_SHARE;
        synchronized (candidates) {
            ArrayList<File> files = new ArrayList<File>(candidates.size());
            for (File f : candidates) {
                if (f.exists()) files.add(f);
            }
            if (files.isEmpty()) return new File[0]; else return files.toArray(new File[files.size()]);
        }
    }

    public File[] getIndividualStoreFiles() {
        Set<File> candidates = _data.SPECIAL_STORE_FILES;
        synchronized (candidates) {
            ArrayList<File> files = new ArrayList<File>(candidates.size());
            for (File f : candidates) {
                if (f.exists()) files.add(f);
            }
            if (files.isEmpty()) return new File[0]; else return files.toArray(new File[files.size()]);
        }
    }

    public boolean isIndividualStore(File f) {
        return _data.SPECIAL_STORE_FILES.contains(f);
    }

    public boolean isIndividualShare(File f) {
        return _data.SPECIAL_FILES_TO_SHARE.contains(f) && SharingUtils.isFilePhysicallyShareable(f) && !SharingUtils.isApplicationSpecialShare(f);
    }

    /**
     * Cleans all stale entries from the Set of individual files.
     */
    private void cleanIndividualFiles() {
        Set<File> files = _data.SPECIAL_FILES_TO_SHARE;
        synchronized (files) {
            for (Iterator<File> i = files.iterator(); i.hasNext(); ) {
                File f = i.next();
                if (!(SharingUtils.isFilePhysicallyShareable(f))) i.remove();
            }
        }
    }

    public synchronized boolean isFileShared(File file) {
        if (file == null) return false;
        if (_fileToFileDescMap.get(file) == null) return false;
        return true;
    }

    public boolean isRareFile(FileDesc fd) {
        return rareDefinition.evaluate(fd);
    }

    /** Returns true if file has a shareable extension.  Case is ignored. */
    private static boolean hasShareableExtension(File file) {
        if (file == null) return false;
        String filename = file.getName();
        int begin = filename.lastIndexOf(".");
        if (begin == -1) return false;
        String ext = filename.substring(begin + 1).toLowerCase();
        return _extensions.contains(ext);
    }

    public boolean isFileInCompletelySharedDirectory(File f) {
        File dir = f.getParentFile();
        if (dir == null) return false;
        synchronized (this) {
            return _completelySharedDirectories.contains(dir);
        }
    }

    public boolean isFolderShared(File dir) {
        if (dir == null) return false;
        synchronized (this) {
            return _completelySharedDirectories.contains(dir);
        }
    }

    public boolean isStoreFile(File file) {
        if (_storeToFileDescMap.containsKey(file) || _storeDirectories.contains(file)) {
            return true;
        }
        return false;
    }

    /**
	 * Returns true if the given file is in a completely shared directory
	 * or if it is specially shared.
     * NOTE: this does not determine if a file is unshareable as a result of
     * being a LWS file
	 */
    private boolean isFileShareable(File file) {
        if (!SharingUtils.isFilePhysicallyShareable(file)) return false;
        if (_individualSharedFiles.contains(file)) return true;
        if (_data.FILES_NOT_TO_SHARE.contains(file)) return false;
        if (isFileInCompletelySharedDirectory(file)) {
            if (file.getName().toUpperCase().startsWith("LIMEWIRE")) return true;
            if (!hasShareableExtension(file)) return false;
            return true;
        }
        return false;
    }

    private boolean isFileLocatedStoreDirectory(File file) {
        return (_storeDirectories.contains(file.getParentFile()));
    }

    /**
     * Returns true if the XML doc contains information regarding the LWS
     */
    private boolean isStoreXML(LimeXMLDocument doc) {
        return doc != null && doc.getLicenseString() != null && doc.getLicenseString().equals(LicenseType.LIMEWIRE_STORE_PURCHASE.name());
    }

    public boolean isStoreDirectory(File file) {
        return _storeDirectories.contains(file);
    }

    public boolean isFolderShareable(File folder, boolean includeExcludedDirectories) {
        if (!folder.isDirectory() || !folder.canRead()) return false;
        if (folder.equals(SharingSettings.INCOMPLETE_DIRECTORY.getValue())) return false;
        if (SharingUtils.isApplicationSpecialShareDirectory(folder)) {
            return false;
        }
        if (includeExcludedDirectories && _data.DIRECTORIES_NOT_TO_SHARE.contains(folder)) return false;
        File[] faRoots = File.listRoots();
        if (faRoots != null && faRoots.length > 0) {
            for (int i = 0; i < faRoots.length; i++) {
                if (folder.equals(faRoots[i])) return false;
            }
        }
        String name = folder.getName().toLowerCase(Locale.US);
        if (name.equals("cookies")) return false; else if (name.equals("low")) {
            String parent = folder.getParent();
            if (parent != null && parent.toLowerCase(Locale.US).equals("cookies")) return false;
        }
        return true;
    }

    public synchronized QueryRouteTable getQRT() {
        if (_needRebuild) {
            qrpUpdater.cancelRebuild();
            buildQRT();
            _needRebuild = false;
        }
        QueryRouteTable qrt = new QueryRouteTable(_queryRouteTable.getSize());
        qrt.addAll(_queryRouteTable);
        return qrt;
    }

    /**
     * build the qrt.  Subclasses can add other Strings to the
     * QRT by calling buildQRT and then adding directly to the 
     * _queryRouteTable variable. (see xml/MetaFileManager.java)
     */
    protected synchronized void buildQRT() {
        _queryRouteTable = new QueryRouteTable();
        if (SearchSettings.PUBLISH_LIME_KEYWORDS.getBoolean()) {
            for (String entry : SearchSettings.LIME_QRP_ENTRIES.getValue()) _queryRouteTable.addIndivisible(entry);
        }
        FileDesc[] fds = getAllSharedFileDescriptors();
        for (int i = 0; i < fds.length; i++) {
            if (fds[i] instanceof IncompleteFileDesc) {
                if (!SharingSettings.ALLOW_PARTIAL_SHARING.getValue()) continue;
                if (!SharingSettings.PUBLISH_PARTIAL_QRP.getValue()) continue;
                IncompleteFileDesc ifd = (IncompleteFileDesc) fds[i];
                if (!ifd.hasUrnsAndPartialData()) continue;
                _queryRouteTable.add(ifd.getFileName());
            } else _queryRouteTable.add(fds[i].getPath());
        }
    }

    /**
     * Constant for an empty <tt>Response</tt> array to return when there are
     * no matches.
     */
    private static final Response[] EMPTY_RESPONSES = new Response[0];

    public synchronized Response[] query(QueryRequest request) {
        String str = request.getQuery();
        boolean includeXML = shouldIncludeXMLInResponse(request);
        if (request.isWhatIsNewRequest()) return respondToWhatIsNewRequest(request, includeXML);
        if (str.equals(INDEXING_QUERY) || str.equals(BROWSE_QUERY)) return respondToIndexingQuery(includeXML);
        str = _keywordTrie.canonicalCase(str);
        IntSet matches = search(str, null, request.desiresPartialResults());
        if (request.getQueryUrns().size() > 0) matches = urnSearch(request.getQueryUrns(), matches);
        if (matches == null) return EMPTY_RESPONSES;
        List<Response> responses = new LinkedList<Response>();
        final MediaType.Aggregator filter = MediaType.getAggregator(request);
        LimeXMLDocument doc = request.getRichQuery();
        for (IntSet.IntSetIterator iter = matches.iterator(); iter.hasNext(); ) {
            int i = iter.next();
            FileDesc desc = _files.get(i);
            assert desc != null : "unexpected null in FileManager for query:\n" + request;
            if ((filter != null) && !filter.allow(desc.getFileName())) continue;
            desc.incrementHitCount();
            fileManagerController.handleSharedFileUpdate(desc.getFile());
            Response resp = fileManagerController.createResponse(desc);
            if (includeXML) {
                addXMLToResponse(resp, desc);
                if (doc != null && resp.getDocument() != null && !isValidXMLMatch(resp, doc)) continue;
            }
            responses.add(resp);
        }
        if (responses.size() == 0) return EMPTY_RESPONSES;
        return responses.toArray(new Response[responses.size()]);
    }

    /**
     * Responds to a what is new request.
     */
    private Response[] respondToWhatIsNewRequest(QueryRequest request, boolean includeXML) {
        List<URN> urnList = fileManagerController.getNewestUrns(request, 3);
        if (urnList.size() == 0) return EMPTY_RESPONSES;
        Response[] resps = new Response[urnList.size()];
        for (int i = 0; i < urnList.size(); i++) {
            URN currURN = urnList.get(i);
            FileDesc desc = getFileDescForUrn(currURN);
            if ((desc == null) || (desc instanceof IncompleteFileDesc)) throw new RuntimeException("Bad Rep - No IFDs allowed!");
            Response r = fileManagerController.createResponse(desc);
            if (includeXML) addXMLToResponse(r, desc);
            resps[i] = r;
        }
        return resps;
    }

    /** Responds to a Indexing (mostly BrowseHost) query - gets all the shared
     *  files of this client.
     */
    private Response[] respondToIndexingQuery(boolean includeXML) {
        if (_numFiles == 0) return EMPTY_RESPONSES;
        Response[] ret = new Response[_numFiles - _numForcedFiles];
        int j = 0;
        for (int i = 0; i < _files.size(); i++) {
            FileDesc desc = _files.get(i);
            if (desc == null || desc instanceof IncompleteFileDesc || SharingUtils.isForcedShare(desc)) continue;
            assert j < ret.length : "_numFiles is too small";
            ret[j] = fileManagerController.createResponse(desc);
            if (includeXML) addXMLToResponse(ret[j], desc);
            j++;
        }
        assert j == ret.length : "_numFiles is too large";
        return ret;
    }

    /**
     * A normal FileManager will never include XML.
     * It is expected that MetaFileManager overrides this and returns
     * true in some instances.
     */
    protected abstract boolean shouldIncludeXMLInResponse(QueryRequest qr);

    /**
     * This implementation does nothing.
     */
    protected abstract void addXMLToResponse(Response res, FileDesc desc);

    /**
     * Determines whether we should include the response based on XML.
     */
    protected abstract boolean isValidXMLMatch(Response res, LimeXMLDocument doc);

    /**
     * Returns a set of indices of files matching q, or null if there are no
     * matches.  Subclasses may override to provide different notions of
     * matching.  The caller of this method must not mutate the returned
     * value.
     */
    protected IntSet search(String query, IntSet priors, boolean partial) {
        IntSet ret = priors;
        for (int i = 0; i < query.length(); ) {
            if (isDelimiter(query.charAt(i))) {
                i++;
                continue;
            }
            int j;
            for (j = i + 1; j < query.length(); j++) {
                if (isDelimiter(query.charAt(j))) break;
            }
            Iterator<IntSet> iter = _keywordTrie.getPrefixedBy(query, i, j);
            if (SharingSettings.ALLOW_PARTIAL_SHARING.getValue() && SharingSettings.ALLOW_PARTIAL_RESPONSES.getValue() && partial) iter = new MultiIterator<IntSet>(iter, _incompleteKeywordTrie.getPrefixedBy(query, i, j));
            if (iter.hasNext()) {
                IntSet matches = null;
                while (iter.hasNext()) {
                    IntSet s = iter.next();
                    if (matches == null) {
                        if (i == 0 && j == query.length() && !(iter.hasNext())) return s;
                        matches = new IntSet();
                    }
                    matches.addAll(s);
                }
                if (ret == null) ret = matches; else ret.retainAll(matches);
            } else {
                return null;
            }
            if (ret.size() == 0) return null;
            i = j;
        }
        if (ret == null || ret.size() == 0) return null;
        return ret;
    }

    /**
     * Find all files with matching full URNs
     */
    private synchronized IntSet urnSearch(Iterable<URN> urnsIter, IntSet priors) {
        IntSet ret = priors;
        for (URN urn : urnsIter) {
            IntSet hits = _urnMap.get(urn);
            if (hits != null) {
                IntSet.IntSetIterator iter = hits.iterator();
                while (iter.hasNext()) {
                    FileDesc fd = _files.get(iter.next());
                    if (fd == null || fd instanceof IncompleteFileDesc) continue;
                    if (fd.containsUrn(urn)) {
                        if (ret == null) ret = new IntSet();
                        ret.add(fd.getIndex());
                    }
                }
            }
        }
        return ret;
    }

    public boolean isFileApplicationShared(String name) {
        File file = new File(SharingUtils.APPLICATION_SPECIAL_SHARE, name);
        try {
            file = FileUtils.getCanonicalFile(file);
        } catch (IOException bad) {
            return false;
        }
        return isFileShared(file);
    }

    public void addFileEventListener(FileEventListener listener) {
        if (listener == null) {
            throw new NullPointerException("FileEventListener is null");
        }
        eventListeners.addIfAbsent(listener);
    }

    public void removeFileEventListener(FileEventListener listener) {
        if (listener == null) {
            throw new NullPointerException("FileEventListener is null");
        }
        eventListeners.remove(listener);
    }

    /**
     * dispatches a FileManagerEvent to any registered listeners 
     */
    protected void dispatchFileEvent(FileManagerEvent evt) {
        for (FileEventListener listener : eventListeners) {
            listener.handleFileEvent(evt);
        }
    }

    public Iterator<Response> getIndexingIterator(final boolean includeXML) {
        return new Iterator<Response>() {

            int startRevision = _revision;

            /** Points to the index that is to be examined next. */
            int index = 0;

            Response preview;

            private boolean preview() {
                assert preview == null;
                if (_revision != startRevision) {
                    return false;
                }
                synchronized (FileManagerImpl.this) {
                    while (index < _files.size()) {
                        FileDesc desc = _files.get(index);
                        index++;
                        if (desc == null || desc instanceof IncompleteFileDesc || SharingUtils.isForcedShare(desc)) continue;
                        preview = fileManagerController.createResponse(desc);
                        if (includeXML) addXMLToResponse(preview, desc);
                        return true;
                    }
                    return false;
                }
            }

            public boolean hasNext() {
                if (_revision != startRevision) {
                    return false;
                }
                if (preview != null) {
                    synchronized (FileManagerImpl.this) {
                        if (_files.get(index - 1) == null) {
                            preview = null;
                        }
                    }
                }
                return preview != null || preview();
            }

            public Response next() {
                if (hasNext()) {
                    Response item = preview;
                    preview = null;
                    return item;
                }
                throw new NoSuchElementException();
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    private class QRPUpdater {

        private final Set<String> qrpWords = new HashSet<String>();

        public QRPUpdater() {
            synchronized (this) {
                for (String entry : SearchSettings.LIME_QRP_ENTRIES.getValue()) qrpWords.add(entry);
            }
        }

        public synchronized void cancelRebuild() {
        }
    }

    /** A bunch of inspectables for FileManager */
    @SuppressWarnings("unused")
    @InspectableContainer
    private class FMInspectables {

        private static final int VERSION = 2;

        private void addVersion(Map<String, Object> m) {
            m.put("ver", VERSION);
        }

        /** An inspectable that returns some info about the QRP */
        @InspectionPoint("FileManager QRP info")
        public final Inspectable QRP = new Inspectable() {

            public Object inspect() {
                Map<String, Object> ret = new HashMap<String, Object>();
                addVersion(ret);
                synchronized (FileManagerImpl.this) {
                    ret.put("qrt", getQRT().getRawDump());
                }
                return ret;
            }
        };

        /** An inspectable that returns stats about hits, uploads & alts */
        @InspectionPoint("FileManager h/u/a stats")
        public final Inspectable FDS = new FDInspectable(false);

        /** An inspectable that returns stats about hits, uploads & alts > 0 */
        @InspectionPoint("FileManager h/u/a stats > 0")
        public final Inspectable FDSNZ = new FDInspectable(true);

        /** An inspectable that counts how many shared fds match a custom criteria */
        @InspectionPoint("FileManager custom criteria")
        public final Inspectable CUSTOM = new Inspectable() {

            public Object inspect() {
                Map<String, Object> ret = new HashMap<String, Object>();
                ret.put("ver", 1);
                ret.put("crit", MessageSettings.CUSTOM_FD_CRITERIA.getValueAsString());
                int total = 0;
                int matched = 0;
                try {
                    RPNParser parser = new RPNParser(MessageSettings.CUSTOM_FD_CRITERIA.getValue());
                    synchronized (FileManagerImpl.this) {
                        for (FileDesc fd : getAllSharedFileDescriptors()) {
                            total++;
                            if (parser.evaluate(fd)) matched++;
                        }
                    }
                } catch (IllegalArgumentException badSimpp) {
                    ret.put("error", badSimpp.toString());
                    return ret;
                }
                ret.put("match", matched);
                ret.put("total", total);
                return ret;
            }
        };
    }

    /** Inspectable with information about File Descriptors */
    private class FDInspectable implements Inspectable {

        private final boolean nonZero;

        /**
         * @param nonZero whether to return only results greater than 0
         */
        FDInspectable(boolean nonZero) {
            this.nonZero = nonZero;
        }

        public Object inspect() {
            Map<String, Object> ret = new HashMap<String, Object>();
            ret.put("ver", FMInspectables.VERSION);
            ArrayList<Double> hits = new ArrayList<Double>();
            ArrayList<Double> uploads = new ArrayList<Double>();
            ArrayList<Double> completeUploads = new ArrayList<Double>();
            ArrayList<Double> alts = new ArrayList<Double>();
            ArrayList<Double> keywords = new ArrayList<Double>();
            ArrayList<Double> altsHits = new ArrayList<Double>();
            ArrayList<Double> altsUploads = new ArrayList<Double>();
            ArrayList<Double> hitsUpload = new ArrayList<Double>();
            ArrayList<Double> hitsKeywords = new ArrayList<Double>();
            ArrayList<Double> uploadsToComplete = new ArrayList<Double>();
            Map<Integer, FileDesc> topHitsFDs = new TreeMap<Integer, FileDesc>(Comparators.inverseIntegerComparator());
            Map<Integer, FileDesc> topUpsFDs = new TreeMap<Integer, FileDesc>(Comparators.inverseIntegerComparator());
            Map<Integer, FileDesc> topAltsFDs = new TreeMap<Integer, FileDesc>(Comparators.inverseIntegerComparator());
            Map<Integer, FileDesc> topCupsFDs = new TreeMap<Integer, FileDesc>(Comparators.inverseIntegerComparator());
            synchronized (FileManagerImpl.this) {
                FileDesc[] fds = getAllSharedFileDescriptors();
                hits.ensureCapacity(fds.length);
                uploads.ensureCapacity(fds.length);
                int rare = 0;
                int total = 0;
                for (int i = 0; i < fds.length; i++) {
                    if (fds[i] instanceof IncompleteFileDesc) continue;
                    total++;
                    if (isRareFile(fds[i])) rare++;
                    int numAlts = fileManagerController.getAlternateLocationCount(fds[i].getSHA1Urn());
                    if (!nonZero || numAlts > 0) {
                        alts.add((double) numAlts);
                        topAltsFDs.put(numAlts, fds[i]);
                    }
                    int hitCount = fds[i].getHitCount();
                    if (!nonZero || hitCount > 0) {
                        hits.add((double) hitCount);
                        topHitsFDs.put(hitCount, fds[i]);
                    }
                    int upCount = fds[i].getAttemptedUploads();
                    if (!nonZero || upCount > 0) {
                        uploads.add((double) upCount);
                        topUpsFDs.put(upCount, fds[i]);
                    }
                    int cupCount = fds[i].getCompletedUploads();
                    if (!nonZero || cupCount > 0) {
                        completeUploads.add((double) upCount);
                        topCupsFDs.put(cupCount, fds[i]);
                    }
                    double keywordsCount = HashFunction.getPrefixes(HashFunction.keywords(fds[i].getPath())).length;
                    keywords.add(keywordsCount);
                    if (!nonZero) {
                        int index = hits.size() - 1;
                        hitsUpload.add(hits.get(index) - uploads.get(index));
                        altsHits.add(alts.get(index) - hits.get(index));
                        altsUploads.add(alts.get(index) - uploads.get(index));
                        hitsKeywords.add(hits.get(index) - keywordsCount);
                        uploadsToComplete.add(uploads.get(index) - completeUploads.get(index));
                    }
                }
                ret.put("rare", Double.doubleToLongBits((double) rare / total));
            }
            ret.put("hits", StatsUtils.quickStatsDouble(hits).getMap());
            ret.put("hitsh", StatsUtils.getHistogram(hits, 10));
            ret.put("ups", StatsUtils.quickStatsDouble(uploads).getMap());
            ret.put("upsh", StatsUtils.getHistogram(uploads, 10));
            ret.put("cups", StatsUtils.quickStatsDouble(completeUploads).getMap());
            ret.put("cupsh", StatsUtils.getHistogram(completeUploads, 10));
            ret.put("alts", StatsUtils.quickStatsDouble(alts).getMap());
            ret.put("altsh", StatsUtils.getHistogram(alts, 10));
            ret.put("kw", StatsUtils.quickStatsDouble(keywords).getMap());
            ret.put("kwh", StatsUtils.getHistogram(keywords, 10));
            ret.put("hut", StatsUtils.quickStatsDouble(hitsUpload).getTTestMap());
            ret.put("aht", StatsUtils.quickStatsDouble(altsHits).getTTestMap());
            ret.put("aut", StatsUtils.quickStatsDouble(altsUploads).getTTestMap());
            ret.put("hkt", StatsUtils.quickStatsDouble(hitsKeywords).getTTestMap());
            ret.put("ucut", StatsUtils.quickStatsDouble(uploadsToComplete).getTTestMap());
            QueryRouteTable topHits = new QueryRouteTable();
            QueryRouteTable topUps = new QueryRouteTable();
            QueryRouteTable topCups = new QueryRouteTable();
            QueryRouteTable topAlts = new QueryRouteTable();
            Iterator<FileDesc> hitIter = topHitsFDs.values().iterator();
            Iterator<FileDesc> upIter = topUpsFDs.values().iterator();
            Iterator<FileDesc> cupIter = topCupsFDs.values().iterator();
            Iterator<FileDesc> altIter = topAltsFDs.values().iterator();
            for (int i = 0; i < 10; i++) {
                if (hitIter.hasNext()) topHits.add(hitIter.next().getPath());
                if (upIter.hasNext()) topUps.add(upIter.next().getPath());
                if (altIter.hasNext()) topAlts.add(altIter.next().getPath());
                if (cupIter.hasNext()) topCups.add(cupIter.next().getPath());
            }
            ret.put("hitsq", topHits.getRawDump());
            ret.put("upsq", topUps.getRawDump());
            ret.put("cupsq", topCups.getRawDump());
            ret.put("altsq", topAlts.getRawDump());
            return ret;
        }
    }

    private class RareFileDefinition {

        private RPNParser parser;

        RareFileDefinition() {
        }

        private synchronized boolean evaluate(FileDesc fd) {
            try {
                return parser.evaluate(fd);
            } catch (IllegalArgumentException badSimpp) {
                return false;
            }
        }
    }
}
