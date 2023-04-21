package jwebdownloader.modelo.parsers.video;

import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import jwebdownloader.control.Propiedades;
import jwebdownloader.modelo.parsers.Utilidades;
import org.apache.log4j.Logger;

public class YouTubeParser {

    private URL urlYouTube;

    private String urlVideo;

    private String nombreVideo;

    private static final Logger log = Logger.getLogger("YouTubeParser");

    public YouTubeParser(String urlYouTube) throws Exception {
        try {
            this.urlYouTube = new URL(urlYouTube);
            String responseBody = Utilidades.getResponseBody(urlYouTube);
            getVideo(responseBody);
        } catch (Exception ex) {
            log.error("Error al parsear la página de youtube " + urlYouTube, ex);
            throw new Exception("Error al parsear la página de youtube " + urlYouTube + " " + ex.getMessage());
        }
    }

    private void getVideo(String codigoHTML) {
        Matcher m = Pattern.compile(Propiedades.getPropiedad("patronYoutubeParser")).matcher(codigoHTML);
        if (m.find()) {
            log.debug("Patrón Youtube encuentra coincidencias");
            if (m.groupCount() == 3) {
                urlVideo = urlYouTube.getProtocol() + "://" + urlYouTube.getHost() + "/get_video?video_id=" + m.group(1) + "&t=" + m.group(2);
                nombreVideo = m.group(3).trim();
            } else if (m.groupCount() == 2) {
                urlVideo = Utilidades.decodificaURI(m.group(1).replaceAll("%252C", "%2C"));
                nombreVideo = m.group(2).trim();
            }
            if (nombreVideo.isEmpty()) {
                nombreVideo = "YouTube";
            }
            nombreVideo += ".flv";
        }
    }

    public String getNombreVideo() {
        return nombreVideo;
    }

    public String getUrlVideo() {
        return urlVideo;
    }
}
