package config;

import org.dom4j.DocumentException;
import xmlRW.ReadXml;

public class HtmlUrls {

    public String HtmlUrlsPath;

    public HtmlUrls() {
        ConfigFilePath cfp = new ConfigFilePath();
        HtmlUrlsPath = cfp.getHtmlUrlsPath();
    }

    @SuppressWarnings("static-access")
    public String getArtistsUrls() {
        String res = "";
        try {
            ReadXml rxm = new ReadXml(HtmlUrlsPath);
            res = rxm.getAttributes("/HtmlUrls/ArtistUrls");
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return res;
    }

    @SuppressWarnings("static-access")
    public String getPeopleUrls() {
        String res = "";
        try {
            ReadXml rxm = new ReadXml(HtmlUrlsPath);
            res = rxm.getAttributes("/HtmlUrls/PeopleUrls");
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return res;
    }
}
