package net.sf.ulmac.core.libraries;

import java.util.List;
import net.sf.ulmac.core.items.FileQueueItem;
import net.sf.ulmac.core.types.MusicLibraryType;

public interface IMusicLibrary {

    public boolean add(FileQueueItem item, List<String> playLists, List<String> playListFolders, boolean isArtistSubfolder, boolean isGapless, boolean excludeFromShuffle);

    public void dispose();

    public List<String> getPlayListFolderNames();

    public List<String> getPlayListNames();

    public MusicLibraryType getType();

    public boolean init();

    @Override
    public String toString();
}
