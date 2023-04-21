package unico;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.limewire.util.FileUtils;
import com.limegroup.bittorrent.BTMetaInfo;
import com.limegroup.gnutella.RouterService;
import com.limegroup.gnutella.SaveLocationException;

public class TorrentDownload extends LimewireDownload {

    public TorrentDownload(AbstractSearchResult searchResult) {
        super(searchResult);
    }

    public void run() {
        System.out.println((((TorrentResult) searchResult).searchLink));
        String url = TorrentzSearch.getTorrentFileLink((((TorrentResult) searchResult).searchLink));
        if (url == null) {
            cannotDownloadTorrent();
        } else {
            System.out.println(url);
            byte[] bytes = FileUtils.readFileFully(downloadTorrentFile(url, searchResult.getFileName().replace(" ", "_") + ".torrent"));
            try {
                description = RouterService.downloadTorrent(BTMetaInfo.readFromBytes(bytes), true);
            } catch (SaveLocationException e) {
                e.printStackTrace();
                System.out.println(e.getFile().toString());
            } catch (IOException e) {
                cannotDownloadTorrent();
                e.printStackTrace();
            }
        }
    }

    /**
	 * Download .torrent file from the given URL string
	 * @return .torrent File
	 */
    public static File downloadTorrentFile(String url, String save) {
        try {
            HttpClient client = new HttpClient();
            HttpMethod method = new GetMethod(url);
            method.setFollowRedirects(true);
            client.executeMethod(method);
            InputStream is = method.getResponseBodyAsStream();
            File file = new File(save);
            FileOutputStream out = new FileOutputStream(file);
            int b;
            while ((b = is.read()) != -1) out.write(b);
            return file;
        } catch (HttpException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void cannotDownloadTorrent() {
        javax.swing.JOptionPane.showMessageDialog(Unico.mainFrame, "I'm sorry, I cannot download this torrent, try with another.", "Error", javax.swing.JOptionPane.WARNING_MESSAGE);
    }
}
