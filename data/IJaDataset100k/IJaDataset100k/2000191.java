package ytex.cmc;

import java.io.File;
import javax.sql.DataSource;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.ParserAdapter;
import ytex.cmc.model.CMCDocument;
import ytex.cmc.model.CMCDocumentCode;
import ytex.kernel.KernelContextHolder;

/**
 * load cmc corpus and labels into corpus_doc and corpus_label tables. combine
 * sections into single string that represents document; use original xml
 * demarcation.
 * 
 * @author vijay
 * 
 */
public class DocumentLoaderImpl {

    public static final String CLINICAL_HISTORY = "<text origin=\"CCHMC_RADIOLOGY\" type=\"CLINICAL_HISTORY\">";

    public static final String IMPRESSION = "\n<text origin=\"CCHMC_RADIOLOGY\" type=\"IMPRESSION\">";

    public static final String TEXT = "</text>";

    private JdbcTemplate jdbcTemplate;

    public DocumentLoaderImpl(DataSource ds) {
        this.jdbcTemplate = new JdbcTemplate(ds);
    }

    public void saveDocument(CMCDocument cDoc) {
        StringBuilder b = new StringBuilder(CLINICAL_HISTORY);
        b.append(cDoc.getClinicalHistory());
        b.append(TEXT);
        b.append(IMPRESSION);
        b.append(cDoc.getImpression());
        b.append(TEXT);
        jdbcTemplate.update("insert into corpus_doc (corpus_name, instance_id, doc_group, doc_text) values ('cmc.2007', ?,?,?)", cDoc.getDocumentId(), cDoc.getDocumentSet(), b.toString());
        for (CMCDocumentCode c : cDoc.getDocumentCodes()) {
            jdbcTemplate.update("insert into corpus_label (corpus_name, instance_id, label, class) values ('cmc.2007',?,?,?)", cDoc.getDocumentId(), c.getCode(), "1");
        }
    }

    public class CMCHandler extends DefaultHandler {

        private String currTag;

        private String cdata;

        private CMCDocument cDoc;

        private String documentSet;

        public CMCHandler(String documentSet) {
            init();
            this.documentSet = documentSet;
        }

        public void init() {
            currTag = "";
            cdata = "";
        }

        public void startElement(String namespace, String localName, String qName, Attributes atts) {
            cdata = "";
            currTag = localName.toLowerCase();
            if (currTag.equals("doc")) {
                cDoc = new CMCDocument();
                cDoc.setDocumentSet(documentSet);
                int id = Integer.parseInt(atts.getValue("id"));
                cDoc.setDocumentId(id);
            } else if (currTag.equals("code")) {
                if (atts.getValue("origin").trim().equalsIgnoreCase("CMC_MAJORITY")) {
                    currTag = "code_target";
                }
            } else if (currTag.equals("text")) {
                String type = atts.getValue("type").trim();
                if (type.equalsIgnoreCase("CLINICAL_HISTORY")) {
                    currTag = "text_clinical";
                } else if (type.equalsIgnoreCase("IMPRESSION")) {
                    currTag = "text_radio";
                } else {
                    System.err.println("Unknown text type: " + type);
                }
            }
        }

        public void characters(char[] ch, int start, int length) {
            cdata += new String(ch, start, length);
        }

        public void endElement(String uri, String localName, String qName) {
            cdata = cdata.trim().toLowerCase();
            if (localName.equalsIgnoreCase("doc")) {
                saveDocument(cDoc);
                currTag = "";
            } else if (currTag.equalsIgnoreCase("text_clinical")) {
                cDoc.setClinicalHistory(cdata);
            } else if (currTag.equalsIgnoreCase("text_radio")) {
                cDoc.setImpression(cdata);
            } else if (currTag.equalsIgnoreCase("code_target")) {
                if (cdata.length() > 0) {
                    CMCDocumentCode code = new CMCDocumentCode();
                    code.setDocument(cDoc);
                    code.setCode(cdata);
                    cDoc.getDocumentCodes().add(code);
                }
            }
        }
    }

    public void process(String urlString, String documentSet) throws Exception {
        System.out.println("Processing URL " + urlString);
        SAXParserFactory spf = SAXParserFactory.newInstance();
        SAXParser sp = spf.newSAXParser();
        ParserAdapter pa = new ParserAdapter(sp.getParser());
        pa.setContentHandler(new CMCHandler(documentSet));
        pa.parse(urlString);
    }

    public static void main(String args[]) throws Exception {
        if (args.length != 1) {
            System.out.println("usage: java " + DocumentLoaderImpl.class.getName() + " [path to cmc data]");
        } else {
            DataSource ds = (DataSource) KernelContextHolder.getApplicationContext().getBean("dataSource");
            DocumentLoaderImpl l = new DocumentLoaderImpl(ds);
            l.process(args[0] + File.separator + "training/2007ChallengeTrainData.xml", "train");
            l.process(args[0] + File.separator + "testing-with-codes/2007ChallengeTestDataCodes.xml", "test");
        }
    }
}
