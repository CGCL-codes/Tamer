package org.iqual.chaplin.intro.lesson7;

import static org.iqual.chaplin.DynaCastUtils.$;
import java.io.File;

/**
 * @author Zbynek Slajchrt
 * @since Jul 19, 2009 8:13:39 PM
 */
public class LocalSongWaveDataLookupService implements SongLookupService {

    public abstract static class SongWaveData implements Song {

        private byte[] data;

        public void setData(byte[] data) {
            this.data = data;
        }

        public byte[] getWaveData() {
            return data;
        }
    }

    public Song findSong(String songId) throws SongNotFoundException {
        byte[] data = loadDataFromLocalPath("songs/" + songId + ".wav");
        SongWaveData waveData = $();
        waveData.setData(data);
        return waveData;
    }

    private byte[] loadDataFromLocalPath(String songPath) throws SongNotFoundException {
        File file = new File(songPath);
        if (!file.exists()) {
            throw new SongNotFoundException(songPath);
        }
        return new byte[0];
    }
}
