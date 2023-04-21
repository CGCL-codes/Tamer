package tardistv.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import tardistv.model.Episode;
import tardistv.utilities.Network;

/**
 * @author Tardis TV Team
 */
public class Mininova {

    /**
     * Searchs for the torrent with more seeds.
     * @param episode The episode to look for
     * @return True if the torrent was found, false otherwise.
     */
    public boolean SearchTorrent(Episode episode) {
        try {
            String code = Network.HTMLSource("http://www.mininova.org/search/" + episode.getName() + "%2B" + episode.getNumber() + "/seeds");
            String[] chunks = code.split("Leechers</a></th></tr>")[1].split("</table>")[0].split("<tr>");
            for (String line : chunks) if (line.toLowerCase().contains(episode.getNumber().toLowerCase()) && line.toLowerCase().contains(episode.getName().toLowerCase())) {
                episode.setTorrent("http://www.mininova.org/get/" + line.split("href=\"/get/")[1].split("\"")[0]);
                return true;
            }
        } catch (MalformedURLException ex) {
            Logger.getLogger(Mininova.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Mininova.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
}
