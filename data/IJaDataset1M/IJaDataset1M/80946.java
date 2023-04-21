package it.f2.juboxplayer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class BeanSongs {

    private List songList = new ArrayList();

    public List getSongList() {
        return songList;
    }

    public Song getSongFromCod(int cod) {
        Iterator i = songList.iterator();
        while (i.hasNext()) {
            Song s = (Song) i.next();
            if (s.getCod() == cod) return s;
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public void loadSongList() {
        Document doc = EnvProperties.getDomSongs();
        NodeList songs = doc.getElementsByTagName("songs");
        for (int i = 0; i < songs.getLength(); i++) {
            if (songs.item(i) instanceof Element) {
                Element songeEle = (Element) songs.item(i);
                NodeList s = songeEle.getElementsByTagName("s");
                for (int z = 0; z < s.getLength(); z++) {
                    Element sEle = (Element) s.item(z);
                    Song song = new Song();
                    song.setCod(Integer.parseInt(sEle.getAttribute("cod")));
                    song.setName(sEle.getAttribute("name"));
                    song.setPath(sEle.getAttribute("path"));
                    getSongList().add(song);
                }
            }
        }
    }

    public void print() {
        System.out.println("################### BeanSongs #######################");
        Iterator i = getSongList().iterator();
        while (i.hasNext()) {
            Song s = (Song) i.next();
            System.out.print("Cod<");
            System.out.print(s.getCod());
            System.out.print("> Name<");
            System.out.print(s.getName());
            System.out.print("> Path<");
            System.out.print(s.getPath());
            System.out.println(">");
        }
        System.out.println("################# End BeanSongs #####################");
    }
}
