package gov.lanl.resource.openurl.filesystem;

import java.sql.Date;
import gov.lanl.util.DateUtil;
import gov.lanl.util.xml.XmlUtil;

public class OpenURLaDORe9OREBuilder {

    private String baseUrl;

    private StringBuffer aggregation;

    private StringBuffer resourceDescriptions;

    private String creationDate;

    public static final String HEADER = "<rdf:RDF xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\"" + " xmlns:ore=\"http://www.openarchives.org/ore/terms/\"" + " xmlns:dcterms=\"http://purl.org/dc/terms/\"" + " xmlns:dc=\"http://purl.org/dc/elements/1.1/\">";

    public static final String FOOTER = "\n</rdf:Description>";

    public OpenURLaDORe9OREBuilder(String baseUrl) {
        this.baseUrl = baseUrl;
        this.creationDate = DateUtil.date2UTC(new Date(System.currentTimeMillis()));
        aggregation = new StringBuffer();
        resourceDescriptions = new StringBuffer();
        aggregation.append(HEADER);
        aggregation.append(getResourceMapDescription());
        aggregation.append(getAggregationHeader());
    }

    private String getResourceMapDescription() {
        String rem = baseUrl + "/rem/rdf";
        String desc = "\n<rdf:Description rdf:about=\"" + rem + "\">" + "  <rdf:type rdf:resource=\"http://www.openarchives.org/ore/terms/ResourceMap\"/>" + "  <ore:describes rdf:resource=\"" + baseUrl + "/aggregation\" />" + "  <dcterms:created rdf:datatype=\"http://www.w3.org/2001/XMLSchema#dateTime\">" + creationDate + "</dcterms:created>" + "</rdf:Description>";
        return desc;
    }

    private String getAggregationHeader() {
        String desc = "\n<rdf:Description rdf:about=\"" + baseUrl + "/aggregation\">" + "  <rdf:type rdf:resource=\"http://www.openarchives.org/ore/terms/Aggregation\"/>" + "  <ore:isDescribedBy rdf:resource=\"" + baseUrl + "/rem/rdf\" />" + "  <dcterms:created rdf:datatype=\"http://www.w3.org/2001/XMLSchema#dateTime\">" + creationDate + "</dcterms:created>";
        return desc;
    }

    public void addIdentifier(String id) {
        String url = XmlUtil.encode(baseUrl + "/openurl-aDORe4?url_ver=Z39.88-2004&rft_id=" + id + "&svc_id=info:lanl-repo/svc/getDatastream");
        String aggregates = "\n<ore:aggregates rdf:resource=\"" + url + "\"/>";
        addDescription(url, id);
        aggregation.append(aggregates);
    }

    public void addDescription(String url, String id) {
        String desc = "\n<rdf:Description rdf:about=\"" + url + "\">" + "    <dc:identifier>" + id + "</dc:identifier>" + "</rdf:Description>";
        resourceDescriptions.append(desc);
    }

    public String getAggregation() {
        aggregation.append(FOOTER);
        aggregation.append(resourceDescriptions);
        aggregation.append("\n</rdf:RDF>");
        return aggregation.toString();
    }
}
