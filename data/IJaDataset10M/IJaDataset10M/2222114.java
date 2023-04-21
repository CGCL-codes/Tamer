package net.meblabs.bookimp;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import net.meblabs.commons.progress.Progress;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.io.UrlResource;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class WikiAuthorImporter implements AuthorImporter {

    private class Author implements IAuthor {

        private String fname;

        private String lname;

        public Author(String fname, String lname) {
            this.fname = fname;
            this.lname = lname;
        }

        @Override
        public String getFname() {
            return fname;
        }

        @Override
        public String getLname() {
            return lname;
        }
    }

    private Progress progress;

    @Override
    public String getId() {
        return WikiAuthorImporter.class.getCanonicalName();
    }

    @Override
    public String getDescription() {
        return "Wikipedia importer";
    }

    @Override
    public Collection<IAuthor> doImport() throws Exception {
        progress.initialize(2, "Ściągam autorów amerykańskich");
        String url = "http://pl.wikipedia.org/wiki/Kategoria:Ameryka%C5%84scy_autorzy_fantastyki";
        UrlResource resource = new UrlResource(url);
        InputStream urlInputStream = resource.getInputStream();
        StringWriter writer = new StringWriter();
        IOUtils.copy(urlInputStream, writer);
        progress.advance("Parsuję autorów amerykańskich");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        String httpDoc = writer.toString();
        httpDoc = httpDoc.replaceFirst("(?s)<!DOCTYPE.+?>\\n", "");
        httpDoc = httpDoc.replaceAll("(?s)<script.+?</script>", "");
        httpDoc = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\" ?>\n" + httpDoc;
        ByteArrayInputStream byteInputStream = new ByteArrayInputStream(httpDoc.getBytes("UTF-8"));
        Document doc = builder.parse(byteInputStream);
        ArrayList<String> authorNames = new ArrayList<String>();
        ArrayList<IAuthor> authors = new ArrayList<IAuthor>();
        XPathFactory xpathFactory = XPathFactory.newInstance();
        XPath xpath = xpathFactory.newXPath();
        NodeList list = (NodeList) xpath.evaluate("//ul/li/div/div/a", doc, XPathConstants.NODESET);
        for (int i = 0; i < list.getLength(); i++) {
            String name = list.item(i).getTextContent();
            if (StringUtils.isNotBlank(name)) {
                authorNames.add(name);
            }
        }
        list = (NodeList) xpath.evaluate("//td/ul/li/a", doc, XPathConstants.NODESET);
        for (int i = 0; i < list.getLength(); i++) {
            String name = list.item(i).getTextContent();
            if (StringUtils.isNotBlank(name)) {
                authorNames.add(name);
            }
        }
        for (String name : authorNames) {
            int idx = name.lastIndexOf(' ');
            String fname = name.substring(0, idx).trim();
            String lname = name.substring(idx + 1).trim();
            authors.add(new Author(fname, lname));
        }
        progress.advance("Wykonano");
        return authors;
    }

    @Override
    public boolean isSupportsProgress() {
        return true;
    }

    @Override
    public void setProgress(Progress progress) {
        this.progress = progress;
    }
}
