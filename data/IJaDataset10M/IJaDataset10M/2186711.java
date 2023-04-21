package jshm.internal.xml;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.netbeans.spi.wizard.ResultProgressHandle;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import jshm.Game;
import jshm.GameSeries;
import jshm.GameTitle;
import jshm.Platform;
import jshm.SongOrder;
import jshm.Tiers;
import jshm.internal.ConsoleProgressHandle;
import jshm.rb.RbGame;
import jshm.rb.RbGameTitle;
import jshm.rb.RbSong;
import jshm.sh.scraper.RbSongScraper;
import jshm.util.IsoDateParser;
import jshm.util.PhpUtil;

public class RbSongDataGenerator {

    public static final String DTD_URL = "http://jshm.sourceforge.net/songdata/rb_songdata.dtd";

    private static void usage() {
        List<GameTitle> titles = GameTitle.getBySeries(GameSeries.ROCKBAND);
        System.out.printf("Usage: java %s <%s>\n", RbSongDataGenerator.class.getName(), PhpUtil.implode("|", titles));
        System.exit(-1);
    }

    static ResultProgressHandle progress = ConsoleProgressHandle.getInstance();

    public static void main(String[] args) throws Exception {
        if (args.length != 1) usage();
        final String ttlString = args[0];
        GameTitle ttl = GameTitle.valueOf(ttlString);
        if (!(ttl instanceof RbGameTitle)) usage();
        jshm.util.TestTimer.start();
        Map<Integer, RbSong> songMap = new HashMap<Integer, RbSong>();
        List<SongOrder> allOrders = new ArrayList<SongOrder>();
        Map<String, String> tierMap = new HashMap<String, String>();
        for (Game g : Game.getByTitle(ttl)) {
            progress.setBusy("Downloading song list for " + g);
            List<RbSong> songs = RbSongScraper.scrape((RbGame) g);
            assert null != RbSongScraper.lastScrapedTiers;
            tierMap.put(g.toString(), new Tiers(RbSongScraper.lastScrapedTiers).getPacked());
            progress.setBusy(String.format("Processing %s songs", songs.size()));
            for (RbSong s : songs) {
                if (songMap.containsKey(s.getScoreHeroId())) {
                    songMap.get(s.getScoreHeroId()).addPlatform(g.platform);
                } else {
                    songMap.put(s.getScoreHeroId(), s);
                }
            }
            progress.setBusy("Downloading song order lists for " + g);
            List<SongOrder> orders = RbSongScraper.scrapeOrders(progress, (RbGame) g, songMap);
            progress.setBusy(String.format("Processing %s song orders", orders.size()));
            allOrders.addAll(orders);
        }
        progress.setBusy("Creating XML file");
        createXml(ttl, songMap, allOrders, tierMap);
        jshm.util.TestTimer.stop();
        progress.setBusy("All done");
    }

    private static void createXml(GameTitle ttl, Map<Integer, RbSong> songMap, List<SongOrder> orders, Map<String, String> tierMap) {
        Document xml = null;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            xml = builder.newDocument();
            Element root = CE(xml, "songData");
            AC(xml, root);
            Element tmp = CE(xml, "gameTitle");
            tmp.setAttribute("id", ttl.toString());
            AC(root, tmp);
            _(xml, root, "date", IsoDateParser.getIsoDate(new Date()));
            Element tiers = CE(xml, "tiers");
            AC(root, tiers);
            for (String key : tierMap.keySet()) {
                Element el = CE(xml, "tier");
                AC(tiers, el);
                el.setAttribute("game", key);
                el.setAttribute("packed", tierMap.get(key));
            }
            Element songs = CE(xml, "songs");
            AC(root, songs);
            for (Integer key : songMap.keySet()) {
                RbSong s = songMap.get(key);
                Element el = CE(xml, "song");
                AC(songs, el);
                el.setAttribute("id", String.valueOf(s.getScoreHeroId()));
                el.setAttribute("title", s.getTitle());
                Element platforms = CE(xml, "platforms");
                AC(el, platforms);
                for (Platform p : s.getPlatforms()) {
                    tmp = CE(xml, "platform");
                    tmp.setAttribute("id", p.name());
                    AC(platforms, tmp);
                }
            }
            Element ordersEl = CE(xml, "songOrders");
            AC(root, ordersEl);
            for (SongOrder o : orders) {
                Element el = CE(xml, "songOrder");
                AC(ordersEl, el);
                el.setAttribute("group", o.getGroup().name());
                el.setAttribute("platform", o.getPlatform().name());
                el.setAttribute("song", String.valueOf(o.getSong().getScoreHeroId()));
                el.setAttribute("tier", String.valueOf(o.getTier()));
                el.setAttribute("order", String.valueOf(o.getOrder()));
            }
            xml.setXmlStandalone(true);
            xml.getDocumentElement().normalize();
            FileOutputStream out = new FileOutputStream(new File(ttl.title + ".xml"));
            DOMSource domSource = new DOMSource(xml);
            StreamResult streamResult = new StreamResult(out);
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer serializer = tf.newTransformer();
            serializer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, DTD_URL);
            serializer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            serializer.setOutputProperty(OutputKeys.INDENT, "yes");
            serializer.transform(domSource, streamResult);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-2);
        }
    }

    /**
	 * One liner to make a new tag with a text node and append
	 * it to parent when you won't need to modify it later.
	 */
    private static void _(Document xml, Element parent, String tag, Object contents) {
        Element tmp = CE(xml, tag);
        AC(tmp, CTN(xml, contents.toString()));
        AC(parent, tmp);
    }

    /**
	 * createElement shortcut
	 */
    private static Element CE(Document xml, String name) {
        return xml.createElement(name);
    }

    /**
	 * createTextNode shortcut
	 * @param xml
	 * @param contents
	 * @return
	 */
    private static Text CTN(Document xml, String contents) {
        return xml.createTextNode(contents);
    }

    /**
	 * appendChild shortcut
	 * @param parent
	 * @param child
	 */
    private static void AC(Node parent, Node child) {
        parent.appendChild(child);
    }
}
