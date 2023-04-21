package edu.whitman.halfway.jigs;

import edu.whitman.halfway.util.*;
import java.util.*;
import java.io.*;
import java.lang.ref.*;
import org.apache.log4j.Logger;

/** 
    Used to implement IncludeModules that find all pictures that match
    a certain AlbumObjectFilter.
 */
public abstract class FilterBase implements IncludeModule {

    static Logger log = Logger.getLogger(FilterBase.class);

    ArrayList pictures;

    ArrayList albums;

    boolean isAlbumInclude;

    /** A map from canonical saFile objects to SoftReferences, each
     * holding an Album (for searching)*/
    static Map searchDirCache = new HashMap();

    public abstract boolean initialize(List args, BasicAlbum album);

    /** Call this on args first to determine if we're including
     * pictures or albums */
    protected void setIncludeType(String s) {
        if (s.equalsIgnoreCase("album")) {
            isAlbumInclude = true;
        } else if (s.equalsIgnoreCase("picture")) {
            isAlbumInclude = false;
        } else {
            usage();
            log.error("Invalid args. " + StringUtil.newline + usage());
            throw new IllegalArgumentException("Frist arg must be \"Picture\" or \"Album\"");
        }
    }

    protected void setIncludeType(List args) {
        setIncludeType((String) args.remove(0));
    }

    public BasicAlbum getSearchAlbum(BasicAlbum currentAlbum, String albumString) {
        assert currentAlbum != null;
        File saFile = FileUtil.getValidFile(currentAlbum.getDir(), albumString);
        if (saFile != null && saFile.exists() && saFile.isDirectory()) {
            String canonical = null;
            try {
                canonical = saFile.getCanonicalPath();
            } catch (IOException e) {
                log.warn("Canonicalization of saFile failed, ignoring cache.");
            }
            if (searchDirCache != null & canonical != null && searchDirCache.containsKey(canonical)) {
                SoftReference srToAlbum = (SoftReference) searchDirCache.get(canonical);
                BasicAlbum searchAlbum = (BasicAlbum) srToAlbum.get();
                if (searchAlbum != null) {
                    log.info("Using cached searchAlbum " + searchAlbum);
                    return searchAlbum;
                }
            }
            log.info("\n\n\t\tBuilding search album on " + saFile);
            BasicAlbum searchAlbum = new BasicAlbum(saFile, currentAlbum.getFilter());
            if (!searchAlbum.isUsefullAlbum()) {
                searchAlbum = null;
            } else if (canonical != null && searchDirCache != null) {
                searchDirCache.put(canonical, new SoftReference(searchAlbum));
            }
            return searchAlbum;
        } else {
            log.error("Invalid album " + saFile + ", are you sure " + albumString + " is a valid directory");
            return null;
        }
    }

    public boolean finishInitialize(AlbumObjectFilter filter, List lastArgs, BasicAlbum currentAlbum) {
        boolean recursive = false;
        if (lastArgs.size() < 1 || lastArgs.size() > 2) {
            log.error("Invalid args to PictureFilterBase, need searchDir and optional [recursive]");
            return false;
        }
        String albumString = (String) lastArgs.get(0);
        BasicAlbum searchAlbum = getSearchAlbum(currentAlbum, albumString);
        if (searchAlbum == null) {
            return false;
        }
        if (log.isDebugEnabled()) log.debug("finishInitialize on search album " + searchAlbum.getDir() + "\n\t with " + searchAlbum.getSubAlbums().length + " subalbums");
        if (lastArgs.size() == 2) {
            String rString = (String) lastArgs.get(1);
            if (rString.trim().equalsIgnoreCase("recursive")) {
                recursive = true;
            }
        }
        if (!isAlbumInclude) {
            log.info("Finding pictures in search album" + searchAlbum);
            pictures = FilterUtil.findPictures(searchAlbum, new PictureFilterAdapter(filter), recursive);
            if (log.isDebugEnabled()) log.debug(("Found " + pictures.size() + " pictures"));
            setParents(pictures, currentAlbum);
            albums = new ArrayList(0);
        } else {
            log.info("Finding albums in search album" + searchAlbum);
            albums = FilterUtil.findAlbums(searchAlbum, new AlbumFilterAdapter(filter), recursive);
            setParents(albums, currentAlbum);
            pictures = new ArrayList(0);
        }
        searchAlbum = null;
        System.gc();
        log.info("Memory: " + MiscUtil.getMemoryStats());
        return true;
    }

    static void setParents(ArrayList objs, Album parent) {
        Iterator iter = objs.iterator();
        while (iter.hasNext()) {
            setParent(iter.next(), parent);
        }
    }

    static void setParent(Object obj, Album parent) {
        log.debug("Giving " + obj + " parent " + parent);
        if (obj instanceof BasicAlbum) {
            ((BasicAlbum) obj).setParent(parent);
        } else if (obj instanceof BasicPicture) {
            ((BasicPicture) obj).setParent(parent);
        } else {
            log.error("Assertion failled, unexpected type of AlbumObject.");
            throw new Error();
        }
    }

    public ArrayList getPictures() {
        return pictures;
    }

    public ArrayList getAlbums() {
        return albums;
    }

    public abstract String usage();
}
