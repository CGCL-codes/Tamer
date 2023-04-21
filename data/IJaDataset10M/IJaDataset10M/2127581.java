package com.evolution.player.jamendo.tests;

import junit.framework.Assert;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.junit.Test;
import com.evolution.player.core.media.MediaInfo;
import com.evolution.player.core.media.search.IMediaSearchPattern;
import com.evolution.player.core.media.search.ISearchPattern;
import com.evolution.player.core.media.search.IStringSearchPattern;
import com.evolution.player.core.media.search.SearchMatch;
import com.evolution.player.core.media.search.SearchType;
import com.evolution.player.internal.jamendo.JamendoMediaInfo;
import com.evolution.player.internal.jamendo.search.JamendoSearchProvider;

/**
 * @since 0.5
 */
public class JamendoSearchTests {

    /**
	 * @since 0.6
	 */
    public class MediaSearchPattern implements IMediaSearchPattern {

        private final SearchType fType;

        private final int fJamendoId;

        public MediaSearchPattern(int jamendoId, SearchType type) {
            fJamendoId = jamendoId;
            fType = type;
        }

        public SearchType getType() {
            return fType;
        }

        public MediaInfo getMedia() {
            return new JamendoMediaInfo(String.valueOf(fJamendoId), "", "", "", -1);
        }
    }

    /**
	 * @since 0.6
	 */
    public class StringSearchPattern implements IStringSearchPattern {

        private final String fPattern;

        private final SearchType fType;

        public StringSearchPattern(String pattern, SearchType type) {
            fPattern = pattern;
            fType = type;
        }

        public String getPattern() {
            return fPattern;
        }

        public SearchType getType() {
            return fType;
        }
    }

    @Test
    public void searchArtistTest01() throws Exception {
        JamendoSearchProvider provider = new JamendoSearchProvider();
        ISearchPattern pattern = new StringSearchPattern("After Glow", SearchType.ARTIST);
        Assert.assertTrue(provider.canHandle(pattern));
        SearchMatch[] matches = provider.search(pattern, new NullProgressMonitor());
        Assert.assertEquals(1, matches.length);
        String label = matches[0].getLabel();
        Assert.assertEquals("After Glow", label);
        MediaInfo[] medias = matches[0].getMedias(new NullProgressMonitor());
        Assert.assertTrue(medias.length > 0);
        Thread.sleep(1000);
    }

    @Test
    public void searchAlbumSongs() throws Exception {
        JamendoSearchProvider provider = new JamendoSearchProvider();
        ISearchPattern pattern = new MediaSearchPattern(39221, SearchType.ALBUM_SONGS);
        Assert.assertTrue(provider.canHandle(pattern));
        SearchMatch[] matches = provider.search(pattern, new NullProgressMonitor());
        Assert.assertEquals(1, matches.length);
        String label = matches[0].getLabel();
        Assert.assertEquals("Combat Circus", label);
        MediaInfo[] medias = matches[0].getMedias(new NullProgressMonitor());
        Assert.assertTrue(medias.length > 0);
        Thread.sleep(1000);
    }

    @Test
    public void searchArtistSongs() throws Exception {
        JamendoSearchProvider provider = new JamendoSearchProvider();
        ISearchPattern pattern = new MediaSearchPattern(39221, SearchType.DISCOGRAPY);
        Assert.assertTrue(provider.canHandle(pattern));
        SearchMatch[] matches = provider.search(pattern, new NullProgressMonitor());
        Assert.assertEquals(1, matches.length);
        String label = matches[0].getLabel();
        Assert.assertEquals("Talco", label);
        MediaInfo[] medias = matches[0].getMedias(new NullProgressMonitor());
        Assert.assertTrue(medias.length > 0);
        Thread.sleep(1000);
    }

    @Test
    public void searchArtistTest03() throws Exception {
        JamendoSearchProvider provider = new JamendoSearchProvider();
        ISearchPattern pattern = new StringSearchPattern("Jamendo:23723", SearchType.ARTIST);
        Assert.assertTrue(provider.canHandle(pattern));
        SearchMatch[] matches = provider.search(pattern, new NullProgressMonitor());
        Assert.assertEquals(0, matches.length);
        Thread.sleep(1000);
    }

    @Test
    public void searchAlbumTest01() throws Exception {
        JamendoSearchProvider provider = new JamendoSearchProvider();
        ISearchPattern pattern = new StringSearchPattern("Combat Circus", SearchType.ALBUM);
        Assert.assertTrue(provider.canHandle(pattern));
        SearchMatch[] matches = provider.search(pattern, new NullProgressMonitor());
        Assert.assertEquals(1, matches.length);
        String label = matches[0].getLabel();
        Assert.assertEquals("Combat Circus - Talco", label);
        MediaInfo[] medias = matches[0].getMedias(new NullProgressMonitor());
        Assert.assertTrue(medias.length > 0);
        Thread.sleep(1000);
    }
}
