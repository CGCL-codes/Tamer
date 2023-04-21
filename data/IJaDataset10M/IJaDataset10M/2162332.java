package edu.psu.its.lionshare.metadata.manager;

import edu.psu.its.lionshare.share.ShareManager;
import edu.psu.its.lionshare.metadata.MetadataManager;
import edu.psu.its.lionshare.metadata.OntologyType;
import edu.psu.its.lionshare.metadata.MetadataExtractor;
import edu.psu.its.lionshare.metadata.MetadataUtility;
import edu.psu.its.lionshare.metadata.MetadataType;
import edu.psu.its.lionshare.search.local.SearchIndex;
import edu.psu.its.lionshare.database.Metadata;
import edu.psu.its.lionshare.database.MetadataSelect;
import edu.psu.its.lionshare.metadata.autogen.CombinedMetadataExtractor;
import com.limegroup.gnutella.util.CommonUtils;
import com.hp.hpl.jena.rdf.model.ResIterator;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.rdf.model.Literal;
import java.io.ByteArrayInputStream;
import org.apache.lucene.document.Field;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Hits;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.FilterIndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.FSDirectory;
import org.hibernate.Session;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Enumeration;
import java.util.ResourceBundle;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NamedNodeMap;

public class DatabaseMetadataManager implements MetadataManager {

    private static final Log LOG = LogFactory.getLog(DatabaseMetadataManager.class);

    private static final String DEFAULT_EXTRACTOR = "anything";

    private Map<String, OntologyType> ontologies = null;

    private Map<String, String> extractors = null;

    public static final String KEYWORDS_FIELD = "Keywords";

    public static final String ID_FIELD = "ID";

    public DatabaseMetadataManager() {
        ontologies = new HashMap<String, OntologyType>();
        extractors = new HashMap<String, String>();
        try {
            byte[] byteArray = DEFAULT_ONTOLOGY.getBytes("UTF-8");
            ByteArrayInputStream bais = new ByteArrayInputStream(byteArray);
            loadOntology(bais);
            File nmd_dir = new File(CommonUtils.getUserSettingsDir(), "nmd");
            File[] files = nmd_dir.listFiles();
            FileInputStream fis = null;
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                fis = new FileInputStream(files[i]);
                if (file.getName().indexOf(".lion.ont.xml") != -1) {
                    loadOntology(fis);
                }
                fis.close();
            }
        } catch (Exception e) {
            LOG.debug("DatabaseMetadataManager(): Exception." + e);
        }
        try {
            ResourceBundle rb = ResourceBundle.getBundle("metadataextractors");
            Enumeration enumKeys = rb.getKeys();
            String sKey = null;
            String sValue = null;
            while (enumKeys.hasMoreElements()) {
                sKey = (String) enumKeys.nextElement();
                sValue = (String) rb.getObject(sKey);
                extractors.put(sKey, sValue);
            }
            extractors.put(DEFAULT_EXTRACTOR, "edu.psu.its.lionshare.metadata.autogen.DefaultMetadataAutogeneration");
        } catch (Exception e) {
            LOG.trace("", e);
        }
    }

    public void initialize(ShareManager manager) {
    }

    private void loadOntology(InputStream is) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(is);
            Element root = doc.getDocumentElement();
            NamedNodeMap map = root.getAttributes();
            String uri = null;
            String name = null;
            String display = null;
            for (int j = 0; j < map.getLength(); j++) {
                Node attr = map.item(j);
                if (attr.getNodeName().equals("uri")) {
                    uri = attr.getNodeValue();
                } else if (attr.getNodeName().equals("name")) {
                    name = attr.getNodeValue();
                } else if (attr.getNodeName().equals("display")) {
                    display = attr.getNodeValue();
                }
            }
            String ontology = MetadataUtility.convertDocumentToString(doc);
            OntologyType type = new OntologyType(OntologyType.ONTOLOGY_TYPE_LIONSHARE, name, uri, ontology, display);
            ontologies.put(uri, type);
        } catch (Exception e) {
        }
    }

    public MetadataType createMetadataType() throws Exception {
        return new DatabaseMetadataType(new Metadata());
    }

    public Object saveMetadata(MetadataType meta) {
        Metadata metadata = new Metadata();
        metadata.setXml((meta.getMetadata()));
        metadata.setSignedxml((meta.getSignedMetadata()));
        metadata.setUri(meta.getOntologyURI());
        Session session = null;
        try {
            session = MetadataSelect.getSession();
            MetadataSelect.insert(metadata, session);
            MetadataSelect.closeSession(session);
        } catch (Exception e) {
            LOG.trace("", e);
        }
        org.apache.lucene.document.Document luc_doc = rdfToLuceneDocument(meta.getMetadata(), metadata.getId());
        SearchIndex.getInstance().addDocument(luc_doc, SearchIndex.SHARED_FILE_INDEX_PATH);
        return metadata.getId();
    }

    public MetadataType getMetadata(Object id) throws Exception {
        LOG.debug("getMetadata(Object id) : The id is: " + id);
        Metadata meta = MetadataSelect.getMetaByID(((Long) id).longValue());
        LOG.debug("getMetadata(Object id) : meta is: " + meta);
        return new DatabaseMetadataType(meta);
    }

    public String getMetadataSchemaURI(Object id) {
        Metadata meta = MetadataSelect.getMetaByID(((Long) id).longValue());
        if (meta != null) {
            return meta.getUri();
        }
        return null;
    }

    public void deleteMetadata(Object id) throws Exception {
        Metadata meta = MetadataSelect.getMetaByID(((Long) id).longValue());
        Session session = null;
        try {
            session = MetadataSelect.getSession();
            MetadataSelect.delete(meta, session);
            MetadataSelect.closeSession(session);
        } catch (Exception e) {
        }
        try {
            String path = SearchIndex.SHARED_FILE_INDEX_PATH;
            boolean create_flag = false;
            if (!(new File(path)).exists()) {
                create_flag = true;
            }
            IndexReader reader = FilterIndexReader.open(FSDirectory.getDirectory(new File(path), create_flag));
            int deleted = reader.delete(new Term(ID_FIELD, id.toString()));
            reader.close();
        } catch (Exception e) {
        }
    }

    public void updateMetadata(MetadataType metadata, Object id) throws Exception {
        Metadata meta = MetadataSelect.getMetaByID(((Long) id).longValue());
        meta.setXml((metadata.getMetadata()));
        meta.setSignedxml((metadata.getSignedMetadata()));
        meta.setUri(metadata.getOntologyURI());
        Session session = null;
        try {
            session = MetadataSelect.getSession();
            MetadataSelect.update(meta, session);
            MetadataSelect.closeSession(session);
        } catch (Exception e) {
        }
        org.apache.lucene.document.Document luc_doc = rdfToLuceneDocument(metadata.getMetadata(), id);
        SearchIndex.getInstance().addDocument(luc_doc, SearchIndex.ALL_FILE_INDEX_PATH);
        SearchIndex.getInstance().addDocument(luc_doc, SearchIndex.SHARED_FILE_INDEX_PATH);
    }

    public boolean isMetadataShared(Object id) {
        return false;
    }

    public Map<String, OntologyType> getAvailableOntologies() {
        return new HashMap<String, OntologyType>(ontologies);
    }

    public OntologyType getOntologyType(String uri) {
        if (ontologies.get(uri) != null) {
            return ontologies.get(uri);
        } else {
            return ontologies.get(DEFAULT_ONTOLOGY_URI);
        }
    }

    public void addOntology(String uri, String ontology, boolean overwrite) throws IOException {
        String filename = uri + ".lion.ont.xml";
        File nmd_dir = new File(CommonUtils.getUserSettingsDir(), "nmd");
        File file = new File(nmd_dir, filename);
        if (file.exists() && !overwrite) {
            throw new IOException("Ontology Exists");
        }
        Document doc = null;
        try {
            doc = MetadataUtility.convertStringToDocument(ontology);
        } catch (Exception e) {
            throw new IOException("Corrupt Ontology");
        }
        if (doc != null) {
            try {
                file.createNewFile();
                Source source = new DOMSource(doc);
                Result result = new StreamResult(new FileOutputStream(file));
                Transformer xformer = TransformerFactory.newInstance().newTransformer();
                xformer.transform(source, result);
            } catch (Exception e) {
                LOG.trace("", e);
                throw new IOException("Could not write xml to file");
            }
            loadOntology(new FileInputStream(file));
        }
    }

    public String removeOntology(String uri) {
        return null;
    }

    public MetadataExtractor getMetadataExtractorForFile(File file) {
        String name = file.getName();
        int index = name.lastIndexOf(".");
        String ext = "";
        if (index + 1 < name.length()) {
            ext = name.substring(index + 1, name.length()).toLowerCase();
        }
        String extractor = extractors.get(ext);
        String default_extractor = extractors.get(DEFAULT_EXTRACTOR);
        ArrayList<MetadataExtractor> extracts = new ArrayList<MetadataExtractor>();
        try {
            if (extractor != null) {
                Class filetype = Class.forName(extractor);
                Object obj = filetype.newInstance();
                if (obj instanceof MetadataExtractor) extracts.add((MetadataExtractor) obj);
            }
            Class defaulttype = Class.forName(default_extractor);
            Object obj = defaulttype.newInstance();
            if (obj instanceof MetadataExtractor) extracts.add((MetadataExtractor) obj);
        } catch (Exception e) {
            LOG.trace("", e);
        }
        return new CombinedMetadataExtractor(extracts);
    }

    public String getDefaultDisplay() {
        return "edu.psu.its.lionshare.gui.nmd.custom.CustomizableMetadataDisplay";
    }

    public static org.apache.lucene.document.Document rdfToLuceneDocument(String rdf_metadata, Object id) {
        org.apache.lucene.document.Document lucene_document = new org.apache.lucene.document.Document();
        try {
            Model model = ModelFactory.createDefaultModel();
            StringReader reader = new StringReader(rdf_metadata);
            model.read(reader, "");
            ResIterator iterator = model.listSubjects();
            String keywords = "";
            while (iterator.hasNext()) {
                Resource resource = iterator.nextResource();
                keywords = keywords + " " + addProperties(resource, lucene_document);
            }
            lucene_document.add(Field.Text(KEYWORDS_FIELD, keywords));
            lucene_document.add(Field.Text(ID_FIELD, id.toString()));
        } catch (Exception e) {
            LOG.trace("Unable to index metadata", e);
        }
        return lucene_document;
    }

    public static String addProperties(Resource resource, org.apache.lucene.document.Document lucene) {
        StmtIterator iter = resource.listProperties();
        String keywords = "";
        while (iter.hasNext()) {
            Statement statement = iter.nextStatement();
            LOG.debug(statement.getSubject());
            LOG.debug(statement.getPredicate());
            if (statement.getObject() instanceof Literal) {
                lucene.add(Field.Text(statement.getPredicate().toString(), ((Literal) statement.getObject()).getString()));
                keywords = keywords + ((Literal) statement.getObject()).getString() + "\n";
                LOG.debug(keywords);
            } else if (statement.getObject() instanceof Resource) {
                keywords = keywords + " " + addProperties(statement.getResource(), lucene);
            }
        }
        return keywords;
    }

    public List<Object> query(Object query, Object query_type) {
        LOG.debug("New code is being queried");
        Query lquery = null;
        IndexSearcher searcher = null;
        Hits hits = null;
        List<Object> results = new ArrayList<Object>();
        try {
            searcher = new IndexSearcher(SearchIndex.SHARED_FILE_INDEX_PATH);
            lquery = QueryParser.parse((String) query, KEYWORDS_FIELD, new StandardAnalyzer());
            hits = searcher.search(lquery);
        } catch (IOException e) {
            return results;
        } catch (Exception e) {
            return results;
        }
        for (int i = 0; i < hits.length(); i++) {
            try {
                org.apache.lucene.document.Document doc = hits.doc(i);
                results.add(new Long(doc.get(ID_FIELD)));
            } catch (Exception e) {
            }
        }
        LOG.debug("We had this many results: " + results.size());
        return results;
    }

    private static final String DEFAULT_ONTOLOGY_URI = "http://lionshare.its.psu.edu/dublin-core-default";

    private static final String DEFAULT_ONTOLOGY = "<?xml version=\"1.0\" encoding=\"UTF-8\"?> " + "<ontology name=\"dublin-core-default\" " + "uri=\"http://purl.org/dc-default/elements/1.1\" " + "display=\"edu.psu.its.lionshare.gui.nmd.custom.CustomizableMetadataDisplay\"> " + "<component schema_uri=\"http://purl.org/dc/elements/1.1\" " + "element=\"title\"/> " + "<component schema_uri=\"http://purl.org/dc/elements/1.1\" " + "element=\"creator\"/> " + "<component schema_uri=\"http://purl.org/dc/elements/1.1\" " + "element=\"subject\"/> " + "<component schema_uri=\"http://purl.org/dc/elements/1.1\" " + "element=\"description\"/> " + "<component schema_uri=\"http://purl.org/dc/elements/1.1\" " + "element=\"publisher\"/> " + "<component schema_uri=\"http://purl.org/dc/elements/1.1\" " + "element=\"contributor\"/> " + "<component schema_uri=\"http://purl.org/dc/elements/1.1\" " + "element=\"date\"/> " + "<component schema_uri=\"http://purl.org/dc/elements/1.1\" " + "element=\"type\"/> " + "<component schema_uri=\"http://purl.org/dc/elements/1.1\" " + "element=\"format\"/> " + "<component schema_uri=\"http://purl.org/dc/elements/1.1\" " + "element=\"identifier\"/> " + "<component schema_uri=\"http://purl.org/dc/elements/1.1\" " + "element=\"source\"/> " + "<component schema_uri=\"http://purl.org/dc/elements/1.1\" " + "element=\"language\"/> " + "<component schema_uri=\"http://purl.org/dc/elements/1.1\" " + "element=\"relation\"/> " + "<component schema_uri=\"http://purl.org/dc/elements/1.1\" " + "element=\"coverage\"/> " + "<component schema_uri=\"http://purl.org/dc/elements/1.1\" " + "element=\"rights\"/> " + "</ontology>";
}
