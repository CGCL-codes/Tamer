package audictiv.server.services.search;

import java.sql.SQLException;
import java.util.ArrayList;
import audictiv.client.services.search.SearchService;
import audictiv.server.model.SqlAdapter;
import audictiv.shared.Album;
import audictiv.shared.Artist;
import audictiv.shared.Band;
import audictiv.shared.Song;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class SearchServiceImpl extends RemoteServiceServlet implements SearchService {

    public ArrayList<Album> getSearchResultAlbum(String keywords) {
        try {
            return SqlAdapter.getInstance().SearchAlbum(keywords);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Artist> getSearchResultArtist(String keywords) {
        try {
            return SqlAdapter.getInstance().SearchArtist(keywords);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<Band> getSearchResultBand(String keywords) {
        try {
            return SqlAdapter.getInstance().SearchBand(keywords);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<Song> getSearchResultSong(String keywords) {
        try {
            return SqlAdapter.getInstance().SearchSong(keywords);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<String> makeSuggestionWords() {
        try {
            return SqlAdapter.getInstance().makeSuggestionWords();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
