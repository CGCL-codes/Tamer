package net.sourceforge.filebot.web;

import static net.sourceforge.filebot.web.WebRequest.*;
import static net.sourceforge.tuned.XPathUtilities.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import org.w3c.dom.Document;
import net.sourceforge.tuned.FileUtilities;

public class SubsceneSubtitleDescriptor implements SubtitleDescriptor {

    private String title;

    private String language;

    private URL subtitlePage;

    private Map<String, String> subtitleInfo;

    public SubsceneSubtitleDescriptor(String title, String language, URL subtitlePage) {
        this.title = title;
        this.language = language;
        this.subtitlePage = subtitlePage;
    }

    @Override
    public String getName() {
        return title;
    }

    @Override
    public String getLanguageName() {
        return language;
    }

    @Override
    public String getType() {
        return null;
    }

    @Override
    public ByteBuffer fetch() throws Exception {
        String subtitlePagePath = FileUtilities.getNameWithoutExtension(subtitlePage.getFile());
        String path = String.format("%s-dlpath-%s/%s.zipx", subtitlePagePath, getSubtitleInfo().get("filmId"), getSubtitleInfo().get("typeId"));
        URL downloadLocator = new URL(subtitlePage.getProtocol(), subtitlePage.getHost(), path);
        Map<String, String> downloadPostData = subtitleInfo;
        HttpURLConnection connection = (HttpURLConnection) downloadLocator.openConnection();
        connection.addRequestProperty("Referer", subtitlePage.toString());
        return WebRequest.post(connection, downloadPostData);
    }

    private synchronized Map<String, String> getSubtitleInfo() {
        if (subtitleInfo == null) {
            try {
                Document dom = getHtmlDocument(subtitlePage);
                subtitleInfo = new HashMap<String, String>();
                subtitleInfo.put("subtitleId", selectString("//INPUT[@name='subtitleId']/@value", dom));
                subtitleInfo.put("typeId", selectString("//INPUT[@name='typeId']/@value", dom));
                subtitleInfo.put("filmId", selectString("//INPUT[@name='filmId']/@value", dom));
            } catch (Exception e) {
                throw new RuntimeException("Failed to extract subtitle info", e);
            }
        }
        return subtitleInfo;
    }

    @Override
    public String getPath() {
        return String.format("%s.%s", getName(), subtitleInfo == null ? null : subtitleInfo.get("typeId"));
    }

    @Override
    public long getLength() {
        return -1;
    }

    @Override
    public int hashCode() {
        return subtitlePage.getPath().hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof SubsceneSubtitleDescriptor) {
            SubsceneSubtitleDescriptor other = (SubsceneSubtitleDescriptor) object;
            return subtitlePage.getPath().equals(other.getPath());
        }
        return false;
    }

    @Override
    public String toString() {
        return String.format("%s [%s]", getName(), getLanguageName());
    }
}
