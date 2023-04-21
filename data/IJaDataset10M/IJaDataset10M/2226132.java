package com.phbc.lyricsMystro.dao;

import com.phbc.lyricsMystro.model.SongLyrics;
import com.phbc.lyricsMystro.model.mockdata.DataFactory;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author jkruger
 */
public class SongLyricsMockDao implements SongLyricsDao {

    /** Creates a new instance of SongLyricsMockDao */
    SongLyricsMockDao() {
    }

    public Map<String, SongLyrics> getAllLyrics() {
        List<SongLyrics> songs = DataFactory.createSongs(40);
        Map lyricsMap = new HashMap<String, SongLyrics>(songs.size());
        for (SongLyrics song : songs) {
            lyricsMap.put(song.toString(), song);
        }
        return lyricsMap;
    }

    public void storeSong(SongLyrics song) {
    }

    public void deleteSong(SongLyrics song) {
    }

    public void deleteSong(String title) {
    }
}
