package br.com.infomais.web.noticia;

import java.net.URL;
import java.util.List;
import br.com.infomais.bean.Noticia;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

public class RssNoticiaReader extends NoticiaReader {

    public static final String RSS_URL = "http://g1.globo.com/Rss2/0,,AS0-6174,00.xml";

    private List<SyndEntry> listOfEntries;

    private int index = 0;

    @Override
    public void configura() {
        SyndFeedInput input = new SyndFeedInput();
        try {
            SyndFeed feed = input.build(new XmlReader(new URL(RSS_URL)));
            listOfEntries = feed.getEntries();
        } catch (Exception e) {
        }
    }

    @Override
    public Noticia getNextNoticia() {
        Noticia atual = null;
        if (listOfEntries != null) {
            if (index < listOfEntries.size()) {
                SyndEntry atualEntry = listOfEntries.get(index);
                atual = convert(atualEntry);
                index++;
            }
        }
        return atual;
    }

    @Override
    public Noticia getNextRandom() {
        Noticia atual = null;
        if (listOfEntries != null && listOfEntries.size() > 0) {
            int index = (int) (Math.random() * listOfEntries.size());
            if (index > listOfEntries.size()) index = 0;
            atual = convert(listOfEntries.get(index));
        }
        return atual;
    }

    private Noticia convert(SyndEntry entry) {
        Noticia noticia = new Noticia();
        noticia.setDescricao(entry.getDescription().getValue());
        noticia.setLink(entry.getLink());
        noticia.setTitulo(entry.getTitle());
        return noticia;
    }
}
