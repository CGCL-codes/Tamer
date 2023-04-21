package eu.sacrej.bggtool.net;

import java.util.Hashtable;
import com.exploringxml.xml.*;
import eu.sacrej.bggtool.utils.*;

/**
 * This class is used to handle all the data parsing capabilities. Typically this will be between a NetManager and some panel.
 * The static functions expect a string of data (XML probably) returned by the server via the NetManager, and will convert this into a nice Hashtable for convenience.
 * @author bkirman
 *
 */
public final class DataParser {

    private static int FIRST[] = { 1 };

    public static GameList buildSearchResults(String data) {
        GameList results = new GameList();
        Node root;
        try {
            root = new Xparse().parse(data);
        } catch (Exception e) {
            return null;
        }
        Node doc = root.find("boardgames", FIRST);
        Node game = doc.find("boardgame", FIRST);
        if (game == null) {
            return null;
        }
        int i = 1;
        while (game != null) {
            GameItem this_game = new GameItem();
            System.out.println();
            if (((Node) game.find("yearpublished", FIRST)).contents.length() != 0) {
                String yearpub = ((Node) game.find("yearpublished", FIRST)).getCharacters();
                this_game.setYear(yearpub);
            }
            String gamename = ((Node) game.find("name", FIRST)).getCharacters().replaceAll("&#039;", "'");
            this_game.setName(gamename);
            this_game.setId((String) game.attributes.get("objectid"));
            results.addGame(this_game, GameList.NO_SORT);
            i++;
            int o[] = { i };
            game = doc.find("boardgame", o);
        }
        return results;
    }

    public static GameList buildCollection(String data, boolean sort_priority) {
        GameList results = new GameList();
        Node root;
        try {
            root = new Xparse().parse(data);
        } catch (Exception e) {
            results.appendGame(new GameItem("data:" + data, ""));
            return results;
        }
        Node doc = root.find("items", FIRST);
        int count = (Integer.parseInt((String) doc.attributes.get("totalitems")));
        if (count == 0) return null;
        for (int i = 1; i <= count; i++) {
            GameItem this_game = new GameItem();
            int o[] = { i };
            Node game = doc.find("item", o);
            try {
                Node status = game.find("status", FIRST);
                if (status.attributes.get("wishlistpriority") != null) this_game.setPriority((String) status.attributes.get("wishlistpriority"));
                Node stats = game.find("stats", FIRST);
                if (stats.attributes.get("rating") != null) this_game.setRating((String) stats.attributes.get("rating"));
            } catch (Exception e) {
            }
            this_game.setId((String) game.attributes.get("objectid"));
            this_game.setName(game.find("name", FIRST).getCharacters());
            if (sort_priority) results.addGame(this_game, GameList.SORT_PRIORITY); else results.addGame(this_game, GameList.SORT_NAME);
        }
        return results;
    }

    public static GameList buildGameDetails(String data) {
        GameItem results = new GameItem();
        Node root;
        try {
            root = new Xparse().parse(data);
        } catch (Exception e) {
            return null;
        }
        Node doc;
        int occur[] = { 1, 1 };
        try {
            doc = root.find("boardgames/boardgame", occur);
        } catch (Exception e) {
            return null;
        }
        results.setId((String) doc.attributes.get("objectid"));
        String yearpub = (String) ((Node) doc.find("yearpublished", FIRST)).getCharacters();
        results.setYear(yearpub);
        String gamename = (String) ((Node) doc.find("name", FIRST)).getCharacters().replaceAll("&#039;", "'");
        results.setName(gamename);
        results.setPlayers(((Node) doc.find("minplayers", FIRST)).getCharacters() + "-" + ((Node) doc.find("maxplayers", FIRST)).getCharacters());
        results.setPlayTime(((Node) doc.find("playingtime", FIRST)).getCharacters());
        results.setDescription(HTMLStripper.strip(((Node) doc.find("description", FIRST)).getCharacters().replaceAll("&#039;", "'")));
        results.setPublisher(((Node) doc.find("boardgamepublisher", FIRST)).getCharacters().replaceAll("&#039;", "'"));
        Node stats = (Node) doc.find("statistics/ratings", occur);
        results.setNumRatings(((Node) stats.find("usersrated", FIRST)).getCharacters());
        try {
            results.setRating(((Node) stats.find("average", FIRST)).getCharacters());
        } catch (Exception e) {
            results.setRating("N/A");
        }
        try {
            results.setNumComments(((Node) stats.find("numcomments", FIRST)).getCharacters());
        } catch (Exception e) {
        }
        String designers = "";
        Node designer = ((Node) doc.find("boardgamedesigner", FIRST));
        if (designer != null) {
            designers = designer.getCharacters().replaceAll("&#039;", "'");
        }
        int i = 1;
        while (designer != null) {
            i++;
            int o[] = { i };
            designer = ((Node) doc.find("boardgamedesigner", o));
            if (designer != null) {
                designers = designers + ", " + designer.getCharacters().replaceAll("&#039;", "'");
            }
        }
        results.setDesigner(designers);
        GameList out = new GameList();
        out.appendGame(results);
        return out;
    }

    public static GameList buildComments(String data) {
        GameList results = new GameList();
        Node root;
        try {
            root = new Xparse().parse(data);
        } catch (Exception e) {
            return null;
        }
        int occur[] = { 1, 1 };
        Node doc = root.find("boardgames/boardgame", occur);
        for (int i = 1; i <= 40; i++) {
            GameItem this_game = new GameItem();
            int o[] = { i };
            try {
                Node comment = doc.find("comment", o);
                this_game.setName((String) comment.attributes.get("username"));
                this_game.setDescription(comment.getCharacters().replaceAll("&#039;", "'"));
            } catch (Exception e) {
                break;
            }
            results.addGame(this_game, GameList.NO_SORT);
        }
        return results;
    }
}
