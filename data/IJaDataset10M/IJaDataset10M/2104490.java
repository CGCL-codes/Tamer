package clubmixer.commons.plugins.importer;

import com.slychief.clubmixer.server.library.entities.Song;
import java.util.List;

/**
 *
 * @author Alexander Schindler
 */
public interface IImporter {

    List<Song> getUnimportedSongs();

    void importSong(Song song);

    void postImportProcedure();
}
