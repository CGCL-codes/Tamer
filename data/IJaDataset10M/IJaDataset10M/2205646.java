package org.ala.documentmapper;

import java.util.Iterator;
import java.util.List;
import org.ala.repository.ParsedDocument;
import org.ala.repository.Predicates;
import org.ala.repository.Triple;
import org.ala.util.MimeType;
import org.w3c.dom.Document;

/**
 * World Thysanoptera, Australasia (WTA) document mapper
 *
 * @see <a href="http://anic.ento.csiro.au/thrips/index.html">World Thysanoptera</a>
 *
 * @author "Nick dos Remedios <Nick.dosRemedios@csiro.au>"
 */
public class WtaDocumentMapper extends XMLDocumentMapper {

    /**
	 * Initialise the mapper, adding new XPath expressions
	 * for extracting content.
	 */
    public WtaDocumentMapper() {
        this.contentType = MimeType.HTML.toString();
        setRecursiveValueExtraction(true);
        String subject = MappingUtils.getSubject();
        addDCMapping("//html/head/meta[@scheme=\"URL\" and @name=\"ALA.Guid\"]/attribute::content", subject, Predicates.DC_IDENTIFIER);
        addDCMapping("//div[@class=\"content plus-images\"]/h1/em/text()", subject, Predicates.DC_TITLE);
        addTripleMapping("//div[@class=\"content plus-images\"]/h1/em/text()", subject, Predicates.SCIENTIFIC_NAME);
        addTripleMapping("//h3[contains(.,\"distribution\")]/following-sibling::p[1]", subject, Predicates.DISTRIBUTION_TEXT);
        addTripleMapping("//h2[contains(.,\"Recognition data\")]/following-sibling::p[1]", subject, Predicates.MORPHOLOGICAL_TEXT);
        addTripleMapping("//h3[contains(.,\"Host plants\")]/following-sibling::p[1]", subject, Predicates.HABITAT_TEXT);
        addTripleMapping("//h3[contains(.,\"Life history\")]/following-sibling::p[1]" + "|//h3[contains(.,\"Related and similar species\")]/following-sibling::p[1]", subject, Predicates.DESCRIPTIVE_TEXT);
        addTripleMapping("//div[@id=\"imgContainer\"]/ul/li/a/attribute::href", subject, Predicates.IMAGE_URL);
    }

    @Override
    public List<ParsedDocument> map(String uri, byte[] content) throws Exception {
        String documentStr = new String(content);
        documentStr = documentStr.replaceAll("</p>[\\s]*<p>", "");
        content = documentStr.getBytes();
        return super.map(uri, content);
    }

    /**
	 * @see ala.documentmapper.XMLDocumentMapper#extractProperties(org.w3c.dom.Document)
	 */
    @Override
    protected void extractProperties(List<ParsedDocument> pds, Document xmlDocument) throws Exception {
        ParsedDocument pd = pds.get(0);
        List<Triple<String, String, String>> triples = pd.getTriples();
        String source = "http://anic.ento.csiro.au/thrips/identifying_thrips/";
        String subject = MappingUtils.getSubject();
        pd.getDublinCore().put(Predicates.DC_LICENSE.toString(), "http://www.csiro.au/org/LegalNoticeAndDisclaimer.html");
        for (Iterator<Triple<String, String, String>> iter = triples.iterator(); iter.hasNext(); ) {
            Triple<String, String, String> triple = iter.next();
            String predicate = triple.getPredicate().toString();
            if (predicate.equals(Predicates.DC_TITLE.toString())) {
                String titleText = (String) triple.getObject();
                String trimmedTitleText = titleText.split(" \\|")[0].trim();
                triple.setObject(trimmedTitleText);
            } else if (predicate.equals(Predicates.IMAGE_URL.toString())) {
                String imageUrl = (String) triple.getObject();
                String title = getXPathSingleValue(xmlDocument, "//a[@href='" + imageUrl + "']/text()");
                imageUrl = source + imageUrl;
                triple.setObject(imageUrl);
                ParsedDocument imageDoc = MappingUtils.retrieveImageDocument(pd, imageUrl);
                if (imageDoc != null) {
                    imageDoc.getDublinCore().put(Predicates.DC_TITLE.toString(), title);
                    pds.add(imageDoc);
                }
            }
        }
        Triple<String, String, String> newTriple = new Triple<String, String, String>(subject, Predicates.KINGDOM.toString(), "Animalia");
        triples.add(newTriple);
    }
}
