package com.magnatune.internal.ui.search;

import java.util.ArrayList;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.internal.misc.StringMatcher;
import com.evolution.player.core.media.MediaInfo;
import com.evolution.player.core.media.search.IMediaSearchPattern;
import com.evolution.player.core.media.search.ISearchPattern;
import com.evolution.player.core.media.search.IStringSearchPattern;
import com.evolution.player.core.media.search.SearchMatch;
import com.evolution.player.core.media.search.SearchProvider;
import com.evolution.player.core.media.search.SearchType;
import com.magnatune.internal.core.MagnatuneCore;
import com.magnatune.internal.core.MagnatunesUtil;
import com.magnatune.internal.core.db.MagnatuneAlbumInfo;
import com.magnatune.internal.core.db.MagnatuneArtistInfo;
import com.magnatune.internal.core.db.MagnatuneDB;
import com.magnatune.internal.core.db.MagnatuneSongInfo;

public class MagnatuneSearchProvider extends SearchProvider {

    public MagnatuneSearchProvider() {
    }

    @Override
    public String getName() {
        return "Magnatune";
    }

    @Override
    public boolean canHandle(ISearchPattern searchPattern) {
        if (searchPattern.getType() == SearchType.ALBUM_SONGS || searchPattern.getType() == SearchType.DISCOGRAPY) return MagnatunesUtil.isMagnatuneMedia(((IMediaSearchPattern) searchPattern).getMedia(), false);
        if (searchPattern.getType() == SearchType.ALBUM || searchPattern.getType() == SearchType.ARTIST || searchPattern.getType() == SearchType.SONG) return true;
        return false;
    }

    @Override
    public SearchMatch[] search(ISearchPattern searchPattern, IProgressMonitor monitor) {
        if (searchPattern.getType() == SearchType.ALBUM_SONGS) {
            MagnatuneDB db = MagnatuneCore.getMagnatuneDBAccessor().getSynchronizing();
            MediaInfo media = ((IMediaSearchPattern) searchPattern).getMedia();
            MagnatuneArtistInfo artist = db.getArtist(media);
            if (artist == null) return null;
            MagnatuneAlbumInfo album = artist.getAlbum(media);
            if (album == null) return null;
            return new SearchMatch[] { new MagnatuneAlbumSongsMatch(this, album) };
        } else if (searchPattern.getType() == SearchType.DISCOGRAPY) {
            MagnatuneDB db = MagnatuneCore.getMagnatuneDBAccessor().getSynchronizing();
            MediaInfo media = ((IMediaSearchPattern) searchPattern).getMedia();
            MagnatuneArtistInfo artist = db.getArtist(media);
            if (artist == null) return null;
            return new SearchMatch[] { new MagnatuneArtistSongsMatch(this, artist) };
        } else if (searchPattern.getType() == SearchType.ARTIST) {
            String pattern = ((IStringSearchPattern) searchPattern).getPattern();
            StringMatcher matcher = new StringMatcher(pattern, true, false);
            ArrayList<MagnatuneArtistSongsMatch> result = new ArrayList<MagnatuneArtistSongsMatch>();
            MagnatuneDB db = MagnatuneCore.getMagnatuneDBAccessor().getSynchronizing();
            MagnatuneArtistInfo[] artists = db.getArtists();
            for (MagnatuneArtistInfo artist : artists) {
                if (matcher.match(artist.getArtistName())) {
                    result.add(new MagnatuneArtistSongsMatch(this, artist));
                }
            }
            return result.toArray(new MagnatuneArtistSongsMatch[result.size()]);
        } else if (searchPattern.getType() == SearchType.ALBUM) {
            String pattern = ((IStringSearchPattern) searchPattern).getPattern();
            StringMatcher matcher = new StringMatcher(pattern, true, false);
            ArrayList<MagnatuneAlbumSongsMatch> result = new ArrayList<MagnatuneAlbumSongsMatch>();
            MagnatuneDB db = MagnatuneCore.getMagnatuneDBAccessor().getSynchronizing();
            MagnatuneArtistInfo[] artists = db.getArtists();
            for (MagnatuneArtistInfo artist : artists) {
                MagnatuneAlbumInfo[] albums = artist.getAlbums();
                for (MagnatuneAlbumInfo album : albums) {
                    if (matcher.match(album.getAlbumName())) {
                        result.add(new MagnatuneAlbumSongsMatch(this, album));
                    }
                }
            }
            return result.toArray(new MagnatuneAlbumSongsMatch[result.size()]);
        } else if (searchPattern.getType() == SearchType.SONG) {
            String pattern = ((IStringSearchPattern) searchPattern).getPattern();
            StringMatcher matcher = new StringMatcher(pattern, true, false);
            ArrayList<MagnatuneSongMatch> result = new ArrayList<MagnatuneSongMatch>();
            MagnatuneDB db = MagnatuneCore.getMagnatuneDBAccessor().getSynchronizing();
            MagnatuneArtistInfo[] artists = db.getArtists();
            for (MagnatuneArtistInfo artist : artists) {
                MagnatuneAlbumInfo[] albums = artist.getAlbums();
                for (MagnatuneAlbumInfo album : albums) {
                    MagnatuneSongInfo[] tracks = album.getTracks();
                    for (MagnatuneSongInfo track : tracks) {
                        if (matcher.match(track.getSongName())) {
                            result.add(new MagnatuneSongMatch(this, track));
                        }
                    }
                }
            }
            return result.toArray(new MagnatuneSongMatch[result.size()]);
        }
        return null;
    }
}
