package espider.player.playlist;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author Vincent
 *
 */
public class PlaylistSpider extends Playlist {

    /**
	 * Comment for <code>serialVersionUID</code>
	 */
    private static final long serialVersionUID = 1L;

    public PlaylistSpider(String pathPlaylist) throws IOException {
        playlist = new File(pathPlaylist);
        if (!playlist.exists()) playlist.createNewFile();
        load();
    }

    public void load() {
    }

    public void save() {
        try {
            DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder domBuilder = domFactory.newDocumentBuilder();
            Document doc = domBuilder.newDocument();
            Element playlist = doc.createElement("playlist");
            Iterator<PlaylistItem> it = this.iterator();
            while (it.hasNext()) {
                PlaylistItem pli = it.next();
                Element file = doc.createElement("file");
                file.setAttribute("id", String.valueOf(0));
                Element location = doc.createElement("path");
                location.appendChild(doc.createTextNode(pli.getLocation()));
                file.appendChild(location);
                playlist.appendChild(file);
            }
            doc.appendChild(playlist);
            Transformer t = TransformerFactory.newInstance().newTransformer();
            t.transform(new DOMSource(doc), new StreamResult(new File("playlist.xml")));
        } catch (Exception ex) {
            System.err.println(ex.toString());
            ex.printStackTrace();
        }
    }
}
