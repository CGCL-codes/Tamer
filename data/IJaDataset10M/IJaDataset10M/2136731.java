package net.kodeninja.jem.server.storage;

import java.net.URI;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import net.kodeninja.jem.server.JemServer;
import net.kodeninja.util.MimeType;

public class MemoryStorageModule implements StorageModule {

    protected Set<MediaUpdateHook> hooks = new HashSet<MediaUpdateHook>();

    protected Set<MediaCollection> collections = new HashSet<MediaCollection>();

    protected Set<MediaItem> mediaList = new HashSet<MediaItem>(1000);

    protected MimeTypeTree<MediaItem> mediaMimetypes = new MimeTypeTree<MediaItem>();

    protected Map<MetadataType, Map<Metadata, List<MediaItem>>> mediaMetadata = new HashMap<MetadataType, Map<Metadata, List<MediaItem>>>();

    private int updating = 0;

    private int changes = 0;

    private int microUpdateThreshold = MICRO_UPDATE_DEFAULT_THRESHOLD;

    private static final int MICRO_UPDATE_DEFAULT_THRESHOLD = 1000;

    public void hookMediaUpdate(MediaUpdateHook hook) {
        hooks.add(hook);
    }

    public void unhookMediaUpdate(MediaUpdateHook hook) {
        hooks.add(hook);
    }

    private synchronized void announceUpdate() {
        if (changes > 0) {
            JemServer.getInstance().addLog("Announcing media update...");
            for (MediaUpdateHook hook : hooks) hook.mediaChanged();
            changes = 0;
        }
    }

    private synchronized void midUpdate() {
        if (changes > microUpdateThreshold) {
            microUpdateThreshold *= 2;
            JemServer.getInstance().addLog("Announcing media micro-update...");
            for (MediaUpdateHook hook : hooks) hook.mediaChanged();
            changes = 0;
        }
    }

    public synchronized void startUpdate() {
        if (updating == 0) microUpdateThreshold = MICRO_UPDATE_DEFAULT_THRESHOLD;
        updating++;
    }

    public synchronized void finishUpdate() {
        if (updating > 0) updating--;
        if (updating == 0) announceUpdate();
    }

    public synchronized Set<MediaItem> getAllMedia() {
        Set<MediaItem> result = new HashSet<MediaItem>();
        result.addAll(mediaList);
        return result;
    }

    public int mediaCount() {
        return mediaList.size();
    }

    public Set<MediaItem> getMediaMatching(MimeType type) {
        return mediaMimetypes.getMatching(type);
    }

    public synchronized Set<MediaItem> getMediaMatching(MetadataType type, String value) {
        Set<MediaItem> result = new HashSet<MediaItem>();
        Map<Metadata, List<MediaItem>> metadata = mediaMetadata.get(type);
        if (metadata != null) for (Metadata md : metadata.keySet()) if (md.getB().equalsIgnoreCase(value)) result.addAll(metadata.get(md));
        return result;
    }

    public boolean mediaExists(URI uri) {
        return mediaList.contains(new MediaItem(uri, null));
    }

    public synchronized MediaItem addNewMedia(URI uri, MimeType type) {
        MediaItem item = new MediaItem(uri, new LinkedList<Metadata>());
        if (mediaList.add(item)) {
            mediaMimetypes.add(item, type);
            for (MediaCollection collection : collections) collection.addMedia(item);
            changes++;
            midUpdate();
        }
        return item;
    }

    public boolean removeMedia(MediaItem item) {
        boolean result = false;
        mediaList.remove(item);
        mediaMimetypes.remove(item);
        Iterator<MetadataType> typeIt = mediaMetadata.keySet().iterator();
        while (typeIt.hasNext()) {
            MetadataType type = typeIt.next();
            Map<Metadata, List<MediaItem>> metadataList = mediaMetadata.get(type);
            Iterator<Metadata> metadataIt = metadataList.keySet().iterator();
            while (metadataIt.hasNext()) {
                Metadata metadata = metadataIt.next();
                List<MediaItem> metadataMediaList = metadataList.get(metadata);
                if (metadataMediaList.remove(item)) {
                    result = true;
                    if (metadataMediaList.size() == 0) metadataIt.remove();
                }
            }
            if (metadataList.size() == 0) typeIt.remove();
        }
        if (result) {
            changes++;
            midUpdate();
            for (MediaCollection collection : collections) collection.addMedia(item);
        }
        return result;
    }

    public MimeType getMimeType(MediaItem item) {
        return mediaMimetypes.getMimeType(item);
    }

    public synchronized Set<String> getMetadataValues(MetadataType type) {
        Set<String> result = new TreeSet<String>();
        Map<Metadata, List<MediaItem>> metadataMap = mediaMetadata.get(type);
        if (metadataMap != null) for (Metadata metadata : metadataMap.keySet()) result.add(metadata.getB());
        return result;
    }

    public synchronized void addMediaMetadata(MediaItem item, MetadataType type, String value) {
        for (Metadata md : item.getMetadataList()) if (md.getType().equals(type) && (md.getValue().equalsIgnoreCase(value))) return;
        Map<Metadata, List<MediaItem>> metadataMap = mediaMetadata.get(type);
        if (metadataMap == null) {
            metadataMap = new HashMap<Metadata, List<MediaItem>>();
            mediaMetadata.put(type, metadataMap);
        }
        for (Metadata metadata : metadataMap.keySet()) if (metadata.getB().equalsIgnoreCase(value)) {
            metadataMap.get(metadata).add(item);
            item.getB().add(metadata);
            return;
        }
        Metadata metadata = new Metadata(type, value);
        item.getB().add(metadata);
        List<MediaItem> itemList = new LinkedList<MediaItem>();
        metadataMap.put(metadata, itemList);
        itemList.add(item);
        changes++;
        midUpdate();
    }

    public synchronized boolean removeMediaMetadata(MediaItem item, MetadataType type, String value) {
        boolean result = false;
        Map<Metadata, List<MediaItem>> metadataMap = mediaMetadata.get(type);
        if (metadataMap != null) {
            Iterator<Metadata> metadataIt = metadataMap.keySet().iterator();
            while (metadataIt.hasNext()) {
                Metadata metadata = metadataIt.next();
                if (metadata.getB().equalsIgnoreCase(value)) {
                    List<MediaItem> itemList = metadataMap.get(metadata);
                    if (itemList.remove(item)) {
                        result = true;
                        if (itemList.size() == 0) metadataIt.remove();
                    }
                }
            }
            if (metadataMap.size() == 0) mediaMetadata.remove(metadataMap);
        }
        if (result) {
            changes++;
            midUpdate();
        }
        return result;
    }

    public synchronized Set<MediaCollection> getAllCollections() {
        Set<MediaCollection> result = new HashSet<MediaCollection>();
        result.addAll(collections);
        return result;
    }

    public int collectionCount() {
        return collections.size();
    }

    public synchronized void addCollection(MediaCollection collection) {
        collections.add(collection);
        for (MediaItem mi : mediaList) collection.addMedia(mi);
    }

    public synchronized boolean removeCollection(MediaCollection collection) {
        return collections.remove(collection);
    }

    public synchronized void setupCollection(MediaCollection collection) {
        for (MediaItem item : mediaList) collection.addMedia(item);
    }

    public String getName() {
        return "Memory Storage Module";
    }

    public int getVersionMajor() {
        return 0;
    }

    public int getVersionMinor() {
        return 1;
    }

    public int getVersionRevision() {
        return 0;
    }
}
