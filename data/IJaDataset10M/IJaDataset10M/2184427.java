package org.stanwood.media.store.xmlstore;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedSet;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.stanwood.media.Controller;
import org.stanwood.media.MediaDirectory;
import org.stanwood.media.logging.StanwoodException;
import org.stanwood.media.model.Actor;
import org.stanwood.media.model.Certification;
import org.stanwood.media.model.Chapter;
import org.stanwood.media.model.Episode;
import org.stanwood.media.model.Film;
import org.stanwood.media.model.IEpisode;
import org.stanwood.media.model.IFilm;
import org.stanwood.media.model.ISeason;
import org.stanwood.media.model.IShow;
import org.stanwood.media.model.IVideo;
import org.stanwood.media.model.IVideoActors;
import org.stanwood.media.model.IVideoCertification;
import org.stanwood.media.model.IVideoExtra;
import org.stanwood.media.model.IVideoFile;
import org.stanwood.media.model.IVideoGenre;
import org.stanwood.media.model.IVideoRating;
import org.stanwood.media.model.Mode;
import org.stanwood.media.model.Rating;
import org.stanwood.media.model.SearchResult;
import org.stanwood.media.model.Season;
import org.stanwood.media.model.Show;
import org.stanwood.media.model.VideoFile;
import org.stanwood.media.model.VideoFileSet;
import org.stanwood.media.progress.IProgressMonitor;
import org.stanwood.media.setup.MediaDirConfig;
import org.stanwood.media.source.HybridFilmSource;
import org.stanwood.media.source.ISource;
import org.stanwood.media.source.NotInStoreException;
import org.stanwood.media.source.SourceException;
import org.stanwood.media.source.TagChimpSource;
import org.stanwood.media.source.xbmc.XBMCSource;
import org.stanwood.media.store.IStore;
import org.stanwood.media.store.StoreException;
import org.stanwood.media.store.StoreVersion;
import org.stanwood.media.util.Version;
import org.stanwood.media.xml.IterableNodeList;
import org.stanwood.media.xml.SimpleErrorHandler;
import org.stanwood.media.xml.XMLParser;
import org.stanwood.media.xml.XMLParserException;
import org.stanwood.media.xml.XMLParserNotFoundException;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import com.sun.org.apache.xpath.internal.XPathAPI;

/**
 * This store is used to store the show and film information in a XML called .mediaManager-xmlStore.xml. This is located
 * in the root media directory.
 */
public class XMLStore2 extends BaseXMLStore implements IStore {

    private static final StoreVersion STORE_VERSION = new StoreVersion(new Version("2.1"), 3);

    private static final String DTD_WEB_LOCATION = XMLParser.DTD_WEB_LOCATION + "/MediaManager-XmlStore-" + STORE_VERSION.getVersion() + ".dtd";

    private static final String DTD_LOCATION = "-//STANWOOD//DTD XMLStore " + STORE_VERSION.getVersion() + "//EN";

    private static final Log log = LogFactory.getLog(XMLStore2.class);

    private static final String FILENAME = ".mediaManager-xmlStore.xml";

    private static final String STIP_PUNCUATION = "'()[],";

    private final DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    private NodeList fileNodes;

    private Document storeDoc;

    public XMLStore2() {
    }

    /**
	 * This is used to write a episode or special too the store
	 *
	 * @param episode The episode or special too write
	 * @param episodeFile the file witch the episode is stored in
	 * @param rootMediaDir This is the directory which is the root of media, this can be the current directory if it was
	 *            not specified on the command line.
	 * @throws StoreException Thrown if their is a problem with the store
	 */
    @Override
    public void cacheEpisode(File rootMediaDir, File episodeFile, IEpisode episode) throws StoreException {
        clearCaches();
        ISeason season = episode.getSeason();
        IShow show = season.getShow();
        Document doc = getCache(rootMediaDir);
        Element seasonNode = getSeasonNode(rootMediaDir, season, doc);
        if (episode.isSpecial()) {
            cacheEpisode("special", rootMediaDir, episodeFile, doc, show, seasonNode, episode);
        } else {
            cacheEpisode("episode", rootMediaDir, episodeFile, doc, show, seasonNode, episode);
        }
    }

    private void clearCaches() {
        fileNodes = null;
    }

    private void cacheEpisode(String nodeName, File rootMediaDir, File episodeFile, Document doc, IShow show, Element seasonNode, IEpisode episode) throws StoreException {
        if (log.isDebugEnabled()) {
            log.debug("cache episode");
        }
        try {
            episode.getFiles().add(new VideoFile(episodeFile, episodeFile, null, rootMediaDir));
            Node node = selectSingleNode(seasonNode, nodeName + "[number=" + episode.getEpisodeNumber() + "]");
            if (node == null) {
                node = doc.createElement(nodeName);
                ((Element) node).setAttribute("number", String.valueOf(episode.getEpisodeNumber()));
                seasonNode.appendChild(node);
            }
            writeEpisodeCommonData(doc, episode, node, episodeFile, rootMediaDir);
            File cacheFile = getCacheFile(rootMediaDir, FILENAME);
            writeCache(cacheFile, doc);
        } catch (XMLParserException e) {
            throw new StoreException(Messages.getString("XMLStore2.UNABLE_WRITE_CACHE") + e.getMessage());
        }
    }

    private void writeEpisodeCommonData(Document doc, IEpisode episode, Node node, File episodeFile, File rootMediaDir) throws StoreException {
        try {
            ((Element) node).setAttribute("title", episode.getTitle());
            ((Element) node).setAttribute("url", urlToText(episode.getUrl()));
            if (episode.getDate() != null) {
                ((Element) node).setAttribute("firstAired", df.format(episode.getDate()));
            }
            ((Element) node).setAttribute("episodeId", episode.getEpisodeId());
            ((Element) node).setAttribute("imageUrl", urlToText(episode.getImageURL()));
            Node summaryNode = selectSingleNode(node, "summary");
            if (summaryNode != null) {
                summaryNode.getParentNode().removeChild(summaryNode);
            }
            summaryNode = doc.createElement("summary");
            node.appendChild(summaryNode);
            summaryNode.appendChild(doc.createTextNode(episode.getSummary()));
            writeRating(episode, ((Element) node));
            writeDirectors((Element) node, episode);
            writeWriters((Element) node, episode);
            writeActors(node, episode);
            episode.getFiles().add(new VideoFile(episodeFile, episodeFile, null, rootMediaDir));
            writeFilenames(doc, node, episode, rootMediaDir);
        } catch (XMLParserException e) {
            throw new StoreException(Messages.getString("XMLStore2.UNABLE_WRITE_EPISODE_DATA"), e);
        }
    }

    private void writeActors(Node node, IVideoActors episode) {
        Document doc = node.getOwnerDocument();
        Element actors = doc.createElement("actors");
        if (episode.getActors() != null) {
            for (Actor actor : episode.getActors()) {
                Element actorNode = doc.createElement("actor");
                actorNode.setAttribute("name", actor.getName());
                actorNode.setAttribute("role", actor.getRole());
                actors.appendChild(actorNode);
            }
        }
        node.appendChild(actors);
    }

    private void readActors(Node node, IVideoActors episode) throws XMLParserException {
        List<Actor> actors = new ArrayList<Actor>();
        for (Node n : selectNodeList(node, "actors/actor")) {
            Element e = (Element) n;
            actors.add(new Actor(e.getAttribute("name"), e.getAttribute("role")));
        }
        episode.setActors(actors);
    }

    private void appendFile(Document doc, Node parent, IVideoFile file, File rootMediaDir) throws StoreException {
        if (file != null) {
            try {
                Element fileNode = (Element) selectSingleNode(parent, "file[@location=" + quoteXPathQuery(makePathRelativeToMediaDir(file.getLocation(), rootMediaDir)) + "]");
                if (fileNode == null) {
                    fileNode = doc.createElement("file");
                    parent.appendChild(fileNode);
                }
                fileNode.setAttribute("location", makePathRelativeToMediaDir(file.getLocation(), rootMediaDir));
                if (file.getPart() != null) {
                    fileNode.setAttribute("part", String.valueOf(file.getPart()));
                }
            } catch (XMLParserException e) {
                throw new StoreException(Messages.getString("XMLStore2.UNABLE_READ_XML"), e);
            }
        }
    }

    /**
	 * This is used to write a film to the store.
	 *
	 * @param filmFile The file which the film is stored in
	 * @param film The film to write
	 * @param rootMediaDir This is the directory which is the root of media, this can be the current directory if it was
	 *            not specified on the command line.
	 * @throws StoreException Thrown if their is a problem with the store
	 */
    @Override
    public void cacheFilm(File rootMediaDir, File filmFile, IFilm film, Integer part) throws StoreException {
        if (log.isDebugEnabled()) {
            log.debug("cache film " + filmFile.getAbsolutePath());
        }
        clearCaches();
        Document doc = getCache(rootMediaDir);
        try {
            Node storeNode = getStoreNode(doc);
            film.getFiles().add(new VideoFile(filmFile, filmFile, part, rootMediaDir));
            appendFilm(doc, storeNode, film, rootMediaDir, false);
            File cacheFile = getCacheFile(rootMediaDir, FILENAME);
            writeCache(cacheFile, doc);
        } catch (XMLParserException e) {
            throw new StoreException(Messages.getString("XMLStore2.UNABLE_PARSE_CACHE_FILE"), e);
        }
    }

    private void appendFilm(Document doc, Node filmsNode, IFilm film, File rootMediaDir, boolean force) throws XMLParserException, StoreException {
        Element filmNode = (Element) selectSingleNode(filmsNode, "film[@id='" + film.getId() + "']");
        if (filmNode != null) {
            filmNode.getParentNode().removeChild(filmNode);
        }
        filmNode = doc.createElement("film");
        filmsNode.appendChild(filmNode);
        filmNode.setAttribute("id", film.getId());
        filmNode.setAttribute("title", film.getTitle());
        filmNode.setAttribute("sourceId", film.getSourceId());
        filmNode.setAttribute("url", urlToText(film.getFilmUrl()));
        if (film.getStudio() != null) {
            filmNode.setAttribute("studio", film.getStudio());
        }
        Date date = film.getDate();
        if (date != null) {
            filmNode.setAttribute("releaseDate", df.format(date));
        }
        filmNode.setAttribute("imageUrl", urlToText(film.getImageURL()));
        appendDescription(doc, film.getSummary(), film.getDescription(), filmNode);
        writeRating(film, filmNode);
        if (film.getCountry() != null) {
            Element country = doc.createElement("country");
            country.appendChild(doc.createTextNode(film.getCountry()));
            filmNode.appendChild(country);
        }
        writeGenres(film, filmNode);
        writeCertifications(film, filmNode);
        writeDirectors(filmNode, film);
        writeWriters(filmNode, film);
        writeActors(filmNode, film);
        writeChapters(film, filmNode);
        writeFilenames(doc, filmNode, film, rootMediaDir);
    }

    private void writeChapters(IFilm film, Element filmNode) {
        Document doc = filmNode.getOwnerDocument();
        Element chaptersNode = doc.createElement("chapters");
        for (Chapter chapter : film.getChapters()) {
            Element chap = doc.createElement("chapter");
            chap.setAttribute("number", String.valueOf(chapter.getNumber()));
            chap.setAttribute("name", chapter.getName());
            chaptersNode.appendChild(chap);
        }
        filmNode.appendChild(chaptersNode);
    }

    private void readChapters(Film film, Element filmNode) throws XMLParserException {
        List<Chapter> chapters = new ArrayList<Chapter>();
        for (Node n : selectNodeList(filmNode, "chapters/chapter")) {
            Element chapNode = (Element) n;
            Chapter chapter = new Chapter(chapNode.getAttribute("name"), Integer.parseInt(chapNode.getAttribute("number")));
            chapters.add(chapter);
        }
        film.setChapters(chapters);
    }

    protected void readWriters(IVideo video, Node videoNode) throws XMLParserException, NotInStoreException {
        List<String> writers = new ArrayList<String>();
        for (Node node : selectNodeList(videoNode, "writers/writer/text()")) {
            String writer = node.getTextContent();
            writers.add(writer);
        }
        video.setWriters(writers);
    }

    private void writeWriters(Element node, IVideo video) {
        Document doc = node.getOwnerDocument();
        Element writersNode = doc.createElement("writers");
        if (video.getWriters() != null) {
            for (String value : video.getWriters()) {
                Element writerNode = doc.createElement("writer");
                writerNode.appendChild(doc.createTextNode(value));
                writersNode.appendChild(writerNode);
            }
        }
        node.appendChild(writersNode);
    }

    protected void readDirectors(IVideo video, Node videoNode) throws XMLParserException, NotInStoreException {
        List<String> directors = new ArrayList<String>();
        for (Node node : selectNodeList(videoNode, "directors/director/text()")) {
            String director = node.getTextContent();
            directors.add(director);
        }
        video.setDirectors(directors);
    }

    private void writeDirectors(Element node, IVideo video) {
        Document doc = node.getOwnerDocument();
        Element directorsNode = doc.createElement("directors");
        if (video.getDirectors() != null) {
            for (String value : video.getDirectors()) {
                Element director = doc.createElement("director");
                director.appendChild(doc.createTextNode(value));
                directorsNode.appendChild(director);
            }
        }
        node.appendChild(directorsNode);
    }

    protected void readCertifications(IVideoCertification video, Node videoNode) throws XMLParserException, NotInStoreException {
        List<Certification> certifications = new ArrayList<Certification>();
        for (Node node : selectNodeList(videoNode, "certifications/certification")) {
            Element certificationEl = (Element) node;
            certifications.add(new Certification(certificationEl.getAttribute("certification"), certificationEl.getAttribute("type")));
        }
        video.setCertifications(certifications);
    }

    private void writeCertifications(IVideoCertification video, Element node) {
        Document doc = node.getOwnerDocument();
        Element certificationsNode = doc.createElement("certifications");
        if (video.getCertifications() != null) {
            for (Certification cert : video.getCertifications()) {
                Element certificationNode = node.getOwnerDocument().createElement("certification");
                certificationNode.setAttribute("type", cert.getType());
                certificationNode.setAttribute("certification", cert.getCertification());
                certificationsNode.appendChild(certificationNode);
            }
        }
        node.appendChild(certificationsNode);
    }

    private void writeGenres(IVideoGenre video, Element node) {
        Document doc = node.getOwnerDocument();
        Element genresNode = doc.createElement("genres");
        if (video.getGenres() != null) {
            for (String value : video.getGenres()) {
                Element genre = node.getOwnerDocument().createElement("genre");
                genre.setAttribute("name", value);
                if (value.equals(video.getPreferredGenre())) {
                    genre.setAttribute("preferred", "true");
                }
                genresNode.appendChild(genre);
            }
        }
        node.appendChild(genresNode);
    }

    protected void readGenres(IVideoGenre video, Node videoNode) throws XMLParserException, NotInStoreException {
        List<String> genres = new ArrayList<String>();
        for (Node node : selectNodeList(videoNode, "genres/genre")) {
            Element genreEl = (Element) node;
            String genre = genreEl.getAttribute("name");
            String preferred = genreEl.getAttribute("preferred");
            if (preferred.equals("true")) {
                video.setPreferredGenre(genre);
            }
            genres.add(genre);
        }
        video.setGenres(genres);
    }

    private void writeExtraParams(IVideoExtra video, Element node) {
        Document doc = node.getOwnerDocument();
        Element extraNode = doc.createElement("extra");
        if (video.getExtraInfo() != null) {
            for (Entry<String, String> e : video.getExtraInfo().entrySet()) {
                Element param = node.getOwnerDocument().createElement("param");
                param.setAttribute("key", e.getKey());
                param.setAttribute("value", e.getValue());
                extraNode.appendChild(param);
            }
        }
        node.appendChild(extraNode);
    }

    protected void readExtraParams(IVideoExtra video, Node videoNode) throws XMLParserException, NotInStoreException {
        Map<String, String> params = new HashMap<String, String>();
        for (Node node : selectNodeList(videoNode, "extra/param")) {
            Element extraEl = (Element) node;
            String value = extraEl.getAttribute("value");
            String key = extraEl.getAttribute("key");
            params.put(key, value);
        }
        video.setExtraInfo(params);
    }

    /**
	 * Used to append a set of filenames to the document under the given parent node
	 *
	 * @param doc The document to append the filenames to
	 * @param parent The parent node
	 * @param filenames The filenames to append
	 * @throws StoreException Thrown if their is a tore releated problem
	 */
    protected void writeFilenames(Document doc, Node parent, IVideo video, File rootMediaDir) throws StoreException {
        for (IVideoFile filename : video.getFiles()) {
            appendFile(doc, parent, filename, rootMediaDir);
        }
    }

    private Element getSeasonNode(File rootMediaDir, ISeason season, Document doc) throws StoreException {
        Element showNode = getShowNode(doc, season.getShow(), false);
        Node node = getSeasonNode(season, showNode, doc);
        if (node == null) {
            node = createSeasonNode(season, showNode, doc);
        }
        return (Element) node;
    }

    /**
	 * This is used to write a season too the store.
	 *
	 * @param season The season too write
	 * @param episodeFile The file the episode is stored in
	 * @param rootMediaDir This is the directory which is the root of media, this can be the current directory if it was
	 *            not specified on the command line.
	 * @throws StoreException Thrown if their is a problem with the store
	 */
    @Override
    public void cacheSeason(File rootMediaDir, File episodeFile, ISeason season) throws StoreException {
        if (log.isDebugEnabled()) {
            log.debug("cache season " + season.getSeasonNumber());
        }
        clearCaches();
        Document doc = getCache(rootMediaDir);
        Element node = getSeasonNode(rootMediaDir, season, doc);
        node.setAttribute("url", urlToText(season.getURL()));
        File cacheFile = getCacheFile(rootMediaDir, FILENAME);
        writeCache(cacheFile, doc);
    }

    private Element createSeasonNode(ISeason season, Element showNode, Document doc) {
        Element seasonEl = doc.createElement("season");
        seasonEl.setAttribute("number", String.valueOf(season.getSeasonNumber()));
        showNode.appendChild(seasonEl);
        seasonEl.setAttribute("url", urlToText(season.getURL()));
        return seasonEl;
    }

    private Node getSeasonNode(ISeason season, Element showNode, Document doc) throws StoreException {
        Node node;
        try {
            node = selectSingleNode(showNode, "season[@number=" + season.getSeasonNumber() + "]");
        } catch (XMLParserException e) {
            throw new StoreException(e.getMessage(), e);
        }
        return node;
    }

    /**
	 * This is used to write a show too the store.
	 *
	 * @param show The show too write
	 * @param episodeFile The file the episode is stored in
	 * @param rootMediaDir This is the directory which is the root of media, this can be the current directory if it was
	 *            not specified on the command line.
	 * @throws StoreException Thrown if their is a problem with the store
	 */
    @Override
    public void cacheShow(File rootMediaDir, File episodeFile, IShow show) throws StoreException {
        clearCaches();
        if (log.isDebugEnabled()) {
            log.debug("cache show " + show.getShowId() + ":" + show.getSourceId());
        }
        Document doc = getCache(rootMediaDir);
        getShowNode(doc, show, false);
        File cacheFile = getCacheFile(rootMediaDir, FILENAME);
        writeCache(cacheFile, doc);
    }

    private Element getStoreNode(Document doc) throws XMLParserException, StoreException {
        Element storeElement = getFirstChildElement(doc, "store");
        if (storeElement == null) {
            throw new StoreException(Messages.getString("XMLStore2.UNABLE_FIND_STORE_NODE"));
        }
        return storeElement;
    }

    private Element getShowNode(Document doc, IShow show, boolean force) throws StoreException {
        try {
            Node storeNode = getStoreNode(doc);
            Node node = selectSingleNode(storeNode, "show[@id='" + show.getShowId() + "']");
            if (node == null || force) {
                Element showElement;
                if (node == null) {
                    showElement = doc.createElement("show");
                } else {
                    showElement = (Element) node;
                }
                showElement.setAttribute("id", String.valueOf(show.getShowId()));
                showElement.setAttribute("url", urlToText(show.getShowURL()));
                showElement.setAttribute("name", show.getName());
                showElement.setAttribute("imageUrl", urlToText(show.getImageURL()));
                showElement.setAttribute("sourceId", show.getSourceId());
                if (show.getStudio() != null) {
                    showElement.setAttribute("studio", show.getStudio());
                }
                Node descriptionNode = selectSingleNode(showElement, "description");
                if (descriptionNode != null) {
                    showElement.removeChild(descriptionNode);
                }
                appendDescription(doc, show.getShortSummary(), show.getLongSummary(), showElement);
                writeCertifications(show, showElement);
                writeGenres(show, showElement);
                writeExtraParams(show, showElement);
                storeNode.appendChild(showElement);
                node = showElement;
            }
            return (Element) node;
        } catch (XMLParserException e) {
            throw new StoreException(Messages.getString("XMLStore2.UNABLE_PARSE_XML"), e);
        }
    }

    private void appendDescription(Document doc, String shortSummary, String longSummary, Element parent) {
        Element description = doc.createElement("description");
        if (shortSummary != null) {
            Element shortDesc = doc.createElement("short");
            shortDesc.appendChild(doc.createCDATASection(shortSummary));
            description.appendChild(shortDesc);
        }
        if (longSummary != null) {
            Element longDesc = doc.createElement("long");
            longDesc.appendChild(doc.createCDATASection(longSummary));
            description.appendChild(longDesc);
        }
        parent.appendChild(description);
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public IEpisode getEpisode(File rootMediaDir, File episodeFile, ISeason season, int episodeNum) throws StoreException, MalformedURLException {
        Document doc = getCache(rootMediaDir);
        if (doc == null) {
            return null;
        }
        IEpisode episode = null;
        try {
            episode = getEpisodeFromCache(episodeNum, season, doc, rootMediaDir);
            return episode;
        } catch (NotInStoreException e) {
            return null;
        }
    }

    /**
	 * This will get a film from the store. If the film can't be found, then it will return null.
	 *
	 * @param filmFile The file the film is located in.
	 * @param filmId The id of the film
	 * @param rootMediaDir This is the directory which is the root of media, this can be the current directory if it was
	 *            not specified on the command line.
	 * @return The film, or null if it can't be found
	 * @throws StoreException Thrown if their is a problem retrieving the data
	 * @throws MalformedURLException Thrown if their is a problem creating URL's
	 */
    @Override
    public IFilm getFilm(File rootMediaDir, File filmFile, String filmId) throws StoreException, MalformedURLException {
        Document doc = getCache(rootMediaDir);
        if (doc == null) {
            return null;
        }
        try {
            Element filmNode = (Element) selectSingleNode(doc, "store/film[@id='" + filmId + "']");
            if (filmNode == null) {
                if (log.isDebugEnabled()) {
                    log.debug("Film with id '" + filmId + "' is not in store " + XMLStore2.class.getName());
                }
                return null;
            }
            IFilm film = parseFilmNode(rootMediaDir, filmNode);
            return film;
        } catch (XMLParserException e) {
            throw new StoreException(Messages.getString("XMLStore2.UNABLE_READ_FILM_FROM_STORE"), e);
        } catch (NotInStoreException e) {
            throw new StoreException(Messages.getString("XMLStore2.UNABLE_READ_FILM_FROM_STORE"), e);
        } catch (ParseException e) {
            throw new StoreException(Messages.getString("XMLStore2.UNABLE_READ_FILM_FROM_STORE"), e);
        }
    }

    private IFilm parseFilmNode(File rootMediaDir, Element filmNode) throws XMLParserException, NotInStoreException, ParseException, MalformedURLException {
        Film film = new Film(getAttribute(filmNode, "id"));
        readGenres(film, filmNode);
        film.setCountry(getStringFromXMLOrNull(filmNode, "country/text()"));
        try {
            film.setDate(df.parse(getStringFromXML(filmNode, "@releaseDate")));
        } catch (XMLParserNotFoundException e) {
        }
        try {
            String studio = getAttribute(filmNode, "studio");
            film.setStudio(studio);
        } catch (XMLParserNotFoundException e) {
        }
        film.setFilmUrl(new URL(getAttribute(filmNode, "url")));
        film.setDescription(getStringFromXML(filmNode, "description/long/text()"));
        film.setSummary(getStringFromXML(filmNode, "description/short/text()"));
        try {
            film.setImageURL(new URL(getAttribute(filmNode, "imageUrl")));
        } catch (MalformedURLException e) {
            film.setImageURL(null);
        }
        film.setTitle(getAttribute(filmNode, "title"));
        parseRating(film, filmNode);
        readActors(filmNode, film);
        readWriters(film, filmNode);
        readDirectors(film, filmNode);
        film.setSourceId(getAttribute(filmNode, "sourceId"));
        readChapters(film, filmNode);
        readCertifications(film, filmNode);
        readFiles(film, filmNode, rootMediaDir);
        return film;
    }

    private void readFiles(IVideo video, Element videoNode, File rootMediaDir) throws XMLParserException {
        SortedSet<IVideoFile> files = new VideoFileSet();
        for (Node node : selectNodeList(videoNode, "file")) {
            String location = ((Element) node).getAttribute("location");
            String originalLocation = ((Element) node).getAttribute("orginalLocation");
            File orgLocFile = null;
            if (!originalLocation.equals("")) {
                orgLocFile = new File(rootMediaDir, originalLocation);
            }
            String strPart = ((Element) node).getAttribute("part");
            Integer part = null;
            if (!strPart.equals("")) {
                part = Integer.parseInt(strPart);
            }
            files.add(new VideoFile(new File(rootMediaDir, location), orgLocFile, part, rootMediaDir));
        }
        video.setFiles(files);
    }

    private void parseRating(IVideoRating film, Element node) throws XMLParserException {
        try {
            int numberOfVotes = getIntegerFromXML(node, "rating/@numberOfVotes");
            float rating = getFloatFromXML(node, "rating/@value");
            film.setRating(new Rating(rating, numberOfVotes));
        } catch (XMLParserNotFoundException e) {
        }
    }

    private void writeRating(IVideoRating video, Element node) {
        if (video.getRating() != null) {
            Element ratingNode = node.getOwnerDocument().createElement("rating");
            ratingNode.setAttribute("value", String.valueOf(video.getRating().getRating()));
            ratingNode.setAttribute("numberOfVotes", String.valueOf(video.getRating().getNumberOfVotes()));
            node.appendChild(ratingNode);
        }
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public ISeason getSeason(File rootMediaDir, File episodeFile, IShow show, int seasonNum) throws StoreException, MalformedURLException {
        Document doc = getCache(rootMediaDir);
        if (doc == null) {
            return null;
        }
        ISeason season = null;
        try {
            season = getSeasonFromCache(seasonNum, show, doc);
            return season;
        } catch (NotInStoreException e) {
            return null;
        }
    }

    private IEpisode getEpisodeFromCache(int episodeNum, ISeason season, Document doc, File rootMediaDir) throws NotInStoreException, StoreException, MalformedURLException {
        try {
            IShow show = season.getShow();
            Node episodeNode = selectSingleNode(doc, "store/show[@id='" + show.getShowId() + "' and @sourceId='" + show.getSourceId() + "']/" + "season[@number=" + season.getSeasonNumber() + "]/episode[@number=" + episodeNum + "]");
            if (episodeNode == null) {
                throw new NotInStoreException();
            }
            IEpisode episode = new Episode(episodeNum, season, false);
            readCommonEpisodeInfo(episodeNode, episode, rootMediaDir);
            return episode;
        } catch (ParseException e) {
            throw new StoreException(Messages.getString("XMLStore2.UNABLE_PARSE_DATA"), e);
        } catch (XMLParserException e) {
            throw new StoreException(Messages.getString("XMLStore2.UNABLE_PARSE_CACHE"), e);
        }
    }

    private IEpisode getSpecialFromCache(int specialNum, ISeason season, Document doc, File rootMediaDir) throws NotInStoreException, StoreException, MalformedURLException {
        try {
            IShow show = season.getShow();
            Node episodeNode = selectSingleNode(doc, "store/show[@id='" + show.getShowId() + "' and @sourceId='" + show.getSourceId() + "']/" + "season[@number=" + season.getSeasonNumber() + "]/special[@number=" + specialNum + "]");
            if (episodeNode == null) {
                throw new NotInStoreException();
            }
            IEpisode episode = new Episode(specialNum, season, true);
            readCommonEpisodeInfo(episodeNode, episode, rootMediaDir);
            return episode;
        } catch (ParseException e) {
            throw new StoreException("Unable to parse date: " + e.getMessage(), e);
        } catch (XMLParserException e) {
            throw new StoreException("Unable to parse cache: " + e.getMessage(), e);
        }
    }

    private void readCommonEpisodeInfo(Node episodeNode, IEpisode episode, File rootMediaDir) throws XMLParserException, NotInStoreException, MalformedURLException, ParseException {
        try {
            String summary = getStringFromXML(episodeNode, "summary/text()");
            URL url = new URL(getStringFromXML(episodeNode, "@url"));
            String title = getStringFromXML(episodeNode, "@title");
            String airDate = getStringFromXML(episodeNode, "@firstAired");
            String episodeId = getStringFromXML(episodeNode, "@episodeId");
            String urlStr = getStringFromXML(episodeNode, "@imageUrl");
            URL imageUrl = null;
            if (urlStr.length() > 0) {
                imageUrl = new URL(urlStr);
            }
            episode.setUrl(url);
            episode.setSummary(summary);
            episode.setTitle(title);
            episode.setDate(df.parse(airDate));
            episode.setEpisodeId(episodeId);
            episode.setImageURL(imageUrl);
            readActors(episodeNode, episode);
            readWriters(episode, episodeNode);
            parseRating(episode, (Element) episodeNode);
            readDirectors(episode, episodeNode);
            readFiles(episode, (Element) episodeNode, rootMediaDir);
        } catch (XMLParserNotFoundException e) {
            throw new NotInStoreException();
        }
    }

    private ISeason getSeasonFromCache(int seasonNum, IShow show, Document doc) throws StoreException, NotInStoreException, MalformedURLException {
        try {
            Node seasonNode = selectSingleNode(doc, "store/show[@id='" + show.getShowId() + "' and @sourceId='" + show.getSourceId() + "']/season[@number=" + seasonNum + "]");
            if (seasonNode == null) {
                throw new NotInStoreException();
            }
            ISeason season = new Season(show, seasonNum);
            season.setURL(new URL(getStringFromXML(seasonNode, "@url")));
            return season;
        } catch (XMLParserException e) {
            throw new StoreException(Messages.getString("XMLStore2.UNABLE_PARSE_CACHE"), e);
        }
    }

    private Show getShowFromCache(Document doc, String showId) throws StoreException, NotInStoreException, MalformedURLException {
        try {
            Node storeNode = getStoreNode(doc);
            Element showNode = (Element) selectSingleNode(storeNode, "show[@id=" + quoteXPathQuery(showId) + "]");
            if (showNode == null) {
                throw new NotInStoreException();
            }
            String imageURL = showNode.getAttribute("imageUrl");
            String showURL = showNode.getAttribute("url");
            String name = showNode.getAttribute("name");
            String sourceId = showNode.getAttribute("sourceId");
            String longSummary = getStringFromXML(showNode, "description/long/text()");
            String shortSummary = getStringFromXML(showNode, "description/short/text()");
            String studio = showNode.getAttribute("studio");
            Show show = new Show(showId);
            if (studio != null && studio.length() > 0) {
                show.setStudio(studio);
            }
            readGenres(show, showNode);
            readExtraParams(show, showNode);
            readCertifications(show, showNode);
            show.setName(name);
            try {
                show.setImageURL(new URL(imageURL));
            } catch (MalformedURLException e) {
                log.warn(MessageFormat.format(Messages.getString("XMLStore2.UNABLE_GET_SHOW_URL"), imageURL));
            }
            show.setLongSummary(longSummary);
            show.setShortSummary(shortSummary);
            show.setShowURL(new URL(showURL));
            show.setSourceId(sourceId);
            return show;
        } catch (XMLParserNotFoundException e) {
            throw new NotInStoreException();
        } catch (XMLParserException e) {
            throw new StoreException(Messages.getString("XMLStore2.UNABLE_PARSE_CACHE"), e);
        }
    }

    /**
	 * This will get a show from the store. If the season can't be found, then it will return null.
	 *
	 * @param episodeFile the file which the episode is stored in
	 * @param showId The id of the show to get.
	 * @param rootMediaDir This is the directory which is the root of media, this can be the current directory if it was
	 *            not specified on the command line.
	 * @return The show if it can be found, otherwise null.
	 * @throws StoreException Thrown if their is a problem with the store
	 * @throws MalformedURLException Thrown if their is a problem creating URL's
	 */
    @Override
    public IShow getShow(File rootMediaDir, File episodeFile, String showId) throws StoreException, MalformedURLException {
        Document doc = getCache(rootMediaDir);
        Show show = null;
        try {
            show = getShowFromCache(doc, showId);
            return show;
        } catch (NotInStoreException e) {
            return null;
        }
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public IEpisode getSpecial(File rootMediaDir, File episodeFile, ISeason season, int specialNumber) throws MalformedURLException, IOException, StoreException {
        Document doc = getCache(rootMediaDir);
        if (doc == null) {
            return null;
        }
        IEpisode episode = null;
        try {
            episode = getSpecialFromCache(specialNumber, season, doc, rootMediaDir);
            return episode;
        } catch (NotInStoreException e) {
            return null;
        }
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public void renamedFile(File rootMediaDir, File oldFile, File newFile) throws StoreException {
        Document doc = getCache(rootMediaDir);
        if (doc != null) {
            try {
                for (Node node : selectNodeList(doc, "//file[@location=" + quoteXPathQuery(makePathRelativeToMediaDir(oldFile, rootMediaDir)) + "]")) {
                    Element fileNode = (Element) node;
                    fileNode.setAttribute("location", makePathRelativeToMediaDir(newFile, rootMediaDir));
                    if (fileNode.getAttribute("orginalLocation").equals("")) {
                        fileNode.setAttribute("orginalLocation", makePathRelativeToMediaDir(oldFile, rootMediaDir));
                    }
                }
                File cacheFile = getCacheFile(rootMediaDir, FILENAME);
                writeCache(cacheFile, doc);
            } catch (XMLParserException e) {
                throw new StoreException(Messages.getString("XMLStore2.UNABLE_PARSE_XML2"), e);
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    public SearchResult searchMedia(String name, Mode mode, Integer part, MediaDirConfig dirConfig, File mediaFile) throws StoreException {
        Document doc = getCache(dirConfig.getMediaDir());
        if (doc != null) {
            Node store;
            try {
                store = getStoreNode(doc);
                if (store != null) {
                    if (dirConfig.getMode() == Mode.TV_SHOW) {
                        return searchForTVShow(store, mediaFile, dirConfig.getPattern(), dirConfig.getMediaDir(), name);
                    } else {
                        return searchForFilm(store, mediaFile, dirConfig);
                    }
                }
            } catch (XMLParserException e) {
                throw new StoreException(Messages.getString("XMLStore2.UNABLE_PARSE_STORE_XML"), e);
            }
        }
        return null;
    }

    private SearchResult searchForFilm(Node store, File episodeFile, MediaDirConfig dirConfig) throws XMLParserException {
        SearchResult result = null;
        for (Node node : selectNodeList(store, "film/file[@location=" + quoteXPathQuery(makePathRelativeToMediaDir(episodeFile, dirConfig.getMediaDir())) + "]")) {
            Element filmEl = (Element) node.getParentNode();
            Integer part = null;
            if (!((Element) node).getAttribute("part").equals("")) {
                part = Integer.parseInt(((Element) node).getAttribute("part"));
            }
            result = new SearchResult(filmEl.getAttribute("id"), filmEl.getAttribute("url"), filmEl.getAttribute("sourceId"), part, dirConfig.getMode());
        }
        return result;
    }

    private String makePathRelativeToMediaDir(File episodeFile, File rootMediaDir) {
        String path = rootMediaDir.getAbsolutePath();
        int len = path.length();
        if (episodeFile.getAbsolutePath().startsWith(path)) {
            return episodeFile.getAbsolutePath().substring(len + 1);
        } else {
            return episodeFile.getAbsolutePath();
        }
    }

    private SearchResult searchForTVShow(Node store, File episodeFile, String renamePattern, File rootMediaDir, String title) throws XMLParserException {
        String query = quoteXPathQuery(makePathRelativeToMediaDir(episodeFile, rootMediaDir));
        NodeList showNodes = selectNodeList(store, "show/season/episode/file[@location=" + query + "]");
        if (showNodes != null && showNodes.getLength() > 0) {
            Element showEl = (Element) showNodes.item(0).getParentNode().getParentNode().getParentNode();
            Integer part = null;
            if (!((Element) showNodes.item(0)).getAttribute("part").equals("")) {
                part = Integer.parseInt(((Element) showNodes.item(0)).getAttribute("part"));
            }
            return new SearchResult(showEl.getAttribute("id"), showEl.getAttribute("url"), showEl.getAttribute("sourceId"), part, Mode.TV_SHOW);
        }
        query = quoteXPathQuery(title);
        showNodes = selectNodeList(store, "show[@name=" + query + "]");
        if (showNodes != null && showNodes.getLength() > 0) {
            Element showEl = (Element) showNodes.item(0);
            Integer part = null;
            return new SearchResult(showEl.getAttribute("id"), showEl.getAttribute("url"), showEl.getAttribute("sourceId"), part, Mode.TV_SHOW);
        }
        for (Node showNode : selectNodeList(store, "show")) {
            Element showEl = (Element) showNode;
            if (stripPuncuation(showEl.getAttribute("name")).equalsIgnoreCase(title)) {
                Integer part = null;
                return new SearchResult(showEl.getAttribute("id"), showEl.getAttribute("url"), showEl.getAttribute("sourceId"), part, Mode.TV_SHOW);
            }
        }
        return null;
    }

    private String stripPuncuation(String value) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            if (STIP_PUNCUATION.indexOf(c) == -1) {
                result.append(c);
            }
        }
        return result.toString();
    }

    private Document getCache(File rootMediaDirectory) throws StoreException {
        if (storeDoc == null) {
            File cacheFile = getCacheFile(rootMediaDirectory, FILENAME);
            if (cacheFile.exists()) {
                Document doc = null;
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                factory.setValidating(true);
                try {
                    DocumentBuilder builder = XMLParser.createDocBuilder(factory);
                    SimpleErrorHandler errorHandler = new SimpleErrorHandler(cacheFile);
                    builder.setErrorHandler(errorHandler);
                    doc = builder.parse(cacheFile);
                    if (errorHandler.hasErrors()) {
                        throw new StoreException(Messages.getString("XMLStore2.ERRORS_IN_FILE") + cacheFile);
                    }
                } catch (SAXException e) {
                    throw new StoreException(Messages.getString("XMLStore2.UNABLE_PARSE_CACHE_FILE"), e);
                } catch (IOException e) {
                    throw new StoreException(Messages.getString("XMLStore2.UNABLE_READ_CACHE_FILE"), e);
                } catch (ParserConfigurationException e) {
                    throw new StoreException(Messages.getString("XMLStore2.UNABLE_PARSE_CACHE_FILE"), e);
                }
                storeDoc = doc;
            } else {
                Document doc = createCacheFile(cacheFile);
                storeDoc = doc;
            }
        } else {
            if (log.isDebugEnabled()) {
                log.debug("Using cacched Store doc");
            }
        }
        return storeDoc;
    }

    private Document createCacheFile(File cacheFile) throws StoreException {
        Document doc = null;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(false);
        try {
            DocumentBuilder builder = createDocBuilder(factory);
            DocumentType docType = builder.getDOMImplementation().createDocumentType("store", DTD_LOCATION, DTD_WEB_LOCATION);
            doc = builder.getDOMImplementation().createDocument(null, "store", docType);
            Element storeElement = getFirstChildElement(doc, "store");
            storeElement.setAttribute("version", STORE_VERSION.getVersion().toString());
            storeElement.setAttribute("revision", String.valueOf(STORE_VERSION.getRevision()));
        } catch (ParserConfigurationException e) {
            throw new StoreException(Messages.getString("XMLStore2.UNABLE_CREATE_CACHE"), e);
        }
        return doc;
    }

    /** {@inheritDoc} */
    @Override
    public void setParameter(String key, String value) {
    }

    /** {@inheritDoc} */
    @Override
    public String getParameter(String key) {
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public void performedActions(MediaDirectory dir) throws StoreException {
        if (log.isInfoEnabled()) {
            log.info("XMLStore2: Cleaning up store after actions were performed");
        }
        File rootMediaDir = dir.getMediaDirConfig().getMediaDir();
        Document cache = getCache(rootMediaDir);
        try {
            boolean changed = false;
            for (Node fileNode : getFileNodes(cache)) {
                Element el = (Element) fileNode;
                File location = new File(dir.getMediaDirConfig().getMediaDir(), el.getAttribute("location"));
                if (!location.exists()) {
                    if (log.isDebugEnabled()) {
                        log.debug("Unable to find file '" + location.getAbsolutePath() + "' so removing from store");
                    }
                    el.getParentNode().removeChild(el);
                    changed = true;
                }
            }
            for (Node filmNode : selectNodeList(cache, "store/film")) {
                if (!hasFileNodes(filmNode)) {
                    filmNode.getParentNode().removeChild(filmNode);
                    changed = true;
                }
            }
            for (Node showNode : selectNodeList(cache, "store/show")) {
                for (Node seasonNode : selectNodeList(showNode, "season")) {
                    for (Node episodeNode : selectNodeList(seasonNode, "episode")) {
                        if (!hasFileNodes(episodeNode)) {
                            seasonNode.removeChild(episodeNode);
                            changed = true;
                        }
                    }
                    for (Node episodeNode : selectNodeList(seasonNode, "specials")) {
                        if (!hasFileNodes(episodeNode)) {
                            seasonNode.removeChild(episodeNode);
                            changed = true;
                        }
                    }
                    if (!hasFileNodes(seasonNode)) {
                        showNode.removeChild(seasonNode);
                        changed = true;
                    }
                }
                if (!hasFileNodes(showNode)) {
                    showNode.getParentNode().removeChild(showNode);
                    changed = true;
                }
            }
            if (changed) {
                File cacheFile = getCacheFile(rootMediaDir, FILENAME);
                writeCache(cacheFile, cache);
            }
        } catch (XMLParserException e) {
            throw new StoreException(Messages.getString("XMLStore2.UNABLE_PARSE_STORE_XML2"), e);
        }
    }

    private boolean hasFileNodes(Node parentNode) throws XMLParserException {
        IterableNodeList nodes = selectNodeList(parentNode, ".//file");
        return !(nodes == null || nodes.getLength() == 0);
    }

    /** {@inheritDoc} */
    @Override
    public void fileDeleted(MediaDirectory dir, File file) throws StoreException {
        File rootMediaDir = dir.getMediaDirConfig().getMediaDir();
        Document cache = getCache(rootMediaDir);
        try {
            boolean changed = false;
            for (Node fileNode : getFileNodes(cache)) {
                Element el = (Element) fileNode;
                File location = new File(dir.getMediaDirConfig().getMediaDir(), el.getAttribute("location"));
                if (location.equals(file)) {
                    if (log.isDebugEnabled()) {
                        log.debug("Unable to find file '" + location.getAbsolutePath() + "' so removing from store");
                    }
                    el.getParentNode().removeChild(el);
                    changed = true;
                }
            }
            if (changed) {
                File cacheFile = getCacheFile(rootMediaDir, FILENAME);
                writeCache(cacheFile, cache);
            }
        } catch (XMLParserException e) {
            throw new StoreException(Messages.getString("XMLStore2.UNABLE_PARSE_STORE_XML1"), e);
        }
    }

    private Element getNodeWithFile(MediaDirectory dir, File file) throws StoreException, XMLParserException {
        File rootMediaDir = dir.getMediaDirConfig().getMediaDir();
        Document cache = getCache(rootMediaDir);
        for (Node fileNode : getFileNodes(cache)) {
            Element el = (Element) fileNode;
            File location = new File(dir.getMediaDirConfig().getMediaDir(), el.getAttribute("location"));
            if (location.equals(file)) {
                return (Element) fileNode.getParentNode();
            }
        }
        return null;
    }

    private IterableNodeList getFileNodes(Document cache) throws XMLParserException {
        if (fileNodes == null) {
            String path = "//file";
            try {
                fileNodes = XPathAPI.selectNodeList(cache, path);
            } catch (Exception e) {
                throw new XMLParserException(MessageFormat.format(Messages.getString("XMLParser.UNABLE_FIND_PATH"), path), e);
            }
        }
        return new IterableNodeList(fileNodes);
    }

    /** {@inheritDoc} */
    @Override
    public IEpisode getEpisode(MediaDirectory dir, File file) throws StoreException {
        try {
            File rootMediaDir = dir.getMediaDirConfig().getMediaDir();
            Element episodeNode = getNodeWithFile(dir, file);
            if (episodeNode == null || !episodeNode.getNodeName().equals("episode")) {
                return null;
            }
            IEpisode episode = parseEpisodeNode(file, rootMediaDir, episodeNode);
            return episode;
        } catch (XMLParserException e) {
            throw new StoreException(Messages.getString("XMLStore2.UNABLE_PARSE_STORE_XML1"), e);
        } catch (MalformedURLException e) {
            throw new StoreException(Messages.getString("XMLStore2.UNABLE_CREATE_URL"), e);
        }
    }

    protected IEpisode parseEpisodeNode(File file, File rootMediaDir, Element episodeNode) throws StoreException, MalformedURLException {
        Element seasonNode = (Element) episodeNode.getParentNode();
        if (seasonNode == null || !seasonNode.getNodeName().equals("season")) {
            return null;
        }
        Element showNode = (Element) seasonNode.getParentNode();
        if (showNode == null || !showNode.getNodeName().equals("show")) {
            return null;
        }
        IShow show = getShow(rootMediaDir, file, showNode.getAttribute("id"));
        if (show == null) {
            return null;
        }
        ISeason season = getSeason(rootMediaDir, file, show, Integer.parseInt(seasonNode.getAttribute("number")));
        if (season == null) {
            return null;
        }
        IEpisode episode = getEpisode(rootMediaDir, file, season, Integer.parseInt(episodeNode.getAttribute("number")));
        return episode;
    }

    /** {@inheritDoc} */
    @Override
    public IFilm getFilm(MediaDirectory dir, File file) throws StoreException {
        try {
            File rootMediaDir = dir.getMediaDirConfig().getMediaDir();
            Element filmNode = getNodeWithFile(dir, file);
            if (filmNode == null || !filmNode.getNodeName().equals("film")) {
                return null;
            }
            IFilm film = getFilm(rootMediaDir, file, filmNode.getAttribute("id"));
            return film;
        } catch (XMLParserException e) {
            throw new StoreException(Messages.getString("XMLStore2.UNABLE_PARSE_STORE_XML1"), e);
        } catch (MalformedURLException e) {
            throw new StoreException(Messages.getString("XMLStore2.UNABLE_CREATE_URL"), e);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void init(Controller controller, File nativeDir) throws StoreException {
    }

    /** {@inheritDoc} */
    @Override
    public Collection<IEpisode> listEpisodes(MediaDirConfig dirConfig, IProgressMonitor monitor) throws StoreException {
        try {
            File rootMediaDir = dirConfig.getMediaDir();
            Document doc = getCache(dirConfig.getMediaDir());
            if (doc != null) {
                Node store = getStoreNode(doc);
                IterableNodeList normalList = selectNodeList(store, "//episode");
                int normalSize = normalList.getLength();
                IterableNodeList specialList = selectNodeList(store, "//special");
                int specialSize = specialList.getLength();
                monitor.beginTask(MessageFormat.format(Messages.getString("XMLStore2.READING_EPISODES"), dirConfig.getMediaDir().getAbsolutePath()), normalSize + specialSize);
                List<IEpisode> episodes = new ArrayList<IEpisode>(normalSize + specialSize);
                for (int i = 0; i < normalSize; i++) {
                    if (monitor.isCanceled()) {
                        break;
                    }
                    Element episodeNode = (Element) normalList.item(i);
                    Element seasonNode = (Element) episodeNode.getParentNode();
                    Element showNode = (Element) seasonNode.getParentNode();
                    IShow show = new XMLShow(showNode);
                    ISeason season = new XMLSeason(show, seasonNode);
                    episodes.add(new XMLEpisode(season, episodeNode, rootMediaDir));
                    monitor.worked(1);
                }
                for (int i = 0; i < specialSize; i++) {
                    if (monitor.isCanceled()) {
                        break;
                    }
                    Element episodeNode = (Element) specialList.item(i);
                    Element seasonNode = (Element) episodeNode.getParentNode();
                    Element showNode = (Element) seasonNode.getParentNode();
                    IShow show = new XMLShow(showNode);
                    ISeason season = new XMLSeason(show, seasonNode);
                    episodes.add(new XMLEpisode(season, episodeNode, rootMediaDir));
                    monitor.worked(1);
                }
                return episodes;
            }
            return Collections.emptyList();
        } catch (XMLParserException e) {
            throw new StoreException(Messages.getString("XMLStore2.UNABLE_PARSE_STORE_XML1"), e);
        } finally {
            monitor.done();
        }
    }

    /** {@inheritDoc} */
    @Override
    public Collection<IFilm> listFilms(MediaDirConfig dirConfig, IProgressMonitor monitor) throws StoreException {
        try {
            File rootMediaDir = dirConfig.getMediaDir();
            Document doc = getCache(dirConfig.getMediaDir());
            if (doc != null) {
                Node store = getStoreNode(doc);
                List<Element> list = selectChildNodes(store, "film");
                int size = list.size();
                monitor.beginTask(MessageFormat.format(Messages.getString("XMLStore2.READING_FILMS"), dirConfig.getMediaDir().getAbsolutePath()), size);
                List<IFilm> films = new ArrayList<IFilm>(size);
                for (int i = 0; i < size; i++) {
                    if (monitor.isCanceled()) {
                        break;
                    }
                    IFilm film = new XMLFilm(list.get(i), rootMediaDir);
                    films.add(film);
                    monitor.worked(1);
                }
                return films;
            }
            return Collections.emptyList();
        } catch (XMLParserException e) {
            throw new StoreException(Messages.getString("XMLStore2.UNABLE_PARSE_STORE_XML1"), e);
        } finally {
            monitor.done();
        }
    }

    /** {@inheritDoc} */
    @Override
    public void upgrade(MediaDirectory mediaDirectory) throws StoreException {
        File rootMediaDirectory = mediaDirectory.getMediaDirConfig().getMediaDir();
        Document cache = getCache(rootMediaDirectory);
        try {
            Element storeNode = getStoreNode(cache);
            StoreVersion currentVersion = getCurrentVersion(storeNode);
            if (currentVersion.getVersion().compareTo(STORE_VERSION.getVersion()) < 0) {
                try {
                    upgrade20to21(mediaDirectory, cache, storeNode);
                } catch (StoreException e) {
                    throw e;
                } catch (StanwoodException e) {
                    throw new StoreException("Unable to update store", e);
                }
                writeUpgradedStore(rootMediaDirectory, storeNode);
            } else if (currentVersion.getRevision() < 2) {
                upgradeRevision21(mediaDirectory, cache, storeNode);
                writeUpgradedStore(rootMediaDirectory, storeNode);
            } else if (currentVersion.getRevision() < STORE_VERSION.getRevision()) {
                try {
                    upgradeRevisionTo21_3(mediaDirectory, cache, storeNode);
                } catch (StoreException e) {
                    throw e;
                } catch (StanwoodException e) {
                    throw new StoreException("Unable to update store", e);
                }
                writeUpgradedStore(rootMediaDirectory, storeNode);
            }
        } catch (XMLParserException e) {
            throw new StoreException("Unable to upgrade the store", e);
        }
    }

    protected StoreVersion getCurrentVersion(Element storeNode) {
        StoreVersion currentVersion = new StoreVersion(new Version("2.0"), 1);
        if (!storeNode.getAttribute("version").equals("")) {
            currentVersion.setVersion(new Version(storeNode.getAttribute("version")));
        }
        if (!storeNode.getAttribute("revision").equals("")) {
            currentVersion.setRevision(Integer.parseInt(storeNode.getAttribute("revision")));
        }
        return currentVersion;
    }

    private void writeUpgradedStore(File rootMediaDirectory, Element storeNode) throws StoreException {
        File file = getCacheFile(rootMediaDirectory, FILENAME);
        Document upgraded = createCacheFile(file);
        NodeList children = storeNode.getChildNodes();
        Element upgradedStoreNode = getFirstChildElement(upgraded, "store");
        for (int i = 0; i < children.getLength(); i++) {
            Node upgradedNode = upgraded.importNode(children.item(i), true);
            upgradedStoreNode.appendChild(upgradedNode);
        }
        writeCache(file, upgraded);
    }

    private void upgrade20to21(MediaDirectory mediaDirectory, Document doc, Element storeNode) throws StanwoodException {
        MediaDirConfig dirConfig = mediaDirectory.getMediaDirConfig();
        File rootMediaDir = dirConfig.getMediaDir();
        log.info(MessageFormat.format("Upgrading store in media directory ''{0}'' from version 2.0 to {1}", rootMediaDir, STORE_VERSION.toString()));
        if (dirConfig.getMode() == Mode.TV_SHOW) {
            NodeList children = storeNode.getChildNodes();
            for (int i = 0; i < children.getLength(); i++) {
                if (children.item(i).getNodeName().equals("show")) {
                    XMLShow orgShow = new XMLShow((Element) children.item(i));
                    String sourceId = orgShow.getSourceId();
                    if (getUpgradedSourceId(mediaDirectory.getController(), sourceId) != null) {
                        sourceId = getUpgradedSourceId(mediaDirectory.getController(), sourceId);
                    }
                    SearchResult searchResult = new SearchResult(orgShow.getShowId(), sourceId, orgShow.getShowURL().toExternalForm(), null, Mode.TV_SHOW);
                    try {
                        IShow show = getShowFromSource(mediaDirectory, searchResult);
                        log.info(MessageFormat.format("Recache show: {0}", show.getName()));
                        getShowNode(doc, show, true);
                    } catch (MalformedURLException e) {
                        throw new StoreException("Unable to update store", e);
                    } catch (IOException e) {
                        throw new StoreException("Unable to update store", e);
                    }
                }
            }
        } else {
            NodeList children = storeNode.getChildNodes();
            for (int i = 0; i < children.getLength(); i++) {
                if (children.item(i).getNodeName().equals("film")) {
                    XMLFilm orgFilm = new XMLFilm((Element) children.item(i), rootMediaDir);
                    for (IVideoFile files : orgFilm.getFiles()) {
                        String sourceId = orgFilm.getSourceId();
                        if (getUpgradedSourceId(mediaDirectory.getController(), sourceId) != null) {
                            sourceId = getUpgradedSourceId(mediaDirectory.getController(), sourceId);
                        }
                        SearchResult searchResult = new SearchResult(orgFilm.getId(), sourceId, orgFilm.getFilmUrl().toExternalForm(), files.getPart(), Mode.TV_SHOW);
                        try {
                            IFilm film = getFilmFromSource(mediaDirectory, searchResult);
                            orgFilm.setStudio(film.getStudio());
                            orgFilm.setSourceId(sourceId);
                        } catch (MalformedURLException e) {
                            throw new StoreException("Unable to update store", e);
                        } catch (IOException e) {
                            throw new StoreException("Unable to update store", e);
                        }
                    }
                }
            }
        }
    }

    private void upgradeRevisionTo21_3(MediaDirectory mediaDirectory, Document cache, Element storeNode) throws StanwoodException {
        MediaDirConfig dirConfig = mediaDirectory.getMediaDirConfig();
        File rootMediaDir = dirConfig.getMediaDir();
        log.info(MessageFormat.format("Upgrading store in media directory ''{0}'' from version 2.0 to {1}", rootMediaDir, STORE_VERSION.toString()));
        if (dirConfig.getMode() == Mode.FILM) {
            NodeList children = storeNode.getChildNodes();
            for (int i = 0; i < children.getLength(); i++) {
                if (children.item(i).getNodeName().equals("film")) {
                    XMLFilm orgFilm = new XMLFilm((Element) children.item(i), rootMediaDir);
                    for (IVideoFile files : orgFilm.getFiles()) {
                        if (orgFilm.getImageURL() == null || orgFilm.getImageURL().toExternalForm().contains("cf1.imgobject.com")) {
                            String sourceId = orgFilm.getSourceId();
                            SearchResult searchResult = new SearchResult(orgFilm.getId(), sourceId, orgFilm.getFilmUrl().toExternalForm(), files.getPart(), Mode.TV_SHOW);
                            try {
                                IFilm film = getFilmFromSource(mediaDirectory, searchResult);
                                if (film != null) {
                                    orgFilm.setImageURL(film.getImageURL());
                                    orgFilm.setDescription(film.getDescription());
                                    orgFilm.setSummary(film.getSummary());
                                    orgFilm.setWriters(film.getWriters());
                                } else {
                                    log.error(MessageFormat.format("Unable to fetch film information for id {0}", orgFilm.getId()));
                                }
                            } catch (MalformedURLException e) {
                                throw new StoreException("Unable to update store", e);
                            } catch (IOException e) {
                                throw new StoreException("Unable to update store", e);
                            }
                        }
                    }
                }
            }
        }
    }

    private void upgradeRevision21(MediaDirectory mediaDirectory, Document cache, Element storeNode) {
        MediaDirConfig dirConfig = mediaDirectory.getMediaDirConfig();
        File rootMediaDir = dirConfig.getMediaDir();
        log.info(MessageFormat.format("Upgrading store in media directory ''{0}'' from version 2.1 to 2.1.2", rootMediaDir));
        if (dirConfig.getMode() == Mode.TV_SHOW) {
            NodeList children = storeNode.getChildNodes();
            for (int i = 0; i < children.getLength(); i++) {
                if (children.item(i).getNodeName().equals("show")) {
                    XMLShow show = new XMLShow((Element) children.item(i));
                    show.setSourceId(getUpgradedSourceId(mediaDirectory.getController(), show.getSourceId()));
                }
            }
        } else {
            NodeList children = storeNode.getChildNodes();
            for (int i = 0; i < children.getLength(); i++) {
                if (children.item(i).getNodeName().equals("film")) {
                    XMLFilm film = new XMLFilm((Element) children.item(i), rootMediaDir);
                    film.setSourceId(getUpgradedSourceId(mediaDirectory.getController(), film.getSourceId()));
                }
            }
        }
    }

    private String getUpgradedSourceId(Controller controller, String oldSourceId) {
        String id = oldSourceId;
        if (oldSourceId.equals(TagChimpSource.OLD_SOURCE_ID)) {
            id = TagChimpSource.class.getName();
        } else if (oldSourceId.equals(HybridFilmSource.OLD_SOURCE_ID)) {
            id = HybridFilmSource.class.getName();
        } else if (oldSourceId.startsWith("xbmc-")) {
            id = XBMCSource.class.getName() + "#" + oldSourceId.substring(5);
        }
        return id;
    }

    private IShow getShowFromSource(MediaDirectory mediaDirectory, SearchResult searchResult) throws IOException {
        URL showUrl = new URL(searchResult.getUrl());
        String sourceId = searchResult.getSourceId();
        for (ISource source : mediaDirectory.getSources()) {
            try {
                if (sourceId == null || sourceId.equals("") || source.getInfo().getId().equals(sourceId)) {
                    IShow show = source.getShow(searchResult.getId(), showUrl, null);
                    if (show != null) {
                        return show;
                    }
                }
            } catch (SourceException e) {
                log.error(e.getMessage(), e);
                return null;
            }
        }
        return null;
    }

    private IFilm getFilmFromSource(MediaDirectory mediaDirectory, SearchResult searchResult) throws IOException {
        URL filmUrl = new URL(searchResult.getUrl());
        String sourceId = searchResult.getSourceId();
        for (ISource source : mediaDirectory.getSources()) {
            try {
                if (sourceId == null || sourceId.equals("") || source.getInfo().getId().equals(sourceId)) {
                    IFilm show = source.getFilm(searchResult.getId(), filmUrl, null);
                    if (show != null) {
                        return show;
                    }
                }
            } catch (SourceException e) {
                log.error(e.getMessage(), e);
                return null;
            }
        }
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public void fileUpdated(MediaDirectory mediaDirectory, File file) throws StoreException {
    }
}
