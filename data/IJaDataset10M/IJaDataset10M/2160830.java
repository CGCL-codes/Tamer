package amplitude.persistence.hibernate;

import java.io.File;
import java.io.RandomAccessFile;
import java.sql.SQLException;
import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import amplitude.persistence.hibernate.dao.AlbumDAO;
import amplitude.persistence.hibernate.dao.ArtistDAO;
import amplitude.persistence.hibernate.dao.GenreDAO;
import amplitude.persistence.hibernate.dao.SongDAO;
import amplitude.persistence.hibernate.dao._RootDAO;
import de.ueberdosis.mp3info.ID3Reader;
import de.ueberdosis.mp3info.ID3Tag;

public class DBLoader {

    ArtistDAO artistDAO = new ArtistDAO();

    AlbumDAO albumDAO = new AlbumDAO();

    SongDAO songDAO = new SongDAO();

    GenreDAO genreDAO = new GenreDAO();

    private boolean songExists(String path) throws SQLException, ClassNotFoundException {
        String queryString = "from Song where URL = ?";
        Query query = songDAO.getQuery(queryString);
        query.setString(0, path);
        return query.list().size() > 0;
    }

    Artist artist;

    Album album;

    Song song;

    public void addFromDirectory(File path) throws Exception {
        Logger logger = Logger.getLogger("Amplitude");
        logger.debug("Loading files from " + path.getAbsolutePath());
        addFiles(path);
    }

    private void addFiles(File path) throws Exception {
        Logger logger = Logger.getLogger("Amplitude");
        DBUtils utils = new DBUtils();
        if (!utils.databaseExists()) {
            utils.createDatabase();
        }
        File[] files = path.listFiles();
        if (files == null) {
            return;
        }
        for (int index = 0; index < files.length; index++) {
            File next = files[index];
            if (next.isDirectory()) {
                addFromDirectory(next);
            } else {
                if (songExists(next.getCanonicalPath())) continue;
                RandomAccessFile f = null;
                try {
                    f = new RandomAccessFile(next, "r");
                    ID3Tag tag = ID3Reader.readTag(f);
                    logger.debug("Adding " + next.getAbsolutePath());
                    artist = findOrCreateArtistByName(tag.getArtist());
                    album = findOrCreateAlbum(artist, tag.getAlbum());
                    song = createSong(artist, album, tag, next);
                    artistDAO.saveOrUpdate(artist);
                    albumDAO.saveOrUpdate(album);
                    songDAO.saveOrUpdate(song);
                } finally {
                    if (f != null) {
                        f.close();
                    }
                }
            }
        }
    }

    Artist findOrCreateArtistByName(String name) throws Exception {
        if (artist != null && artist.getArtistName().equals(name)) return artist;
        Query q = artistDAO.getQuery("from Artist where artistName = ?");
        q.setString(0, name);
        List<Artist> artists = q.list();
        if (artists.size() > 0) return artists.get(0);
        artist = new Artist();
        artist.setArtistName(name);
        return artist;
    }

    Album findOrCreateAlbum(Artist artist, String albumName) throws Exception {
        if (album != null && album.getAlbumName().equals(albumName) && album.getArtist().getArtistName().equals(artist.getArtistName())) return album;
        Query q = albumDAO.getQuery("from Album where albumName = ?");
        q.setString(0, albumName);
        List<Album> albums = q.list();
        if (albums.size() > 0) return albums.get(0);
        album = new Album();
        album.setArtist(artist);
        album.setAlbumName(albumName);
        return album;
    }

    Song createSong(Artist artist, Album album, ID3Tag tag, File songFile) throws Exception {
        Song song = new Song();
        song.setAlbum(album);
        song.setFile(songFile);
        song.setGenre(getOrCreateGenre(tag.getGenreS()));
        song.setLength("0");
        song.setSongName(tag.getTitle());
        song.setTrackNumber(tag.getTrack());
        return song;
    }

    Genre getOrCreateGenre(String genreName) {
        Query q = genreDAO.getQuery("from Genre where genreName = ?");
        q.setString(0, genreName);
        List<Genre> genres = q.list();
        if (genres.size() > 0) return genres.get(0);
        Genre genre = new Genre();
        genre.setGenreName(genreName);
        genreDAO.save(genre);
        return genre;
    }
}
