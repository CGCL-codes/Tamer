package com.magnatune.internal.ui.search;

import java.util.ArrayList;
import org.eclipse.core.runtime.IProgressMonitor;
import com.evolution.player.core.media.loader.DownloadableMedia;
import com.evolution.player.core.media.search.SearchProvider;
import com.magnatune.internal.core.db.MagnatuneAlbumInfo;
import com.magnatune.internal.core.db.MagnatuneArtistInfo;
import com.magnatune.internal.core.db.MagnatuneSongInfo;

/**
 * @since 0.7
 */
public class MagnatuneArtistSongsMatch extends MagnatuneSearchMatch {

    private final MagnatuneArtistInfo fArtist;

    public MagnatuneArtistSongsMatch(SearchProvider searchProvider, MagnatuneArtistInfo artist) {
        super(searchProvider);
        fArtist = artist;
    }

    @Override
    public String getLabel() {
        return fArtist.getArtistName();
    }

    @Override
    public DownloadableMedia[] getMedias(IProgressMonitor monitor) {
        ArrayList<DownloadableMedia> result = new ArrayList<DownloadableMedia>();
        MagnatuneAlbumInfo[] albums = fArtist.getAlbums();
        for (MagnatuneAlbumInfo album : albums) {
            MagnatuneSongInfo[] tracks = album.getTracks();
            for (MagnatuneSongInfo track : tracks) {
                result.add(track);
            }
        }
        return result.toArray(new DownloadableMedia[result.size()]);
    }
}
