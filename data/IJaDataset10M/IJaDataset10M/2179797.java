package importer.services.lyrics;

import importer.models.common.Track;
import importer.models.lyricswiki.LyricsResult;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class LyricsWikiLyricsProvider extends LyricsProvider {

    private static JAXBContext jaxbContext;

    private static Unmarshaller umarshaller;

    {
        try {
            jaxbContext = JAXBContext.newInstance("importer.models.lyricswiki");
            umarshaller = jaxbContext.createUnmarshaller();
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    private URL makeUrl(String artistName, String trackName) throws MalformedURLException {
        return new URL("http://lyricwiki.org/api.php?artist=" + artistName + "&song=" + trackName + "&fmt=xml");
    }

    @Override
    public List<LyricsResult> getLyricsFor(Track track) {
        try {
            URL url = makeUrl(track.getArtist(), track.getTrackName());
            LyricsResult result = (LyricsResult) umarshaller.unmarshal(url);
            if ("Not found".equals(result.getLyrics())) return null;
            return Arrays.asList(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
