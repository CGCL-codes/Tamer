package org.nightlabs.eclipse.rap.dionysos;

import java.net.MalformedURLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import javax.jdo.FetchPlan;
import javax.xml.namespace.QName;
import javax.xml.rpc.ServiceException;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.ser.BeanDeserializerFactory;
import org.apache.axis.encoding.ser.BeanSerializerFactory;
import org.apache.axis.encoding.ser.EnumDeserializerFactory;
import org.apache.axis.encoding.ser.EnumSerializerFactory;
import org.nightlabs.dionysos.core.PlayerException;
import org.nightlabs.dionysos.core.mediaplayer.PlayerManagerStatus;
import org.nightlabs.dionysos.facade.axis.RemoteEvent;
import org.nightlabs.dionysos.facade.axis.client.MediaClient;
import org.nightlabs.dionysos.facade.axis.client.PlayerClient;
import org.nightlabs.dionysos.facade.axis.client.QueueClient;
import org.nightlabs.dionysos.facade.axis.ser.ReflectionDeserializerFactory;
import org.nightlabs.dionysos.facade.axis.ser.ReflectionSerializerFactory;
import org.nightlabs.dionysos.jdo.Album;
import org.nightlabs.dionysos.jdo.Artist;
import org.nightlabs.dionysos.jdo.Cover;
import org.nightlabs.dionysos.jdo.GenreID;
import org.nightlabs.dionysos.jdo.Image;
import org.nightlabs.dionysos.jdo.MediaContainer;
import org.nightlabs.dionysos.jdo.MediaContainerID;
import org.nightlabs.dionysos.jdo.PlayList;
import org.nightlabs.dionysos.jdo.PlayerHistory;
import org.nightlabs.dionysos.jdo.PlayerHistoryID;
import org.nightlabs.dionysos.jdo.PlayerItem;
import org.nightlabs.dionysos.jdo.PlayerItemID;
import org.nightlabs.dionysos.jdo.PlayerQueue;
import org.nightlabs.dionysos.jdo.PlayerQueueID;
import org.nightlabs.dionysos.jdo.Resource;
import org.nightlabs.dionysos.jdo.ResourceID;
import org.nightlabs.dionysos.jdo.ResourceType;
import org.nightlabs.dionysos.jdo.ResourceTypeID;
import org.nightlabs.dionysos.jdo.Stream;
import org.nightlabs.dionysos.jdo.StreamingStation;
import org.nightlabs.dionysos.jdo.Track;
import org.nightlabs.dionysos.jdo.User;
import org.nightlabs.dionysos.jdo.UserID;
import org.nightlabs.dionysos.jdo.util.JDOChangeEvent;
import org.nightlabs.jdo.NLJDOHelper;
import org.nightlabs.jdo.ObjectID;

/**
 * This class wraps all Dionysos server connections
 * in a centralized place.
 * 
 * @author Marc Klinger - marc[at]nightlabs[dot]de
 */
public class DionysosConnector {

    private static final Class[] reflectionMappingClasses = { Artist.class, Album.class, Track.class, Resource.class, ResourceType.class, User.class, Cover.class, Image.class, PlayList.class, PlayerManagerStatus.class, RemoteEvent.class, JDOChangeEvent.class, MediaContainerID.class, GenreID.class, ResourceID.class, ResourceTypeID.class, UserID.class, PlayerHistory.class, PlayerHistoryID.class, PlayerQueue.class, PlayerQueueID.class, PlayerItem.class, PlayerItemID.class, StreamingStation.class, Stream.class };

    private static final Class[] beanMappingClasses = {};

    private RemoteEventHandler remoteEventHandler = null;

    public static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(DionysosConnector.class);

    protected String language = Locale.ENGLISH.getLanguage();

    private static DionysosConnector sharedInstance;

    public static DionysosConnector sharedInstance() {
        if (sharedInstance == null) sharedInstance = new DionysosConnector();
        return sharedInstance;
    }

    private Set<RemoteEventListener> listeners;

    public void addRemoteEventListener(RemoteEventListener listener) {
        if (listeners == null) {
            listeners = new HashSet<RemoteEventListener>();
            if (remoteEventHandler == null) {
                remoteEventHandler = new RemoteEventHandler(this);
                remoteEventHandler.setDaemon(true);
                remoteEventHandler.start();
            } else {
                remoteEventHandler.doRestart();
            }
        }
        listeners.add(listener);
    }

    public void removeRemoteEventListener(RemoteEventListener listener) {
        if (listeners == null) return;
        listeners.remove(listener);
        if (listeners.isEmpty()) remoteEventHandler.doStop();
    }

    private MediaClient getMedia() throws ServiceException {
        return new MediaClient(createCall("Media"));
    }

    private QueueClient getQueue() throws ServiceException {
        return new QueueClient(createCall("Queue"));
    }

    private PlayerClient getPlayer() throws ServiceException {
        return new PlayerClient(createCall("Player"));
    }

    public static final String[] DEFAULT_FETCH_GROUPS = { MediaContainer.FETCH_GROUP_NAMES };

    public static final String[] PLAY_LIST_FETCH_GROUPS = new String[] { FetchPlan.DEFAULT, MediaContainer.FETCH_GROUP_NAMES, PlayList.FETCH_GROUP_NAMES, Track.FETCH_GROUP_ARTIST };

    public static final String[] PLAY_LIST_TRACKS_FETCH_GROUPS = new String[] { MediaContainer.FETCH_GROUP_NAMES, FetchPlan.DEFAULT, Track.FETCH_GROUP_ARTIST };

    private Collection<? extends MediaContainer> localizeAll(Collection<? extends MediaContainer> mediaContainers) {
        for (MediaContainer container : mediaContainers) container.localize(language);
        return mediaContainers;
    }

    /**
   * Getter for <code>language</code>.
   * @return Returns the language.
   */
    public String getLanguage() {
        return language;
    }

    /**
   * Setter for <code>language</code>.
   * @param language The language to set.
   */
    public void setLanguage(String language) {
        this.language = language;
    }

    private static String getNamespaceByType(Class clazz) {
        StringBuffer ns = new StringBuffer();
        ns.append("http://");
        String[] parts = clazz.getPackage().getName().split("\\.");
        for (int i = parts.length - 1; i >= 0; i--) {
            ns.append(parts[i]);
            if (i > 0) ns.append(".");
        }
        ns.append("/");
        return ns.toString();
    }

    private static void registerTypeMapping(Call call, Class clazz, Class serializer, Class deserializer) {
        QName qname = new QName(getNamespaceByType(clazz), clazz.getSimpleName());
        if (log.isDebugEnabled()) {
            log.debug("Registering type mapping for xml type " + qname);
            log.debug("    serializer=" + serializer.getName());
            log.debug("    deserializer=" + deserializer.getName());
        }
        call.registerTypeMapping(clazz, qname, serializer, deserializer);
    }

    private static void registerBeanMapping(Call call, Class clazz) {
        registerTypeMapping(call, clazz, BeanSerializerFactory.class, BeanDeserializerFactory.class);
    }

    private static void registerReflectionMapping(Call call, Class clazz) {
        registerTypeMapping(call, clazz, ReflectionSerializerFactory.class, ReflectionDeserializerFactory.class);
    }

    static Call createCall(String serviceName) throws ServiceException {
        String serviceBaseURL = getServiceBaseURL();
        String endpoint = serviceBaseURL + serviceName;
        Service service = new Service();
        Call call = (Call) service.createCall();
        try {
            call.setTargetEndpointAddress(new java.net.URL(endpoint));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        for (Class clazz : reflectionMappingClasses) registerReflectionMapping(call, clazz);
        for (Class clazz : beanMappingClasses) registerBeanMapping(call, clazz);
        return call;
    }

    /**
	 * Get the service base URL from the preference store.
	 * @return The service base URL from the preference store
	 */
    private static String getServiceBaseURL() {
        String serviceBaseURL = DionysosPlugin.getDefault().getPreferenceStore().getString(IDionysosConstants.PREF_BASE_URL);
        while (serviceBaseURL == null || "".equals(serviceBaseURL)) {
            System.err.println("Service base url is empty! This sould actually never happen - but it does!");
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            serviceBaseURL = DionysosPlugin.getDefault().getPreferenceStore().getString(IDionysosConstants.PREF_BASE_URL);
        }
        if (!serviceBaseURL.endsWith("/")) serviceBaseURL += "/";
        return serviceBaseURL;
    }

    private static Throwable getCause(Throwable cause, Class<PlayerException> clazz) {
        while (cause != null && !(cause.getClass() == clazz)) cause = cause.getCause();
        return cause;
    }

    private static PlayerException getPlayerException(Throwable e) {
        String err = "Error in DionysosConnector: " + e.getMessage();
        log.error(err, e);
        Throwable cause = getCause(e, PlayerException.class);
        if (cause != null) return (PlayerException) cause; else return new PlayerException(err, e);
    }

    private static RuntimeException getRuntimeException(Throwable e) {
        String err = "Error in DionysosConnector: " + e.getMessage();
        log.error(err, e);
        if (e instanceof RuntimeException) return (RuntimeException) e; else return new RuntimeException(err, e);
    }

    @SuppressWarnings("unchecked")
    public Collection<Artist> getArtists() {
        try {
            Collection<Artist> artists = getMedia().getArtists(DEFAULT_FETCH_GROUPS, NLJDOHelper.MAX_FETCH_DEPTH_NO_LIMIT);
            localizeAll(artists);
            return artists;
        } catch (Exception e) {
            throw getRuntimeException(e);
        }
    }

    public Cover getAlbumCoverWithData(String mediaConatainerID) {
        try {
            return getMedia().getAlbumCoverWithData(mediaConatainerID);
        } catch (Exception e) {
            throw getRuntimeException(e);
        }
    }

    public Collection getPlayLists() {
        try {
            return getMedia().getPlayLists(PLAY_LIST_FETCH_GROUPS, NLJDOHelper.MAX_FETCH_DEPTH_NO_LIMIT);
        } catch (Exception e) {
            throw getRuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public List<Album> getAlbums(String artistMediaContainerID) {
        return (List<Album>) getSubContainers(artistMediaContainerID, DEFAULT_FETCH_GROUPS, true);
    }

    @SuppressWarnings("unchecked")
    public List<Track> getTracks(String albumMediaContainerID) {
        return (List<Track>) getSubContainers(albumMediaContainerID, DEFAULT_FETCH_GROUPS, true);
    }

    @SuppressWarnings("unchecked")
    public List<PlayList> getPlayListTracks(String playListMediaContainerID) {
        return (List<PlayList>) getSubContainers(playListMediaContainerID, PLAY_LIST_TRACKS_FETCH_GROUPS, true);
    }

    public void addAlbumCover(String mediaContainerID, byte[] imageData, String filename) {
        try {
            MediaClient media = getMedia();
            media.addAlbumCover(mediaContainerID, imageData, filename);
        } catch (Exception e) {
            throw getRuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public List<? extends MediaContainer> getSubContainers(String mediaContainerID, String[] fetchGroups, boolean localize) {
        try {
            List<? extends MediaContainer> subContainers = getMedia().getSubContainers(mediaContainerID, fetchGroups, NLJDOHelper.MAX_FETCH_DEPTH_NO_LIMIT);
            if (localize) localizeAll(subContainers);
            return subContainers;
        } catch (Exception e) {
            throw getRuntimeException(e);
        }
    }

    public MediaContainer getMediaContainer(String mediaContainerID, String[] fetchGroups, boolean localize) {
        try {
            MediaContainer mc = getMedia().getMediaContainer(mediaContainerID, fetchGroups, NLJDOHelper.MAX_FETCH_DEPTH_NO_LIMIT);
            if (localize) mc.localize(language);
            return mc;
        } catch (Exception e) {
            throw getRuntimeException(e);
        }
    }

    public void setName(String mediaContainerID, String name) {
        try {
            MediaClient media = getMedia();
            media.setName(mediaContainerID, language, name);
        } catch (Exception e) {
            throw getRuntimeException(e);
        }
    }

    public void storeMediaContainer(MediaContainer mc) {
        try {
            MediaClient media = getMedia();
            media.storeMediaContainer(mc);
        } catch (Exception e) {
            throw getRuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public List<PlayerItem> getQueueList() {
        try {
            QueueClient queue = getQueue();
            List<PlayerItem> queueList = queue.getQueueList(new String[] { PlayerItem.FETCH_GROUP_RESOURCE_CONTAINER, PlayerItem.FETCH_GROUP_CREATE_USER, MediaContainer.FETCH_GROUP_NAMES, Track.FETCH_GROUP_ARTIST, Track.FETCH_GROUP_ALBUM, Stream.FETCH_GROUP_STREAMING_STATION }, NLJDOHelper.MAX_FETCH_DEPTH_NO_LIMIT);
            for (PlayerItem item : queueList) item.getResourceContainer().localize(language);
            return queueList;
        } catch (Exception e) {
            throw getRuntimeException(e);
        }
    }

    public void enqueue(String mediaContainerID) {
        try {
            User user = getUserFromPreferences();
            QueueClient queue = getQueue();
            queue.enqueue(mediaContainerID, user);
        } catch (Exception e) {
            throw getRuntimeException(e);
        }
    }

    private User getUserFromPreferences() {
        User user = null;
        String userID = DionysosPlugin.getDefault().getPreferenceStore().getString(IDionysosConstants.PREF_USER_ID);
        if (userID != null && !"".equals(userID)) {
            String password = DionysosPlugin.getDefault().getPreferenceStore().getString(IDionysosConstants.PREF_PASSWORD);
            user = new User(userID);
            if (password != null && !"".equals(password)) user.setPassword(password);
        }
        return user;
    }

    public void enqueueAll(List mediaContainers) {
        try {
            User user = getUserFromPreferences();
            QueueClient queue = getQueue();
            queue.enqueueAll(mediaContainers, user);
        } catch (Exception e) {
            throw getRuntimeException(e);
        }
    }

    public void unqueue(int index, String mediaContainerID) {
        try {
            QueueClient queue = getQueue();
            queue.unqueue(index, mediaContainerID);
        } catch (Exception e) {
            throw getRuntimeException(e);
        }
    }

    public void unqueueAll(List indezes, List mediaContainerIDs) {
        try {
            QueueClient queue = getQueue();
            queue.unqueueAll(indezes, mediaContainerIDs);
        } catch (Exception e) {
            throw getRuntimeException(e);
        }
    }

    public void play() {
        try {
            PlayerClient player = getPlayer();
            player.play();
        } catch (Exception e) {
            throw getRuntimeException(e);
        }
    }

    public void stop() {
        try {
            PlayerClient player = getPlayer();
            player.stop();
        } catch (Exception e) {
            throw getRuntimeException(e);
        }
    }

    public void next() {
        try {
            PlayerClient player = getPlayer();
            player.next();
        } catch (Exception e) {
            throw getRuntimeException(e);
        }
    }

    public void pause() {
        try {
            PlayerClient player = getPlayer();
            player.pause();
        } catch (Exception e) {
            throw getRuntimeException(e);
        }
    }

    public void queueMove(int oldIndex, int newIndex) {
        try {
            QueueClient queue = getQueue();
            queue.move(oldIndex, newIndex);
        } catch (Exception e) {
            throw getRuntimeException(e);
        }
    }

    public void moveSubContainer(String mediaContainerID, int oldIndex, int newIndex) {
        try {
            MediaClient media = getMedia();
            media.moveSubContainer(mediaContainerID, oldIndex, newIndex);
        } catch (Exception e) {
            throw getRuntimeException(e);
        }
    }

    public String getPlayerStatus() throws PlayerException {
        return getPlayerManagerStatus().getStatus();
    }

    public PlayerManagerStatus getPlayerManagerStatus() throws PlayerException {
        try {
            PlayerClient player = getPlayer();
            PlayerManagerStatus status = player.getStatus();
            return status;
        } catch (Exception e) {
            throw getPlayerException(e);
        }
    }

    public PlayerItem getPlayerItem() throws PlayerException {
        try {
            return getPlayer().getPlayerItem();
        } catch (Exception e) {
            throw getPlayerException(e);
        }
    }

    public void mergeArtists(List srcArtistMediaContainerIDs, String destArtistMediaContainerID) {
        try {
            MediaClient media = getMedia();
            for (Iterator iter = srcArtistMediaContainerIDs.iterator(); iter.hasNext(); ) media.mergeArtists((String) iter.next(), destArtistMediaContainerID);
        } catch (Exception e) {
            throw getRuntimeException(e);
        }
    }

    public void storePlayList(PlayList playList) {
        try {
            getMedia().storeMediaContainer(playList);
        } catch (Exception e) {
            throw getRuntimeException(e);
        }
    }

    public void deletePlayList(MediaContainerID playListID) {
        try {
            getMedia().removeFromContainersAndDelete(playListID);
        } catch (Exception e) {
            throw getRuntimeException(e);
        }
    }

    /**
   * removes subContainer
   * 
   * @param mediaContainer is the MediaContainer that holds the SubContainers
   * @param subContainersToRemove the subContainers to remove
   */
    public void removeSubcontainerNoDelete(Map<MediaContainerID, Set<MediaContainerID>> tracksToRemove) {
        try {
            getMedia().removeSubContainerNoDelete(tracksToRemove);
        } catch (Exception e) {
            throw getRuntimeException(e);
        }
    }

    public String getPersistenceCapableClassName(ObjectID objectID) {
        try {
            MediaClient media = getMedia();
            return media.getPersistenceCapableClassName(objectID);
        } catch (Exception e) {
            throw getRuntimeException(e);
        }
    }

    public void update() {
        try {
            getMedia().update();
        } catch (Exception e) {
            throw getRuntimeException(e);
        }
    }

    public PlayerQueueID getDefaultPlayerQueueID() {
        try {
            return getQueue().getDefaultPlayerQueueID();
        } catch (Exception e) {
            throw getRuntimeException(e);
        }
    }

    /**
	 * Get the listeners.
	 * @return the listeners
	 */
    public Set<RemoteEventListener> getListeners() {
        return listeners;
    }

    @SuppressWarnings("unchecked")
    public Collection<StreamingStation> getStreamingStations(String[] fetchGroups, int maxFetchDepth) {
        try {
            Collection<StreamingStation> stations = getMedia().getStreamingStations(fetchGroups, maxFetchDepth);
            localizeAll(stations);
            return stations;
        } catch (Exception e) {
            throw new RuntimeException("getDefaultPlayerQueueID failed", e);
        }
    }

    @SuppressWarnings("unchecked")
    public List<Stream> getStreams(String streamingStationMediaContainerID) {
        List<Stream> streams = (List<Stream>) getSubContainers(streamingStationMediaContainerID, DEFAULT_FETCH_GROUPS, true);
        localizeAll(streams);
        return streams;
    }
}
