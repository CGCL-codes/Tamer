package espider.player.playlist;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

/**
 * @author Vincent
 *
 */
public class PlaylistM3U extends Playlist {

    /**
	 * Comment for <code>serialVersionUID</code>
	 */
    private static final long serialVersionUID = 1L;

    public PlaylistM3U(String pathPlaylist) throws IOException {
        playlist = new File(pathPlaylist);
        if (!playlist.exists()) playlist.createNewFile();
        load();
    }

    public void load() {
        BufferedReader br = null;
        PlaylistItem pli = null;
        try {
            br = new BufferedReader(new FileReader(playlist));
            String line = null;
            String songFile = null;
            String artist = null;
            String title = null;
            String duration = null;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("#EXTINF:")) {
                    int indA = line.indexOf(",", 0);
                    if (indA != -1) {
                        title = line.substring(indA + 1, line.indexOf("-", 0) - 1);
                        artist = line.substring(line.indexOf("-", 0) + 2, line.length());
                    }
                    int indB = line.indexOf(":", 0);
                    if (indB != -1) {
                        if (indB < indA) {
                            duration = line.substring(indB + 1, indA).trim();
                        }
                        ;
                    }
                    line = br.readLine();
                    pli = new PlaylistItem(artist, title, duration, line);
                    this.add(pli);
                } else {
                    songFile = line;
                    if (!songFile.startsWith("http:")) {
                        File f = new File(songFile);
                        if (f.exists()) {
                            pli = new PlaylistItem(songFile);
                            this.add(pli);
                        } else if ((new File(playlist.getParent() + "\\" + songFile).exists())) {
                            pli = new PlaylistItem(playlist.getParent() + "\\" + songFile);
                            this.add(pli);
                        }
                    } else {
                        System.out.println("Playlist load radio : " + songFile);
                        pli = new PlaylistItem(songFile);
                        this.add(pli);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Can't load .m3u playlist " + e);
        } finally {
            try {
                this.trimToSize();
                if (br != null) {
                    br.close();
                }
            } catch (Exception ioe) {
                System.out.println("Can't close .m3u playlist");
            }
        }
    }

    public void save() {
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(playlist));
            bw.write("#EXTM3U");
            bw.newLine();
            Iterator<PlaylistItem> it = this.iterator();
            while (it.hasNext()) {
                PlaylistItem pli = it.next();
                bw.write("#EXTINF:" + pli.getM3UExtInf());
                bw.newLine();
                bw.write(pli.getLocation());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Can't save playlist");
        } finally {
            try {
                if (bw != null) {
                    bw.close();
                }
            } catch (IOException ioe) {
                System.out.println("Can't close playlist");
            }
        }
    }
}
