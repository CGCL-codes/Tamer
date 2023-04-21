package net.sf.jmp3renamer.plugins.MusicBrainz;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import net.sf.jmp3renamer.plugins.MusicBrainz.ws2.Artist;
import net.sf.jmp3renamer.plugins.MusicBrainz.ws2.Metadata;
import net.sf.jmp3renamer.plugins.MusicBrainz.ws2.Release;
import net.sf.jmp3renamer.plugins.MusicBrainz.ws2.ReleaseList;
import net.sf.jmp3renamer.plugins.Web.provider.Album;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AlbumGetter {

    private static transient Logger logger = LoggerFactory.getLogger(AlbumGetter.class);

    private String charset = MusicBrainz.getProperty("service.charset");

    private Unmarshaller unm;

    public AlbumGetter(Unmarshaller unmarshaller) throws JAXBException {
        this.unm = unmarshaller;
    }

    public List<Album> findAlbum(String artist, String album) throws Exception {
        List<Album> resultList = new ArrayList<Album>();
        String serviceUri = MusicBrainz.getProperty("service.uri");
        String query = URLEncoder.encode(artist + " " + album, charset);
        String request = serviceUri + "release?query=" + query;
        logger.debug("Request: {}", request);
        HttpURLConnection con = (HttpURLConnection) new URL(request).openConnection();
        con.setRequestProperty("User-Agent", "JMP3renamer MusicBrainz Plug-in " + MusicBrainz.VERSION);
        InputStream in = con.getInputStream();
        Object o = unm.unmarshal(in);
        Metadata md = (Metadata) o;
        ReleaseList rl = md.getReleaseList();
        if (rl != null) {
            List<Release> list = rl.getRelease();
            for (Release release : list) {
                MusicBrainzAlbum mba = new MusicBrainzAlbum();
                mba.setTitle(release.getTitle());
                Artist artistName = release.getArtistCredit().getNameCredit().get(0).getArtist();
                mba.setArtist(artistName.getName());
                mba.setId(release.getId());
                if ("Compilation".equalsIgnoreCase(release.getReleaseGroup().getType())) {
                    mba.setCompilation(true);
                }
                String releaseDate = release.getDate();
                if (releaseDate != null) {
                    mba.setYear(releaseDate.substring(0, 4));
                }
                resultList.add(mba);
                logger.debug("Found release: {} - {}", mba.getArtist(), mba.getTitle());
            }
        }
        Collections.sort(resultList);
        return resultList;
    }
}
