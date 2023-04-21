package eulergui.project;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.logging.Logger;
import n3_project.IOManager;
import n3_project.helpers.GuiResourceBundle;
import net.sf.parser4j.parser.service.ParserException;
import unif.Instanciator;
import eulergui.EulerGUI;
import eulergui.drools_engine.ParserLink;
import eulergui.gui.TemporaryFrame;
import eulergui.infrastructure.cache.URICacheImpl;
import eulergui.infrastructure.cache.URICacheOSCache;
import eulergui.infrastructure.interfaces.URICache;
import eulergui.parser.n3.impl.parser4j.entity.N3ParseResult;
import eulergui.util.ReaderUtils;
import eulergui.util.StackHelper;
import eulergui.util.StringHelper;
import eulergui.util.URLHelper;

/**
 * an N3 source coming from a File or URL;
 *
 * Note about persistence: to be saved by
 * XMLencoder, a property must have a *public* getter and setter. On purpose
 * several properties don't follow the set/get pattern, because URI and location are
 * the main properties we want to serialize.
 * TODO PENDING : implement equals() and hashcode() with uri()
 */
public class N3Source {

    private URL location;

    private String source;

    private boolean activated = true;

    private Project project;

    private boolean isTransient = false;

    private boolean readOnly = false;

    private boolean hidden = false;

    private N3ParseResult parseResult = null;

    private String uri_;

    private final Map<String, URI> knownURIPrefixes;

    private Thread readerThread;

    private Exception exception;

    private boolean knownURIPrefixesSet;

    private boolean accessible = true;

    protected File localN3File;

    private long n3ParseTimestamp;

    private long locationTimeStamp;

    private transient boolean currentlyUpdating;

    /**
     * Java Bean stuff for XMLEncoder
     */
    public N3Source() {
        knownURIPrefixes = new TreeMap<String, URI>();
    }

    public N3Source(File f, Project project2) {
        knownURIPrefixes = new TreeMap<String, URI>();
        try {
            uri_ = f.toURI().toString();
            location = f.toURI().toURL();
            placeInProject(project2);
        } catch (final MalformedURLException e) {
            e.printStackTrace();
            throw new RuntimeException(MessageFormat.format(GuiResourceBundle.getString("unable.to.create.n3source.from.file.at.0"), f.getAbsolutePath()));
        }
    }

    public N3Source(File f) {
        knownURIPrefixes = new TreeMap<String, URI>();
        try {
            uri_ = f.toURI().toString();
            location = f.toURI().normalize().toURL();
        } catch (final MalformedURLException e) {
            e.printStackTrace();
            throw new RuntimeException(MessageFormat.format(GuiResourceBundle.getString("unable.to.create.n3source.from.file.at.0"), f.getAbsolutePath()));
        }
    }

    public N3Source(String uri, String source) {
        knownURIPrefixes = new TreeMap<String, URI>();
        this.source = source;
        try {
            setLocation(Instanciator.makeN3File(source).toURI().toURL());
            setURI(uri);
            knownURIPrefixes.put(":", new URI(uri));
        } catch (final FileNotFoundException e) {
            e.printStackTrace();
        } catch (final IOException e) {
            e.printStackTrace();
        } catch (final URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public N3Source(URL url) {
        knownURIPrefixes = new TreeMap<String, URI>();
        setURI(url.toString());
    }

    /**
     * The "location" is the absolute location where the original data stream is
     * available. Everything is resolved properly. One only has to open an
     * InputStream from this location and it will always work.
     */
    public URL getLocation() {
        try {
            if (source != null) {
                assert location != null : "";
                return location;
            }
            if (getURI() != null) {
                if (URLHelper.isRelative(getURI())) {
                    if (getProject() != null && getProject().getLocation() != null) {
                        location = new URL(getProject().getLocation(), getURI());
                    }
                } else {
                    location = new URL(getURI());
                }
            }
            if (location != null) {
                location = new URL(location.toURI().normalize().toString());
            }
        } catch (final MalformedURLException e) {
            e.printStackTrace();
        } catch (final URISyntaxException e) {
            e.printStackTrace();
        }
        return location;
    }

    /** see {@link #getLocation()} */
    public void setLocation(URL location) {
        this.location = location;
    }

    /**
     * set a (possibly) simplified (relative) URI;
     * this method is only called by XMLDecoder
     */
    public void setURI(String uri) {
        this.uri_ = uri;
    }

    /**
     * get a (possibly) simplified URI; the minimum Information provided by the
     * user and / or stored in the file to be able to recreate the location. It
     * can be a relative path, absolute URL, or various other things.
     * In any case, without an inference, it is not trivial to open a stream on the
     * data that this URI is supposed to represent. Everything else follows
     * through processing (fields: location, shortname)
     */
    public String getURI() {
        return uri_;
    }

    /**
     * @return URI As N3 term, ie surrounded with <>
     */
    public String getURIAsN3() {
        return '<' + getURI() + '>';
    }

    /**
     * show user visible short name (e.g. for button);
     * relativize Location with respect to project's location
     */
    public String showShortName() {
        URI projectUri = null;
        if (getURI() != null) {
            try {
                if (getProject() != null && getProject().getLocation() != null) {
                    projectUri = new URL(getProject().getLocation(), ".").toURI();
                    final URI uri = projectUri.relativize(getLocation().toURI());
                    if (uri.isAbsolute()) {
                        final String uriAsString = uri.toString();
                        return uriAsString;
                    } else {
                        return uri.toString();
                    }
                }
            } catch (final URISyntaxException e) {
                e.printStackTrace();
            } catch (final MalformedURLException e) {
                e.printStackTrace();
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
        return getURI();
    }

    public void setActivated(boolean b) {
        final boolean activated_before = activated;
        activated = b;
        if (activated_before != b) {
            if (project != null) {
                project.fireN3SourceActivationChanged(this);
            }
        }
    }

    public boolean isActivated() {
        return activated;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setTransient(boolean aTransient) {
        isTransient = aTransient;
    }

    public boolean isTransient() {
        return isTransient;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    /**
	 * not null <--> the N3 source is given by a String, not by a File or URL
	 * 
	 * @return the N3 source String
	 */
    public String getSource() {
        return source;
    }

    /**
     * get the absolute URI
     */
    public String uri() {
        if (getProject() != null) {
            try {
                if (getProject() != null && getProject().getLocation() != null && getURI() != null) {
                    return new URL(getProject().getLocation(), getURI()).toString();
                }
            } catch (final MalformedURLException e) {
                e.printStackTrace();
            }
        }
        return getURI();
    }

    /** */
    public void placeInProject(Project project) {
        if (this.project != project && project != null) {
            this.project = project;
            try {
                prepare(project);
            } catch (final Exception e) {
                System.out.println("N3Source.setProject(): " + e);
                TemporaryFrame.showException("PROBLEM when preparing N3 source " + getURI(), e, " , caused by " + e.getCause().getLocalizedMessage(), TemporaryFrame.WARNING);
            }
        }
    }

    public Project getProject() {
        return project;
    }

    public String showLongName() {
        return getURI() + " " + getClass();
    }

    /**
     * prepare N3 source: take in account Project's base URI
     * @throws IOException
     * @throws FileNotFoundException
     */
    public void prepare(Project project2) {
        placeInProject(project2);
        try {
            if (getLocation() == null) {
                if (getURI() != null) {
                    URL loc;
                    final URL projectLocation = project.getLocation();
                    if (projectLocation != null) {
                        if (URLHelper.isRelative(getURI())) {
                            loc = new URL(projectLocation, getURI());
                        } else {
                            final URI uri = new URI(getURI());
                            loc = projectLocation.toURI().relativize(uri).toURL();
                        }
                        setLocation(loc);
                    }
                }
            }
        } catch (final MalformedURLException e) {
            throw new RuntimeException("Unable to create an absolute n3 URI from this URI: " + getURI() + ", with URI base " + project.getURLPrefix(), e);
        } catch (final URISyntaxException e) {
            throw new RuntimeException("Unable to create an absolute n3 URI from this URI: " + getURI() + ", with URI base " + project.getURLPrefix(), e);
        }
    }

    /**
     * like {@link #prepare(Project)} , but re-download and re-translates to N3 if timestamp is older than file or URL;
     *
     */
    public void update() {
        synchronized (this) {
            currentlyUpdating = true;
            doUpdate();
            manageParseN3();
            currentlyUpdating = false;
            notify();
        }
    }

    /**
	 * manage N3 Cache using timestamp; downloads if not up-to-date; convert to
	 * N3 if original format is not N3; called by {@link #update()} , but can be
	 * overridden;
	 * */
    protected void doUpdate() {
        manageN3Cache(this);
    }

    /**
	 *
	 */
    private void manageParseN3() {
        try {
            boolean parsingOutdated = isParsingOutdated();
            if (parseResult == null && parsingOutdated) {
                doParseN3();
            } else {
                if (parsingOutdated) {
                    synchronized (parseResult) {
                        doParseN3();
                    }
                }
            }
        } catch (final Exception e) {
            throw new RuntimeException(GuiResourceBundle.getString("Error.during") + " re-parsing N3 source \"" + showShortName() + "\"", e);
        }
    }

    /**
	 * @return is Parsing Outdated, or never tried ?
	 */
    public boolean isParsingOutdated() {
        boolean parsingAlreadyTried = n3ParseTimestamp >= locationTimeStamp && n3ParseTimestamp > 0;
        boolean parsingOutdated = !parsingAlreadyTried;
        return parsingOutdated;
    }

    /**
     * get parse Result (name does NOT begin with get because we don't want this
     * field to be processed by XMLEncoder)
     */
    public N3ParseResult parseResult() {
        if (parseResult == null) {
            if (isParsingOutdated()) {
                System.err.println("N3Source.parseResult(): parseResult (" + getURI() + ") was null ==> re-parse (and possibly re-read N3 source)");
            }
            update();
        }
        return parseResult;
    }

    public boolean isInError() {
        return (parseResult != null && parseResult.isInError());
    }

    /** unconditionally Parse N3
	 * @throws ParserException
	 * @throws IOException
	 */
    private void doParseN3() throws ParserException, IOException {
        n3ParseTimestamp = System.currentTimeMillis();
        Logger logger = Logger.getLogger("eulergui.cache");
        logger.info("N3Source.manageParseN3(): before N3 parse: " + getURI() + " Thread " + Thread.currentThread().getName());
        parseResult = new ParserLink().parse(this);
        logger.info("N3Source.manageParseN3(): N3 parsed: " + getURI());
    }

    /** unconditionally download and convert to N3 if original format is not N3;
	 * sets field File {@link #localN3File}
	 * @return TODO*/
    protected File convertToN3() {
        if (localN3File == null) {
            localN3File = makeLocalN3File();
        }
        try {
            IOManager.downloadN3ToLocalCache(this, localN3File);
        } catch (Exception e) {
            StackHelper.printStackTrace(5);
            System.err.println("N3Source.convertToN3(): retry: " + this);
            IOManager.downloadN3ToLocalCache(this, localN3File);
        }
        return localN3File;
    }

    /**
	 * @return is URI TimeStamp Uptodate ? as a side effect sets field
	 *         {@link #locationTimeStamp}
	 */
    public boolean hasChanged() {
        final String location = getLocation().toString();
        final long old_locationTimeStamp = locationTimeStamp;
        if (lastTimeStampOlderThan(200)) synchronized (this) {
            locationTimeStamp = URICacheImpl.getURITimeStamp(location);
            return locationTimeStamp > old_locationTimeStamp && old_locationTimeStamp > 0;
        } else {
            return false;
        }
    }

    /**
	 * last TimeStamp Older Than given duration, compared to current time
	 * 
	 * @param i
	 * @return
	 */
    private boolean lastTimeStampOlderThan(int duration) {
        return new Date().getTime() - locationTimeStamp > duration;
    }

    /**
	 * manage N3 Cache, using timestamp: convertToN3 only if if original source
	 * is younger than converted N3, that is: if cache is Up to Date, get Cached
	 * URI As File; otherwise call {@link #convertToN3()}, and store in cache
	 * the local N3 representation of the N3 source
	 * 
	 * @return
	 */
    public static File manageN3Cache(N3Source n3Source) {
        File cachedN3 = null;
        try {
            if (n3Source == null || n3Source.getLocation() == null) {
                return File.createTempFile("empty", "");
            }
            final String location = n3Source.getLocation().toString();
            if (URLHelper.isLocal(n3Source.getLocation()) && n3Source.getClass() == N3Source.class) {
                if (n3Source.hasChanged()) {
                    if (!n3Source.isTransient()) {
                        new TemporaryFrame(n3Source.getURI() + " has changed on disk", 30000, TemporaryFrame.WARNING);
                    }
                }
                return new File(n3Source.getLocation().getPath().replace("%20", " "));
            }
            Logger.getLogger("eulergui.cache").info("manageN3Cache() " + n3Source.getURI());
            final URICache cache = URICacheOSCache.instance();
            final boolean cacheUptoDate = cache.uptoDateInCache(location, n3Source.locationTimeStamp);
            synchronized (n3Source) {
                if (cacheUptoDate) {
                    cachedN3 = (File) cache.getCachedURIAsObject(location);
                    n3Source.localN3File = cachedN3;
                } else {
                    if (n3Source.isAccessible()) {
                        if (n3Source.n3ParseTimestamp > 0) {
                            new TemporaryFrame(n3Source.getURI() + " has changed on Internet", 30000, TemporaryFrame.WARNING);
                        }
                        cachedN3 = n3Source.convertToN3();
                        cache.cacheObject(location, cachedN3);
                    }
                }
            }
        } catch (final Exception e) {
            System.out.println("N3Source.manageN3Cache(): " + n3Source.getURI() + " : " + e);
            TemporaryFrame.showException("PROBLEM when managing N3 Cache for N3 source " + n3Source.getURI(), e, " , caused by " + e.getCause().getLocalizedMessage(), TemporaryFrame.WARNING);
            n3Source.setAccessible(false);
            try {
                cachedN3 = File.createTempFile("empty", "");
            } catch (final IOException e1) {
                e1.printStackTrace();
            }
        }
        return cachedN3;
    }

    /** Cache directory for downloaded and (possibly converted from other formats) N3 sources */
    public File getN3Cache_dir() {
        final String dirName = EulerGUI.instance().getCache_dir() + File.separator + "n3_cache";
        final File dir = new File(dirName);
        dir.mkdirs();
        return dir;
    }

    @Override
    public String toString() {
        return uri();
    }

    public String getEditableFileOrURL() {
        return getLocation().toString();
    }

    /**
     * add an URI Map used as N3 prefixes
     */
    public void addKnownURIPrefixes(Map<String, URI> knownURIPrefixes) {
        for (final Entry<String, URI> entry : knownURIPrefixes.entrySet()) {
            this.knownURIPrefixes.put(entry.getKey(), entry.getValue());
        }
        knownURIPrefixesSet = true;
    }

    /**
     * get Known URIs used as N3 prefixes; when the N3 parser has not run yet,
     * do it.
     */
    public Set<URI> getKnownURIs() {
        return new TreeSet<URI>(getKnownURIPrefixes().values());
    }

    public Map<String, URI> getKnownURIPrefixes() {
        if (!knownURIPrefixesSet) {
            try {
                manageParseN3();
                System.out.println("N3Source.getKnownURIPrefixes(): parsing had not been done.");
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
        return new TreeMap<String, URI>(knownURIPrefixes);
    }

    public void setReaderThread(Thread t) {
        this.readerThread = t;
    }

    /**
     * waits at most 50s for the download
     */
    public void waitForDownloadThread() {
        if (readerThread != null) {
            if (readerThread.isAlive()) {
                Logger.getLogger("theDefault").info(MessageFormat.format(GuiResourceBundle.getString("n3source.waitfordownloadthread.0"), showShortName()));
                try {
                    readerThread.join(50000);
                } catch (final InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Logger.getLogger("theDefault").info("N3Source.waitForDownloadThread(): " + showShortName() + " finished; " + "readerThread.isAlive(): " + readerThread.isAlive() + " " + new Date());
            if (readerThread.isAlive()) {
            }
        }
    }

    public void setException(Exception e) {
        this.exception = e;
    }

    public Exception getException() {
        return exception;
    }

    /**
     * get Original RDF Format, "N3" or "RDF"; for converted sources like plain
     * XML, XMI, etc, it is "N3".
     */
    public String getOriginalRDFFormat() {
        return "N3";
    }

    /** get the Local N3 Cache File, or the file itself if it is Local and native N3;
     * if it does not exist already create the local Cache File and fill it;
     * the Local N3 Cache File is the translation into N3 in the case of a non native N3 source */
    public File getLocalN3() {
        return getLocalCache();
    }

    private File getLocalCache() {
        return IOManager.getLocalCache(this);
    }

    /** make Local N3 File, but do not populate it
	 * @param n3Source
	 * @return
	 */
    protected final File makeLocalN3File() {
        String prefix = StringHelper.subStringAfterLastSlash(getURI());
        if (prefix.length() < 3) {
            prefix += "N3";
        }
        try {
            localN3File = File.createTempFile(prefix + "_", ".n3", getN3Cache_dir());
            return localN3File;
        } catch (final IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /** @param newName new (relative) Name of the Local N3 file */
    public void renameLocalN3(String newName) {
        try {
            File localN3 = getLocalN3();
            final File newFile = new File(localN3.getParentFile().getCanonicalPath() + File.separator + newName);
            if (newFile.exists()) {
                newFile.delete();
            }
            final URL newFileUrl = newFile.toURI().toURL();
            final boolean renamedOk = localN3.renameTo(newFile);
            System.out.println(newFile);
            if (renamedOk) {
                if (source != null) {
                    setLocation(newFileUrl);
                    setURI(getLocation().toString());
                }
            } else {
                ReaderUtils.copyReader(new FileReader(localN3), new FileWriter(newFile));
            }
        } catch (final MalformedURLException e) {
            e.printStackTrace();
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    /** @return name of an N3 File, either the original source, or else a download or a translation from other format;
     *
     * TODO this method is confusing.
    // TODO all calls to this method should be removed and replaced by either
    // calls to getLocalN3().getAbsolutePath() or getLocation().
     * PROPOSED jmv : 2010-11-07
     * reintroduce N3SourceFromOtherFormat */
    public String fileName() {
        if (this.getClass() == N3Source.class) {
            return getLocation().toString();
        } else {
            final String file = StringHelper.substringAfterLastOrSelf(getLocation().getFile(), "/");
            File localN3 = getLocalN3();
            try {
                if (localN3 != null) {
                    return localN3.getCanonicalPath();
                }
                return IOManager.getTemporaryFileWithSuffix(file).getAbsolutePath();
            } catch (final IOException e) {
                System.out.println("N3Source.getFileName(): " + e);
                return localN3.getAbsolutePath();
            }
        }
    }

    public void setAccessible(boolean b) {
        accessible = b;
    }

    /** an N3 source is not accessible when it has raised e.g. an UnknownHostException */
    public boolean isAccessible() {
        return accessible;
    }

    /**
	 * @return
	 */
    public boolean isNull() {
        return source == null && uri_ == null && location == null;
    }

    /**
	 * @return the currentlyUpdating
	 */
    public boolean isCurrentlyUpdating() {
        return currentlyUpdating;
    }
}
