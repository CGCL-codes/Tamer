package core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import util.FileUtils;

/**
 * A music library located on the local file system.
 * 
 * @author Jason
 * 
 */
public class LocalMusicLibrary extends AbstractMusicLibrary {

    private final HashMap<Song, File> songHash = new HashMap<Song, File>();

    public void load(File libraryDataFile) {
        if (libraryDataFile.isFile()) loadSongsFromXML(libraryDataFile); else if (libraryDataFile.isDirectory()) {
            loadSongsFromFileSystem(libraryDataFile);
        }
    }

    private void loadSongsFromFileSystem(File libraryDataFile) {
        loadRecursive(libraryDataFile);
    }

    private void loadRecursive(File folder) {
        for (File file : folder.listFiles()) {
            if (file.isDirectory()) {
                loadRecursive(file);
            } else process(file);
        }
    }

    private void process(File file) {
        String name = file.getName();
        if (FileUtils.isSound(file)) {
            Song song = FileUtils.readTag(file, this);
            if (song == null) {
                song = new Song(this);
                song.setTitle(name);
            }
            songHash.put(song, file);
            super.addSong(song);
        }
    }

    protected void loadSongsFromXML(File libraryDataFile) {
    }

    @Override
    public InputStream getDataFor(Song song) {
        File file = songHash.get(song);
        try {
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
