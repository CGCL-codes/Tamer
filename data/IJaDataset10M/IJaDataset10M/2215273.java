package translator;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */
public class contentRdfWriter {

    private String localeVersion;

    private String authorName;

    private String localeName;

    private String previewURL;

    private String display;

    public contentRdfWriter() {
        mttConfigurationProperties propsFile = new mttConfigurationProperties();
        authorName = propsFile.getProperty("author");
        localeName = propsFile.getProperty("localeName");
        previewURL = propsFile.getProperty("previewURL");
        localeVersion = propsFile.getProperty("version");
        display = propsFile.getProperty("displayName");
    }

    public String getContentRdfString(TranslationFile currentFile) {
        String rdfString = "";
        rdfString = "<?xml version=\"1.0\"?>";
        rdfString = rdfString + "\n" + "<RDF:RDF xmlns:RDF=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\"";
        rdfString = rdfString + "\n" + "         xmlns:chrome=\"http://www.mozilla.org/rdf/chrome#\">";
        rdfString = rdfString + "\n";
        rdfString = rdfString + "\n" + "  <!-- list all the skins being supplied by this package -->";
        rdfString = rdfString + "\n" + "  <RDF:Seq about=\"urn:mozilla:locale:root\">";
        rdfString = rdfString + "\n" + "    <RDF:li resource=\"urn:mozilla:locale:" + localeName + "\"/>";
        rdfString = rdfString + "\n" + "  </RDF:Seq>";
        rdfString = rdfString + "\n";
        rdfString = rdfString + "\n" + "  <!-- locale information -->";
        rdfString = rdfString + "\n" + "<RDF:Description about=\"urn:mozilla:locale:" + localeName + "\"";
        rdfString = rdfString + "\n" + "      chrome:displayName=\"" + display + "\"";
        rdfString = rdfString + "\n" + "       chrome:author=\"" + authorName + "\"";
        rdfString = rdfString + "\n" + "       chrome:name=\"" + localeName + "\"";
        rdfString = rdfString + "\n" + "       chrome:previewURL=\"" + previewURL + "\">";
        rdfString = rdfString + "\n" + "   <chrome:packages>";
        rdfString = rdfString + "\n" + "      <RDF:Seq about=\"urn:mozilla:locale:" + localeName + ":packages\">";
        rdfString = rdfString + "\n" + "        <RDF:li resource=\"urn:mozilla:locale:" + localeName + ":" + currentFile.getComponentPath() + "\"/>";
        rdfString = rdfString + "\n" + "      </RDF:Seq>";
        rdfString = rdfString + "\n" + "    </chrome:packages>";
        rdfString = rdfString + "\n" + "  </RDF:Description>";
        rdfString = rdfString + "\n" + "  <RDF:Description about=\"urn:mozilla:locale:" + localeName + ":" + currentFile.getComponentPath() + "\"";
        rdfString = rdfString + "\n" + "       chrome:localeVersion=\"" + localeVersion + "\"/>";
        rdfString = rdfString + "\n" + "</RDF:RDF>";
        return rdfString;
    }
}
