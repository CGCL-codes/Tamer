package clubmixer.library.persistence.dao;

import java.util.List;
import clubmixer.library.persistence.entities.Song2;
import org.hibernate.Query;
import org.hibernate.Transaction;

/**
 *
 * @author Alexander Schindler
 */
public class SongDAO extends AbstractGenericDAO<Song2> implements ISongDAO {

    public List<Song2> filteredSearch(Song2 song) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<Song2> search(String artist, String title, boolean inFile, boolean inDir, boolean searchNewSongs) {
        List<Song2> result = null;
        Transaction tx = getSession().beginTransaction();
        Query q;
        if (!inFile && !inDir && !searchNewSongs) {
            q = getSession().getNamedQuery("song.by.artist.title");
            q.setParameter("artist", "%" + artist + "%");
            q.setParameter("title", "%" + title + "%");
        } else {
            String namedQuery = null;
            if (inFile && !inDir && !searchNewSongs) {
                namedQuery = "song.by.artist.title.filename";
            } else if (!inFile && inDir && !searchNewSongs) {
                namedQuery = "song.by.artist.title.directory";
            } else if (inFile && inDir && !searchNewSongs) {
                namedQuery = "song.by.artist.title.filename.directory";
            }
            q = getSession().getNamedQuery(namedQuery);
            if (artist.isEmpty()) {
                artist = title;
            }
            q.setParameter("arg", "%" + artist + "%");
        }
        result = q.list();
        return result;
    }

    public List<Song2> getByArtist(String artistName) {
        return getByNamedQuery("song.byArtist", "artist", artistName);
    }
}
