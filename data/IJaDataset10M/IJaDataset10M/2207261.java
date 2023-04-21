package libVideoKrowdix;

import java.io.File;
import java.util.Vector;
import javax.media.MediaLocator;
import javax.swing.ImageIcon;

class Video {

    protected static void crearVideoQT(String dirJpeg, String prefijoJpeg, String nombreVideo, int frameRate) throws Exception {
        if (!nombreVideo.endsWith(".MOV") && !nombreVideo.endsWith(".mov")) nombreVideo += ".mov";
        if (frameRate < 1) frameRate = 1;
        MediaLocator oml = null;
        if ((oml = createMediaLocator(dirJpeg + File.separator + nombreVideo)) == null) {
            throw new Exception("No es posible crear el MediaLocator de la salida para " + nombreVideo);
        }
        File[] f = new File(dirJpeg).listFiles();
        Vector<String> nombresjpeg = new Vector<String>();
        int n = 1;
        for (File ff : f) if (ff.getName().startsWith(prefijoJpeg) && ff.getName().endsWith(".jpg")) {
            nombresjpeg.add(dirJpeg + File.separator + prefijoJpeg + String.format("%05d", n) + ".jpg");
            n++;
        }
        ImageIcon ic = new ImageIcon(nombresjpeg.get(0));
        int w = ic.getImage().getWidth(null), h = ic.getImage().getHeight(null);
        JpegToQTMovie imageToMovie = new JpegToQTMovie();
        imageToMovie.doIt(w, h, frameRate, nombresjpeg, oml);
    }

    private static MediaLocator createMediaLocator(String url) {
        MediaLocator ml;
        if (url.indexOf(":") > 0 && (ml = new MediaLocator(url)) != null) return ml;
        if (url.startsWith(File.separator)) {
            if ((ml = new MediaLocator("file:" + url)) != null) return ml;
        } else {
            String file = "file:" + System.getProperty("user.dir") + File.separator + url;
            if ((ml = new MediaLocator(file)) != null) return ml;
        }
        return null;
    }

    protected static void convertirQTAvi() {
    }
}
